package entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "books")
public class Book implements Serializable, ICopyable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    private String title;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @Column(name = "isbn_number")
    private String isbnNumber;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Override
    public Book copy() {
        Book copy = new Book();
        copy.setTitle(title);
        copy.setAuthor(author);
        copy.setCategory(category);
        copy.setIsbnNumber(isbnNumber);
        return copy;
    }
}
