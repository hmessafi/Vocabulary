package pl.nikowis.ui;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import pl.nikowis.entities.User;
import pl.nikowis.security.UserRoles;
import pl.nikowis.services.SessionService;
import pl.nikowis.ui.base.I18nCustomComponent;
import pl.nikowis.ui.base.InitializableComponent;

/**
 * Header component for all the views.
 * Created by nikowis on 2016-08-31.
 *
 * @author nikowis
 */
@SpringComponent
public class Menu extends I18nCustomComponent implements InitializableComponent {

    private static final String HEADER_STYLE = "myHeader";

    @Autowired
    private SessionService sessionService;

    private MenuBar bar;
    private MenuBar.MenuItem links, home, quizes, profile, words, users, logout;

    @Override
    public void initializeComponent() {
        VerticalLayout mainLayout = new VerticalLayout();
        HorizontalLayout components = new HorizontalLayout();

        bar = new MenuBar();
        links = bar.addItem(getMessage("menu.title"), null);
        home = links.addItem(getMessage("menu.home"), menuItem -> redirect(HomeView.VIEW_NAME));
        quizes = links.addItem(getMessage("menu.quizes"), menuItem -> redirect(QuizView.VIEW_NAME));
        profile = links.addItem(getMessage("menu.profiles"), menuItem -> redirect(UserProfileView.VIEW_NAME));
        words = links.addItem(getMessage("menu.words"), menuItem -> redirect(WordListView.VIEW_NAME));
        users = links.addItem(getMessage("menu.users"), menuItem -> redirect(UserListView.VIEW_NAME));
        logout = links.addItem(getMessage("menu.logout"), menuItem  -> logoutAndRedirect());

        components.addComponent(bar);
        components.setSpacing(true);
        components.setSizeFull();

        mainLayout.addComponent(components);
        mainLayout.setSizeFull();
        mainLayout.setComponentAlignment(components, Alignment.MIDDLE_CENTER);
        setCompositionRoot(mainLayout);
        refresh();
    }

    public void refresh() {
        User currentUser = sessionService.getUser();
        boolean isLoggedUser = currentUser != null;
        boolean isAdmin = sessionService.hasRole(UserRoles.ROLE_ADMIN);
        quizes.setVisible(isLoggedUser);
        profile.setVisible(isLoggedUser);
        words.setVisible(isLoggedUser);
        logout.setVisible(isLoggedUser);
        users.setVisible(isAdmin);
    }

    private void redirect(String viewName) {
        getUI().getNavigator().navigateTo(viewName);
    }

    private void logoutAndRedirect() {
        sessionService.eraseUser();
        redirect(LoginView.VIEW_NAME);
    }

}
