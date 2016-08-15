package pl.nikowis.ui;

import com.vaadin.data.Item;
import com.vaadin.data.Validator;
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
import pl.nikowis.services.UserService;

import javax.persistence.criteria.CriteriaBuilder;

/**
 * Created by nikowis on 2016-08-11.
 *
 * @author nikowis
 */
@SpringView
public class RegisterView extends CustomComponent implements View {

    public static final String VIEW_NAME = "register";

    private UserService userService;

    @PropertyId("username" )
    private TextField usernameField;

    @PropertyId("password")
    private PasswordField passwordField;
    private PasswordField repeatPasswordField;
    private Button submitButton;
    private FieldGroup fieldGroup;
    private User user;

    public RegisterView(UserService userService) {
        this.userService = userService;

        intializeComponents();

        setSizeFull();

        VerticalLayout fields = new VerticalLayout(
                usernameField
                , passwordField
                , repeatPasswordField
                , submitButton
        );

        fields.setCaption("Create new account.");
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, false));
        fields.setSizeUndefined();

        VerticalLayout viewLayout = new VerticalLayout(fields);
        viewLayout.setSizeFull();
        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        setCompositionRoot(viewLayout);
    }

    private void intializeComponents() {
        usernameField = new TextField("Username");
        passwordField = new PasswordField("Password");
        repeatPasswordField = new PasswordField("Repeat password");
        repeatPasswordField.addValidator(new Validator() {
            @Override
            public void validate(Object o) throws InvalidValueException {
                if(passwordField.getValue().length()>0
                        && !passwordField.getValue().equals(o.toString())) {
                    throw new InvalidValueException("Passwords don't match.");
                }
            }
        });
        submitButton = new Button("Create new account", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    fieldGroup.commit();
                    userService.createNewUser(user);
                    getUI().getNavigator().navigateTo(LoginView.VIEW_NAME);
                } catch (FieldGroup.CommitException e) {
                    e.printStackTrace();
                }
            }
        });
        user = new User();

        BeanItem<User> bean = new BeanItem<User>(user);
        fieldGroup = new FieldGroup(bean);
        fieldGroup.bindMemberFields(this);
        usernameField.setValue("");
        passwordField.setValue("");
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        //empty
    }
}
