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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import dto.EmployeeDto;


import java.io.IOException;
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

    @FXML
    public BorderPane borderPane;


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
            borderPane.setCenter(new FXMLLoader(getClass().getResource("/view/employee_signup_form.fxml")).load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void btnReportOnAction(ActionEvent actionEvent) {
    }
}
