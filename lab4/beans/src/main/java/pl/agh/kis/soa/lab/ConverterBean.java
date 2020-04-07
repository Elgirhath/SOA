package pl.agh.kis.soa.lab;

import javax.ejb.Remote;
import javax.ejb.Stateless;

@Remote(ConverterBeanInterface.class)
@Stateless
public class ConverterBean implements ConverterBeanInterface{
    public ConverterBean() {
    }

    @Override
    public double celsius2fahrenheit(double temp) {
        return 9.0/5 * temp + 32;
    }

    @Override
    public double fahrenheit2celsius(double temp) {
        return 5.0/9 * (temp - 32);
    }
}
