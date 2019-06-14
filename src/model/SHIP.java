package model;

public enum SHIP {
    BLUE("view/resources/shipchooser/playerShip2_blue.png"),
    GGEEN("view/resources/shipchooser/playerShip2_green.png"),
    ORANGE("view/resources/shipchooser/playerShip2_orange.png"),
    RED("view/resources/shipchooser/playerShip2_red.png");

    String urlShip;

    SHIP(String urlShip) {
        this.urlShip = urlShip;
    }
}
