import entities.Book;
import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Named("BookManager")
@SessionScoped
public class BookManager implements Serializable {

    Book addedBook;
    ArrayList<Book> books;
    public ArrayList<Book> getBooks() {
        ArrayList<Book> joinedBooks = new ArrayList<>(books);
        joinedBooks.add(addedBook);
        return joinedBooks;
    }

    @Getter
    ArrayList<Book> cart;
    NBPConnector nbpConnector;

    BookManager() {
        books = (ArrayList<Book>)BookRepository.getBooks();
        cart = new ArrayList<>();
        nbpConnector = new NBPConnector();
        addedBook = new Book(-1, "", "", "", "", new Date(), 0.0, "");

        List<String> currencyCodes = getAllCurrencyCodes();
        filterCurrencyCodes = currencyCodes.toArray(new String[0]);

        List<String> types = getAllTypes();
        filterTypes = types.toArray(new String[0]);
    }

    public ArrayList<String> getAllTypes() {
        return BookRepository.getBooks().stream().map(b -> b.getType()).distinct().collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<String> getAllCurrencyCodes() {
        return BookRepository.getBooks().stream().map(b -> b.getCurrencyCode()).distinct().collect(Collectors.toCollection(ArrayList::new));
    }

    public void filter() {
        Stream<Book> bookStream = BookRepository.getBooks().stream();
        if (minPrice >= 0.01) {
            bookStream = bookStream.filter(
                    b -> nbpConnector.convert(b.getPrice(), b.getCurrencyCode(), priceFilterCurrencyCode) >= minPrice
            );
        }
        if (maxPrice >= 0.01) {
            bookStream = bookStream.filter(
                    b -> nbpConnector.convert(b.getPrice(), b.getCurrencyCode(), priceFilterCurrencyCode) <= maxPrice
            );
        }
        if (filterTypes != null && filterTypes.length > 0) {
            bookStream = bookStream.filter(b -> Arrays.asList(filterTypes).contains(b.getType()));
        }
        if (filterCurrencyCodes != null && filterCurrencyCodes.length > 0) {
            bookStream = bookStream.filter(b -> Arrays.asList(filterCurrencyCodes).contains(b.getCurrencyCode()));
        }

        if (convertCurrencyCode != null && !convertCurrencyCode.isEmpty()) {
            bookStream = convertCurrency(bookStream);
        }

        books = bookStream.collect(Collectors.toCollection(ArrayList::new));
    }

    Stream<Book> convertCurrency(Stream<Book> bookStream) {
        bookStream = bookStream.map(
                b -> {
                    double price = nbpConnector.convert(b.getPrice(), b.getCurrencyCode(), convertCurrencyCode);
                    BigDecimal bd = BigDecimal.valueOf(price);
                    bd = bd.setScale(2, RoundingMode.HALF_UP);
                    b.setPrice(bd.doubleValue());
                    b.setCurrencyCode(convertCurrencyCode);
                    return b;
                }
        );
        return bookStream;
    }

    public void remove(Book book) {
        BookRepository.remove(book);
        books = (ArrayList<Book>) BookRepository.getBooks();
    }

    @Getter
    @Setter
    private Book editableBook = null;
    public void setEditable(Book book) {
        editableBook = book;
    }
    public boolean isEditable(Book book) {
        if (book.getId() == addedBook.getId()) {return true;}

        if (editableBook == null) {return false;}

        return book.getId() == editableBook.getId();
    }
    public void saveEdit(Book book) {
        if (book.getId() == addedBook.getId()) {
            BookRepository.add(book);
            books = (ArrayList<Book>) BookRepository.getBooks();
            addedBook = new Book(-1, "", "", "", "", new Date(), 0.0, "");
        }
        else {
            BookRepository.update(book);
            editableBook = null;
        }
    }

    public Book getEditedBook(Book book) {
        if (book.getId() == addedBook.getId()) {
            return addedBook;
        }
        return editableBook;
    }

    public boolean isAdded(Book book) {
        return book.getId() == addedBook.getId();
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