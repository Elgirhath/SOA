import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named("BackingBean")
@SessionScoped
public class BackingBean implements Serializable {
    @Inject
    private TopicSender topicSender;

    @Inject
    private TopicReceiver topicReceiver;

    public void send() {
        topicSender.sendMsg("Test!!!!!!");
    }

    public void receive() {
        String msg = topicReceiver.receiveMsg();
        System.out.println(msg);
    }
}
