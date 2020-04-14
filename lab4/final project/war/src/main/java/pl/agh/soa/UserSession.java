package pl.agh.soa;

import lombok.Getter;
import lombok.Setter;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Named("UserSession")
@SessionScoped
public class UserSession implements Serializable {

    @EJB
    private ISeatManagerBean seatsManager;

    @EJB
    private IAccountManagerBean accountManager;

    public int getBalance() {
        return accountManager.getBalance();
    }

    public void buy(int seatId) {
        seatsManager.buyTicket(seatId);
        int ticketPrice = seatsManager.getSeatPrice(seatId);
        accountManager.setBalance(accountManager.getBalance() - ticketPrice);
        accountManager.getPurchasedSeatIds().add(seatId);
    }

    public ArrayList<Seat> getSeats() {
        return (ArrayList<Seat>) seatsManager.getSeatList();
    }

    public boolean isNotAvailable(int seatId) {
        int ticketPrice = seatsManager.getSeatPrice(seatId);
        return ticketPrice > accountManager.getBalance() || !seatsManager.isSeatAvailable(seatId);
    }

    public ArrayList<Seat> getPurchasedSeats() {
        List<Seat> seats = seatsManager.getSeatList();
        List<Integer> ids = accountManager.getPurchasedSeatIds();
        return seats.stream().filter(s -> ids.contains(s.id)).collect(Collectors.toCollection(ArrayList::new));
    }
}
