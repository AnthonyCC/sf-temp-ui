package com.freshdirect.cms.application.draftchange.converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cmsadmin.domain.DraftChange;

public class DraftChangeToContentNodeApplicatorTest {

    private ContentServiceI service;

    @Before
    public void setUp() throws Exception {
        List<XmlTypeService> list = new ArrayList<XmlTypeService>();
        list.add(new XmlTypeService("classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));
        CompositeTypeService typeService = new CompositeTypeService(list);
        service = new XmlContentService(typeService, new FlexContentHandler(), "classpath:/com/freshdirect/cms/fdstore/content/DraftApplicatorStoreData.xml");

        CmsManager.setInstance(new CmsManager(service, null));
    }

    @Test
    public void applySimpleFieldChangeToExistingNodeTest() {
        DraftChange draftChange = createDraftChange("FULL_NAME", "Product:r1_prod1_3", "UNDERTESTFULLNAME r1_prod1_3");
        ContentNodeI node = service.getContentNode(ContentKey.getContentKey("Product:r1_prod1_3"), DraftContext.MAIN);

        node = DraftChangeToContentNodeApplicator.applyDraftChangeToNode(draftChange, node);
        Assert.assertEquals(draftChange.getValue(), node.getAttributeValue("FULL_NAME"));
    }

    @Test
    public void applySimpleFieldChangeToNonExistingNodeTest() {
        DraftChange draftChange = createDraftChange("FULL_NAME", "Product:TestNodeNonExists", "UNDERTESTFULLNAME TestNodeNonExists");

        ContentNodeI node = DraftChangeToContentNodeApplicator.createContentNodeFromDraftChange(draftChange, service, DraftContext.MAIN);
        Assert.assertEquals(draftChange.getValue(), node.getAttributeValue("FULL_NAME"));
    }

    @Test
    public void applyOneToOneFieldChangeToExistingNodeTest() {

        DraftChange draftChange = createDraftChange("PROD_IMAGE", "Product:r1_prod1_3", "Image:15247576");

        ContentNodeI node = service.getContentNode(ContentKey.getContentKey("Product:r1_prod1_3"), DraftContext.MAIN);
        node = DraftChangeToContentNodeApplicator.applyDraftChangeToNode(draftChange, node);
        Assert.assertEquals(ContentKey.getContentKey(draftChange.getValue()), node.getAttributeValue("PROD_IMAGE"));
    }

    @Test
    public void applyOneToManyFieldChangeToExistingNodeTest() {
        DraftChange draftChange = createDraftChange("brands", "Product:r1_prod1_3", "Brand:bd_100_grass_fed|Brand:bd_18_rabbits|Brand:bd_21st_amendment");

        String[] keys = StringUtils.split("Brand:bd_100_grass_fed|Brand:bd_18_rabbits|Brand:bd_21st_amendment", DraftChangeToContentNodeApplicator.SEPARATOR);
        List<ContentKey> draftKeys = new ArrayList<ContentKey>();
        for (int i = 0; i < keys.length; i++) {
            draftKeys.add(ContentKey.getContentKey(keys[i]));
        }

        ContentNodeI node = service.getContentNode(ContentKey.getContentKey("Product:r1_prod1_3"), DraftContext.MAIN);
        node = DraftChangeToContentNodeApplicator.applyDraftChangeToNode(draftChange, node);

        Assert.assertEquals(draftKeys, node.getAttributeValue("brands"));
    }

    private DraftChange createDraftChange(String attributeName, String contentKey, String value) {
        DraftChange draftChange = new DraftChange();

        draftChange.setAttributeName(attributeName);
        draftChange.setContentKey(contentKey);
        draftChange.setId(1L);
        draftChange.setUserName("Test");
        draftChange.setValue(value);

        return draftChange;
    }
}
