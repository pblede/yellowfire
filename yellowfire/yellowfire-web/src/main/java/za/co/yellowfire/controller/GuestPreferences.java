package za.co.yellowfire.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@SessionScoped
@ManagedBean(name = "guestPreferences")
public class GuestPreferences {
    public String getTheme() {
        return "overcast";
    }
}
