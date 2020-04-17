import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Named("Manager")
@SessionScoped
public class Manager implements Serializable {
    @Getter
    @Setter
    String firstName;

    @Getter
    @Setter
    String email;

    public void validateEmail(FacesContext context, UIComponent comp, Object value) {
        String val = (String) value;

        boolean correct = true;
        try {
            InternetAddress emailAddr = new InternetAddress(val);
            emailAddr.validate();
        }
        catch (AddressException ex) {
            correct = false;
        }

        if (!correct) {
            setErrorMessage(context, comp, "Invalid email address");
        }
    }

    @Getter
    @Setter
    int age;

    public void validateAge(FacesContext context, UIComponent comp, Object value) {
        if (value == null) {return;}
        int val = (Integer) value;

        if (val < 10 || val > 100) {
            setErrorMessage(context, comp, "Age should be between 10 and 100");
        }
    }


    @Getter
    @Setter
    String sex;

    public boolean isFemale() {
        if (sex == null) {
            return false;
        }
        return sex.equals("female");
    }

    public boolean isMale() {
        if (sex == null) {
            return false;
        }
        return sex.equals("male");
    }

    @Getter
    @Setter
    String education = "";

    @Getter
    @Setter
    int height;

    public void validateHeight(FacesContext context, UIComponent comp, Object value) {
        if(value == null || sex == null) { return; }

        int val = (Integer) value;

        if(sex.equals("male")) {
            if (val < 165 || val > 200) {
                setErrorMessage(context, comp, "Height of a male should be between 165 and 200");
            }
        } else {
            if (val < 150 || val > 185) {
                setErrorMessage(context, comp, "Height of female should be between 150 and 185");
            }
        }
    }

    @Getter
    @Setter
    int bustSize;

    @Getter
    @Setter
    String cupSize;

    @Getter
    @Setter
    int waistSize;

    @Getter
    @Setter
    int chestSize;

    @Getter
    @Setter
    int hipSize;

    void setErrorMessage(FacesContext context, UIComponent comp, String message) {
        ((UIInput) comp).setValid(false);

        FacesMessage facesMessage = new FacesMessage(message);
        context.addMessage(comp.getClientId(context), facesMessage);
    }

    @Getter
    @Setter
    String monthlySpent;

    @Getter
    @Setter
    String shopFrequency;

    @Getter
    @Setter
    String[] clothColors;

    @Getter
    @Setter
    String[] clothTypes;

    public String[] getAllClothTypes() {
        if (isFemale()) {
            return new String[]{"garsonki", "bluzki", "spódniczki", "spodnie"};
        }
        else if (isMale()) {
            return new String[]{"spodnie", "spodenki", "garnitury", "koszule", "krawaty"};
        }
        return new String[0];
    }
}
