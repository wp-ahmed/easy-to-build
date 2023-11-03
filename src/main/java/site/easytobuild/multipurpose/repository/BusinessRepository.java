package site.easytobuild.multipurpose.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.easytobuild.multipurpose.entity.Business;

import java.util.List;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Integer> {
    public Business findByBusinessId(int businessId);

    public Business findByBusinessName(String businessName);

    public Business findByDatabaseName(String databaseName);

    public List<Business> findByCustomerCustomerId(int customerId);

    void deleteByBusinessId(int businessId);

}
