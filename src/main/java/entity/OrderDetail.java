package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "OrderDetail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detailID")
    private Integer detailID;

    @ManyToOne
    @JoinColumn(name = "orderID", nullable = false)
    private Order order;

    @Column(name = "productID", nullable = false)
    private Integer productID;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unitPrice", nullable = false)
    private Double unitPrice;

    @Column(name = "totalPrice", nullable = false)
    private Double totalPrice;
}
