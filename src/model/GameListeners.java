package model;

import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class GameListeners {
    private boolean isLeftKeyPressed;
    private boolean isRightKeyPressed;
    private boolean isUpKeyPressed;
    private boolean isDownKeyPressed;

    private int angle;

    public GameListeners() {
    }

    public void movesShip(ImageView ship) {
        if (isLeftKeyPressed && !isRightKeyPressed) {
            if (angle > -30) angle -= 5;
            ship.setRotate(angle);
            if (ship.getLayoutX() > -10) ship.setLayoutX(ship.getLayoutX() - 5);
        }

        if (isRightKeyPressed && !isLeftKeyPressed) {
            if (angle < 30) angle += 5;
            ship.setRotate(angle);
            if (ship.getLayoutX() < 495) ship.setLayoutX(ship.getLayoutX() + 5);
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
            if (ship.getLayoutY() > 10) ship.setLayoutY(ship.getLayoutY() - 5);
        }
        if (isDownKeyPressed && !isUpKeyPressed) {
            if (ship.getLayoutY() < 700) ship.setLayoutY(ship.getLayoutY() + 5);
        }
    }

    public void createKeyListeners(Scene gameScene, Timeline firing) {
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
}
