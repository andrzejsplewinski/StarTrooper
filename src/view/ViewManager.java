package view;

import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.*;

import java.util.*;

import static javafx.scene.layout.BackgroundSize.DEFAULT;

public class ViewManager {
    private static final int HEIGHT = 900;
    private static final int WIDTH = 1600;
    private AnchorPane mainPane;
    private Stage mainStage;

    private final static int MENU_BUTTONS_STARTS_X = 100;
    private final static int MENU_BUTTONS_STARTS_Y = 150;

    private GameSubScene helpSubScene;
    private GameSubScene scoreSubScene;
    private GameSubScene shipChooseSubScene;

    private GameSubScene sceneToHide;

    private List<GameButtons> menuButtons;

    private List<ShipPicker> shipList;
    private SHIP shipChoosen;

    private ScoreManager scoreManager;

    public ViewManager() {

        menuButtons = new ArrayList<>();
        mainPane = new AnchorPane();
        Scene mainScene = new Scene(mainPane, WIDTH, HEIGHT);
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        scoreManager = new ScoreManager();
        createSubScene();
        createButtons();
        createBackground();
        createLogo();
    }

    private void showSubScene(GameSubScene subScene) {
        if (sceneToHide != null) {
            sceneToHide.moveSubScene();
        }
        subScene.moveSubScene();
        sceneToHide = subScene;
    }

    private void createSubScene() {
        createShipChooserSubScene();
        createScoreSubScene();
        createHelpSubScene();
    }

    private void createShipChooserSubScene() {
        shipChooseSubScene = new GameSubScene();
        mainPane.getChildren().add(shipChooseSubScene);

        InfoLabel infoLabel = new InfoLabel("Choose your ship");
        infoLabel.setLayoutX(110);
        infoLabel.setLayoutY(25);

        shipChooseSubScene.getPane().getChildren().add(infoLabel);
        shipChooseSubScene.getPane().getChildren().add(createShipsToChoose());
        shipChooseSubScene.getPane().getChildren().add(createButtonToStart());
    }

    private void createHelpSubScene() {
        helpSubScene = new GameSubScene();
        mainPane.getChildren().add(helpSubScene);

        InfoLabel infoLabel = new InfoLabel("KEYS IN THE GAME");
        infoLabel.setLayoutX(110);
        infoLabel.setLayoutY(25);

        GameLabel label1 = new GameLabel("UP ARROW");
        label1.setLayoutY(90);
        label1.setLayoutX(40);
        GameLabel label2 = new GameLabel("DOWN ARROW");
        label2.setLayoutY(150);
        label2.setLayoutX(40);
        GameLabel label3 = new GameLabel("LEFT ARROW");
        label3.setLayoutY(210);
        label3.setLayoutX(40);
        GameLabel label4 = new GameLabel("RIGHT ARROW");
        label4.setLayoutY(270);
        label4.setLayoutX(40);
        GameLabel label5 = new GameLabel("PRESS SPACE TO SHOOT");
        label5.setLayoutY(330);
        label5.setLayoutX(40);

        helpSubScene.getPane().getChildren().add(label1);
        helpSubScene.getPane().getChildren().add(label2);
        helpSubScene.getPane().getChildren().add(label3);
        helpSubScene.getPane().getChildren().add(label4);
        helpSubScene.getPane().getChildren().add(label5);
        helpSubScene.getPane().getChildren().add(infoLabel);
    }

