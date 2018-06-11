package com.security.wrr.common;

import com.security.wrr.WrrApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@ComponentScan(basePackageClasses = WrrApplication.class, useDefaultFilters = true)
public class ServletContextConfig extends WebMvcConfigurationSupport {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/templates/**");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}
