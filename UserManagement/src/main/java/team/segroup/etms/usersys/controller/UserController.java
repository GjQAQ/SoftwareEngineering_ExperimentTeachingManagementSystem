package team.segroup.etms.usersys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.segroup.etms.usersys.dto.UncheckedUserDto;
import team.segroup.etms.usersys.dto.UserDto;
import team.segroup.etms.usersys.entity.UncheckedUser;
import team.segroup.etms.usersys.entity.User;
import team.segroup.etms.usersys.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @GetMapping("/{nid}")
    public ResponseEntity<UserDto> retrieveUser(@PathVariable("nid") String nid) {
        User user = userService.retrieveUser(nid);
        if (user == null) {
            return ResponseEntity.notFound().build();
        } else {
            user.setPassword("");
            return ResponseEntity.ok(new UserDto(user));
        }
    }

    // TODO: return value
    @PostMapping("/{nid}/validate")
    public ResponseEntity<User> validateUser(@PathVariable("nid") String nid) {
        boolean valid = userService.validateUser(nid, true);
        if (valid) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{nid}/validate")
    public ResponseEntity<String> invalidateUser(@PathVariable("nid") String nid) {
        boolean result = userService.validateUser(nid, false);
        if (result) {
            return ResponseEntity.ok("ok");
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{nid}/activate")
    public ResponseEntity<User> activateUser(@PathVariable("nid") String nid) {
        boolean valid = userService.activateUser(nid);
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
