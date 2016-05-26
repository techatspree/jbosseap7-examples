package de.akquinet.jbosscc.examples.eap.bean;

import de.akquinet.jbosscc.examples.eap.dynamic.AdminService;
import de.akquinet.jbosscc.examples.eap.dynamic.SecurityService;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.security.auth.Subject;
import java.security.AccessController;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BatchJobImpl implements BatchJob {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(BatchJobImpl.class.getName());

    private final String name;
    private final Principal creatingPrincipal;
    private final Random random = new Random();
    private final LocalDateTime creationTime = LocalDateTime.now();
    private boolean running;

    public BatchJobImpl(final String name) {
        this.name = name;
        this.creatingPrincipal = lookupSecurityService().getCallerPrincipal();
        log("CREATED");
    }

    @Override
    public String call() throws Exception {
        log("START");

        work();

        log("DONE");
        return name;
    }

    public void work() throws Exception {
        logSubject();

        running = true;

        for (int i = 0; i < random.nextInt(200) + 1; i++) {
            Thread.sleep(2000L);
            log("RUNNING " + i);
        }
    }

    private void logSubject() {
        final Subject subject = Subject.getSubject(AccessController.getContext());

        if (subject != null) {
            log("Subject is " + subject.getPrincipals());
        } else {
            log("Subject is NULL");
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    private void log(final String msg) {
        final String message = name + ":[" + Thread.currentThread().getName() + "](" + creationTime + "): " + msg;
        final String principalInfo = lookupSecurityService().getPrincipal();

        LOGGER.info(message);
        LOGGER.info("Principal: " + principalInfo);
        LOGGER.info("Call admin method: " + callAdminMethod());

        final FacesContext facesContext = FacesContext.getCurrentInstance();

        // Does not work after Thread.sleep anymore. Instance is thread-local and bound to the request
        if (facesContext != null) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
        } else {
            LOGGER.warning("Faces context not found");
        }
    }

    private SecurityService lookupSecurityService() {
        try {
            final InitialContext context = new InitialContext();
            return (SecurityService) context.lookup("java:global/eap-jumble/SecurityService");
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "lookup", e);

            throw new RuntimeException("Lookup");
        }
    }

    private String callAdminMethod() {
        try {
            final InitialContext context = new InitialContext();
            final AdminService service = (AdminService) context.lookup("java:global/eap-jumble/AdminService");
            return service.secretAdminMethod();
        } catch (final Exception e) {
            return e.getClass().getName();
        }
    }


    @Override
    public int hashCode() {
        return (name + "@" + creationTime).hashCode();
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final BatchJobImpl that = (BatchJobImpl) o;

        return this.name.equals(that.name) && this.creationTime.equals(that.creationTime);
    }

    // Propagation works for all methods with the exception of hashCode, equals, toString
    // and all other methods declared in Object!
    @Override
    public String toString() {
        return name + "@" + creationTime +
            (isRunning() ? " running" : " not running") +
            ", created by: " + creatingPrincipal.getName() + ", current user context: " + lookupSecurityService().getPrincipal();
    }

    @Override
    public String getName() {
        return name;
    }
}
