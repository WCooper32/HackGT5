import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.layout.StackPane;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;


public class Game extends Application {

    private int score;
    private Stage window;
    private Square avatar = new Square;


    @Override
    public void start(Stage stage) {
        window = stage;
        score = 0;

        VBox vbox = new VBox();

        Label name = new Label("Power Platformer");
        name.setFont(new Font(50));

        Label points = new Label("Points: " + score);
        points.setFont(new Fonet(30));

        vbox.getChildren().addAll(name, points);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(30);

        Scene scene = new Scene(vbox, 480, 600);
        stage.setTitle("Power Platformer");

        scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                case UP:
                    updateLabels("up");
                    break;
                case DOWN:
                    updateLabels("down");
                    break;
                case LEFT:
                    updateLabels("left");
                    break;
                case RIGHT:
                    updateLabels("right");
                    break;
                }
            }
        });

        stage.setScene(scene);
        stage.show();

    }

    public static void main(String args[]) {
        launch(args);
    }

}
