package beans.entity;

import entities.Book;
import entities.Inventory;
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

@Named("InventoryManager")
@SessionScoped
public class InventoryBeanManager extends EntityBeanManager<Inventory> {
    @Override
    protected Inventory getEmpty() {
        Inventory inventory = new Inventory();
        Book book = new Book();
        inventory.setBook(book);
        return inventory;
    }

    @Override
    protected ArrayList<Inventory> loadItems() {
        Query q = entityManager.createQuery("select c from Inventory c", Inventory.class);
        List<Inventory> inventories = q.getResultList();
        return (ArrayList<Inventory>) inventories;
    }

    @Override
    protected void remove(Inventory item) {
        entityManager.getTransaction().begin();
        Query q = entityManager.createQuery("delete from Inventory c where c.id=:id");
        q.setParameter("id", item.getBook().getId());
        q.executeUpdate();
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

    @Override
    protected void update(Inventory item) {
        Inventory oldInventory = entityManager.find(Inventory.class, item.getBook().getId());
        Inventory newInventory = item.copy();
        Book book = entityManager.find(Book.class, oldInventory.getBook().getId());
        newInventory.setBook(book);
        entityManager.getTransaction().begin();
        entityManager.merge(newInventory);
        entityManager.getTransaction().commit();
    }

    @Override
    protected void reassignReferences(Inventory item) {
        InventoryDTO dto = inventoryDtos.get(item.getBook().getId());

        Book book = entityManager.find(Book.class, dto.bookId);
        item.setBook(book);
    }

    Map<Integer, InventoryDTO> inventoryDtos = new HashMap<>();

    public InventoryDTO getInventoryDto(Inventory inventory) {
        if (!inventoryDtos.containsKey(inventory.getBook().getId())) {
            inventoryDtos.put(inventory.getBook().getId(), new InventoryDTO());
        }
        return inventoryDtos.get(inventory.getBook().getId());
    }

    @Data
    public class InventoryDTO {
        int bookId;
    }
}
