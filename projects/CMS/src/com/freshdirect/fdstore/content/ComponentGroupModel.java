package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.cms.ContentKey;
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

    private static final Category LOGGER = LoggerFactory.getInstance(ComponentGroupModel.class);

    private static final String CHARACTERISTIC_PATH_ID_SEPARATOR = "/";

	private List<String> characteristics;

	private List<ProductModel> optionalProds = new ArrayList<ProductModel>();
	
	private List<ProductModel> chefsPicks = new ArrayList<ProductModel>();

	public ComponentGroupModel(ContentKey cKey) {
		super(cKey);
	}

	/**
	 * Return image from the HEADER_IMAGE attribute, and IMAGE_BLANK if not specified.
	 * @return
	 */
	public Image getHeaderImage() {
            return FDAttributeFactory.constructImage(this, "HEADER_IMAGE", Image.BLANK_IMAGE);
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
	public List<String> getCharacteristicNames() {
		if (this.characteristics == null) {
			try {
				FDProduct fdp = getHeaderSku().getProduct();
				FDVariation[] fdvs = fdp.getVariations();

				final List<ContentKey> attribErpChars = (List<ContentKey>) getCmsAttributeValue("CHARACTERISTICS");
				final List<ContentKey> erpChars = attribErpChars != null ? attribErpChars : Collections.<ContentKey>emptyList();
				characteristics = new ArrayList<String>();
				for (ContentKey erpCharacteristic : erpChars) {
					String erpCharName = getErpCharacteristicName(erpCharacteristic.getId());
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
	public Map<String,FDVariationOption[]> getVariationOptions() throws FDResourceException {
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
	public Map<String,FDVariationOption[]> getVariationOptions(FDConfigurableI configuration) throws FDResourceException {
		Map<String,FDVariationOption[]> rtnVariation = new HashMap<String,FDVariationOption[]>();
		List<String> erpChars = getCharacteristicNames();
		ProductModel p = (ProductModel) this.getParentNode();
		try {
			FDProduct fdp = getHeaderSku().getProduct();
			FDVariation[] fdvs = fdp.getVariations();
			for (Iterator<String> itr = erpChars.iterator(); itr.hasNext();) {
				String erpCharName = itr.next();

				int counter = 0;
				for (int i = 0; i < fdvs.length; i++) {
					FDVariation var = fdvs[i];
					if (var.getName().equalsIgnoreCase(erpCharName)) {
						FDVariationOption[] varOpts = var.getVariationOptions();
						String selectedOption = configuration==null ? null : (String)configuration.getOptions().get(var.getName());
						for (FDVariationOption varOpt : varOpts) {
							if (selectedOption!=null && !selectedOption.equals(varOpt.getName())) {
								continue;
							}
							
							String optSkuCode = varOpt.getSkuCode();
							if (optSkuCode == null) {
								counter ++;
								continue;
							}

							SkuModel sku = (SkuModel) ContentFactory.getInstance().getContentNode(optSkuCode);
							if (sku != null && !sku.isUnavailable()) {
                                                            counter ++;
							}
						}

						if (counter > 0) {
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

	public List<ProductModel> getOptionalProducts() {
		ContentNodeModelUtil.refreshModels(this, "OPTIONAL_PRODUCTS", optionalProds, false);
		return optionalProds;
	}

	public List<ProductModel> getAvailableOptionalProducts() {
		List<ProductModel> availables = new ArrayList<ProductModel>();
		for (ProductModel optionalPrdoduct : getOptionalProducts()) {
			if (!optionalPrdoduct.isUnavailable()) {
				availables.add(optionalPrdoduct);
			}
		}
		return availables;
	}

	public boolean isShowOptions() {
		return this.getAttribute("SHOW_OPTIONS", true);
	}

	private String getErpCharacteristicName(String erpCharacteristic) {
        int pos = erpCharacteristic.indexOf(CHARACTERISTIC_PATH_ID_SEPARATOR);
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



