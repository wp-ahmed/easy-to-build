package site.easytobuild.multipurpose.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import site.easytobuild.multipurpose.entity.Business;
import site.easytobuild.multipurpose.entity.Customer;
import site.easytobuild.multipurpose.jenkins.Jenkins;
import site.easytobuild.multipurpose.service.BusinessService;
import site.easytobuild.multipurpose.service.CustomerService;
import site.easytobuild.multipurpose.util.AesUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.security.Principal;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Controller
public class BusinessSelectionController {

    @Value("${jenkins.host}")
    private String host;
    @Value("${jenkins.username}")
    private String userName;
    @Value("${jenkins.token}")
    private String token;

    private final BusinessService businessService;

    private final CustomerService customerService;

    @Autowired
    public BusinessSelectionController(BusinessService businessService, CustomerService customerService) {
        this.businessService = businessService;
        this.customerService = customerService;
    }

    @GetMapping("/")
    public String businessSelection(Model model) {
        Business business = new Business();

        model.addAttribute("business", business);
        return "business-selection";
    }

    @PostMapping("/save-business")
    public String saveBusiness(@ModelAttribute("business") @Validated Business business, BindingResult bindingResult, Principal principal, RedirectAttributes attributes) {

        if(bindingResult.hasErrors()) {
            return "business-selection";
        }
        String tomcatDir = business.getTomcatDir();
        while (tomcatDir.endsWith("/") || tomcatDir.endsWith(" ")) {
            tomcatDir = tomcatDir.substring(0, tomcatDir.length() - 1);
        }

        if (!isTomcatIsInstalled(tomcatDir)) {
            attributes.addFlashAttribute("tomcatError", "Tomcat is not installed on your device. Please install Tomcat before proceeding with the business building process");
            return "redirect:/";
        }

        String databaseUrl = business.getDatabaseUrl();
        while (databaseUrl.endsWith("/") || databaseUrl.endsWith(" ")) {
            databaseUrl = databaseUrl.substring(0, databaseUrl.length() - 1);
        }

        String fullDatabaseUrl = "jdbc:mysql://" + databaseUrl + "/";

        boolean connection = testDatabaseConnection(fullDatabaseUrl, business.getDatabaseUsername(), business.getDatabasePassword());
        if (!connection) {
            attributes.addFlashAttribute("connectionError", "Failed to connect to the database. Please check the database URL and credentials you provided.");
            return "redirect:/";
        }
        String jenkinsHost = host;
        while (jenkinsHost.endsWith("/") || jenkinsHost.endsWith(" ")) {
            jenkinsHost = jenkinsHost.substring(0, jenkinsHost.length() - 1);
        }

        Jenkins jenkins = new Jenkins(jenkinsHost, userName, token, business.getBusinessName(), "jenkins-" + business.getBusinessType(), business.getDatabaseName(),
                business.getDatabaseUsername(), business.getDatabasePassword(), business.getTomcatDir(), business.getGoogleClientId(), business.getGoogleClientSecret());

        String path = "src/main/resources/jenkins/database-creation.xml";
        File file = new File(path);
        String absolutePath = "@" + file.getAbsolutePath();
        jenkins.buildDatabaseProject(absolutePath);
        business.setBuildNum(1);
        business.setBuildStatus("success");
        String hashPassword = AesUtils.encrypt(business.getDatabasePassword());
        business.setDatabasePassword(hashPassword);
        businessService.save(business, principal);

        return "redirect:/show-details?businessName="+business.getBusinessName();
    }

    @GetMapping("/show-details")
    public String showBusinessDetails(@RequestParam String businessName, Model model, Principal principal) {
        Business business = businessService.findByBusinessName(businessName);
        if(business == null) {
            return "error/404";
        }
        Customer customer = business.getCustomer();
        Customer loggedInCustomer = customerService.findByCustomerName(principal.getName());
        if(customer.getCustomerId() != loggedInCustomer.getCustomerId()) {
            return "error/404";
        }
        model.addAttribute("customer",customer);
        model.addAttribute("business", business);
        return "show-details";
    }

