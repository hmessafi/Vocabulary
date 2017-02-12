package pl.nikowis.ui;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
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

    private static final String FOOTER_STYLE = "footer";

    @Override
    public void initializeComponent() {
        CssLayout mainLayout = new CssLayout();
        Label label = new Label(getMessage("footer.title"));
        mainLayout.addComponent(label);
        this.setStyleName(FOOTER_STYLE);
        setCompositionRoot(mainLayout);
    }
}
