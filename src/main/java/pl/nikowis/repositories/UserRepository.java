package pl.nikowis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nikowis.entities.User;

/**
 * Repository for users.
 * Created by nikowis on 2016-08-11.
 *
 * @author nikowis
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findOneByUsername(String username);
}
