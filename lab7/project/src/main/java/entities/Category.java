package entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "categories")
public class Category implements Serializable, ICopyable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Override
    public Category copy() {
        Category copy = new Category();
        copy.name = name;
        return copy;
    }
}
