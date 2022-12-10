package team.segroup.etms.usersys.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    public User(UncheckedUser from, boolean active) {
        this.uid = from.getUid();
        this.nid = from.getNid();
        this.username = from.getUsername();
        this.password = from.getPassword();
        this.email = from.getEmail();
        this.active = active;
        this.admin = false;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uid;
    private String nid;
    private String username;
    private String password;
    private String email;
    private boolean active;
    private boolean admin;
}
