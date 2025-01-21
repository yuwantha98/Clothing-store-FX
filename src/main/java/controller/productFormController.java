package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Product;
import model.Supplier;


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


    ObservableList<Product> productList;



    public void initialize() {
        colID.setCellValueFactory(new PropertyValueFactory<>("pID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("pName"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("pSize"));
        colSupplier.setCellValueFactory(new PropertyValueFactory<>("sID"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("pQuantity"));
        colBPrice.setCellValueFactory(new PropertyValueFactory<>("pBuyingPrice"));
        colSPrice.setCellValueFactory(new PropertyValueFactory<>("pPrice"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("pDiscount"));
        colProfit.setCellValueFactory(new PropertyValueFactory<>("pProfit"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("pCategory"));

        loadSupplierNames();
        loadProductData();
    }
    private void loadProductData() {
        productList = FXCollections.observableArrayList();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "SELECT p.pID, p.pName, p.pSize, s.sName, p.pQuantity, p.pBuyingPrice, p.pPrice, p.pDiscount, p.pProfit, p.pCategory FROM Product p JOIN Supplier s ON p.sID=s.sID";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                productList.add(new Product(
                        resultSet.getString("pID"),
                        resultSet.getString("pName"),
                        resultSet.getString("pSize"),
                        resultSet.getString("sName"),
                        resultSet.getInt("pQuantity"),
                        resultSet.getDouble("pBuyingPrice"),
                        resultSet.getDouble("pPrice"),
                        resultSet.getDouble("pDiscount"),
                        resultSet.getDouble("pProfit"),
                        resultSet.getString("pCategory")
                ));
            }

            // Set the data in the TableView
            tblProduct.setItems(productList);

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load supplier data.").show();
        }
    }

    private void loadSupplierNames() {
        ObservableList<String> supplierNames = FXCollections.observableArrayList();

        try {
            Connection connection = DBConnection.getInstance().getConnection();

            String query = "SELECT sName FROM Supplier";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                supplierNames.add(resultSet.getString("sName"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        cmbProductSupplier.setItems(supplierNames);
    }

    @FXML
    public void btnAddProductOnAction(ActionEvent actionEvent) throws SQLException {

        String SQL = "INSERT INTO Product (pName, pSize, sID, pQuantity, pBuyingPrice, pPrice, pDiscount, pCategory) VALUES(?,?,?,?,?,?,?,?)";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT sID FROM Supplier WHERE sName = ?");
        ps.setString(1, (String) cmbProductSupplier.getValue());
        ResultSet resultSet = ps.executeQuery();

        String supplierID = null;
        if (resultSet.next()) {
            supplierID = resultSet.getString("sID");
            System.out.println(resultSet.getString("sID"));
        } else {
            throw new RuntimeException("Supplier not found for the selected company: " + cmbProductSupplier.getValue());
        }

        double bp = Double.parseDouble(txtProductBPrice.getText());
        double sp = Double.parseDouble(txtProductSPrice.getText());
        double dis = Double.parseDouble(txtProductDiscount.getText());

        double profit = sp - ( bp + sp*dis/100);

        Product product = new Product(
                "",
                txtProductName.getText(),
                txtProductSize.getText(),
                supplierID,
                Integer.parseInt(txtProductQty.getText()),
                Double.parseDouble(txtProductBPrice.getText()),
                Double.parseDouble(txtProductSPrice.getText()),
                Double.parseDouble(txtProductDiscount.getText()),
                profit,
                txtProductCategory.getText()
        );


        PreparedStatement insertPs = connection.prepareStatement(SQL);

        insertPs.setString(1, product.getPName());
        insertPs.setString(2, product.getPSize());
        insertPs.setString(3, product.getSID());
        insertPs.setInt(4, product.getPQuantity());
        insertPs.setDouble(5, product.getPBuyingPrice());
        insertPs.setDouble(6, product.getPPrice());
        insertPs.setDouble(7, product.getPDiscount());
        insertPs.setString(8, product.getPCategory());
        int rowsAffected = insertPs.executeUpdate();

        if (rowsAffected > 0) {
            new Alert(Alert.AlertType.INFORMATION, "Product Added successfully!").show();
              loadProductData();

            txtProductName.clear();
            txtProductSize.clear();
            txtProductQty.clear();
            txtProductBPrice.clear();
            txtProductSPrice.clear();
            txtProductDiscount.clear();
            txtProductSearchByID.clear();
            txtProductCategory.clear();

        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to add the Product. Please try again.").show();
        }

    }

    @FXML
    public void btnUpdateProductOnAction(ActionEvent actionEvent) {
        String productID = txtProductSearchByID.getText();
        String updatedName = txtProductName.getText();
        String updatedSize = txtProductSize.getText();
        String updatedSupplierName = (String) cmbProductSupplier.getValue(); // Supplier name
        int updatedQty = Integer.parseInt(txtProductQty.getText());
        double updatedBPrice = Double.parseDouble(txtProductBPrice.getText());
        double updatedSPrice = Double.parseDouble(txtProductSPrice.getText());
        double updatedDiscount = Double.parseDouble(txtProductDiscount.getText());
        String updatedCategory = txtProductCategory.getText();

        if (productID == null || productID.trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid Product ID to update.").show();
        } else if (updatedName.trim().isEmpty() || updatedSize.trim().isEmpty() || updatedSupplierName.trim().isEmpty() ||
                updatedCategory.trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all fields before updating.").show();
        } else {
            try {
                // Establish database connection
                Connection connection = DBConnection.getInstance().getConnection();

                // Fetch the sID from the Supplier table based on the supplier name
                String supplierQuery = "SELECT sID FROM Supplier WHERE sName = ?";
                PreparedStatement supplierPs = connection.prepareStatement(supplierQuery);
                supplierPs.setString(1, updatedSupplierName);
                ResultSet supplierResultSet = supplierPs.executeQuery();

                String sID = null;
                if (supplierResultSet.next()) {
                    sID = supplierResultSet.getString("sID");
                } else {
                    new Alert(Alert.AlertType.ERROR, "Supplier not found.").show();
                    return;
                }

                // Prepare the update query for the Product table
                String updateQuery = "UPDATE Product SET pName = ?, pSize = ?, sID = ?, pQuantity = ?, pBuyingPrice = ?, " +
                        "pPrice = ?, pDiscount = ?, pCategory = ? WHERE pID = ?";
                PreparedStatement ps = connection.prepareStatement(updateQuery);

                // Set the parameters
                ps.setString(1, updatedName);
                ps.setString(2, updatedSize);
                ps.setString(3, sID); // Supplier ID
                ps.setInt(4, updatedQty);
                ps.setDouble(5, updatedBPrice);
                ps.setDouble(6, updatedSPrice);
                ps.setDouble(7, updatedDiscount);
                ps.setString(8, updatedCategory);
                ps.setString(9, productID); // Product ID to update

                // Execute the update query
                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    new Alert(Alert.AlertType.INFORMATION, "Product details updated successfully!").show();

                    // Reload data if necessary
                    loadProductData();
                } else {
                    new Alert(Alert.AlertType.WARNING, "No product found with the provided ID.").show();
                }

            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "An error occurred while updating product details.").show();
            }
        }

    }

    @FXML
    public void btnDeleteProductOnAction(ActionEvent actionEvent) {
        String productID = txtProductSearchByID.getText();

        if (productID == null || productID.trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid product ID to delete.").show();
        }else {

            try {
                Connection connection = DBConnection.getInstance().getConnection();

                String deleteQuery = "DELETE FROM Product WHERE pID = ?";
                PreparedStatement ps = connection.prepareStatement(deleteQuery);

                ps.setString(1, productID);

                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    new Alert(Alert.AlertType.INFORMATION, "Product details delete successfully!").show();

                    loadProductData();

                } else {
                    new Alert(Alert.AlertType.WARNING, "No product found with the provided ID.").show();
                }

            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "An error occurred while deleting product details.").show();
            }
        }
    }

    @FXML
    public void btnSearchProductOnAction(ActionEvent actionEvent) {
        String productID = txtProductSearchByID.getText();

        if(productID == null || productID.trim().isEmpty()){
            new Alert(Alert.AlertType.ERROR, "Please enter a valid product ID.").show();
        }else {
            try {
                Connection connection = DBConnection.getInstance().getConnection();

                // Updated query to fetch supplier name with product details
                String searchSQL = "SELECT p.*, s.sName FROM Product p JOIN Supplier s ON p.sID = s.sID WHERE p.pID = ?";
                PreparedStatement ps = connection.prepareStatement(searchSQL);
                ps.setString(1, productID);

                ResultSet resultSet = ps.executeQuery();

                if (resultSet.next()) {
                    txtProductName.setText(resultSet.getString("pName"));
                    txtProductSize.setText(resultSet.getString("pSize"));
                    cmbProductSupplier.setValue(resultSet.getString("sName"));
                    txtProductQty.setText(String.valueOf(resultSet.getInt("pQuantity")));
                    txtProductBPrice.setText(String.valueOf(resultSet.getDouble("pBuyingPrice")));
                    txtProductSPrice.setText(String.valueOf(resultSet.getDouble("pPrice")));
                    txtProductDiscount.setText(String.valueOf(resultSet.getDouble("pDiscount")));
                    txtProductCategory.setText(resultSet.getString("pCategory"));
                } else {
                    // If no product is found, show a warning and clear fields
                    new Alert(Alert.AlertType.WARNING, "No product found with the provided ID.").show();

                    // Clear the text fields and ComboBox
                    txtProductName.clear();
                    txtProductSize.clear();
                    cmbProductSupplier.setValue(null);
                    txtProductQty.clear();
                    txtProductBPrice.clear();
                    txtProductSPrice.clear();
                    txtProductDiscount.clear();
                    txtProductCategory.clear();
                }

            } catch (SQLException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "An error occurred while searching for the supplier.").show();
            }
        }
    }
}
