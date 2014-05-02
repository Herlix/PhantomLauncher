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
    private Button checkButton;
    @FXML
    private Button signUpButton;
    @FXML
    private Button backToLogin;

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
        mCon.close();
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
        try {
            Login.sshConnect();
            System.out.println("Connected...");
        } catch (Exception ex) {
            System.out.println("Connection falied...");
        }
    }
    /*
     When you click the Check username button it will login with the Master user
     and compare the entered username with all usernames in the db idUsers column
     */

    @FXML
    public void checkAction(ActionEvent event) {
        try {
            masterLogin();
            Statement st = mCon.createStatement();
            String sql = "Select idUsers from Users;";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String id = rs.getString("idUsers");
                String id2 = signUpUserName.getText();
                if (id.equals(id2)) {
                    System.out.println("Name taken");
                } else {
                    userName = signUpUserName.getText();
                    System.out.println("You can use that name");
                }
            }
            mCon.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * signUpAction reads the text-fields and runs the signUp() if all
     * information was entered correctly.
     */
    @FXML
    public void signUpAction(ActionEvent event) throws SQLException {
        password = signUpPassword.getText();
        passwordConfirm = signUpConfirm.getText();
        firstName = signUpFirstName.getText();
        lastName = signUpLastName.getText();
        age = Integer.parseInt(SignUpAge.getText());
        email = SignUpEmail.getText();
        if (userName != null && password.length() > 5 && password.equals(passwordConfirm) && firstName != null && lastName != null && age > 0 && email != null) {
            signUp();
            Login.uCon.close(); // log out master
            screen.setScreen("Login");
        } else {
            System.out.println("Någonting gick fel! Antagligen glömde du trycka på check username");
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
        String mySqlInlog = "CREATE USER '" + userName + "'@'localhost' IDENTIFIED BY '" + password + "';";
        String mySqlGrant = "GRANT SELECT ON phantom.* TO '" + userName + "'@'localhost';";
        String addToDB = "INSERT INTO Users VALUES( '" + userName + "','" + password + "','" + firstName + "','" + lastName + "'," + age + ",'" + email + "');";
        masterLogin();
        try {
            Statement st = mCon.createStatement();
            st.execute(mySqlInlog);
            st.execute(mySqlGrant);
            st.execute(addToDB);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
