package com.freshdirect.smartstore.fdstore;

import java.util.Date;

import javax.naming.Context;

import junit.framework.TestCase;

import org.mockejb.interceptor.AspectSystem;

import com.freshdirect.TestUtils;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.common.pricing.CharacteristicValuePrice;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.Pricing;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.common.pricing.SalesUnitRatio;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.ZonePriceInfoModel;
import com.freshdirect.fdstore.ZonePriceListing;
import com.freshdirect.fdstore.ZonePriceModel;
import com.freshdirect.fdstore.aspects.FDFactoryProductInfoAspect;
import com.freshdirect.fdstore.aspects.FDProductAspect;
import com.freshdirect.fdstore.aspects.ZoneConfigurationAspect;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.pricing.ProductModelPricingAdapter;

public class ZonePricingTest extends TestCase {

    XmlContentService service;
    
    protected void setUp() throws Exception {
        Context context = TestUtils.createContext();
        FDCachedFactory.mockInstances();

        service = TestUtils.initCmsManagerFromXmls("classpath:/com/freshdirect/cms/fdstore/content/FeaturedProducts.xml");
        TestUtils.createTransaction(context);
        TestUtils.initFDStoreProperties();
        
        TestUtils.createMockContainer(context);

        AspectSystem aspectSystem = TestUtils.createAspectSystem();
        
        
        FDFactoryProductInfoAspect fa = new FDFactoryProductInfoAspect();
        fa.addAvailableSku("PAS0040040", 50, 30);
        fa.addAvailableSku("PAS0059136", 40, 25);
        
        fa.addZonePriceInfo("PAS0059136", create(30, "cheap-zone"));
        fa.addZonePriceInfo("PAS0059136", create(60, "expensive-zone"));
        
        fa.addAvailableSku("PAS0062422", 200, 190);
        fa.addAvailableSku("PAS0062425", 120, 110);
        
        fa.addZonePriceInfo("PAS0062425", create(80, 60, "cheap-zone", 10, 20));
        fa.addZonePriceInfo("PAS0062425", create(140, 120, "expensive-zone", 30, 15));
        
        aspectSystem.add(fa);
        
        ZoneConfigurationAspect za = new ZoneConfigurationAspect();
        za.addZoneMasterInfo(ZonePriceListing.MASTER_DEFAULT_ZONE);
        za.addZoneMasterInfo(ZonePriceListing.MASTER_DEFAULT_ZONE, "cheap-zone");
        za.addZoneMasterInfo(ZonePriceListing.MASTER_DEFAULT_ZONE, "expensive-zone");
        
        aspectSystem.add(za);
        
        FDProductAspect fpr = new FDProductAspect();
        
        FDSalesUnit[] salesUnits =  {
                new FDSalesUnit("salesUnit", "salesUnit desc", 2, 3, "foot")
        };
        
        fpr.addProduct(new FDProduct("PAS0062425", 1, new Date(), null, null, salesUnits,
                new Pricing(
                        new ZonePriceListing()
                            .addZonePrice(new ZonePriceModel(ZonePriceListing.MASTER_DEFAULT_ZONE, new MaterialPrice[]{ new MaterialPrice(20, "db", 0.0)} ))
                            .addZonePrice(new ZonePriceModel("cheap-zone", new MaterialPrice[] { new MaterialPrice(15, "db", 0.0)})),
                        new CharacteristicValuePrice[0],
                        new SalesUnitRatio[0]
                ) 
        , salesUnits, null));
        aspectSystem.add(fpr);
        
    }
    
    
    public void testPricing() throws InvalidContentKeyException {
        ContentNodeModel node = ContentFactory.getInstance().getContentNodeByKey(ContentKey.create(FDContentTypes.PRODUCT, "ckibni_chocchip"));
        
        assertNotNull("ckibni_chocchip", node);
        assertTrue("ckibni_chocchip is a product", node instanceof ProductModel);
        
        ProductModel product = (ProductModel) node;
        
        
        
        {
            SkuModel defaultSkuInTheMain = product.getDefaultSku();
            assertNotNull("default sku", defaultSkuInTheMain);
            assertEquals("default sku is PAS0059136", "PAS0059136", defaultSkuInTheMain.getContentName());
            
            assertEquals("default price is in the main zone",40.0, product.getPrice(0));
            
            assertEquals("price in the PricingContext.DEFAULT with 0.1 saving", 36.0, product.getPrice(0.1));
            assertEquals("PriceFormatted", "$40.00/ea", product.getPriceFormatted(0.0));
            assertEquals("PriceFormatted with 0.1 saving", "$36.00/ea", product.getPriceFormatted(0.1));

            assertEquals("getWasPriceFormatted", null, product.getWasPriceFormatted(0.0));
        }
        
        {
            PricingContext ctx = new PricingContext("cheap-zone");
            ProductModelPricingAdapter adapter = new ProductModelPricingAdapter(product, ctx);
                        
            SkuModel defaultSkuInTheCheapZone = adapter.getDefaultSku();
            assertNotNull("default sku", defaultSkuInTheCheapZone);
            assertEquals("default sku is PAS0059136 in cheap-zone", "PAS0059136", defaultSkuInTheCheapZone.getContentName());

            
            assertEquals("price in the 'cheap-zone'", 30.0, adapter.getPrice(0));
            assertEquals("price in the 'cheap-zone' with 0.1 saving", 27.0, adapter.getPrice(0.1));
            assertEquals("PriceFormatted", "$30.00/ea", adapter.getPriceFormatted(0.0));
            assertEquals("PriceFormatted with 0.1 saving", "$27.00/ea", adapter.getPriceFormatted(0.1));
            
        }

        {
            PricingContext ctx = new PricingContext("expensive-zone");
            ProductModelPricingAdapter adapter = new ProductModelPricingAdapter(product, ctx);
            
            SkuModel defaultSkuInTheExpensiveZone = adapter.getDefaultSku();
            assertNotNull("default sku", defaultSkuInTheExpensiveZone);
            assertEquals("default sku is PAS0040040 in expensive-zone", "PAS0040040", defaultSkuInTheExpensiveZone.getContentName());

            assertEquals("price in the 'expensive-zone'", 50.0, adapter.getPrice(0));
            assertEquals("price in the 'expensive-zone' with 0.1 saving", 45.0, adapter.getPrice(0.1));
            assertEquals("PriceFormatted", "$50.00/ea", adapter.getPriceFormatted(0.0));
            assertEquals("PriceFormatted with 0.1 saving", "$45.00/ea", adapter.getPriceFormatted(0.1));
            
        }
    }
    
