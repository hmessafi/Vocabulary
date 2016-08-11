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
import org.springframework.context.annotation.DependsOn;
import pl.nikowis.entities.User;
import pl.nikowis.services.SessionService;

/**
 * Created by nikowis on 2016-08-02.
 */
@SpringView
@DependsOn
public class HomeView extends CustomComponent implements View {

    private SessionService sessionService;

    public static final String VIEW_NAME = "home";

    private Label greeting;
    private Button logout;

    private User user;

    @Autowired
    public HomeView(SessionService sessionService) {

        this.sessionService = sessionService;

        initializeComponents();

        setSizeFull();
        VerticalLayout fields = new VerticalLayout(greeting, logout);
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, false));
        fields.setSizeUndefined();

        VerticalLayout viewLayout = new VerticalLayout(fields);
        viewLayout.setSizeFull();
        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        setCompositionRoot(viewLayout);

    }

    private void initializeComponents() {
        greeting = new Label();
        logout = new Button("Logout", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                sessionService.setUser(null);
                getUI().getNavigator().navigateTo(LoginView.VIEW_NAME);
            }
        });
        user = sessionService.getUser();
        if(user!=null) {
            greeting.setValue("Hello " + user.getUsername());
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        //empty
    }
}