package pl.nikowis.ui;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.ButtonRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import pl.nikowis.entities.User;
import pl.nikowis.services.UserService;
import pl.nikowis.ui.base.I18nCustomComponent;

/**
 * Admin view for browsing and deleting users.
 * Created by nikowis on 2016-08-28.
 *
 * @author nikowis
 */
@SpringView(name = UserListView.VIEW_NAME)
public class UserListView extends I18nCustomComponent implements View {

    public static final String VIEW_NAME = "userList";
    private final String COL_USERNAME = "username";
    private final String COL_ENABLED = "enabled";
    private final String COL_ROLE = "role.name";
    private final String COL_DELETE = "delete";

    @Autowired
    private UserService userService;

    private Grid users;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        initializeComponents();

        CssLayout mainLayout = new CssLayout(users);
        this.setCaption(getMessage("userListView.title"));

        setCompositionRoot(mainLayout);
    }

    private void initializeComponents() {

        users = new Grid(getMessage("userListView.users.title"));
        users.setSelectionMode(Grid.SelectionMode.NONE);
        initializeGridContent();
    }

    private void initializeGridContent() {
        BeanItemContainer<User> userContainer = new BeanItemContainer<User>(User.class);
        userContainer.addAll(userService.getAll());
        userContainer.addNestedContainerProperty(COL_ROLE);
        GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(userContainer);
        gpc.addGeneratedProperty(COL_DELETE, new PropertyValueGenerator<String>() {
            @Override
            public String getValue(Item item, Object itemId, Object propertyId) {
                return getMessage("userListView.users.delete");
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        });
        users.setContainerDataSource(gpc);
        users.setColumns(COL_USERNAME, COL_ENABLED, COL_ROLE, COL_DELETE);
        users.getColumn(COL_DELETE).setRenderer(new ButtonRenderer(event -> deleteUser(((User)event.getItemId()))));
    }

    private void deleteUser(User user) {
        userService.deleteUser(user);
        initializeGridContent();
    }

    private void redirect(String viewName) {
        getUI().getNavigator().navigateTo(viewName);
    }


}
