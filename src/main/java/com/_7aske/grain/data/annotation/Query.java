package com._7aske.grain.data.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation for specifying a query to be executed by a repository method.
 * For queries that modify the database, <code>modifying</code> should be set to true.
 *
 * <pre>
 *{@code @Query("SELECT u FROM User u WHERE u.status = :status")}
 *{@code List<User> findUsersByStatus(@Param("status") int status);}
 * </pre>
 */
@Retention(RUNTIME)
@Documented
@Target(ElementType.METHOD)
public @interface Query {
    String value();

    boolean nativeQuery() default false;

    boolean modifying() default false;
}
