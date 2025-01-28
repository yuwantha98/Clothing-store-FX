package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class adminFormController {

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
            stage.setResizable(false);
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
        try {
            borderPane.setCenter(new FXMLLoader(getClass().getResource("/view/sales_report_form.fxml")).load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