    private void createScoreSubScene() {
        scoreSubScene = new GameSubScene();
        mainPane.getChildren().add(scoreSubScene);

        InfoLabel infoLabel = new InfoLabel("BEST SCORES");
        infoLabel.setLayoutX(110);
        infoLabel.setLayoutY(25);
        scoreSubScene.getPane().getChildren().add(infoLabel);

        HashMap<String, Integer> highScore = scoreManager.getHighScores();
        List<Map.Entry<String, Integer>> list = new LinkedList<>(highScore.entrySet());
        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        if (!list.isEmpty()) {
            GameLabel label1 = new GameLabel("Name: " + list.get(0).toString().replace("=", "   Score: "));
            label1.setLayoutX(40);
            label1.setLayoutY(120);
            scoreSubScene.getPane().getChildren().add(label1);
        }
        if (list.size() >= 2) {
            GameLabel label2 = new GameLabel("Name: " + list.get(1).toString().replace("=", "   Score: "));
            label2.setLayoutX(40);
            label2.setLayoutY(190);
            scoreSubScene.getPane().getChildren().add(label2);
        }
        if (list.size() >= 3) {
            GameLabel label3 = new GameLabel("Name: " + list.get(2).toString().replace("=", "   Score: "));
            label3.setLayoutX(40);
            label3.setLayoutY(260);
            scoreSubScene.getPane().getChildren().add(label3);
        }
    }

    private HBox createShipsToChoose() {
        HBox box = new HBox();
        box.setSpacing(20);
        shipList = new ArrayList<>();
        for (SHIP ship : SHIP.values()) {
            ShipPicker shipToPick = new ShipPicker(ship);
            shipList.add(shipToPick);
            box.getChildren().add(shipToPick);
            shipToPick.setOnMouseClicked(event -> {
                for (ShipPicker ship1 : shipList) {
                    ship1.setCircleChoosen(false);
                }
                shipToPick.setCircleChoosen(true);
                shipChoosen = shipToPick.getShip();
            });
        }
        box.setLayoutX(50);
        box.setLayoutY(100);
        return box;
    }

    private GameButtons createButtonToStart() {
        GameButtons startButton = new GameButtons("START");
        startButton.setLayoutX(350);
        startButton.setLayoutY(300);

        startButton.setOnAction(event -> {
            if (shipChoosen != null) {
                GameViewManager gameViewManager = new GameViewManager();
                gameViewManager.createNewGame(mainStage, shipChoosen);
            }
        });
        return startButton;
    }

    public Stage getMainStage() {
        return mainStage;
    }

    private void addMenuButtons(GameButtons button) {
        button.setLayoutY(MENU_BUTTONS_STARTS_Y + menuButtons.size() * 100);
        button.setLayoutX(MENU_BUTTONS_STARTS_X);
        menuButtons.add(button);
        mainPane.getChildren().add(button);
    }

    private void createButtons() {
        createStartButton();
        createScoreButton();
        createHelpButton();
        createExitButton();
    }

    private void createStartButton() {
        GameButtons startButton = new GameButtons("START");
        addMenuButtons(startButton);
        startButton.setOnAction(event -> showSubScene(shipChooseSubScene));
    }

    private void createScoreButton() {
        GameButtons scoreButton = new GameButtons("SCORES");
        addMenuButtons(scoreButton);
        scoreButton.setOnAction(event -> showSubScene(scoreSubScene));
    }

    private void createHelpButton() {
        GameButtons helpButton = new GameButtons("HELP");
        addMenuButtons(helpButton);
        helpButton.setOnAction(event -> showSubScene(helpSubScene));
    }

    private void createExitButton() {
        GameButtons exitButton = new GameButtons("EXIT");
        addMenuButtons(exitButton);
        exitButton.setOnAction(event -> mainStage.close());
    }

    private void createBackground() {
        Image backgroundImage = new Image("view/resources/space.jpg", 1920, 1200, false, true);
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                DEFAULT);
        mainPane.setBackground(new Background(background));
    }

    private void createLogo() {
        ImageView logo = new ImageView("view/resources/logo.png");
        logo.setLayoutX(350);
        logo.setLayoutY(100);
        logo.setOnMouseEntered(event -> logo.setEffect(new DropShadow()));
        logo.setOnMouseExited(event -> logo.setEffect(null));
        mainPane.getChildren().add(logo);
    }
}
