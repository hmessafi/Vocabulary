package pl.nikowis.ui.base;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.CustomComponent;
import org.springframework.beans.factory.annotation.Autowired;
import pl.nikowis.services.I18n;

/**
 * Base class for vaadin custom components with i18n.
 * Created by nikowis on 2016-09-02.
 *
 * @author nikowis
 */
@SpringComponent
public abstract class I18nCustomComponent extends CustomComponent{

    @Autowired
    private I18n i18n;

    public I18n getI18n() {
        return i18n;
    }

    /**
     * Gets the message from i18n.
     * @param code message code
     * @return message
     */
    protected String getMessage(String code) {
        return i18n.getMessage(code, getLocale());
    }
}
