package pl.nikowis.ui;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.springframework.beans.factory.annotation.Autowired;
import pl.nikowis.entities.User;
import pl.nikowis.services.SessionService;
import pl.nikowis.services.UserService;
import pl.nikowis.ui.base.I18nCustomComponent;

/**
 * Login page.
 * Created by nikowis on 2016-08-01.
 */
@SpringView(name = LoginView.VIEW_NAME)
public class LoginView extends I18nCustomComponent implements View {

    public static final String VIEW_NAME = "login";

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserService userService;

    private TextField username;

    private PasswordField password;

    private Button login, register;

    private FieldGroup fieldGroup;

    private User user;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

        if (sessionService.getUser() != null) {
            redirect(HomeView.VIEW_NAME);
        }

        initalizeComponents();

        setSizeFull();

        VerticalLayout fields = new VerticalLayout(
                username
                , password
                , login
                , register
        );

        fields.setCaption(getMessage("loginView.title"));
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, false));
        fields.setSizeUndefined();

        VerticalLayout mainLayout = new VerticalLayout(fields);
        mainLayout.setSizeFull();
        mainLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        mainLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        setCompositionRoot(mainLayout);
        username.focus();
    }

    private void initalizeComponents() {
        username = new TextField(getMessage("loginView.username"));
        username.setRequired(true);

        password = new PasswordField(getMessage("loginView.password"));
        password.setRequired(true);
        password.setValue("");
        password.setNullRepresentation("");

        login = new Button(getMessage("loginView.login"), FontAwesome.SIGN_IN);
        login.addClickListener(clickEvent -> commitAndAuthenticateUser());

        register = new Button(getMessage("loginView.register"), FontAwesome.USER);
        register.addClickListener(clickEvent -> redirect(RegisterView.VIEW_NAME));

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
            System.out.println(e.getMessage());
        }

        authenticateUser();
        sessionService.setUser(user);
        redirect(HomeView.VIEW_NAME);
    }

    private boolean authenticateUser() {
        user = userService.authenticateUser(user);
        return true;
    }
}
