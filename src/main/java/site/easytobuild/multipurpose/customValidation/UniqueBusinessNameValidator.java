package site.easytobuild.multipurpose.customValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import site.easytobuild.multipurpose.entity.Business;
import site.easytobuild.multipurpose.service.BusinessService;

public class UniqueBusinessNameValidator implements ConstraintValidator<UniqueBusinessName, String> {

    private final BusinessService businessService;

    @Autowired
    public UniqueBusinessNameValidator(BusinessService businessService) {
        this.businessService = businessService;
    }
    public UniqueBusinessNameValidator() {
        this.businessService = null;
    }

    @Override
    public void initialize(UniqueBusinessName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String businessName, ConstraintValidatorContext constraintValidatorContext) {
        if(businessService == null || businessName == null || businessName.isEmpty()) {
            return true;
        }
        Business business = businessService.findByBusinessName(businessName);
        return business == null;
    }
}