    public void testDeals() throws InvalidContentKeyException {
        // 
        ContentNodeModel node = ContentFactory.getInstance().getContentNodeByKey(ContentKey.create(FDContentTypes.PRODUCT, "pietrt_apple_fro"));
        
        assertNotNull("pietrt_apple_fro", node);
        assertTrue("pietrt_apple_fro is a product", node instanceof ProductModel);
        
        ProductModel product = (ProductModel) node;

        
        {
            SkuModel defaultSkuInTheMain = product.getDefaultSku();
            assertNotNull("default sku", defaultSkuInTheMain);
            assertEquals("default sku is PAS0062425", "PAS0062425", defaultSkuInTheMain.getContentName());
            
            assertEquals("default price is in the main zone",120.0, product.getPrice(0));
            
            assertEquals("price in the PricingContext.DEFAULT with 0.1 saving", 108.0, product.getPrice(0.1));
            assertEquals("PriceFormatted", "$120.00/ea", product.getPriceFormatted(0.0));
            assertEquals("PriceFormatted with 0.1 saving", "$108.00/ea", product.getPriceFormatted(0.1));

            assertEquals("getWasPriceFormatted", "$120.00", product.getWasPriceFormatted(0.3));
            
            assertEquals("getAboutPriceFormatted", "about $80.00/salesunit", product.getAboutPriceFormatted(0.0));
            
            assertEquals("getAboutPriceFormatted", "about $72.00/salesunit", product.getAboutPriceFormatted(0.1));
            
            assertEquals("highest deal percentage in the main zone", 0, product.getHighestDealPercentage());
            
        }

        {
            PricingContext ctx = new PricingContext("cheap-zone");
            ProductModelPricingAdapter adapter = new ProductModelPricingAdapter(product, ctx);
            
            SkuModel defaultSkuInTheCheapZone = adapter.getDefaultSku();
            assertNotNull("default sku", defaultSkuInTheCheapZone);
            assertEquals("default sku is PAS0062425", "PAS0062425", defaultSkuInTheCheapZone.getContentName());
            
            assertEquals("default price is in the main zone",120.0, product.getPrice(0));
            
            assertEquals("price in the cheap-zone", 60.0, adapter.getPrice(0));
            assertEquals("price in the cheap-zone with 0.1 saving", 54.0, adapter.getPrice(0.1));
            assertEquals("PriceFormatted", "$60.00/ea", adapter.getPriceFormatted(0.0));
            assertEquals("PriceFormatted with 0.1 saving", "$54.00/ea", adapter.getPriceFormatted(0.1));

            assertEquals("getWasPriceFormatted", "$60.00", adapter.getWasPriceFormatted(0.3));
            
            assertEquals("getAboutPriceFormatted", "about $40.00/salesunit", adapter.getAboutPriceFormatted(0.0));
            
            assertEquals("getAboutPriceFormatted", "about $36.00/salesunit", adapter.getAboutPriceFormatted(0.1));
            
            assertEquals("highest deal percentage in cheap-zone", 20, adapter.getHighestDealPercentage());
            assertEquals("tiered deal percentage in cheap-zone", 20, adapter.getTieredDealPercentage());
            assertEquals("deal percentage in cheap-zone", 10, adapter.getDealPercentage());

        }
    }

