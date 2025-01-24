package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pID;

    @Column(nullable = false)
    private String pName;

    @Column(nullable = false)
    private String pSize;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sID", nullable = false)
    private Supplier supplier;

    @Column(nullable = false)
    private Integer pQuantity;

    @Column(nullable = false)
    private Double pBuyingPrice;

    @Column(nullable = false)
    private Double pPrice;

    @Column(nullable = false)
    private Double pDiscount;

    @Column(nullable = false)
    private Double pProfit;

    @Column(nullable = false)
    private String pCategory;
}
