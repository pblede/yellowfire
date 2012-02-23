package za.co.yellowfire.common.ui;

import za.co.yellowfire.common.annotation.Titles;
import za.co.yellowfire.common.domain.Title;

import javax.enterprise.inject.Produces;

/**
 * Provides data that is common across the application
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class CommonProvider {
    
    @Produces @Titles
    private String[] getTitles() {
        Title[] temp = Title.values();
        String[] titles = new String[temp.length];
        for (int i = 0; i < temp.length; i++) {
            titles[i] = temp[i].name();
        }
        return titles;
    }
}
