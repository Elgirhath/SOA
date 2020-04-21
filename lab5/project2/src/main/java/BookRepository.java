import entities.Book;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    public static List<Book> getBooks() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("JPA-zajecia");
        EntityManager em = factory.createEntityManager();

        try {
            Query q = em.createQuery("select s from Book s", Book.class);
            List<Book> books = q.getResultList();
            return books;
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return new ArrayList<>();
    }

    public static void remove(Book book) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("JPA-zajecia");
        EntityManager em = factory.createEntityManager();

        try {
            em.getTransaction().begin();
            Query q = em.createQuery("delete from Book b where b.id=:id");
            q.setParameter("id", book.getId());
            q.executeUpdate();
            em.getTransaction().commit();
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public static void update(Book book) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("JPA-zajecia");
        EntityManager em = factory.createEntityManager();

        try {
            Book dbBook = em.find(Book.class, book.getId());
            em.getTransaction().begin();
            dbBook.copy(book);
            em.getTransaction().commit();
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public static void add(Book book) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("JPA-zajecia");
        EntityManager em = factory.createEntityManager();
        Session session = em.unwrap(Session.class);

        try {
            Transaction t = session.beginTransaction();
            Book addedBook = new Book();
            addedBook.copy(book);
            session.save(addedBook);
            t.commit();
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
