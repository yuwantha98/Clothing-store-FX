package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class employeeFormController {

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
    public void btnProductOnAction(ActionEvent actionEvent) {
        try {
            borderPane.setCenter(new FXMLLoader(getClass().getResource("/view/product_form.fxml")).load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void btnSupplierOnAction(ActionEvent actionEvent) {
        try {
            borderPane.setCenter(new FXMLLoader(getClass().getResource("/view/supplier_form.fxml")).load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void btnOrderOnAction(ActionEvent actionEvent) {
//        try {
//            borderPane.setCenter(new FXMLLoader(getClass().getResource("/view/order_form.fxml")).load());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}
