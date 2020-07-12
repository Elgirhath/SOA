package db;

import javax.ejb.Stateless;

import entity.Message;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;

@Stateless
@TransactionManagement(value= TransactionManagementType.BEAN)
public class DatabaseManager {
    public long saveData(String data) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("JPA-zajecia");
        EntityManager entityManager = factory.createEntityManager();

        Session session = entityManager.unwrap(Session.class);

        try {
            Transaction t = session.beginTransaction();
            Message addedRecord = new Message();
            addedRecord.setStartTime(new Date());
            addedRecord.setData(data);
            session.save(addedRecord);
            t.commit();
            return addedRecord.getId();
        }
        catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public Message getMessage(long id) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("JPA-zajecia");
        EntityManager entityManager = factory.createEntityManager();

        Session session = entityManager.unwrap(Session.class);

        try {
            return session.get(Message.class, (int) id);
        }
        catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void saveEndTime(long id, Date date) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("JPA-zajecia");
        EntityManager entityManager = factory.createEntityManager();

        Session session = entityManager.unwrap(Session.class);

        try {
            Message message = getMessage(id);
            Transaction t = session.beginTransaction();
            message.setEndTime(date);
            session.update(message);
            t.commit();
        }
        catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
}