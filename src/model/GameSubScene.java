package model;

import javafx.animation.TranslateTransition;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class GameSubScene extends SubScene {

    private final static String BACKGROUND_IMAGE = "model/resources/blue_panel.png";

    private boolean isHidden;

    public GameSubScene() {
        super(new AnchorPane(), 600, 400);
        prefWidth(600);
        prefHeight(400);

        Image image = new Image(BACKGROUND_IMAGE, 600, 400, false, true);
        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                null);

        AnchorPane root = (AnchorPane) this.getRoot();
        root.setBackground(new Background(backgroundImage));

        isHidden = true;

        setLayoutX(2000);
        setLayoutY(150);
    }

    public void moveSubScene() {
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.3));
        transition.setNode(this);

        int x = isHidden ? -1050 : 0;
        transition.setToX(x);
        isHidden = !isHidden;

        transition.play();
    }

    public AnchorPane getPane() {
        return (AnchorPane) this.getRoot();
    }

}
