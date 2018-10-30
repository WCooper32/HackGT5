import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javafx.scene.text.Font;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;


public class Game extends Application {

    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();

    private ArrayList<Block> platforms = new ArrayList<>();

    private StackPane display = new StackPane();
    private Pane appRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Pane uiRoot = new Pane();
    private HBox labels = new HBox();
    private static Label score = new Label();
    private static Label livesLabel = new Label();

    private int lives;
    private Block player;
    private Block coin;
    private Point2D playerVelocity = new Point2D(0, 0);
    private boolean canJump = true;

    private int levelWidth;

    private static Random rand = new Random();
    private static Random rand2 = new Random();
    private static final boolean[][] canPutCoinHere = new boolean[(LevelData.LEVEL1.length)][30];
    private static int totalCoins = 0;
    private int coinRow;
    private int coinColumn;

    private Stage primaryStage;
    private Scene scene;

    private void initContent() {
        //initate the lives, player, and the game Root
        lives = 6;
        player = new Block(0, 600, 40, 40, Color.CYAN);
        gameRoot.getChildren().add(player);

        Rectangle background = new Rectangle(1280, 720);
        levelWidth = LevelData.LEVEL1[0].length() * 60;

        //detect where the coin can be placed
        for (int x = 0; x < 5; ++x) {
            for (int y = 0; y < 30; ++y) {
                canPutCoinHere[x][y] = false;
            }
        }

        //add the platforms
        for (int i = 0; i < LevelData.LEVEL1.length; ++i) {
            String line = LevelData.LEVEL1[i];
            for (int j = 0; j < line.length(); ++j) {
                switch (line.charAt(j)) {
                case '0':
                    if ((i > 4) && (i < 10)) {
                        canPutCoinHere[i][j] = true;
                    }
                    break;
                case '1':
                    Block platform = new Block(j*60, i*60, 60, 60, Color.BROWN);
                    gameRoot.getChildren().add(platform);
                    platforms.add(platform);
                    break;
                }
            }
        }

        //get the label
        this.livesLabel = new Label();
        livesLabel.setTextFill(Color.WHITE);
        livesLabel.setFont(new Font(40.0));

        this.score = new Label();
        score.setText("Score: 0");
        score.setTextFill(Color.WHITE);
        score.setFont(new Font(40.0));

        resetPlayer();

        generateCoins();

        //make the screen follow the user
        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();

            if (offset > 640 && offset < levelWidth - 640) {
                gameRoot.setLayoutX(-(offset - 640));
            }
        });

        labels.getChildren().addAll(livesLabel, score);
        labels.setPadding(new Insets(10));
        labels.setSpacing(50);

        appRoot.getChildren().addAll(background, gameRoot, uiRoot);
        display.getChildren().addAll(appRoot, labels);
    }

    private void generateCoins() {
        //start at 5, max is 9
        int row = 0;
        int column = 0;
        String line = "Hello world!";
        char currVal = 'x';
        while (currVal != '0') {
            row = ((rand.nextInt(5)) + 5);
            column = rand.nextInt(30);
            line = LevelData.LEVEL1[row];
            currVal = line.charAt(column);
        }
        coin = new Block((column*60), (row*60), 20, 20, Color.YELLOW);
        this.coinRow = row;
        this.coinColumn = column;
        gameRoot.getChildren().add(coin);
    }

    private void resetPlayer() {
        //reset the Player and the window location
        player.setTranslateX(0);
        player.setTranslateY(0);
        gameRoot.setLayoutX(0);
        lives--;
        livesLabel.setText("Lives: " + lives);
    }

    private void update() {
        //check to see if player is touching coin
        Bounds playerBounds = player.getBoundsInParent();
        double xMinPlayer = playerBounds.getMinX();
        double xMaxPlayer = playerBounds.getMaxX();
        double yMinPlayer = playerBounds.getMinY();
        double yMaxPlayer = playerBounds.getMaxY();

        // System.out.printf("Player: %.2f, %.2f, %.2f, %.2f\n", xMinPlayer, xMaxPlayer, yMinPlayer, yMaxPlayer);

        Bounds coinBounds = coin.getBoundsInParent();
        double xMinCoin = coinBounds.getMinX();
        double xMaxCoin = coinBounds.getMaxX();
        double yMinCoin = coinBounds.getMinY();
        double yMaxCoin = coinBounds.getMaxY();

        // System.out.printf("Coin: %.2f, %.2f, %.2f, %.2f\n", xMinCoin, xMaxCoin, yMinCoin, yMaxCoin);

        if ((xMinCoin > xMinPlayer) && (xMinCoin < xMaxPlayer) && (yMinCoin > yMinPlayer) && (yMinCoin < yMaxPlayer)) {
            updateScore();
            gameRoot.getChildren().remove(coin);
            generateCoins();
        } else if ((xMaxCoin > xMinPlayer) && (xMaxCoin < xMaxPlayer) && (yMinCoin > yMinPlayer) && (yMinCoin < yMaxPlayer)) {
            updateScore();
            gameRoot.getChildren().remove(coin);
            generateCoins();
        } else if ((xMinCoin > xMinPlayer) && (xMinCoin < xMaxPlayer) && (yMaxCoin > yMinPlayer) && (yMaxCoin < yMaxPlayer)) {
            updateScore();
            gameRoot.getChildren().remove(coin);
            generateCoins();
        } else if ((xMaxCoin > xMinPlayer) && (xMaxCoin < xMaxPlayer) && (yMaxCoin > yMinPlayer) && (yMaxCoin < yMaxPlayer)) {
            updateScore();
            gameRoot.getChildren().remove(coin);
            generateCoins();
        }

        if ((isPressed(KeyCode.W) || isPressed(KeyCode.UP))
         && player.getTranslateY() >= 5) {
            jumpPlayer();
        }

        if ((isPressed(KeyCode.A) || isPressed(KeyCode.LEFT))
         && player.getTranslateX() >= 5) {
            movePlayerX(-5);
        }

        if ((isPressed(KeyCode.D) || isPressed(KeyCode.RIGHT))
         && player.getTranslateX() + 40 <= levelWidth - 5) {
            movePlayerX(5);
        }

        if(playerVelocity.getY() < 10) {
            playerVelocity = playerVelocity.add(0, 1);
        }

        if (isPressed(KeyCode.ENTER)) {
            resetPlayer();
        }

        movePlayerY((int)playerVelocity.getY());

        if (player.getTranslateY() > 800) {
            resetPlayer();
        }
    }

    private static void updateScore() {
        //update totalCoins
        totalCoins = totalCoins + 1;
        score.setText("Score: " + totalCoins);
    }

    private void movePlayerX(int value) {
        boolean movingRight = value > 0;

        for(int i = 0; i < Math.abs(value); ++i) {
            for (Block platform : platforms) {
                if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingRight) {
                        if (player.getTranslateX() + 40 == platform.getTranslateX()) {
                            return;
                        }
                    } else {
                        if (player.getTranslateX() == platform.getTranslateX() + 60) {
                            return;
                        }
                    }
                }
            }
            player.setTranslateX(player.getTranslateX() + (movingRight ? 1 : -1));
        }
    }

    private void movePlayerY(int value) {
        boolean movingDown = value > 0;

        for(int i = 0; i < Math.abs(value); ++i) {
            for (Block platform : platforms) {
                if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingDown) {
                        if (player.getTranslateY() + 40 == platform.getTranslateY()) {
                            player.setTranslateY(player.getTranslateY() - 1);
                            canJump = true;
                            return;
                        }
                    } else {
                        if (player.getTranslateY() == platform.getTranslateY() + 60) {
                            return;
                        }
                    }
                }
            }
            player.setTranslateY(player.getTranslateY() + (movingDown ? 1 : -1));
        }
    }

    private void jumpPlayer() {
        //calculate velocity
        if (canJump) {
            playerVelocity = playerVelocity.add(0, -30);
            canJump = false;
        }
    }

    private boolean isPressed(KeyCode key) {
        //check to see if a key is pressed
        return keys.getOrDefault(key, false);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //set up the screen
        this.primaryStage = primaryStage;
        initContent();

        Scene scene = new Scene(display);
        this.scene = scene;
        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        primaryStage.setTitle("Power Platformer");
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
