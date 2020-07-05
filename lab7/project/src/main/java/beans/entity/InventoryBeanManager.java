package beans.entity;

import entities.Book;
import entities.Inventory;
import lombok.Data;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Persistence;
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

    @Inject
    NotificationManager notificationManager;

    public ArrayList<Inventory> getItems() {
        return new ArrayList<>(items);
    }

    public void reload() {
        items = loadItems();
    }

    @Override
    protected Inventory getEmpty() {
        Inventory inventory = new Inventory();
        Book book = new Book();
        inventory.setBook(book);
        return inventory;
    }

    @Override
    protected ArrayList<Inventory> loadItems() {
        Query q = entityManager.createQuery("select c from Inventory c order by c.book.id", Inventory.class);
        List<Inventory> inventories = q.getResultList();
        return (ArrayList<Inventory>) inventories;
    }

    @Override
    protected void update(Inventory item) {
        entityManager.clear();
        Inventory oldInventory = entityManager.find(Inventory.class, item.getBook().getId());
        if (oldInventory.getCount() == 0 && item.getCount() != 0) {
            notificationManager.SendAvailabilityNotifications(item.getBook());
        }
        super.update(item);
    }

    @Override
    public ArrayList<Inventory> getAll() {
        return getAll(Inventory.class);
    }
}
