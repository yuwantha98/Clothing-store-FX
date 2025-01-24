package dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderTm {
    private Integer pID;         // Product ID
    private String itemName;     // Product Name
    private String customerName;
    private Integer qty;         // Quantity
    private Double unitPrice;    // Unit price
    private Double discount;     // Discount (percentage)
    private Double totalPrice;   // Total price
}
