package com.freshdirect.cms.ui.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@Profile("database")
@ComponentScan({ "com.freshdirect.cms.draft.controller", "com.freshdirect.cms.mediaassociation.controller" })
public class CmsGwtWebControllerConfiguration {

    @Bean
    public MappingJackson2HttpMessageConverter converter() {
        return new MappingJackson2HttpMessageConverter();
    }

}
