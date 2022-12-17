package team.segroup.etms.usersys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.segroup.etms.usersys.entity.UncheckedUser;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UncheckedUserDto {
    private String nid;
    private String name;
    private String password;
    private String email;

    public UncheckedUserDto(UncheckedUser uncheckedUser) {
        nid = uncheckedUser.getNid();
        name = uncheckedUser.getUsername();
        password = uncheckedUser.getPassword();
        email = uncheckedUser.getEmail();
    }
}
