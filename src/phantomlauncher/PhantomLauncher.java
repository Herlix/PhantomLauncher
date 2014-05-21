package phantomlauncher;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Alexander & Joakim
 */
public class PhantomLauncher extends Application {

    /**
     * Standard main method. A backup method for starting with external inputs
     * at startup.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Start method that prompts the stage to show.
     */
    @Override
    public void start(Stage stage) throws Exception {
        ScreenController handler = new ScreenController(stage);
        handler.loadScreen("Login", "Login.fxml");
        handler.loadScreen("SignUp", "SignUp.fxml");
        handler.loadScreen("Profile", "Profile.fxml");
        handler.setScreen("Login");      
        Scene scene = new Scene(handler);        
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Phantom Launcher");
        stage.setResizable(false);        
        stage.show();

    }
}
