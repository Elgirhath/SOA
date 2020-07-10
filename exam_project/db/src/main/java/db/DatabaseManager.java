package db;

import entity.TimestampEntity;

import javax.ejb.Stateless;
import org.hibernate.Session;
import org.hibernate.Transaction;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;

@Stateless
public class DatabaseManager {
    public void saveStartTime(Date time) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("JPA-zajecia");
        EntityManager entityManager = factory.createEntityManager();

        Session session = entityManager.unwrap(Session.class);

        try {
            Transaction t = session.beginTransaction();
            TimestampEntity addedRecord = new TimestampEntity();
            addedRecord.setStartTime(time);
            session.save(addedRecord);
            t.commit();
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}