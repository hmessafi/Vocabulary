package pl.nikowis.services.impl;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.nikowis.entities.User;
import pl.nikowis.repositories.RoleRepository;
import pl.nikowis.repositories.UserRepository;
import pl.nikowis.security.UserRoles;
import pl.nikowis.services.UserService;

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
    private RoleRepository roleRepository;

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
            return null;
        } else if(user.getPassword().equals(dbUser.getPassword())) {
            return dbUser;
        }
        return null;
    }

    @Override
    public User createNewUser(User user) {
        Preconditions.checkNotNull(user);
        user.setRole(roleRepository.findOneByName(UserRoles.ROLE_USER));
        return userRepository.save(user);
    }

    @Override
    public User createNewAdmin(User user) {
        Preconditions.checkNotNull(user);
        user.setRole(roleRepository.findOneByName(UserRoles.ROLE_ADMIN));
        return userRepository.save(user);
    }

}
