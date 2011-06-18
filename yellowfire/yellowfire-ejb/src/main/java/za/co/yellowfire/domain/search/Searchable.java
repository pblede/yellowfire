package za.co.yellowfire.domain.search;

import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks a field as searchable
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Target({TYPE})
@Retention(RUNTIME)
@Documented
@Qualifier
public @interface Searchable {
    String name();
}
