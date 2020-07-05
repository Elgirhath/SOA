package beans.entity;

import entities.Author;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Named("AuthorManager")
@SessionScoped
public class AuthorBeanManager extends EntityBeanManager<Author> {
    @Override
    protected Author getEmpty() {
        return new Author();
    }

    @Override
    protected ArrayList<Author> loadItems() {
        Query q = entityManager.createQuery("select a from Author a order by a.id", Author.class);
        List<Author> authors = q.getResultList();
        return (ArrayList<Author>) authors;
    }

    @Override
    public ArrayList<Author> getAll() {
        return getAll(Author.class);
    }
}
