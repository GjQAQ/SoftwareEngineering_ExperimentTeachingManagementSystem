package team.segroup.etms.usersys.controller;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.segroup.etms.usersys.dto.UncheckedUserDto;
import team.segroup.etms.usersys.dto.UserDto;
import team.segroup.etms.usersys.entity.UncheckedUser;
import team.segroup.etms.usersys.entity.User;
import team.segroup.etms.usersys.service.UserService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

    // note: accept Content-Type:application/json only
    @PutMapping("/{nid}")
    public ResponseEntity<UserDto> updateUser(
        @PathVariable("nid") String nid,
        @RequestBody UserDto newUser) {
        boolean result = userService.updateUser(newUser);
        if (result) {
            return ResponseEntity.ok(newUser);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{nid}")
    public ResponseEntity<UserDto> removeUser(@PathVariable("nid") String nid) {
        User user = userService.retrieveUser(nid);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        userService.removeUser(nid);
        return ResponseEntity.ok(new UserDto(user));
    }

    @PostMapping("/{nid}/validate")
    public ResponseEntity<UserDto> validateUser(@PathVariable("nid") String nid) {
        User validUser = userService.validateUser(nid, true);
        if (validUser != null) {
            return ResponseEntity.ok(new UserDto(validUser));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{nid}/validate")
    public ResponseEntity<String> invalidateUser(@PathVariable("nid") String nid) {
        userService.validateUser(nid, false);
        return ResponseEntity.ok("ok");
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

    @PostMapping("/batch")
    public ResponseEntity<BatchRegistryResponse> batchRegister(
        @RequestPart("csv") MultipartFile csv,
        @RequestParam("checked") boolean checked
    ) {
        Iterable<CSVRecord> records;
        try {
            InputStreamReader reader = new InputStreamReader(csv.getInputStream());
            records = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .parse(reader);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

        Stream<UncheckedUserDto> newUsers = StreamSupport
            .stream(records.spliterator(), false)
            .map((record) -> new UncheckedUserDto(
                record.get("nid"),
                record.get("name"),
                record.get("password"),
                record.get("email")
            ));
        List<String>[] res = userService.registerBatch(newUsers, checked);

        BatchRegistryResponse response = new BatchRegistryResponse();
        response.put("pass", res[0]);
        response.put("fail", res[1]);
        return ResponseEntity.ok(response);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private static class BatchRegistryResponse extends HashMap<String, List<String>> {
    }
}
