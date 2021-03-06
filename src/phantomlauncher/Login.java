package phantomlauncher;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.text.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Alexander & Joakim
 */
public class Login implements Initializable, ScreenInterface {

    static Connection uCon = null; // User-Connection
    public static String dbPasswd;
    public static String dbUser;

    @FXML
    public static TextField idUser;
    @FXML
    private PasswordField passID;
    @FXML
    private Text error;

    private ScreenController screen;

    /**
     * Tells the ScreenController that this is a screen.
     */
    @Override
    public void ScreenHandler(ScreenController screen) {
        this.screen = screen;
    }

    /**
     * Standard initialize method for autorun at startup.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            sshConnect();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Failed to connect!");
            System.exit(0);
        }
    }

    /*
     * This is the button event for login.
     */
    @FXML
    public void loginAction(ActionEvent event) {
        loginDB();
        idUser.setText("");
        passID.setText("");
    }

    /*
     * This is the button event for the sign up function.
     */
    @FXML
    public void signUpAction(ActionEvent event) {
        screen.setScreen("SignUp"); // Switch screen
    }

    /*
     * This Method logs onto the server using info from the inputfields.
     */
    public void loginDB() {
        dbUser = idUser.getText().toLowerCase();
        dbPasswd = passID.getText();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            uCon = DriverManager.getConnection("jdbc:mysql://localhost:4321/" + "phantom", dbUser, dbPasswd);
            error.setText("");
            Profile.getInfo();
            Profile.updateProgramList();
            screen.setScreen("Profile");// Switch screen
        } catch (Exception e) {
            error.setText("Wrong Username or Password");
        }
    }

    /*
     * This Method is used to create a port forward thtough jsch for the
     * mysql connection. This is run once at startup and is running continuously
     * throughout the run.
     */
    public static void sshConnect() {
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession("pi", "178.174.222.105", 22);
            session.setPassword("AJ/#2014");
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Establishing Connection...");
            session.connect();
            session.setPortForwardingL(4321, "localhost", 3306);
        } catch (Exception e) {
            System.err.print(e);
        }
    }

    /**
     * Logout and Terminate program.
     */
    public void closeDown(ActionEvent event) throws SQLException {
        System.exit(0);
    }

    /**
     * Minimize program.
     */
    public void minimize(ActionEvent event) {
        ScreenController.stage.setIconified(true);
    }

}
