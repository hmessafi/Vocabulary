package pl.nikowis.services;

import pl.nikowis.entities.User;

/**
 * Service for users.
 * Created by nikowis on 2016-08-11.
 *
 * @author nikowis
 */
public interface UserService {

    /**
     * Saves the user to the database.
     * @param user user to save
     * @return saved user.
     */
    User saveUser(User user);

    /**
     * Checks if this user matches a user from the database.
     * @param user user to check
     * @return the user from the database or null
     */
    User authenticateUser(User user);

    /**
     * Creates an user, saves it to the database and assigns user role.
     * @param user user to create
     * @return saved user
     */
    User createNewUser(User user);

    /**
     * Creates an user, saves it to the database and assigns admin role.
     * @param user admin to create
     * @return saved admin
     */
    User createNewAdmin(User user);
}
