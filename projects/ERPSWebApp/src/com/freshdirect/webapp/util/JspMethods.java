/*
 * JspMethods.java
 * 
 * Created on March 13, 2002, 11:42 AM
 */
package com.freshdirect.webapp.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.ZonePriceInfoModel;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.Domain;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.MediaI;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.fdstore.util.ProductDisplayUtil;
import com.freshdirect.fdstore.util.HowToCookItUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;

/**
 * 
 * @author rgayle
 * @version
 */
public class JspMethods {

	private static Logger LOGGER = LoggerFactory
			.getInstance(JspMethods.class);

	public final static ProductModel.PriceComparator priceComp = new ProductModel.PriceComparator();

	public static String leadZeroes(double dblItem, int maxLength) {
		long dblToLng = Math.round(dblItem * 100);
		return JspMethods.leadZeroes(dblToLng + "", maxLength);
	}

	public static String leadZeroes(int intItem, int maxLength) {
		return JspMethods.leadZeroes(intItem + "", maxLength);
	}

	public static String leadZeroes(String ItemToPad, int maxLength) {
		String stringItem = ItemToPad;
		StringBuffer leadString = new StringBuffer();
		int lenStringThing = stringItem.length();
		for (int i = (maxLength - lenStringThing); i > 0; i--) {
			leadString.append("0");
		}
		leadString.append(stringItem);
		return leadString.toString();
	}

	public static String getImageDimensions(Image imgItem) {

		int height = -1;
		int width = -1;
		StringBuffer imgDimString = new StringBuffer();
		if (imgItem != null) {
			width = imgItem.getWidth();
			height = imgItem.getHeight();
			if (width != -1 && height != -1) {
				imgDimString.append(" width=\"");
				imgDimString.append(width);
				imgDimString.append("\" ");

				imgDimString.append(" height=\"");
				imgDimString.append(height);
				imgDimString.append("\" ");
			}
		} else
			imgDimString.append("");
		return imgDimString.toString();
	}

	public static String getProductNameToUse(ContentNodeModel topFolder) {
		Object attribute = topFolder.getCmsAttributeValue("LIST_AS");
		String useName = attribute instanceof String ? (String) attribute : "" ;
		String displayAttribute = null;
		if ("nav".equalsIgnoreCase(useName)) {
			displayAttribute = "nav";
		} else if ("glance".equalsIgnoreCase(useName)) {
			displayAttribute = "glance";
		} else {
			displayAttribute = "full";
		}
		return displayAttribute;
	}

	public static String getTaxonomy(ProductModel product) {
		return getTaxonomy(product, false);
	}

	public static String getTaxonomy(ProductModel product, boolean showId) {
		if (product instanceof ConfiguredProduct) {
			product = ((ConfiguredProduct) product).getSourceProduct();
		}
		ContentNodeModel parent = product.getParentNode();
		List<CategoryModel> catList = new ArrayList<CategoryModel>();
		while (parent instanceof CategoryModel) {
			catList.add((CategoryModel)parent);
			parent = parent.getParentNode();
		}

		String taxonomy = "";

		taxonomy += showId ? "<span title=\""
				+ ((DepartmentModel) parent).getContentName() + "\">" : "";
		taxonomy += ((DepartmentModel) parent).getFullName().toUpperCase();
		taxonomy += showId ? "</span>" : "";
		taxonomy += " &raquo; ";

		for (int k = catList.size() - 1; k >= 0; --k) {
			CategoryModel c = (CategoryModel) catList.get(k);
			if (k > 0) {
				taxonomy += showId ? "<span title=\"" + c.getContentName()
						+ "\">" : "";
				taxonomy += c.getFullName();
				taxonomy += showId ? "</span>" : "";
				taxonomy += " &raquo; ";
			} else {
				taxonomy += showId ? "<span title=\"" + c.getContentName()
						+ "\">" : "";
				taxonomy += c.getFullName();
				taxonomy += showId ? "</span>" : "";
			}
		}

		return taxonomy;
	}

//	public static String getDisplayName(ContentNodeModel content_node,
//			ProductModel prodNode) {
//		if (content_node == null
//				|| prodNode == null
//				|| !(ContentNodeI.TYPE_CATEGORY.equals(content_node
//						.getContentType())))
//			return "";
//		String nameToUse = JspMethods.getProductNameToUse(content_node);
//		if (nameToUse == null || nameToUse.equalsIgnoreCase("full"))
//			return prodNode.getFullName();
//		if (nameToUse != null && nameToUse.equalsIgnoreCase("nav"))
//			return prodNode.getNavName();
//		if (nameToUse != null && nameToUse.equalsIgnoreCase("glance"))
//			return prodNode.getGlanceName();
//		return prodNode.getFullName();
//	}

