package com.freshdirect.webapp.taglib.fdstore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;

import com.freshdirect.common.pricing.CharacteristicValuePrice;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.Pricing;
import com.freshdirect.common.pricing.SalesUnitRatio;
import com.freshdirect.common.pricing.util.GroupScaleUtil;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.util.ProductImpression;
import com.freshdirect.webapp.util.TransactionalProductImpression;

/**
 * This tag generates client-side scripts to support pricing controls Used in
 * DYF and YMAL transactional displays
 * 
 * Before using this tag you have to include script
 * /assets/javascript/pricing.js unconditionally in the HTML
 * 
 * 
 * 
 * @author segabor
 * 
 */
public class TxSingleProductPricingSupportTag extends BodyTagSupport {
	private static final long serialVersionUID = 2149022868536471972L;

	static final DecimalFormat QUANTITY_FORMATTER = new DecimalFormat("0.##");

	// JavaScript namespace to avoid conflicts with another pricing scripts
	private String namespace;

	private String formName;

	private String statusPlaceholder;
	
	private String subTotalPlaceholderId;
	
	private String couponStatusPlaceholderId;

	// INPUT Product impressions
	private ProductImpression impression;

	private FDUserI customer;

	public void setCustomer(FDUserI customer) {
		this.customer = customer;
	}

	// [required]
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	// [required]
	public void setImpression(ProductImpression impression) {
		this.impression = impression;
	}

	// [optional]
	public void setFormName(String formName) {
		this.formName = formName;
	}

	// [optional]
	public void setStatusPlaceholder(String statusPlaceholder) {
		this.statusPlaceholder = statusPlaceholder;
	}

	// [optional]
	public void setCouponStatusPlaceholderId(String couponStatusPlaceholderId) {
		this.couponStatusPlaceholderId = couponStatusPlaceholderId;
	}
	
	public String getCouponStatusPlaceholderId() {
		return couponStatusPlaceholderId;
	}
	
	public void setSubTotalPlaceholderId(String subTotalPlaceholderId) {
		this.subTotalPlaceholderId = subTotalPlaceholderId;
	}
	
	public String getSubTotalPlaceholderId() {
		return subTotalPlaceholderId;
	}
	
	

	/**
	 * Main entry point
	 */
	@Override
	public int doStartTag() throws JspException {
		String content = doIncludePricing();

		if (content != null) {
			try {
				pageContext.getOut().print(content);
			} catch (Exception e) {
			}
		}

		return EVAL_BODY_INCLUDE;
	}
	
	public String getContent() throws JspException {
		String content = doIncludePricing();
		
		if (content != null) {
			try {
				return content;
			} catch (Exception e) {
			}
		}
		
		return "";
	}

	private String doIncludePricing() throws JspException {
		StringBuffer buf = new StringBuffer();

		if (impression != null) {
			appendStatusUpdater(buf, namespace, statusPlaceholder, couponStatusPlaceholderId);
			if (impression.isTransactional()) {
				appendMaterialScripts(buf, impression, customer);
				if (namespace != null && formName != null)
					appendConfiguratorScript(buf, customer, (TransactionalProductImpression) impression, namespace, formName,
							statusPlaceholder, subTotalPlaceholderId, couponStatusPlaceholderId);
				else
					throw new JspException("namespace and formName parameters required");
			} else {

			}
		} else {
			throw new JspException("impression parameter required");
		}

		return buf.toString();
	}

	private void appendStatusUpdater(StringBuffer buf, String namespace, String statusPlaceholder, String couponStatusPlaceholderId) {
		buf.append("<script type=\"text/javascript\">\n");
		buf.append("if (!" + namespace + ")\n");
		buf.append("	var " + namespace + " = new Object();\n");
		buf.append("\n");
		buf.append(namespace + ".updateStatus = function(msg) {\n");
		if (statusPlaceholder != null) {
			buf.append("	var status = document.getElementById('" + statusPlaceholder + "');\n");
			buf.append("	var oldTxt;\n");
			buf.append("	if (status != null && FDSearch['statusUpdater']) {\n");
			buf.append("		FDSearch.statusUpdater(status,msg)\n");
			buf.append("	}\n");
		}
		buf.append("};\n");
		buf.append(namespace + ".updateCouponStatus = function(msg) {\n");
		if (couponStatusPlaceholderId != null) {
			buf.append("	var couponStatusElem = document.getElementById('" + couponStatusPlaceholderId + "');\n");
			buf.append("	var oldTxt;\n");
			buf.append("	if (couponStatusElem != null && FDSearch['couponStatusUpdater']) {\n");
			buf.append("		FDSearch.couponStatusUpdater(couponStatusElem, msg)\n");
			buf.append("	}\n");
		}
		buf.append("};\n");
		buf.append("</script>\n");
	}

