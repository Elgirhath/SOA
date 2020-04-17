import lombok.Getter;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

@Named("Banner")
@SessionScoped
public class Banner implements Serializable {
    Map<String, Integer> clicksPerAd;
    @Getter
    String selectedAd;

    Banner() {
        clicksPerAd = new HashMap<String, Integer>() {{
            put("Najlepszy lek na kaszel suchy i mokry! Kaszlex", 0);
            put("Tylko dzisiaj - buty do 50% taniej! Butmania", 0);
            put("Zrób zakupy za 100zł, zaoszczędź 50zł - tylko w MediaShop", 0);
            put("Schab bez kości - 15,99zł/kg. MikroSklep", 0);
        }};
    }

    public void init() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!facesContext.isPostback() && !facesContext.isValidationFailed()) {
            selectedAd = getRandomAd();
        }
    }

    public String getRandomAd() {
        Random random = new Random();
        int index = random.nextInt(clicksPerAd.keySet().size());
        return (String) clicksPerAd.keySet().toArray()[index];
    }

    public void incrementClicks() {
        clicksPerAd.put(selectedAd, clicksPerAd.get(selectedAd) + 1);
    }

    public int getClicks() {
        return clicksPerAd.get(selectedAd);
    }
}
