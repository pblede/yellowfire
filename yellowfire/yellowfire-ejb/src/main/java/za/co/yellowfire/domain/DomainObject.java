package za.co.yellowfire.domain;

import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public interface DomainObject extends Serializable {
    Serializable getId();
}
