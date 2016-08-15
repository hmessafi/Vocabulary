package pl.nikowis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nikowis.entities.Role;

/**
 * Repository for roles.
 * Created by nikowis on 2016-08-15.
 *
 * @author nikowis
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findOneByName(String name);
}
