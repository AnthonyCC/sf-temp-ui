package com.freshdirect.fdstore.customer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import com.freshdirect.fdstore.FDMaterialSalesArea;
import com.freshdirect.fdstore.FDPlantMaterial;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.SalesAreaInfo;
import com.freshdirect.fdstore.ZonePriceInfoListing;
import com.freshdirect.fdstore.ZonePriceInfoModel;
import com.freshdirect.fdstore.ZonePriceListing;
import com.freshdirect.fdstore.content.TestFDInventoryCache;
import com.freshdirect.framework.util.DayOfWeekSet;
import com.mockrunner.mock.ejb.MockUserTransaction;

/**
 * Supporting base class for more advanced EJB testcases using a mock EJB container.
 */
public abstract class FDCustomerManagerTestSupport extends DbTestCaseSupport {

    public FDCustomerManagerTestSupport(String name) {
        super(name);
    }

    ContentServiceI service;
    protected Context context;
    protected MockContainer container;
    protected AspectSystem aspectSystem;

    MockUserTransaction mockTransaction;
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

    public static FDProductInfo createProductInfo(String sku, Date now, String[] materials, TestFDInventoryCache inventoryCache, final EnumAvailabilityStatus status) {
        ZonePriceInfoListing dummyList = new ZonePriceInfoListing();
        ZonePriceInfoModel dummy = new ZonePriceInfoModel(1.0, 1.0, "ea", null, false, 0, 0, ZonePriceListing.DEFAULT_ZONE_INFO);
        dummyList.addZonePriceInfo(ZonePriceListing.DEFAULT_ZONE_INFO, dummy);
        Map<String, FDPlantMaterial> plantInfo = new HashMap<String, FDPlantMaterial>() {

            {
                put("1000", new FDPlantMaterial(EnumATPRule.MATERIAL, false, false, DayOfWeekSet.EMPTY, 1, "1000", false));
            }
        };

        Map<String, FDMaterialSalesArea> mAvail = new HashMap<String, FDMaterialSalesArea>() {

            {
                put("1000" + "1000", new FDMaterialSalesArea(new SalesAreaInfo("1000", "1000"), status.getStatusCode(),
                        new java.util.GregorianCalendar(3000, java.util.Calendar.JANUARY, 1).getTime(), "XYZ", null, "1000"));
            };
        };
        ;

        return new FDProductInfo(sku, 0, null, null, ZonePriceInfoListing.getDummy(), plantInfo, mAvail, false);

    }

    public static interface MockErpCustomerHome extends ErpCustomerHome, EJBLocalHome {
    }
}
