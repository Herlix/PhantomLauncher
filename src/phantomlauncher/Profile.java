package phantomlauncher;

import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import org.apache.commons.io.FileUtils;

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
    private static ImageView BmiCalculator;
    @FXML
    private Button loadInfo;
    @FXML
    private static ListView programs;
    @FXML
    private static Button startThis;
    private static Statement st;
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
            st = Login.uCon.createStatement();
            String sql = "Select* from Users WHERE idUsers = '" + Login.idUser.getText() + "';";

            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                userName.setText(rs.getString("idUsers"));
                fullName.setText(rs.getString("FirstName") + " " + rs.getString("LastName"));
                email.setText(rs.getString("Email"));
                age.setText(rs.getString("Age"));
                if (!rs.getString("image").equals("")) {
                    String path = rs.getString("image");
                    try {
                        Image image = new Image(path);
                        profileImage.setImage(image);
                        profileImage.setFitWidth(85);
                    } catch (Exception bild) {
                    }
                }
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
            String oldPass = JOptionPane.showInputDialog("Enter your old Password");
            if (oldPass.equals(Login.dbPasswd)) {
                String password = JOptionPane.showInputDialog("Enter Password");
                if (!password.equals(Login.dbPasswd)) {
                    String passConfirm = JOptionPane.showInputDialog("Enter Password again");
                    if (!passConfirm.equals(Login.dbPasswd) && password.length() > 5 && password.equals(passConfirm)) {
                        String set = "SET password FOR '" + userName.getText() + "'@'localhost'=PASSWORD('" + password + "')";
                        st.executeQuery(set);
                        Login.dbPasswd = password;
                        JOptionPane.showMessageDialog(null, "Your password has been changed!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid Entry");
                        passwordChanger();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "New password can't match old password");
                    passwordChanger();
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
            String emailChange = JOptionPane.showInputDialog("Enter Email");
            if (emailChange.contains("@") && emailChange.contains(".") && emailChange.length() > 5 && !emailChange.equals(email.getText())) {
                String emailConfirm = JOptionPane.showInputDialog("Enter Email again");
                if (SignUp.checkEmail(emailChange, emailConfirm)) {
                    String set = "UPDATE phantom.Users SET email = '" + emailChange + "' WHERE idUsers = '" + userName.getText() + "'";
                    st.execute(set);
                    email.setText(emailChange);
                    JOptionPane.showMessageDialog(null, "Your email has been changed!");
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid entry or input did not match");
                    changeEmailAdress();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid entry");
                changeEmailAdress();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Email Not Changed");
        }
    }

    public String runProgram(String runThis) throws SQLException {
        if (!runThis.equals("")) {
            try {
                Runtime.getRuntime().exec("java -jar " + runThis + ".jar " + Login.dbUser + " " + Login.dbPasswd);
                Login.uCon.close();
                System.exit(0);
            } catch (Exception ex) {
                System.out.println("Något gick fel");
            }
        }
        return "something went wrong";
    }

    public void changeImage() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Image");
            fileChooser.getExtensionFilters().addAll(
                    new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                    new ExtensionFilter("All Files", "*.*"));
            File selectedFile = fileChooser.showOpenDialog(ScreenController.stage);
            if (selectedFile != null) {
                File file = selectedFile;
                File desc = new File((new File("")).getParentFile() + file.getName());
                FileUtils.copyFile(file, desc);
                Image img = new Image(desc.toURI().toURL().toExternalForm());
                st.execute("UPDATE phantom.Users SET image = '" + desc.toURI().toURL().toExternalForm() + "' WHERE idUsers = '" + Login.dbUser + "';");
                profileImage.setImage(img);
                profileImage.setFitWidth(85);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void addProgram(ActionEvent event) {
        try {
            String app = "";
            String input = JOptionPane.showInputDialog("Enter serial");
            String read = "Select * from phantom.activision WHERE serial = '" + input + "';";
            ResultSet rs = st.executeQuery(read);
            rs.next();
            app = rs.getString("application");

            String add = "INSERT INTO phantom.validation VALUES( '0','" + Login.dbUser + "','" + app + "','" + input + "');";
            st.execute(add);

            String delete = "DELETE FROM phantom.activision WHERE serial = '" + input + "';";
            st.executeUpdate(delete);

            updateProgramList();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid entry, try again");
        }
    }

    public static void updateProgramList() {
        try {
            ObservableList<String> items = FXCollections.observableArrayList();
            String read = "Select * from phantom.validation WHERE Users_idUsers = '" + Login.dbUser + "';";
            ResultSet rs = st.executeQuery(read);
            while (rs.next()) {
                items.addAll(rs.getString("Applications"));
            }
            programs.setItems(items);
            programs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        } catch (SQLException e) {
            System.err.println(e);
        }

    }

    public void startProgrm(ActionEvent event) throws SQLException {
        String run = "";
        String process = programs.getSelectionModel().getSelectedItems().toString().replace("]", "").replace("[", "");
        for (int i = 0; i < process.length(); i++) {
            if (process.charAt(i + 1) == '-') {
                break;
            } else {
                run = run + process.charAt(i);
            }
        }
        runProgram(run);
    }

    public void selectProgram() {
        if (!programs.getSelectionModel().getSelectedItems().toString().equals("")) {
            startThis.setDisable(false);
            startThis.setOpacity(10);
        } else {
            startThis.setDisable(true);
            startThis.setOpacity(6);
        }
    }
}