	public static String getDisplayName(ProductModel prodNode, String nameToUse) {
		if (prodNode == null)
			return "";
		if (nameToUse == null || nameToUse.equalsIgnoreCase("full"))
			return prodNode.getFullName();
		if (nameToUse != null && nameToUse.equalsIgnoreCase("nav"))
			return prodNode.getNavName();
		if (nameToUse != null && nameToUse.equalsIgnoreCase("glance"))
			return prodNode.getGlanceName();
		return prodNode.getFullName();
	}

	public static double getPrice(ProductModel theProduct) throws JspException {
		List<SkuModel> skus = theProduct.getSkus();
		SkuModel sku = null;
		// remove the unavailable sku's
		for (ListIterator<SkuModel> li = skus.listIterator(); li.hasNext();) {
			sku = li.next();
			if (sku.isUnavailable()) {
				li.remove();
			}
		}

		FDProductInfo productInfo = null;
		// ProductModel.PriceComparator priceComp = new
		// ProductModel.PriceComparator();
		double prodPrice = 0.0;
		if (skus.size() == 0)
			return prodPrice; // skip this item..it has no skus. Hmmm?
		if (skus.size() == 1) {
			sku = (SkuModel) skus.get(0); // we only need one sku
		} else {
			sku = (SkuModel) Collections.min(skus, priceComp);
		}
		if (sku != null && sku.getSkuCode() != null) {
			//
			// get the FDProductInfo from the FDCachedFactory
			//
			try {
				productInfo = FDCachedFactory.getProductInfo(sku.getSkuCode());
				prodPrice = productInfo.getZonePriceInfo(theProduct.getPricingContext().getZoneId()).getDefaultPrice();
			} catch (FDResourceException fdre) {
				LOGGER.warn("FDResourceException occured", fdre);
				throw new JspException(
						"JspMethods.getPrice method caught an FDResourceException");
			} catch (FDSkuNotFoundException fdsnfe) {
				LOGGER.warn("FDSkuNotFoundException occured", fdsnfe);
				throw new JspException(
						"JspMethods.getPrice method caught an FDSkuNotFoundException");
			}
		}
		return prodPrice;
	}
	
	public static String getProductRating(ProductModel theProduct)
		throws JspException {
		return getProductRating(theProduct, null);	
	}

	public static String getProductRating(ProductModel theProduct, String skuCode)
			throws JspException {
		try {
			return theProduct.getProductRating(skuCode);
		} catch (FDResourceException fdre) {
			LOGGER.warn("FDResourceException occured", fdre);
			throw new JspException(
					"JspMethods.getPrice method caught an FDResourceException");
		}
	}
	
	public static String getFreshnessGuaranteed(ProductModel theProduct)
	throws JspException {
		try {
			if(FDStoreProperties.IsFreshnessGuaranteedEnabled()) {
				return theProduct.getFreshnessGuaranteed();
			}
		} catch (FDResourceException fdre) {
			LOGGER.warn("FDResourceException occured", fdre);
			throw new JspException(
			"JspMethods.getFreshnessGuaranteed method caught an FDResourceException");
		}
		return null;
	}

        public static String getAttributeValue(ProductModel theProduct, String domainName) {
            List<DomainValue> ratings = theProduct.getRating();
            return HowToCookItUtil.getProductDomainValue(ratings, domainName, "");
        }
	

	public static class ContentNodeComparator implements Comparator {

		public int compare(Object obj1, Object obj2) {
			if (obj1 instanceof ContentNodeModel
					&& !(obj2 instanceof ContentNodeModel)) {
				return -1;
			} else if (obj2 instanceof ContentNodeModel
					&& !(obj1 instanceof ContentNodeModel)) {
				return 1;
			}
			String name1 = ((ContentNodeModel) obj1).getContentType() + ":"
					+ ((ContentNodeModel) obj1).getFullName();
			String name2 = ((ContentNodeModel) obj2).getContentType() + ":"
					+ ((ContentNodeModel) obj2).getFullName();
			return name1.compareToIgnoreCase(name2);
		}
	}

