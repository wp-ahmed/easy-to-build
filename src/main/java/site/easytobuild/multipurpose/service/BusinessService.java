package site.easytobuild.multipurpose.service;

import site.easytobuild.multipurpose.entity.Business;

import java.security.Principal;
import java.util.List;

public interface BusinessService {
    public Business findByBusinessId(int businessId);

    public Business findByBusinessName(String businessName);

    public Business findByDatabaseName(String databaseName);

    public List<Business> findByCustomer(int customerId);

    void save(Business business, Principal principal);

    void deleteByBusinessId(int businessId);
}
