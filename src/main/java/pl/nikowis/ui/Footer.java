package pl.nikowis.ui;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.springframework.beans.factory.annotation.Autowired;
import pl.nikowis.exceptions.base.BusinessException;
import pl.nikowis.services.I18n;

/**
 * Footer component for all the views.
 * Created by nikowis on 2016-08-31.
 *
 * @author nikowis
 */
@SpringComponent
public class Footer extends CustomComponent {

    private I18n i18n;

    final private VerticalLayout mainLayout = new VerticalLayout();
    final private HorizontalLayout components = new HorizontalLayout();
    @Autowired
    public Footer(I18n i18n) {
        this.i18n = i18n;
        Label label = new Label(i18n.getMessage("footer.title", getLocale()));
        components.addComponent(label);
        components.setSpacing(true);
        components.setMargin(new MarginInfo(true, true, true, false));
        components.setSizeUndefined();

        mainLayout.addComponent(components);
        mainLayout.setSizeFull();
        mainLayout.setComponentAlignment(components, Alignment.MIDDLE_CENTER);
        mainLayout.setStyleName("myFooter");
        setCompositionRoot(mainLayout);
    }

}
