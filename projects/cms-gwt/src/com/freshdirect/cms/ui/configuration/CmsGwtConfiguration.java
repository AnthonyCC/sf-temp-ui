package com.freshdirect.cms.ui.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableJpaRepositories({ "com.freshdirect.cms.ui.editor.reports.repository", "com.freshdirect.cms.ui.editor.publish.repository",
        "com.freshdirect.cms.ui.editor.publish.feed.repository" })
@ComponentScan({ "com.freshdirect.cms.ui.serviceimpl", "com.freshdirect.cms.ui.editor" })
@EnableAsync
public class CmsGwtConfiguration {

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

    @Profile("database")
    @Bean
    public static PropertySourcesPlaceholderConfigurer freshdirectDeveloperPropertyPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocation(new ClassPathResource("fdcms-db.properties"));
        configurer.setIgnoreUnresolvablePlaceholders(true);
        return configurer;
    }
}
