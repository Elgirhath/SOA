package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="books")
public class Book implements Serializable{
    @Id
    @GeneratedValue
    @Column(name="id")
    private int id;

    Book(String title, String author, String type, double price, String currencyCode) {
        this.title = title;
        this.author = author;
        this.type = type;
        this.price = price;
        this.currencyCode = currencyCode;
    }

    @Column(name="title")
    private String title;

    @Column(name="author")
    private String author;

    @Column(name="type")
    private String type;

    @Column(name="isbn")
    private String isbnNumber;

    @Column(name="release_date")
    private Date releaseDate;

    @Column(name="price")
    private double price;

    @Column(name="currency_code")
    private String currencyCode;

    public Book copy(Book book) {
        setTitle(book.getTitle());
        setAuthor(book.getAuthor());
        setType(book.getType());
        setCurrencyCode(book.currencyCode);
        setPrice(book.getPrice());
        setIsbnNumber(book.isbnNumber);
        setReleaseDate(book.releaseDate);
        return this;
    }
}
