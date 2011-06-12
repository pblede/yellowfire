package za.co.yellowfire.ui.model;

import za.co.yellowfire.domain.Archiveable;

import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class DataTableRow<T> implements Serializable, Comparable<DataTableRow<T>> {
    private static final long serialVersionUID = 1L;

    private boolean selected = false;
    private T object;
    private RequestResult result = new RequestResult();
    private float score = 0;
    private boolean readonly;

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

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    /**
     * Determines if the object is read-only
     * @return boolean
     */
    public boolean isReadonly() {
        return readonly;
    }

    /**
     * Determines if the object is archived
     * @return boolean
     */
    public boolean isArchived() {
        if (getObject() != null && getObject() instanceof Archiveable && ((Archiveable) getObject()).isArchived()) return true;
        return false;
    }

    /**
     * Determines if the object is editable. Editable objects are those that are not read-only and not archived.
     * @return boolean
     */
    public boolean isEditable() {
        if (isReadonly()) return false;
        if (isArchived()) return false;
        return true;
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
    public int compareTo(DataTableRow<T> o) {
        Object x = o.getObject();
        if (getObject() instanceof  Comparable && x instanceof Comparable) {
            return ((Comparable) getObject()).compareTo(x);
        }
        return 0;
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
