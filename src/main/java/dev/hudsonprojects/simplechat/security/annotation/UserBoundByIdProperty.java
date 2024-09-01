package dev.hudsonprojects.simplechat.security.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface UserBoundByIdProperty {

}
