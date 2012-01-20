package za.co.yellowfire;

/**
 * @author Mark Ashworth
 * @version 0.0.1
 */
public enum Format {
	DATE_DISPLAY("dd MMM yyyy"),
	DATE("yyyy-MM-dd"),
    TIME("hh:mm:ss");
	
	private String format;
	
	private Format(String format) {
		this.format = format;
	}

	/**
	 * Format
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}
}
