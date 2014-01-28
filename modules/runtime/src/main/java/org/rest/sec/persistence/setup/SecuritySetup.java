package org.rest.sec.persistence.setup;

import java.util.Set;

import org.rest.common.event.BeforeSetupEvent;
import org.rest.common.spring.util.CommonSpringProfileUtil;
import org.rest.sec.model.Principal;
import org.rest.sec.model.Privilege;
import org.rest.sec.model.Role;
import org.rest.sec.persistence.service.IPrincipalService;
import org.rest.sec.persistence.service.IPrivilegeService;
import org.rest.sec.persistence.service.IRoleService;
import org.rest.sec.util.SecurityConstants;
import org.rest.sec.util.SecurityConstants.Privileges;
import org.rest.sec.util.SecurityConstants.Roles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

@Component
@Profile(CommonSpringProfileUtil.DEPLOYED)
public class SecuritySetup implements ApplicationListener<ContextRefreshedEvent> {
    private final Logger logger = LoggerFactory.getLogger(SecuritySetup.class);

    private boolean setupDone;

    @Autowired
    private IPrincipalService principalService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IPrivilegeService privilegeService;

    @Autowired
    private ApplicationContext eventPublisher;

    public SecuritySetup() {
        super();
    }

    //

    /**
     * - note that this is a compromise - the flag makes this bean statefull which can (and will) be avoided in the future by a more advanced mechanism <br>
     * - the reason for this is that the context is refreshed more than once throughout the lifecycle of the deployable <br>
     * - alternatives: proper persisted versioning
     */
    @Override
    public final void onApplicationEvent(final ContextRefreshedEvent event) {
        if (!setupDone) {
            logger.info("Executing Setup");
            eventPublisher.publishEvent(new BeforeSetupEvent(this));

            createPrivileges();
            createRoles();
            createPrincipals();

            setupDone = true;
            logger.info("Setup Done");
        }
    }

    // Privilege

    private void createPrivileges() {
        createPrivilegeIfNotExisting(Privileges.CAN_PRIVILEGE_READ);
        createPrivilegeIfNotExisting(Privileges.CAN_PRIVILEGE_WRITE);

        createPrivilegeIfNotExisting(Privileges.CAN_ROLE_READ);
        createPrivilegeIfNotExisting(Privileges.CAN_ROLE_WRITE);

        createPrivilegeIfNotExisting(Privileges.CAN_USER_READ);
        createPrivilegeIfNotExisting(Privileges.CAN_USER_WRITE);
    }

    final void createPrivilegeIfNotExisting(final String name) {
        final Privilege entityByName = privilegeService.findByName(name);
        if (entityByName == null) {
            final Privilege entity = new Privilege(name);
            privilegeService.create(entity);
        }
    }

    // Role

    private void createRoles() {
        final Privilege canPrivilegeRead = privilegeService.findByName(Privileges.CAN_PRIVILEGE_READ);
        final Privilege canPrivilegeWrite = privilegeService.findByName(Privileges.CAN_PRIVILEGE_WRITE);
        final Privilege canRoleRead = privilegeService.findByName(Privileges.CAN_ROLE_READ);
        final Privilege canRoleWrite = privilegeService.findByName(Privileges.CAN_ROLE_WRITE);
        final Privilege canUserRead = privilegeService.findByName(Privileges.CAN_USER_READ);
        final Privilege canUserWrite = privilegeService.findByName(Privileges.CAN_USER_WRITE);

        Preconditions.checkNotNull(canPrivilegeRead);
        Preconditions.checkNotNull(canPrivilegeWrite);
        Preconditions.checkNotNull(canRoleRead);
        Preconditions.checkNotNull(canRoleWrite);
        Preconditions.checkNotNull(canUserRead);
        Preconditions.checkNotNull(canUserWrite);

        createRoleIfNotExisting(Roles.ROLE_ADMIN, Sets.<Privilege> newHashSet(canUserRead, canUserWrite, canRoleRead, canRoleWrite, canPrivilegeRead, canPrivilegeWrite));
    }

    final void createRoleIfNotExisting(final String name, final Set<Privilege> privileges) {
        final Role entityByName = roleService.findByName(name);
        if (entityByName == null) {
            final Role entity = new Role(name);
            entity.setPrivileges(privileges);
            roleService.create(entity);
        }
    }

    // Principal/User

    final void createPrincipals() {
        final Role roleAdmin = roleService.findByName(Roles.ROLE_ADMIN);

        // createPrincipalIfNotExisting(SecurityConstants.ADMIN_USERNAME, SecurityConstants.ADMIN_PASS, Sets.<Role> newHashSet(roleAdmin));
        createPrincipalIfNotExisting(SecurityConstants.ADMIN_EMAIL, SecurityConstants.ADMIN_PASS, Sets.<Role> newHashSet(roleAdmin));
    }

    final void createPrincipalIfNotExisting(final String loginName, final String pass, final Set<Role> roles) {
        final Principal entityByName = principalService.findByName(loginName);
        if (entityByName == null) {
            final Principal entity = new Principal(loginName, pass, roles);
            principalService.create(entity);
        }
    }

}
