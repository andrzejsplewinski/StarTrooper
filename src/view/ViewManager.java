package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.GameButtons;
import model.GameSubScene;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.layout.BackgroundSize.DEFAULT;

@SuppressWarnings("unused")
public class ViewManager {

    private static final int HEIGHT = 900;
    private static final int WIDTH = 1600;
    private AnchorPane mainPane;
    private Scene mainScene;
    private Stage mainStage;

    private final static int MENU_BUTTONS_STARTS_X = 100;
    private final static int MENU_BUTTONS_STARTS_Y = 150;

    private GameSubScene creditsSubScene;
    private GameSubScene helpSubScene;
    private GameSubScene scoreSubScene;
    private GameSubScene shipChooseSubScene;

    private GameSubScene sceneToHide;

    List<GameButtons> menuButtons;

    public ViewManager() {
        menuButtons = new ArrayList<>();
        mainPane = new AnchorPane();
        mainScene = new Scene(mainPane, WIDTH, HEIGHT);
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        createSubScene();
        createButtons();
        createBackground();
        createLogo();
    }

    private void showSubScene(GameSubScene subScene){
        if(sceneToHide != null){
            sceneToHide.moveSubScene();
        }
        subScene.moveSubScene();
        sceneToHide = subScene;

    }

    private void createSubScene() {
        creditsSubScene = new GameSubScene();
        mainPane.getChildren().add(creditsSubScene);

        helpSubScene = new GameSubScene();
        mainPane.getChildren().add(helpSubScene);

        scoreSubScene = new GameSubScene();
        mainPane.getChildren().add(scoreSubScene);

        shipChooseSubScene = new GameSubScene();
        mainPane.getChildren().add(shipChooseSubScene);
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
        createCreditsButton();
        createExitButton();
    }

    private void createStartButton() {
        GameButtons startButton = new GameButtons("START");
        addMenuButtons(startButton);

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showSubScene(shipChooseSubScene);
            }
        });
    }

    private void createScoreButton() {
        GameButtons scoreButton = new GameButtons("SCORES");
        addMenuButtons(scoreButton);
        scoreButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showSubScene(scoreSubScene);
            }
        });
    }

    private void createHelpButton() {
        GameButtons helpButton = new GameButtons("HELP");
        addMenuButtons(helpButton);
        helpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showSubScene(helpSubScene);
            }
        });
    }

    private void createCreditsButton() {
        GameButtons creditsButton = new GameButtons("CREDITS");
        addMenuButtons(creditsButton);

        creditsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showSubScene(creditsSubScene);
            }
        });
    }

    private void createExitButton() {
        GameButtons exitButton = new GameButtons("EXIT");
        addMenuButtons(exitButton);

        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainStage.close();
            }
        });
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

        logo.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                logo.setEffect(new DropShadow());
            }
        });

        logo.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                logo.setEffect(null);
            }
        });
        mainPane.getChildren().add(logo);
    }
}
