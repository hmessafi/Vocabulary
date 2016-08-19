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
 * Login page.
 * Created by nikowis on 2016-08-01.
 */
@SpringView(name = LoginView.VIEW_NAME)
public class LoginView extends CustomComponent implements View {

    public static final String VIEW_NAME = "login";

    private SessionService sessionService;

    private UserService userService;

    private TextField username;

    private PasswordField password;

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
                username
                , password
                , loginButton
                , registerButton
        );

        fields.setCaption("Login to access the application.");
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, false));
        fields.setSizeUndefined();

        VerticalLayout mainLayout = new VerticalLayout(fields);
        mainLayout.setSizeFull();
        mainLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        mainLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        setCompositionRoot(mainLayout);
    }

    private void initalizeComponents() {
        username = new TextField("User:");
        username.setWidth("300px");
        username.setRequired(true);

        password = new PasswordField("Password:");
        password.setWidth("300px");
        password.setRequired(true);
        password.setValue("");
        password.setNullRepresentation("");

        loginButton = new Button("Login");
        loginButton.addClickListener(clickEvent -> commitAndAuthenticateUser());

        registerButton = new Button("Register");
        registerButton.setCaption("Register new user");
        registerButton.addClickListener(clickEvent ->  redirect(RegisterView.VIEW_NAME));

        user = new User();

        BeanItem<User> bean = new BeanItem<User>(user);
        fieldGroup = new FieldGroup(bean);
        fieldGroup.bindMemberFields(this);
        username.setValue("");
        password.setValue("");
    }

    private void redirect(String viewName) {
        getUI().getNavigator().navigateTo(viewName);
    }

    private void commitAndAuthenticateUser() {
        try {
            fieldGroup.commit();
        } catch (FieldGroup.CommitException e) {
            e.printStackTrace();
        }

        if (isValid()) {
            sessionService.setUser(user);
            redirect(HomeView.VIEW_NAME);
        } else {
            password.setValue(null);
            password.focus();
        }
    }

    private boolean isValid() {
        User dbUser = userService.authenticateUser(user);
        if (dbUser == null) {
            return false;
        }
        user = dbUser;
        return true;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        username.focus();
    }

}
