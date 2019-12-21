package model;

import javafx.animation.PathTransition;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ElementBehavior {
    private Random randomPositionGenerator = new Random();
    private List<ImageView> greenEnemies = new ArrayList<>();
    private List<ImageView> redEnemies = new ArrayList<>();
    public ElementBehavior() {
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

    public void laserMoves(List<Node> laser, int GAME_HEIGHT) {
        for (int i = 0; i < laser.size(); i++) {
            if (laser.get(i).getLayoutY() < GAME_HEIGHT) {
                laser.get(i).setLayoutY(laser.get(i).getLayoutY() - 7);
            } else laser.remove(i);
        }
    }

    public void moveElementFromUpToDown(List<ImageView> element, int speed, int route) {
        for (ImageView elements : element) {
            elements.setLayoutY(elements.getLayoutY() + speed);
            elements.setRotate(elements.getRotate() + route);
        }
    }
}
