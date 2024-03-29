package site.easytobuild.multipurpose.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import site.easytobuild.multipurpose.entity.Customer;
import site.easytobuild.multipurpose.service.CustomerService;

@Controller
public class RegisterController {
    @Autowired
    CustomerService customerService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String registerCustomer(Model model){
        Customer customer = new Customer();
        model.addAttribute("customer",customer);
        return "/register";
    }

    @PostMapping("/register")
    public String registerNewOwner(@ModelAttribute("customer") @Validated Customer customer, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            return "/register";
        }
        String hashPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(hashPassword);
        customerService.save(customer);

        return "redirect:/login";
    }

}
