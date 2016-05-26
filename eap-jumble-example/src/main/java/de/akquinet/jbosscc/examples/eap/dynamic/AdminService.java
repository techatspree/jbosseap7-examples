package de.akquinet.jbosscc.examples.eap.dynamic;

import org.jboss.ejb3.annotation.SecurityDomain;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.logging.Logger;

@Stateless
@RolesAllowed("admin")
@SecurityDomain("jumble-domain")
public class AdminService {
    @Inject
    private Logger log;

    public String secretAdminMethod() {
        log.info("Admin method called");

        return "SUCCESS!";
    }
}
