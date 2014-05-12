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
            String password = JOptionPane.showInputDialog("Enter Password");
            String passConfirm = JOptionPane.showInputDialog("Enter Password again");
            if (password.length() > 5 && password.equals(passConfirm)) {
                try {
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
            String emailChange = JOptionPane.showInputDialog("Enter Email");
            String emailConfirm = JOptionPane.showInputDialog("Enter Email again");
            if (SignUp.checkEmail(emailChange, emailConfirm)) {
                try {
                    String set = "UPDATE phantom.Users SET email = '" + emailChange + "' WHERE idUsers = '" + userName.getText() + "'";
                    st.execute(set);
                    JOptionPane.showMessageDialog(null, "Your email has been changed!");
                    getInfo();
                } catch (Exception ex) {
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid entry or input did not match");
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

    public void addProgram(ActionEvent event) throws SQLException {
        try {
            String input = JOptionPane.showInputDialog("Enter serial");
            if (input.equals("ALJ8-UTLE-8UlP-2OGC")) {
                st.execute("UPDATE phantom.validation SET BmiCalculator = 'BmiCalculator' WHERE Users_idUsers = '" + Login.dbUser + "';");
                updateProgramList();
            } else if (input.equals("1KDG-1111-39UG-9UIG")) {
                st.execute("UPDATE phantom.validation SET NumberApp = 'NumberApp' WHERE Users_idUsers = '" + Login.dbUser + "';");
                updateProgramList();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid input!, try again");
            }
        } catch (Exception e) {

        }
    }

    public static void updateProgramList() {
        try {
            ObservableList<String> items = FXCollections.observableArrayList();
            String read = "Select * from phantom.validation WHERE Users_idUsers = '" + Login.dbUser + "';";
            ResultSet rs = st.executeQuery(read);
            while (rs.next()) {
                if (!rs.getString("BmiCalculator").equals("")) {
                    items.addAll(rs.getString("BmiCalculator") + " - A simple BMI calculator");
                }
                if (!rs.getString("NumberApp").equals("")) {
                    items.addAll(rs.getString("NumberApp") + " - A simple app for saving your numbers and notes");
                }
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
