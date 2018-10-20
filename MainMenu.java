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

public class MainMenu extends Application {

  private Stage window;
  private Scene mainMenu, gameMenu;

  public void start(Stage mainStage) {
    //Define variables
    window = mainStage;
    Label label = new Label("Power Platformer");
    Button startButton = new Button("Start");
    Button exitButton = new Button("Exit");

    //Handle button presses
    startButton.setOnAction(e -> startGame());
    exitButton.setOnAction(e -> closeGame());

    //Create the layout
    VBox layout = new VBox(30);
    layout.setAlignment(Pos.CENTER);
    layout.getChildren().addAll(label, startButton, exitButton);

    //Set the scene and show
    mainMenu = new Scene(layout, 300, 300);
    mainStage.setTitle("Power Platformer - Main Menu");
    mainStage.setScene(mainMenu);
    mainStage.show();
  }

  private void startGame() {
    System.out.println("Starting game...");
    Game game = new Game();
    game.start(window);
  }


  private void closeGame() {
    System.out.println("Closing game...");
    window.close();
  }

  public static void main(String[] args) {
    launch(args);
  }

}
