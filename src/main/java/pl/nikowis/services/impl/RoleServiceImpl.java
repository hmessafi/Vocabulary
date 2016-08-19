package pl.nikowis.services.impl;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.nikowis.entities.Role;
import pl.nikowis.repositories.RoleRepository;
import pl.nikowis.services.RoleService;

/**
 * Created by nikowis on 2016-08-15.
 *
 * @author nikowis
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role save(Role userRole) {
        Preconditions.checkNotNull(userRole);
        return roleRepository.save(userRole);
    }

    @Override
    public Role findOneByName(String name) {
        Preconditions.checkNotNull(name);
        return roleRepository.findOneByName(name);
    }
}
