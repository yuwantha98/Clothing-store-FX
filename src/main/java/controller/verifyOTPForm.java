package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class verifyOTPForm {

    public String OTP;

    @FXML
    public JFXTextField txtOTPValue;

    public void setOtp(String otp) {
        OTP = otp;
    }

    @FXML
    public void btnVerifyOTPOnAction(ActionEvent actionEvent) {
        String enteredOtp = txtOTPValue.getText();

        if (enteredOtp.equals(OTP)) {
            navigateToForgotPasswordForm(actionEvent);
        } else {
            new Alert(Alert.AlertType.ERROR, "Invalid OTP! Please try again.").show();
        }
    }

    private void navigateToForgotPasswordForm(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/forgot_password_form.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("Forgot Password Form");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
