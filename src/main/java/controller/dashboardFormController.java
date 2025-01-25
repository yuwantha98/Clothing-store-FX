package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import dto.EmployeeDto;
import util.CrudUtil;
import util.EncryptionUtil;
import util.EmailUtil;

import javax.mail.MessagingException;
import java.util.Random;

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

    public void forgotPassword(MouseEvent mouseEvent) {

        String email = txtEmail.getText();
        if (email.isEmpty() || !isValidEmail(email)) {
            new Alert(Alert.AlertType.WARNING, "Please enter a valid email.").show();
            return;
        }

        String generatedOtp = generateOtp();
        openOtpPopup(generatedOtp);

        new Thread(() ->{
            try {
                String subject = "Password Reset OTP";
                String body = "Your OTP for password reset is: " + generatedOtp;

                EmailUtil.sendEmail(email, subject, body);

                javafx.application.Platform.runLater(() ->
                        new Alert(Alert.AlertType.INFORMATION, "OTP sent to " + email).show()
                );

            } catch (MessagingException e) {
                e.printStackTrace();
                javafx.application.Platform.runLater(() ->
                        new Alert(Alert.AlertType.ERROR, "Failed to send OTP. Please try again.").show()
                );
            }

        }).start();
    }

    private boolean isValidEmail(String email) {
        return email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    private void openOtpPopup(String otp) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/verify_otp_form.fxml"));
            Parent root = fxmlLoader.load();

            verifyOTPForm controller = fxmlLoader.getController();
            controller.setOtp(otp);

            Stage stage = new Stage();
            stage.setTitle("OTP Verify Form");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
