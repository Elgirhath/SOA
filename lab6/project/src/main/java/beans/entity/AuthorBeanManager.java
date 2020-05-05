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
    protected void remove(Author author) {
        Session session = entityManager.unwrap(Session.class);
        Transaction t = session.beginTransaction();
        Author dbAuthor = session.find(Author.class, author.getId());
        session.delete(dbAuthor);
        t.commit();
    }

    @Override
    protected void update(Author author) {
        Author oldAuthor = entityManager.find(Author.class, author.getId());
        Author newAuthor = author.copy();
        newAuthor.setId(oldAuthor.getId());
        entityManager.getTransaction().begin();
        entityManager.merge(newAuthor);
        entityManager.getTransaction().commit();
    }
}
