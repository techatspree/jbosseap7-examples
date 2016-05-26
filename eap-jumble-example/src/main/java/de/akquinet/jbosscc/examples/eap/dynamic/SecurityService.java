package de.akquinet.jbosscc.examples.eap.dynamic;

import org.jboss.ejb3.annotation.SecurityDomain;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import java.security.Principal;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
@RolesAllowed({"user", "admin"})
@SecurityDomain("jumble-domain")
public class SecurityService {
    @Inject
    private Logger log;

    @Resource
    private SessionContext ctx;

    public String getPrincipal() {
        final Principal principal = getCallerPrincipal();

//        logEnvironment();

        return principal.getName();
    }

    public Principal getCallerPrincipal() {
        return ctx.getCallerPrincipal();
    }

    private void logEnvironment() {
        final Map<String, Object> contextData = ctx.getContextData();

        log.info("Context data: " + contextData);

        try {
            final Context namingContext = (Context) ctx.lookup("java:comp/env");
            final NamingEnumeration<NameClassPair> enumeration = namingContext.list("");

            while (enumeration.hasMoreElements()) {
                final NameClassPair nameClassPair = enumeration.nextElement();
                log.info(namingContext.lookup(nameClassPair.getName()).toString());
            }
        } catch (NamingException e) {
            log.log(Level.WARNING, "log environment", e);
        }
    }
}
