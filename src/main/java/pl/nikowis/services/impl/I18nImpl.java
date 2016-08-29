package pl.nikowis.services.impl;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import pl.nikowis.services.I18n;

import java.util.Locale;

/**
 * Created by nikowis on 2016-08-29.
 *
 * @author nikowis
 */
@Service
public class I18nImpl implements I18n {

    private ResourceBundleMessageSource resourceBundleMessageSource;

    public I18nImpl() {
        resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setBasename("messages");
        resourceBundleMessageSource.setDefaultEncoding("utf-8");
    }

    @Override
    public String getMessage(String code, Object[] params, Locale locale) {
        return resourceBundleMessageSource.getMessage(code, params, locale);
    }

    @Override
    public String getMessage(String code, Locale locale) {
        return resourceBundleMessageSource.getMessage(code, null, locale);
    }
}
