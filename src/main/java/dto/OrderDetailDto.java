package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDetailDto {
    private Integer detailID; // Unique identifier for the order detail
    private Integer orderID; // ID of the associated order
    private Integer productID; // ID of the product
    private Integer quantity; // Quantity of the product ordered
    private Double unitPrice; // Unit price of the product
    private Double totalPrice; // Total price for this detail
}
