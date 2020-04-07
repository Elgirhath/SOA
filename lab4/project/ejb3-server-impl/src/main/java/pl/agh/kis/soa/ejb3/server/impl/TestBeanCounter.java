package pl.agh.kis.soa.ejb3.server.impl;

import pl.agh.kis.soa.ejb3.server.api.ILocalTestBeanCounter;
import pl.agh.kis.soa.ejb3.server.api.ITestBeanCounter;

import javax.ejb.Local;
import javax.ejb.Singleton;

@Singleton
@Local(ILocalTestBeanCounter.class)
public class TestBeanCounter implements ITestBeanCounter {
    long counterNumber = 0;

    @Override
    public void increment() {
        counterNumber++;
    }

    @Override
    public long getNumber() {
        return counterNumber;
    }
}
