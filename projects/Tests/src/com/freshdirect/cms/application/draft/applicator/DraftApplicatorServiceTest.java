package com.freshdirect.cms.application.draft.applicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

public class DraftApplicatorServiceTest {

    private List<ContentNodeI> nodes = new ArrayList<ContentNodeI>();
    private ContentServiceI service;

    @Before
    public void setUp() throws Exception {
        List<XmlTypeService> list = new ArrayList<XmlTypeService>();
        list.add(new XmlTypeService("classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));
        CompositeTypeService typeService = new CompositeTypeService(list);
        service = new XmlContentService(typeService, new FlexContentHandler(), "classpath:/com/freshdirect/cms/fdstore/content/simple.xml");

        CmsManager.setInstance(new CmsManager(service, null));

        nodes.add(service.getContentNode(ContentKey.getContentKey("Product:prod1"), DraftContext.MAIN));
        nodes.add(service.getContentNode(ContentKey.getContentKey("Product:r1_prod1_1"), DraftContext.MAIN));
    }

    @Test
    public void applyChangesToExistingNodesAndCreateNonExistingNodes() {
        DraftChange draftChange1 = createDraftChange("FULL_NAME", "Product:prod1", "UNDERTESTFULLNAME prod1 firstChange");
        DraftChange draftChange2 = createDraftChange("FULL_NAME", "Product:VV", "UNDERTESTFULLNAME prod VV");
        DraftChange draftChange3 = createDraftChange("FULL_NAME", "Product:prod1", "UNDERTESTFULLNAME prod1 secondChange");
        DraftChange draftChange4 = createDraftChange("FULL_NAME", "Product:r1_prod1_1", "UNDERTESTFULLNAME r1_prod1_1 firstChange");
        List<DraftChange> draftChanges = new ArrayList<DraftChange>(Arrays.asList(draftChange1, draftChange2, draftChange3, draftChange4));

        List<ContentNodeI> returnedNodes = DraftApplicatorService.defaultService().applyDraftChangesToContentNodes(draftChanges, nodes, service, DraftContext.MAIN);
        Assert.assertEquals(draftChange3.getValue(), returnedNodes.get(0).getAttributeValue("FULL_NAME"));
        Assert.assertEquals(draftChange2.getValue(), returnedNodes.get(1).getAttributeValue("FULL_NAME"));
        Assert.assertEquals(draftChange4.getValue(), returnedNodes.get(2).getAttributeValue("FULL_NAME"));
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
