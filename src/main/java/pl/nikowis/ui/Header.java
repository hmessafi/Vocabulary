package pl.nikowis.ui;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.CssLayout;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final String HEADER_STYLE = "header";

    @Autowired
    private Menu menu;

    @Override
    public void initializeComponent() {
        CssLayout mainLayout = new CssLayout();
        menu.initializeComponent();
        mainLayout.addComponents(menu);
        this.setStyleName(HEADER_STYLE);
        setCompositionRoot(mainLayout);
    }

    public void refreshMenu() {
        menu.refresh();
    }
}
