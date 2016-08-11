package pl.nikowis.services;

import pl.nikowis.entities.User;

/**
 * Created by nikowis on 2016-08-11.
 *
 * @author nikowis
 */
public interface UserService {

    User saveUser(User user);

    User authenticateUser(User user);

    User createUser(User user);
}