	/**
	 * PART I Generate pricing basics
	 * 
	 * @param buf
	 */
	private static void appendMaterialScripts(StringBuffer buf, ProductImpression impression, FDUserI customer) {
		FDProductInfo productInfo = impression.getProductInfo();
		if (productInfo == null)
			return;

		FDProduct product = impression.getFDProduct();
		if (product == null)
			return;

		buf.append("<script type=\"text/javascript\">\n");

		// init vars
		buf.append("if (!document.materialPricesArray) {\n");
		buf.append("  document.materialPricesArray = new Array();\n");
		buf.append("}\n");
		buf.append("if (!document.cvPricesArray) {\n");
		buf.append("  document.cvPricesArray = new Array();\n");
		buf.append("}\n");
		buf.append("if (!document.salesUnitRatiosArray) {\n");
		buf.append("  document.salesUnitRatiosArray = new Array();\n");
		buf.append("}\n");

		appendMaterialScriptForProduct(buf, customer, productInfo, product);

		buf.append("</script>\n");
	}

	private static void appendMaterialScriptForProduct(StringBuffer buf, FDUserI customer, FDProductInfo productInfo,
			FDProduct product) {
		Pricing pricing = product.getPricing();
		MaterialPrice[] availMatPrices = pricing.getZonePrice(customer.getUserContext().getPricingContext().getZoneInfo()).getMaterialPrices();
		MaterialPrice[] matPrices = null;
		List<MaterialPrice> matPriceList = new ArrayList<MaterialPrice>();
		String salesOrg=customer.getUserContext().getPricingContext().getZoneInfo().getSalesOrg();
		String distributionChannel=customer.getUserContext().getPricingContext().getZoneInfo().getDistributionChanel();
		if (productInfo.isGroupExists(salesOrg,distributionChannel)) {
			// Has a Group Scale associated with it. Check if there is GS price
			// defined for
			// current pricing zone.
			FDGroup group = productInfo.getGroup(salesOrg,distributionChannel);
			MaterialPrice[] grpPrices = null;
			try {
				grpPrices = GroupScaleUtil.getGroupScalePrices(group, customer.getUserContext().getPricingContext().getZoneInfo());
			} catch (FDResourceException fe) {
				// Never mind. Show regular price for the material.
			}
			if (grpPrices != null) {
				// Group scale price applicable to this material. So modify
				// material prices array
				// to accomodate GS price.
				MaterialPrice regularPrice = availMatPrices[0];// Get the
				// regular
				// price/single
				// unit price
				// first.

				if (grpPrices != null && grpPrices.length > 0) {
					// Get the first group scale price and set the lower bound
					// to be upper bound of regular price.
					MaterialPrice newRegularPrice = new MaterialPrice(regularPrice.getPrice(), regularPrice.getPricingUnit(),
							regularPrice.getScaleLowerBound(), grpPrices[0].getScaleLowerBound(), grpPrices[0].getScaleUnit(),
							regularPrice.getPromoPrice());
					// Add the modified regular price.
					matPriceList.add(newRegularPrice);
					// Add the remaining group scale prices.
					for (int i = 0; i < grpPrices.length; i++) {
						matPriceList.add(grpPrices[i]);
					}
					matPrices = (MaterialPrice[]) matPriceList.toArray(new MaterialPrice[0]);
				}
			}

		}
		if (matPrices == null) {
			// Set the default prices defined for the material.
			matPrices = availMatPrices;
		}
		/*
		 * MaterialPrice[] matPrices = null;
		 * if(productInfo.getSkuCode().equals("FRU0005343")){ matPrices = new
		 * MaterialPrice[] { new MaterialPrice(matPrices1[0].getPrice(),
		 * matPrices1[0].getPricingUnit(), 0.0,
		 * 2,matPrices1[0].getPricingUnit(), matPrices1[0].getPromoPrice()), new
		 * MaterialPrice(2.50, "EA", 2, 4, "EA", 0.0), new MaterialPrice(2.00,
		 * "EA", 4, Double.POSITIVE_INFINITY, "EA", 0.0) }; }
		 */
		CharacteristicValuePrice[] cvPrices = pricing.getCharacteristicValuePrices(customer.getUserContext().getPricingContext());
		SalesUnitRatio[] suRatios = pricing.getSalesUnitRatios();

		// material prices array
		if (matPrices.length > 0) {
			buf.append("if (document.materialPricesArray['" + productInfo.getSkuCode() + "'] == null) {\n");
			buf.append("  document.materialPricesArray['" + productInfo.getSkuCode() + "'] = new Array();\n");

			for (int j = 0; j < matPrices.length; j++) {
				buf.append("  document.materialPricesArray['"
						+ productInfo.getSkuCode()
						+ "']["
						+ j
						+ "] = new MaterialPrice("
						+ matPrices[j].getPrice()
						+ ", '"
						+ matPrices[j].getPricingUnit()
						+ "', "
						+ matPrices[j].getScaleLowerBound()
						+ ", "
						+ (Double.isInfinite(matPrices[j].getScaleUpperBound()) ? "Number.POSITIVE_INFINITY" : matPrices[j]
								.getScaleUpperBound()) + ", '" + matPrices[j].getScaleUnit() + "');\n");
			}

			buf.append("}\n");
		}

		// characteristic value prices
		if (cvPrices.length > 0) {
			buf.append("if (!document.cvPricesArray['" + productInfo.getSkuCode() + "']) {\n");
			buf.append("  document.cvPricesArray['" + productInfo.getSkuCode() + "'] = new Array();\n");

			for (int j = 0; j < cvPrices.length; j++) {
				buf.append("  document.cvPricesArray['" + productInfo.getSkuCode() + "'][" + j + "] = new CharValuePrice('"
						+ cvPrices[j].getCharacteristicName() + "', '" + cvPrices[j].getCharValueName() + "', "
						+ cvPrices[j].getPrice() + ", '" + cvPrices[j].getPricingUnit() + "', " + cvPrices[j].getApplyHow()
						+ ");\n");
			}

			buf.append("}\n");
		}

		// sales unit ratios
		if (suRatios.length > 0) {
			buf.append("if (!document.salesUnitRatiosArray['" + productInfo.getSkuCode() + "']) {\n");
			buf.append("  document.salesUnitRatiosArray['" + productInfo.getSkuCode() + "'] = new Array();\n");

			for (int j = 0; j < suRatios.length; j++) {
				buf.append("  document.salesUnitRatiosArray['" + productInfo.getSkuCode() + "'][" + j + "] = new SalesUnitRatio('"
						+ suRatios[j].getAlternateUnit() + "', '" + suRatios[j].getSalesUnit() + "', " + suRatios[j].getRatio()
						+ ");\n");
			}

			buf.append("}\n");
		}

	}

