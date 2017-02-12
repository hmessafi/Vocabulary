package pl.nikowis.ui;

import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import pl.nikowis.entities.User;
import pl.nikowis.exceptions.EmptyFieldException;
import pl.nikowis.exceptions.PasswordsDontMatchException;
import pl.nikowis.services.UserService;
import pl.nikowis.ui.base.I18nCustomComponent;

/**
 * Registration page.
 * Created by nikowis on 2016-08-11.
 *
 * @author nikowis
 */
@SpringView
public class RegisterView extends I18nCustomComponent implements View {

    public static final String VIEW_NAME = "register";

    @Autowired
    private UserService userService;

    private TextField username;

    private PasswordField password;

    private PasswordField repeatPassword;
    private Button submit;
    private FieldGroup fieldGroup;
    private User user;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        intializeComponents();

        CssLayout mainLayout = new CssLayout(
                username
                , password
                , repeatPassword
                , submit
        );
        this.addStyleName("register-view");
        this.setCaption(getMessage("registerView.title"));
        setCompositionRoot(mainLayout);
    }

    private void intializeComponents() {
        username = new TextField(getMessage("registerView.username"));
        username.setNullRepresentation("");
        username.setRequired(true);
        password = new PasswordField(getMessage("registerView.password"));
        password.setNullRepresentation("");
        password.setRequired(true);
        repeatPassword = new PasswordField(getMessage("registerView.passwordRepeat"));
        repeatPassword.setRequired(true);
        submit = new Button(getMessage("registerView.submit"), FontAwesome.CHECK);
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

    private void validatePasswordMatch() {
        checkNotEmpty(password.getValue());
        checkNotEmpty(repeatPassword.getValue());
        if (!password.getValue().equals(repeatPassword.getValue())) {
            throw new PasswordsDontMatchException();
        }
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


}
