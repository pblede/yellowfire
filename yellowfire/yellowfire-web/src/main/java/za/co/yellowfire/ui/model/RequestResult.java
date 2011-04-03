package za.co.yellowfire.ui.model;

import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class RequestResult implements Serializable {
    private boolean failed = false;
    private String error = "";

    public RequestResult() {
    }

    public RequestResult(boolean failed, String error) {
        this.failed = failed;
        this.error = error;
    }

    public boolean isFailed() {
        return failed;
    }

    public String getError() {
        return error;
    }

    public void failed(String error) {
        this.failed = true;
        this.error = error;
    }

    public void reset() {
        this.failed = false;
        this.error = "";
    }
}
