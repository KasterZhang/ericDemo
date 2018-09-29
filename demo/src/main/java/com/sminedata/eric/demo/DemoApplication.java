package com.sminedata.eric.demo;


import com.sminedata.eric.demo.config.JerseyConfig;

import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;


@SpringBootApplication // 开启组件扫描 ->@Configuration + @ComponentScan +
						// @EnableAutoConfiguration/@Abracadabra[译：咒语]
public class DemoApplication extends SpringBootServletInitializer {

	// public static void main(String[] args) {
	// SpringApplication.run(DemoApplication.class, args); // 负责启动引导应用程序
	// }
	public static void main(String[] args) {
		new DemoApplication().configure(new SpringApplicationBuilder(DemoApplication.class)).run(args);
		
	}

	@Bean
	public ServletRegistrationBean jersetServlet() {
		ServletRegistrationBean registration = new ServletRegistrationBean(new ServletContainer(), "/*");
		// our rest resources will be available in the path /jersey/*
		registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, JerseyConfig.class.getName());
		return registration;
	}


	// @Override
	// protected SpringApplicationBuilder configure(SpringApplicationBuilder
	// builder){
	// return builder.sources(this.getClass());
	// }
}
