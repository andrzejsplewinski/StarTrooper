package model;

public enum SHIP {
    BLUE("view/resources/shipchooser/playerShip2_blue.png"),
    GGEEN("view/resources/shipchooser/playerShip2_green.png"),
    ORANGE("view/resources/shipchooser/playerShip2_orange.png"),
    RED("view/resources/shipchooser/playerShip2_red.png");

    private String urlShip;

    SHIP(String urlShip) {
        this.urlShip = urlShip;
    }

    public String getUrlShip(){
        return this.urlShip;
    }
}
