import lombok.Data;

import java.io.Serializable;

@Data
public class Book implements Serializable {
    String title;
    String author;
    String type;
    double price;
    String currencyCode;
}
