package site.easytobuild.multipurpose.customValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SameDatabaseBusinessNameValidator.class)
public @interface SameDatabaseBusinessName {
    String message() default "Database and Business name must not be equal";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
