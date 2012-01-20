package za.co.yellowfire.log;

public enum LogType {
	INSPECT(""),
	MANAGER("manager"),
	CONTROLLER("controller"),
	CONVERTER("converter"),
	MODEL("model"),
	DOMAIN("domain"),
	AUDIT("audit"),
	LISTENER("listener"),
	SEARCH("search"),
	;
	
	private static final String LOGGER_NAME_PREFIX = "za.co.bluefire.log.";
    private String category;

    private LogType(String category) {
        this.category = LOGGER_NAME_PREFIX + category;
    }

    public String getCategory() {
        return category;
    }
}
