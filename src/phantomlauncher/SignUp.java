package phantomlauncher;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
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
    private Text userError;
    @FXML
    private Text passError;

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
     * Standard initilize method for autorun at startup.
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
            userName = signUpUserName.getText();
            password = signUpPassword.getText();
            firstName = signUpFirstName.getText();
            lastName = signUpLastName.getText();
            age = Integer.parseInt(SignUpAge.getText());
            email = SignUpEmail.getText();
            if (checkUserName(userName) && checkPassword(password) && firstName != null && lastName != null && age > 0 && checkEmail(email)) {
                signUp();
                Login.uCon.close(); // log out master
                screen.setScreen("Login");
            } else {
               //
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
        String mySqlGrant = "GRANT SELECT,UPDATE ON phantom.* TO '" + userName + "'@'localhost';";
        String addToDB = "INSERT INTO Users VALUES( '" + userName + "','" + firstName + "','" + lastName + "'," + age + ",'" + email + "');";
        masterLogin();
        try {
            Statement st = mCon.createStatement();
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
            masterLogin();
            Statement st = mCon.createStatement();
            String sql = "Select idUsers from Users;";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String id = rs.getString("idUsers");
                if (id.equals(userName)) {
                    userError.setText("Name taken!");
                    return false;
                }
            } 
            mCon.close();
            return true;            
        } catch (SQLException ex) {
            return false;
        }
    }

    private boolean checkPassword(String password) {
        passwordConfirm = signUpConfirm.getText();

        if (passwordConfirm.equals(password)) {
            return true;
        } else {
            passError.setText("password does not match!");
            return false;
        }
    }
}
