package com.freshdirect.cmsadmin.schema;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.freshdirect.cmsadmin.category.Schema;
import com.freshdirect.cmsadmin.config.DataSourceConfig;

/**
 * Database DDL schema generation task.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DataSourceConfig.class})
@WebAppConfiguration
@ActiveProfiles("schema")
@Category(Schema.class)
public class SchemaGenerationTest {

    @Test
    public void schemaGeneration() {
        this.getClass();
    }

}
