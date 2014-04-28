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

/**
 *
 * @author Alexander & Joakim
 */
public class Login implements Initializable {

    @FXML
    private TextField idUser;
    @FXML
    private TextField passID;
    @FXML
    private TextField userName;
    @FXML
    private TextField fullName;
    @FXML
    private TextField error;
    @FXML
    private Button login;
    @FXML
    private Button logout;
    
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

    public void loginDB() {
        String dbUser = idUser.getText();
        String dbPasswd = passID.getText();
        Connection con = null;
        String driver = "com.mysql.jdbc.Driver";
        try {
            Class.forName(driver);
            con = DriverManager.getConnection("jdbc:mysql://localhost:4321/" + "phantom", dbUser, dbPasswd);

        } catch (Exception e) {
            error.setText("Wrong Login or Password");
        }
        try {
            Statement st = con.createStatement();
            String sql = "Select* from Users;";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String id = rs.getString("idUsers");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String password = rs.getString("Password");
                userName.setText(id);
                fullName.setText(firstName + " " + lastName);

            }
        } catch (Exception ex) {
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
