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
    public ArrayList<Category> getAll() {
        return getAll(Category.class);
    }
}
