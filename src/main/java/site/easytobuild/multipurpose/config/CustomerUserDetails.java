package site.easytobuild.multipurpose.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import site.easytobuild.multipurpose.repository.CustomerRepository;
import site.easytobuild.multipurpose.entity.Customer;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerUserDetails implements UserDetailsService {

    @Autowired
    CustomerRepository customerService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String customerName, password = null;
        List<GrantedAuthority> authorities = null;
        Customer customer = customerService.findByCustomerName(username);
        if(customer == null) {
            throw new UsernameNotFoundException("user details not found for the user : " + username);
        } else {
            customerName = customer.getCustomerName();
            password = customer.getPassword();
            authorities = new ArrayList<>();
        }
        return new User(username,password,authorities);
    }

}
