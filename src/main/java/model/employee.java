package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class employee {

    private String eName;
    private String eContact;
    private String eEmail;
    private String eAddress;
    private String ePassword;


}
