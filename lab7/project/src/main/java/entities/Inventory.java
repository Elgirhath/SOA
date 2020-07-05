package entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "inventory")
public class Inventory implements Serializable, ICopyable {
    @JsonBackReference
    @Id
    @OneToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "count")
    private int count;

    public Inventory copy() {
        Inventory inventory = new Inventory();
        inventory.book = book;
        inventory.count = count;
        return inventory;
    }
}
