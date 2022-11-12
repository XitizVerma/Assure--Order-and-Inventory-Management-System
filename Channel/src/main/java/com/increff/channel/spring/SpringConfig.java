package com.increff.channel.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@EnableWebMvc
@Configuration
@EnableTransactionManagement
@ComponentScan("com.increff.channel")
@PropertySources({
        @PropertySource(value = "file:./channel.properties", ignoreResourceNotFound = true)
})
public class SpringConfig {
}