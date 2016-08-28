package pl.nikowis.exceptions;

import pl.nikowis.exceptions.base.TechnicalException;

/**
 * Occurs when users tries to access a view undefined in the {@link pl.nikowis.security.AccessChecker}.
 * Created by nikowis on 2016-08-21.
 *
 * @author nikowis
 */
public class UndefinedViewException extends TechnicalException {
}