    @PostMapping("/build-your-business")
    public RedirectView buildYourSite(@RequestParam String businessName, HttpSession session, Principal principal, RedirectAttributes attributes) {
        Business business = businessService.findByBusinessName(businessName);
        String password = AesUtils.decrypt(business.getDatabasePassword());
        Jenkins jenkins = new Jenkins(host, userName, token, business.getBusinessName(), "jenkins-" + business.getBusinessType(), business.getDatabaseName(),
                business.getDatabaseUsername(), password, business.getTomcatDir(), business.getGoogleClientId(), business.getGoogleClientSecret());
        String path = "src/main/resources/jenkins/crm.xml";
        String buildUrl = jenkins.getBuildLink();
        session.setAttribute(principal.getName(), buildUrl);
        File file = new File(path);
        String absolutePath = "@" + file.getAbsolutePath();
        if (business.getBuildNum() == 1) {
            jenkins.buildSpringMavenProject(absolutePath);
        } else {
            jenkins.rebuildExistedSpringMavenProject();
        }
        int buildNum = business.getBuildNum() + 1;
        business.setBuildNum(buildNum);
        businessService.save(business, principal);

        attributes.addAttribute("name", business.getBusinessName());
        return new RedirectView("/build-in-progress");
    }


    @GetMapping("/edit-your-business/{name}")
    public String showEditForm(@PathVariable("name") String businessName, Model model, Principal principal) {
        Business business = businessService.findByBusinessName(businessName);
        if(business == null) {
            return "error/404";
        }

        Customer customer = business.getCustomer();
        String username = principal.getName();
        Customer loggedInCustomer = customerService.findByCustomerName(username);
        if(customer.getCustomerId() != loggedInCustomer.getCustomerId()) {
            return "error/404";
        }
        model.addAttribute("business", business);
        return "edit-business";
    }

    @PostMapping("/edit-your-business")
    public String editBusiness(@ModelAttribute("business") @Validated Business business, BindingResult bindingResult, @RequestParam("databasePassword") String databasePassword,
                                     RedirectAttributes attributes, Principal principal, Model model) {

        Business prevBusiness = businessService.findByBusinessId(business.getBusinessId());
        String prevName = prevBusiness.getBusinessName();
        String prevDatabaseName = prevBusiness.getDatabaseName();


        BindingResult filteredBindingResult = new BeanPropertyBindingResult(business, "business");

        // Copy relevant errors to the filteredBindingResult
        for (FieldError error : bindingResult.getFieldErrors()) {
            if (!(Objects.equals(prevName, business.getBusinessName()) && error.getField().equals("businessName") && Objects.equals(error.getCode(), "UniqueBusinessName"))
                    && !(Objects.equals(prevDatabaseName, business.getDatabaseName()) && error.getField().equals("databaseName") && Objects.equals(error.getCode(), "UniqueDatabaseName"))) {
                filteredBindingResult.addError(error);
            }
        }

        if(filteredBindingResult.hasErrors()) {
            for (ObjectError error : filteredBindingResult.getAllErrors()) {
                String fieldName = null;
                if (error instanceof FieldError) {
                    fieldName = ((FieldError) error).getField();
                }
                model.addAttribute("filteredError_" + fieldName, error.getDefaultMessage());
            }
            return "edit-business";
        }

        if(prevName.equals(business.getBusinessName())) {
            business.setBuildNum(prevBusiness.getBuildNum());
        } else {
            business.setBuildNum(1);
        }

        String tomcatDir = business.getTomcatDir();
        while (tomcatDir.endsWith("/") || tomcatDir.endsWith(" ")) {
            tomcatDir = tomcatDir.substring(0, tomcatDir.length() - 1);
        }

        if (!isTomcatIsInstalled(tomcatDir)) {
            attributes.addFlashAttribute("tomcatError", "Tomcat is not installed on your device. Please install Tomcat before proceeding with the business building process");
            return "redirect:/edit-your-business/"+prevName;
        }


        String databaseUrl = business.getDatabaseUrl();
        while (databaseUrl.endsWith("/") || databaseUrl.endsWith(" ")) {
            databaseUrl = databaseUrl.substring(0, databaseUrl.length() - 1);
        }

        String fullDatabaseUrl = "jdbc:mysql://" + databaseUrl + "/";
        boolean connection = testDatabaseConnection(fullDatabaseUrl, business.getDatabaseUsername(), business.getDatabasePassword());
        if (!connection) {
            attributes.addFlashAttribute("connectionError", "Failed to connect to the database. Please check the database URL and credentials you provided.");
            return "redirect:/edit-your-business/"+prevName;
        }


        Jenkins jenkins = new Jenkins(host, userName, token, business.getBusinessName(), "jenkins-" + business.getBusinessType(), business.getDatabaseName(),
                business.getDatabaseUsername(), business.getDatabasePassword(), business.getTomcatDir(), business.getGoogleClientId(), business.getGoogleClientSecret());

        String path = "src/main/resources/jenkins/database-creation.xml";
        File file = new File(path);
        String absolutePath = "@" + file.getAbsolutePath();

        if(prevDatabaseName.equals(business.getDatabaseName())) {
            jenkins.rebuildExistedDatabaseProject();
        } else {
            jenkins.buildDatabaseProject(absolutePath);
        }
        String hashPassword = AesUtils.encrypt(business.getDatabasePassword());
        business.setDatabasePassword(hashPassword);
        business.setBuildStatus("success");
        businessService.save(business, principal);
        return "redirect:/show-details?businessName="+business.getBusinessName();
    }

