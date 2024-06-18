package com._7aske.grain.data.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation for specifying a named parameter mapping in the {@link Query} annotation.
 *
 * <pre>
 *{@code @Query("SELECT u FROM User u WHERE u.status = :status")}
 *{@code List<User> findUsersByStatus(@Param("status") int status);}
 * </pre>
 *
 * @see Query
 */
@Retention(RUNTIME)
@Documented
@Target(ElementType.PARAMETER)
public @interface Param {
    String value();
}
