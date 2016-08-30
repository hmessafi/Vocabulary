package pl.nikowis.ui;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.vaadin.data.Validator;
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
import pl.nikowis.exceptions.EmptyFieldException;
import pl.nikowis.exceptions.PasswordsDontMatchException;
import pl.nikowis.services.I18n;
import pl.nikowis.services.UserService;

/**
 * Register page.
 * Created by nikowis on 2016-08-11.
 *
 * @author nikowis
 */
@SpringView
public class RegisterView extends CustomComponent implements View {

    public static final String VIEW_NAME = "register";

    private UserService userService;

    private I18n i18n;

    private TextField username;

    private PasswordField password;

    private PasswordField repeatPassword;
    private Button submit;
    private FieldGroup fieldGroup;
    private User user;

    @Autowired
    public RegisterView(UserService userService, I18n i18n) {
        this.userService = userService;
        this.i18n = i18n;

        intializeComponents();

        setSizeFull();

        VerticalLayout fields = new VerticalLayout(
                username
                , password
                , repeatPassword
                , submit
        );

        fields.setCaption(i18n.getMessage("registerView.title", getLocale()));
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, false));
        fields.setSizeUndefined();

        VerticalLayout mainLayout = new VerticalLayout(fields);
        mainLayout.setSizeFull();
        mainLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        mainLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        setCompositionRoot(mainLayout);
    }

    private void intializeComponents() {
        username = new TextField(i18n.getMessage("registerView.username", getLocale()));
        username.setNullRepresentation("");
        username.setRequired(true);
        password = new PasswordField(i18n.getMessage("registerView.password", getLocale()));
        password.setNullRepresentation("");
        password.setRequired(true);
        repeatPassword = new PasswordField(i18n.getMessage("registerView.passwordRepeat", getLocale()));
        repeatPassword.setRequired(true);
        submit = new Button(i18n.getMessage("registerView.submit", getLocale()));
        submit.addClickListener(clickEvent -> submitAndRedirect());
        user = new User();

        BeanItem<User> bean = new BeanItem<User>(user);
        fieldGroup = new FieldGroup(bean);
        fieldGroup.bindMemberFields(this);

    }

    private void checkNotEmpty(String o) {
        if (Strings.isNullOrEmpty(o)) {
            throw new EmptyFieldException();
        }
    }

    private boolean validatePasswordMatch() {
        checkNotEmpty(password.getValue());
        checkNotEmpty(repeatPassword.getValue());
        if (!password.getValue().equals(repeatPassword.getValue())) {
            throw new PasswordsDontMatchException();
        }
        return false;
    }

    private void submitAndRedirect() {
        try {
            fieldGroup.commit();
            validatePasswordMatch();
            userService.createNewUser(user);
            getUI().getNavigator().navigateTo(LoginView.VIEW_NAME);
        } catch (FieldGroup.CommitException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        //empty
    }
}
