package beans.entity;

import entities.Book;
import entities.Borrowing;
import entities.Inventory;
import entities.Reader;
import lombok.Data;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named("BorrowingManager")
@SessionScoped
public class BorrowingBeanManager extends EntityBeanManager<Borrowing> {

    @Inject
    InventoryBeanManager inventoryBeanManager;

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

    public String getReaderName(Reader r) {
        if (r == null) {
            return "";
        }
        return r.getFirstName() + " " + r.getLastName();
    }

    @Override
    protected Borrowing add(Borrowing item) {
        List<Book> books = item.getBook();
        for (Book book : books) {
            Inventory inventory = entityManager.find(Inventory.class, book.getId());
            if (inventory.getCount() < 1) {
                throw new IllegalStateException("Inventory for book " + book.getTitle() + " is 0");
            }
        }
        Borrowing saved = super.add(item);
        for (Book book : books) {
            Inventory inventory = entityManager.find(Inventory.class, book.getId());
            inventory.setCount(inventory.getCount() - 1);
            inventoryBeanManager.update(inventory);
        }
        inventoryBeanManager.reload();
        return saved;
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

    @Override
    public ArrayList<Borrowing> getAll() {
        return getAll(Borrowing.class);
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
