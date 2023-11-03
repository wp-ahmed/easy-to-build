package site.easytobuild.multipurpose.customValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import site.easytobuild.multipurpose.entity.Business;
import site.easytobuild.multipurpose.service.BusinessService;

public class UniqueDatabaseNameValidator implements ConstraintValidator<UniqueDatabaseName, String> {

    private final BusinessService businessService;

    @Autowired
    public UniqueDatabaseNameValidator(BusinessService businessService) {
        this.businessService = businessService;
    }

    public UniqueDatabaseNameValidator() {
        this.businessService = null;
    }

    @Override
    public void initialize(UniqueDatabaseName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String databaseName, ConstraintValidatorContext constraintValidatorContext) {
        if (businessService == null || databaseName == null || databaseName.isEmpty()) {
            return true;
        }
        Business business = businessService.findByDatabaseName(databaseName);
        return business == null;
    }
}
