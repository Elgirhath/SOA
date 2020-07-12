package entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "messages")
public class Message implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "data")
    private String data;

    @Column(name = "end_time")
    private Date endTime;
}