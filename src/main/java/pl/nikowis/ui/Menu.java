package pl.nikowis.ui;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.MenuBar;
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

    @Autowired
    private SessionService sessionService;

    private MenuBar bar;
    private MenuBar.MenuItem links, home, quizes, profile, words, users, logout;

    @Override
    public void initializeComponent() {
        CssLayout mainLayout = new CssLayout();

        bar = new MenuBar();
        links = bar.addItem(getMessage("header.title"), FontAwesome.ARROW_DOWN, null);
        home = links.addItem(getMessage("menu.home"), FontAwesome.HOME, menuItem -> redirect(HomeView.VIEW_NAME));
        quizes = links.addItem(getMessage("menu.quizes"), FontAwesome.PLAY_CIRCLE, menuItem -> redirect(QuizView.VIEW_NAME));
        profile = links.addItem(getMessage("menu.profile"), FontAwesome.USER, menuItem -> redirect(UserProfileView.VIEW_NAME));
        words = links.addItem(getMessage("menu.words"), FontAwesome.LIST, menuItem -> redirect(WordListView.VIEW_NAME));
        users = links.addItem(getMessage("menu.users"), FontAwesome.USERS, menuItem -> redirect(UserListView.VIEW_NAME));
        logout = links.addItem(getMessage("menu.logout"), FontAwesome.SIGN_OUT, menuItem -> logoutAndRedirect());

        mainLayout.addComponent(bar);
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
