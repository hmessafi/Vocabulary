package pl.nikowis.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.springframework.beans.factory.annotation.Autowired;
import pl.nikowis.entities.User;
import pl.nikowis.services.SessionService;

/**
 * Created by nikowis on 2016-08-01.
 */
@SpringView
public class LoginView extends CustomComponent implements View{

    public static final String VIEW_NAME = "login";

    @Autowired
    private SessionService sessionService;

    private final TextField usernameField;

    private final PasswordField passwordField;

    private final Button loginButton;

    public LoginView() {
        setSizeFull();

        usernameField = new TextField("User:");
        usernameField.setWidth("300px");
        usernameField.setRequired(true);

        passwordField = new PasswordField("Password:");
        passwordField.setWidth("300px");
        passwordField.setRequired(true);
        passwordField.setValue("");
        passwordField.setNullRepresentation("");

        loginButton = new Button("Login");

        loginButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                String username = usernameField.getValue();
                String password = passwordField.getValue();

                boolean isValid = username.equals("user")
                        && password.equals("1");

                if (isValid) {
                    sessionService.setUser(new User(username, password));
                    getUI().getNavigator().navigateTo(HomeView.VIEW_NAME);
                } else {
                    passwordField.setValue(null);
                    passwordField.focus();
                }
            }
        });

        VerticalLayout fields = new VerticalLayout(usernameField, passwordField, loginButton);
        fields.setCaption("Login to access the application.");
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, false));
        fields.setSizeUndefined();

        VerticalLayout viewLayout = new VerticalLayout(fields);
        viewLayout.setSizeFull();
        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        setCompositionRoot(viewLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        usernameField.focus();
    }

}
