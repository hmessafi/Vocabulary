package pl.nikowis;

import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.nikowis.entities.Role;
import pl.nikowis.entities.User;
import pl.nikowis.entities.Word;
import pl.nikowis.security.UserRoles;
import pl.nikowis.services.RoleService;
import pl.nikowis.services.UserService;
import pl.nikowis.services.WordService;

import java.lang.reflect.Field;

/**
 * Class for initializating basic data into the database.
 * Created by nikowis on 2016-08-15.
 *
 * @author nikowis
 */
@SpringComponent
public class DatabaseInitializer {

    private final int TOTAL_WORDS = 15;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private WordService wordService;

    public void populate() {
        createRoles();
        createUsers();
    }

    private void createUsers() {
        User user = new User("user", "1");
        User admin = new User("admin", "1");
        User dbAdmin = userService.createNewAdmin(admin);
        User dbUser = userService.createNewUser(user);
        for (int i = 0; i < TOTAL_WORDS; i++) {
            Word word = new Word();
            word.setOriginal("o" + i);
            word.setTranslated("t" + i);
            word.setUser(dbAdmin);
            wordService.save(word);
            word.setId(null);
            word.setUser(dbUser);
            wordService.save(word);
        }
    }

    private void createRoles() {
        Class<UserRoles> userRolesClass = UserRoles.class;
        Field[] fields = userRolesClass.getFields();
        for (Field field : fields) {
            try {
                String role = (String) field.get(null);
                roleService.save(new Role(role));
            } catch (IllegalAccessException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
