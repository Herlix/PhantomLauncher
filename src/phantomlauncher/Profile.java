/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phantomlauncher;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Alexander & Joakim
 */
public class Profile implements Initializable, ScreenInterface {

    @FXML
    private static Text userName;
    @FXML
    private static Text firstName;
    @FXML
    private static Text lastName;
    @FXML
    private static Text age;
    @FXML
    private static Text email;
    @FXML
    private static ImageView profileImage;
    @FXML
    private Button loadInfo;

    private ScreenController screen;

    /**
     * Tells the ScreenController that this is a screen.
     */
    @Override
    public void ScreenHandler(ScreenController screen) {
        this.screen = screen;
    }

    /**
     * Standard initilize method for autorun at startup.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     * Logout program.
     */
    @FXML
    public void logout(ActionEvent event) throws SQLException {
        Login.uCon.close(); //Logout user
        screen.setScreen("Login");
    }

    /**
     * Logout and Terminate program.
     */
    public void closeDown(ActionEvent event) throws SQLException {
        Login.uCon.close(); //Logout user
        System.exit(0);
    }

    /**
     * Minimize program.
     */
    public void minimize(ActionEvent event) {
        ScreenController.stage.setIconified(true);
    }

    public static void getInfo() {
        try {
            Statement st = Login.uCon.createStatement();
            String sql = "Select* from Users;";

            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                userName.setText(rs.getString("idUsers"));
                firstName.setText(rs.getString("FirstName"));
                lastName.setText(rs.getString("LastName"));
                email.setText(rs.getString("Email"));
                age.setText(rs.getString("Age"));
            }
        } catch (Exception ex) {
        }
    }
}
