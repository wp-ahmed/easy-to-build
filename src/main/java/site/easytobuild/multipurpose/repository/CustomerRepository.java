package site.easytobuild.multipurpose.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.easytobuild.multipurpose.entity.Customer;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer> {
    public Customer findByCustomerId(int customerId);

    public Customer findByCustomerName(String customerName);
    public Customer findByEmail(String email);

    void deleteByCustomerId(int customerId);
}