	public static class DomainNameComparator implements Comparator {

		// handles Domains or DomainRefs, DomainValue
		public int compare(Object obj1, Object obj2) {
			String name1 = null;
			String name2 = null;
			if (obj1 instanceof Domain) {
				name1 = ((Domain) obj1).getName();
			}
			if (obj1 instanceof DomainValue) {
				name1 = ((DomainValue) obj1).getDomain().getName();
			}

			if (obj2 instanceof Domain) {
				name2 = ((Domain) obj1).getName();
			}
			if (obj2 instanceof DomainValue) {
				name2 = ((DomainValue) obj2).getDomain().getName();
			}

			if (name1 == null || name2 == null)
				return 0;
			return name1.compareToIgnoreCase(name2);
		}
	}


	public static Comparator<DomainValue> domainValueComp = new Comparator<DomainValue>() {

		public int compare(DomainValue dv1, DomainValue dv2) {
			return dv1.getValue().compareTo(dv2.getValue());
		}
	};


	public static class MyAttributeComparator implements Comparator<ContentNodeModel> {

		private String attribName = null;
		private boolean reverseOrder = false;
		private boolean includeCategory = true;

		public MyAttributeComparator(String attribName) {
			this.attribName = attribName;
		}

		public void setReverseOrder(boolean flag) {
			this.reverseOrder = flag;
		}

		public void setIncludeCategory(boolean flag) {
			this.includeCategory = flag;
		}

		public int compare(ContentNodeModel cn1, ContentNodeModel cn2) {
			String sortField1 = "";
			String sortField2 = "";
			String attribValue1 = "";
			String attribValue2 = "";
			CategoryModel parentCategory = null;

			if (ContentNodeModel.TYPE_CATEGORY.equals(cn1.getContentType())) {
				sortField1 = leadZeroes(((CategoryModel) cn1).getPriority(), 6)
						+ cn1.getFullName() + ":";
			} else {
				parentCategory = (CategoryModel) ((ProductModel) cn1)
						.getParentNode();
				attribValue1 = cn1.getFullName();
				if (!"name".equalsIgnoreCase(attribName)
						&& !"price".equalsIgnoreCase(attribName)) {
					attribValue1 = getAttributeValue((ProductModel) cn1,
							attribName);
				} else if ("price".equalsIgnoreCase(attribName)) {
					if (((ProductModel) cn1).isUnavailable()) {
						attribValue1 = JspMethods.leadZeroes("9999.99", 8);
					} else {
						try {
							attribValue1 = JspMethods.leadZeroes(JspMethods
									.getPrice((ProductModel) cn1), 8);
						} catch (JspException je) {
							LOGGER.warn("JspException occured", je);
							attribValue1 = "xxxxx";
						}
					}
				}
				if (includeCategory) {
					sortField1 = leadZeroes(parentCategory.getPriority(), 6)
							+ parentCategory.getFullName() + ":";
				} else {
					sortField1 = "";
				}
			}

			if (ContentNodeModel.TYPE_CATEGORY.equals(cn2.getContentType())) {
				sortField2 = JspMethods.leadZeroes(((CategoryModel) cn2)
						.getPriority(), 6)
						+ cn2.getFullName() + ":";
			} else {
				parentCategory = (CategoryModel) ((ProductModel) cn2)
						.getParentNode();
				attribValue2 = cn2.getFullName();
				if (!"name".equalsIgnoreCase(attribName)
						&& !"price".equalsIgnoreCase(attribName)) {
					attribValue2 = getAttributeValue((ProductModel) cn2,
							attribName);
				} else if ("price".equalsIgnoreCase(attribName)) {
					if (((ProductModel) cn2).isUnavailable()) {
						attribValue2 = JspMethods.leadZeroes("9999.99", 8);
					} else {
						try {
							attribValue2 = JspMethods.leadZeroes(JspMethods
									.getPrice((ProductModel) cn2), 8);
						} catch (JspException je) {
							LOGGER.warn("JspException occured", je);
							attribValue2 = "xxxxx";
						}
					}
				}
				if (includeCategory) {
					sortField2 = JspMethods.leadZeroes(parentCategory
							.getPriority(), 6)
							+ parentCategory.getFullName() + ":";
				} else {
					sortField2 = "";
				}
			}
			if ("".equals(attribValue1)) {
				attribValue1 = "zzzzzzzzz";
				if (reverseOrder) {
					attribValue1 = "";
				}
			}
			if ("".equals(attribValue2)) {
				attribValue2 = "zzzzzzzzz";
				if (reverseOrder) {
					attribValue2 = "";
				}
			}

			if (reverseOrder) {
				if (attribValue1.compareTo(attribValue2) != 0) {
					String tmpx = attribValue2;
					attribValue2 = attribValue1;
					attribValue1 = tmpx;
				}
			}
			sortField1 = sortField1 + attribValue1;
			sortField2 = sortField2 + attribValue2;
			return sortField1.compareTo(sortField2);
		}
	}

