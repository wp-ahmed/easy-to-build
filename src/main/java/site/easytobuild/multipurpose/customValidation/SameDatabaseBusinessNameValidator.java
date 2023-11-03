package site.easytobuild.multipurpose.customValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import site.easytobuild.multipurpose.entity.Business;

public class SameDatabaseBusinessNameValidator implements ConstraintValidator<SameDatabaseBusinessName, Business> {

    @Override
    public void initialize(SameDatabaseBusinessName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Business business, ConstraintValidatorContext constraintValidatorContext) {
        if(business.getBusinessName() == null || business.getBusinessName().isEmpty() ||
                business.getDatabaseName() == null || business.getDatabaseName().isEmpty()) {
            return true;
        }

        return !business.getDatabaseName().equals(business.getBusinessName());
    }
}
