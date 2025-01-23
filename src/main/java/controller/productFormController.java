package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import dto.ProductDto;
import dto.tm.ProductTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import repository.DaoFactory;
import repository.service.ProductDao;
import repository.service.SupplierDao;

import java.sql.*;

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
    public JFXTextField txtProductName;

    @FXML
    public JFXTextField txtProductSize;

    @FXML
    public JFXComboBox cmbProductSupplier;

    @FXML
    public JFXTextField txtProductQty;

    @FXML
    public JFXTextField txtProductBPrice;

    @FXML
    public JFXTextField txtProductSPrice;

    @FXML
    public JFXTextField txtProductDiscount;

    @FXML
    public JFXTextField txtProductCategory;

    @FXML
    public JFXTextField txtProductSearchByID;

    ProductDao productDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.PRODUCT);
    SupplierDao supplierDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.SUPPLIER);

    public void initialize() {
        colID.setCellValueFactory(new PropertyValueFactory<>("pID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("pName"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("pSize"));
        colSupplier.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("pQuantity"));
        colBPrice.setCellValueFactory(new PropertyValueFactory<>("pBuyingPrice"));
        colSPrice.setCellValueFactory(new PropertyValueFactory<>("pPrice"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("pDiscount"));
        colProfit.setCellValueFactory(new PropertyValueFactory<>("pProfit"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("pCategory"));

        loadSupplierNames();
        loadProductData();
    }

    private void loadSupplierNames() {
        try {
            ObservableList<String> supplierNames = FXCollections.observableArrayList(supplierDao.getAllSupplierNames());
            cmbProductSupplier.setItems(supplierNames);
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading suppliers: " + e.getMessage()).show();
        }
    }

    private void loadProductData() {
        try {
            ObservableList<ProductTm> products = productDao.findAll();
            tblProduct.setItems(products);
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading products: " + e.getMessage()).show();
        }
    }

    @FXML
    public void btnAddProductOnAction(ActionEvent actionEvent) {
        try {
            Integer supplierID = supplierDao.findSupplierIDByName((String) cmbProductSupplier.getValue());
            if (supplierID == null) {
                new Alert(Alert.AlertType.ERROR, "Supplier not found!").show();
                return;
            }

            ProductDto productDto = new ProductDto(
                    txtProductName.getText(),
                    txtProductSize.getText(),
                    supplierID,
                    Integer.parseInt(txtProductQty.getText()),
                    Double.parseDouble(txtProductBPrice.getText()),
                    Double.parseDouble(txtProductSPrice.getText()),
                    Double.parseDouble(txtProductDiscount.getText()),
                    Double.parseDouble(txtProductSPrice.getText()) - (Double.parseDouble(txtProductBPrice.getText())+Double.parseDouble(txtProductSPrice.getText())*Double.parseDouble(txtProductDiscount.getText())/100),
                    txtProductCategory.getText()
            );

            boolean isAdded = productDao.save(productDto);
            if (isAdded) {
                new Alert(Alert.AlertType.INFORMATION, "Product added successfully!").show();
                clearForm();
                loadProductData();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to add product.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    public void btnUpdateProductOnAction(ActionEvent actionEvent) {
        try {
            ProductDto productDto = new ProductDto(
                    txtProductName.getText(),
                    txtProductSize.getText(),
                    supplierDao.findSupplierIDByName((String) cmbProductSupplier.getValue()),
                    Integer.parseInt(txtProductQty.getText()),
                    Double.parseDouble(txtProductBPrice.getText()),
                    Double.parseDouble(txtProductSPrice.getText()),
                    Double.parseDouble(txtProductDiscount.getText()),
                    Double.parseDouble(txtProductSPrice.getText()) - Double.parseDouble(txtProductBPrice.getText()),
                    txtProductCategory.getText()
            );

            boolean isUpdated = productDao.update(productDto, Integer.parseInt(txtProductSearchByID.getText()));
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Product updated successfully!").show();
                clearForm();
                loadProductData();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update product.").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    public void btnDeleteProductOnAction(ActionEvent actionEvent) {
        try {
            boolean isDeleted = productDao.delete(Integer.parseInt(txtProductSearchByID.getText()));
            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION, "Product deleted successfully!").show();
                clearForm();
                loadProductData();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete product.").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    public void btnSearchProductOnAction(ActionEvent actionEvent) {
        try {
            ProductDto productDto = productDao.find(Integer.parseInt(txtProductSearchByID.getText()));
            if (productDto != null) {
                txtProductName.setText(productDto.getPName());
                txtProductSize.setText(productDto.getPSize());
                cmbProductSupplier.setValue(supplierDao.findSupplierNameByID(productDto.getSupplierID()));
                txtProductQty.setText(String.valueOf(productDto.getPQuantity()));
                txtProductBPrice.setText(String.valueOf(productDto.getPBuyingPrice()));
                txtProductSPrice.setText(String.valueOf(productDto.getPPrice()));
                txtProductDiscount.setText(String.valueOf(productDto.getPDiscount()));
                txtProductCategory.setText(productDto.getPCategory());
            } else {
                new Alert(Alert.AlertType.ERROR, "Product not found.").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    private void clearForm() {
        txtProductName.clear();
        txtProductSize.clear();
        cmbProductSupplier.setValue(null);
        txtProductQty.clear();
        txtProductBPrice.clear();
        txtProductSPrice.clear();
        txtProductDiscount.clear();
        txtProductCategory.clear();
        txtProductSearchByID.clear();
    }
}

