package com.freshdirect.fdstore.customer;

import java.util.Date;

import javax.ejb.EJBLocalHome;
import javax.naming.Context;

import org.mockejb.MockContainer;
import org.mockejb.interceptor.AspectSystem;
import org.mockejb.jndi.MockContextFactory;

import com.freshdirect.DbTestCaseSupport;
import com.freshdirect.TestUtils;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ejb.ErpCustomerHome;
import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.fdstore.EnumAvailabilityStatus;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.ZonePriceInfoListing;
import com.freshdirect.fdstore.ZonePriceInfoModel;
import com.freshdirect.fdstore.ZonePriceListing;
import com.freshdirect.fdstore.content.TestFDInventoryCache;
import com.mockrunner.mock.ejb.MockUserTransaction;

/**
 * Supporting base class for more advanced EJB testcases using a mock EJB
 * container.
 */
public abstract class FDCustomerManagerTestSupport extends DbTestCaseSupport {

    public FDCustomerManagerTestSupport(String name) {
        super(name);
    }

    ContentServiceI                 service;
    protected Context               context;
    protected MockContainer         container;
    protected AspectSystem          aspectSystem;

    MockUserTransaction             mockTransaction;
    private ErpCustomerFinderAspect customerInfoAspect;

    public void setUp() throws Exception {

        context = TestUtils.createContext();
        dbUnitSetUp(context);
        dbUnitRegisterPool(context);

        container = TestUtils.createMockContainer(context);

        mockTransaction = TestUtils.createTransaction(context);

        aspectSystem = TestUtils.createAspectSystem();

        aspectSystem.add(customerInfoAspect = new ErpCustomerFinderAspect(null));
    }
    
    

    public void tearDown() throws Exception {
        dbUnitTearDown(null);
        MockContextFactory.revertSetAsInitial();
    }

    protected void setCustomerInfo(ErpCustomerInfoModel customerInfo) {
        this.customerInfoAspect.setCustomerInfo(customerInfo);
    }

    /**
     * @param sku
     * @param now
     * @param materials
     * @param inventoryCache
     * @return
     */
    public static FDProductInfo createProductInfo(String sku, Date now, String[] materials, TestFDInventoryCache inventoryCache) {
        return createProductInfo(sku, now, materials, inventoryCache, EnumAvailabilityStatus.AVAILABLE);
    }
    
    public static FDProductInfo createProductInfo(String sku, Date now, String[] materials, TestFDInventoryCache inventoryCache, EnumAvailabilityStatus status) {
        ZonePriceInfoListing dummyList = new ZonePriceInfoListing();
        ZonePriceInfoModel dummy = new ZonePriceInfoModel(1.0, 1.0, "ea", null, false, 0, 0, ZonePriceListing.MASTER_DEFAULT_ZONE);
        dummyList.addZonePriceInfo(ZonePriceListing.MASTER_DEFAULT_ZONE, dummy);
        return new FDProductInfo(sku,1, materials,EnumATPRule.MATERIAL, status, now,inventoryCache,null,null,dummyList, null, null, null, new Date[0],null);
    }
    
    
    public static interface MockErpCustomerHome extends ErpCustomerHome, EJBLocalHome {
    }
}
