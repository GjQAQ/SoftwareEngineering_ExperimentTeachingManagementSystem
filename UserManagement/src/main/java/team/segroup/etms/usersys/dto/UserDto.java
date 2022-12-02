package team.segroup.etms.usersys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.segroup.etms.usersys.entity.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    public UserDto(User user) {
        this.nid = user.getNid();
        this.name = user.getUsername();
        this.email = user.getEmail();
        this.active = user.isActive();
    }

    private String nid;
    private String name;
    private String email;
    private boolean active;
}
