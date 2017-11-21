package com.freshdirect.cms;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.xml.sax.SAXException;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.persistence.service.DatabaseContentProvider;

@EnableWebMvc
@WebAppConfiguration
@ContextConfiguration(classes = { DatabaseTestConfiguration.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringConfigTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringConfigTest.class);

    @Autowired
    private DatabaseContentProvider dbcp;

    @Test
    public void main() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException, URISyntaxException {

        Assert.assertTrue(true);
        // cps.notifyPreviewAboutChangedContent(Arrays.asList(ContentKeyFactory.get(ContentType.Producer, "test")));

        Map<Attribute, Object> values = dbcp.getAttributeValues(ContentKeyFactory.get(ContentType.Sku, "HMR0066218"), Arrays.asList(ContentTypes.Sku.ORGANIC));
        for (Attribute attribute : values.keySet()) {
            LOGGER.debug("OneAttribute " + attribute + " = " + values.get(attribute));
        }

        Map<Attribute, Object> allValues = dbcp.getAllAttributesForContentKey(ContentKeyFactory.get(ContentType.Sku, "HMR0066218"));
        for (Attribute attribute : allValues.keySet()) {
            LOGGER.debug("AllAttributes " + attribute + " = " + allValues.get(attribute));
        }
    }
}
