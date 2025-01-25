package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import repository.DaoFactory;
import repository.service.EmployeeDao;
import util.EncryptionUtil;

public class forgotPasswordForm {
    @FXML
    public JFXTextField txtConfirmPassword;

    @FXML
    public JFXTextField txtPassword;

    @FXML
    public JFXTextField txtEmail;

    EmployeeDao employeeDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.EMPLOYEE);

    @FXML
    public void btnPasswordUpdateOnAction(ActionEvent actionEvent) {
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        if (!validateInputs()) {
            return;
        }

        if (!password.equals(confirmPassword)) {
            new Alert(Alert.AlertType.ERROR, "Passwords do not match.").show();
            return;
        }

        if (employeeDao.existsByEmail(email)) {
            try {

                String encryptedPassword = EncryptionUtil.encrypt(confirmPassword);

                if(employeeDao.updatePasswordByEmail(email, encryptedPassword)){
                    new Alert(Alert.AlertType.INFORMATION, "Password updated successfully!").show();
                }else {
                    new Alert(Alert.AlertType.ERROR, "Failed to update password").show();
                }

                ((Stage) txtPassword.getScene().getWindow()).close();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to update password. Please try again.").show();
            }
        }else {
            new Alert(Alert.AlertType.ERROR, "This mail doesn't has my Database.").show();
        }
    }

    private boolean validateInputs() {
        if (txtEmail.getText().isEmpty() ||
                txtPassword.getText().isEmpty() ||
                txtConfirmPassword.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "All fields are required.").show();
            return false;
        }

        if (!txtEmail.getText().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-z]{2,}")) {
            new Alert(Alert.AlertType.ERROR, "Invalid email format.").show();
            return false;
        }
        return true;
    }
}
