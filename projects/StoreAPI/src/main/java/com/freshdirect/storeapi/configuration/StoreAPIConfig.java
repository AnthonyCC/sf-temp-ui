package com.freshdirect.storeapi.configuration;

import java.util.Properties;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import com.freshdirect.fdstore.FDStoreProperties;

@Configuration
@ComponentScan({ "com.freshdirect.storeapi", "com.freshdirect.fdstore.content.browse.grabber", "com.freshdirect.smartstore.impl" })
public class StoreAPIConfig {

    @Resource
    private Environment env;

    @Bean
    public DataSource storeDataSource() throws NamingException {
        final String jndiName = "fddatasource";
        final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
        dsLookup.setResourceRef(true);

        Properties environment = new Properties();

        environment.put(Context.INITIAL_CONTEXT_FACTORY, FDStoreProperties.getInitialContextFactory());
        environment.put(Context.PROVIDER_URL, FDStoreProperties.getProviderURL());

        dsLookup.setJndiEnvironment(environment);

        DataSource dataSource = dsLookup.getDataSource(jndiName);

        return dataSource;
    }
}
