package com.freshdirect.cms.application.draft;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsRequest;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsRequestI.Source;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.UserI;
import com.freshdirect.cms.application.draft.extractor.DraftChangeExtractorService;
import com.freshdirect.cms.application.service.SimpleContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.node.ChangedContentNode;
import com.freshdirect.cmsadmin.domain.DraftChange;

import junit.framework.TestCase;


public class DraftChangeExtractorServiceTest extends TestCase {

    private ContentServiceI contentService;
    private DraftContext draftContext = new DraftContext(0, "TestDraft");
    private UserI testUser = new UserI() {
        
        @Override
        public String getName() {
            return "VajasUbul";
        }
        
        @Override
        public boolean isAllowedToWrite() {
            return true;
        }
    };

    private DraftChangeExtractorService extractorService = DraftChangeExtractorService.defaultService();

    protected void setUp() throws Exception {
        
        final XmlTypeService cmsTypeService = new XmlTypeService(
                "classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml");

        contentService = new SimpleContentService( cmsTypeService );
        
    }

    public void testChangedNode() {
        // create test node
        ContentNodeI testNode = createTestNode("Product:prd1");
        assertNotNull(testNode);

        // create test changed node
        ChangedContentNode node = new ChangedContentNode(testNode);

        // test unmodified state
        assertEquals( testNode, node.getWrappedNode() );
        assertNotNull( node.getChanges());
        assertTrue( !node.isChanged() );
        assertTrue( node.getChanges().keySet().isEmpty());

        // update attribute, test consequences
        node.setAttributeValue("FULL_NAME", "Prd");
        
        assertTrue( node.isChanged() );
        assertNotNull( node.getChanges() );
        assertEquals(1, node.getChanges().keySet().size() );
        assertTrue( node.getChanges().keySet().contains("FULL_NAME") );
        assertEquals("Prd", node.getChanges().get("FULL_NAME") );
    }
    
    
    public void testAttributeChange() {
        // create test node
        ContentNodeI testNode = createTestNode("Product:prd1");
        assertNotNull(testNode);

        // create test changed node
        ChangedContentNode node = new ChangedContentNode(testNode);
        // update attribute, test consequences
        node.setAttributeValue("FULL_NAME", "Prd");

        final CmsRequestI request = createTestRequest();
        request.addNode(node);
        
        List<DraftChange> changes = extractorService.extractChangesFromRequest(request);
        assertNotNull(changes);
        assertTrue(!changes.isEmpty());
        assertEquals(1, changes.size() );
        
        DraftChange change = changes.iterator().next();
        // test basic properties of draft change object
        assertDraftChangeProperties(change);

        assertEquals( testNode.getKey().getEncoded(), change.getContentKey() );
        assertEquals("FULL_NAME", change.getAttributeName() );
        assertEquals( "Prd", change.getValue());
    }
    

    public void testRelationshipChange() {
        // create test node
        ContentNodeI testCat = createTestNode("Category:cat1");
        assertNotNull(testCat);
        
        assertRelationshipChange(testCat, "products", Collections.<ContentKey>emptyList(), null );

        assertRelationshipChange(testCat, "products",
                Arrays.asList(ContentKey.getContentKey("Product:prd1")),
                "Product:prd1" );
        assertRelationshipChange(testCat, "products",
                Arrays.asList(ContentKey.getContentKey("Product:prd1"), ContentKey.getContentKey("Product:prd2")),
                "Product:prd1|Product:prd2" );
        assertRelationshipChange(testCat, "products",
                Arrays.asList(ContentKey.getContentKey("Product:prd1"), ContentKey.getContentKey("Product:prd2"), ContentKey.getContentKey("Product:prd3")),
                "Product:prd1|Product:prd2|Product:prd3" );

    }

    private void assertRelationshipChange( ContentNodeI node, String relshipName, List<ContentKey> value, String expectedValue ) {

        ChangedContentNode cat = new ChangedContentNode(node);
        cat.setAttributeValue(relshipName, value );

        final CmsRequestI request = createTestRequest();
        request.addNode(cat);

        List<DraftChange> changes = extractorService.extractChangesFromRequest(request);
        assertNotNull(changes);
        assertTrue(!changes.isEmpty());
        assertEquals(1, changes.size() );

        DraftChange change = changes.iterator().next();
        // test basic properties of draft change object
        assertDraftChangeProperties(change);

        assertEquals( cat.getKey().getEncoded(), change.getContentKey() );
        assertEquals( relshipName, change.getAttributeName() );
        assertEquals( expectedValue, change.getValue());
    }
    
    
    private void assertDraftChangeProperties(DraftChange change) {
        assertNotNull(change);
        
        // test basic properties of draft change object
        assertEquals( testUser.getName(), change.getUserName() );
        assertNotNull( change.getDraft() );
        assertEquals( Long.valueOf(draftContext.getDraftId()), change.getDraft().getId() );
        assertEquals( draftContext.getDraftName(), change.getDraft().getName() );

    }
    
    
    private ContentNodeI createTestNode(String contentKey) {
        return contentService.createPrototypeContentNode(ContentKey.getContentKey(contentKey), draftContext);
    }
    
    
    private CmsRequestI createTestRequest() {
        return new CmsRequest( testUser, Source.ELSE, draftContext );
    }
}
