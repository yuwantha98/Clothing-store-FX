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
    private Integer detailID;
    private Integer orderID;
    private Integer productID;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
}