	public static List sorter(Collection CatsAndProds, String orderBy) {
		return JspMethods.sorter(CatsAndProds, orderBy, true, true);
	}

	public static List sorter(Collection CatsAndProds,
			String orderBy, boolean reverseOrder, boolean includeFolder) {
		// sort the items by folder + (specified-attribute | Name | price)
		List sortedList = new ArrayList(CatsAndProds);
		MyAttributeComparator sortByRatingAttribute = new MyAttributeComparator(
				orderBy);
		sortByRatingAttribute.setReverseOrder(reverseOrder);
		sortByRatingAttribute.setIncludeCategory(includeFolder);
		Collections.sort(sortedList, sortByRatingAttribute);

		return sortedList;
	}

	/* utility method */
	public static void dumpRequest(HttpServletRequest request) {
		Enumeration rpn = request.getParameterNames();
		while (rpn.hasMoreElements()) {
			String paramName = (String) rpn.nextElement();
			String paramValues[] = request.getParameterValues(paramName);
			if (paramValues == null) {
				LOGGER.debug("Param name=" + paramName + ",  Value=[is null]");
			} else if (paramValues.length == 1 && paramValues[0].length() == 0) {
				LOGGER.debug("Param name=" + paramName
						+ ",  Value=[Empty String]");
			} else if (paramValues.length == 1) {
				LOGGER.debug("Param name=" + paramName + ",  Value="
						+ paramValues[0]);
			} else if (paramValues.length > 1) {
				LOGGER.debug("Param name=" + paramName);
				for (int j = 0; j < paramValues.length; j++) {
					LOGGER.debug("   Value #" + j + 1 + "=" + paramValues[j]);
				}
			}
		}
	}


	
	/**
	 * Utility method used in layouts/featured_all.jsp and layouts/how_to_cookit.jspf
	 * Extracted from CatLayoutManager.jspf
	 */
	public static String displayFAProducts(LinkedList productLinks,
			boolean showPrices, boolean listIsUnavailable) {
		return displayFAProducts(productLinks, null, showPrices,
				listIsUnavailable);
	}

