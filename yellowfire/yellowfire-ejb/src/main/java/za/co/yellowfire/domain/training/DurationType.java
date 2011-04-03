package za.co.yellowfire.domain.training;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public enum DurationType {
    Hours,
    Days,
    Years;

    public int getId() {
        return this.ordinal();
    }

    public static DurationType valueOf(int id) {

        for (DurationType type : DurationType.values()) {
            if (type.ordinal() == id) {
                return type;
            }
        }
        throw new IllegalArgumentException(
            "No enum const DurationType.ordinal == " + id);
    }
}
