package pl.nikowis.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import org.springframework.beans.factory.annotation.Autowired;
import pl.nikowis.entities.User;
import pl.nikowis.security.UserRoles;
import pl.nikowis.services.SessionService;
import pl.nikowis.services.WordService;
import pl.nikowis.ui.base.I18nCustomComponent;

/**
 * Home page.
 * Created by nikowis on 2016-08-02.
 */
@SpringView(name = HomeView.VIEW_NAME)
public class HomeView extends I18nCustomComponent implements View {

    public static final String VIEW_NAME = "home";

    @Autowired
    private SessionService sessionService;

    @Autowired
    private WordService wordService;

    private Button logout, wordList, quiz, userList, userProfile;

    private User user;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        initializeComponents();

        CssLayout mainLayout = new CssLayout(logout, wordList, quiz, userList, userProfile);
        this.setCaption(getMessage("homeView.title"));
        user = sessionService.getUser();
        if (user != null) {
            mainLayout.setCaption(getI18n().getMessage(
                    "homeView.greeting"
                    , new Object[]{user.getUsername()}
                    , getLocale())
            );
        }
        mainLayout.addStyleName("home-view");
        setCompositionRoot(mainLayout);
    }

    private void initializeComponents() {
        logout = new Button(getMessage("homeView.logout"), FontAwesome.SIGN_OUT);

        logout.addClickListener(clickEvent -> logoutAndRedirect());

        quiz = new Button(getMessage("homeView.quiz"), FontAwesome.PLAY_CIRCLE);
        quiz.addClickListener(clickEvent -> redirect(QuizView.VIEW_NAME));

        wordList = new Button(getMessage("homeView.wordList"), FontAwesome.LIST);
        wordList.addClickListener(clickEvent -> redirect(WordListView.VIEW_NAME));

        userList = new Button(getMessage("homeView.userList"), FontAwesome.USERS);
        userList.addClickListener(clickEvent -> redirect(UserListView.VIEW_NAME));
        userList.setVisible(sessionService.hasRole(UserRoles.ROLE_ADMIN));
        userProfile = new Button(getMessage("homeView.userProfile"), FontAwesome.USER);
        userProfile.addClickListener(clickEvent -> redirect(UserProfileView.VIEW_NAME));
    }

    private void redirect(String viewName) {
        getUI().getNavigator().navigateTo(viewName);
    }

    private void logoutAndRedirect() {
        sessionService.eraseUser();
        redirect(LoginView.VIEW_NAME);
    }

}