package model;

import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.util.List;
import java.util.Random;

public class ElementsBehavior {

    private boolean isLeftKeyPressed;
    private boolean isRightKeyPressed;
    private boolean isUpKeyPressed;
    private boolean isDownKeyPressed;

    private int angle;

    private Random randomPositionGenerator = new Random();

    public ElementsBehavior() {
    }

    public void relocateElementsPosition(List<ImageView> element, double relocatePosition) {
        setThePositionOfTheElementsWhenTheElementsAreBehindTheScene(element, relocatePosition);
    }

    private void setThePositionOfTheElementsWhenTheElementsAreBehindTheScene(List<ImageView> elements, double positionX) {
        for (ImageView element : elements) {
            if (element.getLayoutY() > 900) {
                element.relocate(positionX, -300);
            }
        }
    }

    public void relocateStarPosition(ImageView star) {
        if (star.getLayoutY() > 1200) {
            star.setLayoutX(randomPositionGenerator.nextInt(370));
            star.setLayoutY(-(randomPositionGenerator.nextInt(3200) + 600));
        }
    }

    public void initializeTransitionGreenEnemies(List<ImageView> greenEnemies) {
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

    public void initializeTransitionRedEnemies(List<ImageView> redEnemies) {
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

    public void specialElementBehavior(List<ImageView> enemies, ImageView ship) {
        for (ImageView black : enemies) {
            black.setLayoutX(ship.getLayoutX());
        }
        for (ImageView blue : enemies) {
            if (blue.getLayoutY() > 500) {
                if (blue.getLayoutX() > ship.getLayoutX()) {
                    blue.setLayoutX(blue.getLayoutX() - 5);
                } else {
                    blue.setLayoutX(blue.getLayoutX() + 5);
                }
            }
        }
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
