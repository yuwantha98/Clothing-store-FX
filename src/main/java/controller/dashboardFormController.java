package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Employee;
import org.jasypt.util.text.BasicTextEncryptor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class dashboardFormController {

    @FXML
    public JFXTextField txtEmail;

    @FXML
    public JFXPasswordField jfxPassword;

    @FXML
    public void btnEmpolyeeLogin(ActionEvent actionEvent) throws SQLException {

        String adminEmail = "admin@gmail.com";
        String adminPassword = "12345";

        String enteredEmail = txtEmail.getText();
        String enteredPassword = jfxPassword.getText();

        if (enteredEmail.equals(adminEmail) && enteredPassword.equals(adminPassword)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin_form.fxml"));
                Scene scene = new Scene(loader.load());

                Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();

                stage.setScene(scene);
                stage.setTitle("Admin Form");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        String SQL = "SELECT * FROM Employee WHERE eEmail="+"'"+txtEmail.getText()+"'";

        Connection connection = DBConnection.getInstance().getConnection();
        ResultSet resultSet = connection.createStatement().executeQuery(SQL);

        if(resultSet.next()){
            Employee emp = new Employee(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );

            String key = "#1998YUwa";
            BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
            basicTextEncryptor.setPassword(key);
            String decrypt = basicTextEncryptor.decrypt(emp.getEPassword());

            if(decrypt.equals(jfxPassword.getText())){
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/employee_form.fxml"));
                    Scene scene = new Scene(loader.load());

                    Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();

                    stage.setScene(scene);
                    stage.setTitle("Employee Form");
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                new Alert(Alert.AlertType.ERROR,"Password doesn't match").show();
            }

        }else {
            new Alert(Alert.AlertType.ERROR,"User not found").show();
        }

    }

}
