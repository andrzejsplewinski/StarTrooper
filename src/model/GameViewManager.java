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
    private GameBackground gameBackground;
    private ScoreManager scoreManager;
    private ElementBehavior elementBehavior;
    private GameListeners gameListeners;

    public GameViewManager() {
        initializeStage();
        randomPositionGenerator = new Random();
        elementBehavior = new ElementBehavior();
        gameListeners = new GameListeners();
        Duration firingInterval = Duration.millis(1000);
        Timeline firing = new Timeline(
                new KeyFrame(Duration.ZERO, event -> fire()),
                new KeyFrame(firingInterval));
        gameListeners.createKeyListeners(gameScene, firing);
        scoreManager = new ScoreManager();
    }

    private void initializeStage() {
        gamePane = new AnchorPane();
        gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
        gameStage = new Stage();
        gameStage.setScene(gameScene);
    }

    public void createNewGame(Stage menuStage, SHIP shipChosen) {
        this.menuStage = menuStage;
        this.menuStage.hide();
        gameBackground = new GameBackground();
        gameBackground.createBackground(gamePane);
        createShip(shipChosen);
        createGameElements(shipChosen);
        createGameLoop();
        gameStage.show();
    }

    private void createGameElements(SHIP shipChosen) {
        playerLife = 2;
        star = new ImageView(GOLD_STAR_IMAGE);
        setElementsOnPosition(star);
        gamePane.getChildren().add(star);
        pointsLabel = new SmallInfoLabel("Points : 00");
        pointsLabel.setLayoutX(420);
        pointsLabel.setLayoutY(60);
        gamePane.getChildren().add(pointsLabel);
        playerLifes = new ArrayList<>();
        brownMeteor = new ArrayList<>();
        greyMeteors = new ArrayList<>();
        blackEnemies = new ArrayList<>();
        blueEnemies = new ArrayList<>();
        greenEnemies = new ArrayList<>();
        redEnemies = new ArrayList<>();

        createPlayerLifes(shipChosen);
        createMeteor(3, brownMeteor, BROWN_METEOR);
        createMeteor(4, greyMeteors, GREY_METEOR);
        createBlackAndBlueEnemies(blackEnemies, BLACK_ENEMIES_IMAGE);
        createBlackAndBlueEnemies(blueEnemies, BLUE_ENEMIES_IMAGE);
        createGreenEnemies();
        createRedEnemies();
    }

    private void createPlayerLifes(SHIP shipChosen) {
        for (int i = 0; i < 3; i++) {
            playerLifes.add(new ImageView(shipChosen.getLifePath()));
            playerLifes.get(i).setLayoutX(380 + (playerLifes.size() * 50));
            playerLifes.get(i).setLayoutY(120);
            gamePane.getChildren().add(playerLifes.get(i));
        }
    }

    private void createMeteor(int numberOfMeteors, List<ImageView> meteors, String meteor) {
        for (int i = 0; i < numberOfMeteors; i++) {
            meteors.add(new ImageView(meteor));
            setElementsOnPosition(meteors.get(i));
            gamePane.getChildren().add(meteors.get(i));
        }
    }

    private void createGreenEnemies() {
        greenEnemies.add(new ImageView(GREEN_ENEMIES_IMAGE));
        for (ImageView green : greenEnemies) {
            gamePane.getChildren().add(green);
            elementBehavior.initializeTransitionGreenEnemies(greenEnemies);
        }
    }

    private void createRedEnemies() {
        redEnemies.add(new ImageView(RED_ENEMIES_IMAGE));
        for (ImageView red : redEnemies) {
            gamePane.getChildren().add(red);
            elementBehavior.initializeTransitionRedEnemies(redEnemies);
        }
    }

    private void createBlackAndBlueEnemies(List<ImageView> enemies, String enemy) {
        enemies.add(new ImageView(enemy));
        for (ImageView element : enemies) {
            setElementsOnPosition(element);
            gamePane.getChildren().add(element);
        }
    }

    private void getScoreGame() {
        String name = JOptionPane.showInputDialog("Please enter your name:");
        scoreManager.saveScore(Score.of(name, points));
    }

    private void moveGameElements() {
        star.setLayoutY(star.getLayoutY() + 5);
        elementBehavior.laserMoves(laser);
        elementBehavior.moveElementFromUpToDown(brownMeteor, 7, 4);
        elementBehavior.moveElementFromUpToDown(greyMeteors, 9, 6);
        elementBehavior.moveElementFromUpToDown(greenEnemies, 1, 0);
        elementBehavior.moveElementFromUpToDown(redEnemies, 2, 0);
        elementBehavior.moveElementFromUpToDown(blackEnemies, 4, 0);
        elementBehavior.moveElementFromUpToDown(blueEnemies, 15, 0);
        elementBehavior.specialElementBehavior(blackEnemies, ship);
        elementBehavior.specialElementBehavior(blueEnemies, ship);
    }

    private void setElementsOnPosition(ImageView image) {
        double relocatePosition = randomPositionGenerator.nextInt(370);
        image.setLayoutX(relocatePosition);
        image.setLayoutY(-(randomPositionGenerator.nextInt(3200) + 600));
    }

    private void createShip(SHIP shipChosen) {
        ship = new ImageView(shipChosen.getShipPath());
        ship.setLayoutX(GAME_WIDTH / 2 - 50);
        ship.setLayoutY(GAME_HEIGHT - 90);
        gamePane.getChildren().add(ship);
    }

    private void checkIfElementsAreBehindTheSceneAndRelocate() {
        double relocatePosition = randomPositionGenerator.nextInt(370);
        elementBehavior.relocateElementsPosition(greenEnemies, relocatePosition);
        elementBehavior.relocateElementsPosition(blackEnemies, relocatePosition);
        elementBehavior.relocateElementsPosition(blueEnemies, relocatePosition);
        elementBehavior.relocateElementsPosition(redEnemies, 0);
        elementBehavior.relocateElementsPosition(brownMeteor, relocatePosition);
        elementBehavior.relocateElementsPosition(greyMeteors, relocatePosition);
        elementBehavior.relocateStarPosition(star);
    }

    private void createGameLoop() {
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameBackground.moveBackground();
                checkIfElementsAreBehindTheSceneAndRelocate();
                moveGameElements();
                collisionsHandling();
                gameListeners.movesShip(ship);
            }
        };
        gameTimer.start();
    }

    private void collisionsHandling() {
        shipCollideWithStar(ship, star);
        double relocatePosition = randomPositionGenerator.nextInt(370);
        laserCollideWithElement(laser, brownMeteor, relocatePosition);
        laserCollideWithElement(laser, greyMeteors, relocatePosition);
        laserCollideWithElement(laser, greenEnemies, relocatePosition);
        laserCollideWithElement(laser, blackEnemies, relocatePosition);
        laserCollideWithElement(laser, blueEnemies, relocatePosition);
        laserCollideWithElement(laser, redEnemies, 0);
        elementCollideWithShip(ship, brownMeteor, relocatePosition);
        elementCollideWithShip(ship, greyMeteors, relocatePosition);
        elementCollideWithShip(ship, blackEnemies, relocatePosition);
        elementCollideWithShip(ship, blueEnemies, relocatePosition);
        elementCollideWithShip(ship, greenEnemies, relocatePosition);
        elementCollideWithShip(ship, redEnemies, 0);
    }

    private void shipCollideWithStar(ImageView ship, ImageView star) {
        Random randomPositionGenerator = new Random();
        if (star.getBoundsInParent().intersects(ship.getBoundsInParent())) {
            star.relocate(randomPositionGenerator.nextInt(370), -(randomPositionGenerator.nextInt(3200) + 600));
            addPoints(3);
        }
    }

    private void elementCollideWithShip(ImageView ship, List<ImageView> element, double layoutX) {
        for (ImageView elements : element) {
            if (elements.getBoundsInParent().intersects(ship.getBoundsInParent())) {
                elements.relocate(layoutX, -300);
                removeLife();
            }
        }
    }

    private void laserCollideWithElement(List<Node> laser, List<ImageView> element, double layoutX) {
        for (ImageView elements : element) {
            for (int i = 0; i < laser.size(); i++) {
                if (laser.get(i).getBoundsInParent().intersects(elements.getBoundsInParent())) {
                    elements.relocate(layoutX, -300);
                    gamePane.getChildren().remove(laser.get(i));
                    laser.remove(i);
                    addPoints(2);
                }
            }
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
