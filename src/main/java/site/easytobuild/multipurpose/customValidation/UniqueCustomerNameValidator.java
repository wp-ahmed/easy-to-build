package site.easytobuild.multipurpose.customValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import site.easytobuild.multipurpose.entity.Customer;
import site.easytobuild.multipurpose.service.CustomerService;

import java.util.List;

public class UniqueCustomerNameValidator implements ConstraintValidator<UniqueCustomerName, String> {

    private final CustomerService customerService;

    @Autowired
    public UniqueCustomerNameValidator(CustomerService customerService) {
        this.customerService = customerService;
    }

    public UniqueCustomerNameValidator() {
        this.customerService = null;
    }

    @Override
    public void initialize(UniqueCustomerName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String customerName, ConstraintValidatorContext constraintValidatorContext) {
        if(customerService == null || customerName == null || customerName.isEmpty()) {
            return true;
        }
        Customer customer = customerService.findByCustomerName(customerName);
        return customer == null;
    }
}
