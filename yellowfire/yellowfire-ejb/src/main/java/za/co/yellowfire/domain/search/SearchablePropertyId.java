package za.co.yellowfire.domain.search;

import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks the id of an entity as searchable
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Qualifier
public @interface SearchablePropertyId {
    String name() default "";
    float boost() default 0;
}
