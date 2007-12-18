/*
 * $Workfile:$
 *
 * $Date:$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.customer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;


import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.DepartmentModel;import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.lists.FDCustomerProductList;
import com.freshdirect.fdstore.lists.FDCustomerProductListLineItem;

/**
 *
 *
 * @version $Revision:$
 * @author $Author:$
 */
public class OrderLineUtil {
	
	private final static Category LOGGER = LoggerFactory.getInstance(OrderLineUtil.class);

	private OrderLineUtil() {
	}
	

	/**
	 * CCL
	 * brings a list of old items up to date, throwing away anything that can't be updated
	 */
	public static List updateCCLItems(List listItems) throws FDResourceException {
		assert (listItems != null);
		
		List validItems = new ArrayList(listItems.size());

		for (Iterator i = listItems.iterator(); i.hasNext();) {
			FDCustomerProductListLineItem pl = (FDCustomerProductListLineItem)i.next();
			FDProductSelectionI ps;
			try {
				ps = (FDProductSelectionI)pl.convertToSelection();
			} catch (FDSkuNotFoundException e1) {
				// TODO Auto-generated catch block
	            LOGGER.warn("No sku found for " + pl);
				continue;
			} catch (FDResourceException e1) {
				// TODO Auto-generated catch block	
				LOGGER.warn("Resource exception (" + pl + ')');
				continue;
			} 
			try {
				OrderLineUtil.cleanup(ps);
			} catch (FDInvalidConfigurationException.Unavailable e) {
				LOGGER.warn("Unavailable item found: " + e.getMessage() + '(' + pl + ')');
				// OK, let it go
			} catch (FDInvalidConfigurationException.InvalidSalesUnit e) {
				LOGGER.warn("Configuration exception: " + e.getMessage() + '(' + pl + ')');
				// OK, let it go
			} catch (FDInvalidConfigurationException.MissingVariation e) {
				LOGGER.warn("Missing variation exception: " + e.getMessage() + '(' + pl + ')');
				// OK, let it go
			} catch (FDInvalidConfigurationException.InvalidOption e) {
				LOGGER.warn("Invalid option exeption: " + e.getMessage() + '(' + pl + ')');
				// OK, let it go
			} catch (FDInvalidConfigurationException e) {	
				LOGGER.warn("Configuration exception: " + e.getMessage() + '(' + pl + ')');
				continue;
			}
			validItems.add(pl);
		}
		return validItems;
	}


	/**
	 * brings a list of old items up to date, throwing away anything that can't be updated
	 */
	public static List update(List productSelections, boolean removeUnavailable) throws FDResourceException {

		List validSelections = new ArrayList(productSelections.size());

		for (Iterator i = productSelections.iterator(); i.hasNext();) {
			FDProductSelectionI ps = (FDProductSelectionI) i.next();
			try {
				OrderLineUtil.cleanup(ps);
				validSelections.add(ps);
			} catch (FDInvalidConfigurationException.Unavailable ue) {
			    if (removeUnavailable) continue;
			    validSelections.add(ps);
			} catch (FDInvalidConfigurationException e) {				
				continue;
			}
		}

		return validSelections;
	}
	
	public static List cleanAndRemoveDuplicateProductSelections(List productSelections, boolean removeUnavailable) throws FDResourceException {
		List cleanList = update(productSelections,removeUnavailable);
		return removeDuplicateProductSelections(cleanList);
	}
	

	public static List removeDuplicateProductSelections(List productSelections) throws FDResourceException {
		List cleanList = new ArrayList(productSelections);
		for (int i = 0; i < cleanList.size() - 1; i++) {
			if (OrderLineUtil.isSameConfiguration((FDProductSelectionI) cleanList.get(i), (FDProductSelectionI) cleanList.get(i + 1))) {
				cleanList.remove(i + 1);
				i--;
			}
		}
		return cleanList;
	}

