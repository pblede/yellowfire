package za.co.yellowfire.ui.resources;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public enum MessageKey {
    dialogRaces("dialog.races"),
    dialogRegister("dialog.register"),
    dialogLogin("dialog.login"),
    dialogWarning("dialog.warning"),
    dialogInfo("dialog.info"),
    dialogError("dialog.error"),
    dialogResultCalendar("dialog.result.calendar"),
    dialogResultDetails("dialog.result.details"),
    dialogResultRace("dialog.result.race"),
    dialogResultTraining("dialog.result.training");

    private String key;

    private MessageKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
