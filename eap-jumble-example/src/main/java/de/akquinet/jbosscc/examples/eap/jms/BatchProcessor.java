package de.akquinet.jbosscc.examples.eap.jms;

import de.akquinet.jbosscc.examples.eap.Resources;
import de.akquinet.jbosscc.examples.eap.bean.BatchJob;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSDestinationDefinition;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

@JMSDestinationDefinition(
    name = Resources.MY_QUEUE,
    interfaceName = "javax.jms.Queue",
    destinationName = Resources.MY_QUEUE_NAME)

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = Resources.MY_QUEUE),

//    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = Resources.TEST_QUEUE),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class BatchProcessor implements MessageListener {
    @Inject
    private Logger log;

    @Override
    public void onMessage(final Message message) {
        try {
            final ObjectMessage omsg = (ObjectMessage) message;

            log.info("MDB: " + whereFrom(this));
            log.info("Message: " + whereFrom(omsg));
            final BatchJob batchJob = (BatchJob) omsg.getObject();

            log.info("JMS executing " + batchJob.getName());

            batchJob.work();
        } catch (final Exception e) {
            log.log(Level.SEVERE, "batch job", e);
        }
    }

    public static String whereFrom(Object o) {
        if (o == null) {
            return null;
        }
        final Class<?> clazz = o.getClass();
        ClassLoader loader = clazz.getClassLoader();

        if (loader == null) {
            // Try the bootstrap classloader - obtained from the ultimate parent of the System Class Loader.
            loader = ClassLoader.getSystemClassLoader();

            while (loader != null && loader.getParent() != null) {
                loader = loader.getParent();
            }
        }

        if (loader != null) {
            final String name = clazz.getCanonicalName();
            final URL resource = loader.getResource(name.replace(".", "/") + ".class");

            if (resource != null) {
                return loader + ":" + resource.toString();
            }
        }

        return "Unknown: " + loader;
    }
}
