package beans.entity;

import entities.Reader;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Named("ReaderManager")
@SessionScoped
public class ReaderBeanManager extends EntityBeanManager<Reader> {
    @Override
    protected Reader getEmpty() {
        return new Reader();
    }

    @Override
    protected ArrayList<Reader> loadItems() {
        Query q = entityManager.createQuery("select r from Reader r order by r.id", Reader.class);
        List<Reader> readers = q.getResultList();
        return (ArrayList<Reader>) readers;
    }

    @Override
    protected void remove(Reader item) {
        Session session = entityManager.unwrap(Session.class);
        Transaction t = session.beginTransaction();
        Reader dbReader = session.find(Reader.class, item.getId());
        session.delete(dbReader);
        t.commit();
    }

    @Override
    protected void update(Reader item) {
        Reader oldReader = entityManager.find(Reader.class, item.getId());
        Reader newReader = item.copy();
        newReader.setId(oldReader.getId());
        entityManager.getTransaction().begin();
        entityManager.merge(newReader);
        entityManager.getTransaction().commit();
    }
}
