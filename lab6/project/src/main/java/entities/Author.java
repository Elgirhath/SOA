package entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "authors")
public class Author implements ICopyable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @JsonManagedReference
    @OneToMany(mappedBy = "author", targetEntity = Book.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Book> books;

    public String toString() {
        return firstName + " " + lastName;
    }

    public Author copy() {
        Author copy = new Author();
        copy.setFirstName(firstName);
        copy.setLastName(lastName);
        return copy;
    }
}
