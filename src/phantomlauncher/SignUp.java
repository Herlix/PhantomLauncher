package phantomlauncher;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;

/**
 *
 * @author Joakim & Alexander
 */
public class SignUp implements Initializable, ScreenInterface {

    private String userName;
    private String password;
    private String passwordConfirm;
    private String firstName;
    private String lastName;
    private int age = 0;
    private String email;
    private String driver = "com.mysql.jdbc.Driver";
    private Connection mCon; // Master-Connection
    private Statement st;

    @FXML
    private TextField signUpUserName;
    @FXML
    private PasswordField signUpPassword;
    @FXML
    private PasswordField signUpConfirm;
    @FXML
    private TextField signUpFirstName;
    @FXML
    private TextField signUpLastName;
    @FXML
    private TextField SignUpAge;
    @FXML
    private TextField SignUpEmail;
    @FXML
    private Button signUpButton;
    @FXML
    private Button backToLogin;
    @FXML
    private Label userError;
    @FXML
    private Label passError1;
    @FXML
    private Label passError2;
    @FXML
    private Label ageError;

    private ScreenController screen;

    /**
     * Tells the ScreenController that this is a screen.
     */
    @Override
    public void ScreenHandler(ScreenController screen) {
        this.screen = screen;
    }

    /**
     * Back to login.
     */
    @FXML
    public void backToLogin(ActionEvent event) throws SQLException {
        screen.setScreen("Login"); // Switch screen        
    }

    /**
     * Minimize program.
     */
    public void minimize(ActionEvent event) {
        ScreenController.stage.setIconified(true);
    }

    /**
     * Standard initialize method for autorun at startup.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     * signUpAction reads the text-fields and runs the signUp() if all
     * information was entered correctly.
     */
    @FXML
    public void signUpAction(ActionEvent event) throws SQLException {
        try {
            masterLogin();
            st = mCon.createStatement();
            userName = signUpUserName.getText();
            password = signUpPassword.getText();
            firstName = signUpFirstName.getText();
            lastName = signUpLastName.getText();
            try {
                ageError.setText("");
                age = Integer.parseInt(SignUpAge.getText());
            } catch (NumberFormatException ex) {
                ageError.setText("Not a valid age!");
            }
            email = SignUpEmail.getText();
            if (checkUserName(userName) && checkPassword(password) && firstName != null && lastName != null && age > 0 && age < 120 && checkEmail(email)) {
                signUp();
                screen.setScreen("Login");
            } 
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Login to phantom db as Master account.
     */
    public void masterLogin() {
        try {
            Class.forName(driver);
            mCon = DriverManager.getConnection("jdbc:mysql://localhost:4321/" + "phantom", "Master", "master");
        } catch (ClassNotFoundException | SQLException ex) {

        }
    }

    /**
     * Creates a new db account with select privilege and stores the user info
     * in the Users table of phantom db.
     */
    public void signUp() {
        String mySqlInlog = "CREATE USER '" + userName + "'@'localhost' IDENTIFIED BY '" + password + "'";
        String mySqlGrant = "GRANT SELECT, UPDATE ON phantom.* TO '" + userName + "'@'localhost';";
        String addToDB = "INSERT INTO Users VALUES( '" + userName + "','" + firstName + "','" + lastName + "'," + age + ",'" + email + "');";
        try {      
            st.execute(mySqlInlog);
            st.execute(mySqlGrant);
            st.execute(addToDB);
            mCon.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean checkEmail(String email) {
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '@') {
                return true;
            }
        }
        return false;
    }
    /*
     When you click the Check username button it will login with the Master user
     and compare the entered username with all usernames in the db idUsers column
     */
    private boolean checkUserName(String userName) {
        try {
            userError.setText("");
            String getId = "Select idUsers from Users;";
            ResultSet rs = st.executeQuery(getId);
            while (rs.next()) {
                String id = rs.getString("idUsers");
                if (id.equals(userName)) {
                    userError.setText("Name taken!");
                    return false;
                }
            } 
                       
        } catch (SQLException ex) {
        }
        return true; 
    }

    private boolean checkPassword(String password) {
        passwordConfirm = signUpConfirm.getText();
        if (password.length() < 6) {
            passError1.setText("Password to short!");
            return false;
        }
        passError1.setText("");
        if (passwordConfirm.equals(password)) {
            passError2.setText("");
            return true;
        } else {
            passError2.setText("Password does not match!");
            return false;
        }
    }
}
