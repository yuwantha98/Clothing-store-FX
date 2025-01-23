package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import dto.EmployeeDto;
import util.CrudUtil;
import util.EncryptionUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class dashboardFormController {

    @FXML
    public JFXTextField txtEmail;

    @FXML
    public JFXPasswordField jfxPassword;

    @FXML
    public void btnEmpolyeeLogin(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

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

        String SQL = "SELECT * FROM Employee WHERE eEmail=?";

        ResultSet rst = CrudUtil.execute(SQL, txtEmail.getText());

        if(rst.next()){
            EmployeeDto emp = new EmployeeDto(
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5)
            );

            String decrypt1 = EncryptionUtil.decrypt(emp.getEPassword());


            if(decrypt1.equals(jfxPassword.getText())){
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
