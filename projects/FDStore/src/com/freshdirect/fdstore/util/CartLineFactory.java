package com.freshdirect.fdstore.util;

import java.rmi.RemoteException;
import java.util.*;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.erp.ejb.ErpInfoHome;
import com.freshdirect.erp.ejb.ErpInfoSB;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.fdstore.*;
import com.freshdirect.fdstore.content.*;
import com.freshdirect.fdstore.customer.*;

public class CartLineFactory {

	private static Category LOGGER = LoggerFactory.getInstance( CartLineFactory.class );

    public List createOrderLines(Collection products) throws FDResourceException {
        List lines = new ArrayList();
        for (Iterator pIter = products.iterator(); pIter.hasNext(); ) {
            ProductModel prdModel = (ProductModel) pIter.next();
            
            for (Iterator sIter = prdModel.getSkus().iterator(); sIter.hasNext(); ) {
                SkuModel sku = (SkuModel) sIter.next();

				this.createLines(lines, sku);
            }
        }
        
        return lines;
    }

	public List createOrderLines(String[] materials) throws FDResourceException {
		try {
			Context ctx = ErpServicesProperties.getInitialContext();
			ErpInfoHome home = (ErpInfoHome) ctx.lookup("freshdirect.erp.Info");
			ErpInfoSB infoBean = home.create();
			
			List lines = new ArrayList();
			for (int i=0; i<materials.length; i++) {		
				String mat = materials[i];
				Collection prods = infoBean.findProductsBySapId(mat);
			
				if (prods.isEmpty()) {
					LOGGER.info("No product found for material "+mat+" - skipping.");
					continue;
				}
			
				SkuModel sku = this.findSku(prods);
				if (sku==null) {
					LOGGER.info("No content node for material "+mat+" - skipping.");
					continue;	
				}
				this.createLines(lines, sku);				
			}
			return lines;
		} catch (NamingException ex) {
			throw new FDResourceException(ex);	
		} catch (CreateException ex) {
			throw new FDResourceException(ex);	
		} catch (RemoteException ex) {
			throw new FDResourceException(ex);	
		}
	}

	/**
	 * @param prods Collection of ErpProductInfoModel
	 */
	private SkuModel findSku(Collection prods) throws FDResourceException {
		for (Iterator i=prods.iterator(); i.hasNext(); ) {
			ErpProductInfoModel pim = (ErpProductInfoModel)i.next();
			String skuCode = pim.getSkuCode();
			try {
				ProductModel prod = ContentFactory.getInstance().getProduct( skuCode );
				return prod.getSku( skuCode );
			
			} catch (FDSkuNotFoundException ex) {
			}
		}
		return null;
		
	}

	protected void createLines(List lines, SkuModel sku) throws FDResourceException {
		FDProduct product;
		try {
            FDProductInfo productInfo = sku.getProductInfo();
            if (productInfo.isDiscontinued() || productInfo.isOutOfSeason() || productInfo.isTempUnavailable()) {
                //
                // only do skus that might actually be available for sale
                //
                return;
            }
            product = FDCachedFactory.getProduct(productInfo);
        } catch (FDSkuNotFoundException ex) {
            // no sku? fuggedaboudit...
			return;
        }

        boolean multiple = false; // one order line per sku
        
        // creates the minimum number of configured products to exercise
        // all of the options of all variations and all of the sales units
        // how many to make?
        // find the maximum of the # of sales units and the number of options in each varaition
        int max = product.getSalesUnits().length;
        FDVariation[] variations = product.getVariations();
        for (int i=0; i<variations.length; i++) {
            FDVariation variation = variations[i];
            max = Math.max(max, variation.getVariationOptions().length);
        }

        for (int n=0; ( (n < max) && ((!multiple && (n == 0)) || multiple)); n++) {

			ProductModel prdModel = (ProductModel)sku.getParentNode();
			
            
			FDSalesUnit[] units = product.getSalesUnits();
			FDSalesUnit salesUnit = units[n % units.length];

			Map optionMap = new HashMap();
			for (int i=0; i<variations.length; i++) {
			    FDVariation variation = variations[i];
			    FDVariationOption[] options = variation.getVariationOptions();
			    FDVariationOption option = options[n % options.length];
			    optionMap.put(variation.getName(), option.getName());
			}

			FDConfiguration conf = new FDConfiguration(this.getQuantity(product.getSkuCode()), salesUnit.getName(), optionMap);

			FDCartLineModel cartLine = new FDCartLineModel(new FDSku(product), prdModel, conf, null, ZonePriceListing.MASTER_DEFAULT_ZONE);
			
			try {
				cartLine.refreshConfiguration();
			} catch (FDInvalidConfigurationException e) {
				throw new FDResourceException(e);
			}
            
            lines.add(cartLine);
        }
		
	}

	protected int getQuantity(String skuCode ) {
		//
		// pick a random quantity between 5 and 10, except for...
		//
		if (skuCode.startsWith("MEA")) {
		    //
		    // MEAT
		    // quantity 1 -> 3 for meat
		    //
		    return 1 + (int) (3.0 * Math.random());
		} else if (skuCode.startsWith("TEA") || skuCode.startsWith("COF")) {
		    //
		    // COFFEE & TEA
		    // quantity 1
		    //
		    return 1;
		} else if (skuCode.startsWith("SEA")) {
		    //
		    // SEAFOOD
		    // quantity 1 -> 5
		    //
		    return 1 + (int) (5.0 * Math.random());
		} else if (skuCode.startsWith("DEL")) {
		    //
		    // DELI
		    // quantity 1
		    //
		    return 1;
		} else if (skuCode.startsWith("FRU") || skuCode.startsWith("YEL")) {
		    //
		    // FRUIT
		    // quantity 3
		    //
		    return 3;
		} else if (skuCode.startsWith("VEG")) {
		    //
		    // VEGGIES
		    // quantity 1 -> 5
		    //
		    return 1 + (int) (5.0 * Math.random());
		} else if (skuCode.startsWith("CHE")) {
		    //
		    // CHEESE
		    // quantity 1
		    //
		    return 1;
		}
		return 5 + (int) (5.0 * Math.random());
	}
}
