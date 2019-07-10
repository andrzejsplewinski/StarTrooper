package model;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

public class GameViewManager {

    private AnchorPane gamePane;
    private Scene gameScene;
    private Stage gameStage;

    private final static int GAME_WIDTH = 600;
    private final static int GAME_HEIGHT = 800;

    private Stage menuStage;
    private ImageView ship;

    private boolean isLeftKeyPressed;
    private boolean isRightKeyPressed;
    private boolean isUpKeyPressed;
    private boolean isDownKeyPressed;

    private int angle;
    private AnimationTimer gameTimer;

    private GridPane gridPane1;
    private GridPane gridPane2;
    private final static String BACKGROUND_IMAGE = "view/resources/darkPurple.png";

    private final static String BROWN_METEOR = "view/resources/meteorBrown_med3.png";
    private final static String GREY_METEOR = "view/resources/meteorGrey_med1.png";

    private List<ImageView> brownMeteors;
    private List<ImageView> greyMeteors;
    private Random randomPositionGenerator;

    private ImageView star;
    private SmallInfoLabel pointsLabel;
    private List<ImageView> playerLifes;
    private int playerLife;
    private int points;
    private Map<String, Integer> highScore;
    private Map<String, Integer> score;
    public final HashMap<String, Integer> sortedScoreMap = new HashMap<>();

    private final static int SHIP_RADIUS = 30;
    private final static int STAR_RADIUS = 12;
    private final static int ENEMY_RADIUS = 27;
    private final static int METEOR_RADIUS = 20;
    private final static int LASER_RADIUS = 12;

    private final static String GOLD_STAR_IMAGE = "view/resources/star_gold.png";

    private final static String LASER_PLAYER_SHOOT_IMAGE = "view/resources/shipchooser/fire01.png";
    private static final String HIGHSCORE_FILE = "ranking.list";

    private List<Node> laser = new ArrayList<>();

    private Duration firingInterval = Duration.millis(1000);
    private Timeline firing = new Timeline(
            new KeyFrame(Duration.ZERO, event -> fire()),
            new KeyFrame(firingInterval));

    private final static String BLACK_ENEMIES_IMAGE = "view/resources/enemies/enemyBlack1.png";
    private final static String BLUE_ENEMIES_IMAGE = "view/resources/enemies/enemyBlue2.png";
    private final static String GREEN_ENEMIES_IMAGE = "view/resources/enemies/enemyGreen3.png";
    private final static String RED_ENEMIES_IMAGE = "view/resources/enemies/enemyRed4.png";
    private List<ImageView> blackEnemies;
    private List<ImageView> blueEnemies;
    private List<ImageView> greenEnemies;
    private List<ImageView> redEnemies;

    public GameViewManager() {
        initializeStage();
        createKeyListeners();
        initializeHighScore();
        randomPositionGenerator = new Random();
    }

    private void initializeHighScore() {
        score = this.getHighScore();
    }

    private void getScoreGame() {
        String name = JOptionPane.showInputDialog("Please enter your name:");
        score = new HashMap<>();
        score.put(name, points);
        saveScore();
    }

