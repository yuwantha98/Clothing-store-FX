package controller;

import com.jfoenix.controls.JFXTextField;
import dto.tm.EmployeeTm;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import dto.EmployeeDto;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import util.EncryptionUtil;
import repository.DaoFactory;
import repository.service.EmployeeDao;

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
    public TableView tblEmployee;

    @FXML
    public TableColumn colID;

    @FXML
    public TableColumn colName;

    @FXML
    public TableColumn colContact;

    @FXML
    public TableColumn colEmail;

    @FXML
    public JFXTextField txtEmployeeSearchByID;

    EmployeeDao employeeDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.EMPLOYEE);

    public void initialize(){
        colID.setCellValueFactory(new PropertyValueFactory<>("eID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("eName"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("eContact"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("eEmail"));

        loadTable();
    }

    public void loadTable(){

        try {
            ObservableList<EmployeeTm> all = employeeDao.findAll();
            tblEmployee.setItems(all);
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    public void btnSignUpOnAction(ActionEvent actionEvent) throws SQLException {
        String email = txtEmpEmail.getText();
        String password = txtEmpPassword.getText();
        String confirmPassword = txtEmpConfirmPassword.getText();

        if (!validateInputs()) {
            return;
        }

        if (!password.equals(confirmPassword)) {
            new Alert(Alert.AlertType.ERROR, "Passwords do not match.").show();
            return;
        }

        if (employeeDao.existsByEmail(email)) {
            new Alert(Alert.AlertType.ERROR, "Email already exists! Please use a different email.").show();
            return;
        }

        String encryptedPassword = EncryptionUtil.encrypt(password);

        EmployeeDto employee = new EmployeeDto(
                txtEmpName.getText(),
                txtEmpContact.getText(),
                txtEmpEmail.getText(),
                encryptedPassword
        );

        try {
            boolean isAdded = employeeDao.save(employee);
            if (isAdded) {
                new Alert(Alert.AlertType.INFORMATION, "Employee added successfully!").show();
                clearForm();
                loadTable();
            } else {
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private boolean validateInputs() {
        if (txtEmpName.getText().isEmpty() ||
                txtEmpContact.getText().isEmpty() ||
                txtEmpEmail.getText().isEmpty() ||
                txtEmpPassword.getText().isEmpty() ||
                txtEmpConfirmPassword.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "All fields are required.").show();
            return false;
        }

        if (!txtEmpEmail.getText().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-z]{2,}")) {
            new Alert(Alert.AlertType.ERROR, "Invalid email format.").show();
            return false;
        }

        if (!txtEmpContact.getText().matches("\\d{10}")) {
            new Alert(Alert.AlertType.ERROR, "Invalid contact number.").show();
            return false;
        }

        return true;
    }

    private void clearForm() {
        txtEmpName.clear();
        txtEmpContact.clear();
        txtEmpEmail.clear();
        txtEmpPassword.clear();
        txtEmpConfirmPassword.clear();
    }


    @FXML
    public void btnUpdateEmployeeOnAction(ActionEvent actionEvent) {
        EmployeeDto employeeDto = new EmployeeDto(
                txtEmpName.getText(),
                txtEmpContact.getText(),
                txtEmpEmail.getText(),
                ""
        );
        try {
            boolean isUpdate = employeeDao.update(employeeDto, Integer.valueOf(txtEmployeeSearchByID.getText()));
            if(isUpdate){
                new Alert(Alert.AlertType.INFORMATION,"Employer Update Successfully !").show();
                loadTable();
            }else {
                new Alert(Alert.AlertType.ERROR,"Something went wrong !").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    @FXML
    public void btnDeleteEmployeeOnAction(ActionEvent actionEvent) {
        try {
            boolean delete = employeeDao.delete(Integer.valueOf(txtEmployeeSearchByID.getText()));
            if(delete){
                new Alert(Alert.AlertType.INFORMATION,"Employer DELETE Successfully !").show();
                loadTable();
                clearForm();
            }else {
                new Alert(Alert.AlertType.ERROR,"Something went wrong !").show();
            }

        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    public void btnSearchEmployeeOnAction(ActionEvent actionEvent) {
        try {
            Integer employeeId = Integer.valueOf(txtEmployeeSearchByID.getText());
            EmployeeDto employeeDto = employeeDao.find(employeeId);

            if (employeeDto != null) {
                txtEmpName.setText(employeeDto.getEName());
                txtEmpContact.setText(employeeDto.getEContact());
                txtEmpEmail.setText(employeeDto.getEEmail());
            } else {
                new Alert(Alert.AlertType.WARNING, "No employee found with ID: " + employeeId).show();
                clearForm();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid ID format. Please enter a valid number.").show();
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error fetching employee: " + e.getMessage()).show();
        }
    }
}
