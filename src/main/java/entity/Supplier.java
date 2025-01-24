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
@Table(name = "Supplier")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sID")
    private Integer sID;

    @Column(name = "sName", nullable = false)
    private String sName;

    @Column(name = "sCompany", nullable = false)
    private String sCompany;

    @Column(name = "sContact", nullable = false)
    private String sContact;

}
