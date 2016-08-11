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
import pl.nikowis.services.SessionService;

/**
 * Created by nikowis on 2016-08-02.
 */
@SpringView
public class HomeView extends CustomComponent implements View {

    @Autowired
    private SessionService sessionService;

    public static final String VIEW_NAME = "home";

    private Label greeting = new Label();
    private Button logout = new Button("Logout", new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent event) {
            getUI().getNavigator().navigateTo(LoginView.VIEW_NAME);
        }
    });


    public HomeView() {
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

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        greeting.setValue("Hello " + sessionService.getUser().getUsername());
    }
}