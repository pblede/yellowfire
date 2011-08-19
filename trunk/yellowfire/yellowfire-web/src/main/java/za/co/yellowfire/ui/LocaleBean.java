package za.co.yellowfire.ui;

import za.co.yellowfire.Format;

import java.io.Serializable;

public class LocaleBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	public String getDateFormat() {
		return Format.DATE_DISPLAY.getFormat();
	}

    public String getTimeFormat() {
		return Format.TIME.getFormat();
	}
}
