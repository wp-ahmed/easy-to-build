package site.easytobuild.multipurpose.customValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import site.easytobuild.multipurpose.entity.Customer;
import site.easytobuild.multipurpose.service.CustomerService;

public class UniqueCustomerEmailValidator implements ConstraintValidator<UniqueCustomerEmail, String> {

    private final CustomerService customerService;

    @Autowired
    public UniqueCustomerEmailValidator(CustomerService customerService) {
        this.customerService = customerService;
    }

    public UniqueCustomerEmailValidator() {
        this.customerService = null;
    }

    @Override
    public void initialize(UniqueCustomerEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if(customerService == null || email == null || email.isEmpty()) {
            return true;
        }
        Customer customer = customerService.findByEmail(email);
        return customer == null;
    }
}
