package pl.agh.kis.soa.ejb3.server.impl;

import pl.agh.kis.soa.ejb3.server.api.ILocalTestAddBean;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

@Stateless
@LocalBean
public class TestAddBean implements ILocalTestAddBean {
    public TestAddBean() {

    }

    @Override
    public int add(int a, int b) {
        int r = a + b;
        return r;
    }
}
