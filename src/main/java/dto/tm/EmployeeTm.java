package dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmployeeTm {
    private Integer eID;
    private String eName;
    private String eContact;
    private String eEmail;
    private String ePassword;
}
