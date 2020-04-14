package pl.agh.soa;

import javax.ejb.Lock;
import javax.ejb.Remote;
import java.util.List;

@Remote
public interface ISeatManagerBean {
    @Lock
    List<Seat> getSeatList();
    @Lock
    int getSeatPrice(int seatId);
    @Lock
    void buyTicket(int seatId);
    @Lock
    boolean isSeatAvailable(int seatId);
}