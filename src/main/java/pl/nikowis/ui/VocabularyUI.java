package pl.nikowis.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.ErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
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

    @Autowired
    private SpringViewProvider viewProvider;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private I18n i18n;

    final VerticalLayout layout = new VerticalLayout();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        this.setNavigator(new Navigator(this, this));
        getNavigator().addProvider(viewProvider);

        if (sessionService.getUser() != null) {
            getNavigator().navigateTo(HomeView.VIEW_NAME);
        } else {
            getNavigator().navigateTo(LoginView.VIEW_NAME);
        }

        layout.addComponent(new Label("Adssadasd"));
        VaadinSession.getCurrent().setErrorHandler(new ErrorHandler() {

            @Override
            public void error(com.vaadin.server.ErrorEvent errorEvent) {
                for (Throwable t = errorEvent.getThrowable(); t != null; t = t.getCause()) {
                    if (t.getCause() == null) {
                        //final cause
                        String exceptionClassName = t.getClass().getSimpleName();
                        if (t instanceof BusinessException) {
                            Notification.show(
                                    i18n.getMessage("BusinessException.title", getLocale()) + "\n"
                                            + i18n.getMessage(exceptionClassName + ".description", getLocale())
                                    , Notification.Type.ERROR_MESSAGE
                            );
                        } else if (t instanceof TechnicalException) {
                            Notification.show(
                                    i18n.getMessage("TechnicalException.title", getLocale())
                                            + t.toString()
                                    , Notification.Type.ERROR_MESSAGE
                            );
                        } else {
                            errorEvent.getThrowable().printStackTrace();
                        }
                    }
                }
            }

        });
//        UI.getCurrent().setErrorHandler(new DefaultErrorHandler() {
//            @Override
//            public void error(com.vaadin.server.ErrorEvent event) {
//                // Find the final cause
//                String cause = "<b>The click failed because:</b><br/>";
//                for (Throwable t = event.getThrowable(); t != null;
//                     t = t.getCause())
//                    if (t.getCause() == null) // We're at final cause
//                        cause += t.getClass().getName() + "<br/>";
//
//                // Display the error message in a custom fashion
//                layout.addComponent(new Label(cause, ContentMode.HTML));
//
//                // Do the default error handling (optional)
//                doDefault(event);
//            }
//        });

    }
}
