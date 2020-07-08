import com.google.common.eventbus.Subscribe;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.*;
import javax.xml.soap.Text;
import java.net.InetAddress;

@Stateless
public class TopicReceiver {
    @Resource(mappedName = "java:/jms/queue/SOA_test")
    Queue topic;

    @Inject
    JMSContext context;

    public String receiveMsg() {
        System.out.println("abc");
        return "abc";
//        if (sub == null) {
//            try {
//                Connection connection = cf.createConnection();
//                connection.setClientID(InetAddress.getLocalHost().getHostName());
//                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//                sub = session.createDurableSubscriber(topic, "Sub");
//            }
//            catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//        try {
//            Message message = sub.receive(10);
//            String text = message.getBody(String.class);
//            return text;
//        }
//        catch(Exception ex) {
//            ex.printStackTrace();
//        }
//        return null;
    }
}
