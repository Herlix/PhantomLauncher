package phantomlauncher;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.application.Platform;
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
    private Button closeDown;
    @FXML
    private Button checkAll;
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

        if (!signUpUserName.getText().equals("")) {
            checkUserName(userName);
        }

        if (!signUpPassword.getText().equals("")) {
            if (signUpPassword.getText().length() > 5) {
                if (!signUpPassword.getText().equals("") && !signUpPassConfirm.getText().equals("")) {
                    checkPassword(password, passwordConfirm);
                }
            } else {
                passError1.setText("password to short");
            }
        }

        if (!SignUpEmail.getText().equals("")) {
            if (SignUpEmail.getText().equals(SignUpEmailConfirm.getText())) {
                if (checkEmail(email, emailConfirm)) {
                    accept.setDisable(false);
                } else {
                    emailError.setText("Email does not match each other");
                }

            }
        }

        if (accept.isSelected() && checkUserName(userName) && checkEmail(email, emailConfirm) && checkPassword(password, passwordConfirm) && !age.equals("") && !userName.equals("") && !lastName.equals("")) {
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
        String mySqlGrant = "GRANT SELECT, UPDATE, INSERT ON phantom.* TO '" + userName.toLowerCase() + "'@'localhost';";
        String addToDB = "INSERT INTO Users VALUES( '" + userName + "','" + firstName + "','" + lastName + "','" + age + "','" + email + "');";
        try {
            st.execute(mySqlInlog);
            st.execute(mySqlGrant);
            st.execute(addToDB);
            mCon.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean checkEmail(String email, String emailConfirm) {
        emailError.setText("");
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '@' && email.length() > 5 && email.equals(emailConfirm)) {
                emailError.setText("");
                return true;
            }
        }
        emailError.setText("Invalid Entry");
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

    private boolean checkPassword(String password, String passwordConfirm) {
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
