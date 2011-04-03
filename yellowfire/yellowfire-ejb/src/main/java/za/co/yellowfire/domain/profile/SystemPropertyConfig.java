package za.co.yellowfire.domain.profile;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public enum SystemPropertyConfig {
    Timezone("system.timezone", "CAT")
    ;

    private String name;
    private Object defaultValue;

    private SystemPropertyConfig(String name, Object defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }
}
