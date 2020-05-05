package beans.entity;

import entities.Author;
import entities.Book;
import entities.Category;
import lombok.Data;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named("BookManager")
@SessionScoped
public class BookBeanManager extends EntityBeanManager<Book> {

    @Override
    protected Book getEmpty() {
        Book book = new Book();

        Author author = new Author();
        book.setAuthor(author);

        Category category = new Category();
        book.setCategory(category);
        return book;
    }

    @Override
    protected ArrayList<Book> loadItems() {
        Query q = entityManager.createQuery("select b from Book b order by b.id", Book.class);
        List<Book> books = q.getResultList();
        return (ArrayList<Book>) books;
    }

    protected void remove(Book item) {
        entityManager.getTransaction().begin();
        Query q = entityManager.createQuery("delete from Book b where b.id=:id");
        q.setParameter("id", item.getId());
        q.executeUpdate();
        entityManager.getTransaction().commit();
    }

    @Override
    protected void update(Book item) {
        Book oldBook = entityManager.find(Book.class, item.getId());
        Book newBook = item.copy();
        newBook.setId(oldBook.getId());
        entityManager.getTransaction().begin();
        entityManager.merge(newBook);
        entityManager.getTransaction().commit();
    }

    Map<Integer, BookDto> bookDTOs = new HashMap<>();

    public BookDto getBookProperties(Book book) {
        if (!bookDTOs.containsKey(book.getId())) {
            BookDto dto = new BookDto();

            dto.authorId = book.getAuthor() != null ? book.getAuthor().getId() : 1;
            dto.categoryId = book.getCategory() != null ? book.getCategory().getId() : 1;

            bookDTOs.put(book.getId(), dto);
        }
        return bookDTOs.get(book.getId());
    }

    @Override
    protected void reassignReferences(Book item) {
        int authorId = bookDTOs.get(item.getId()).authorId;
        Author author = entityManager.find(Author.class, authorId);
        item.setAuthor(author);

        int categoryId = bookDTOs.get(item.getId()).categoryId;
        Category category = entityManager.find(Category.class, categoryId);
        item.setCategory(category);
    }

    public String getAuthorName(Author author) {
        return author.toString();
    }

    public ArrayList<Author> getAuthors() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Author> query = cb.createQuery(Author.class);
        Root<Author> rootEntry = query.from(Author.class);

        query.select(rootEntry);

        TypedQuery<Author> allQuery = entityManager.createQuery(query);
        return (ArrayList<Author>) allQuery.getResultList();
    }

    public ArrayList<Category> getCategories() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Category> query = cb.createQuery(Category.class);
        Root<Category> rootEntry = query.from(Category.class);

        query.select(rootEntry);

        TypedQuery<Category> allQuery = entityManager.createQuery(query);
        return (ArrayList<Category>) allQuery.getResultList();
    }

    @Data
    public class BookDto {
        int authorId;
        int categoryId;
    }
}