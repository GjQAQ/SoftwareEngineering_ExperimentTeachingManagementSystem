package team.segroup.etms.usersys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.segroup.etms.usersys.dto.UncheckedUserDto;
import team.segroup.etms.usersys.entity.UncheckedUser;
import team.segroup.etms.usersys.entity.User;
import team.segroup.etms.usersys.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @GetMapping("/{uid}")
    public ResponseEntity<User> retrieveUser(@PathVariable("uid") int uid) {
        User user = userService.retrieveUser(uid);
        if (user == null) {
            return ResponseEntity.notFound().build();
        } else {
            user.setPassword("");
            return ResponseEntity.ok(user);
        }
    }

    @PostMapping("/{uid}/validate")
    public ResponseEntity<User> validateUser(@PathVariable("uid") int uid) {
        boolean valid = userService.validateUser(uid, true);
        if (valid) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{uid}/activate")
    public ResponseEntity<User> activateUser(@PathVariable("uid") int uid) {
        boolean valid = userService.activateUser(uid);
        if (valid) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // note: accept Content-Type:application/json only
    @PostMapping
    public ResponseEntity<UncheckedUser> registerNewUser(@RequestBody UncheckedUserDto userDto) {
        UncheckedUser registered = userService.register(userDto);
        if (registered == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(registered);
        }
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
