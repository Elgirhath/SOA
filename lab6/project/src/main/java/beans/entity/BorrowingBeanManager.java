package beans.entity;

import entities.Book;
import entities.Borrowing;
import entities.Reader;
import lombok.Data;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named("BorrowingManager")
@SessionScoped
public class BorrowingBeanManager extends EntityBeanManager<Borrowing> {
    @Override
    protected Borrowing getEmpty() {
        Borrowing b = new Borrowing();
        b.setReader(new Reader());
        return b;
    }

    @Override
    protected ArrayList<Borrowing> loadItems() {
        Query q = entityManager.createQuery("select b from Borrowing b order by b.id", Borrowing.class);
        List<Borrowing> borrowings = q.getResultList();
        return (ArrayList<Borrowing>) borrowings;
    }

    @Override
    protected void remove(Borrowing item) {
        Session session = entityManager.unwrap(Session.class);
        Transaction t = session.beginTransaction();
        Borrowing dbBorrowing = session.find(Borrowing.class, item.getId());
        session.delete(dbBorrowing);
        t.commit();
    }

    @Override
    protected void update(Borrowing item) {
        Borrowing oldBorrowing = entityManager.find(Borrowing.class, item.getId());
        Borrowing newBorrowing = item.copy();
        newBorrowing.setId(oldBorrowing.getId());
        entityManager.getTransaction().begin();
        entityManager.merge(newBorrowing);
        entityManager.getTransaction().commit();
    }

    public ArrayList<Book> getBooks() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = cb.createQuery(Book.class);
        Root<Book> rootEntry = query.from(Book.class);

        query.select(rootEntry);

        TypedQuery<Book> allQuery = entityManager.createQuery(query);
        return (ArrayList<Book>) allQuery.getResultList();
    }

    public ArrayList<Reader> getReaders() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Reader> query = cb.createQuery(Reader.class);
        Root<Reader> rootEntry = query.from(Reader.class);

        query.select(rootEntry);

        TypedQuery<Reader> allQuery = entityManager.createQuery(query);
        return (ArrayList<Reader>) allQuery.getResultList();
    }

    public String getReaderName(Reader r) {
        if (r == null) {
            return "";
        }
        return r.getFirstName() + " " + r.getLastName();
    }

    @Override
    protected void add(Borrowing item) {
        Session session = entityManager.unwrap(Session.class);
        Transaction t = session.beginTransaction();
        session.save(item.copy());
        t.commit();
    }

    @Override
    protected void reassignReferences(Borrowing item) {
        BorrowingDTO dto= borrowingDtos.get(item.getId());

        Reader reader = entityManager.find(Reader.class, dto.readerId);
        item.setReader(reader);

        if (item.getBook() == null) {
            item.setBook(new ArrayList<>());
        }
        item.getBook().clear();

        ArrayList<Integer> bookIds = dto.bookIds;
        for(int id : bookIds) {
            Book book = entityManager.find(Book.class, id);
            item.getBook().add(book);
        }
        borrowingDtos.get(item.getId()).bookIds.clear();
    }

    Map<Integer, BorrowingDTO> borrowingDtos = new HashMap<>();

    public BorrowingDTO getBorrowingDto(Borrowing borrowing) {
        if (!borrowingDtos.containsKey(borrowing.getId())) {
            BorrowingDTO dto = new BorrowingDTO();

            if (borrowing.getBook() != null) {
                dto.bookIds = borrowing.getBook().stream()
                        .map(Book::getId)
                        .collect(Collectors.toCollection(ArrayList::new));
            }
            else {
                dto.bookIds = new ArrayList<>();
            }

            borrowingDtos.put(borrowing.getId(), dto);
        }
        return borrowingDtos.get(borrowing.getId());
    }

    @Data
    public class BorrowingDTO {
        ArrayList<Integer> bookIds;
        int readerId;
    }
}
