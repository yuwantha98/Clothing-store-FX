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
@Table(name = "Employee") // Ensure the table name matches your database
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate IDs
    @Column(name = "eID") // Map to the column name in the database
    private Integer eID;

    @Column(name = "eName", nullable = false) // Explicit mapping for name
    private String eName;

    @Column(name = "eContact", nullable = false) // Explicit mapping for contact
    private String eContact;

    @Column(name = "eEmail", unique = true, nullable = false) // Explicit mapping for email
    private String eEmail;

    @Column(name = "ePassword", nullable = false) // Explicit mapping for password
    private String ePassword;
}
