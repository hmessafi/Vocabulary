package pl.nikowis.ui;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
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

    @Autowired
    private SessionService sessionService;

    final private VerticalLayout mainLayout = new VerticalLayout();
    final private HorizontalLayout components = new HorizontalLayout();
    private Label title;

    @Override
    public void initializeComponent() {
        title = new Label(getMessage("header.title"));

        components.addComponent(title);
        components.setSpacing(true);
        components.setMargin(new MarginInfo(true, true, true, false));
        components.setSizeUndefined();

        mainLayout.addComponent(components);
        mainLayout.setSizeFull();
        mainLayout.setComponentAlignment(components, Alignment.MIDDLE_CENTER);
        mainLayout.setStyleName("myHeader");
        setCompositionRoot(mainLayout);
    }
}
