package beans.entity;

import beans.LoginManager;
import entities.*;
import lombok.Data;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named("BookManager")
@SessionScoped
public class BookBeanManager extends EntityBeanManager<Book> {

    @Inject
    LoginManager loginManager;

    @Inject
    InventoryBeanManager inventoryBeanManager;

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

    @Override
    protected Book add(Book item) {
        Book saved = super.add(item);

        loginManager.processNewBookNotification(item);
        Inventory inventory = new Inventory() {{
            setBook(saved);
            setCount(10);
        }};
        inventoryBeanManager.add(inventory);
        inventoryBeanManager.reload();
        return saved;
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

    @Override
    public ArrayList<Book> getAll() {
        return getAll(Book.class);
    }

    public String getAuthorName(Author author) {
        return author.toString();
    }


    @Data
    public class BookDto {
        int authorId;
        int categoryId;
    }
}