    public void testAdapters() throws InvalidContentKeyException {
        ContentNodeModel node = ContentFactory.getInstance().getContentNodeByKey(ContentKey.create(FDContentTypes.PRODUCT, "pietrt_apple_fro"));
        
        assertNotNull("pietrt_apple_fro", node);
        assertTrue("pietrt_apple_fro is a product", node instanceof ProductModel);
        
        ProductModel product = (ProductModel) node;
        {
            PricingContext ctx = new PricingContext("cheap-zone");
            
            ProductModelPricingAdapter adapter = new ProductModelPricingAdapter(product, ctx);
            SkuModel defaultSkuInTheMain = adapter.getDefaultSku();
            assertNotNull("default sku", defaultSkuInTheMain);
            assertEquals("default sku is PAS0062425", "PAS0062425", defaultSkuInTheMain.getContentName());
            
            assertEquals("default price is in the default zone",60.0, adapter.getPrice(0));
            
            assertEquals("price in the default zone with 0.1 saving", 54.0, adapter.getPrice(0.1));
            assertEquals("PriceFormatted", "$60.00/ea", adapter.getPriceFormatted(0.0));
            assertEquals("PriceFormatted with 0.1 saving", "$54.00/ea", adapter.getPriceFormatted(0.1));

            assertEquals("getWasPriceFormatted", "$60.00", adapter.getWasPriceFormatted(0.3));
            
            assertEquals("getAboutPriceFormatted", "about $40.00/salesunit", adapter.getAboutPriceFormatted(0.0));
            
            assertEquals("getAboutPriceFormatted", "about $36.00/salesunit", adapter.getAboutPriceFormatted(0.1));
            
            
            assertEquals("deal percentage in cheap zone", 10, adapter.getDealPercentage());
            assertEquals("tiered deal percentage in cheap zone", 20, adapter.getTieredDealPercentage());
            assertEquals("highest deal percentage in cheap zone", 20, adapter.getHighestDealPercentage());
        }
        
        {
            PricingContext ctx = new PricingContext("expensive-zone");
            
            ProductModelPricingAdapter adapter = new ProductModelPricingAdapter(product, ctx);
            SkuModel defaultSkuInTheMain = adapter.getDefaultSku();
            assertNotNull("default sku", defaultSkuInTheMain);
            assertEquals("default sku is PAS0062425", "PAS0062425", defaultSkuInTheMain.getContentName());

            assertEquals("deal percentage in expensive zone", 30, adapter.getDealPercentage());
            assertEquals("tiered deal percentage in expensive zone", 15, adapter.getTieredDealPercentage());
            assertEquals("highest deal percentage in expensive zone", 30, adapter.getHighestDealPercentage());
            
        }
        
    }
    
    
    static ZonePriceInfoModel create(double sellingPrice, String sapZoneId) {
        return new ZonePriceInfoModel(sellingPrice, sellingPrice, false, 0, 0, sapZoneId);        
    }
    
    static ZonePriceInfoModel create(double sellingPrice, double promoPrice, String sapZoneId) {
        return new ZonePriceInfoModel(sellingPrice, promoPrice, true, 0, 0, sapZoneId);        
    }

    static ZonePriceInfoModel create(double sellingPrice, double promoPrice, String sapZoneId, int dealPercentage, int tieredDealPercentage) {
        return new ZonePriceInfoModel(sellingPrice, promoPrice, true, dealPercentage, tieredDealPercentage, sapZoneId);        
    }

}
