package pl.nikowis.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ErrorHandler;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import pl.nikowis.exceptions.base.BusinessException;
import pl.nikowis.exceptions.base.TechnicalException;
import pl.nikowis.services.I18n;
import pl.nikowis.services.SessionService;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
@SpringUI
public class VocabularyUI extends UI {

    public static final String VIEW_LAYOUT_STYLE = "view-layout";

    @Autowired
    private SpringViewProvider viewProvider;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private I18n i18n;

    @Autowired
    private Header header;

    @Autowired
    private Footer footer;

    final private CssLayout mainLayout = new CssLayout();
    final private CssLayout viewLayout = new CssLayout();

    public Header getHeader() {
        return header;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        footer.initializeComponent();
        header.initializeComponent();
        mainLayout.addComponents(header, viewLayout, footer);
        viewLayout.addStyleName(VIEW_LAYOUT_STYLE);
        Navigator navigator = new Navigator(this, viewLayout);
        this.setNavigator(navigator);
        getNavigator().addProvider(viewProvider);
        if (sessionService.getUser() != null) {
            getNavigator().navigateTo(HomeView.VIEW_NAME);
        } else {
            getNavigator().navigateTo(LoginView.VIEW_NAME);
        }
        setContent(mainLayout);

        navigator.addViewChangeListener(new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(ViewChangeEvent viewChangeEvent) {
                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent viewChangeEvent) {
                ((VocabularyUI) (getUI())).getHeader().refreshMenu();
            }
        });

        VaadinSession.getCurrent().setErrorHandler(new ErrorHandler() {

            @Override
            public void error(com.vaadin.server.ErrorEvent errorEvent) {
                for (Throwable t = errorEvent.getThrowable(); t != null; t = t.getCause()) {
                    if (t.getCause() == null) {
                        //final cause
                        String exceptionClassName = t.getClass().getSimpleName();
                        if (t instanceof BusinessException) {
                            Notification notification = new Notification(
                                    i18n.getMessage("BusinessException.title", getLocale()) + "\n"
                                            + i18n.getMessage(exceptionClassName + ".description", getLocale())
                                    , Notification.Type.ERROR_MESSAGE
                            );
                            notification.setIcon(FontAwesome.TIMES_CIRCLE);
                            notification.show(Page.getCurrent());

                        } else if (t instanceof TechnicalException) {
                            Notification notification = new Notification(
                                    i18n.getMessage("BusinessException.title", getLocale()) + "\n"
                                            + i18n.getMessage(exceptionClassName + ".description", getLocale())
                                    , Notification.Type.ERROR_MESSAGE
                            );
                            notification.setIcon(FontAwesome.TIMES_CIRCLE);
                            notification.show(Page.getCurrent());
                        } else {
                            errorEvent.getThrowable().printStackTrace();
                        }
                    }
                }
            }

        });

    }
}
