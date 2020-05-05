package beans.search;

import clojure.lang.Obj;
import entities.Author;
import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Named("AdvancedSearchManager")
@SessionScoped
public class AdvancedSearchManager implements Serializable {
    private EntityManager entityManager;

    public AdvancedSearchManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("soa_lab6");
        entityManager = factory.createEntityManager();
    }

    @Getter
    @Setter
    private String query;

    public ArrayList<String> getResult() {
        if (query == null || query.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            try {
                TypedQuery<Object[]> readerQuery = entityManager.createQuery(query, Object[].class);

                return makeStringFromListOfObjectArrays(readerQuery.getResultList());
            }
            catch (Exception ex) {
                TypedQuery<Object> readerQuery = entityManager.createQuery(query, Object.class);

                List<Object> results = readerQuery.getResultList();
                return makeStringFromListOfObjects(results);
            }
        }
        catch(Exception ex) {
            ArrayList<String> result = new ArrayList<String>(){{ add(ex.toString()); }};
            return result;
        }
    }

    private ArrayList<String> makeStringFromListOfObjects(List<Object> list) {
        if (list.size() <= 0) { return new ArrayList<>(); }
        ArrayList<String> result = new ArrayList<>();
        for (Object object : list) {
            result.add(makeStringFromObject(object));
        }
        return result;
    }

    private ArrayList<String> makeStringFromListOfObjectArrays(List<Object[]> list) {
        if (list.size() <= 0) { return new ArrayList<>(); }
        ArrayList<String> result = new ArrayList<>();
        for (Object[] objects : list) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < objects.length; i++) {
                if (i > 0) sb.append(", ");
                sb.append(makeStringFromObject(objects[i]));
            }
            result.add(sb.toString());
        }
        return result;
    }



    private String makeStringFromObject(Object object) {
        if (object instanceof Integer) {
            return String.valueOf((int) object);
        }
        if (object instanceof Long) {
            return String.valueOf((long) object);
        }
        if (object instanceof String) {
            return (String) object;
        }
        if (object instanceof Date) {
            return object.toString();
        }
        return object.toString();
    }
}
