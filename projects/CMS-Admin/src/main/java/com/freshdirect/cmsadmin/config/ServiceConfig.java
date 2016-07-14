package com.freshdirect.cmsadmin.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

@Configuration
public class ServiceConfig {

    @Bean
    @Qualifier(value = "sortByNameIgnoreCaseAsc")
    public Sort sortByNameIgnoreCaseAsc() {
        Order order = new Order(Sort.Direction.ASC, "name", Sort.NullHandling.NATIVE);
        order = order.ignoreCase();
        return new Sort(order);
    }

}
