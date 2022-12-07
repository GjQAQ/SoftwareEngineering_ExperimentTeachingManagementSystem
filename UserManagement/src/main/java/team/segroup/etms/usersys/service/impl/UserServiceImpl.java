package team.segroup.etms.usersys.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.segroup.etms.usersys.dto.UncheckedUserDto;
import team.segroup.etms.usersys.dto.UserDto;
import team.segroup.etms.usersys.entity.UncheckedUser;
import team.segroup.etms.usersys.entity.User;
import team.segroup.etms.usersys.repository.UncheckedUserRepository;
import team.segroup.etms.usersys.repository.UserRepository;
import team.segroup.etms.usersys.service.UserService;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
    public boolean verify(String nid, String password) {
        password = hash(password);
        Optional<User> user = userRepository.findByNidAndPassword(nid, password);
        return user.isPresent();
    }

    @Override
    public UncheckedUser register(UncheckedUserDto userDto) {
        // TODO:verify unchecked user

        Optional<UncheckedUser> origin = uncUserRepository.findByNid(userDto.getNid());
        if (origin.isPresent()) {
            return null;
        }

        UncheckedUser uncheckedUser = new UncheckedUser(
            null,
            userDto.getNid(),
            userDto.getName(),
            hash(userDto.getPassword()),
            userDto.getEmail()
        );
        return uncUserRepository.save(uncheckedUser);
    }

    @Transactional
    @Override
    public List<String>[] registerBatch(
        Stream<UncheckedUserDto> users,
        boolean checked
    ) {
        List<String> ok = new LinkedList<>();
        List<String> fail = new LinkedList<>();
        users.forEach(newUser -> {
            UncheckedUser registered = register(newUser);
            if (registered == null) {
                fail.add(newUser.getNid());
            } else {
                ok.add(registered.getNid());
            }
        });

        List<String>[] res = (List<String>[]) Array.newInstance(List.class, 2);
        res[0] = ok;
        res[1] = fail;

        if (checked) {
            for (String nid : ok) {
                validateUser(nid, true);
            }
        }
        return res;
    }

    @Transactional
    @Override
    public User validateUser(String nid, boolean valid) {
        Optional<UncheckedUser> newUser = uncUserRepository.findByNid(nid);
        if (!newUser.isPresent()) {
            logger.warn("Unchecked user with nid=" + nid + " does not exist but was tried to validate.");
            return null;
        }

        uncUserRepository.deleteByNid(nid);
        if (valid) {
            User user = new User(newUser.get(), false);
            user.setUid(null);
            userRepository.save(user);
            return user;
        } else {
            return null;
        }
    }

    @Override
    public boolean activateUser(String nid) {
        // TODO:improve
        Optional<User> userOptional = userRepository.findByNid(nid);
        if (!userOptional.isPresent()) {
            logger.warn("User with nid=" + nid + " does not exist but was tried to activate.");
            return false;
        }

        User user = userOptional.get();
        user.setActive(true);
        userRepository.save(user);
        return true;
    }

    @Override
    public User retrieveUser(String nid) {
        Optional<User> user = userRepository.findByNid(nid);
        if (!user.isPresent()) {
            logger.warn("User with nid=" + nid + " does not exist but was retrieved.");
            return null;
        }

        return user.get();
    }

    @Override
    public boolean removeUser(String nid) {
        Optional<User> user = userRepository.findByNid(nid);
        if (!user.isPresent()) {
            logger.warn("User with nid=" + nid + " does not exist but was removed.");
            return false;
        }

        userRepository.deleteByNid(nid);
        return true;
    }

    @Override
    public boolean updateUser(UserDto userDto) {
        // TODO:optimize
        String nid = userDto.getNid();
        Optional<User> optionalUser = userRepository.findByNid(nid);
        if (!optionalUser.isPresent()) {
            return false;
        }

        User user = optionalUser.get();
        userDto.coverUser(user);
        userRepository.save(user);
        return true;
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
