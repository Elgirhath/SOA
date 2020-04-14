package pl.agh.soa;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.ejb.Stateful;
import java.util.ArrayList;
import java.util.List;

@Stateful
public class AccountManagerBean implements IAccountManagerBean {
    int balance = 150;

    @Override
    public int getBalance() {
        return balance;
    }

    @Override
    public void setBalance(int balance) {
        this.balance = balance;
    }

    List<Integer> purchasedSeatIds = new ArrayList<>();

    @Override
    public List<Integer> getPurchasedSeatIds() {
        return purchasedSeatIds;
    }
}
