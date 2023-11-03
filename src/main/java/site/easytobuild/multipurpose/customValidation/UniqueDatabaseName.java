package site.easytobuild.multipurpose.customValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueDatabaseNameValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueDatabaseName {

    String message() default "The database name you entered is already registered. Please choose a different name.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
