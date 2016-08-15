package pl.nikowis.services;

import pl.nikowis.entities.User;

/**
 * Service for users.
 * Created by nikowis on 2016-08-11.
 *
 * @author nikowis
 */
public interface UserService {

    User saveUser(User user);

    User authenticateUser(User user);

    User createNewUser(User user);

    User createNewAdmin(User user);
}
