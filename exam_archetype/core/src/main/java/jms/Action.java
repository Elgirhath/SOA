package jms;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Stateless
public class Action {
    @Inject
    QueueSender queueSender;

    public void call(long id) {
        try {
            int sleepTime = new Random().nextInt(45) + 5;
            TimeUnit.SECONDS.sleep(sleepTime);
            queueSender.callPostAction(id);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
