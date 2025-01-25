package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import dto.tm.OrderTm;
import entity.Order;
import entity.OrderDetail;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import repository.DaoFactory;
import repository.service.EmployeeDao;
import repository.service.OrderDao;
import repository.service.OrderDetailDao;
import repository.service.ProductDao;
import util.EmailUtil;

import javax.mail.MessagingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class orderFormController {

    @FXML
    public TableView tblOrder;

    @FXML
    public TableColumn colProductID;

    @FXML
    public TableColumn colCustomerName;

    @FXML
    public TableColumn colProductName;

    @FXML
    public TableColumn colQty;

    @FXML
    public TableColumn colUnitPrice;

    @FXML
    public TableColumn colDiscount;

    @FXML
    public TableColumn colTotalPrice;

    @FXML
    public JFXTextField txtCustomerName;

    @FXML
    public JFXTextField txtCustomerContact;

    @FXML
    public JFXTextField txtCustomerEmail;

    @FXML
    public JFXTextField txtProductName;

    @FXML
    public JFXTextField txtProductDiscount;

    @FXML
    public JFXTextField txtProductQty;

    @FXML
    public JFXTextField txtBuyingQty;

    @FXML
    public JFXComboBox cmbProductID;

    @FXML
    public JFXComboBox cmbEmployeeID;

    @FXML
    public JFXTextField txtProductPrice;

    ObservableList<OrderTm> orderList = FXCollections.observableArrayList();
    OrderDao orderDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.ORDER);
    OrderDetailDao orderDetailDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.ORDERDETAIL);
    ProductDao productDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.PRODUCT);
    EmployeeDao employeeDao = DaoFactory.getDaoFactory().getDaoType(DaoFactory.DaoType.EMPLOYEE);

    public void initialize() {
        colProductID.setCellValueFactory(new PropertyValueFactory<>("pID"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        tblOrder.setItems(orderList);

        loadProductIDs();
        loadEmployeeIDs();

        cmbProductID.setOnAction(event -> loadProductDetails());

        txtProductName.setEditable(false);
        txtProductQty.setEditable(false);
        txtProductDiscount.setEditable(false);
        txtProductPrice.setEditable(false);
    }

    private void loadProductIDs() {
        try {
            cmbProductID.setItems(productDao.getAllProductIDs());
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading product IDs: " + e.getClass()).show();
        }
    }

    private void loadEmployeeIDs() {
        try {
            cmbEmployeeID.setItems(employeeDao.getAllEmployeeIDs());
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading employee IDs: " + e.getMessage()).show();
        }
    }

    private void loadProductDetails() {
        try {
            Integer productId = (Integer) cmbProductID.getValue();
            if (productId == null) return;

            var product = productDao.find(productId);
            if (product != null) {
                txtProductName.setText(product.getPName());
                txtProductQty.setText(String.valueOf(product.getPQuantity()));
                txtProductDiscount.setText(String.valueOf(product.getPDiscount()));
                txtProductPrice.setText(String.valueOf(product.getPPrice()));
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error loading product details: " + e.getMessage()).show();
        }
    }

    @FXML
    public void btnAddToCartOnAction(ActionEvent actionEvent) {
        try {
            Integer productId = (Integer) cmbProductID.getValue();
            String productName = txtProductName.getText();
            String customerName = txtCustomerName.getText();
            Integer availableQty = Integer.parseInt(txtProductQty.getText());
            Integer buyingQty = Integer.parseInt(txtBuyingQty.getText());
            Double unitPrice = Double.parseDouble(txtProductPrice.getText());
            Double discount = Double.parseDouble(txtProductDiscount.getText());

            if (buyingQty > availableQty) {
                new Alert(Alert.AlertType.ERROR, "Not enough stock available!").show();
                return;
            }

            Double totalPrice = (unitPrice - (unitPrice * discount / 100)) * buyingQty;

            OrderTm orderTm = new OrderTm(
                    productId,
                    customerName,
                    productName,
                    buyingQty,
                    unitPrice,
                    discount,
                    totalPrice
            );

            orderList.add(orderTm);
            tblOrder.refresh();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error adding to cart: " + e.getMessage()).show();
        }
    }

    @FXML
    public void btnRemoveProductOnAction(ActionEvent actionEvent) {
        OrderTm selectedItem = (OrderTm) tblOrder.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            orderList.remove(selectedItem);
            tblOrder.refresh();
        } else {
            new Alert(Alert.AlertType.WARNING, "No product selected!").show();
        }
    }

    @FXML
    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
        try {
            // Validate inputs
            if (txtCustomerName.getText().isEmpty() || txtCustomerContact.getText().isEmpty() || orderList.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please fill all fields and add items to the cart!").show();
                return;
            }

            // Create Order
            Order order = new Order();
            order.setOrderDate(new Date());
            order.setCustomerName(txtCustomerName.getText());
            order.setTotalAmount(orderList.stream().mapToDouble(OrderTm::getTotalPrice).sum());

            List<OrderDetail> orderDetails = new ArrayList<>();
            for (OrderTm orderTm : orderList) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setProductID(orderTm.getPID());
                orderDetail.setQuantity(orderTm.getQty());
                orderDetail.setUnitPrice(orderTm.getUnitPrice());
                orderDetail.setTotalPrice(orderTm.getTotalPrice());
                orderDetails.add(orderDetail);
            }
            order.setOrderDetails(orderDetails);

            boolean isOrderSaved = orderDao.save(order);

            if (isOrderSaved) {
                for (OrderTm orderTm : orderList) {
                    Integer productID = orderTm.getPID();
                    Integer buyingQty = orderTm.getQty();

                    Integer currentQuantity = productDao.getProductQuantity(productID);
                    if (currentQuantity != null) {
                        int newQuantity = currentQuantity - buyingQty;

                        // Update the product quantity in the database
                        productDao.updateProductQuantity(productID, newQuantity);
                    }
                }

                // Send order details to customer's email
                String customerEmail = txtCustomerEmail.getText();
                String emailSubject = "Order Confirmation - Order #" + order.getOrderID();
                StringBuilder emailBody = new StringBuilder();
                emailBody.append("Dear ").append(txtCustomerName.getText()).append(",\n\n");
                emailBody.append("Thank you for your order. Here are the details:\n");
                emailBody.append("Order Date: ").append(order.getOrderDate()).append("\n");
                emailBody.append("Order Details:\n");
                for (OrderTm orderTm : orderList) {
                    emailBody.append("- Product: ").append(orderTm.getItemName())
                            .append(", Quantity: ").append(orderTm.getQty())
                            .append(", Total Price: ").append(orderTm.getTotalPrice()).append("\n");
                }
                emailBody.append("\nTotal Amount: ").append(order.getTotalAmount()).append("\n");
                emailBody.append("\nWe appreciate your business!\n\nBest regards,\nDulmark Clothing");

                try {
                    EmailUtil.sendEmail(customerEmail, emailSubject, emailBody.toString());
                    new Alert(Alert.AlertType.INFORMATION, "Order placed and email sent successfully!").show();
                    clearForm();
                } catch (MessagingException e) {
                    new Alert(Alert.AlertType.WARNING, "Order placed, but failed to send email: " + e.getMessage()).show();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to place the order!").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error placing order: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    private void clearForm() {
        cmbProductID.setValue(null);
        cmbEmployeeID.setValue(null);
        txtCustomerName.clear();
        txtCustomerContact.clear();
        txtCustomerEmail.clear();
        txtProductName.clear();
        txtProductQty.clear();
        txtProductDiscount.clear();
        txtBuyingQty.clear();
        txtProductPrice.clear();
        orderList.clear();
        tblOrder.refresh();
    }
}
