package pl.nikowis.ui;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
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
public class Header extends I18nCustomComponent implements InitializableComponent{

    private static final String HEADER_STYLE = "myHeader";

    @Autowired
    private SessionService sessionService;

    @Autowired
    private Menu menu;

    @Override
    public void initializeComponent() {
        VerticalLayout mainLayout = new VerticalLayout();
        HorizontalLayout components = new HorizontalLayout();

        menu.initializeComponent();

        components.addComponents(menu);
        components.setSpacing(true);

        mainLayout.addComponent(components);
        mainLayout.setSizeFull();
        mainLayout.setComponentAlignment(components, Alignment.MIDDLE_CENTER);
        mainLayout.setStyleName(HEADER_STYLE);
        setCompositionRoot(mainLayout);
    }

    public void refreshMenu() {
        menu.refresh();
    }
}
