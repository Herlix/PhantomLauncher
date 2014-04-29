/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package phantomlauncher;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.scene.text.*;

/**
 * FXML Controller class
 *
 * @author Alexander
 */
public class Profile implements Initializable {
    
    @FXML
    private Text userName;
    @FXML
    private Text firstName;
    @FXML
    private Text lastName;
    @FXML
    private Text age;
    @FXML
    private Text email;
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getInfo();
    }    
    
    private void getInfo() {
        try {
            Statement st = Login.con.createStatement();
            String sql = "Select* from Users;";

            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                userName.setText(rs.getString("idUsers"));
                firstName.setText(rs.getString("FirstName"));
                lastName.setText(rs.getString("LastName"));
                email.setText (rs.getString("Email"));
                age.setText(rs.getString("Age"));
            }
        } catch (Exception ex) {
        }
    }
}
