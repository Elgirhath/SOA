package beans.entity;


import entities.Book;
import entities.BorrowingAvailableNotification;
import entities.Reader;

import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Named(value = "NotificationManager")
@SessionScoped
public class NotificationManager extends EntityBeanManager<BorrowingAvailableNotification> {

    @Resource(mappedName = "java:/jms/queue/SOA_test")
    private Queue queue;

    @JMSConnectionFactory("java:/ConnectionFactory")
    @Inject
    JMSContext context;

    public void SendAvailabilityNotifications(Book book) {
        Query query = entityManager.createQuery("select n from BorrowingAvailableNotification n where n.book = :book");
        query.setParameter("book", book);
        List<BorrowingAvailableNotification> notifications = query.getResultList();
        for(BorrowingAvailableNotification notification : notifications) {
            SendAvailabilityNotification(notification.getReader(), book);
            delete(notification);
        }
    }

    public void SendAvailabilityNotification(Reader reader, Book book) {
        String txt = reader.getId() + "\n" + book.getId();
        context.createProducer().send(queue, txt);
    }

    @Override
    protected BorrowingAvailableNotification getEmpty() {
        return new BorrowingAvailableNotification();
    }

    @Override
    protected ArrayList<BorrowingAvailableNotification> loadItems() {
        return null;
    }

    @Override
    public ArrayList<BorrowingAvailableNotification> getAll() {
        return getAll(BorrowingAvailableNotification.class);
    }
}
