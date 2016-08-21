package pl.nikowis.ui;

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

    private TextField username;

    private PasswordField password;

    private PasswordField repeatPassword;
    private Button submitButton;
    private FieldGroup fieldGroup;
    private User user;

    @Autowired
    public RegisterView(UserService userService) {
        this.userService = userService;

        intializeComponents();

        setSizeFull();

        VerticalLayout fields = new VerticalLayout(
                username
                , password
                , repeatPassword
                , submitButton
        );

        fields.setCaption("Create new account.");
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
        username = new TextField("Username");
        username.setNullRepresentation("");
        password = new PasswordField("Password");
        password.setNullRepresentation("");
        repeatPassword = new PasswordField("Repeat password");
        repeatPassword.addValidator(o -> validatePasswordMatch(o));
        submitButton = new Button("Create new account");
        submitButton.addClickListener(clickEvent -> submitAndRedirect());
        user = new User();

        BeanItem<User> bean = new BeanItem<User>(user);
        fieldGroup = new FieldGroup(bean);
        fieldGroup.bindMemberFields(this);

    }

    private void validatePasswordMatch(Object o) {
        if ((password.getValue().length() > 0
                && !password.getValue().equals(o.toString()))
                || Strings.isNullOrEmpty(password.getValue())) {
            throw new Validator.InvalidValueException("Passwords don't match.");
        }
    }

    private void submitAndRedirect() {
        try {
            fieldGroup.commit();
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
