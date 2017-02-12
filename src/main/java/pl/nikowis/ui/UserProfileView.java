package pl.nikowis.ui;

import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import pl.nikowis.entities.User;
import pl.nikowis.exceptions.EmptyFieldException;
import pl.nikowis.exceptions.PasswordsDontMatchException;
import pl.nikowis.services.QuizService;
import pl.nikowis.services.SessionService;
import pl.nikowis.services.UserService;
import pl.nikowis.services.WordService;
import pl.nikowis.ui.base.I18nCustomComponent;

/**
 * User profile view to edit user's basic information
 * and view statistics.
 * Created by nikowis on 02.09.2016
 *
 * @author nikowis
 */

@SpringView(name = UserProfileView.VIEW_NAME)
public class UserProfileView extends I18nCustomComponent implements View {

    public static final String VIEW_NAME = "userProfile";
    private final String DELETE_STYLE = "delete";

    @Autowired
    private SessionService sessionService;

    @Autowired
    private WordService wordService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private UserService userService;

    private TextField username, totalScore, quizesCompleted, totalWords;
    private PasswordField oldPassword, newPassword, newPasswordRepeat;
    private Button submit, delete;

    private User currentUser;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        initializeComponents();
        currentUser = sessionService.getUser();
        username.setValue(currentUser.getUsername());
        quizesCompleted.setValue(Long.toString(quizService.count(currentUser.getId())));
        totalWords.setValue(Long.toString(wordService.count(currentUser.getId())));
        totalScore.setValue(Long.toString(wordService.getTotalScore(currentUser.getId())));

        CssLayout passwordsLayout = new CssLayout(oldPassword, newPassword, newPasswordRepeat, submit);
        passwordsLayout.setCaption(getMessage("userProfileView.passwordsLayout"));
        passwordsLayout.addStyleName("profile-view-password");

        CssLayout userInfoLayout = new CssLayout(username, totalScore, quizesCompleted, totalWords);
        userInfoLayout.setCaption(getMessage("userProfileView.userInfoLayout"));
        userInfoLayout.setEnabled(false);

        this.setCaption(getMessage("userProfileView.title"));

        CssLayout mainLayout = new CssLayout(userInfoLayout, passwordsLayout, delete);
        mainLayout.addStyleName("profile-view");
        setCompositionRoot(mainLayout);
    }

    private void initializeComponents() {
        submit = new Button(getMessage("userProfileView.submitPassword"), FontAwesome.CHECK);
        submit.addClickListener(clickEvent -> submitNewPassword());
        
        delete = new Button(getMessage("userProfileView.deleteProfile"), FontAwesome.TIMES);
        delete.addClickListener(clickEvent -> deleteAccount());
        delete.addStyleName(DELETE_STYLE);

        oldPassword = new PasswordField(getMessage("userProfileView.oldPassword"));
        oldPassword.setRequired(true);
        oldPassword.setNullRepresentation("");
        newPassword = new PasswordField(getMessage("userProfileView.newPassword"));
        newPassword.setRequired(true);
        newPassword.setNullRepresentation("");
        newPasswordRepeat = new PasswordField(getMessage("userProfileView.newPasswordRepeat"));
        newPasswordRepeat.setRequired(true);
        newPasswordRepeat.setNullRepresentation("");

        username = new TextField(getMessage("userProfileView.username"));
        totalScore = new TextField(getMessage("userProfileView.totalScore"));
        quizesCompleted = new TextField(getMessage("userProfileView.quizesCompleted"));
        totalWords = new TextField(getMessage("userProfileView.totalWords"));
    }

    private void deleteAccount() {
        //TODO: it should be not possible to delete the current admin account from this view
        //(there should always be at least one admin).
        //
        userService.deleteUser(currentUser);
        sessionService.eraseUser();
        redirect(LoginView.VIEW_NAME);
    }

    private void redirect(String viewName) {
        getUI().getNavigator().navigateTo(viewName);
    }

    private void submitNewPassword() {
        userService.authenticatePassword(currentUser, oldPassword.getValue());
        validateNewPasswordMatch();
        currentUser.setPassword(newPassword.getValue());
        userService.saveUserWithNewPassword(currentUser);
        Notification.show( getMessage("userProfileView.submitPassword.success"), Notification.Type.TRAY_NOTIFICATION);
    }

    private void checkNotEmpty(String o) {
        if (Strings.isNullOrEmpty(o)) {
            throw new EmptyFieldException();
        }
    }

    private void validateNewPasswordMatch() {
        checkNotEmpty(newPassword.getValue());
        checkNotEmpty(newPasswordRepeat.getValue());
        if (!newPassword.getValue().equals(newPasswordRepeat.getValue())) {
            throw new PasswordsDontMatchException();
        }
    }
}
