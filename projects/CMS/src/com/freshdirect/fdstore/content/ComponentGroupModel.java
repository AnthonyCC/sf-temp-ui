package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.content.attributes.EnumAttributeName;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ComponentGroupModel extends ContentNodeModelImpl {

	private final static Category LOGGER = LoggerFactory.getInstance(ComponentGroupModel.class);

	private final static Image IMAGE_BLANK = new Image("/media_stat/images/layout/clear.gif", 1, 1);

	private List characteristics;

	private List optionalProds = new ArrayList();
	
	private List<ProductModel> chefsPicks = new ArrayList<ProductModel>();

	public ComponentGroupModel(ContentKey cKey) {
		super(cKey);
	}

	/**
	 * Return image from the HEADER_IMAGE attribute, and IMAGE_BLANK if not specified.
	 * @return
	 */
	public Image getHeaderImage() {
            return FDAttributeFactory.constructImage(this, "HEADER_IMAGE", IMAGE_BLANK);
	}

	/**
	 * Return image from the HEADER_IMAGE attribute, and null if not specified
	 * @return
	 */
        public Image getHeaderImageIfExists() {
            return FDAttributeFactory.constructImage(this, "HEADER_IMAGE");
        }
	
	
	/** 
	 * @return List of String (ERP characteristic names)
	 */
	public List getCharacteristicNames() {
		if (this.characteristics == null) {
			try {
				FDProduct fdp = getHeaderSku().getProduct();
				FDVariation[] fdvs = fdp.getVariations();

				final List<ContentKey> attribErpChars = (List<ContentKey>) getCmsAttributeValue("CHARACTERISTICS");
				final List<ContentKey> erpChars = attribErpChars != null ? attribErpChars : Collections.EMPTY_LIST;
				characteristics = new ArrayList();
				for (Iterator itr = erpChars.iterator(); itr.hasNext();) {
					String erpCharacteristic = ((ContentKey) itr.next()).getId();
					String erpCharName = getErpCharacteristicName(erpCharacteristic);
					// ensure it exists in FDVariations
					for (int x = 0; x < fdvs.length; x++) {
						if (fdvs[x].getName().equals(erpCharName)) {
							characteristics.add(erpCharName);
							break;
						}
					}
				}

			} catch (FDSkuNotFoundException snfe) {
				throw new FDRuntimeException(snfe);
			} catch (FDResourceException fdre) {
				throw new FDRuntimeException(fdre);
			}
		}
		return this.characteristics;
	}

	public boolean isUnavailable() throws FDResourceException {
		return isUnavailable(null);
	}

	public boolean isUnavailable(FDConfigurableI config) throws FDResourceException {
		boolean unavailable = false;
		if (getCharacteristicNames().size() > 0) {
			unavailable = getVariationOptions(config).containsValue(null);
		} else if (getAvailableOptionalProducts().size() < 1) {
			unavailable = true;
		}
		return unavailable;
	}

	public boolean hasCharacteristics() {
		return !getCharacteristicNames().isEmpty();
	}

	/**
	 * @return Map of String (characteristic option name) -> FDVariationOption[]
	 */
	public Map getVariationOptions() throws FDResourceException {
		return getVariationOptions(null);
	}

	private SkuModel getHeaderSku() {
		ProductModel product = (ProductModel) getParentNode();
		return product.getSku(0);
	}

	/**
	 * Returns the variation options where there's a corresponding SKU that is available.
	 * If a configuration is passed in, the check is only done against that specific config. 
	 * 
	 * @param configuration null, or desired configuration options
	 * 
	 * @return Map of String (ERP characteristic name) -> FDVariationOption[]
	 */
	public Map getVariationOptions(FDConfigurableI configuration) throws FDResourceException {
		Map rtnVariation = new HashMap();
		List erpChars = getCharacteristicNames();
		ProductModel p = (ProductModel) this.getParentNode();
		try {
			FDProduct fdp = getHeaderSku().getProduct();
			FDVariation[] fdvs = fdp.getVariations();
			for (Iterator itr = erpChars.iterator(); itr.hasNext();) {
				String erpCharName = (String) itr.next();

				List optionsList = new ArrayList();
				for (int i = 0; i < fdvs.length; i++) {
					FDVariation var = fdvs[i];
					if (var.getName().equalsIgnoreCase(erpCharName)) {
						FDVariationOption[] varOpts = var.getVariationOptions();
						String selectedOption = configuration==null ? null : (String)configuration.getOptions().get(var.getName());
						for (int voIdx = 0; voIdx < varOpts.length; voIdx++) {
							if (selectedOption!=null && !selectedOption.equals(varOpts[voIdx].getName())) {
								continue;
							}
							
							String optSkuCode = varOpts[voIdx].getAttribute(EnumAttributeName.SKUCODE);
							if (optSkuCode == null) {
								optionsList.add(optSkuCode);
								continue;
							}

							SkuModel sku = (SkuModel) ContentFactory.getInstance().getContentNode(optSkuCode);
							if (sku != null && !sku.isUnavailable()) {
								optionsList.add(optSkuCode);
							}
						}

						if (optionsList.size() > 0) {
							rtnVariation.put(erpCharName, varOpts);
						} else {
							rtnVariation.put(erpCharName, null);
						}
					}
				}
			}
		} catch (FDSkuNotFoundException snfe) {
			LOGGER.warn("ComponentGroupModel: catching FDSkuNotFoundException for default sku for prod: "
				+ p
				+ " ,SkuCode: "
				+ p.getDefaultSku()
				+ " and Continuing:\nException message:= "
				+ snfe.getMessage());
		}

		return rtnVariation;
	}

	public List getOptionalProducts() {
		ContentNodeModelUtil.refreshModels(this, "OPTIONAL_PRODUCTS", optionalProds, false);
		return optionalProds;
	}

	public List getAvailableOptionalProducts() {
		List available = new ArrayList();
		for (Iterator prodItr = getOptionalProducts().iterator(); prodItr.hasNext();) {
			ProductModel prod = (ProductModel) prodItr.next();
			if (!prod.isUnavailable()) {
				available.add(prod);
			}
		}
		return available;
	}

	public boolean isShowOptions() {
		return this.getAttribute("SHOW_OPTIONS", true);
	}

	private String getErpCharacteristicName(String erpCharacteristic) {
		int pos = erpCharacteristic.indexOf("/");
		return pos != -1 ? erpCharacteristic.substring(pos + 1) : erpCharacteristic;
	}

	public boolean isOptionsDropDownVertical() {
		int ddStyle=this.getAttribute("COMPONENTGROUP_LAYOUT", EnumComponentGroupLayout.VERTICAL.getId());
		return ddStyle==EnumComponentGroupLayout.VERTICAL.getId();
	}

	public boolean isShowInPopupOnly() {
		int ddStyle=this.getAttribute("COMPONENTGROUP_LAYOUT", EnumComponentGroupLayout.VERTICAL.getId());
		return ddStyle==EnumComponentGroupLayout.POPUP_ONLY.getId();
	}

	/**
	 * This override is here, because this is the proper place for this method, not in the parent ContentNodeModelImpl
	 */
	@Override
        public Html getEditorial() {
	    return FDAttributeFactory.constructHtml(this, "EDITORIAL");
        }

	public List<ProductModel> getChefsPicks() {
            ContentNodeModelUtil.refreshModels(this, "CHEFS_PICKS", chefsPicks, false);
            return new ArrayList<ProductModel>(chefsPicks);
	}
}



