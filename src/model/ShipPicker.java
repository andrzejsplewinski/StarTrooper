package model;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


public class ShipPicker extends VBox {

    private ImageView circleImage;

    private String circleNotChoosen = "view/resources/shipchooser/grey_circle.png";

    private SHIP ship;

    public ShipPicker(SHIP ship){
        circleImage = new ImageView(circleNotChoosen);
        ImageView shipImage = new ImageView(ship.getUrlShip());
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
        String imageToSet;
        if (isCircleChoosen){
            imageToSet = "view/resources/shipchooser/blue_circle.png";
        }else imageToSet = circleNotChoosen;
        circleImage.setImage(new Image(imageToSet));
    }
}
