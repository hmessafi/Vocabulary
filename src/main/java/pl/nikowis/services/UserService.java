package pl.nikowis.services;

import pl.nikowis.entities.User;
import pl.nikowis.exceptions.WrongPasswordException;

import java.util.List;

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
    User saveUserWithNewPassword(User user);

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

    /**
     * Gets all users sorted by username.
     * @return user list
     */
    List<User> getAll();

    /**
     * Sets the user stuatus to disabled, and changes his password.
     * @param user user to delete
     */
    void deleteUser(User user);

    /**
     * Checks if the user and the password match.
     * @param user user to check
     * @param password password to check
     */
    void authenticatePassword(User user, String password) throws WrongPasswordException;
}
