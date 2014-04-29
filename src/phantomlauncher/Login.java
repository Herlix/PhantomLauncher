/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.text.*;

/**
 *
 * @author Alexander & Joakim
 */
public class Login implements Initializable {

    static Connection con = null;
    
    @FXML
    private TextField idUser;
    @FXML
    private PasswordField passID;
    @FXML
    private Text error;
    @FXML
    private Button login;
    @FXML
    private Button signUp;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            sshConnect();
        } catch (Exception ex) {
            error.setText("Failed to login");
        }
    }

    @FXML
    public void loginAction(ActionEvent event) {
        loginDB();
        idUser.setText("");
        passID.setText("");
    }

    @FXML
    public void signUpAction(ActionEvent event) {
        //todo
    }

    public void loginDB() {
        String dbUser = idUser.getText();
        String dbPasswd = passID.getText();

        String driver = "com.mysql.jdbc.Driver";
        try {
            Class.forName(driver);
            con = DriverManager.getConnection("jdbc:mysql://localhost:4321/" + "phantom", dbUser, dbPasswd);
            error.setText("");
        } catch (Exception e) {
            error.setText("Wrong Username or Password");

        }
    }

    public static void sshConnect() {
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession("pi", "178.174.222.105", 22);
            session.setPassword("AJ/#2014");
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Establishing Connection...");
            session.connect();
            int assinged_port = session.setPortForwardingL(4321, "localhost", 3306);

        } catch (Exception e) {
            System.err.print(e);
        }

    }

}
