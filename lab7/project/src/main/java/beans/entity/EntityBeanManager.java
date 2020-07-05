package beans.entity;

import beans.PersistenceBeanManager;
import entities.ICopyable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.NotFoundException;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class EntityBeanManager<T extends ICopyable> extends PersistenceBeanManager {
    protected abstract T getEmpty();

    protected abstract ArrayList<T> loadItems();

    protected void reassignReferences(T item) {}

    public abstract ArrayList<T> getAll();

    ArrayList<T> items;

    EntityBeanManager() {
        super();

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

    protected T add(T item) {
        Session session = entityManager.unwrap(Session.class);
        Transaction t = session.beginTransaction();
        T copy = (T) item.copy();
        session.save(copy);
        t.commit();
        return copy;
    }

    protected void update(T item) {
        entityManager.getTransaction().begin();
        entityManager.merge(item);
        entityManager.getTransaction().commit();
    }

    protected void remove(T item) {
        entityManager.getTransaction().begin();
        entityManager.remove(item);
        entityManager.getTransaction().commit();
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

    protected ArrayList<T> getAll(Class<T> type) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(type);
        Root<T> rootEntry = query.from(type);

        query.select(rootEntry);

        TypedQuery<T> allQuery = entityManager.createQuery(query);
        return (ArrayList<T>) allQuery.getResultList();
    }
}