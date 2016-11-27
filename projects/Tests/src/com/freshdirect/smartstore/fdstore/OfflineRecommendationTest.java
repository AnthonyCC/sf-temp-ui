/**
 * 
 */
package com.freshdirect.smartstore.fdstore;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.mockejb.jndi.MockContextFactory;

import com.freshdirect.TestUtils;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.aspects.FDCustomerAspect;
import com.freshdirect.fdstore.aspects.FDFactoryProductInfoAspect;
import com.freshdirect.fdstore.customer.FDCustomerManagerTestSupport;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.ProfileModel;
import com.freshdirect.fdstore.customer.ejb.FDServiceLocator;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.smartstore.ConfigurationStatus;
import com.freshdirect.smartstore.RecommendationServiceConfig;
import com.freshdirect.smartstore.RecommendationServiceType;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.impl.ScriptedRecommendationService;
import com.freshdirect.smartstore.offline.OfflineRecommenderCmd;
import com.freshdirect.smartstore.service.RecommendationServiceFactory;

/**
 * @author zsombor
 *
 */
public class OfflineRecommendationTest extends FDCustomerManagerTestSupport {

    FDServiceLocator locator;
    
    public OfflineRecommendationTest(String name) {
        super(name);
    }

    
    @Override
    public void setUp() throws Exception {
        super.setUp();
        this.setUpDataSet("/com/freshdirect/smartstore/ejb/OfflineRecommender.xml");

        MockContextFactory.setAsInitial();
        
        aspectSystem.add(new FDCustomerAspect() {
            @Override
            public FDCustomerModel getCustomerModel(PrimaryKey pk) {
                FDCustomerModel m = new FDCustomerModel();
                m.setPK(pk);
                m.setProfile(new ProfileModel());
                return m;
            }
            
        });
        TestUtils.initCohortSelector();
        
        Variant vr = new Variant("simple-variant", EnumSiteFeature.DYF, new RecommendationServiceConfig("simple-variant-config",
                RecommendationServiceType.SCRIPTED));
        ScriptedRecommendationService srs = new ScriptedRecommendationService(vr, RecommendationServiceFactory.configureSampler(vr.getServiceConfig(),
                new HashMap<String, ConfigurationStatus>()), true, "toList(\"a1\",\"a2\")");
        vr.setRecommender(srs);
        
        VariantSelectorFactory.getSelector(EnumSiteFeature.DYF).addCohort("A", vr );
        VariantSelectorFactory.getSelector(EnumSiteFeature.DYF).addCohort("B", vr );
        
        aspectSystem.add(new FDFactoryProductInfoAspect().addAvailableSku("a1sku", 2.0).addAvailableSku("a2sku", 3.0));
        
        TestUtils.initCmsManagerFromXmls("classpath:/com/freshdirect/cms/fdstore/content/simple3.xml");
        
        locator = new FDServiceLocator();
    }

    final static int[] DAYS = {1,2,5, 101};
    @Override
    protected IDataSet processDataset(IDataSet input) {
        final long DAY = 24*3600*1000;
        ReplacementDataSet r = new ReplacementDataSet(input);
        r.addReplacementObject("now", new Date());
        for (int day : DAYS) {
            r.addReplacementObject("day_" + day, new Date(System.currentTimeMillis() - DAY * day));
        }
        return r;
    }
    
    
    /* (non-Javadoc)
     * @see com.freshdirect.DbTestCaseSupport#getAffectedTables()
     */
    @Override
    protected String[] getAffectedTables() {
        return new String[] { "CUST.SS_OFFLINE_RECOMMENDATION", "CUST.CUSTOMER", "CUST.PROMO_CUSTOMER", "CUST.SALE", "CUST.FDCUSTOMER", "CUST.PROFILE", "CUST.CUSTOMERINFO", "CUST.FDUSER",
                   "ERPS.PRICING_REGION_ZIPS", "ERPS.PRICING_ZONE", "ERPS.PRICING_REGION"};
    }

    /* (non-Javadoc)
     * @see com.freshdirect.DbTestCaseSupport#getSchema()
     */
    @Override
    protected String getSchema() {
        return null;
    }
    
    public void testRecommend() throws FDResourceException, RemoteException, DataSetException, DatabaseUnitException {
        Set<String> customers = OfflineRecommenderCmd.lookupCustomers(locator.getOfflineRecommender(), 4, 100);

        assertNotNull("customers", customers);
        assertEquals("customers", 1, customers.size());
        assertTrue("customer 4", customers.contains("4"));
        int count = locator.getOfflineRecommender().recommend(new String[] { "DYF" }, "10", null);
        
        assertEquals("offline recommendation", 2, count);
        
        IDataSet liveData = getActualDataSet("CUST.SS_OFFLINE_RECOMMENDATION");
        IDataSet expectedData = loadDataSet("/com/freshdirect/smartstore/ejb/OfflineRecommender-result.xml");
        assertTableEquals(expectedData, liveData, "CUST.SS_OFFLINE_RECOMMENDATION", "LAST_MODIFIED");             
    }
}
