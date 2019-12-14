package model;

public enum SHIP {
    BLUE("view/resources/shipchooser/playerShip2_blue.png", "view/resources/shipchooser/playerLife2_blue.png"),
    GREEN("view/resources/shipchooser/playerShip2_green.png", "view/resources/shipchooser/playerLife2_green.png"),
    ORANGE("view/resources/shipchooser/playerShip2_orange.png", "view/resources/shipchooser/playerLife2_orange.png"),
    RED("view/resources/shipchooser/playerShip2_red.png", "view/resources/shipchooser/playerLife2_red.png");

    private String shipPath;
    private String lifePath;

    SHIP(String ShipPath, String lifePath) {
        this.shipPath = ShipPath;
        this.lifePath = lifePath;
    }

    public String getShipPath(){
        return this.shipPath;
    }

    public String getLifePath() {
        return lifePath;
    }
}
