package site.easytobuild.multipurpose.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.easytobuild.multipurpose.repository.CustomerRepository;
import site.easytobuild.multipurpose.entity.Customer;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;
    @Override
    @Transactional
    public Customer findByCustomerId(int customerId) {
        return customerRepository.findByCustomerId(customerId);
    }

    @Override
    @Transactional
    public Customer findByCustomerName(String customerName) {
        return customerRepository.findByCustomerName(customerName);
    }

    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public void save(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public void deleteByCustomerId(int customerId) {

    }
}
