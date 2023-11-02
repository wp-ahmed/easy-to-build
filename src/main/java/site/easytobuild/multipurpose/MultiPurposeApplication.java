package site.easytobuild.multipurpose;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import site.easytobuild.multipurpose.util.XmlModifier;


@SpringBootApplication
public class MultiPurposeApplication extends SpringBootServletInitializer {

	@Autowired
	private XmlModifier xmlModifier;

	public static void main(String[] args)  {
		SpringApplication.run(MultiPurposeApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(MultiPurposeApplication.class);
	}
	@PostConstruct
	public void init() {
		xmlModifier.modifyXmlFile("src/main/resources/jenkins/crm.xml");
		xmlModifier.modifyXmlFile("src/main/resources/jenkins/database-creation.xml");
	}
}
