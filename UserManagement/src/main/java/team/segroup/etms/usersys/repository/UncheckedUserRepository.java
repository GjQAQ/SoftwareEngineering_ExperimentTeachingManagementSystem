package team.segroup.etms.usersys.repository;

import org.springframework.data.repository.CrudRepository;
import team.segroup.etms.usersys.entity.UncheckedUser;

import java.util.Optional;

public interface UncheckedUserRepository extends CrudRepository<UncheckedUser,Integer> {
    Optional<UncheckedUser> findByUid(int uid);
    Optional<UncheckedUser> findByNid(String nid);

    void deleteByUid(int uid);
    void deleteByNid(String nid);
}
