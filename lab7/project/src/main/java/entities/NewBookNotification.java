package entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "new_book_notifications")
public class NewBookNotification implements Serializable, ICopyable{
    @Id
    @OneToOne
    @JoinColumn(name = "reader_id")
    private Reader reader;

    @Override
    public Object copy() {
        NewBookNotification copy = new NewBookNotification();
        copy.reader = reader;
        return copy;
    }
}
