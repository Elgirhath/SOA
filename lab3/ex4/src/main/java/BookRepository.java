import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    public static List<Book> getBooks() {
        return new ArrayList<Book>() {{
            add(new Book() {{
                title = "Lśnienie";
                author = "Stephen King";
                type = "Horror";
                price = 7.99;
                currencyCode = "USD";
            }});
            add(new Book() {{
                title = "Wieża Jaskółki";
                author = "Andrzej Sapkowski";
                type = "Fantasy";
                price = 39.99;
                currencyCode = "PLN";
            }});
            add(new Book() {{
                title = "Drużyna Pierścienia";
                author = "J. R. R. Tolkien";
                type = "Fantasy";
                price = 5.49;
                currencyCode = "EUR";
            }});
            add(new Book() {{
                title = "Słownik Polsko-Angielski";
                author = "Jan Kowalski";
                type = "Educative";
                price = 9.99;
                currencyCode = "PLN";
            }});
            add(new Book() {{
                title = "Harry Potter i Komnata Tajemnic";
                author = "J. K. Rowling";
                type = "Adventure";
                price = 9.99;
                currencyCode = "USD";
            }});
            add(new Book() {{
                title = "Cień wiatru";
                author = "Carlos Ruiz Zafón";
                type = "Mystery";
                price = 9.49;
                currencyCode = "EUR";
            }});
            add(new Book() {{
                title = "Zielona Mila";
                author = "Stephen King";
                type = "Dark Fantasy";
                price = 5.99;
                currencyCode = "USD";
            }});
            add(new Book() {{
                title = "Pan Samochodzik i Templariusze";
                author = "Zbigniew Nienacki";
                type = "Adventure";
                price = 25.99;
                currencyCode = "PLN";
            }});
            add(new Book() {{
                title = "Dziewczyna z Tatuażem";
                author = "Stieg Larsson";
                type = "Thriller";
                price = 7.99;
                currencyCode = "EUR";
            }});
        }};
    }
}
