package com.freshdirect.smartstore.fdstore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.mockejb.MockContainer;
import org.mockejb.interceptor.AspectSystem;

import com.freshdirect.TestUtils;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.event.RecommendationEventLogger;
import com.freshdirect.event.RecommendationEventsAggregate;
import com.freshdirect.fdstore.aspects.FDFactoryProductInfoAspect;
import com.freshdirect.fdstore.aspects.ProductStatisticProviderAspect;
import com.freshdirect.fdstore.aspects.ProductStatisticUserProviderAspect;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.webapp.util.FDEventUtil;

import junit.framework.TestCase;

public class RecommendationServiceTestBase extends TestCase {
    
    XmlContentService service;
    AspectSystem aspectSystem ;
    
    public RecommendationServiceTestBase(String name) {
        super(name);
    }
    
    public RecommendationServiceTestBase() {
        super();
    }
    
    public void setUp() throws Exception {
        super.setUp();
        EnumSiteFeature.mock();
        List list = new ArrayList();
        list.add(new XmlTypeService("classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));

        CompositeTypeService typeService = new CompositeTypeService(list);

        service = new XmlContentService(typeService, new FlexContentHandler(), getCmsXmlName());

        CmsManager.setInstance(new CmsManager(service, null));

        Context context = TestUtils.createContext();
        
        TestUtils.createTransaction(context);
        
        MockContainer mockContainer = TestUtils.createMockContainer(context);

        aspectSystem = TestUtils.createAspectSystem();

        initAspects(aspectSystem);
        ScoreProvider.setInstance(new ProductStatisticUserProviderAspect() {
            
            public Map getUserProductScores(String userId) {
                try {
                    Map map = new HashMap();
                    if ("user-with-favorite-prods".equals(userId)) {
                        map.put(ContentKey.create(FDContentTypes.PRODUCT, "gro_enfamil_powder_m_02"), new Float(10));
                    }
                    // gro_7gen_diaperlg
                    if ("user-with-favorite-prods2".equals(userId)) {
                        map.put(ContentKey.create(FDContentTypes.PRODUCT, "gro_enfamil_powder_m_02"), new Float(10));
                        map.put(ContentKey.create(FDContentTypes.PRODUCT, "gro_7gen_diaperlg"), new Float(20));
                    }
                    return map;
                } catch (InvalidContentKeyException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        
        createRecommendationEventLoggerMockup();

    }

    protected String getCmsXmlName() {
        return "classpath:/com/freshdirect/cms/fdstore/content/FeaturedProducts.xml";
    }

    protected void createRecommendationEventLoggerMockup() throws NamingException {
        RecommendationEventLoggerMockup eventLogger = new RecommendationEventLoggerMockup();
        RecommendationEventLogger.setInstance(eventLogger);
    }

    protected void initAspects(AspectSystem aspectSystem) {
        aspectSystem.add(new FDFactoryProductInfoAspect().addAvailableSku("SPE0063144", 2.0).addAvailableSku("SPE0060510", 3.0).addAvailableSku("SPE0000468",
                4.0).addAvailableSku("GRO001792", 5.0).addAvailableSku("FRO0066635", 64.0)
                // YourFavorites in FI
                .addAvailableSku("HBA0063975", 100).addAvailableSku("HBA0072207", 110).addAvailableSku("GRO0065252").addAvailableSku("GRO0057899")
                .addAvailableSku("GRO001066").addAvailableSku("HBA0063975").addAvailableSku("HBA0072637").addAvailableSku("GRO001055")
             //   .addAvailableSku("FRO0060512", 32) // for spe_bruces_whtsvn:'Bruce's Seven Layer White Cake, Frozen'
                
        );
        
        aspectSystem.add(new ProductStatisticProviderAspect() {
            public Map getGlobalProductScores() {
                try {
                    Map map = new HashMap();
                    map.put(ContentKey.create(FDContentTypes.PRODUCT, "cfncndy_ash_mcrrd"), new Float(100));
                    map.put(ContentKey.create(FDContentTypes.PRODUCT, "dai_orgval_whlmilk_01"), new Float(90));
                    map.put(ContentKey.create(FDContentTypes.PRODUCT, "dai_organi_2_milk_02"), new Float(80));
                    
                    
                    String[] keys = StringUtils.split("gro_earths_oat_cereal, gro_enfamil_powder_m_02,hba_1ups_aloe_rf,hab_pampers_crsrs_4,hba_svngen_dprs_3",',');
                    for (int i=0;i<keys.length;i++) {
                        map.put(ContentKey.create(FDContentTypes.PRODUCT, keys[i].trim()), new Float(200-i));
                    }
                    map.put(ContentKey.create(FDContentTypes.PRODUCT, "spe_walkers_shortbre_02"), new Float(5));
                    
                    return map;
                } catch (InvalidContentKeyException e) {
                    throw new RuntimeException(e);
                }
            }

        });
    }
    
    RecommendationEventLoggerMockup getMockup() {
        RecommendationEventLogger instance = RecommendationEventLogger.getInstance();
        RecommendationEventLoggerMockup result = null;
        if (!(instance instanceof RecommendationEventLoggerMockup)) {
            try {
                result = new RecommendationEventLoggerMockup();
                RecommendationEventLogger.setInstance(result);
            } catch (NamingException e) {
                throw new RuntimeException(e);
            }
        } else {
            result = (RecommendationEventLoggerMockup) instance;
        }
        return result;
    }

    
    protected void tearDown() throws Exception {
        FDEventUtil.flushImpressions();
        getMockup().getCollectedEvents().clear();
        super.tearDown();
    }

    protected void assertRecommendationEventsAggregate(String label, String contentKey, RecommendationEventsAggregate event) {
        assertEquals(label + " content key", contentKey, event.getContentId());
        assertEquals(label + " variant id", "fi", event.getVariantId());
    }

    protected void assertNode(String string, List nodes, int i, String key) {
        assertNotNull("not-null:" + string + '[' + i + ']', nodes.get(i));
        assertEquals("content-key:" + string + '[' + i + ']', key, ((ContentNodeModel) nodes.get(i)).getContentKey().getId());
    }

}
