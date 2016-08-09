package pl.nikowis.ui;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("valo")
@SpringUI
public class VocabularyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        this.setNavigator(new Navigator(this,this));
        getNavigator().addView(LoginView.NAME, LoginView.class);
        getNavigator().addView(HomeView.NAME, HomeView.class);
        getNavigator().navigateTo(LoginView.NAME);
    }
}
