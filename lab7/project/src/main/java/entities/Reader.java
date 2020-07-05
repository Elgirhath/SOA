package entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="readers")
public class Reader implements Serializable, ICopyable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Override
    public Reader copy() {
        Reader copy = new Reader();
        copy.setFirstName(firstName);
        copy.setLastName(lastName);
        copy.setEmail(email);
        return copy;
    }
}