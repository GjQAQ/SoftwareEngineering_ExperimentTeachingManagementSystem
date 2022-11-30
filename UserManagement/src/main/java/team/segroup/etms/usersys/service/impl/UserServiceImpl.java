package team.segroup.etms.usersys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.segroup.etms.usersys.entity.User;
import team.segroup.etms.usersys.repository.UserRepository;
import team.segroup.etms.usersys.service.UserService;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private MessageDigest sha1;

    public UserServiceImpl() {
        try {
            sha1 = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean verify(int uid, String password) {
        password = hash(password);
        Optional<User> user = userRepository.findByUidAndPassword(uid, password);
        return user.isPresent();
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String hash(String input) {
        byte[] digest = sha1.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder builder = new StringBuilder();
        for (byte b : digest) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
