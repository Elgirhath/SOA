package beans.entity;

import entities.ICopyable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.NotFoundException;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class EntityBeanManager<T extends ICopyable> implements Serializable {
    protected EntityManager entityManager;
    protected abstract T getEmpty();

    protected abstract ArrayList<T> loadItems();
    protected abstract void remove(T item);
    protected abstract void update(T item);

    protected void reassignReferences(T item) {}

    ArrayList<T> items;

    EntityBeanManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("soa_lab6");
        entityManager = factory.createEntityManager();

        items = loadItems();

        added = getEmpty();
    }

    public ArrayList<T> getItems() {
        ArrayList<T> joinedItems = new ArrayList<>(items);
        joinedItems.add(added);
        return joinedItems;
    }

    @Getter
    @Setter
    private T edited = null;
    public boolean isEdited(@NotNull T item) {
        return item.equals(edited);
    }

    @Getter
    @Setter
    private T added = null;
    public boolean isAdded(@NotNull T item) {
        return item.equals(added);
    }

    public boolean isEditable(T item) {
        if (item == null) {
            return false;
        }
        return isEdited(item) || isAdded(item);
    }

    public void save(T item) {
        reassignReferences(item);

        if (isAdded(item)) {
            add(item);
            added = getEmpty();
        }
        if (isEdited(item)) {
            update(item);
            edited = null;
        }

        items = loadItems();
    }

    protected void add(T item) {
        Session session = entityManager.unwrap(Session.class);
        Transaction t = session.beginTransaction();
        session.save(item.copy());
        t.commit();
    }

    public void delete(T item) {
        remove(item);

        items = loadItems();
    }

    public T getEditableVersionOf(T item) {
        if (isEdited(item)) {
            return edited;
        }
        if (isAdded(item)) {
            return added;
        }
        throw new NotFoundException();
    }
}