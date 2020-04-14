package pl.agh.soa;

import javax.ejb.Singleton;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class SeatManagerBean implements ISeatManagerBean, Serializable {
    List<Seat> seats;

    public SeatManagerBean() {
        seats = new ArrayList<Seat>();
        for(int i = 0; i < 10; i++) {
            final int cost = i/2 * 10 + 50;
            final int seatId = i;
            seats.add(new Seat(){
                {
                    id = seatId;
                    isAvailable = true;
                    price = cost;
                }
            });
        }
    }

    public List<Seat> getSeatList() {
        return seats;
    }

    private Seat getSeatById(int seatId) {
        return seats.stream().filter(s -> s.id == seatId).findFirst().get();
    }

    public int getSeatPrice(int seatId) {
        return getSeatById(seatId).price;
    }

    public void buyTicket(int seatId) {
        Seat seat = getSeatById(seatId);
        seat.isAvailable = false;
    }

    public boolean isSeatAvailable(int seatId) {
        return getSeatById(seatId).isAvailable;
    }
}
