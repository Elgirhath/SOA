package jms;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven(mappedName = "java:/jms/queue/SOA_test", activationConfig =
        {
                @ActivationConfigProperty(propertyName="destination", propertyValue="java:/jms/queue/SOA_test")
        })
public class MessageDrivenBean implements MessageListener {
    @Inject
    Action action;

    @Inject
    PostAction postAction;

    @Override
    public void onMessage(Message message) {

        try {
            String txt = message.getBody(String.class);
            String[] parts = txt.split(":");
            long id = Long.parseLong(parts[1]);
            if (parts[0].equals("action")) {
                action.call(id);
            }
            else {
                postAction.call(id);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}