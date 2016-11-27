package com.freshdirect.cmsadmin.config;

import java.sql.SQLException;

import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Web configuration.
 */
@Configuration
@EnableWebMvc
@EnableSpringDataWebSupport
public class WebConfig extends WebMvcConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebConfig.class);

    /**
     * H2 WebServer. Access: http://localhost:8082/ Driver Class: org.h2.Driver JDBC URL:jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1 Username: sa Password: leave empty
     *
     * @return webServer instance
     */

    @Profile(value = {"developer", "test"})
    @Bean(name = "h2WebServer", destroyMethod = "stop")
    public Server createWebServer() {
        Server webServer = null;
        try {
            webServer = Server.createWebServer("-web", "-webAllowOthers").start();
        } catch (SQLException e) {
            LOGGER.error("An SQL exception happened during the H2 webserver initialization.", e);
        }
        return webServer;
    }

    /**
     * Create mappingJackson2HttpMessageConverter bean.
     *
     * @return mappingJackson2HttpMessageConverter
     */
    @Bean
    public MappingJackson2HttpMessageConverter converter() {
        return new MappingJackson2HttpMessageConverter();
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
    }
}
