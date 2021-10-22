package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/***
 * This class is the entry point of the application.
 * @author Tchaikousky Thomas
 */
public class Main extends Application {

    /***
     * This method sets the window, stage, and scene for the FXML view of the application.
     * @param primaryStage The primaryStage.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view_controller/login.fxml"));
        primaryStage.setTitle("Software 2");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    /***
     * This method starts the program.
     * @param args String array of arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
