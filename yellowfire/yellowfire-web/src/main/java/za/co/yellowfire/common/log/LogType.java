package za.co.yellowfire.common.log;

public enum LogType {
	INSPECT(""),
    APPLICATION("application"),
	MANAGER("manager"),
	CONTROLLER("controller"),
	CONVERTER("converter"),
	MODEL("model"),
	DOMAIN("domain"),
	AUDIT("audit"),
	LISTENER("listener"),
	SEARCH("search"),
	PROFILE("profile"),
	SETUP("setup"),
	TEST("test"),
	;
	
	private static final String LOGGER_NAME_PREFIX = "za.co.yellowfire.log.";
    private String category;

    private LogType(String category) {
        this.category = LOGGER_NAME_PREFIX + category;
    }

    public String getCategory() {
        return category;
    }
}
