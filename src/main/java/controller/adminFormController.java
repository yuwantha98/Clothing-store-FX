package controller;

import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Employee;

import java.sql.*;

public class adminFormController {

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
    public JFXTextField txtEmployeeName;

    @FXML
    public JFXTextField txtEmployeeContact;

    @FXML
    public JFXTextField txtEmployeeEmail;

    @FXML
    public JFXTextField txtSearchEmployeeByID;

    private ObservableList<Employee> employeeList;

    @FXML
    public void initialize() {

        colID.setCellValueFactory(new PropertyValueFactory<>("eId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("eName"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("eContact"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("eEmail"));

        loadEmployeeData();
    }

    private void loadEmployeeData() {
        employeeList = FXCollections.observableArrayList();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT * FROM Employee";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                employeeList.add(new Employee(
                        resultSet.getString("eID"),
                        resultSet.getString("eName"),
                        resultSet.getString("eContact"),
                        resultSet.getString("eEmail"),
                        "" // Exclude password for security reasons
                ));
            }

            // Set the data in the TableView
            tblEmployee.setItems(employeeList);

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load employee data.").show();
        }
    }

    @FXML
    public void btnBackOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard_form.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("Dulmark Clothing");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnEmployeeAddOnAction(ActionEvent actionEvent) {
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
    public void btnReportOnAction(ActionEvent actionEvent) {
    }

    @FXML
    public void btnUpdateEmplyeeOnAction(ActionEvent actionEvent) {

        String employeeID = txtSearchEmployeeByID.getText();
        String updatedName = txtEmployeeName.getText();
        String updatedContact = txtEmployeeContact.getText();
        String updatedEmail = txtEmployeeEmail.getText();

        if (employeeID == null || employeeID.trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid Employee ID to update.").show();
        }else {

            if (updatedName.trim().isEmpty() || updatedContact.trim().isEmpty() || updatedEmail.trim().isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Please fill in all fields before updating.").show();
            } else {

                try {
                    // Establish database connection
                    Connection connection = DBConnection.getInstance().getConnection();

                    // Prepare the update query
                    String updateQuery = "UPDATE Employee SET eName = ?, eContact = ?, eEmail = ? WHERE eID = ?";
                    PreparedStatement ps = connection.prepareStatement(updateQuery);

                    // Set the parameters
                    ps.setString(1, updatedName);
                    ps.setString(2, updatedContact);
                    ps.setString(3, updatedEmail);
                    ps.setString(4, employeeID);

                    // Execute the update query
                    int rowsAffected = ps.executeUpdate();

                    if (rowsAffected > 0) {
                        new Alert(Alert.AlertType.INFORMATION, "Employee details updated successfully!").show();

                        loadEmployeeData();

                    } else {
                        new Alert(Alert.AlertType.WARNING, "No employee found with the provided ID.").show();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "An error occurred while updating employee details.").show();
                }
            }
        }
    }

    @FXML
    public void btnDeleteEmplyeeOnAction(ActionEvent actionEvent) {

        String employeeID = txtSearchEmployeeByID.getText();

        if (employeeID == null || employeeID.trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid Employee ID to delete.").show();
        }else {

            try {
                // Establish database connection
                Connection connection = DBConnection.getInstance().getConnection();

                // Prepare the update query
                String deleteQuery = "DELETE FROM Employee WHERE eID = ?";
                PreparedStatement ps = connection.prepareStatement(deleteQuery);

                ps.setString(1, employeeID);

                // Execute the update query
                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    new Alert(Alert.AlertType.INFORMATION, "Employee details delete successfully!").show();

                    loadEmployeeData();

                } else {
                    new Alert(Alert.AlertType.WARNING, "No employee found with the provided ID.").show();
                }

            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "An error occurred while deleting employee details.").show();
            }
        }
    }

    @FXML
    public void btnSearchEmplyeeOnAction(ActionEvent actionEvent) {

        String empId = txtSearchEmployeeByID.getText();

        if(empId == null || empId.trim().isEmpty()){
            new Alert(Alert.AlertType.ERROR, "Please enter a valid Employee ID.").show();
        }else {

            try {

                Connection connection = DBConnection.getInstance().getConnection();

                String searchSQL = "SELECT * FROM Employee WHERE eID = ?";
                PreparedStatement ps = connection.prepareStatement(searchSQL);
                ps.setString(1, empId);

                // Execute the query
                ResultSet resultSet = ps.executeQuery();

                if (resultSet.next()) {
                    // If the employee exists, set the text fields with their details
                    txtEmployeeName.setText(resultSet.getString("eName"));
                    txtEmployeeContact.setText(resultSet.getString("eContact"));
                    txtEmployeeEmail.setText(resultSet.getString("eEmail"));
                } else {
                    // If no employee found, show an alert
                    new Alert(Alert.AlertType.WARNING, "No employee found with the provided ID.").show();

                    // Clear the text fields if no result
                    txtEmployeeName.clear();
                    txtEmployeeContact.clear();
                    txtEmployeeEmail.clear();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "An error occurred while searching for the employee.").show();
            }
        }
    }
}
