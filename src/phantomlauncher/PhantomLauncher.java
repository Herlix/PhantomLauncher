package phantomlauncher;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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

        // We need a handler that keeps track of the different "screens" we 
        // want to switch between. We also need to add the different
        // "screens" to that handler.
        ScreenController handler = new ScreenController(stage);
        handler.loadScreen("Login", "Login.fxml");
        handler.loadScreen("SignUp", "SignUp.fxml");
        handler.loadScreen("Profile", "Profile.fxml");
        handler.setScreen("Login"); // Start with this screen.        
        Scene scene = new Scene(handler);        
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Phantom Launcher");
        stage.setResizable(false);        
        stage.show();

    }
}
