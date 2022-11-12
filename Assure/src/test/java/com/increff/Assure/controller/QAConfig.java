package com.increff.Assure.controller;


import com.increff.Assure.spring.SpringConfig;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(//
        basePackages = { "com.increff.Assure" }, //
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = { SpringConfig.class })//
)
@PropertySources({ //
        @PropertySource(value = "classpath:./test.properties") //
})
public class QAConfig {


}
