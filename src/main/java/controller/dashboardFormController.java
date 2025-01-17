package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class dashboardFormController {

    @FXML
    public JFXTextField txtEmail;

    @FXML
    public JFXPasswordField jfxPassword;

    @FXML
    public void btnEmpolyeeLogin(ActionEvent actionEvent) {
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
    }


    @FXML
    public void btnEmpolyeeSignUp(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/employee_signup_form.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("Employee Sign Up Form");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnAdminLogin(ActionEvent actionEvent) {
        String correctEmail = "admin@gmail.com";
        String correctPassword = "12345";

        String enteredEmail = txtEmail.getText();
        String enteredPassword = jfxPassword.getText();

        if (enteredEmail.equals(correctEmail) && enteredPassword.equals(correctPassword)) {
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
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText("Invalid Credentials");
            alert.setContentText("The email or password you entered is incorrect.");
            alert.showAndWait();
        }
    }

}
