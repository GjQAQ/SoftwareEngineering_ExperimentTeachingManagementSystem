package team.segroup.etms.usersys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UncheckedUserDto {
    private String nid;
    private String name;
    private String password;
    private String email;
}
