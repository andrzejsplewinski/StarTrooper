package model;

public enum SHIP {
    BLUE("view/resources/shipchooser/playerShip2_blue.png", "view/resources/shipchooser/playerLife2_blue.png"),
    GREEN("view/resources/shipchooser/playerShip2_green.png", "view/resources/shipchooser/playerLife2_green.png"),
    ORANGE("view/resources/shipchooser/playerShip2_orange.png", "view/resources/shipchooser/playerLife2_orange.png"),
    RED("view/resources/shipchooser/playerShip2_red.png", "view/resources/shipchooser/playerLife2_red.png");

    private String urlShip;
    private String urlLife;

    SHIP(String urlShip, String urlLife) {
        this.urlShip = urlShip;
        this.urlLife = urlLife;
    }

    public String getUrlShip(){
        return this.urlShip;
    }

    public String getUrlLife() {
        return urlLife;
    }
}
