package com.freshdirect.cmsadmin.config;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Freshdirect datasource common configuration.
 */
@Configuration
@ComponentScan("com.freshdirect.cmsadmin")
@EnableJpaRepositories("com.freshdirect.cmsadmin.repository.jpa")
@EnableTransactionManagement
public class DataSourceConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier(value = "jpaProperties")
    private Properties jpaProperties;

    /**
     * Default H2 datasource bean.
     *
     * @return H2datasource
     */
    @Bean(name = "defaultH2DataSource")
    @Profile(value = { "developer", "test" })
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
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
    @Bean(name = "hikariDataSource")
    @Profile(value = { "euedge-dev-env", "schema" })
    public DataSource hikariDataSource(@Value("${spring.datasource.driverClassName}") String driverClassName, @Value("${spring.datasource.url}") String url,
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

    @Bean(name = "datasource")
    @Profile(value = { "fd-dev-env", "production" })
    public DataSource jndiDataSource() throws NamingException {
        Context ctx = new InitialContext();
        return (DataSource) ctx.lookup("java:comp/env/jdbc/datasource");
    }

    /**
     * Create entityManagerFactory.
     *
     * @return entityManagerFactory
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(new String[] { "com.freshdirect.cmsadmin.domain" });
        em.setJpaProperties(jpaProperties);
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setPersistenceUnitName("persistenceUnit");
        return em;
    }

    /**
     * Create transactionManager.
     *
     * @param entityManagerFactory
     *            EntityManagerFactory entityManagerFactory
     * @return transactionManager
     */
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    /**
     * Create propertyPlaceholderConfigurer in production environment.
     *
     * @return configurer
     */
    @Profile("production")
    @Bean
    public static PropertySourcesPlaceholderConfigurer productionPropertyPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocation(new ClassPathResource("application-production.properties"));
        configurer.setIgnoreUnresolvablePlaceholders(true);
        return configurer;
    }

    /**
     * Create propertyPlaceholderConfigurer in euedge developer environment.
     *
     * @return configurer
     */
    @Profile("euedge-dev-env")
    @Bean
    public static PropertySourcesPlaceholderConfigurer euedgeDeveloperPropertyPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocation(new ClassPathResource("application-euedge-dev-env.properties"));
        configurer.setIgnoreUnresolvablePlaceholders(true);
        return configurer;
    }

    /**
     * Create propertyPlaceholderConfigurer in freshdirect developer environment.
     *
     * @return configurer
     */
    @Profile("fd-dev-env")
    @Bean
    public static PropertySourcesPlaceholderConfigurer freshdirectDeveloperPropertyPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocation(new ClassPathResource("application-fd-dev-env.properties"));
        configurer.setIgnoreUnresolvablePlaceholders(true);
        return configurer;
    }

    /**
     * Create propertyPlaceholderConfigurer in test environment.
     *
     * @return configurer
     */
    @Profile("test")
    @Bean
    public static PropertySourcesPlaceholderConfigurer testPropertyPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocation(new ClassPathResource("application-test.properties"));
        configurer.setIgnoreUnresolvablePlaceholders(true);
        return configurer;
    }

    /**
     * Create propertyPlaceholderConfigurer in developer environment.
     *
     * @return configurer
     */
    @Profile("developer")
    @Bean
    public static PropertySourcesPlaceholderConfigurer developerPropertyPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocation(new ClassPathResource("application-developer.properties"));
        configurer.setIgnoreUnresolvablePlaceholders(true);
        return configurer;
    }

    /**
     * Create propertyPlaceholderConfigurer in schema environment.
     *
     * @return configurer
     */
    @Profile("schema")
    @Bean
    public static PropertySourcesPlaceholderConfigurer schemaPropertyPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocation(new ClassPathResource("application-schema.properties"));
        configurer.setIgnoreUnresolvablePlaceholders(true);
        return configurer;
    }

    /**
     * Create hibernateProperties bean.
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
    public Properties hibernatePropertiesWithSchemaCreation(@Value("${hibernate.dialect}") String dialect, @Value("${hibernate.show_sql}") String showSql,
            @Value("${hibernate.format_sql}") String formatSql, @Value("${hibernate.default_schema}") String defaultSchema,
            @Value("${javax.persistence.create-database-schemas}") String isSchemaCreated,
            @Value("${javax.persistence.schema-generation.database.action}") String dbAction, @Value("${javax.persistence.schema-generation.scripts.action}") String scriptAction,
            @Value("${javax.persistence.schema-generation.scripts.create-target}") String scriptCreateTarget,
            @Value("${javax.persistence.schema-generation.scripts.drop-target}") String scriptDropTarget, @Value("${javax.persistence.sql-load-script-source}") String importFile) {
        Properties properties = new Properties();
        properties.put(org.hibernate.cfg.AvailableSettings.DIALECT, dialect);
        properties.put(org.hibernate.cfg.AvailableSettings.SHOW_SQL, showSql);
        properties.put(org.hibernate.cfg.AvailableSettings.FORMAT_SQL, formatSql);
        properties.put(org.hibernate.cfg.Environment.DEFAULT_SCHEMA, defaultSchema);
        properties.put(org.hibernate.jpa.AvailableSettings.SCHEMA_GEN_CREATE_SCHEMAS, isSchemaCreated);
        properties.put(org.hibernate.jpa.AvailableSettings.SCHEMA_GEN_DATABASE_ACTION, dbAction);
        properties.put(org.hibernate.jpa.AvailableSettings.SCHEMA_GEN_SCRIPTS_ACTION, scriptAction);
        properties.put(org.hibernate.jpa.AvailableSettings.SCHEMA_GEN_SCRIPTS_CREATE_TARGET, scriptCreateTarget);
        properties.put(org.hibernate.jpa.AvailableSettings.SCHEMA_GEN_SCRIPTS_DROP_TARGET, scriptDropTarget);
        properties.put(org.hibernate.jpa.AvailableSettings.SCHEMA_GEN_LOAD_SCRIPT_SOURCE, importFile);
        return properties;
    }

    /**
     * Create LDAP Context Source.
     *
     * Required to join LDAP server
     *
     * @param url
     *            String url
     * @param base
     *            String base
     * @param userDn
     *            String userDn
     * @param password
     *            String password
     * @return ldapContextSource
     */
    @Profile(value = { "developer", "schema" })
    @Bean(name = "ldapsource")
    public LdapContextSource ldapContextSource(@Value("${ldap.url}") String url, @Value("${ldap.base}") String base, @Value("${ldap.user_dn}") String userDn,
            @Value("${ldap.password}") String password) {
        return createLdapContextSource(url, base, userDn, password);
    }

    /**
     * Create LDAP Context Source.
     *
     * Required to join LDAP server
     *
     * @return ldapContextSource
     */
    @Profile(value = { "euedge-dev-env", "fd-dev-env", "production" })
    @Bean(name = "ldapsource")
    public LdapContextSource jndiLdapContextSource() throws NamingException {
        Context ctx = new InitialContext();
        String url = (String) ctx.lookup("java:comp/env/ldap/url");
        String base = (String) ctx.lookup("java:comp/env/ldap/base");
        String userDn = (String) ctx.lookup("java:comp/env/ldap/userdn");
        String password = (String) ctx.lookup("java:comp/env/ldap/password");
        return createLdapContextSource(url, base, userDn, password);
    }

    private LdapContextSource createLdapContextSource(String url, String base, String userDn, String password) {
        final LdapContextSource ldapContextSource = new LdapContextSource();
        ldapContextSource.setUrl(url);
        ldapContextSource.setBase(base);
        ldapContextSource.setUserDn(userDn);
        ldapContextSource.setPassword(password);
        ldapContextSource.setReferral("follow");
        return ldapContextSource;
    }
}
