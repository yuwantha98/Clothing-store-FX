package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class productFormController {

    @FXML
    public TableView tblProduct;

    @FXML
    public TableColumn colID;

    @FXML
    public TableColumn colName;

    @FXML
    public TableColumn colSize;

    @FXML
    public TableColumn colSupplier;

    @FXML
    public TableColumn colQty;

    @FXML
    public TableColumn colBPrice;

    @FXML
    public TableColumn colDiscount;

    @FXML
    public TableColumn colSPrice;

    @FXML
    public TableColumn colProfit;

    @FXML
    public TableColumn colCategory;

    @FXML
    public JFXTextField txtProductID;

    @FXML
    public JFXTextField txtProductName;

    @FXML
    public JFXTextField txtProductSize;

    @FXML
    public JFXTextField txtProductSupplier;

    @FXML
    public JFXTextField txtProductQty;

    @FXML
    public JFXTextField txtProductBPrice;

    @FXML
    public JFXTextField txtProductSPrice;

    @FXML
    public JFXTextField txtProductDiscount;

    @FXML
    public JFXTextField txtProductProfit;

    @FXML
    public JFXTextField txtProductCategory;

    @FXML
    public JFXTextField txtProductSearchByID;

    @FXML
    public void btnAddProductOnAction(ActionEvent actionEvent) {
    }

    @FXML
    public void btnUpdateProductOnAction(ActionEvent actionEvent) {
    }

    @FXML
    public void btnDeleteProductOnAction(ActionEvent actionEvent) {
    }

    @FXML
    public void btnSearchProductOnAction(ActionEvent actionEvent) {
    }
}
