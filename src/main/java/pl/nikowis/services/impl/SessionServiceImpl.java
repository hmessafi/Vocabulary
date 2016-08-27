package pl.nikowis.services.impl;


import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import com.vaadin.server.VaadinService;
import com.vaadin.server.WrappedSession;
import org.springframework.stereotype.Service;
import pl.nikowis.entities.User;
import pl.nikowis.services.SessionService;

/**
 * Created by nikowis on 2016-08-05.
 */
@Service
public class SessionServiceImpl implements SessionService {
    private static final String USER_SESSION_ATTR_NAME = "user";

    @Override
    public User getUser() {
        WrappedSession session = VaadinService.getCurrentRequest().getWrappedSession();
        return (User) session.getAttribute(USER_SESSION_ATTR_NAME);
    }

    @Override
    public void setUser(User user) {
        Preconditions.checkNotNull(user);
        WrappedSession session = VaadinService.getCurrentRequest().getWrappedSession();
        session.setAttribute(USER_SESSION_ATTR_NAME, user);
    }

    @Override
    public User eraseUser() {
        User user = getUser();
        WrappedSession session = VaadinService.getCurrentRequest().getWrappedSession();
        session.setAttribute(USER_SESSION_ATTR_NAME, null);
        return user;
    }
}
