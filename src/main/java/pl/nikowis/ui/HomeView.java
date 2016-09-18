package pl.nikowis.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
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

    private Label greeting;
    private Button logout, wordList, quiz, userList, userProfile;

    private User user;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        initializeComponents();
        setSizeFull();
        VerticalLayout fields = new VerticalLayout(greeting, logout, wordList, quiz, userList, userProfile);
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, false));
        fields.setSizeUndefined();
        VerticalLayout mainLayout = new VerticalLayout(fields);
        mainLayout.setSizeFull();
        mainLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        mainLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        setCompositionRoot(mainLayout);

    }

    private void initializeComponents() {
        greeting = new Label();
        logout = new Button(getMessage("homeView.logout"));

        logout.addClickListener(clickEvent -> logoutAndRedirect());

        quiz = new Button(getMessage("homeView.quiz"));
        quiz.addClickListener(clickEvent -> redirect(QuizView.VIEW_NAME));
        boolean userHasAnyWords = wordService.count(sessionService.getUser().getId()) > 0 ;
        quiz.setVisible(userHasAnyWords);

        wordList = new Button(getMessage("homeView.wordList"));
        wordList.addClickListener(clickEvent -> redirect(WordListView.VIEW_NAME));

        user = sessionService.getUser();
        if (user != null) {
            greeting.setValue(getI18n().getMessage(
                    "homeView.greeting"
                    , new Object[]{user.getUsername()}
                    , getLocale())
            );
        }

        userList = new Button(getMessage("homeView.userList"));
        userList.addClickListener(clickEvent -> redirect(UserListView.VIEW_NAME));
        userList.setVisible(sessionService.hasRole(UserRoles.ROLE_ADMIN));
        userProfile = new Button(getMessage("homeView.userProfile"));
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