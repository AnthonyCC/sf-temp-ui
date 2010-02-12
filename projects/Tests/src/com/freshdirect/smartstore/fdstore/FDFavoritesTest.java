/**
 * 
 */
package com.freshdirect.smartstore.fdstore;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;

import junit.framework.TestCase;

import com.freshdirect.TestUtils;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.event.RecommendationEventLogger;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.FavoriteList;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.RecommendationServiceConfig;
import com.freshdirect.smartstore.RecommendationServiceType;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.dsl.CompileException;
import com.freshdirect.smartstore.service.RecommendationServiceFactory;

/**
 * @author zsombor
 * @author csongor
 */
public class FDFavoritesTest extends TestCase {

    private XmlContentService service;

    RecommendationEventLoggerMockup eventLogger;
    
    public FDFavoritesTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();

        List list = new ArrayList();
        list.add(new XmlTypeService("classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));

        CompositeTypeService typeService = new CompositeTypeService(list);

        service = new XmlContentService(typeService, new FlexContentHandler(), "classpath:/com/freshdirect/cms/fdstore/content/FeaturedProducts.xml,classpath:/com/freshdirect/smartstore/fdstore/FDFavorites.xml");

        CmsManager.setInstance(new CmsManager(service, null));

        Context context = TestUtils.createContext();
        
        TestUtils.createTransaction(context);
        
        TestUtils.createAspectSystem();
               
        eventLogger = new RecommendationEventLoggerMockup();
        RecommendationEventLogger.setInstance(eventLogger);
    }

    RecommendationService favrs = null;

    RecommendationService getFeaturedItemsService() throws CompileException {
        if (favrs == null) {
        	favrs = RecommendationServiceFactory.configure(new Variant("favorites", EnumSiteFeature.FAVORITES,
					new RecommendationServiceConfig("favorites_config", RecommendationServiceType.FAVORITES)
        	    .set(RecommendationServiceFactory.CKEY_SAMPLING_STRATEGY, "deterministic")));
        }
        return favrs;
    }

    public void testRecommendationService() throws CompileException {
    	FavoriteList favorites = (FavoriteList) ContentFactory.getInstance().getContentNodeByKey(
    			new ContentKey(FDContentTypes.FAVORITE_LIST, RecommendationServiceFactory.DEFAULT_FAVORITE_LIST_ID));

        assertNotNull("spe_cooki_cooki category", favorites);

        SessionInput si = new SessionInput((String) null, null, null);

        List nodes = getFeaturedItemsService().recommendNodes(si);
        assertNotNull("recommended nodes", nodes);
        assertEquals("recommended nodes size", 5, nodes.size());

        assertNode("0-spe_madmoose_chc", nodes, 0, "spe_madmoose_chc");
        assertNode("1-spe_moore_lemon", nodes, 1, "spe_moore_lemon");
        assertNode("3-cake_choclayer", nodes, 2, "cake_choclayer");
        assertNode("4-spe_walkers_shortbre_02", nodes, 3, "spe_walkers_shortbre_02");
        assertNode("5-fro_up_mudck", nodes, 4, "fro_up_mudck");
    }

    private void assertNode(String string, List nodes, int i, String key) {
        assertNotNull("not-null:" + string + '[' + i + ']', nodes.get(i));
        assertEquals("content-key:" + string + '[' + i + ']', key, ((ContentNodeModel) nodes.get(i)).getContentKey().getId());
    }
}
