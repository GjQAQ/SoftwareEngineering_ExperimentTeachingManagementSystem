package team.segroup.etms.usersys.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.segroup.etms.usersys.dto.UncheckedUserDto;
import team.segroup.etms.usersys.entity.UncheckedUser;
import team.segroup.etms.usersys.entity.User;
import team.segroup.etms.usersys.repository.UncheckedUserRepository;
import team.segroup.etms.usersys.repository.UserRepository;
import team.segroup.etms.usersys.service.UserService;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private UncheckedUserRepository uncUserRepository;

    private MessageDigest sha1;
    private final Logger logger = LoggerFactory.getLogger(getClass());

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

    @Override
    public UncheckedUser register(UncheckedUserDto userDto) {
        // TODO:verify unchecked user

        Optional<UncheckedUser> origin = uncUserRepository.findByUid(userDto.getUid());
        if (origin.isPresent()) {
            return null;
        }

        UncheckedUser uncheckedUser = new UncheckedUser(
            userDto.getUid(),
            userDto.getName(),
            hash(userDto.getPassword()),
            userDto.getEmail()
        );
        return uncUserRepository.save(uncheckedUser);
    }

    @Transactional
    @Override
    public boolean validateUser(int uid, boolean valid) {
        Optional<UncheckedUser> newUser = uncUserRepository.findByUid(uid);
        if (!newUser.isPresent()) {
            logger.warn("Unchecked user with uid=" + uid + " does not exist but was tried to validate.");
            return false;
        }

        uncUserRepository.deleteByUid(uid);
        User user = new User(newUser.get(), false);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean activateUser(int uid) {
        // TODO:improve
        Optional<User> userOptional = userRepository.findByUid(uid);
        if (!userOptional.isPresent()) {
            logger.warn("User with uid=" + uid + " does not exist but was tried to activate.");
            return false;
        }

        User user = userOptional.get();
        user.setActive(true);
        userRepository.save(user);
        return true;
    }

    @Override
    public User retrieveUser(int uid) {
        Optional<User> user = userRepository.findByUid(uid);
        if (!user.isPresent()) {
            logger.warn("User with uid=" + uid + " does not exist but was retrieved.");
            return null;
        }

        return user.get();
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setUncUserRepository(UncheckedUserRepository uncUserRepository) {
        this.uncUserRepository = uncUserRepository;
    }

    private String hash(String input) {
        byte[] digest = sha1.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder builder = new StringBuilder();
        for (byte b : digest) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
