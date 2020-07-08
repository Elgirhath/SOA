import org.apache.activemq.artemis.jms.client.ActiveMQTopic;

import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.jms.*;
import javax.inject.Inject;

@Stateless
public class TopicSender {
    @Resource(mappedName = "java:/jms/queue/SOA_test")
    private Queue topic;

    @JMSConnectionFactory("java:/ConnectionFactory")
    @Inject
    JMSContext context;

    public void sendMsg(String txt) {
        try {
            context.createProducer().send(topic, txt);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
