package com.hayeon.demo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

@Configuration
@ComponentScan(basePackages = "com.hayeon.demo",
        excludeFilters = @ComponentScan.Filter({
                Controller.class,
                RestController.class
        }))
@PropertySource(value = "classpath:application.properties")
public class RootConfig {

}