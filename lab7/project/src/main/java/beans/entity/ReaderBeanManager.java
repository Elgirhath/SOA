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
    public ArrayList<Reader> getAll() {
        return getAll(Reader.class);
    }
}
