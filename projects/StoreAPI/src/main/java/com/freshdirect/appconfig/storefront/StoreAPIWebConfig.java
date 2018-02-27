package com.freshdirect.appconfig.storefront;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@Profile("database")
@ComponentScan("com.freshdirect.cms.changecontrol.controller")
public class StoreAPIWebConfig {

    @Bean
    public MappingJackson2HttpMessageConverter converter() {
        return new MappingJackson2HttpMessageConverter();
    }

    @Profile("database")
    @Bean
    public static PropertySourcesPlaceholderConfigurer databaseProfilePropertyPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocation(new ClassPathResource("fdcms-db.properties"));
        configurer.setIgnoreUnresolvablePlaceholders(true);
        return configurer;
    }
}
