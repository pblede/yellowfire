package za.co.yellowfire.ui.model;

import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class DataTableRow<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean selected = false;
    private T object;
    private RequestResult result = new RequestResult();
    private float score = 0;
    
    public DataTableRow(T object) {
        this.object = object;
    }

    public DataTableRow(T object, float score) {
        this.object = object;
        this.score = score;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public RequestResult getResult() {
        return result;
    }

    public void setResult(RequestResult result) {
        this.result = result;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "DataTableRow{" +
                "selected=" + selected +
                ", result=" + result +
                ", object=" + object +
                '}';
    }
}
