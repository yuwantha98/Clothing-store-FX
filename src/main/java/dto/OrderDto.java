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
    private Integer orderID;
    private Date orderDate;
    private String customerName;
    private Double totalAmount;
    private List<OrderDetailDto> orderDetails;
}
