package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product {

    private Long pID;
    private String pName;
    private String pSize;
    private String pSupplier;
    private Integer pQuantity;
    private Double pBuyingPrice;
    private Double pPrice;
    private Double pDiscount;
    private Double pProfit;
    private String pCategory;

}
