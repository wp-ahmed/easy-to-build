package site.easytobuild.multipurpose.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import site.easytobuild.multipurpose.customValidation.SameDatabaseBusinessName;
import site.easytobuild.multipurpose.customValidation.UniqueBusinessName;
import site.easytobuild.multipurpose.customValidation.UniqueDatabaseName;

@Entity
@Table(name = "business_type")
@SameDatabaseBusinessName
public class Business {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "business_id")
    private int businessId;

    @Column(name = "business_name")
    @NotBlank(message = "Business name is required")
    @UniqueBusinessName
    private String businessName;

    @Column(name = "business_type")
    @NotBlank(message = "Business type is required")
    private String businessType;

    @Column(name = "database_name")
    @NotBlank(message = "Database name is required")
    @UniqueDatabaseName
    private String databaseName;

    @Column(name = "database_url")
    @NotBlank(message = "Database URL is required")
    private String databaseUrl;

    @Column(name = "sql_name")
    @NotBlank(message = "Username is required")
    private String databaseUsername;

    @Column(name = "sql_password")
    @NotBlank(message = "Database password is required")
    private String databasePassword;

    @Column(name = "tomcat_dir")
    @NotBlank(message = "Tomcat is required")
    private String tomcatDir;

    @Column(name = "google_client_id")
    private String googleClientId;

    @Column(name = "google_client_secret")
    private String googleClientSecret;

    @Column(name = "build_num")
    private int buildNum;

    @Column(name = "build_status")
    private String buildStatus;

    @ManyToOne
    @JoinColumn(name="customer_id", nullable=false)
    private Customer customer;

    public Business() {

    }

    public Business(String businessName, String businessType, String databaseName, String databaseUrl, String databaseUsername,
                    String databasePassword, String tomcatDir, String googleClientId, String googleClientSecret, int buildNum,
                    String buildStatus, Customer customer) {
        this.businessName = businessName;
        this.businessType = businessType;
        this.databaseName = databaseName;
        this.databaseUrl = databaseUrl;
        this.databaseUsername = databaseUsername;
        this.databasePassword = databasePassword;
        this.tomcatDir = tomcatDir;
        this.googleClientId = googleClientId;
        this.googleClientSecret = googleClientSecret;
        this.buildNum = buildNum;
        this.buildStatus = buildStatus;
        this.customer = customer;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public void setDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public void setDatabaseUsername(String databaseUsername) {
        this.databaseUsername = databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    public String getTomcatDir() {
        return tomcatDir;
    }

    public void setTomcatDir(String tomcatDir) {
        this.tomcatDir = tomcatDir;
    }

    public String getGoogleClientId() {
        return googleClientId;
    }

    public void setGoogleClientId(String googleClientId) {
        this.googleClientId = googleClientId;
    }

    public String getGoogleClientSecret() {
        return googleClientSecret;
    }

    public void setGoogleClientSecret(String googleClientSecret) {
        this.googleClientSecret = googleClientSecret;
    }

    public int getBuildNum() {
        return buildNum;
    }

    public void setBuildNum(int buildNum) {
        this.buildNum = buildNum;
    }

    public String getBuildStatus() {
        return buildStatus;
    }

    public void setBuildStatus(String buildStatus) {
        this.buildStatus = buildStatus;
    }
}
