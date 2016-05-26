package de.akquinet.jbosscc.examples.eap.jms;

import de.akquinet.jbosscc.examples.eap.Resources;
import de.akquinet.jbosscc.examples.eap.bean.JmsBatchJob;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.jms.DeliveryMode;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;

@Singleton
public class JmsService {
    @Resource(lookup = Resources.MY_QUEUE)
    private Queue myQueue;

    @Inject
    private JMSContext jmsContext;

    public void submitBatchToJms(final String name) {
        final Message message = jmsContext.createObjectMessage(new JmsBatchJob(name));
        final JMSProducer jmsProducer = jmsContext.createProducer();
        jmsProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
        jmsProducer.send(myQueue, message);

    }
}
