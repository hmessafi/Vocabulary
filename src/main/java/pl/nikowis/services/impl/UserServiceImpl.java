package pl.nikowis.services.impl;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.nikowis.entities.User;
import pl.nikowis.exceptions.NoSuchUsernameUserException;
import pl.nikowis.exceptions.base.UserIsDeletedException;
import pl.nikowis.exceptions.base.UsernameAlreadyInUse;
import pl.nikowis.exceptions.base.WrongPasswordException;
import pl.nikowis.repositories.UserRepository;
import pl.nikowis.security.UserRoles;
import pl.nikowis.services.RoleService;
import pl.nikowis.services.UserService;

import java.util.List;

/**
 * Created by nikowis on 2016-08-11.
 *
 * @author nikowis
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Override
    public User saveUser(User user) {
        Preconditions.checkNotNull(user);
        return userRepository.save(user);
    }

    @Override
    public User authenticateUser(User user) {
        Preconditions.checkNotNull(user);
        User dbUser = userRepository.findOneByUsername(user.getUsername());
        if(dbUser == null) {
            throw new NoSuchUsernameUserException();
        }
        if(!dbUser.isEnabled()) {
            throw new UserIsDeletedException();
        }
        if(!dbUser.getPassword().equals(dbUser.getPassword())) {
            throw new WrongPasswordException();
        }
        return dbUser;
    }

    @Override
    public User createNewUser(User user) {
        Preconditions.checkNotNull(user);
        if(userRepository.findOneByUsername(user.getUsername()) != null) {
            throw new UsernameAlreadyInUse();
        }
        user.setEnabled(true);
        user.setRole(roleService.findOneByName(UserRoles.ROLE_USER));
        return userRepository.save(user);
    }

    @Override
    public User createNewAdmin(User user) {
        Preconditions.checkNotNull(user);
        user.setEnabled(true);
        user.setRole(roleService.findOneByName(UserRoles.ROLE_ADMIN));
        return userRepository.save(user);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll(new Sort(Sort.Direction.DESC, "username"));
    }

    @Override
    public void deleteUser(User user) {
        user.setEnabled(false);
        changePassword(user);
        userRepository.save(user);
    }

    private void changePassword(User user) {
        //TODO: hashing users password;
    }

}
