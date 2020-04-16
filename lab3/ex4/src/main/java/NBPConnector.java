import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NBPConnector {
    Map<String, Double> exchangeRates;

    NBPConnector() {
        exchangeRates = new HashMap<String, Double>();

        exchangeRates.put("PLN", 1.0);

        try {
            URL url = new URL("http://www.nbp.pl/kursy/xml/LastA.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(url.openStream());

            NodeList entries = doc.getElementsByTagName("pozycja");
            for (int i = 0; i < entries.getLength(); i++) {
                Element element = (Element) entries.item(i);
                double multiplier = Double.parseDouble(element.getElementsByTagName("przelicznik").item(0).getFirstChild().getNodeValue());
                NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
                String rateString = element.getElementsByTagName("kurs_sredni").item(0).getFirstChild().getNodeValue();
                double rate = format.parse(rateString).doubleValue();
                String code = element.getElementsByTagName("kod_waluty").item(0).getFirstChild().getNodeValue();
                exchangeRates.put(code, rate / multiplier);
            }
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    double convert(double value, String sourceCurrencyCode, String destCurrencyCode) {
        double multiplier = exchangeRates.get(sourceCurrencyCode) / exchangeRates.get(destCurrencyCode);
        return value * multiplier;
    }
}
