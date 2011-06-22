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
@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Qualifier
public @interface SearchableProperty {
    /**
     * The name of the searchable property
     */
    String name() default "";

    /**
     * The boost factor of the property
     */
    float boost() default 0;

    /**
     * Whether the field should be mapped to a dynamic field in the search provider
     */
    boolean dynamic() default false;

    /**
     * The type of the dynamic field
     */
    SearchablePropertyType type() default SearchablePropertyType.INFER;
}
