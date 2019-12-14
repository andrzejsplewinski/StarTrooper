package model;

import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class GameBackground {
    private final static String BACKGROUND_IMAGE = "view/resources/darkPurple.png";
    private GridPane gridPane1;
    private GridPane gridPane2;


    public GameBackground() {
    }

    public void createBackground(AnchorPane gamePane) {
        gridPane1 = new GridPane();
        gridPane2 = new GridPane();

        for (int i = 0; i < 12; i++) {
            ImageView backgroundImage1 = new ImageView(BACKGROUND_IMAGE);
            ImageView backgroundImage2 = new ImageView(BACKGROUND_IMAGE);

            GridPane.setConstraints(backgroundImage1, i % 3, i / 3);
            GridPane.setConstraints(backgroundImage2, i % 3, i / 3);
            gridPane1.getChildren().add(backgroundImage1);
            gridPane2.getChildren().add(backgroundImage2);
        }
        gridPane2.setLayoutY(-1024);
        gamePane.getChildren().add(gridPane1);
        gamePane.getChildren().add(gridPane2);
    }


    public void moveBackground() {
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
