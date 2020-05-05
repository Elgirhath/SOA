package beans.entity;

import entities.Category;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Named("CategoryManager")
@SessionScoped
public class CategoryBeanManager extends EntityBeanManager<Category> {
    @Override
    protected Category getEmpty() {
        return new Category();
    }

    @Override
    protected ArrayList<Category> loadItems() {
        Query q = entityManager.createQuery("select c from Category c", Category.class);
        List<Category> categories = q.getResultList();
        return (ArrayList<Category>) categories;
    }

    @Override
    protected void remove(Category item) {
        entityManager.getTransaction().begin();
        Query q = entityManager.createQuery("delete from Category c where c.id=:id");
        q.setParameter("id", item.getId());
        q.executeUpdate();
        entityManager.getTransaction().commit();
    }

    @Override
    protected void update(Category item) {
        Category oldCategory = entityManager.find(Category.class, item.getId());
        Category newCategory = item.copy();
        newCategory.setId(oldCategory.getId());
        entityManager.getTransaction().begin();
        entityManager.merge(newCategory);
        entityManager.getTransaction().commit();
    }
}
