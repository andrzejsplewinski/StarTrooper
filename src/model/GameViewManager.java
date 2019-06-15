package model;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
    private int angle;
    private AnimationTimer gameTimer;





    public GameViewManager() {
        initializeStage();
        createKeyListeners();
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
                }
            }
        });
    }

    public void createNewGame(Stage menuStage, SHIP shipChoosen){
        this.menuStage = menuStage;
        this.menuStage.hide();
        createShip(shipChoosen);
        createGameLoop();
        gameStage.show();
    }
    private void createShip(SHIP shipChoosen){
        ship = new ImageView(shipChoosen.getUrlShip());
        ship.setLayoutX(GAME_WIDTH/2 - 50);
        ship.setLayoutY(GAME_HEIGHT - 90);
        gamePane.getChildren().add(ship);

    }

    private void createGameLoop(){
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                movesShip();
            }
        };
        gameTimer.start();
    }

    private void movesShip(){
        if(isLeftKeyPressed && !isRightKeyPressed){
            if(angle > -30){
                angle -= 5;
            }
            ship.setRotate(angle);
            if(ship.getLayoutX() > -10){
                ship.setLayoutX(ship.getLayoutX() - 3);
            }
        }
        if (isRightKeyPressed && !isLeftKeyPressed){
            if(angle < 30){
                angle += 5;
            }
            ship.setRotate(angle);
            if(ship.getLayoutX() < 498){
                ship.setLayoutX(ship.getLayoutX()+3);
            }
        }
        if(!isLeftKeyPressed && !isRightKeyPressed){
            if(angle < 0){
                angle += 5;
            } else if (angle > 0){
                angle -= 5;
            }
            ship.setRotate(angle);
        }
        if(isLeftKeyPressed && isRightKeyPressed){
            if(angle < 0){
                angle += 5;
            } else if (angle > 0){
                angle -= 5;
            }
            ship.setRotate(angle);
        }
    }

}
