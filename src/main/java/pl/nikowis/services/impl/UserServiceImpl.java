package pl.nikowis.services.impl;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.nikowis.entities.User;
import pl.nikowis.exceptions.NoSuchUsernameException;
import pl.nikowis.exceptions.UserIsDeletedException;
import pl.nikowis.exceptions.UsernameAlreadyInUse;
import pl.nikowis.exceptions.WrongPasswordException;
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

    private final int DELETED_PASSWORD_LENGTH =5;

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    public UserServiceImpl() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

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
            throw new NoSuchUsernameException();
        }
        if(!dbUser.isEnabled()) {
            throw new UserIsDeletedException();
        }
        if(!passwordEncoder.matches(user.getPassword(),dbUser.getPassword())) {
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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setRole(roleService.findOneByName(UserRoles.ROLE_USER));
        return userRepository.save(user);
    }

    @Override
    public User createNewAdmin(User user) {
        Preconditions.checkNotNull(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
        user.setPassword(passwordEncoder.encode(RandomStringUtils.random(DELETED_PASSWORD_LENGTH)));
    }

}
