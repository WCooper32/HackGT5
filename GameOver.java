import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;


public class GameOver extends Application {

  Stage window;
  Scene overScreen;

  public void start(Stage mainStage) {
    //Set vars
    window = mainStage;
    Label gameOver = new Label("Game Over");
    Button retry = new Button("Retry");
    Button exit = new Button("Exit");

    //Handle buttons
    retry.setOnAction(e -> {
      Game game = new Game();
      game.start(window);
    });
    exit.setOnAction(e -> {
      System.out.println("Thanks for playing");
      window.close();
    });

    //Layout
    VBox layout = new VBox(30);
    layout.setAlignment(Pos.CENTER);
    layout.getChildren().addAll(gameOver, retry, exit);

    //Set Stage
    overScreen = new Scene(layout, 300, 300);
    mainStage.setTitle("Power Platformer - Game Over");
    mainStage.setScene(overScreen);
    mainStage.show();

  }

  public static void main(String[] args) {
    launch(args);
    System.out.println("Hello World!");
  }





}
