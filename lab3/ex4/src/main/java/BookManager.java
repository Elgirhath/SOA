import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Named("BookManager")
@SessionScoped
public class BookManager implements Serializable {
    @Getter
    ArrayList<Book> books;
    @Getter
    ArrayList<Book> cart;
    NBPConnector nbpConnector;

    BookManager() {
        books = (ArrayList<Book>)BookRepository.getBooks();
        cart = new ArrayList<>();
        nbpConnector = new NBPConnector();

        List<String> currencyCodes = getAllCurrencyCodes();
        filterCurrencyCodes = currencyCodes.toArray(new String[0]);

        List<String> types = getAllTypes();
        filterTypes = types.toArray(new String[0]);
    }

    public ArrayList<String> getAllTypes() {
        return BookRepository.getBooks().stream().map(b -> b.type).distinct().collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<String> getAllCurrencyCodes() {
        return BookRepository.getBooks().stream().map(b -> b.currencyCode).distinct().collect(Collectors.toCollection(ArrayList::new));
    }

    public void filter() {
        Stream<Book> bookStream = BookRepository.getBooks().stream();
        if (minPrice >= 0.01) {
            bookStream = bookStream.filter(
                    b -> nbpConnector.convert(b.price, b.currencyCode, priceFilterCurrencyCode) >= minPrice
            );
        }
        if (maxPrice >= 0.01) {
            bookStream = bookStream.filter(
                    b -> nbpConnector.convert(b.price, b.currencyCode, priceFilterCurrencyCode) <= maxPrice
            );
        }
        if (filterTypes != null && filterTypes.length > 0) {
            bookStream = bookStream.filter(b -> Arrays.asList(filterTypes).contains(b.type));
        }
        if (filterCurrencyCodes != null && filterCurrencyCodes.length > 0) {
            bookStream = bookStream.filter(b -> Arrays.asList(filterCurrencyCodes).contains(b.currencyCode));
        }

        if (convertCurrencyCode != null && !convertCurrencyCode.isEmpty()) {
            bookStream = convertCurrency(bookStream);
        }

        books = bookStream.collect(Collectors.toCollection(ArrayList::new));
    }

    Stream<Book> convertCurrency(Stream<Book> bookStream) {
        bookStream = bookStream.map(
                b -> {
                    double price = nbpConnector.convert(b.price, b.currencyCode, convertCurrencyCode);
                    BigDecimal bd = BigDecimal.valueOf(price);
                    bd = bd.setScale(2, RoundingMode.HALF_UP);
                    b.price = bd.doubleValue();
                    b.currencyCode = convertCurrencyCode;
                    return b;
                }
        );
        return bookStream;
    }

    public void addToCart(Book book) {
        cart.add(book);
    }

    public double getSumInPLN() {
        double sum = 0.0;

        for (Book book : cart) {
            sum += nbpConnector.convert(book.price, book.currencyCode, "PLN");
        }
        BigDecimal bd = BigDecimal.valueOf(sum);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Getter
    @Setter
    String convertCurrencyCode;

    @Getter
    @Setter
    String[] filterTypes;

    @Getter
    @Setter
    String[] filterCurrencyCodes;

    @Getter
    @Setter
    double minPrice;

    @Getter
    @Setter
    double maxPrice;

    @Getter
    @Setter
    String priceFilterCurrencyCode;
}