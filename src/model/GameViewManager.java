package model;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

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
    private int angle;
    private AnimationTimer gameTimer;

    private GridPane gridPane1;
    private GridPane gridPane2;
    private final static String BACKGROUND_IMAGE = "view/resources/darkPurple.png";

    private final static String BROWN_METEOR = "view/resources/shipchooser/meteorBrown_med3.png";
    private final static String GREY_METEOR = "view/resources/shipchooser/meteorGrey_med1.png";

    private ImageView[] brownMeteors;
    private ImageView[] greyMeteors;

    Random randomPositionGenerator;


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
                }
            }
        });
    }

    public void createNewGame(Stage menuStage, SHIP shipChoosen) {
        this.menuStage = menuStage;
        this.menuStage.hide();
        createBackground();
        createShip(shipChoosen);
        createGameElements();
        createGameLoop();
        gameStage.show();
    }

    private void createGameElements(){
        brownMeteors = new ImageView[3];
        for (int i=0; i < brownMeteors.length; i ++){
            brownMeteors[i] = new ImageView(BROWN_METEOR);
            setElementsOnPosition(brownMeteors[i]);
            gamePane.getChildren().add(brownMeteors[i]);
        }
        greyMeteors = new ImageView[3];
        for (int i=0; i< greyMeteors.length; i++){
            greyMeteors[i] = new ImageView(GREY_METEOR);
            setElementsOnPosition(greyMeteors[i]);
            gamePane.getChildren().add(greyMeteors[i]);
        }
    }

    private void moveGameElements(){

        for (int i=0; i< brownMeteors.length; i++){
            brownMeteors[i].setLayoutY(brownMeteors[i].getLayoutY()+7);
            brownMeteors[i].setRotate(brownMeteors[i].getRotate()+4);
        }

        for (int i=0; i< greyMeteors.length; i++){
            greyMeteors[i].setLayoutY(greyMeteors[i].getLayoutY()+7);
            greyMeteors[i].setRotate(greyMeteors[i].getRotate()+4);
        }
    }

    private void checkIfElementsAreBehindTheSceneAndRelocate(){

        for(int i =0 ; i<brownMeteors.length;i++){
            if(brownMeteors[i].getLayoutY()>900){
                setElementsOnPosition(brownMeteors[i]);
            }
        }

        for(int i =0 ; i<greyMeteors.length;i++){
            if(greyMeteors[i].getLayoutY()>900){
                setElementsOnPosition(greyMeteors[i]);
            }
        }
    }

    private void setElementsOnPosition(ImageView image){
        image.setLayoutX(randomPositionGenerator.nextInt(370));
        image.setLayoutY(-(randomPositionGenerator.nextInt(3200)+600));
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
}
