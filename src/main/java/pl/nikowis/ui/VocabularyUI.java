package pl.nikowis.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import pl.nikowis.services.SessionService;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
@SpringUI
public class VocabularyUI extends UI {

    @Autowired
    private SpringViewProvider viewProvider;

    @Autowired
    private SessionService sessionService;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        this.setNavigator(new Navigator(this, this));
        getNavigator().addProvider(viewProvider);

        if(sessionService.getUser() != null) {
            getNavigator().navigateTo(HomeView.VIEW_NAME);
        } else {
            getNavigator().navigateTo(LoginView.VIEW_NAME);
        }

    }
}