    private HashMap<String, Integer> getHighScore() {
        highScore = new HashMap<>();
        String line;
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(HIGHSCORE_FILE));

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);

                if (parts.length >= 2) {
                    String key = parts[0].replace("{", "");
                    String value = parts[1].replace("}", "");
                    int intValue = Integer.parseInt(value);
                    highScore.put(key, intValue);
                }
            }
        } catch (IOException e) {
            System.out.println("Read exception: " + e);
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                System.out.println("Read exception: " + e);
            }
        }
        return sortHighScoreMap();
    }

    private HashMap<String, Integer> sortHighScoreMap() {

        List<Map.Entry<String, Integer>> list = new LinkedList<>(highScore.entrySet());
        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        for (Map.Entry<String, Integer> sortList : list) {
            if (sortedScoreMap.size() < 3) {
                sortedScoreMap.put(sortList.getKey(), sortList.getValue());
                System.out.println("Name: " + sortList.getKey() + "  Score: " + sortList.getValue());
            }
        }
        return sortedScoreMap;
    }


    private void saveScore() {
        File scoreFile = new File(HIGHSCORE_FILE);
        BufferedWriter writer = null;

        try {
            FileWriter writerFile = new FileWriter(scoreFile, true);
            writer = new BufferedWriter(writerFile);
            writer.newLine();
            writer.write(String.valueOf(score));

        } catch (IOException e) {
            System.out.println("Write exception: " + e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.out.println("Write exception: " + e);
                }
            }
        }
    }

    private void initializeStage() {
        gamePane = new AnchorPane();
        gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
        gameStage = new Stage();
        gameStage.setScene(gameScene);
    }

    private void fire() {
        Node newLaser = new ImageView(LASER_PLAYER_SHOOT_IMAGE);
        newLaser.relocate(ship.getLayoutX() + 48, ship.getLayoutY() - 20);
        laser.add(newLaser);
        gamePane.getChildren().add(newLaser);
    }

    private void createKeyListeners() {
        gameScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                isLeftKeyPressed = true;
            } else if (event.getCode() == KeyCode.RIGHT) {
                isRightKeyPressed = true;
            } else if (event.getCode() == KeyCode.UP) {
                isUpKeyPressed = true;
            } else if (event.getCode() == KeyCode.DOWN) {
                isDownKeyPressed = true;
            } else if (event.getCode() == KeyCode.SPACE && firing.getStatus() != Animation.Status.RUNNING) {
                firing.playFromStart();
            }
        });
        gameScene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                isLeftKeyPressed = false;
            } else if (event.getCode() == KeyCode.RIGHT) {
                isRightKeyPressed = false;
            } else if (event.getCode() == KeyCode.UP) {
                isUpKeyPressed = false;
            } else if (event.getCode() == KeyCode.DOWN) {
                isDownKeyPressed = false;
            } else if (event.getCode() == KeyCode.SPACE) {
                firing.stop();
            }
        });
    }

    public void createNewGame(Stage menuStage, SHIP shipChoosen) {
        this.menuStage = menuStage;
        this.menuStage.hide();
        createBackground();
        createShip(shipChoosen);
        createGameElements(shipChoosen);
        createGameLoop();
        gameStage.show();
    }

    private void createGameElements(SHIP shipChossen) {
        playerLife = 2;
        star = new ImageView(GOLD_STAR_IMAGE);
        setElementsOnPosition(star);
        gamePane.getChildren().add(star);
        pointsLabel = new SmallInfoLabel("Points : 00");
        pointsLabel.setLayoutX(420);
        pointsLabel.setLayoutY(60);
        gamePane.getChildren().add(pointsLabel);
        playerLifes = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            playerLifes.add(new ImageView(shipChossen.getUrlLife()));
            playerLifes.get(i).setLayoutX(380 + (playerLifes.size() * 50));
            playerLifes.get(i).setLayoutY(120);
            gamePane.getChildren().add(playerLifes.get(i));
        }

        brownMeteors = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            brownMeteors.add(new ImageView(BROWN_METEOR));
            setElementsOnPosition(brownMeteors.get(i));
            gamePane.getChildren().add(brownMeteors.get(i));
        }

        greyMeteors = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            greyMeteors.add(new ImageView(GREY_METEOR));
            setElementsOnPosition(greyMeteors.get(i));
            gamePane.getChildren().add(greyMeteors.get(i));
        }

        blackEnemies = new ArrayList<>();
        blackEnemies.add(new ImageView(BLACK_ENEMIES_IMAGE));
        for (ImageView black : blackEnemies) {
            setElementsOnPosition(black);
            gamePane.getChildren().add(black);
        }
        blueEnemies = new ArrayList<>();
        blueEnemies.add(new ImageView(BLUE_ENEMIES_IMAGE));
        for (ImageView blue : blueEnemies) {
            setElementsOnPosition(blue);
            gamePane.getChildren().add(blue);
        }
        greenEnemies = new ArrayList<>();
        greenEnemies.add(new ImageView(GREEN_ENEMIES_IMAGE));
        for (ImageView green : greenEnemies) {
            gamePane.getChildren().add(green);
            initializeTransitionGreenEnemies();
        }

        redEnemies = new ArrayList<>();
        redEnemies.add(new ImageView(RED_ENEMIES_IMAGE));
        for (ImageView red : redEnemies) {
            gamePane.getChildren().add(red);
            initializeTransitionRedEnemies();
        }
    }


    private void moveGameElements() {
        star.setLayoutY(star.getLayoutY() + 5);

        for (int i = 0; i < laser.size(); i++) {
            if (laser.get(i).getLayoutY() < GAME_HEIGHT) {
                laser.get(i).setLayoutY(laser.get(i).getLayoutY() - 7);
            } else laser.remove(i);
        }

        for (ImageView brownMeteor : brownMeteors) {
            brownMeteor.setLayoutY(brownMeteor.getLayoutY() + 7);
            brownMeteor.setRotate(brownMeteor.getRotate() + 4);
        }

        for (ImageView greyMeteor : greyMeteors) {
            greyMeteor.setLayoutY(greyMeteor.getLayoutY() + 7);
            greyMeteor.setRotate(greyMeteor.getRotate() + 4);
        }

        for (ImageView black : blackEnemies) {
            black.setLayoutY(black.getLayoutY() + 4);
            black.setLayoutX(ship.getLayoutX());
        }

        for (ImageView blue : blueEnemies) {
            blue.setLayoutY(blue.getLayoutY() + 15);
            if (blue.getLayoutY() > 500) {
                if (blue.getLayoutX() > ship.getLayoutX()) {
                    blue.setLayoutX(blue.getLayoutX() - 5);
                } else {
                    blue.setLayoutX(blue.getLayoutX() + 5);
                }
            }
        }
        for (ImageView green : greenEnemies) {
            green.setLayoutY(green.getLayoutY() + 1);
        }
        for (ImageView redEnemy : redEnemies) {
            redEnemy.setLayoutY(redEnemy.getLayoutY() + 2);

        }
    }

    private void checkIfElementsAreBehindTheSceneAndRelocate() {

        if (star.getLayoutY() > 1200) {
            setElementsOnPosition(star);
        }

        for (ImageView brownMeteor : brownMeteors) {
            if (brownMeteor.getLayoutY() > 900) {
                setElementsOnPosition(brownMeteor);
            }
        }

        for (ImageView greyMeteor : greyMeteors) {
            if (greyMeteor.getLayoutY() > 900) {
                setElementsOnPosition(greyMeteor);
            }
        }
        for (ImageView black : blackEnemies) {
            if (black.getLayoutY() > 900) {
                setElementsOnPosition(black);
            }
        }
        for (ImageView blue : blueEnemies) {
            if (blue.getLayoutY() > 900) {
                setElementsOnPosition(blue);
            }
        }
        for (ImageView green : greenEnemies) {
            if (green.getLayoutY() > 900) {
                green.relocate(Math.random(), -300);
            }
        }
        for (ImageView red : redEnemies) {
            if (red.getLayoutY() > 900) {
                red.relocate(0, -300);
            }
        }
    }

    private void setElementsOnPosition(ImageView image) {
        image.setLayoutX(randomPositionGenerator.nextInt(370));
        image.setLayoutY(-(randomPositionGenerator.nextInt(3200) + 600));
    }

    private void createShip(SHIP shipChoosen) {
        ship = new ImageView(shipChoosen.getUrlShip());
        ship.setLayoutX(GAME_WIDTH / 2 - 50);
        ship.setLayoutY(GAME_HEIGHT - 90);
        gamePane.getChildren().add(ship);

    }

    private void createGameLoop() {
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                moveBackground();
                checkIfElementsAreBehindTheSceneAndRelocate();
                moveGameElements();
                checkIfElementsCollide();
                movesShip();
            }
        };
        gameTimer.start();
    }

    private void movesShip() {

        if (isLeftKeyPressed && !isRightKeyPressed) {
            if (angle > -30) {
                angle -= 5;
            }
            ship.setRotate(angle);
            if (ship.getLayoutX() > -10) {
                ship.setLayoutX(ship.getLayoutX() - 5);
            }
        }

        if (isRightKeyPressed && !isLeftKeyPressed) {
            if (angle < 30) {
                angle += 5;
            }
            ship.setRotate(angle);
            if (ship.getLayoutX() < 495) {
                ship.setLayoutX(ship.getLayoutX() + 5);
            }
        }
        if (!isLeftKeyPressed && !isRightKeyPressed) {
            if (angle < 0) {
                angle += 5;
            } else if (angle > 0) {
                angle -= 5;
            }
            ship.setRotate(angle);
        }
        if (isLeftKeyPressed && isRightKeyPressed) {
            if (angle < 0) {
                angle += 5;
            } else if (angle > 0) {
                angle -= 5;
            }
            ship.setRotate(angle);
        }
        if (isUpKeyPressed && !isDownKeyPressed) {
            if (ship.getLayoutY() > 10) {
                ship.setLayoutY(ship.getLayoutY() - 5);
            }
        }
        if (isDownKeyPressed && !isUpKeyPressed) {
            if (ship.getLayoutY() < 700) {
                ship.setLayoutY(ship.getLayoutY() + 5);
            }
        }

    }

    private void createBackground() {
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


    private void moveBackground() {
        gridPane1.setLayoutY(gridPane1.getLayoutY() + 0.5);
        gridPane2.setLayoutY(gridPane2.getLayoutY() + 0.5);

        if (gridPane1.getLayoutY() >= 1024) {
            gridPane1.setLayoutY(-1024);
        }
        if (gridPane2.getLayoutY() >= 1024) {
            gridPane2.setLayoutY(-1024);
        }
    }

    private void addPoints(int point) {
        points += point;
        List<Integer> pointsList = new ArrayList<>();
        pointsList.add(points);
        for (int i : pointsList) {
            int sum = IntStream.of(i).sum();
            String textToSet = "POINTS : ";
            if (sum < 10) {
                textToSet = textToSet + "0";
            }
            pointsLabel.setText(textToSet + sum);
        }
    }

    private void checkIfElementsCollide() {

        if (SHIP_RADIUS + STAR_RADIUS > calculateDistance(ship.getLayoutX() + 49, star.getLayoutX() + 15,
                ship.getLayoutY() + 37, star.getLayoutY() + 15)) {
            setElementsOnPosition(star);
            addPoints(3);
        }

        for (ImageView brownMeteor : brownMeteors) {
            if (SHIP_RADIUS + METEOR_RADIUS > calculateDistance(ship.getLayoutX() + 49, brownMeteor.getLayoutX() + 20,
                    ship.getLayoutY() + 37, brownMeteor.getLayoutY() + 20)) {
                removeLife();
                setElementsOnPosition(brownMeteor);
            }
        }

        for (ImageView greyMeteor : greyMeteors) {
            if (SHIP_RADIUS + METEOR_RADIUS > calculateDistance(ship.getLayoutX() + 49, greyMeteor.getLayoutX() + 20,
                    ship.getLayoutY() + 37, greyMeteor.getLayoutY() + 20)) {
                setElementsOnPosition(greyMeteor);
                removeLife();
            }
        }

        for (ImageView brownMeteor : brownMeteors) {
            for (int k = 0; k < laser.size(); k++) {
                if (METEOR_RADIUS + LASER_RADIUS > calculateDistance(brownMeteor.getLayoutX() + 20, laser.get(k).getLayoutX() + 12,
                        brownMeteor.getLayoutY() + 20, laser.get(k).getLayoutY() + 12)) {
                    addPoints(1);
                    setElementsOnPosition(brownMeteor);
                    gamePane.getChildren().remove(laser.get(k));
                    laser.remove(k);
                }
            }
        }

        for (ImageView greyMeteor : greyMeteors) {
            for (int k = 0; k < laser.size(); k++) {
                if (METEOR_RADIUS + LASER_RADIUS > calculateDistance(greyMeteor.getLayoutX() + 20, laser.get(k).getLayoutX() + 12,
                        greyMeteor.getLayoutY() + 20, laser.get(k).getLayoutY() + 12)) {
                    addPoints(1);
                    setElementsOnPosition(greyMeteor);
                    gamePane.getChildren().remove(laser.get(k));
                    laser.remove(k);
                }
            }
        }

        for (ImageView blackEnemy : blackEnemies) {
            for (int k = 0; k < laser.size(); k++) {
                if (ENEMY_RADIUS + LASER_RADIUS > calculateDistance(blackEnemy.getLayoutX() + 49, laser.get(k).getLayoutX() + 12,
                        blackEnemy.getLayoutY() + 37, laser.get(k).getLayoutY() + 12)) {
                    addPoints(2);
                    setElementsOnPosition(blackEnemy);
                    gamePane.getChildren().remove(laser.get(k));
                    laser.remove(k);
                }
            }
        }
        for (ImageView blackEnemy : blackEnemies) {
            if (ENEMY_RADIUS + SHIP_RADIUS > calculateDistance(blackEnemy.getLayoutX() + 49, ship.getLayoutX() + 49,
                    blackEnemy.getLayoutY() + 37, ship.getLayoutY() + 37)) {
                setElementsOnPosition(blackEnemy);
                removeLife();
            }
        }


        for (ImageView blueEnemy : blueEnemies) {
            for (int k = 0; k < laser.size(); k++) {
                if (ENEMY_RADIUS + LASER_RADIUS > calculateDistance(blueEnemy.getLayoutX() + 49, laser.get(k).getLayoutX() + 12,
                        blueEnemy.getLayoutY() + 37, laser.get(k).getLayoutY() + 12)) {
                    addPoints(2);
                    setElementsOnPosition(blueEnemy);
                    gamePane.getChildren().remove(laser.get(k));
                    laser.remove(k);
                }
            }
        }
        for (ImageView blueEnemy : blueEnemies) {
            if (ENEMY_RADIUS + SHIP_RADIUS > calculateDistance(blueEnemy.getLayoutX() + 52, ship.getLayoutX() + 49,
                    blueEnemy.getLayoutY() + 40, ship.getLayoutY() + 37)) {
                setElementsOnPosition(blueEnemy);
                removeLife();
            }
        }


        for (int i = 0; i < laser.size(); i++) {
            for (ImageView greenEnemy : greenEnemies) {
                if (laser.get(i).getBoundsInParent().intersects(greenEnemy.getBoundsInParent())) {
                    addPoints(2);
                    greenEnemy.relocate(Math.random(), -300);
                    gamePane.getChildren().remove(laser.get(i));
                    laser.remove(i);
                }
            }
        }
        for (ImageView greenEnemy : greenEnemies) {
            if (greenEnemy.getBoundsInParent().intersects(ship.getBoundsInParent())) {
                greenEnemy.relocate(Math.random(), -300);
                removeLife();
            }
        }

        for (int i = 0; i < laser.size(); i++) {
            for (ImageView redEnemy : redEnemies) {
                if (laser.get(i).getBoundsInParent().intersects(redEnemy.getBoundsInParent())) {
                    addPoints(2);
                    redEnemy.relocate(0, -300);
                    gamePane.getChildren().remove(laser.get(i));
                    laser.remove(i);
                }
            }
        }
        for (ImageView redEnemy : redEnemies) {
            if (ship.getBoundsInParent().intersects(redEnemy.getBoundsInParent())) {
                redEnemy.relocate(0, -100);
                removeLife();
            }
        }
    }

    private double calculateDistance(double x1, double x2, double y1, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    private void removeLife() {

        gamePane.getChildren().remove(playerLifes.get(playerLife));
        playerLife--;
        if (playerLife < 0) {
            getScoreGame();
            gameStage.close();
            gameTimer.stop();
            menuStage.show();

        }
    }

    private void initializeTransitionGreenEnemies() {
        for (ImageView green : greenEnemies) {
            Path path = new Path();
            path.getElements().add(new MoveTo(250, -100));
            ArcTo arcTo = new ArcTo();
            arcTo.setX(150);
            arcTo.setY(150);
            arcTo.setRadiusX(50);
            arcTo.setRadiusY(50);
            path.getElements().add(arcTo);
            path.getElements().add(new ClosePath());

            PathTransition transition = new PathTransition();
            transition.setNode(green);
            transition.setDuration(Duration.seconds(2));
            transition.setPath(path);
            transition.setCycleCount(PathTransition.INDEFINITE);
            transition.play();
        }
    }

    private void initializeTransitionRedEnemies() {
        for (ImageView red : redEnemies) {
            Path path = new Path();

            MoveTo moveTo = new MoveTo();
            moveTo.setX(30);
            moveTo.setY(50);

            LineTo line = new LineTo();
            line.setX(570);
            line.setY(50);

            LineTo lineBack = new LineTo(30, 50);

            path.getElements().add(moveTo);
            path.getElements().add(line);
            path.getElements().add(lineBack);

            PathTransition transition = new PathTransition();
            transition.setNode(red);
            transition.setDuration(Duration.seconds(4));
            transition.setPath(path);
            transition.setCycleCount(PathTransition.INDEFINITE);
            transition.play();
        }
    }
}

