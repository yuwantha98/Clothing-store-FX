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
@Table(name = "Employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eID")
    private Integer eID;

    @Column(name = "eName", nullable = false)
    private String eName;

    @Column(name = "eContact", nullable = false)
    private String eContact;

    @Column(name = "eEmail", unique = true, nullable = false)
    private String eEmail;

    @Column(name = "ePassword", nullable = false)
    private String ePassword;
}
