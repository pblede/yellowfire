package za.co.yellowfire;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Mark Ashworth
 * @version 0.0.1
 */
public class DateUtil {

	public static Date getDate(Date date, boolean includeTime, boolean includeTimezone) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(date);
		
        if (!includeTime) {
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.AM_PM, Calendar.AM);
        }
        
        if (!includeTimezone) {
            cal.set(Calendar.ZONE_OFFSET, 0);
        }
        
        return cal.getTime();
	}
	
    public static Date getDate(boolean includeTime, boolean includeTimezone) {
        
        Calendar cal = GregorianCalendar.getInstance();
        if (!includeTime) {
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.AM_PM, Calendar.AM);
        }
        
        if (!includeTimezone) {
            cal.set(Calendar.ZONE_OFFSET, 0);
        }
        
        return cal.getTime();
    }
}
