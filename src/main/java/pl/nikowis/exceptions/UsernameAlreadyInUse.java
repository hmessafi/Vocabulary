package pl.nikowis.exceptions;

import pl.nikowis.exceptions.base.BusinessException;

/**
 * Occurs when a new account with existing username is being created.
 * Created by nikowis on 2016-08-28.
 *
 * @author nikowis
 */
public class UsernameAlreadyInUse extends BusinessException {
}
