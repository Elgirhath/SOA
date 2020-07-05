package beans.search;

import beans.entity.BookBeanManager;
import entities.Book;
import entities.Reader;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Named("SearchReadersByBookManager")
@SessionScoped
public class SearchReadersByBookManager implements Serializable {
    private EntityManager entityManager;

    @Inject
    private BookBeanManager bookBeanManager;

    @Getter
    @Setter
    private int bookId;

    @Getter
    @Setter
    private Date minDate = new Date(2019 - 1900, Calendar.JANUARY, 1 + 1);

    @Getter
    @Setter
    private Date maxDate = new Date(2020 - 1900, Calendar.DECEMBER, 31 + 1);

    public SearchReadersByBookManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("soa_lab6");
        entityManager = factory.createEntityManager();
    }

    public List<Book> getBooks() {
        return new ArrayList<>();
    }

    public List<Reader> getResult() {
        if (bookId <= 0) {
            return new ArrayList<>();
        }

        String readerQueryCmd =
                "SELECT  l.reader FROM Borrowing l join l.book b WHERE b.id=:bookId" +
                        " and (l.borrowDate <= :maxDate) and (:minDate <= l.borrowDate)";

        TypedQuery<Reader> readerQuery = entityManager.createQuery(readerQueryCmd, Reader.class);
        readerQuery.setParameter("bookId", bookId);
        readerQuery.setParameter("minDate", minDate);
        readerQuery.setParameter("maxDate", maxDate);

        return readerQuery.getResultList();
    }
}
