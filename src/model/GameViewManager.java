package model;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class GameViewManager {

    private AnchorPane gamePane;
    private Scene gameScene;
    private Stage gameStage;

    private final static int GAME_WIDTH = 600;
    private final static int GAME_HEIGHT = 800;

    private final static String BLACK_ENEMIES_IMAGE = "view/resources/enemies/enemyBlack1.png";
    private final static String BLUE_ENEMIES_IMAGE = "view/resources/enemies/enemyBlue2.png";
    private final static String GREEN_ENEMIES_IMAGE = "view/resources/enemies/enemyGreen3.png";
    private final static String RED_ENEMIES_IMAGE = "view/resources/enemies/enemyRed4.png";
    private final static String BROWN_METEOR = "view/resources/meteorBrown_med3.png";
    private final static String GREY_METEOR = "view/resources/meteorGrey_med1.png";
    private final static String GOLD_STAR_IMAGE = "view/resources/star_gold.png";
    private final static String LASER_PLAYER_SHOOT_IMAGE = "view/resources/shipchooser/fire01.png";

    private Stage menuStage;
    private ImageView ship;

    private AnimationTimer gameTimer;

    private List<ImageView> brownMeteor;
    private List<ImageView> greyMeteors;
    private List<ImageView> blackEnemies;
    private List<ImageView> blueEnemies;
    private List<ImageView> greenEnemies;
    private List<ImageView> redEnemies;

    private Random randomPositionGenerator;

    private ImageView star;
    private SmallInfoLabel pointsLabel;
    private List<ImageView> playerLifes;
    private int playerLife;
    private int points;

    private List<Node> laser = new ArrayList<>();

    private Duration firingInterval = Duration.millis(1000);
    private Timeline firing = new Timeline(
            new KeyFrame(Duration.ZERO, event -> fire()),
            new KeyFrame(firingInterval));

    private GameBackground gameBackground;
    private ScoreManager scoreManager;
    ElementsBehavior elementsBehavior;

    public GameViewManager() {
        initializeStage();
        randomPositionGenerator = new Random();
        elementsBehavior = new ElementsBehavior();
        elementsBehavior.createKeyListeners(gameScene, firing);
        scoreManager = new ScoreManager();

    }

    private void initializeStage() {
        gamePane = new AnchorPane();
        gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
        gameStage = new Stage();
        gameStage.setScene(gameScene);
    }

    public void createNewGame(Stage menuStage, SHIP shipChoosen) {
        this.menuStage = menuStage;
        this.menuStage.hide();
        gameBackground = new GameBackground();
        gameBackground.createBackground(gamePane);
        createShip(shipChoosen);
        createGameElements(shipChoosen);
        createGameLoop();
        gameStage.show();
    }

    private void createGameElements(SHIP shipChossen) {
        playerLife = 2;
        star = new ImageView(GOLD_STAR_IMAGE);
        setElementsOnPosition(star);
        gamePane.getChildren().add(star);
        pointsLabel = new SmallInfoLabel("Points : 00");
        pointsLabel.setLayoutX(420);
        pointsLabel.setLayoutY(60);
        gamePane.getChildren().add(pointsLabel);
        playerLifes = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            playerLifes.add(new ImageView(shipChossen.getLifePath()));
            playerLifes.get(i).setLayoutX(380 + (playerLifes.size() * 50));
            playerLifes.get(i).setLayoutY(120);
            gamePane.getChildren().add(playerLifes.get(i));
        }

        brownMeteor = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            brownMeteor.add(new ImageView(BROWN_METEOR));
            setElementsOnPosition(brownMeteor.get(i));
            gamePane.getChildren().add(brownMeteor.get(i));
        }

        greyMeteors = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            greyMeteors.add(new ImageView(GREY_METEOR));
            setElementsOnPosition(greyMeteors.get(i));
            gamePane.getChildren().add(greyMeteors.get(i));
        }

        blackEnemies = new ArrayList<>();
        blackEnemies.add(new ImageView(BLACK_ENEMIES_IMAGE));
        for (ImageView black : blackEnemies) {
            setElementsOnPosition(black);
            gamePane.getChildren().add(black);
        }
        blueEnemies = new ArrayList<>();
        blueEnemies.add(new ImageView(BLUE_ENEMIES_IMAGE));
        for (ImageView blue : blueEnemies) {
            setElementsOnPosition(blue);
            gamePane.getChildren().add(blue);
        }
        greenEnemies = new ArrayList<>();
        greenEnemies.add(new ImageView(GREEN_ENEMIES_IMAGE));
        for (ImageView green : greenEnemies) {
            gamePane.getChildren().add(green);
            elementsBehavior.initializeTransitionGreenEnemies(greenEnemies);
        }

        redEnemies = new ArrayList<>();
        redEnemies.add(new ImageView(RED_ENEMIES_IMAGE));
        for (ImageView red : redEnemies) {
            gamePane.getChildren().add(red);
            elementsBehavior.initializeTransitionRedEnemies(redEnemies);
        }
    }

    private void getScoreGame() {
        String name = JOptionPane.showInputDialog("Please enter your name:");
        scoreManager.saveScore(Score.of(name, points));
    }

    private void moveGameElements() {
        star.setLayoutY(star.getLayoutY() + 5);
        laserMoves();
        moveElementFromUpToDown(brownMeteor, 7, 4);
        moveElementFromUpToDown(greyMeteors, 9, 6);
        moveElementFromUpToDown(greenEnemies, 1, 0);
        moveElementFromUpToDown(redEnemies, 2, 0);
        moveElementFromUpToDown(blackEnemies, 4, 0);
        moveElementFromUpToDown(blueEnemies, 15, 0);
        elementsBehavior.specialElementBehavior(blackEnemies, ship);
        elementsBehavior.specialElementBehavior(blueEnemies, ship);
    }

    private void laserMoves() {
        for (int i = 0; i < laser.size(); i++) {
            if (laser.get(i).getLayoutY() < GAME_HEIGHT) {
                laser.get(i).setLayoutY(laser.get(i).getLayoutY() - 7);
            } else laser.remove(i);
        }
    }

    private void moveElementFromUpToDown(List<ImageView> element, int speed, int route) {
        for (ImageView elements : element) {
            elements.setLayoutY(elements.getLayoutY() + speed);
            elements.setRotate(elements.getRotate() + route);
        }
    }

    private void setElementsOnPosition(ImageView image) {
        image.setLayoutX(randomPositionGenerator.nextInt(370));
        image.setLayoutY(-(randomPositionGenerator.nextInt(3200) + 600));
    }

    private void createShip(SHIP shipChoosen) {
        ship = new ImageView(shipChoosen.getShipPath());
        ship.setLayoutX(GAME_WIDTH / 2 - 50);
        ship.setLayoutY(GAME_HEIGHT - 90);
        gamePane.getChildren().add(ship);
    }

    private void checkIfElementsAreBehindTheSceneAndRelocate() {
        elementsBehavior.relocateElementsPosition(greenEnemies, randomPositionGenerator.nextInt(370));
        elementsBehavior.relocateElementsPosition(blackEnemies, randomPositionGenerator.nextInt(370));
        elementsBehavior.relocateElementsPosition(blueEnemies, randomPositionGenerator.nextInt(370));
        elementsBehavior.relocateElementsPosition(redEnemies, 0);
        elementsBehavior.relocateElementsPosition(brownMeteor, randomPositionGenerator.nextInt(370));
        elementsBehavior.relocateElementsPosition(greyMeteors, randomPositionGenerator.nextInt(370));
        elementsBehavior.relocateStarPosition(star);
    }

    private void createGameLoop() {
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameBackground.moveBackground();
                checkIfElementsAreBehindTheSceneAndRelocate();
                moveGameElements();
                try {
                    collisionsHandling();
                } catch (CollideExceptions collideExceptions) {
                    collideExceptions.printStackTrace();
                }
                elementsBehavior.movesShip(ship);
            }
        };
        gameTimer.start();
    }

    private void collisionsHandling() throws CollideExceptions {
        shipCollideWithStar(ship, star);

        laserCollideWithElement(laser, brownMeteor, randomPositionGenerator.nextInt(370));
        laserCollideWithElement(laser, greyMeteors, randomPositionGenerator.nextInt(370));
        laserCollideWithElement(laser, greenEnemies, randomPositionGenerator.nextInt(370));
        laserCollideWithElement(laser, blackEnemies, randomPositionGenerator.nextInt(370));
        laserCollideWithElement(laser, blueEnemies, randomPositionGenerator.nextInt(370));
        laserCollideWithElement(laser, redEnemies, 0);

        elementCollideWithShip(ship, brownMeteor, randomPositionGenerator.nextInt(370));
        elementCollideWithShip(ship, greyMeteors, randomPositionGenerator.nextInt(370));

        elementCollideWithShip(ship, blackEnemies, randomPositionGenerator.nextInt(370));
        elementCollideWithShip(ship, blueEnemies, randomPositionGenerator.nextInt(370));
        elementCollideWithShip(ship, greenEnemies, randomPositionGenerator.nextInt(370));
        elementCollideWithShip(ship, redEnemies, 0);
    }

    public void shipCollideWithStar(ImageView ship, ImageView star) {
        Random randomPositionGenerator = new Random();
        if (star.getBoundsInParent().intersects(ship.getBoundsInParent())) {
            star.relocate(randomPositionGenerator.nextInt(370), -(randomPositionGenerator.nextInt(3200) + 600));
            addPoints(3);
        }
    }

    public void elementCollideWithShip(ImageView ship, List<ImageView> element, double layoutX) {
        for (ImageView elements : element) {
            if (elements.getBoundsInParent().intersects(ship.getBoundsInParent())) {
                elements.relocate(layoutX, -300);
                removeLife();
            }
        }
    }

    public void laserCollideWithElement(List<Node> laser, List<ImageView> element, double layoutX) throws CollideExceptions {
        try {
            for (int i = 0; i < laser.size(); i++) {
                for (ImageView elements : element) {
                    if (laser.get(i).getBoundsInParent().intersects(elements.getBoundsInParent())) {
                        elements.relocate(layoutX, -300);
                        gamePane.getChildren().remove(laser.get(i));
                        laser.remove(i);
                        addPoints(2);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addPoints(int point) {
        points += point;
        List<Integer> pointsList = new ArrayList<>();
        pointsList.add(points);
        for (int i : pointsList) {
            int sum = IntStream.of(i).sum();
            String textToSet = "POINTS : ";
            if (sum < 10) {
                textToSet = textToSet + "0";
            }
            pointsLabel.setText(textToSet + sum);
        }
    }

    private void removeLife() {
        gamePane.getChildren().remove(playerLifes.get(playerLife));
        playerLife--;
        if (playerLife < 0) {
            getScoreGame();
            gameStage.close();
            gameTimer.stop();
            menuStage.show();
        }
    }

    private void fire() {
        Node newLaser = new ImageView(LASER_PLAYER_SHOOT_IMAGE);
        newLaser.relocate(ship.getLayoutX() + 48, ship.getLayoutY() - 20);
        laser.add(newLaser);
        gamePane.getChildren().add(newLaser);
    }
}
