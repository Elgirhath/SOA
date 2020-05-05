package beans.search;

import entities.Author;
import entities.Reader;
import lombok.Data;
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

@Named("SearchReadersByAuthorManager")
@SessionScoped
public class SearchReadersByAuthorManager implements Serializable {
    private EntityManager entityManager;

    @Getter
    @Setter
    private Author author;

    @Getter
    @Setter
    private Date minDate = new Date(2019 - 1900, Calendar.JANUARY, 1 + 1);

    @Getter
    @Setter
    private Date maxDate = new Date(2020 - 1900, Calendar.DECEMBER, 31 + 1);

    public SearchReadersByAuthorManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("soa_lab6");
        entityManager = factory.createEntityManager();
    }

    public List<Reader> getResult() {
        String readerQueryCmd =
                "SELECT distinct l.reader FROM Borrowing l join l.book b WHERE " +
                "(l.borrowDate <= :maxDate) and (:minDate <= l.borrowDate)";

        List<Integer> authorBookIds = null;

        if (author != null) {
            String bookQueryCmd = "select b.id from Book b where b.author.id=:authorId";
            TypedQuery<Integer> bookQuery = entityManager.createQuery(bookQueryCmd, Integer.class);
            bookQuery.setParameter("authorId", author.getId());
            authorBookIds = bookQuery.getResultList();
            if (authorBookIds.isEmpty()) {return new ArrayList<>();}
            readerQueryCmd += " and b.id in (:bookIds)";
        }

        TypedQuery<Reader> readerQuery = entityManager.createQuery(readerQueryCmd, Reader.class);
        if (authorBookIds != null) {
            readerQuery.setParameter("bookIds", authorBookIds);
        }
        readerQuery.setParameter("minDate", minDate);
        readerQuery.setParameter("maxDate", maxDate);

        return readerQuery.getResultList();
    }

    public ArrayList<Author> getAuthors() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Author> query = cb.createQuery(Author.class);
        Root<Author> rootEntry = query.from(Author.class);

        query.select(rootEntry);

        TypedQuery<Author> allQuery = entityManager.createQuery(query);
        return (ArrayList<Author>) allQuery.getResultList();
    }

    @Getter
    @Setter
    SearchDto searchDto = new SearchDto();

    public void applyDto() {
        if (searchDto.authorId > 0) {
            author = entityManager.find(Author.class, searchDto.authorId);
        }
        else {
            author = null;
        }
    }

    @Data
    public class SearchDto {
        int authorId;
    }
}