	public static List getValidProductSelectionsFromCCLItems(List cclItems) throws FDResourceException {
		List productSelections = new ArrayList();
		for (Iterator it = cclItems.iterator(); it.hasNext();) {
			FDCustomerProductListLineItem pl = (FDCustomerProductListLineItem)it.next(); 
			try {
				FDProductSelectionI item = pl.convertToSelection();
				item.setRecipeSourceId(pl.getRecipeSourceId());
				productSelections.add(pl.convertToSelection());
			} catch (FDSkuNotFoundException e) {
				// Invalid selections are omitted
				continue;
			} catch (FDResourceException e) {
				throw e;
			}
		}
		return productSelections;
	}

	public static void describe(FDProductSelectionI theProduct) throws FDResourceException, FDInvalidConfigurationException {
		ProductModel prodNode = theProduct.lookupProduct();
		if (prodNode == null) {
			throw new FDInvalidConfigurationException("Can't find product in store for selection " + theProduct.getProductRef());
		}
		
		theProduct.setDescription(prodNode.getFullName());
		
		String recipeId = theProduct.getRecipeSourceId();
		Recipe recipe = recipeId==null ? null : (Recipe) ContentFactory.getInstance().getContentNode(recipeId);
		if (recipe!=null) {
			theProduct.setDepartmentDesc("Recipe: " + recipe.getName());
		} else {
			DepartmentModel dept = prodNode.getDepartment();
			theProduct.setDepartmentDesc(dept.getFullName());
		}
		
		try {
			theProduct.setConfigurationDesc(createConfigurationDescription(theProduct));
		} catch (FDSkuNotFoundException e) {
			throw new FDInvalidConfigurationException(e);
		}

	}

	private static String createConfigurationDescription(FDProductSelectionI theProduct)
		throws FDResourceException,
		FDSkuNotFoundException {

		FDProduct product = theProduct.lookupFDProduct();
		ProductModel prodNode = theProduct.lookupProduct();
		SkuModel skuNode = prodNode.getSku(theProduct.getSkuCode());

		StringBuffer confDescr = new StringBuffer();

		//
		// add sales unit description
		//
		if (theProduct == null) {
			throw new FDSkuNotFoundException("No current product found for " + theProduct.getSku());
		}
		FDSalesUnit unit = product.getSalesUnit(theProduct.getConfiguration().getSalesUnit());
		String salesUnitDescr = unit.getDescription();

		// clean sales unit description
		if (salesUnitDescr != null) {
			if (salesUnitDescr.indexOf("(") > -1) {
				salesUnitDescr = salesUnitDescr.substring(0, salesUnitDescr.indexOf("("));
			}
			salesUnitDescr = salesUnitDescr.trim();
			// empty descriptions, "nm" and "ea" should be ignored
			if ((!"".equals(salesUnitDescr))
				&& (!"nm".equalsIgnoreCase(salesUnitDescr))
				&& (!"ea".equalsIgnoreCase(salesUnitDescr))) {
				if (!prodNode.getAttribute("SELL_BY_SALESUNIT", "").equals("SALES_UNIT")) {
					confDescr.append(salesUnitDescr);
				} else if ((prodNode.getSkus().size() == 1)
					&& (prodNode.getVariationMatrix().isEmpty())
					&& (product.getSalesUnits().length == 1)
					&& (product.getSalesUnits()[0].getName().equalsIgnoreCase("EA"))) {
					confDescr.append(salesUnitDescr);
				}
			}
		}

		//
		// add description of sku variations
		//
		List varMatr = skuNode.getVariationMatrix();
		for (Iterator varIter = varMatr.iterator(); varIter.hasNext();) {
			DomainValue dv =(DomainValue) varIter.next();
			if (dv == null)
				continue;
			if (confDescr.length() > 0)
				confDescr.append(", ");
			confDescr.append(dv.getLabel().trim());
		}
		List varOpts = skuNode.getVariationOptions();
		for (Iterator optIter = varOpts.iterator(); optIter.hasNext();) {
			DomainValue dv = (DomainValue) optIter.next();
			//
			// only add this to the description if it can be used to uniquely describe the selected sku
			//
			boolean useThis = false;
			List skus = prodNode.getSkus();
			for (ListIterator li = skus.listIterator(); li.hasNext();) {
				SkuModel s = (SkuModel) li.next();
				if (s.isUnavailable()) {
					li.remove();
				}
			}
			if (skus.size() == 1) {
				useThis = false;
			} else {
				for (Iterator skIter = skus.iterator(); skIter.hasNext();) {
					SkuModel testSku = (SkuModel) skIter.next();
					List variationOptions = testSku.getVariationOptions();
					for (Iterator voIter = variationOptions.iterator(); voIter.hasNext();) {
						DomainValue testDv = (DomainValue) voIter.next();
						if (dv.getDomain().equals(testDv.getDomain()) && !dv.equals(testDv)) {
							useThis = true;
						}
					}
				}
			}
			if (useThis) {
				if (confDescr.length() > 0)
					confDescr.append(", ");
				confDescr.append(dv.getLabel().trim());
			}
		}

		//
		// add variation options
		//
		FDVariation[] variations = product.getVariations();
		for (int i = 0; i < variations.length; i++) {
			FDVariation variation = variations[i];

			String optionName = (String) theProduct.getConfiguration().getOptions().get(variation.getName());
			if (optionName == null)
				continue;

			FDVariationOption[] options = variation.getVariationOptions();
			for (int j = 0; j < options.length; j++) {
				if (options[j].getName().equals(optionName)) {
					String optDescr = options[j].getDescription();
					if ((!"None".equalsIgnoreCase(optDescr))
						&& (!"nm".equalsIgnoreCase(optDescr))
						&& (!"".equalsIgnoreCase(optDescr))) {
						if (confDescr.length() > 0)
							confDescr.append(", ");
						if (optDescr.indexOf("(") > -1) {
							confDescr.append(optDescr.substring(0, optDescr.indexOf("(")).trim());
						} else {
							confDescr.append(optDescr.trim());
						}
					}
				}
			}
		}

		return confDescr.toString();
	}

