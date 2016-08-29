package pl.nikowis.ui;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.Reindeer;
import org.springframework.beans.factory.annotation.Autowired;
import pl.nikowis.entities.User;
import pl.nikowis.services.I18n;
import pl.nikowis.services.UserService;

/**
 * Admin view for browsing and deleting users.
 * Created by nikowis on 2016-08-28.
 *
 * @author nikowis
 */
@SpringView(name = UserListView.VIEW_NAME)
public class UserListView extends CustomComponent implements View {

    public static final String VIEW_NAME = "userList";

    private UserService userService;

    private I18n i18n;

    private Grid users;
    private Button home;
    private BeanItemContainer<User> userContainer;

    @Autowired
    public UserListView(UserService userService, I18n i18n) {
        this.userService = userService;
        this.i18n = i18n;
        initializeComponents();

        setSizeFull();

        VerticalLayout gridLayout = new VerticalLayout(home, users);
        gridLayout.setSpacing(true);
        gridLayout.setMargin(new MarginInfo(true, true, true, false));
        gridLayout.setSizeUndefined();

        VerticalLayout mainLayout = new VerticalLayout(gridLayout);
        mainLayout.setSizeFull();
        mainLayout.setComponentAlignment(gridLayout, Alignment.MIDDLE_CENTER);
        mainLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        setCompositionRoot(mainLayout);
    }

    private void initializeComponents() {
        home = new Button(i18n.getMessage("userListView.home", getLocale()));
        home.addClickListener(clickEvent -> redirect(HomeView.VIEW_NAME));

        users = new Grid(i18n.getMessage("userListView.users.title", getLocale()));
        users.setWidthUndefined();
        users.setSelectionMode(Grid.SelectionMode.NONE);
        initializeGridContent();
    }

    private void initializeGridContent() {
        BeanItemContainer<User> userContainer = new BeanItemContainer<User>(User.class);
        userContainer.addAll(userService.getAll());
        userContainer.addNestedContainerProperty("role.name");
        GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(userContainer);
        gpc.addGeneratedProperty("delete", new PropertyValueGenerator<String>() {
            @Override
            public String getValue(Item item, Object itemId, Object propertyId) {
                return i18n.getMessage("userListView.users.delete", getLocale());
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        });
        users.setContainerDataSource(gpc);
        users.setColumns("username", "enabled", "score", "role.name", "delete");
        users.getColumn("delete").setRenderer(new ButtonRenderer(event -> deleteUser(((User)event.getItemId()))));
    }

    private void deleteUser(User user) {
        userService.deleteUser(user);
        initializeGridContent();
    }

    private void redirect(String viewName) {
        getUI().getNavigator().navigateTo(viewName);
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        //empty
    }
}
