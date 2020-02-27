package com.csye6225.webservice.RESTfulWebService;

import com.csye6225.webservice.RESTfulWebService.Configuration.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.Arrays;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@EnableConfigurationProperties(StorageProperties.class)
public class RestfulWebServiceApplication {


	public static void main(String[] args) {

		for (String arg: Arrays.asList(args)) {
			System.out.println(arg);
		}

		SpringApplication.run(RestfulWebServiceApplication.class, args);

	}

}