    @GetMapping("/get-progress")
    @ResponseBody
    public String getProgress(@RequestParam("name") String businessName, Principal principal) {
        Business business = businessService.findByBusinessName(businessName);
        if(business == null) {
            return "failure";
        }
        try {
            return getBuildProgress(business);
        } catch (Exception e) {
            // Handle the exception appropriately
            //TODO redirect to fail
            return e.getMessage();
        }
    }

    @GetMapping("/build-in-progress")
    public String progressBuilding(@RequestParam("name") String businessName, Model model) {
        Business business = businessService.findByBusinessName(businessName);
        model.addAttribute("business", businessName);
        return "build-in-progress";
    }

    @GetMapping("/success-build")
    public String successBuild() {
        return "success-page";
    }

    @GetMapping("/failure-build")
    public String failureBuild() {
        return "failure-page";
    }

    @GetMapping("/projects")
    public String showAllProjects(Model model, Principal principal) {
        Customer customer = customerService.findByCustomerName(principal.getName());
        if(customer == null) {
            return "error/404";
        }
        List<Business> businesses = businessService.findByCustomer(customer.getCustomerId());
        model.addAttribute("businesses", businesses);
        return "projects";
    }
    private boolean testDatabaseConnection(String url, String username, String password) {
        try {
            DriverManager.getConnection(url, username, password);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean fileExists(String filePath) {
        return new File(filePath).exists();
    }

    private boolean isTomcatIsInstalled(String tomcatDir) {
        String webappsDir = tomcatDir + "\\webapps";
        String startupScript = "startup.bat";

        return fileExists(tomcatDir + "\\bin\\" + startupScript) && fileExists(webappsDir);
    }

    private String getBuildProgress(Business business) throws Exception {
        // Configure HTTP client with Jenkins credentials
        String name = business.getBusinessName();
        int x = business.getBuildNum() - 1;
        String num = String.valueOf(x);
        String jenkinsHost = host;
        while (jenkinsHost.endsWith("/") || jenkinsHost.endsWith(" ")) {
            jenkinsHost = jenkinsHost.substring(0, jenkinsHost.length() - 1);
        }
        String cmdGetDocId = "\n" +
                "curl -X GET " + jenkinsHost + "/job/" + name + "/" + num + "/api/json" + " --user " + userName + ":" + token;

        Process process = Runtime.getRuntime().exec(cmdGetDocId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        StringBuilder responseBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            responseBuilder.append(line);
        }

        reader.close();
        String response = responseBuilder.toString();
        if (response.startsWith("<html")) {
            return "null";
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);
        String result = jsonNode.get("result").asText();
        return result == null ? "null" : result;
    }
}
