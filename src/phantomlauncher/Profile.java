package phantomlauncher;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Alexander & Joakim
 */
public class Profile implements Initializable, ScreenInterface {

    @FXML
    private static Text userName;
    @FXML
    private static Text fullName;
    @FXML
    private static Text age;
    @FXML
    private static Text email;
    @FXML
    private static ImageView profileImage;
    @FXML
    private static ImageView boulderDash;
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
     * Standard initialize method for autorun at startup.
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

    /**
     * Used to get user info and diplay it.
     */
    public static void getInfo() {
        try {
            Statement st = Login.uCon.createStatement();
            String sql = "Select* from Users WHERE idUsers = '" + Login.idUser.getText() + "';";

            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                userName.setText(rs.getString("idUsers"));
                fullName.setText(rs.getString("FirstName") + " " + rs.getString("LastName"));
                email.setText(rs.getString("Email"));
                age.setText(rs.getString("Age"));
            }
        } catch (Exception ex) {
        }
    }

    /**
     * Button event for changing password.
     */
    public void changePass(ActionEvent event) {
        passwordChanger();
    }

    /**
     * Used to change users password.
     */
    public void passwordChanger() {
        try {
            String password = JOptionPane.showInputDialog("Enter Password");
            String passConfirm = JOptionPane.showInputDialog("Enter Password again");
            if (password.length() > 5 && password.equals(passConfirm)) {
                try {
                    Statement st = Login.uCon.createStatement();
                    String set = "SET password FOR '" + userName.getText() + "'@'localhost'=PASSWORD('" + password + "')";
                    st.executeQuery(set);
                    JOptionPane.showMessageDialog(null, "Your password has been changed!");
                } catch (Exception ex) {
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Entry");
                passwordChanger();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Password Not Changed");
        }
    }

    /**
     * Button event for changing email.
     */
    public void changeEmail(ActionEvent event) {
        changeEmailAdress();
    }

    /**
     * Used to change users email.
     */
    public void changeEmailAdress() {
        try {
            String emailConfirm = "";
            String emailChange = JOptionPane.showInputDialog("Enter Email");
            if (SignUp.checkEmail(emailChange)) {
                emailConfirm = JOptionPane.showInputDialog("Enter Email again");
            }
            if (emailChange.equals(emailConfirm)) {
                try {
                    Statement st = Login.uCon.createStatement();
                    String set = "UPDATE phantom.Users SET email = '" + emailChange + "' WHERE idUsers = '" + userName.getText() + "'";
                    st.execute(set);
                    JOptionPane.showMessageDialog(null, "Your email has been changed!");
                    getInfo();
                } catch (Exception ex) {
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Entry");
                changeEmailAdress();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Email Not Changed");
        }

    }
    
    public void launchBD() {
        try {
            String filePath = "C:/Users/Joakim/Desktop/NumberApp/NumberApp.jar";
            Runtime.getRuntime().exec("java -jar " + filePath + " " + Login.dbUser + " " + Login.dbPasswd);
            } catch (IOException ex) {
                System.out.println("Något gick fel");
        }
        /**
        try {
            File file = new File("phantom.txt");
            OutputStream os = new FileOutputStream(file);
            String login = Login.dbUser + "\n" + Login.dbPasswd;
            os.write(login.getBytes());
            os.close();
        } catch (IOException ex) {
            
        }
    */}
}
