package phantomlauncher;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

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
    private String age;
    private String email;
    private String emailConfirm;
    private Connection mCon; // Master-Connection
    private Statement st;
    private boolean once = false;

    @FXML
    private TextField signUpUserName;
    @FXML
    private PasswordField signUpPassword;
    @FXML
    private PasswordField signUpPassConfirm;
    @FXML
    private TextField signUpFirstName;
    @FXML
    private TextField signUpLastName;
    @FXML
    private ChoiceBox yearAge;
    @FXML
    private ChoiceBox monthAge;
    @FXML
    private ChoiceBox dayAge;
    @FXML
    private TextField SignUpEmail;
    @FXML
    private TextField SignUpEmailConfirm;
    @FXML
    private Button signUpButton;
    @FXML
    private Label userError;
    @FXML
    private Label passError1;
    @FXML
    private Label passError2;
    @FXML
    private static Label emailError;
    @FXML
    private CheckBox accept;

    private ScreenController screen;

    /**
     * Tells the ScreenController that this is a screen.
     */
    @Override
    public void ScreenHandler(ScreenController screen) {
        this.screen = screen;
    }

    /**
     * Terminate Program.
     */
    @FXML
    public void closeDown(ActionEvent event) {
        try {
            mCon.close();
        } catch (Exception e) {
        }

        System.exit(0);
    }

    /**
     * Back to login.
     */
    @FXML
    public void backToLogin(ActionEvent event) {
        screen.setScreen("Login");
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

    public void checkInput() throws SQLException {
        if (once == false) {
            masterLogin();
            st = mCon.createStatement();
            once = true;
        }
        userName = signUpUserName.getText();
        password = signUpPassword.getText();
        passwordConfirm = signUpPassConfirm.getText();
        firstName = signUpFirstName.getText();
        lastName = signUpLastName.getText();
        age = (String) yearAge.getSelectionModel().getSelectedItem() + " - " + (String) monthAge.getSelectionModel().getSelectedItem() + " - " + (String) dayAge.getSelectionModel().getSelectedItem();
        email = SignUpEmail.getText();
        emailConfirm = SignUpEmailConfirm.getText();
        
        checkUserName(userName);
        checkPassword(password, passwordConfirm);
        checkEmail(email, emailConfirm);

        if (checkUserName(userName) && checkPassword(password, passwordConfirm) && !firstName.equals("") &&!lastName.equals("") && !age.equals("") && checkEmail(email, emailConfirm) && accept.isSelected()) {
            signUpButton.setDisable(false);
        }
    }

    /**
     * signUpAction reads the text-fields and runs the signUp() if all
     * information was entered correctly.
     */
    @FXML
    public void signUpAction(ActionEvent event) {
        try {
            signUp();
            screen.setScreen("Login");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Login to phantom db as Master account.
     */
    public void masterLogin() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            mCon = DriverManager.getConnection("jdbc:mysql://localhost:4321/" + "phantom", "Master", "master");
        } catch (ClassNotFoundException | SQLException ex) {

        }
    }

    /**
     * Creates a new db account with select privilege and stores the user info
     * in the Users table of phantom db.
     */
    public void signUp() {
        String mySqlInlog = "CREATE USER '" + userName.toLowerCase() + "'@'localhost' IDENTIFIED BY '" + password + "'";
        String mySqlGrant = "GRANT SELECT, UPDATE, INSERT, DELETE ON phantom.* TO '" + userName.toLowerCase() + "'@'localhost';";
        String addToDB = "INSERT INTO Users VALUES( '" + userName + "','" + firstName + "','" + lastName + "','" + age + "','" + email + "');";
        String addToValidate = "INSERT INTO validation VALUES( 0,'','','" + userName + "');";
        try {
            st.execute(mySqlInlog);
            st.execute(mySqlGrant);
            st.execute(addToDB);
            st.execute(addToValidate);
            mCon.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean checkEmail(String email, String emailConfirm) {
        emailError.setText("");
        boolean checkEmail = false;
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '@' && email.length() > 5) {
                emailError.setText("");
                checkEmail = true;
                break;
            } else {
                emailError.setText("Invalid Entry");
            }
        }
        if (email.equals(emailConfirm) && checkEmail) {
                return true;
        } else if (!emailConfirm.equals("")) {
            emailError.setText("Emails do not match!");
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
                if (id.equals(userName) || userName.equals("")) {
                    userError.setText("Name taken!");
                    return false;
                }
            }

        } catch (SQLException ex) {
        }
        return true;
    }

    private boolean checkPassword(String password, String passwordConfirm) {
        passError1.setText("");
        if (password.equals("")) {
            return false;
        }
        if (password.length() < 6) {
            passError1.setText("Password to short!");
            return false;
        }
        passError1.setText("");
        passError2.setText("");
        if (passwordConfirm.equals(password)) {
            return true;
        } else if (!passwordConfirm.equals("")){
            passError2.setText("Password does not match!");
            return false;
        }
        return false;
    }
}
