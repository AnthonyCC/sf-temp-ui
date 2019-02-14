package com.freshdirect.cms.configuration;

import java.util.Properties;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.freshdirect.cms.changecontrol.controller.ChangePropagatorController;
import com.freshdirect.cms.changecontrol.service.ContentNodeChangeResultSetExtractor;
import com.freshdirect.cms.draft.controller.DraftPopulatorController;
import com.freshdirect.cms.mediaassociation.controller.NotificationReceiverController;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@Profile("database")
@EnableCaching
@EnableAsync
@EnableJpaRepositories(entityManagerFactoryRef = "cmsEntityManagerFactory", transactionManagerRef = "cmsTransactionManager", basePackages = {
        "com.freshdirect.cms.persistence.repository",
        "com.freshdirect.cms.changecontrol.repository", "com.freshdirect.cms.media.repository" })
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan(basePackages = {"com.freshdirect.cms"},
    excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = DraftPopulatorController.class),
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = NotificationReceiverController.class),
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ChangePropagatorController.class)
})
public class DatabaseProfileConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseProfileConfiguration.class);

    @Resource
    private Environment env;

    @Bean
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheCacheManager().getObject());
    }

    @Bean(name = "cmsDataSource")
    @Primary
    public DataSource dataSource(@Value("${spring.datasource.driverClassName}") String driverClassName, @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.user}") String user, @Value("${spring.datasource.password}") String password, @Value("${spring.datasource.pool}") String pool,
            @Value("${spring.datasource.maxPoolSize:10}") int maxPoolSize) {
        DataSource dataSource = null;

        if (driverClassName != null && url != null && user != null && password != null) {
            try {
                dataSource = hikariDataSource(driverClassName, url, user, password, pool, maxPoolSize);
            } catch (Exception exc) {
                LOGGER.error("Failed to create hikari data source", exc);
            }
        }

        if (dataSource == null) {
            JndiTemplate jndiTempate = jndiTempate(env.getProperty("jndi.provider.url", "t3://localhost:7001"));
            try {
                dataSource = jndiDataSource(jndiTempate, env.getProperty("jndi.datasource", "fddatasource"));
            } catch (NamingException e) {
                LOGGER.error("Failed to lookup datasource in JNDI", e);
            }
        }

        return dataSource;
    }

    @Bean(name = "erpsDataSource")
    public DataSource erpsDataSource(@Value("${spring.datasource.driverClassName}") String driverClassName, @Value("${spring.datasource.url}") String url,
            @Value("${spring.erps.datasource.user}") String user, @Value("${spring.erps.datasource.password}") String password, @Value("${spring.datasource.pool}") String pool,
            @Value("${spring.datasource.maxPoolSize:10}") int maxPoolSize) {
        DataSource dataSource = null;

        if (driverClassName != null && url != null && user != null && password != null) {
            try {
                dataSource = hikariDataSource(driverClassName, url, user, password, pool, maxPoolSize);
            } catch (Exception exc) {
                LOGGER.error("Failed to create hikari data source", exc);
            }
        }

        if (dataSource == null) {
            JndiTemplate jndiTempate = jndiTempate(env.getProperty("jndi.provider.url", "t3://localhost:7001"));
            try {
                dataSource = jndiDataSource(jndiTempate, env.getProperty("jndi.datasource", "fddatasource"));
            } catch (NamingException e) {
                LOGGER.error("Failed to lookup datasource in JNDI", e);
            }
        }

        return dataSource;
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheCacheManager() {
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        cacheManagerFactoryBean.setShared(true);
        return cacheManagerFactoryBean;
    }

    /**
     * Create entityManagerFactory.
     *
     * @return entityManagerFactory
     */
    @Bean(name = "cmsEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, Properties jpaProperties) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(
                new String[] { "com.freshdirect.cms.persistence.entity", "com.freshdirect.cms.changecontrol.entity", "com.freshdirect.cms.media.entity", "com.freshdirect.cms" });
        em.setJpaProperties(jpaProperties);
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setPersistenceUnitName("persistenceUnit");
        return em;
    }

    /**
     * Create hibernateProperties bean.
     *
     *
     * @param dialect
     *            String dialect
     * @param showSql
     *            String showSql
     * @param formatSql
     *            String formatSql
     * @param isSchemaCreated
     *            String isSchemaCreated
     * @param dbAction
     *            String dbAction
     * @param scriptAction
     *            String scriptAction
     * @param scriptCreateTarget
     *            String scriptCreateTarget
     * @param scriptDropTarget
     *            String scriptDropTarget
     * @param importFile
     *            String importFile
     *
     * @return properties of parameters
     */
    @Bean(name = "jpaProperties")
    public Properties hibernatePropertiesWithSchemaCreation(@Value("${hibernate.dialect}") String dialect, @Value("${hibernate.show_sql:#{null}}") String showSql,
            @Value("${hibernate.format_sql:#{null}}") String formatSql, @Value("${hibernate.default_schema:#{null}}") String defaultSchema,
            @Value("${javax.persistence.create-database-schemas:#{null}}") Boolean isSchemaCreated,
            @Value("${javax.persistence.schema-generation.database.action:#{null}}") String dbAction,
            @Value("${javax.persistence.schema-generation.scripts.action:#{null}}") String scriptAction,
            @Value("${javax.persistence.schema-generation.scripts.create-target:#{null}}") String scriptCreateTarget,
            @Value("${javax.persistence.schema-generation.scripts.drop-target:#{null}}") String scriptDropTarget,
            @Value("${javax.persistence.sql-load-script-source:#{null}}") String importFile) {
        Properties properties = new Properties();

        properties.put(org.hibernate.cfg.AvailableSettings.DIALECT, dialect);

        if (showSql != null) {
            properties.put(org.hibernate.cfg.AvailableSettings.SHOW_SQL, showSql);
        }

        if (formatSql != null) {
            properties.put(org.hibernate.cfg.AvailableSettings.FORMAT_SQL, formatSql);
        }

        if (defaultSchema != null) {
            properties.put(org.hibernate.cfg.Environment.DEFAULT_SCHEMA, defaultSchema);
        }

        if (isSchemaCreated != null) {
            properties.put(org.hibernate.jpa.AvailableSettings.SCHEMA_GEN_CREATE_SCHEMAS, isSchemaCreated);
        }

        if (dbAction != null) {
            properties.put(org.hibernate.jpa.AvailableSettings.SCHEMA_GEN_DATABASE_ACTION, dbAction);
        }

        if (scriptAction != null) {
            properties.put(org.hibernate.jpa.AvailableSettings.SCHEMA_GEN_SCRIPTS_ACTION, scriptAction);
        }

        if (scriptCreateTarget != null) {
            properties.put(org.hibernate.jpa.AvailableSettings.SCHEMA_GEN_SCRIPTS_CREATE_TARGET, scriptCreateTarget);
        }

        if (scriptDropTarget != null) {
            properties.put(org.hibernate.jpa.AvailableSettings.SCHEMA_GEN_SCRIPTS_DROP_TARGET, scriptDropTarget);
        }

        if (importFile != null) {
            properties.put(org.hibernate.jpa.AvailableSettings.SCHEMA_GEN_LOAD_SCRIPT_SOURCE, importFile);
        }

        return properties;
    }

    /**
     * Create hikariDataSource bean.
     *
     * @param driverClassName
     *            String driverClassName
     * @param url
     *            String url
     * @param user
     *            String user
     * @param password
     *            String password
     * @param pool
     *            String pool
     * @param maxPoolSize
     *            int maxPoolSize
     *
     * @return hikariDataSource
     */
    private DataSource hikariDataSource(@Value("${spring.datasource.driverClassName}") String driverClassName, @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.user}") String user, @Value("${spring.datasource.password}") String password, @Value("${spring.datasource.pool}") String pool,
            @Value("${spring.datasource.maxPoolSize:10}") int maxPoolSize) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverClassName);
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setPoolName(pool);
        config.setMaximumPoolSize(maxPoolSize);

        return new HikariDataSource(config);
    }

    private DataSource jndiDataSource(JndiTemplate template, @Value("${jndi.datasource:cmsdatasource}") String jndiName) throws NamingException {
        final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
        dsLookup.setResourceRef(true);
        DataSource dataSource = dsLookup.getDataSource(jndiName);
        return dataSource;
    }

    private JndiTemplate jndiTempate(@Value("${jndi.provider.url}") String providerUrl) {
        JndiTemplate template = new JndiTemplate();
        
        Properties environment = new Properties();

        environment.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
        environment.put(Context.PROVIDER_URL, providerUrl != null ? providerUrl : "t3://localhost:7001");

        template.setEnvironment(environment);

        return template;
    }

    /**
     * Create transactionManager.
     *
     * @param entityManagerFactory
     *            EntityManagerFactory entityManagerFactory
     * @return transactionManager
     */
    @Bean(name = "cmsTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("cmsEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    /**
     * Default properties for all kind of Spring profiles, except test
     *
     * @return
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer freshdirectDeveloperPropertyPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocation(new ClassPathResource("fdcms-db.properties"));
        configurer.setIgnoreUnresolvablePlaceholders(true);
        return configurer;
    }

    @Bean(name = "cmsJdbcTemplate")
    public JdbcTemplate getJdbcTemplate(@Qualifier("cmsDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public ContentNodeChangeResultSetExtractor getContentNodeChangeResultExtractor() {
        return new ContentNodeChangeResultSetExtractor();
    }
}
