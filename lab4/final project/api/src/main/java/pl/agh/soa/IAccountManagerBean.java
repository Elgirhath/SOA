package pl.agh.soa;

import javax.ejb.Local;
import javax.ejb.Stateful;
import java.util.List;

@Local
public interface IAccountManagerBean {
    int getBalance();
    void setBalance(int balance);
    List<Integer> getPurchasedSeatIds();
}
