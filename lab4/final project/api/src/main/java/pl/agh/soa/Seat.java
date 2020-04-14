package pl.agh.soa;

import lombok.Data;

import java.io.Serializable;

@Data
public class Seat implements Serializable {
    public int id;
    public boolean isAvailable = true;
    public int price;
}