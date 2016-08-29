package pl.nikowis.services;

import java.util.Locale;

/**
 * Service for getting I18n messages.
 * Created by nikowis on 2016-08-29.
 *
 * @author nikowis
 */
public interface I18n {
    String getMessage(String code, Object[] params, Locale locale);
    String getMessage(String code, Locale locale);
}
