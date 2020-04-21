import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table( name = "student" )
public class Student {
    @Id
    @GeneratedValue
    @Column(name="id", nullable=false, insertable = false)
    private int id;

    public Student(String firstName, String lastName, Date createDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.createDate = createDate;
    }

    @Column(name="first_name", nullable = false)
    private String firstName;
    @Column(name="last_name", nullable = false)
    private String lastName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_at", nullable = true)
    private Date createDate;
}
