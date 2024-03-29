package site.easytobuild.multipurpose.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import site.easytobuild.multipurpose.customValidation.UniqueCustomerEmail;
import site.easytobuild.multipurpose.customValidation.UniqueCustomerName;

import java.util.List;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private int customerId;

    @Column(name = "customer_name")
    @NotBlank(message = "Username is required")
    @UniqueCustomerName
    private String customerName;

    @Column(name = "first_name")
    @NotBlank(message = "First name is required")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Column(name = "email")
    @NotBlank(message = "Email is required")
    @UniqueCustomerEmail
    private String email;

    @Column(name = "customer_pass")
    @NotBlank(message = "Password is required")
    private String password;

    @OneToMany(mappedBy = "customer")
    private List<Business> businesses;

    public Customer() {

    }

    public Customer(String customerName, String firstName, String email, String password) {
        this.customerName = customerName;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
    }

    public Customer(String customerName, String firstName, String lastName, String email, String password) {
        this(customerName,firstName,email,password);
        this.lastName = lastName;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Business> getBusinesses() {
        return businesses;
    }

    public void addBusinesses(Business business) {
        this.businesses.add(business);
    }

    public void removeBusiness(Business business) {
        this.businesses.remove(business);
    }
}
