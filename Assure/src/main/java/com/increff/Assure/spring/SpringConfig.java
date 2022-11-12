package com.increff.Assure.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@EnableWebMvc
@Configuration
@EnableTransactionManagement
@ComponentScan("com.increff.Assure")
@PropertySources({
        @PropertySource(value = "file:./assure.properties", ignoreResourceNotFound = true)
})
public class SpringConfig {
}