	// *******************************************************
	public static String displayFAProducts(LinkedList productLinks,
			LinkedList productPrices, boolean showPrices,
			boolean listIsUnavailable) {
		if (productLinks.size() < 1)
			return "";
		int productItemsToDisplay = productLinks.size();
		int columnCutoff = productItemsToDisplay / 2;
		StringBuffer outputRows = new StringBuffer(2000);
		if (productItemsToDisplay % 2 != 0)
			columnCutoff++; // adjust for odd number of items

		if (showPrices == true) {
			for (int k = 0; k < productItemsToDisplay; k++) {
				if (k != 0) {
					outputRows.append("<tr>");
				} else {
					outputRows
							.append("<td width=\"290\"><table border=\"0\" width=\"290\"><tr>");
					if (listIsUnavailable) {
						outputRows
								.append("<td  width=\"260\"><font color=\"#999999\"><b>Currently Unavailable<B></font></td><tr>");
					}
				}
				outputRows.append("<TD width=\"290\">");
				outputRows.append(productLinks.get(k));

				outputRows.append("</td><TD width=\"5\">");
				if (k == 0) { // Since this cell is in the first row use the
								// image tag to set the spacing
					outputRows.append("<IMG SRC=\"");
					outputRows.append("/media_stat/images/layout/clear.gif");
					outputRows
							.append("\" ALT=\"\" WIDTH=\"5\" HEIGHT=\"1\" BORDER=\"0\">");
				} else {
					outputRows.append("&nbsp;");
				}
				outputRows
						.append("</td><TD width=\"70\" class=\"text11bold\">");
				if (productPrices != null) {
					outputRows.append(productPrices.get(k));
				}
				outputRows.append("&nbsp;");

				outputRows.append("</td></tr>");
			}
			outputRows.append("</table></td>");
		} else {
			for (int k = 0; k < columnCutoff; k++) {
				if (k != 0) {
					outputRows.append("<tr>");
				} else {
					outputRows
							.append("<td width=\"290\"><table width=\"290\"><tr>");
					if (listIsUnavailable) {
						outputRows
								.append("<td colspan=\"3\" width=\"260\"><br><font color=\"#999999\"><b>Currently Unavailable</b></font></td><tr>");
					}
				}
				outputRows.append("<TD valign=\"top\" width=\"140\">");
				outputRows.append(productLinks.get(k));
				outputRows.append("</td><TD width=\"10\">");
				if (k == 0) { // Since this cell is in the first row use the
								// image tag to set the spacing
					outputRows.append("<IMG SRC=\"");
					outputRows.append("/media_stat/images/layout/clear.gif");
					outputRows
							.append("\" ALT=\"\" WIDTH=\"10\" HEIGHT=\"1\" BORDER=\"0\">");
				} else {
					outputRows.append("&nbsp;");
				}
				outputRows.append("</td><TD valign=\"top\" width=\"140\">");
				if ((columnCutoff + k) < productLinks.size()) {
					outputRows.append(productLinks.get(columnCutoff + k));
				} else {
					outputRows.append("&nbsp;");
				}
				outputRows.append("</td></tr>");
			}
			outputRows.append("</table></td>");
		}

		return outputRows.toString();
	}



	public static void dumpErrors(ActionResult result) {
		if (!result.isSuccess()) {
			for (ActionError err : result.getErrors()) {
				System.out.println(err.getDescription());
			}
		}
	}

	public static DisplayObject loadLayoutDisplayStrings(
			HttpServletResponse response, String categoryId,
			ContentNodeModel displayThing, String productNameAttribute)
			throws JspException {
		return loadLayoutDisplayStrings(response, categoryId, displayThing,
				productNameAttribute, true, false, null, false);
	}

	public static DisplayObject loadLayoutDisplayStrings(
			HttpServletResponse response, String categoryId,
			ContentNodeModel displayThing, String productNameAttribute,
			boolean showPrice) throws JspException {
		return loadLayoutDisplayStrings(response, categoryId, displayThing,
				productNameAttribute, showPrice, false, null, false);
	}

	public static DisplayObject loadLayoutDisplayStrings(
			HttpServletResponse response, String categoryId,
			ContentNodeModel displayThing, String productNameAttribute,
			boolean showPrice, boolean gotoPrimaryHome, String trkCode)
			throws JspException {
		return loadLayoutDisplayStrings(response, categoryId, displayThing,
				productNameAttribute, showPrice, gotoPrimaryHome, trkCode,
				false);
	}

