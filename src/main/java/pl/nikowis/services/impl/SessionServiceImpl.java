package pl.nikowis.services.impl;


import com.vaadin.server.VaadinService;
import com.vaadin.ui.CustomComponent;
import org.springframework.stereotype.Component;
import pl.nikowis.entities.User;
import pl.nikowis.services.SessionService;

/**
 * Created by nikowis on 2016-08-05.
 */
@Component
public class SessionServiceImpl extends CustomComponent implements SessionService {
    private static final String USER_SESSION_ATTR_NAME="user";

    @Override
    public User getUser() {
       return (User) VaadinService.getCurrentRequest().getWrappedSession().getAttribute(USER_SESSION_ATTR_NAME);
    }

    @Override
    public void setUser(User user) {
        VaadinService.getCurrentRequest().getWrappedSession().setAttribute(USER_SESSION_ATTR_NAME, user);
    }
}
