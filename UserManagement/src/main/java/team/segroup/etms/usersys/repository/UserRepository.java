package team.segroup.etms.usersys.repository;

import org.springframework.data.repository.CrudRepository;
import team.segroup.etms.usersys.entity.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUidAndPassword(int uid, String password);
}