	public static DisplayObject loadLayoutDisplayStrings(
			HttpServletResponse response, String categoryId,
			ContentNodeModel displayThing, String productNameAttribute,
			boolean showPrice, boolean gotoPrimaryHome, String trkCode,
			boolean useAltImage) throws JspException {
		// String itemImage = "";
		DisplayObject displayObj = new DisplayObject();
		String trackingCode = (trkCode == null || "".equals(trkCode.trim())) ? ""
				: "&trk=" + trkCode;

		String itemName = "";
		int imageWidth = 0;
		int imageHeight = 0;
		String itemURL = "";
		String itemAltText = "";
		String rolloverImage = "";
		StringBuffer indicators = new StringBuffer(300);
		StringBuffer rolloverText = new StringBuffer(300);

		// *** load the variables with the appropriate stuff for use by
		// Hoizontal & generic
		String organicIndicator = "/media_stat/images/template/icon_organic.gif";
		String inSeasonIndicator = "/media_stat/images/template/icon_in_season.gif";
		String imagePath = null;
		ContentNodeModel displayFolder = null;
		ProductModel displayProduct = null;
		String imgName = "ro_img_" + displayThing.getContentName();
		rolloverText.setLength(0);
		rolloverImage = "";
		Image itemImage = null;
		indicators.setLength(0);
		SkuModel dfltSku = null;
		if (displayThing.getContentType().equals(ContentNodeModel.TYPE_PRODUCT)) {
			displayProduct = (ProductModel) displayThing;
			PriceCalculator priceCalculator = displayProduct.getPriceCalculator();
			dfltSku = priceCalculator.getSkuModel();
			String thisProdBrandLabel = null;
			try {
				if (dfltSku != null && showPrice) {
					// pi.getAttribute(EnumAttributeName.PRICING_UNIT_DESCRIPTION.getName(),
					// pi.getDefaultPriceUnit().toLowerCase())
					displayObj.setPrice(priceCalculator.getPriceFormatted(0));
					String salesUnitDescr = priceCalculator.getProduct().getSalesUnits()[0].getDescription();
					if (!"nm".equalsIgnoreCase(salesUnitDescr)
							&& !"ea".equalsIgnoreCase(salesUnitDescr)
							&& !"".equalsIgnoreCase(salesUnitDescr)) {
						displayObj.setSalesUnitDescription(salesUnitDescr);
					} else {
						displayObj.setSalesUnitDescription("");
					}

					/* Display Sales Units price-Apple Pricing[AppDev-209] */
					displayObj
							.setDisplaySalesUnitPrice(priceCalculator.getAboutPriceFormatted(0));

				}
				thisProdBrandLabel = displayProduct.getPrimaryBrandName();
			} catch (FDResourceException fde) {
				throw new JspException(fde);
			} catch (FDSkuNotFoundException sknf) {
				throw new JspException(sknf);
			}
			// get the produce Rating

			displayObj.setRating(getProductRating(displayProduct));

			if (useAltImage) {
				itemImage = displayProduct.getAlternateImage();
			}
			if (itemImage == null) {
				itemImage = displayProduct.getCategoryImage();// displayProduct.getContent("ATR_image_product").getString("url");
			}
			imagePath = itemImage.getPath();
			StringBuffer tmpName = new StringBuffer();
			if ("nav".equalsIgnoreCase(productNameAttribute)) {
				tmpName.append(displayProduct.getNavName());
			} else if ("glance".equalsIgnoreCase(productNameAttribute)) {
				tmpName.append(displayProduct.getGlanceName());
			} else {
				if (thisProdBrandLabel != null
						&& thisProdBrandLabel.length() > 0
						&& displayProduct.getFullName().startsWith(
								thisProdBrandLabel)) {
					tmpName.append("<FONT CLASS=\"text10bold\">");
					tmpName.append(thisProdBrandLabel);
					tmpName.append("</font><BR>");
					tmpName.append(displayProduct.getFullName().substring(
							thisProdBrandLabel.length()).trim());
				} else {
					tmpName.append(displayProduct.getFullName());
				}
			}

			itemName = tmpName.toString();
			// itemName
			// =displayProduct.getString(productNameAttribute,"no"+productNameAttribute);
			imageWidth = itemImage.getWidth();// displayProduct.getContent("ATR_image_product").getString("ATR_image_width");
			imageHeight = itemImage.getHeight();// displayProduct.getContent("ATR_image_product").getString("ATR_image_height");
			itemAltText = displayProduct.getFullName();// getString("ATR_full_name",
														// itemName);
			if (gotoPrimaryHome) {
				CategoryModel primaryHome = displayProduct.getPrimaryHome();
				if (primaryHome != null) {
					itemURL = response.encodeURL("product.jsp?productId="
							+ displayProduct + "&catId=" + primaryHome.getContentKey().getId()
							+ trackingCode);
				}
			} else {
				itemURL = response.encodeURL("product.jsp?productId="
						+ displayProduct + "&catId=" + categoryId
						+ trackingCode);
			}
			{ 
			    Image dispRolloverImage = displayProduct.getRolloverImage();
			
			    rolloverImage = dispRolloverImage == null ? "" : dispRolloverImage.getPath();
			}
			if (!rolloverImage.equals("")) {
				rolloverText.append("onMouseover='");
				rolloverText.append("swapImage(\"" + imgName + "\",\""
						+ rolloverImage + "\")");
				// rolloverText.append(rolloverImage);
				rolloverText.append(";return true;'  onMouseout='");
				rolloverText.append("swapImage(\"" + imgName + "\",\""
						+ imagePath + "\")");
				rolloverText.append(";return true;'");
			}

			indicators.append("&nbsp;");

			indicators.append("&nbsp;");
		} else if (ContentNodeModel.TYPE_CATEGORY.equals(displayThing
				.getContentType())) {
		        CategoryModel displayCategory = (CategoryModel) displayThing; 
			displayFolder = (CategoryModel) displayThing;
			itemImage = displayCategory.getCategoryPhoto();
			imagePath = itemImage == null ? "" : itemImage.getPath();// getContent("ATR_image_category_photo").getString("url");
			itemName = displayFolder.getFullName();
			imageWidth = itemImage == null ? 0 : itemImage.getWidth(); // displayFolder.getContent("ATR_image_category_photo").getString("ATR_image_width");
			imageHeight = itemImage == null ? 0 : itemImage.getHeight(); // displayFolder.getContent("ATR_image_category_photo").getString("ATR_image_height");
			itemAltText = displayFolder.getAltText(); // (getString("ATR_alt_text",
														// itemName);
			itemURL = response.encodeURL("/category.jsp?catId=" + displayFolder
					+ trackingCode);
			ContentNodeModel alias = displayCategory.getAlias();
			if (alias != null) {
				if (ContentNodeModel.TYPE_CATEGORY.equals(alias.getContentType())) {
					itemURL = response.encodeURL("/category.jsp?catId="
							+ alias.getContentKey().getId() + trackingCode);
				} else if (ContentNodeModel.TYPE_DEPARTMENT.equals(alias.getContentType())) {
					itemURL = response.encodeURL("/department.jsp?catId="
							+ alias.getContentKey().getId() + trackingCode);
				}
			}

			indicators.append("&nbsp;");
		} else if (displayThing.getContentType().equals(
		            ContentNodeModel.TYPE_DEPARTMENT)) {
			displayFolder = displayThing;
			DepartmentModel department = (DepartmentModel) displayThing;
			itemImage = department.getPhoto();
			imagePath = itemImage == null ? "" : itemImage.getPath();
			itemName = displayFolder.getFullName();
			imageWidth = itemImage == null ? 0 : itemImage.getWidth();
			imageHeight = itemImage == null ? 0 : itemImage.getHeight();
			itemAltText = displayFolder.getAltText();
			itemURL = response.encodeURL("category.jsp?catId=" + displayFolder
					+ trkCode);
			indicators.append("&nbsp;");
		}
		displayObj.setItemName(itemName);
		displayObj.setItemURL(itemURL);
		displayObj.setImageName(imgName);
		displayObj.setImagePath(imagePath);
		displayObj.setImageWidth(imageWidth + "");
		displayObj.setImageHeight(imageHeight + "");
		displayObj.setAltText(itemAltText);
		displayObj.setRolloverString(rolloverText.toString());
		displayObj.setIndicators(indicators.toString());
		return displayObj;

	}
	
