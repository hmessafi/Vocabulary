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
import pl.nikowis.services.SessionService;

/**
 * Home page.
 * Created by nikowis on 2016-08-02.
 */
@SpringView(name = HomeView.VIEW_NAME)
public class HomeView extends CustomComponent implements View {

    public static final String VIEW_NAME = "home";

    private SessionService sessionService;

    private Label greeting;
    private Button logout, wordList, quiz;

    private User user;

    @Autowired
    public HomeView(SessionService sessionService) {

        this.sessionService = sessionService;

        initializeComponents();

        setSizeFull();
        VerticalLayout fields = new VerticalLayout(greeting, logout, wordList, quiz);
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
        logout = new Button("Logout");
        logout.addClickListener(clickEvent -> eraseFromSessionAndRedirect());

        quiz = new Button("Start the quiz");
        quiz.addClickListener(clickEvent -> redirect(QuizView.VIEW_NAME));

        wordList = new Button("Word list");
        wordList.addClickListener(clickEvent ->  redirect(WordListView.VIEW_NAME));

        user = sessionService.getUser();
        if(user!=null) {
            greeting.setValue("Hello " + user.getUsername());
        }
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