package team.segroup.etms.usersys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.segroup.etms.usersys.entity.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String nid;
    private String name;
    private String email;
    private boolean active;
    private boolean admin;

    public UserDto(User user) {
        this.nid = user.getNid();
        this.name = user.getUsername();
        this.email = user.getEmail();
        this.active = user.isActive();
        this.admin = user.isAdmin();
    }

    public User toUser() {
        User user = new User();
        coverUser(user);
        return user;
    }

    public void coverUser(User user) {
        user.setNid(nid);
        user.setUsername(name);
        user.setEmail(email);
        user.setActive(active);
        user.setAdmin(admin);
    }
}
