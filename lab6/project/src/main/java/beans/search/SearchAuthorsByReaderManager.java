package beans.search;

import entities.Author;
import entities.Book;
import entities.Reader;
import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.SessionScoped;
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

@Named("SearchAuthorsByReaderManager")
@SessionScoped
public class SearchAuthorsByReaderManager implements Serializable {
    private EntityManager entityManager;

    @Getter
    @Setter
    private int readerId;

    @Getter
    @Setter
    private Date minDate = new Date(2019 - 1900, Calendar.JANUARY, 1 + 1);

    @Getter
    @Setter
    private Date maxDate = new Date(2020 - 1900, Calendar.DECEMBER, 31 + 1);

    public SearchAuthorsByReaderManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("soa_lab6");
        entityManager = factory.createEntityManager();
    }

    public List<Author> getResult() {
        if (readerId <= 0) {
            return new ArrayList<>();
        }

        String readerQueryCmd =
                "SELECT b.author FROM Borrowing l join l.book b WHERE l.reader.id=:readerId" +
                        " and (l.borrowDate <= :maxDate) and (:minDate <= l.borrowDate)";

        TypedQuery<Author> readerQuery = entityManager.createQuery(readerQueryCmd, Author.class);
        readerQuery.setParameter("readerId", readerId);
        readerQuery.setParameter("minDate", minDate);
        readerQuery.setParameter("maxDate", maxDate);

        return readerQuery.getResultList();
    }

    public ArrayList<Reader> getReaders() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Reader> query = cb.createQuery(Reader.class);
        Root<Reader> rootEntry = query.from(Reader.class);

        query.select(rootEntry);

        TypedQuery<Reader> allQuery = entityManager.createQuery(query);
        return (ArrayList<Reader>) allQuery.getResultList();
    }
}
