package com.csye6225.webservice.RESTfulWebService;

import com.csye6225.webservice.RESTfulWebService.Configuration.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.Arrays;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@EnableConfigurationProperties(StorageProperties.class)
public class RestfulWebServiceApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(RestfulWebServiceApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(RestfulWebServiceApplication.class, args);
	}

}
