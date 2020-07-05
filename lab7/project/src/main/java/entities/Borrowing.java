package entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "borrowings")
public class Borrowing implements Serializable, ICopyable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @ManyToMany
    @JoinColumn(name = "book_id")
    private List<Book> book;

    @ManyToOne
    @JoinColumn(name = "reader_id")
    private Reader reader;

    @Column(name = "borrow_date")
    private Date borrowDate;

    @Column(name = "return_date")
    private Date returnDate;

    @Override
    public Borrowing copy() {
        Borrowing copy = new Borrowing();
        copy.setBook(book);
        copy.setReader(reader);
        copy.setBorrowDate(borrowDate);
        copy.setReturnDate(returnDate);
        return copy;
    }
}