	/**
	 * This is here for marker, some jsp-s still call it, but the meaning of the 
	 * original function is questionable.
	 * 
	 * @param obj
	 * @return
	 */
	public static ProductModel getFeaturedProduct(Object obj) {
	    if (obj instanceof ProductModel) {
	        return (ProductModel) obj;
	    }
	    return null;
	}
    private final static String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    public static String safeJavaScriptVariable(String s) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (ALLOWED_CHARS.indexOf(s.charAt(i)) < 0) {
                buf.append('_');
                buf.append(Integer.toHexString(s.charAt(i)));
            } else {
                buf.append(s.charAt(i));
            }
        }
        return buf.toString();
    }

	public static java.text.NumberFormat currencyFormatter = java.text.NumberFormat
			.getCurrencyInstance(Locale.US);

	public static String removeChars(String source, String charsToRemove) {
		if (source == null || charsToRemove == null || source.length() == 0
				|| charsToRemove.length() == 0)
			return source;
		String uSource = source.toUpperCase();
		String uctr = charsToRemove.toUpperCase();
		StringBuffer workBuffer = new StringBuffer();
		for (int idx = 0; idx < uSource.length(); idx++) {
			char[] chr = { uSource.charAt(idx) };
			if (uctr.indexOf(new String(chr)) == -1) {
				workBuffer.append(chr);
			}
		}
		if (workBuffer.length() != 0) {
			return workBuffer.toString();
		}
		return "";
	}

	/**
	 * Apple Pricing - APPDEV-209. Utility method to calculate the price/lb
	 * based on the base price and weight, for display.
	 * 
	 * @param node -
	 *            ContentNodeI
	 * @return
	 */
	public static String getAboutPriceForDisplay(ContentNodeModel node, PricingContext pricingContext)
	throws JspException {
		if (node.getContentType().equals(ContentNodeModel.TYPE_PRODUCT)) {
			ProductModel product = (ProductModel) node;
			PriceCalculator priceCalculator = new PriceCalculator(pricingContext, product);
			String str = priceCalculator.getAboutPriceFormatted(0);
			return str != null ? str : "";
		}
		return "";
	}

	/**
	* Apple Pricing - APPDEV-209. This method will be used to display the about
	* weight and price/lb in product with single sku and multiple sku pages.
	* 
	* @param prodInfo
	* @return
	* @throws JspException
	*/
	public static String getAboutPriceAndWeightForDisplay(FDProductInfo prodInfo, PricingContext pricingContext)
		throws JspException {
		String displayPriceString = "";
		try {
			FDProduct fdProduct = FDCachedFactory.getProduct(prodInfo);
			if (null != fdProduct.getDisplaySalesUnits()
					&& fdProduct.getDisplaySalesUnits().length > 0) {
				FDSalesUnit fdSalesUnit = fdProduct.getDisplaySalesUnits()[0];
				double salesUnitRatio = (double) fdSalesUnit.getDenominator()
						/ (double) fdSalesUnit.getNumerator();
				String alternateUnit = fdSalesUnit.getName();
				double displayPrice = prodInfo.getZonePriceInfo(pricingContext.getZoneId()).getDefaultPrice()
						/ salesUnitRatio;
				if (displayPrice > 0) {
					displayPriceString = "about " + salesUnitRatio
							+ alternateUnit.toLowerCase() + ", "
							+ JspMethods.currencyFormatter.format(displayPrice)
							+ "/" + alternateUnit.toLowerCase();
				}
			}
		} catch (FDResourceException fdre) {
			throw new JspException(fdre);
		} catch (FDSkuNotFoundException fdse) {
			throw new JspException(fdse);
		}
		return displayPriceString;
	}

	/**
	 * For truncating the decimal number to 2 digits.
	 * 
	 * @param number
	 * @return
	 */
	public static double formatDecimal(double number) {
		DecimalFormat decimalFormat = new DecimalFormat("0.##");
		String strNumber = decimalFormat.format(number);
		strNumber = strNumber.replaceAll(",", ".");
		Double numberDouble = new Double(strNumber);
		return numberDouble.doubleValue();
	}
	
	
	public static class CategoryInfo {
	    CategoryModel category;
	    String catId;
	    String fldrLbl;
	    String navBar;
	    String link;
            CategoryModel workingCategory;
            public CategoryModel getCategory() {
                return category;
            }
            public String getCatId() {
                return catId;
            }
            public String getFldrLbl() {
                return fldrLbl;
            }
            public String getNavBar() {
                return navBar;
            }
            public String getLink() {
                return link;
            }
            public CategoryModel getWorkingCategory() {
                return workingCategory;
            }
            
	}
	
    public static CategoryInfo getCategoryInfo(HttpServletRequest request) {
        CategoryInfo c = new CategoryInfo();
        c.catId = request.getParameter("catId");
        c.fldrLbl = "/media_stat/images/layout/clear.gif";
        c.navBar = "/media_stat/images/layout/clear.gif";

        c.link = "#";

        if (c.catId != null) {
            ContentNodeModel tmplCat = ContentFactory.getInstance().getContentNode(c.catId);
            c.category = (tmplCat instanceof CategoryModel) ? (CategoryModel) tmplCat : null;
            CategoryModel wokingCat = c.category != null ? c.category.getAliasCategory() : null;
            c.workingCategory = wokingCat != null ? wokingCat : c.category;

            if (wokingCat != null && ContentNodeModel.TYPE_CATEGORY.equals(wokingCat.getContentType())) {
                Image categoryLabel = wokingCat.getCategoryLabel();
                if (categoryLabel != null) {
                    c.fldrLbl = categoryLabel.getPath();
                    c.link = "/category.jsp?catId=" + tmplCat;
                }
                MediaI navbar = wokingCat.getCategoryNavBar();
                if (navbar != null) {
                    c.navBar = navbar.getPath();
                }

            }
        }
        return c;
    }
	
}
