package pl.nikowis.services;

import pl.nikowis.entities.Role;

/**
 * Service for roles.
 * Created by nikowis on 2016-08-15.
 *
 * @author nikowis
 */
public interface RoleService {
    /**
     * Saves the role to the database.
     * @param role role to save
     * @return saved role
     */
    Role save(Role role);

    /**
     * Finds role in database.
     * @param name name of the role from {@link pl.nikowis.security.UserRoles}.
     * @return role from database.
     */
    Role findOneByName(String name);
}
