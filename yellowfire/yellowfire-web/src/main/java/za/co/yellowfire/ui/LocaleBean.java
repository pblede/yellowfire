package za.co.yellowfire.ui;

import java.io.Serializable;

import za.co.yellowfire.Format;

public class LocaleBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	public String getDateFormat() {
		return Format.DATE_DISPLAY.getFormat();
	}

    public String getTimeFormat() {
		return Format.TIME.getFormat();
	}
}
