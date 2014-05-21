package phantomlauncher;

import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Joakim & Alexander
 */
public class ScreenController extends StackPane {

    public static Stage stage;

    public ScreenController(Stage stage) {
        this.stage = stage;
    }
    // Stores a name and the nodes of an FMXL.
    private HashMap<String, Node> screens = new HashMap<String, Node>();
    
    /**
     * Loads an FXML and saves it to the HashMap.
     */
    public boolean loadScreen(String name, String resource) {
        try {
            // Loads an FXML.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resource));
            Parent screen = (Parent) fxmlLoader.load();

            // Assign the handler so that we later on can change the "screen"
            ScreenInterface ScreenController = ((ScreenInterface) fxmlLoader.getController());
            ScreenController.ScreenHandler(this);
 
            // Saves the name and screen to the HashMap.
            screens.put(name, screen);
            return true;
        } catch (Exception e) {
            System.out.println("Failed to load screen!");
            return false;
        }
    }

    /**
     * Setting the screen.
     */
    public boolean setScreen(String name) {
        // Checks that the screen exists.
        if (screens.get(name) != null) {
            // Removes the resorces from the screen.
            if (!getChildren().isEmpty()) {
                getChildren().remove(0);
            }

            // adds resorces to the screen.
            getChildren().add(screens.get(name));
            stage.sizeToScene();
            return true;
        } else {
            System.out.println("Failed to set screen!");
            return false;
        }
    }
}
