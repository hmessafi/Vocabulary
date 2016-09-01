package pl.nikowis;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.nikowis.entities.Role;
import pl.nikowis.entities.User;
import pl.nikowis.security.UserRoles;
import pl.nikowis.services.RoleService;
import pl.nikowis.services.UserService;
import pl.nikowis.services.WordService;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = VocabularyApplication.class
        , webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class VocabularyApplicationTests {

    @Autowired
    private RoleService roleService;

    @Autowired
    private WordService wordService;

    @Autowired
    private UserService userService;

    @Test
    public void roleServiceTests() {
        String roleName = "testRole";
        roleService.save(new Role(roleName));
        Assert.assertEquals(roleName, roleService.findOneByName(roleName).getName());
    }

    @Test()
    public void userServiceTests() {
        String testUserUsername = "testUser";
        String testPassword = "1111";
        String testAdminUsername = "testAdmin";
        User admin = userService.createNewAdmin(new User(testAdminUsername, testPassword));
        Assert.assertEquals(testAdminUsername, admin.getUsername());
        User user = userService.createNewAdmin(new User(testUserUsername, testPassword));
        Assert.assertEquals(testUserUsername, user.getUsername());

        admin.setPassword(testPassword);
        User dbAdmin = userService.authenticateUser(admin);
        Assert.assertEquals(admin.getId(), dbAdmin.getId());
        Assert.assertEquals(UserRoles.ROLE_ADMIN, dbAdmin.getRole().getName());

        user.setPassword(testPassword);
        User dbUser = userService.authenticateUser(user);
        Assert.assertEquals(user.getId(), dbUser.getId());
        Assert.assertEquals(UserRoles.ROLE_USER, dbUser.getRole().getName());
    }

    @Test
    public void wordServiceTests() {

    }

}
