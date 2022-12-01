package team.segroup.etms.usersys.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    public User(UncheckedUser from, boolean active) {
        this.uid = from.getUid();
        this.username = from.getUsername();
        this.password = from.getPassword();
        this.email = from.getEmail();
        this.active = active;
    }

    @Id
    private int uid;
    private String username;
    private String password;
    private String email;
    private boolean active;
}
