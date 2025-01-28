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
    private Integer pID;
    private String customerName;
    private String itemName;
    private Integer qty;
    private Double unitPrice;
    private Double discount;
    private Double totalPrice;
}
