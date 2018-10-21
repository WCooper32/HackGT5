import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Game extends Application {

    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();

    private ArrayList<Block> platforms = new ArrayList<>();

    private Pane appRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Pane uiRoot = new Pane();

    private int lives;
    private Block player;
    private Point2D playerVelocity = new Point2D(0, 0);
    private boolean canJump = true;

    private int levelWidth;

    private void initContent() {
        lives = 4;
        player = new Block(0, 600, 40, 40, Color.CYAN);
        gameRoot.getChildren().add(player);

        Rectangle background = new Rectangle(1280, 720);
        levelWidth = LevelData.LEVEL1[0].length() * 60;

        for (int i = 0; i < LevelData.LEVEL1.length; ++i) {
            String line = LevelData.LEVEL1[i];
            for (int j = 0; j < line.length(); ++j) {
                switch (line.charAt(j)) {
                case '0':
                    break;
                case '1':
                    Block platform = new Block(j*60, i*60, 60, 60, Color.BROWN);
                    gameRoot.getChildren().add(platform);
                    platforms.add(platform);
                    break;
                }
            }
        }

        resetPlayer();

        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();

            if (offset > 640 && offset < levelWidth - 640) {
                gameRoot.setLayoutX(-(offset - 640));
            }
        });

        appRoot.getChildren().addAll(background, gameRoot, uiRoot);
    }

    private void resetPlayer() {
        gameRoot.getChildren().remove(player);
        player = new Block(0, 600, 40, 40, Color.CYAN);
        gameRoot.getChildren().add(player);
        lives--;
    }

    private void update() {
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
        if (canJump) {
            playerVelocity = playerVelocity.add(0, -30);
            canJump = false;
        }
    }

    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        initContent();

        Scene scene = new Scene(appRoot);
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
