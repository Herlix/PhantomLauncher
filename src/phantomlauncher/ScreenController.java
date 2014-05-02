/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

    // We use Stackpane as it suits our needs fine. We want to add and remove 
// screens. One could think of this as if we have a stack of cards and each 
// screen is a card, then we switch cards(screens) so that the one we want at 
// the moment is at the top.
// (http://docs.oracle.com/javafx/2/api/javafx/scene/layout/StackPane.html)
// NOTE: This implementation remove unused cards to keep the overhead
//       of handling the GUI to a minimum. The only time we have more than
//       one "screen" in the stack (card in the card deck) is when we are
//       switching to a new "screen". This is useful if we later on want to 
//       add a nice transitioning between the two "sceens".
    
    // This is needed to resize the window after changing the "screen"
    public static Stage stage;

    /**
     * Tells the ScreenController that this is the stage.
     */
    public ScreenController(Stage stage) {
        this.stage = stage;
    }

    // Keep track of all our "screens" and assign each one of them a name
    private HashMap<String, Node> screens = new HashMap<String, Node>();

    /**
     * Used for loading the screens.
     */
    public boolean loadScreen(String name, String resource) {
        try {
            // Load the "screen" from its FXML
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            Parent screen = (Parent) myLoader.load();

            // Assign the handler so that we later on can change the "screen"
            ScreenInterface myScreenController = ((ScreenInterface) myLoader.getController());
            myScreenController.ScreenHandler(this);

            //Send some info to the newly created "screen"
            if (myScreenController instanceof Login) {
                //((Login)myScreenController).setButtonText("Return to Menu");
            }

            // Save the newly loaded "screen" into our hashmap so that we can 
            // later access it (switch to it).
            screens.put(name, screen);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Used for displaying the right screen.
     */
    public boolean setScreen(final String name) {
        // First check that the "screen" we want to switch to has been loaded
        if (screens.get(name) != null) {
            // If we already displaying a "screen" start by removing it.
            if (!getChildren().isEmpty()) {
                getChildren().remove(0);
            }

            // Then display the new "screen" and resize the window to fit the 
            // new "screen"
            getChildren().add(screens.get(name));
            stage.sizeToScene();
            return true;
        } else {
            System.out.println("Could not find screen, " + name);
            return false;
        }
    }
}
