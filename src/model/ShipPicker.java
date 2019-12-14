package model;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


public class ShipPicker extends VBox {

    private static final String BLUE_CIRCLE_PATH = "view/resources/shipchooser/blue_circle.png";
    private static final String DEFAULT_IMAGE_PATH = "view/resources/shipchooser/grey_circle.png";
    private ImageView circleImage;
    private SHIP ship;

    public ShipPicker(SHIP ship){
        circleImage = new ImageView(DEFAULT_IMAGE_PATH);
        ImageView shipImage = new ImageView(ship.getShipPath());
        this.ship = ship;
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.getChildren().add(circleImage);
        this.getChildren().add(shipImage);
    }

    public SHIP getShip() {
        return ship;
    }

    public void setCircleChoosen(boolean isCircleChoosen) {
        String imagePath = isCircleChoosen ? BLUE_CIRCLE_PATH : DEFAULT_IMAGE_PATH;

        circleImage.setImage(new Image(imagePath));
    }
}
