package beans;

import entities.Book;
import entities.BorrowingAvailableNotification;
import entities.Reader;
import lombok.SneakyThrows;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.enterprise.context.SessionScoped;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven(mappedName = "java:/jms/queue/SOA_test", activationConfig =
        {
                @ActivationConfigProperty(propertyName="destination", propertyValue="java:/jms/queue/SOA_test")
        })
public class MessageDrivenBean extends PersistenceBeanManager implements MessageListener {

    @Override
    public void onMessage(Message message) {
        String txt;
        try {
            txt = message.getBody(String.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        String[] props = txt.split("\n");
        BorrowingAvailableNotification notification = new BorrowingAvailableNotification();
        Reader reader = entityManager.find(Reader.class, Integer.parseInt(props[0]));
        Book book = entityManager.find(Book.class, Integer.parseInt(props[1]));
        notification.setReader(reader);
        notification.setBook(book);
        NotificationBroker.getInstance().addNotification(notification);
    }
}
