package model;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private boolean isSpaceKeyPressed;

    private int angle;
    private AnimationTimer gameTimer;

    private GridPane gridPane1;
    private GridPane gridPane2;
    private final static String BACKGROUND_IMAGE = "view/resources/darkPurple.png";

    private final static String BROWN_METEOR = "view/resources/meteorBrown_med3.png";
    private final static String GREY_METEOR = "view/resources/meteorGrey_med1.png";

    private List<ImageView> brownMeteors;
    private List<ImageView> greyMeteors;
    private Random randomPositionGenerator;

    private ImageView star;
    private SmallInfoLabel pointsLabel;
    private List<ImageView> playerLifes;
    private int playerLife;
    private int points;

    private final static int SHIP_RADIUS = 27;
    private final static int STAR_RADIUS = 12;
    private final static int METEOR_RADIUS = 20;
    private final static int BLACK_RADIUS = 27;
    private final static int LASER_PLAYER_RADIUS = 12;

    private final static String GOLD_STAR_IMAGE = "view/resources/star_gold.png";

    private final static String LASER_PLAYER_SHOOT_IMAGE = "view/resources/shipchooser/fire01.png";
    private ImageView laserShoot;


    private final static String BLACK_ENEMIES_IMAGE = "view/resources/enemies/enemyBlack1.png";
    private final static String BLUE_ENEMIES_IMAGE = "view/resources/enemies/enemyBlue2.png";
    private final static String GREEN_ENEMIES_IMAGE = "view/resources/enemies/enemyGreen3.png";
    private final static String RED_ENEMIES_IMAGE = "view/resources/enemies/enemyRed4.png";
    private List<ImageView> blackEnemies;
    private List<ImageView> blueEnemies;
    private List<ImageView> greenEnemies;
    private List<ImageView> redEnemies;


    public GameViewManager() {
        initializeStage();
        createKeyListeners();
        randomPositionGenerator = new Random();
    }

    private void initializeStage() {
        gamePane = new AnchorPane();
        gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
        gameStage = new Stage();
        gameStage.setScene(gameScene);
    }

    private void createKeyListeners() {
        gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.LEFT) {
                    isLeftKeyPressed = true;
                } else if (event.getCode() == KeyCode.RIGHT) {
                    isRightKeyPressed = true;
                } else if (event.getCode() == KeyCode.UP) {
                    isUpKeyPressed = true;
                } else if (event.getCode() == KeyCode.DOWN) {
                    isDownKeyPressed = true;
                } else if (event.getCode() == KeyCode.SPACE) {
                    isSpaceKeyPressed = true;
                }
            }
        });
        gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.LEFT) {
                    isLeftKeyPressed = false;
                } else if (event.getCode() == KeyCode.RIGHT) {
                    isRightKeyPressed = false;
                } else if (event.getCode() == KeyCode.UP) {
                    isUpKeyPressed = false;
                } else if (event.getCode() == KeyCode.DOWN) {
                    isDownKeyPressed = false;
                } else if (event.getCode() == KeyCode.SPACE) {
                    isSpaceKeyPressed = false;
                }
            }
        });
    }

    public void createNewGame(Stage menuStage, SHIP shipChoosen) {
        this.menuStage = menuStage;
        this.menuStage.hide();
        createBackground();
        createShip(shipChoosen);
        createGameElements(shipChoosen);
        createLaserShoot();
        createGameLoop();
        gameStage.show();
    }

    private void createGameElements(SHIP shipChossen) {
        playerLife = 2;
        star = new ImageView(GOLD_STAR_IMAGE);
        setElementsOnPosition(star);
        gamePane.getChildren().add(star);
        pointsLabel = new SmallInfoLabel("Points : 00");
        pointsLabel.setLayoutX(460);
        pointsLabel.setLayoutY(60);
        gamePane.getChildren().add(pointsLabel);
        playerLifes = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            playerLifes.add(new ImageView(shipChossen.getUrlLife()));
            playerLifes.get(i).setLayoutX(410 + (playerLifes.size() * 50));
            playerLifes.get(i).setLayoutY(120);
            gamePane.getChildren().add(playerLifes.get(i));
        }

        brownMeteors = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            brownMeteors.add(new ImageView(BROWN_METEOR));
            setElementsOnPosition(brownMeteors.get(i));
            gamePane.getChildren().add(brownMeteors.get(i));
        }

        greyMeteors = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
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
            green.setLayoutX(0);
            green.setLayoutY(0);
            gamePane.getChildren().add(green);
        }


    }

    private void createLaserShoot() {
        laserShoot = new ImageView(LASER_PLAYER_SHOOT_IMAGE);
        laserShoot.setLayoutY(ship.getLayoutY() - 20);
        laserShoot.setLayoutX(ship.getLayoutX() + 50);
        gamePane.getChildren().add(laserShoot);
    }

    private void moveGameElements() {
        star.setLayoutY(star.getLayoutY() + 5);
        laserShoot.setLayoutY(laserShoot.getLayoutY() - 7);

        for (int i = 0; i < brownMeteors.size(); i++) {
            brownMeteors.get(i).setLayoutY(brownMeteors.get(i).getLayoutY() + 7);
            brownMeteors.get(i).setRotate(brownMeteors.get(i).getRotate() + 4);
        }

        for (int i = 0; i < greyMeteors.size(); i++) {
            greyMeteors.get(i).setLayoutY(greyMeteors.get(i).getLayoutY() + 7);
            greyMeteors.get(i).setRotate(greyMeteors.get(i).getRotate() + 4);
        }

        for (ImageView black : blackEnemies) {
            black.setLayoutY(black.getLayoutY() + 4);
            black.setLayoutX(ship.getLayoutX());
        }

        for (ImageView blue : blueEnemies) {
            blue.setLayoutY(blue.getLayoutY() + 15);
        }

        for (ImageView green : greenEnemies) {

            if (green.getLayoutX() >= 0 && green.getLayoutX()<500) {
                green.setLayoutX(green.getLayoutX() + 5);
                if (green.getLayoutX() == 499 && green.getLayoutX() > 0){
                    green.setLayoutX(green.getLayoutX() - 5);
                }
            }
            green.setLayoutY(green.getLayoutY() + 4);

        }
    }

    private void checkIfElementsAreBehindTheSceneAndRelocate() {

        if (star.getLayoutY() > 1200) {
            setElementsOnPosition(star);
        }

        for (int i = 0; i < brownMeteors.size(); i++) {
            if (brownMeteors.get(i).getLayoutY() > 900) {
                setElementsOnPosition(brownMeteors.get(i));
            }
        }

        for (int i = 0; i < greyMeteors.size(); i++) {
            if (greyMeteors.get(i).getLayoutY() > 900) {
                setElementsOnPosition(greyMeteors.get(i));
            }
        }
        for (ImageView black : blackEnemies) {
            if (black.getLayoutY() > 900) {
                setElementsOnPosition(black);
            }
        }
        for (ImageView blue : blueEnemies) {
            if (blue.getLayoutY() > 900) {
                setElementsOnPosition(blue);
            }
        }
        for (ImageView green : greenEnemies) {
            if (green.getLayoutY() > 900) {
                setElementsOnPosition(green);
            }
        }
    }

    private void setElementsOnPosition(ImageView image) {
        image.setLayoutX(randomPositionGenerator.nextInt(370));
        image.setLayoutY(-(randomPositionGenerator.nextInt(3200) + 600));
    }

    private void createShip(SHIP shipChoosen) {
        ship = new ImageView(shipChoosen.getUrlShip());
        ship.setLayoutX(GAME_WIDTH / 2 - 50);
        ship.setLayoutY(GAME_HEIGHT - 90);
        gamePane.getChildren().add(ship);

    }

    private void createGameLoop() {
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                moveBackground();
                checkIfElementsAreBehindTheSceneAndRelocate();
                moveGameElements();
                checkIfElementsCollide();
                movesShip();
            }
        };
        gameTimer.start();
    }

    private void movesShip() {
        if (isSpaceKeyPressed) {
            createLaserShoot();
        }

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

    private void createBackground() {
        gridPane1 = new GridPane();
        gridPane2 = new GridPane();

        for (int i = 0; i < 12; i++) {
            ImageView backgroundImage1 = new ImageView(BACKGROUND_IMAGE);
            ImageView backgroundImage2 = new ImageView(BACKGROUND_IMAGE);

            //Sets the column,row indeces for the child when contained in a gridpane.

            GridPane.setConstraints(backgroundImage1, i % 3, i / 3);
            GridPane.setConstraints(backgroundImage2, i % 3, i / 3);
            gridPane1.getChildren().add(backgroundImage1);
            gridPane2.getChildren().add(backgroundImage2);
        }
        gridPane2.setLayoutY(-1024);
        gamePane.getChildren().add(gridPane1);
        gamePane.getChildren().add(gridPane2);
    }

    private void moveBackground() {
        gridPane1.setLayoutY(gridPane1.getLayoutY() + 0.5);
        gridPane2.setLayoutY(gridPane2.getLayoutY() + 0.5);

        if (gridPane1.getLayoutY() >= 1024) {
            gridPane1.setLayoutY(-1024);
        }
        if (gridPane2.getLayoutY() >= 1024) {
            gridPane2.setLayoutY(-1024);
        }
    }

    private void checkIfElementsCollide() {
        if (SHIP_RADIUS + STAR_RADIUS > calculateDistance(ship.getLayoutX() + 49, star.getLayoutX() + 15,
                ship.getLayoutY() + 37, star.getLayoutY() + 15)) {
            setElementsOnPosition(star);

            points++;
            String textToSet = "POINTS : ";
            if (points < 10) {
                textToSet = textToSet + "0";
            }
            pointsLabel.setText(textToSet + points);
        }

        for (int i = 0; i < brownMeteors.size(); i++) {
            if (SHIP_RADIUS + METEOR_RADIUS > calculateDistance(ship.getLayoutX() + 49, brownMeteors.get(i).getLayoutX() + 20,
                    ship.getLayoutY() + 37, brownMeteors.get(i).getLayoutY() + 20)) {
                removeLife();
                setElementsOnPosition(brownMeteors.get(i));

            }
        }

        for (int i = 0; i < greyMeteors.size(); i++) {
            if (SHIP_RADIUS + METEOR_RADIUS > calculateDistance(ship.getLayoutX() + 49, greyMeteors.get(i).getLayoutX() + 20,
                    ship.getLayoutY() + 37, greyMeteors.get(i).getLayoutY() + 20)) {
                setElementsOnPosition(greyMeteors.get(i));
                removeLife();
            }
        }

        for (int i = 0; i < brownMeteors.size(); i++) {
            if (METEOR_RADIUS + LASER_PLAYER_RADIUS > calculateDistance(brownMeteors.get(i).getLayoutX() + 20, laserShoot.getLayoutX() + 12,
                    brownMeteors.get(i).getLayoutY() + 20, laserShoot.getLayoutY() + 12)) {
                setElementsOnPosition(brownMeteors.get(i));
                gamePane.getChildren().remove(laserShoot);
            }


            if (METEOR_RADIUS + LASER_PLAYER_RADIUS > calculateDistance(greyMeteors.get(i).getLayoutX() + 20, laserShoot.getLayoutX() + 12,
                    greyMeteors.get(i).getLayoutY() + 20, laserShoot.getLayoutY() + 12)) {
                setElementsOnPosition(greyMeteors.get(i));
                gamePane.getChildren().remove(laserShoot);
            }
        }

        for (int i = 0; i < blackEnemies.size(); i++) {
            if (BLACK_RADIUS + LASER_PLAYER_RADIUS > calculateDistance(blackEnemies.get(i).getLayoutX() + 49, laserShoot.getLayoutX() + 12,
                    blackEnemies.get(i).getLayoutY() + 37, laserShoot.getLayoutY() + 12)) {
                setElementsOnPosition(blackEnemies.get(i));
                gamePane.getChildren().remove(laserShoot);
            }

            if (BLACK_RADIUS + SHIP_RADIUS > calculateDistance(blackEnemies.get(i).getLayoutX() + 49, ship.getLayoutX() + 49,
                    blackEnemies.get(i).getLayoutY() + 37, ship.getLayoutY() + 37)) {
                setElementsOnPosition(blackEnemies.get(i));
                removeLife();
            }
        }
    }

    private void removeLife() {

        gamePane.getChildren().remove(playerLifes.get(playerLife));
        playerLife--;
        if (playerLife < 0) {
            gameStage.close();
            gameTimer.stop();
            menuStage.show();
        }
    }

    private double calculateDistance(double x1, double x2, double y1, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
