package beans.search;

import clojure.lang.Obj;
import entities.Author;
import entities.Book;
import entities.Reader;
import lombok.AllArgsConstructor;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Named("SearchMostReadAuthorsManager")
@SessionScoped
public class SearchMostReadAuthorsManager implements Serializable {
    private EntityManager entityManager;

    public SearchMostReadAuthorsManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("soa_lab6");
        entityManager = factory.createEntityManager();
    }

    public List<OutputDto> getResult() {
        String readerQueryCmd =
                "SELECT bo.author.id, count(bo.id) FROM Borrowing b " +
                "join b.book bo " +
                "group by bo.author.id " +
                "order by count(bo.id) desc";

        TypedQuery<Object[]> readerQuery = entityManager.createQuery(readerQueryCmd, Object[].class);

        List<Object[]> result = readerQuery.getResultList();

        return result.stream()
                .map(objects -> new QueryDto((int) objects[0], (long) objects[1]))
                .map(queryDto -> new OutputDto(entityManager.find(Author.class, queryDto.authorId), queryDto.borrowingCount))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Book> getBooks() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = cb.createQuery(Book.class);
        Root<Book> rootEntry = query.from(Book.class);

        query.select(rootEntry);

        TypedQuery<Book> allQuery = entityManager.createQuery(query);
        return (ArrayList<Book>) allQuery.getResultList();
    }

    @AllArgsConstructor
    @Data
    public class QueryDto {
        int authorId;
        long borrowingCount;
    }

    @AllArgsConstructor
    @Data
    public class OutputDto {
        Author author;
        long borrowingCount;
    }
}
