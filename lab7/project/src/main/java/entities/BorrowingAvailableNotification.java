package entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "borrowing_available_notifications")
public class BorrowingAvailableNotification implements ICopyable{
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "reader_id")
    private Reader reader;

    @OneToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Override
    public Object copy() {
        BorrowingAvailableNotification copy = new BorrowingAvailableNotification();
        copy.book = book;
        copy.reader = reader;
        return copy;
    }
}
