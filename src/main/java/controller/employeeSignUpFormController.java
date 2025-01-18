package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class employeeSignUpFormController {

    @FXML
    public TextField txtEmpName;

    @FXML
    public TextField txtEmpContact;

    @FXML
    public TextField txtEmpEmail;

    @FXML
    public TextField txtEmpPassword;

    @FXML
    public TextField txtEmpConfirmPassword;

    @FXML
    public void btnSignUpOnAction(ActionEvent actionEvent) throws SQLException {
        String SQL = "INSERT INTO Employee (eName,eContact,eEmail,ePassword) VALUES(?,?,?,?)";

        if(txtEmpPassword.getText().equals(txtEmpConfirmPassword.getText())){

            Connection connection = DBConnection.getInstance().getConnection();

            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM Employee WHERE eEmail="+"'"+txtEmpEmail.getText()+"'");

            if(!resultSet.next()) {
                Employee emp = new Employee(
                        "",
                        txtEmpName.getText(),
                        txtEmpContact.getText(),
                        txtEmpEmail.getText(),
                        txtEmpPassword.getText()
                );

                PreparedStatement psTm = connection.prepareStatement(SQL);
                psTm.setString(1,emp.getEName());
                psTm.setString(2,emp.getEContact());
                psTm.setString(3,emp.getEEmail());
                psTm.setString(4,emp.getEPassword());
                int rowsAffected = psTm.executeUpdate();

                if (rowsAffected > 0) {
                    new Alert(Alert.AlertType.INFORMATION, "Employee registered successfully!").show();

                    txtEmpName.clear();
                    txtEmpContact.clear();
                    txtEmpEmail.clear();
                    txtEmpPassword.clear();
                    txtEmpConfirmPassword.clear();

                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to register the employee. Please try again.").show();
                }

            }else{
                new Alert(Alert.AlertType.ERROR,"This email already used").show();
            }
        }else {
            new Alert(Alert.AlertType.ERROR, "Passwords do not match.").show();
        }
    }

    @FXML
    public void btnBackOnAction(ActionEvent actionEvent) {

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
    }
}
