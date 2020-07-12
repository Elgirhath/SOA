package jms;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;

@Stateless
public class QueueSender {
    @Inject
    JMSContext context;

    @Resource(mappedName = "java:/jms/queue/SOA_test")
    private Queue queue;

    public void callAction(long id) {
        try {
            context.createProducer().send(queue, "action:" + id);
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public void callPostAction(long id) {
        try {
            context.createProducer().send(queue, "post-action:" + id);
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
