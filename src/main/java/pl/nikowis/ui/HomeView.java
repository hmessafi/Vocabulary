package pl.nikowis.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.springframework.beans.factory.annotation.Autowired;
import pl.nikowis.entities.User;
import pl.nikowis.security.UserRoles;
import pl.nikowis.services.I18n;
import pl.nikowis.services.SessionService;

/**
 * Home page.
 * Created by nikowis on 2016-08-02.
 */
@SpringView(name = HomeView.VIEW_NAME)
public class HomeView extends CustomComponent implements View {

    public static final String VIEW_NAME = "home";

    private SessionService sessionService;

    private I18n i18n;

    private Label greeting;
    private Button logout, wordList, quiz, userList;

    private User user;

    @Autowired
    public HomeView(SessionService sessionService, I18n i18n) {
        this.i18n = i18n;
        this.sessionService = sessionService;
        initializeComponents();

        setSizeFull();
        VerticalLayout fields = new VerticalLayout(greeting, logout, wordList, quiz, userList);
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
        logout = new Button(i18n.getMessage("homeView.logout", getLocale()));
        logout.addClickListener(clickEvent -> eraseFromSessionAndRedirect());

        quiz = new Button(i18n.getMessage("homeView.quiz", getLocale()));
        quiz.addClickListener(clickEvent -> redirect(QuizView.VIEW_NAME));

        wordList = new Button(i18n.getMessage("homeView.wordList", getLocale()));
        wordList.addClickListener(clickEvent -> redirect(WordListView.VIEW_NAME));

        user = sessionService.getUser();
        if (user != null) {
            greeting.setValue(i18n.getMessage(
                    "homeView.greeting"
                    , new Object[]{user.getUsername()}
                    , getLocale())
            );
        }

        userList = new Button(i18n.getMessage("homeView.userList", getLocale()));
        userList.addClickListener(clickEvent -> redirect(UserListView.VIEW_NAME));
        userList.setVisible(sessionService.hasRole(UserRoles.ROLE_ADMIN));
    }

    private void redirect(String viewName) {
        getUI().getNavigator().navigateTo(viewName);
    }

    private void eraseFromSessionAndRedirect() {
        sessionService.eraseUser();
        redirect(LoginView.VIEW_NAME);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        //empty
    }
}