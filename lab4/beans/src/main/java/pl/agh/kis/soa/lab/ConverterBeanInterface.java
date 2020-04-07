package pl.agh.kis.soa.lab;


import javax.ejb.Remote;

@Remote
public interface ConverterBeanInterface {
    double celsius2fahrenheit(double temp);
    double fahrenheit2celsius(double temp);
}