	/**
	 * PART II Configure pricing script
	 * 
	 * @param buf
	 */
	private static void appendConfiguratorScript(StringBuffer buf, FDUserI customer, TransactionalProductImpression impression,
			String namespace, String formName, String statusPlaceholder, String subTotalPlaceholderId, String couponStatusPlaceholderId) {

		buf.append("<script type=\"text/javascript\">\n");
		buf.append("\n");
		buf.append("  " + namespace + ".instantATC = function() {\n");
		buf.append("  	var query = extract_query_string(document." + formName + ");\n");
		buf.append("  	var url = '/quickbuy/ajax_add_to_cart.jsp';\n");
		buf.append("  	var callback = {\n");
		buf.append("  		success: function(o) {\n");
		
		buf.append("  			var respJSON = {};\n");
		buf.append("  			try {\n");
		buf.append("  				respJSON = YAHOO.lang.JSON.parse(o.responseText);\n");
		buf.append("  			}catch(e) {\n");
		//buf.append("  				console.log('error in txSingleProductPricingSupportTag');\n");
		buf.append("  			}\n\n");
		//buf.append("  			console.log('called success in txSingleProductPricingSupportTag');\n");
		//buf.append("  			console.log(respJSON);\n");
		
		buf.append("  			" + namespace + ".updateStatus(respJSON.statusHtml);\n");
		buf.append("  			" + namespace + ".updateCouponStatus(respJSON.couponStatusHtml);\n");
		buf.append("  			updateYourCartPanel();\n");
		buf.append("  			fdCoremetrics.trackAddToCartEvent();\n");
		buf.append("  		},\n");
		buf.append("  		failure: function(o) {\n");
		buf.append("  			" + namespace + ".updateStatus('Connection error');\n");
		buf.append("  		},\n");
		buf.append("  		argument: []\n");
		buf.append("  	};\n");
        buf.append("    clearCouponStatusATC(null, '" + couponStatusPlaceholderId + "');\n");
		buf.append("  	YAHOO.util.Connect.asyncRequest('POST', url, callback, query);\n");
		buf.append("  	return false;\n");
		buf.append("  };\n");			
		buf.append("\n");
		buf.append("  " + namespace + ".useGroupScalePricing = false;\n");
		buf.append("  " + namespace + ".pricings = new Array(1);\n");
		buf.append("  " + namespace + ".updateTotal = function() {\n");
		buf.append("    var total=0;\n");
		buf.append("    var totalQty=0;\n");
		buf.append("    var p;\n");
		buf.append("    var selectedSkuCode = '';\n");
		buf.append("    for(i=0; i<" + namespace + ".pricings.length; i++ ) {\n");
		buf.append("      if (" + namespace + ".pricings[i] == undefined)\n");
		buf.append("        continue;\n");
		buf.append("\n");
		buf.append("      if (" + namespace + ".useGroupScalePricing) { \n");
		buf.append("        p = " + namespace + ".pricings[i].getPrice();\n");
		buf.append("        if (p!=\"\") {\n");
		buf.append("           totalQty+=Number(" + namespace + ".pricings[i].getQuantity());\n");
		buf.append("        }\n");
		buf.append("      } else { \n");
		buf.append("        p = " + namespace + ".pricings[i].getPrice();\n");
		buf.append("        if (p!=\"\") {\n");
		buf.append("           total+=Number(p.substring(1));\n");
		buf.append("        }\n");
		buf.append("      }\n");
		buf.append("    }\n");
		buf.append("\n");
		buf.append("    for(i=0; i<" + namespace + ".pricings.length; i++ ) {\n");
		buf.append("      if (" + namespace + ".pricings[i] == undefined)\n");
		buf.append("        continue;\n");
		buf.append("\n");
		buf.append("      if (" + namespace + ".useGroupScalePricing) { selectedSkuCode = " + namespace + ".pricings[i].selectedSku; }\n");
		buf.append("\n");
		buf.append("      if (selectedSkuCode!='') {\n");

		buf.append("      	for (var j=0; j<document.materialPricesArray[selectedSkuCode].length; j++) {\n");
		buf.append("        	if ( isWithinBounds(totalQty, document.materialPricesArray[selectedSkuCode][j]) == true ){\n");
		buf.append("          		if (hasScales(selectedSkuCode, j) == true) {\n");
		buf.append("						var price = (calculateGroupScalePrice(selectedSkuCode, totalQty, Number(" + namespace
				+ ".pricings[i].getQuantity())," + namespace + ".pricings[i].salesUnit, j)).price;\n");
		buf.append("            		total += price;\n");
		buf.append("            		break;\n");
		buf.append("          		}\n");
		buf.append("        	}\n");
		buf.append("      	}\n");
		buf.append("      }\n");
		buf.append("    }\n");
		buf.append("\n");
		if(subTotalPlaceholderId != null && subTotalPlaceholderId.length() > 0) {
			buf.append("	var totalWrapper = document.getElementById('"+subTotalPlaceholderId+"');\n");
			buf.append("	var totalField = document.getElementById('"+subTotalPlaceholderId+"_value');\n");
			buf.append("\n");
			buf.append("    if(totalField) {\n");
			buf.append("      var formattedTotal =\"$\"+currencyFormat(total);\n");
			buf.append("      if(this.getEstimatedQuantity()!='') { formattedTotal+=\" est.\"} \n");
			buf.append("      totalField.innerHTML=formattedTotal;\n");
			buf.append("    }\n");		
			buf.append("    if(totalWrapper && totalWrapper.className.indexOf('subtotal-updated')==-1 ) {\n");
			buf.append("      totalWrapper.className+=\" subtotal-updated\";\n");
			buf.append("    }\n");		
		}
		buf.append("\n");
		buf.append("  };\n");
		buf.append("\n");
		buf.append("  " + namespace + ".changeShopQty = function(idx, delta, min, max, increment, qty_inp) {\n");
		buf.append("    var val = qty_inp.value;\n");
		buf.append(" \n");
		buf.append("    var qty = parseFloat(val) + delta;\n");
		buf.append("    var round = true;\n");
		buf.append("    if ((delta > 0) && (val == \"\")) {\n");
		buf.append("        if (delta < min) {\n");
		buf.append("          delta = min;\n");
		buf.append("        }\n");
		buf.append("        qty = delta;\n");
		buf.append("    } else if (isNaN(qty) || (qty < 0)) {\n");
		buf.append("      qty = 0;\n");
		buf.append("      round = false;\n");
		buf.append("    } else if ((qty >= 0) && (qty < min) && (delta < 0)) {\n");
		buf.append("      qty = min;\n");
		buf.append("      round = false;\n");
		buf.append("    } else if ((qty > 0) && (qty < min) && (delta >= 0)) {\n");
		buf.append("      qty = min;\n");
		buf.append("    } else if (qty >= max) {\n");
		buf.append("      qty = max;\n");
		buf.append("    }\n");
		buf.append("    if (round) {\n");
		buf.append("      qty = Math.floor( (qty-min)/increment )*increment + min;\n");
		buf.append("    }\n");
		buf.append("\n");
		buf.append("\n");

		buf.append("    // update 'total' (per item) form field\n");
		buf.append("    if (qty_inp != null)\n");
		buf.append("	  qty_inp.value = qty;\n");
		buf.append("\n");
		buf.append("    " + namespace + ".pricings[idx].setQuantity(qty);\n");
		buf.append("  };\n");
		buf.append("\n");

		buf.append("</script>\n");

		// PART III. --

		ProductModel product = impression.getProductModel();
		SkuModel sku = impression.getSku();
		int j = 0; // it was converted from multiple ATC feature so there'll be one item with 0 index

		buf.append("<script type=\"text/javascript\">\n");

		buf.append(namespace + ".pricings[" + j + "] = new Pricing();\n");
		buf.append(namespace + ".pricings[" + j + "].setSKU(\"" + sku + "\");\n");
		if (product.isSoldBySalesUnits()) {
			buf.append(namespace + ".pricings[" + j + "].setQuantity(" + impression.getConfiguration().getQuantity() + ");\n");
			buf.append(namespace + ".pricings[" + j + "].setSalesUnit(\"\");\n");
		} else {
			buf.append(namespace + ".pricings[" + j + "].setQuantity(0);\n");
			buf.append(namespace + ".pricings[" + j + "].setSalesUnit(\"" + impression.getConfiguration().getSalesUnit() + "\");\n");
		}
		buf.append( namespace + ".pricings["+j+"].minQty = "+((impression.getConfiguration()!=null) ? Double.toString(impression.getConfiguration().getQuantity()) : 0)+";\n");

		// options
		for (Map.Entry<String, String> entry : impression.getConfiguration().getOptions().entrySet()) {
			buf.append(namespace + ".pricings[" + j + "].setOption(\"" + entry.getKey() + "\", \"" + entry.getValue() + "\");\n");
		}

		// change quantity function
		buf.append(namespace + ".pricings[" + j + "].changeQty = function(delta) {\n");
		buf.append("  " + namespace + ".changeShopQty(" + j + ", delta, " + product.getQuantityMinimum() + ", "
				+ customer.getQuantityMaximum(product) + ", " + product.getQuantityIncrement() + ", document.forms['" + formName
				+ "']['quantity_" + j + "']);\n");
		buf.append("};\n");
		
		

		// bind updater function
		buf.append(namespace + ".pricings[" + j + "].setCallbackFunction( " + namespace + ".updateTotal );\n");

		buf.append("</script>\n");
	}
}
