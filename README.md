## Project Name: EasyToBuild

EasyToBuild is a powerful web application built using Spring Boot MVC, Thymeleaf, Hibernate, and MySQL. It provides a seamless experience for users to easily create and deploy customized business websites.

## Description

EasyToBuild simplifies the process of building and deploying business websites by offering a user-friendly interface and automated deployment pipelines. With EasyToBuild, users can quickly create business websites tailored to their specific needs without extensive coding or technical knowledge.

Key Features:

- User Registration: Users can register an account and securely log in to the EasyToBuild platform.
- Business Type Selection: Users can choose from a variety of business types, such as CRM, e-commerce, or simple landing pages.
- Database Configuration: EasyToBuild allows users to provide database connection details for seamless integration with their preferred database system.
- Automated Deployment: The Jenkins pipeline automates the deployment process, ensuring that the website is built, configured, and deployed to the specified server effortlessly.
- Customization: EasyToBuild offers customization options, allowing users to tailor the website's appearance, content, and functionality to meet their specific requirements.
- User-Friendly Interface: The intuitive user interface of EasyToBuild makes it accessible to users of all skill levels, eliminating the need for extensive technical expertise.

## Prerequisites

List all the prerequisites required to install and run the project. For example:

- Java 17
- MySQL
- Tomcat 10.1
- Jenkins (for automated deployment)

## Installation

1. Clone the repository: `git clone <repository-url>`
2. Configure the MySQL database by updating the `application.properties` file with the appropriate database connection credentials.
3. **Replace all placeholder credentials in `application.properties` and `README.md`**: Ensure that you replace the placeholder credentials with your actual database credentials, Jenkins username, token, and host information. Failure to do so may result in connection issues and deployment failures.
4. Build the project using Maven or your preferred build tool: `mvn clean install`
5. Deploy the project on Tomcat by copying the generated WAR file to the Tomcat webapps directory.
6. Start Tomcat and make sure it is running on `localhost:8080`.

## Application Properties

Ensure to update the following properties in `application.properties` with your actual database and Jenkins credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/easy_to_build?createDatabaseIfNotExist=true
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=none
spring.sql.init.mode=always

# Jenkins configurations
jenkins.username=yourusername
jenkins.token=yourtoken
jenkins.host=http://localhost:9090/

## Usage

1. Open a web browser and access `localhost:8080`.
2. Register a new account by providing your email and other required information.
3. Log in using your credentials.
4. You will be redirected to choose your business type from the available categories (CRM, E-commerce, Simple Landing Page).
5. Provide the necessary information for database connection credentials and the Tomcat webapp location.
6. Select the CRM category, and the CRM application will be installed automatically.
7. Access the CRM application using `localhost:8080/your-business-name`, where `your-business-name` refers to the information you registered earlier as your business name.

## License

Feel free to customize and expand upon the sections mentioned above to best fit your project's needs. Including clear instructions and explanations will help users understand and use your project successfully.
