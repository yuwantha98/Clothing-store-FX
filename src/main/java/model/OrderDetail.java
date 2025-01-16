package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDetail {

    private Long pID;
    private Long oID;
    private String pName;
    private int quantity;
    private double pPrice;
    private double discount;

}