	public static boolean isSameConfiguration(FDProductSelectionI cartLine1, FDProductSelectionI cartLine2)
		throws FDResourceException {

		//LOGGER.debug(cartLine1.getSkuCode() +" vs "+cartLine2.getSkuCode());
		String skuCode = cartLine1.getSkuCode();
		if (!skuCode.equals(cartLine2.getSkuCode())) {
			return false;
		}

		ProductModel productNode;
		try {
			productNode = ContentFactory.getInstance().getProduct(skuCode);
		} catch (FDSkuNotFoundException ex) {
			throw new FDResourceException(ex);
		}
		boolean soldBySalesUnit = "SALES_UNIT".equals(productNode.getAttribute("SELL_BY_SALESUNIT", ""));

		if (!soldBySalesUnit && !cartLine1.getSalesUnit().equals(cartLine2.getSalesUnit())) {
			return false;
		}

		Map config1 = cartLine1.getOptions();
		Map config2 = cartLine2.getOptions();
		for (Iterator i = config1.entrySet().iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry) i.next();
			Object c1 = e.getKey();
			Object cv2 = config2.get(c1);

			if (cv2 == null || !cv2.equals(e.getValue())) {
				return false;
			}
		}

		return true;
	}

	public static boolean isSameConfiguration(ConfiguredProduct cartLine1, FDProductSelectionI cartLine2)
	throws FDResourceException {

		//LOGGER.debug(cartLine1.getSkuCode() +" vs "+cartLine2.getSkuCode());
		String skuCode = cartLine1.getSkuCode();
		if (!skuCode.equals(cartLine2.getSkuCode())) {
			return false;
		}
	
		ProductModel productNode;
		try {
			productNode = ContentFactory.getInstance().getProduct(skuCode);
		} catch (FDSkuNotFoundException ex) {
			throw new FDResourceException(ex);
		}
		boolean soldBySalesUnit = "SALES_UNIT".equals(productNode.getAttribute("SELL_BY_SALESUNIT", ""));
	
		if (!soldBySalesUnit && !cartLine1.getSalesUnit().equals(cartLine2.getSalesUnit())) {
			return false;
		}
	
		Map config1 = cartLine1.getOptions();
		Map config2 = cartLine2.getOptions();
		for (Iterator i = config1.entrySet().iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry) i.next();
			Object c1 = e.getKey();
			Object cv2 = config2.get(c1);
	
			if (cv2 == null || !cv2.equals(e.getValue())) {
				return false;
			}
		}
	
		return true;
	}
	
	/**
	 * Bring productselection up-to-date (sku version).
	 *
	 * @throws FDInvalidConfigurationException if the configuration is no longer valid
	 */
	public static void cleanup(FDProductSelectionI prodSel) throws FDResourceException, FDInvalidConfigurationException {

		ProductModel prodNode;
		try {
			prodNode = ContentFactory.getInstance().getProduct(prodSel.getSkuCode());
		} catch (FDSkuNotFoundException e) {
			throw new FDInvalidConfigurationException(e);
		}

		// find most recent fd product based on sku
		FDProductInfo productInfo;
		try {
			productInfo = FDCachedFactory.getProductInfo(prodSel.getSkuCode());
			prodSel.setSku(new FDSku(productInfo.getSkuCode(), productInfo.getVersion()));
		} catch (FDSkuNotFoundException e) {
			throw new FDInvalidConfigurationException(e);
		}

		if (!productInfo.isAvailable()) {
			throw new FDInvalidConfigurationException.Unavailable("SKU no longer available " + prodSel.getSkuCode());
		}

		FDProduct product;
		try {
			product = FDCachedFactory.getProduct(productInfo);
		} catch (FDSkuNotFoundException e) {
			throw new FDInvalidConfigurationException(e);
		}

		ensureValidConfiguration(product, prodSel);

		if (prodNode.hasComponentGroups()) {
			boolean charAvail = prodNode.isCharacteristicsComponentsAvailable(prodSel);
			LOGGER.warn("OrderLineUtil.cleanup() "+prodNode +" -> "+charAvail);
			if (!charAvail) {
				throw new FDInvalidConfigurationException("This configuration is not available");
			}
		}

		prodSel.setQuantity(Math.max(prodSel.getQuantity(), prodNode.getQuantityMinimum()));
		prodSel.setQuantity(Math.min(prodSel.getQuantity(), prodNode.getQuantityMaximum()));

		prodSel.refreshConfiguration();
	}

	private static void ensureValidConfiguration(FDProduct product, FDConfigurableI config) throws FDInvalidConfigurationException {

		// validate salesunit
		boolean salesUnitValid = product.getSalesUnit(config.getSalesUnit()) != null;
		if (!salesUnitValid) {
			throw new FDInvalidConfigurationException.InvalidSalesUnit("Invalid salesUnit found " + config.getSalesUnit());
		}

		// validate options
		Map conf = config.getOptions();
		Set currOptions = new HashSet();
		FDVariation[] variations = product.getVariations();
		for (int vi = 0; vi < variations.length; vi++) {
			FDVariation var = variations[vi];
			String varOpt = (String) conf.get(var.getName());
			currOptions.add(var.getName());
			if (varOpt == null) {
				throw new FDInvalidConfigurationException.MissingVariation("Missing variation found " + var.getName());
			}
			FDVariationOption[] options = var.getVariationOptions();
			boolean optValid = false;
			for (int voi = 0; voi < options.length; voi++) {
				if (options[voi].getName().equals(varOpt)) {
					optValid = true;
					break;
				}
			}
			
			if (!optValid) {
				throw new FDInvalidConfigurationException.InvalidOption("Invalid option " + varOpt + " found for variation " + var.getName());
			}
		}
		
		if(!currOptions.containsAll(conf.keySet())){
			throw new FDInvalidConfigurationException.InvalidOption("Missing Options");
		}
	}


	public static void cleanProductLists(List lists) throws FDResourceException {
		assert (lists != null);
		for (Iterator it = lists.iterator(); it.hasNext();) {
			FDCustomerProductList pl = (FDCustomerProductList) it.next();
			pl.cleanList();
		}	
	}

}