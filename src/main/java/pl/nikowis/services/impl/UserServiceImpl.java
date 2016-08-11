package pl.nikowis.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.nikowis.entities.User;
import pl.nikowis.repositories.UserRepository;
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

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User authenticateUser(User user) {
        User dbUser = userRepository.findOneByUsername(user.getUsername());
        if(dbUser == null) {
            return null;
        } else if(user.getPassword().equals(dbUser.getPassword())) {
            return dbUser;
        }
        return null;
    }

    @Override
    public User createUser(User user) {
        return saveUser(user);
    }
}
