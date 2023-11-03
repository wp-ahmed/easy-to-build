package site.easytobuild.multipurpose.customValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueBusinessNameValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueBusinessName {
    String message() default "The business name you entered is already registered. Please choose a different name.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
