package pl.nikowis.services;

import pl.nikowis.entities.User;

/**
 * Service for saving and retreiving the user from the session.
 * Created by nikowis on 2016-08-05.
 */
public interface SessionService {
    /**
     * Gets the logged user from the session.
     * @return user or null.
     */
    User getUser();

    /**
     * Sets the user as a session parameter.
     * @param user user to set
     */
    void setUser(User user);

    /**
     * Deletes the user parameter from the session.
     * @return user from the session parameter or null.
     */
    User eraseUser();

    /**
     * Checks if the user in the session has the specified role.
     * Use UserRoles class for getting role names.
     * @param roleName name of the role
     * @return result or null if user is not logged in
     */
    boolean hasRole(String roleName);
}
