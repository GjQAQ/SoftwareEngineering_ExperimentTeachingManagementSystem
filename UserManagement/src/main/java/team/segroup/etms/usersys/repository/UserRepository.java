package team.segroup.etms.usersys.repository;

import org.springframework.data.repository.CrudRepository;
import team.segroup.etms.usersys.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByNidAndPassword(String nid, String password);
    Optional<User> findByUid(int uid);
    Optional<User> findByNid(String nid);
    List<User> findAll();

    void deleteByUid(int uid);
    void deleteByNid(String nid);
}
