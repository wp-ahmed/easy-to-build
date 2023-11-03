package site.easytobuild.multipurpose.service;

import site.easytobuild.multipurpose.entity.Customer;

import java.util.List;

public interface CustomerService {

    public Customer findByCustomerId(int customerId);

    public Customer findByCustomerName(String customerName);

    public Customer findByEmail(String email);

    void save(Customer customer);

    void deleteByCustomerId(int customerId);
}
