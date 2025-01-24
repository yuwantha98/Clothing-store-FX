package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDto {
    private Integer orderID; // Unique identifier for the order
    private Date orderDate; // Date of the order
    private String customerName; // ID of the customer who placed the order
    private Double totalAmount; // Total price of the order
    private List<OrderDetailDto> orderDetails; // List of associated order details
}
