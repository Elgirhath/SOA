package beans;

import com.sun.xml.internal.ws.client.RequestContext;
import entities.*;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Named("LoginManager")
@SessionScoped
public class LoginManager extends PersistenceBeanManager {
    @Getter
    private Reader reader;

    @Getter
    @Setter
    private String email;

    public String login() {
        Query query = entityManager.createQuery("select r from Reader r where r.email like :email");
        query.setParameter("email", email);
        query.setMaxResults(1);
        List<Reader> results = query.getResultList();
        if (results.size() == 1) {
            reader = (Reader) query.getSingleResult();
            NotificationBroker.getInstance().addListener(this);
            return "user_panel.xhtml";
        }
        return "";
    }

    private List<BorrowingAvailableNotification> notifications = new ArrayList<>();

    public void processNotification(BorrowingAvailableNotification notification) {
        notifications.add(notification);
    }

    public void processNewBookNotification(Book book) {
        Query query = entityManager.createQuery("select n.reader from NewBookNotification n");
        List<Reader> readers = query.getResultList();

        if (readers.contains(reader)) {
            BorrowingAvailableNotification conv = new BorrowingAvailableNotification();
            conv.setReader(reader);
            conv.setBook(book);
            processNotification(conv);
        }
    }

    public String getNotifications() {
        StringBuilder sb = new StringBuilder();
        for (BorrowingAvailableNotification notification : notifications) {
            sb.append("Book ");
            sb.append(notification.getBook().getTitle());
            sb.append(" is now available at the store!\n");
        }
        return sb.toString();
    }
}
