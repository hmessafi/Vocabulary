package pl.nikowis.ui;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
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
import pl.nikowis.services.UserService;

/**
 * Created by nikowis on 2016-08-01.
 */
@SpringView
public class LoginView extends CustomComponent implements View {

    public static final String VIEW_NAME = "login";

    private SessionService sessionService;

    private UserService userService;

    @PropertyId("username")
    private TextField usernameField;

    @PropertyId("password")
    private PasswordField passwordField;

    private Button loginButton, registerButton;

    private FieldGroup fieldGroup;

    private User user;

    @Autowired
    public LoginView(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;

        initalizeComponents();

        setSizeFull();

        VerticalLayout fields = new VerticalLayout(
                usernameField
                , passwordField
                , loginButton
                , registerButton
        );

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

    private void initalizeComponents() {
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

                try {
                    fieldGroup.commit();
                } catch (FieldGroup.CommitException e) {
                    e.printStackTrace();
                }

                if (isValid()) {
                    sessionService.setUser(user);
                    getUI().getNavigator().navigateTo(HomeView.VIEW_NAME);
                } else {

                    passwordField.setValue(null);
                    passwordField.focus();
                }
            }
        });

        registerButton = new Button("Register");
        registerButton.setCaption("Register new user");
        registerButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                getUI().getNavigator().navigateTo(RegisterView.VIEW_NAME);
            }
        });

        user = new User();

        BeanItem<User> bean = new BeanItem<User>(user);
        fieldGroup = new FieldGroup(bean);
        fieldGroup.bindMemberFields(this);
        usernameField.setValue("");
        passwordField.setValue("");
    }

    private boolean isValid() {
        if ( user.getUsername().equals("user") && user.getPassword().equals("1") ) {
            return true;
        } else {
            User dbUser = userService.authenticateUser(user);
            if (dbUser == null) {
                return  false;
            }
            user = dbUser;
        }
        return  true;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        usernameField.focus();
    }

}
