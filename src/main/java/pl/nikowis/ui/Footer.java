package pl.nikowis.ui;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import pl.nikowis.ui.base.I18nCustomComponent;
import pl.nikowis.ui.base.InitializableComponent;

/**
 * Footer component for all the views.
 * Created by nikowis on 2016-08-31.
 *
 * @author nikowis
 */
@SpringComponent
public class Footer extends I18nCustomComponent implements InitializableComponent{

    private static final String FOOTER_STYLE = "myFooter";

    @Override
    public void initializeComponent() {
        VerticalLayout mainLayout = new VerticalLayout();
        HorizontalLayout components = new HorizontalLayout();
        Label label = new Label(getMessage("footer.title"));
        components.addComponent(label);
        components.setSpacing(true);
        components.setSizeUndefined();

        mainLayout.addComponent(components);
        mainLayout.setSizeFull();
        mainLayout.setComponentAlignment(components, Alignment.MIDDLE_CENTER);
        mainLayout.setStyleName(FOOTER_STYLE);
        setCompositionRoot(mainLayout);
    }
}
