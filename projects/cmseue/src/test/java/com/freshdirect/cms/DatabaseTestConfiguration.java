package com.freshdirect.cms;

import java.util.Arrays;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.freshdirect.cms.cache.CacheEvictors;
import com.freshdirect.cms.cache.CmsCaches;
import com.freshdirect.cms.changecontrol.service.ContentChangeControlService;
import com.freshdirect.cms.changecontrol.service.ContentNodeChangeResultSetExtractor;
import com.freshdirect.cms.changecontrol.service.DatabaseChangeControlService;
import com.freshdirect.cms.core.converter.ScalarValueToSerializedValueConverter;
import com.freshdirect.cms.core.converter.SerializedScalarValueToObjectConverter;
import com.freshdirect.cms.core.service.ContentKeyParentsCollectorService;
import com.freshdirect.cms.core.service.ContentProvider;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.media.converter.MediaEntityToMediaConverter;
import com.freshdirect.cms.media.converter.MediaToMediaEntityConverter;
import com.freshdirect.cms.media.service.DatabaseMediaService;
import com.freshdirect.cms.persistence.entity.converter.AttributeEntityToValueConverter;
import com.freshdirect.cms.persistence.entity.converter.ContentKeyToContentNodeEntityConverter;
import com.freshdirect.cms.persistence.entity.converter.ContentNodeEntityToContentKeyConverter;
import com.freshdirect.cms.persistence.entity.converter.RelationshipToRelationshipEntityConverter;
import com.freshdirect.cms.persistence.entity.converter.ScalarToAttributeEntityConverter;
import com.freshdirect.cms.persistence.repository.BatchSavingRepository;
import com.freshdirect.cms.persistence.repository.NavigationTreeRepository;
import com.freshdirect.cms.persistence.service.DatabaseContentProvider;
import com.freshdirect.cms.validation.service.ValidatorService;
import com.freshdirect.cms.validation.validator.KeyValidator;
import com.freshdirect.cms.validation.validator.TypeValidator;

@Configuration
@EnableJpaRepositories({ "com.freshdirect.cms.persistence.repository", "com.freshdirect.cms.changecontrol.repository", "com.freshdirect.cms.media.repository" })
@EnableTransactionManagement(proxyTargetClass = true)
@EnableCaching
public class DatabaseTestConfiguration {

    @Bean
    public DatabaseMediaService databaseMediaService() {
        return new DatabaseMediaService();
    }

    @Bean
    public MediaEntityToMediaConverter mediaEntityToMediaConverter() {
        return new MediaEntityToMediaConverter();
    }

    @Bean
    public MediaToMediaEntityConverter mediaToMediaEntityConverter() {
        return new MediaToMediaEntityConverter();
    }

    @Bean
    public ContentProvider contentProvider() {
        return new DatabaseContentProvider();
    }

    @Bean
    public ContentTypeInfoService contentTypeInfoService() {
        return new ContentTypeInfoService();
    }

    @Bean
    public ContentKeyParentsCollectorService contentKeyParentsCollectorService() {
        return new ContentKeyParentsCollectorService();
    }

    @Bean
    public AttributeEntityToValueConverter attributeEntityToValueConverter() {
        return new AttributeEntityToValueConverter();
    }

    @Bean
    public ContentKeyToContentNodeEntityConverter contentKeyToContentNodeEntityConverter() {
        return new ContentKeyToContentNodeEntityConverter();
    }

    @Bean
    public ContentNodeEntityToContentKeyConverter contentNodeEntityToContentKeyConverter() {
        return new ContentNodeEntityToContentKeyConverter();
    }

    @Bean
    public RelationshipToRelationshipEntityConverter relationshipToRelationshipEntityConverter() {
        return new RelationshipToRelationshipEntityConverter();
    }

    @Bean
    public ScalarToAttributeEntityConverter scalarToAttributeEntityConverter() {
        return new ScalarToAttributeEntityConverter();
    }

    @Bean
    public ValidatorService validatorService() {
        return new ValidatorService();
    }

    @Bean
    public ContentChangeControlService contentChangeControlService() {
        return new DatabaseChangeControlService();
    }

    @Bean
    public ScalarValueToSerializedValueConverter scalarValueToSerializedValueConverter() {
        return new ScalarValueToSerializedValueConverter();

    }

    @Bean
    public TypeValidator typeValidator() {
        return new TypeValidator();
    }

    @Bean
    public KeyValidator keyValidator() {
        return new KeyValidator();
    }

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
                new ConcurrentMapCache(CmsCaches.PARENT_KEYS_CACHE.cacheName),
                new ConcurrentMapCache(CmsCaches.ATTRIBUTE_CACHE.cacheName)));
        return cacheManager;
    }

    @Bean
    public SerializedScalarValueToObjectConverter coverter() {
        return new SerializedScalarValueToObjectConverter();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer testPropertyPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocation(new ClassPathResource("application-test.properties"));
        configurer.setIgnoreUnresolvablePlaceholders(true);
        return configurer;
    }

    @Bean(name = "defaultH2DataSource")
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
    }

    @Bean
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

    @Bean(name = "jpaProperties")
    public Properties hibernatePropertiesWithSchemaCreation(
            @Value("${hibernate.dialect}") String dialect,
            @Value("${hibernate.show_sql}") String showSql,
            @Value("${hibernate.format_sql}") String formatSql, @Value("${hibernate.default_schema}") String defaultSchema,
            @Value("${javax.persistence.create-database-schemas}") Boolean isSchemaCreated, @Value("${javax.persistence.schema-generation.database.action}") String dbAction,
            @Value("${javax.persistence.schema-generation.scripts.action}") String scriptAction,
            @Value("${javax.persistence.schema-generation.scripts.create-target}") String scriptCreateTarget,
            @Value("${javax.persistence.schema-generation.scripts.drop-target}") String scriptDropTarget, @Value("${javax.persistence.sql-load-script-source}") String importFile) {
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

    @Bean
    public NavigationTreeRepository navigationTreeRepository() {
        return new NavigationTreeRepository();
    }

    @Bean
    public CacheEvictors cacheEvictors() {
        return new CacheEvictors();
    }

    @Bean
    public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public ContentNodeChangeResultSetExtractor getContentNodeChangeResultExtractor() {
        return new ContentNodeChangeResultSetExtractor();
    }

    @Bean
    public BatchSavingRepository batchSavingRepository() {
        return new BatchSavingRepository();
    }
}
