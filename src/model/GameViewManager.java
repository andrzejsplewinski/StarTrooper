package model;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.*;
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

    private Stage menuStage;
    private ImageView ship;

    private boolean isLeftKeyPressed;
    private boolean isRightKeyPressed;
    private boolean isUpKeyPressed;
    private boolean isDownKeyPressed;

    private int angle;
    private AnimationTimer gameTimer;

    private final static String BROWN_METEOR = "view/resources/meteorBrown_med3.png";
    private final static String GREY_METEOR = "view/resources/meteorGrey_med1.png";

    private List<ImageView> brownMeteor;
    private List<ImageView> greyMeteors;
    private Random randomPositionGenerator;

    private ImageView star;
    private SmallInfoLabel pointsLabel;
    private List<ImageView> playerLifes;
    private int playerLife;
    private int points;

    private final static int SHIP_RADIUS = 30;
    private final static int STAR_RADIUS = 12;
    private final static int ENEMY_RADIUS = 27;
    private final static int METEOR_RADIUS = 20;
    private final static int LASER_RADIUS = 12;

    private final static String GOLD_STAR_IMAGE = "view/resources/star_gold.png";

    private final static String LASER_PLAYER_SHOOT_IMAGE = "view/resources/shipchooser/fire01.png";

    private List<Node> laser = new ArrayList<>();

    private Duration firingInterval = Duration.millis(1000);
    private Timeline firing = new Timeline(
            new KeyFrame(Duration.ZERO, event -> fire()),
            new KeyFrame(firingInterval));

    private final static String BLACK_ENEMIES_IMAGE = "view/resources/enemies/enemyBlack1.png";
    private final static String BLUE_ENEMIES_IMAGE = "view/resources/enemies/enemyBlue2.png";
    private final static String GREEN_ENEMIES_IMAGE = "view/resources/enemies/enemyGreen3.png";
    private final static String RED_ENEMIES_IMAGE = "view/resources/enemies/enemyRed4.png";

    private List<ImageView> blackEnemies;
    private List<ImageView> blueEnemies;
    private List<ImageView> greenEnemies;
    private List<ImageView> redEnemies;

    private GameBackground gameBackground;
    private ScoreManager scoreManager;

    public GameViewManager() {
        initializeStage();
        createKeyListeners();
        scoreManager = new ScoreManager();
        randomPositionGenerator = new Random();
    }

    private void getScoreGame() {
        String name = JOptionPane.showInputDialog("Please enter your name:");
        scoreManager.saveScore(Score.of(name, points));
    }

    private void initializeStage() {
        gamePane = new AnchorPane();
        gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
        gameStage = new Stage();
        gameStage.setScene(gameScene);
    }

    private void createKeyListeners() {
        gameScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                isLeftKeyPressed = true;
            } else if (event.getCode() == KeyCode.RIGHT) {
                isRightKeyPressed = true;
            } else if (event.getCode() == KeyCode.UP) {
                isUpKeyPressed = true;
            } else if (event.getCode() == KeyCode.DOWN) {
                isDownKeyPressed = true;
            } else if (event.getCode() == KeyCode.SPACE && firing.getStatus() != Animation.Status.RUNNING) {
                firing.playFromStart();
            }
        });
        gameScene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                isLeftKeyPressed = false;
            } else if (event.getCode() == KeyCode.RIGHT) {
                isRightKeyPressed = false;
            } else if (event.getCode() == KeyCode.UP) {
                isUpKeyPressed = false;
            } else if (event.getCode() == KeyCode.DOWN) {
                isDownKeyPressed = false;
            } else if (event.getCode() == KeyCode.SPACE) {
                firing.stop();
            }
        });
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
            initializeTransitionGreenEnemies();
        }

        redEnemies = new ArrayList<>();
        redEnemies.add(new ImageView(RED_ENEMIES_IMAGE));
        for (ImageView red : redEnemies) {
            gamePane.getChildren().add(red);
            initializeTransitionRedEnemies();
        }
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
        specialElementBehavior();
    }

    private void laserMoves() {
        for (int i = 0; i < laser.size(); i++) {
            if (laser.get(i).getLayoutY() < GAME_HEIGHT) {
                laser.get(i).setLayoutY(laser.get(i).getLayoutY() - 7);
            } else laser.remove(i);
        }
    }

    private void specialElementBehavior() {
        for (ImageView black : blackEnemies) {
            black.setLayoutX(ship.getLayoutX());
        }
        for (ImageView blue : blueEnemies) {
            if (blue.getLayoutY() > 500) {
                if (blue.getLayoutX() > ship.getLayoutX()) {
                    blue.setLayoutX(blue.getLayoutX() - 5);
                } else {
                    blue.setLayoutX(blue.getLayoutX() + 5);
                }
            }
        }
    }

    private void moveElementFromUpToDown(List<ImageView> element, int speed, int route) {
        for (ImageView elements : element) {
            elements.setLayoutY(elements.getLayoutY() + speed);
            elements.setRotate(elements.getRotate() + route);
        }
    }

    private void checkIfElementsAreBehindTheSceneAndRelocate() {
        if (star.getLayoutY() > 1200) {
            setElementsOnPosition(star);
        }
        setEnemyPosition(brownMeteor, randomPositionGenerator.nextInt(370));
        setEnemyPosition(greyMeteors, randomPositionGenerator.nextInt(370));
        setEnemyPosition(blackEnemies, randomPositionGenerator.nextInt(370));
        setEnemyPosition(blueEnemies, randomPositionGenerator.nextInt(370));
        setEnemyPosition(greenEnemies, randomPositionGenerator.nextInt(370));
        setEnemyPosition(redEnemies, 0);
    }

    private void setEnemyPosition(List<ImageView> elements, double position) {
        for (ImageView element : elements) {
            if (element.getLayoutY() > 900) {
                element.relocate(position, -300);
            }
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

    private void createGameLoop() {
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameBackground.moveBackground();
                checkIfElementsAreBehindTheSceneAndRelocate();
                moveGameElements();
                collisionHandling();
                movesShip();
            }
        };
        gameTimer.start();
    }

    private void movesShip() {

        if (isLeftKeyPressed && !isRightKeyPressed) {
            if (angle > -30) {
                angle -= 5;
            }
            ship.setRotate(angle);
            if (ship.getLayoutX() > -10) {
                ship.setLayoutX(ship.getLayoutX() - 5);
            }
        }

        if (isRightKeyPressed && !isLeftKeyPressed) {
            if (angle < 30) {
                angle += 5;
            }
            ship.setRotate(angle);
            if (ship.getLayoutX() < 495) {
                ship.setLayoutX(ship.getLayoutX() + 5);
            }
        }
        if (!isLeftKeyPressed && !isRightKeyPressed) {
            if (angle < 0) {
                angle += 5;
            } else if (angle > 0) {
                angle -= 5;
            }
            ship.setRotate(angle);
        }
        if (isLeftKeyPressed && isRightKeyPressed) {
            if (angle < 0) {
                angle += 5;
            } else if (angle > 0) {
                angle -= 5;
            }
            ship.setRotate(angle);
        }
        if (isUpKeyPressed && !isDownKeyPressed) {
            if (ship.getLayoutY() > 10) {
                ship.setLayoutY(ship.getLayoutY() - 5);
            }
        }
        if (isDownKeyPressed && !isUpKeyPressed) {
            if (ship.getLayoutY() < 700) {
                ship.setLayoutY(ship.getLayoutY() + 5);
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

    private void collisionHandling() {
        shipCollideWithStar();
        shipCollideWithMeteor(brownMeteor);
        shipCollideWithMeteor(greyMeteors);
        laserCollideWithMeteor(brownMeteor);
        laserCollideWithMeteor(greyMeteors);
        enemyCollideWithShip(blackEnemies, 49, 37);
        enemyCollideWithShip(blueEnemies, 52, 40);
        laserCollideWithEnemy(greenEnemies, Math.random());
        laserCollideWithEnemy(blackEnemies, Math.random());
        laserCollideWithEnemy(blueEnemies, Math.random());
        laserCollideWithEnemy(redEnemies, 0);
        greenAndRedEnemyCollideWithShip(greenEnemies, Math.random());
        greenAndRedEnemyCollideWithShip(redEnemies, 0);
    }

    private void shipCollideWithStar() {
        if (SHIP_RADIUS + STAR_RADIUS > calculateDistance(ship.getLayoutX() + 49, star.getLayoutX() + 15,
                ship.getLayoutY() + 37, star.getLayoutY() + 15)) {
            setElementsOnPosition(star);
            addPoints(3);
        }
    }

    private void greenAndRedEnemyCollideWithShip(List<ImageView> enemy, double position) {
        for (ImageView enemies : enemy) {
            if (enemies.getBoundsInParent().intersects(ship.getBoundsInParent())) {
                enemies.relocate(Math.random(), -300);
                removeLife();
            }
        }
    }

    private void shipCollideWithMeteor(List<ImageView> meteor) {
        for (ImageView meteors : meteor) {
            if (SHIP_RADIUS + METEOR_RADIUS > calculateDistance(ship.getLayoutX() + 49, meteors.getLayoutX() + 20,
                    ship.getLayoutY() + 37, meteors.getLayoutY() + 20)) {
                removeLife();
                setElementsOnPosition(meteors);
            }
        }
    }


    private void enemyCollideWithShip(List<ImageView> enemies, int i2, int i3) {
        for (ImageView enemy : enemies) {
            if (ENEMY_RADIUS + SHIP_RADIUS > calculateDistance(enemy.getLayoutX() + i2, ship.getLayoutX() + 49,
                    enemy.getLayoutY() + i3, ship.getLayoutY() + 37)) {
                setElementsOnPosition(enemy);
                removeLife();
            }
        }
    }
    private void laserCollideWithEnemy(List<ImageView> enemy, double i2) {
        for (int i = 0; i < laser.size(); i++) {
            for (ImageView enemies : enemy) {
                if (laser.get(i).getBoundsInParent().intersects(enemies.getBoundsInParent())) {
                    addPoints(2);
                    enemies.relocate(i2, -300);
                    gamePane.getChildren().remove(laser.get(i));
                    laser.remove(i);
                }
            }
        }
    }
    private void laserCollideWithMeteor(List<ImageView> meteor) {
        for (ImageView meteors : meteor) {
            for (int i = 0; i < laser.size(); i++) {
                if (METEOR_RADIUS + LASER_RADIUS > calculateDistance(meteors.getLayoutX() + 20, laser.get(i).getLayoutX() + 12,
                        meteors.getLayoutY() + 20, laser.get(i).getLayoutY() + 12)) {
                    addPoints(1);
                    setElementsOnPosition(meteors);
                    gamePane.getChildren().remove(laser.get(i));
                    laser.remove(i);
                }
            }
        }
    }

    private double calculateDistance(double x1, double x2, double y1, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public void removeLife() {
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

    private void initializeTransitionGreenEnemies() {
        for (ImageView green : greenEnemies) {
            Path path = new Path();
            path.getElements().add(new MoveTo(250, -100));
            ArcTo arcTo = new ArcTo();
            arcTo.setX(150);
            arcTo.setY(150);
            arcTo.setRadiusX(50);
            arcTo.setRadiusY(50);
            path.getElements().add(arcTo);
            path.getElements().add(new ClosePath());
            PathTransition transition = new PathTransition();
            transition.setNode(green);
            transition.setDuration(Duration.seconds(2));
            transition.setPath(path);
            transition.setCycleCount(PathTransition.INDEFINITE);
            transition.play();
        }
    }

    private void initializeTransitionRedEnemies() {
        for (ImageView red : redEnemies) {
            Path path = new Path();
            MoveTo moveTo = new MoveTo();
            moveTo.setX(30);
            moveTo.setY(50);
            LineTo line = new LineTo();
            line.setX(570);
            line.setY(50);
            LineTo lineBack = new LineTo(30, 50);
            path.getElements().add(moveTo);
            path.getElements().add(line);
            path.getElements().add(lineBack);
            PathTransition transition = new PathTransition();
            transition.setNode(red);
            transition.setDuration(Duration.seconds(4));
            transition.setPath(path);
            transition.setCycleCount(PathTransition.INDEFINITE);
            transition.play();
        }
    }
}
