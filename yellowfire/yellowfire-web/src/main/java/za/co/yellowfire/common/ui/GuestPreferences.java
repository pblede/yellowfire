package za.co.yellowfire.common.ui;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named("guestPreferences")
public class GuestPreferences implements Serializable {
    public String getTheme() {
        return "overcast";
    }
}
