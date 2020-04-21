import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("JPA-zajecia");
        EntityManager em = factory.createEntityManager();

        try {
            Student s1 = new Student("adam", "nowak", new Date());
            Student s2 = new Student("marek", "kowalski", new Date());
            Student s3 = new Student("anna", "marchewka", new Date());

            em.getTransaction().begin();
            em.persist(s1);
            em.persist(s2);
            em.persist(s3);
            em.getTransaction().commit();
            System.out.println("a");
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
