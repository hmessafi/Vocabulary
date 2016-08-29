package pl.nikowis.ui;

import com.vaadin.data.fieldgroup.FieldGroup;
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
import pl.nikowis.services.I18n;
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

    private I18n i18n;

    private TextField username;

    private PasswordField password;

    private Button login, register;

    private FieldGroup fieldGroup;

    private User user;

    @Autowired
    public LoginView(UserService userService, SessionService sessionService, I18n i18n) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.i18n = i18n;

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

        fields.setCaption(i18n.getMessage("loginView.title", getLocale()));
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
        username = new TextField(i18n.getMessage("loginView.username", getLocale()));
        username.setWidth("300px");
        username.setRequired(true);

        password = new PasswordField(i18n.getMessage("loginView.password", getLocale()));
        password.setWidth("300px");
        password.setRequired(true);
        password.setValue("");
        password.setNullRepresentation("");

        login = new Button(i18n.getMessage("loginView.login", getLocale()));
        login.addClickListener(clickEvent -> commitAndAuthenticateUser());

        register = new Button(i18n.getMessage("loginView.register", getLocale()));
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
        User dbUser = userService.authenticateUser(user);
        user = dbUser;
        return true;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        username.focus();
    }

}
