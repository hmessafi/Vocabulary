package pl.nikowis.services;

import pl.nikowis.entities.User;

/**
 * Created by nikowis on 2016-08-05.
 */
public interface SessionService {
    User getUser();
    void setUser(User user);
    User eraseUser();
}
