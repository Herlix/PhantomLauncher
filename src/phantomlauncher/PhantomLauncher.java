/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

    public static void main(String[] args) {
        launch(args);
    }

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
        stage.show();
        stage.setTitle("Phantom Launcher");
        stage.setResizable(false);

    }
}
