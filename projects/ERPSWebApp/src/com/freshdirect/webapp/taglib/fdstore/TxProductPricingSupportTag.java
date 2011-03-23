package com.freshdirect.webapp.taglib.fdstore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.common.pricing.CharacteristicValuePrice;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.Pricing;
import com.freshdirect.common.pricing.SalesUnitRatio;
import com.freshdirect.common.pricing.util.GroupScaleUtil;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.util.ProductImpression;
import com.freshdirect.webapp.util.TransactionalProductImpression;

/**
 * This tag generates client-side scripts to support pricing controls
 * Used in DYF and YMAL transactional displays
 * 
 * Before using this tag you have to include script /assets/javascript/pricing.js unconditionally in the HTML
 * 
 * 
 * 
 * @author segabor
 *
 */
public class TxProductPricingSupportTag extends BodyTagSupport {
	private static final long serialVersionUID = 2149022868536471972L;

	static final DecimalFormat QUANTITY_FORMATTER = new DecimalFormat("0.##");

	// JavaScript namespace to avoid conflicts with another pricing scripts
	private String namespace; 

	private String formName;

	// optional
	private String inputNamePostfix;
	
	// INPUT Product impressions
	private List<ProductImpression> impressions;

	// INPUT(legacy) .. or skus
	private Collection<SkuModel> skus;

	private FDUserI customer;

	
	private boolean onlyCore = false;
	
	private List<TransactionalProductImpression> _txImpressions;


	public void setCustomer(FDUserI customer) {
		this.customer = customer;
	}

	// [optional]
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}


	// [optional] required when action is 'common'
	public void setImpressions(List<ProductImpression> impressions) {
		this.impressions = impressions;
	}


	// [optional]
	public void setFormName(String formName) {
		this.formName = formName;
	}

	// [optional]
	public void setInputNamePostfix(String inputNamePostfix) {
		this.inputNamePostfix = inputNamePostfix;
	}


	/**
	 * Set to true if you don't want to include price configurator script included
	 * By default it set to false
	 * 
	 * @param onlyCore
	 */
	public void setOnlyCore(boolean onlyCore) {
		this.onlyCore = onlyCore;
	}

	/**
	 * optional
	 * 
	 * @param skus
	 */
	public void setSkus(Collection<SkuModel> skus) {
		this.skus = skus;
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



	private String doIncludePricing() {
		StringBuffer buf = new StringBuffer();

		if (skus != null) {
			// Legacy way - work with skus
			appendCoreScriptLegacy(buf, customer, skus);
		} else if (impressions != null) {
			_txImpressions = new ArrayList<TransactionalProductImpression>();
			
			for (ProductImpression pi : impressions) {
				if (pi instanceof TransactionalProductImpression) {
					_txImpressions.add((TransactionalProductImpression)pi);
				}
			}

			appendCoreScript(buf, _txImpressions, customer);
			if (!onlyCore)
				appendConfiguratorScript(buf, customer, _txImpressions, namespace, formName, inputNamePostfix, false);
		}

		return buf.toString();
	}


	public static String getHTMLFragment(FDUserI customer, Collection<TransactionalProductImpression> impressions, boolean onlyCore, String namespace, String formName, String formNamePostfix, boolean setMinQuantity) {
		StringBuffer buf = new StringBuffer();

		appendCoreScript(buf, impressions, customer);
		if (!onlyCore)
			appendConfiguratorScript(buf, customer, impressions, namespace, formName, formNamePostfix, setMinQuantity);

		return buf.toString();
	}
	

	/**
	 * PART I
	 * Generate pricing basics
	 * 
	 * @param buf
	 */
	private static void appendCoreScript(StringBuffer buf, Collection<TransactionalProductImpression> _txImpressions, FDUserI customer) {
		// NOTE: this code replaces i_pricing_script.jsp
		
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


		for (TransactionalProductImpression tpi : _txImpressions) {
			// collect pricing info
			FDProductInfo productInfo = tpi.getProductInfo();
			if (productInfo == null)
				continue;

			FDProduct product = tpi.getFDProduct();
			if (product == null)
				continue;
			
			appendCoreScriptInternal(buf, customer, productInfo, product);
		}
		
		buf.append("</script>\n");
	}



	/**
	 * Legacy way of presenting product prices
	 * 
	 * @param buf
	 */
	private static void appendCoreScriptLegacy(StringBuffer buf, FDUserI customer, Collection<SkuModel> skus) {
		// NOTE: this code replaces i_pricing_script.jsp
		
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


		for (SkuModel sku : skus) {
			try {
				FDProductInfo productInfo = FDCachedFactory.getProductInfo(sku.getSkuCode());
				FDProduct product = FDCachedFactory.getProduct(productInfo);

				appendCoreScriptInternal(buf, customer, productInfo, product);
			} catch (FDResourceException e) {
			} catch (FDSkuNotFoundException e) {
			}
		}
		
		buf.append("</script>\n");
	}



	private static void appendCoreScriptInternal(StringBuffer buf, FDUserI customer, FDProductInfo productInfo, FDProduct product) {
		Pricing pricing = product.getPricing();
		MaterialPrice[] availMatPrices = pricing.getZonePrice(customer.getPricingZoneId()).getMaterialPrices();
		MaterialPrice[] matPrices = null;
		List<MaterialPrice> matPriceList=  new ArrayList<MaterialPrice>();
		if(productInfo.isGroupExists()) {
			//Has a Group Scale associated with it. Check if there is GS price defined for
			//current pricing zone.
			FDGroup group = productInfo.getGroup();
			MaterialPrice[] grpPrices = null;
			try {
				grpPrices = GroupScaleUtil.getGroupScalePrices(group, customer.getPricingZoneId());
			}catch(FDResourceException fe){
				//Never mind. Show regular price for the material.
			}
			if(grpPrices != null){
				//Group scale price applicable to this material. So modify material prices array
				//to accomodate GS price.
				MaterialPrice regularPrice = availMatPrices[0];//Get the regular price/single unit price first.				
				

				if(grpPrices != null && grpPrices.length > 0){
					//Get the first group scale price and set the lower bound to be upper bound of regular price.
					MaterialPrice newRegularPrice = new MaterialPrice(regularPrice.getPrice(),regularPrice.getPricingUnit(),regularPrice.getScaleLowerBound(),
													grpPrices[0].getScaleLowerBound(),grpPrices[0].getScaleUnit(), regularPrice.getPromoPrice());
					//Add the modified regular price.
					matPriceList.add(newRegularPrice);
					//Add the remaining group scale prices.
					for(int i = 0; i < grpPrices.length ; i++){
						matPriceList.add(grpPrices[i]);
					}
					matPrices = (MaterialPrice[])matPriceList.toArray(new MaterialPrice[0]);
				}
			}
			
		}
		if(matPrices == null){
			//Set the default prices defined for the material.
			matPrices = availMatPrices;
		}
		/*
		MaterialPrice[] matPrices = null;
		if(productInfo.getSkuCode().equals("FRU0005343")){
			matPrices =
				new MaterialPrice[] {
					new MaterialPrice(matPrices1[0].getPrice(), matPrices1[0].getPricingUnit(), 0.0, 2,matPrices1[0].getPricingUnit(), matPrices1[0].getPromoPrice()),
					new MaterialPrice(2.50, "EA", 2, 4, "EA", 0.0),
					new MaterialPrice(2.00, "EA", 4, Double.POSITIVE_INFINITY, "EA", 0.0)
					};
		}
		*/
		CharacteristicValuePrice[] cvPrices = pricing.getCharacteristicValuePrices();
		SalesUnitRatio[] suRatios = pricing.getSalesUnitRatios();

		
		// material prices array
		if (matPrices.length > 0) {
			buf.append("if (document.materialPricesArray['" + productInfo.getSkuCode() + "'] == null) {\n");
			buf.append("  document.materialPricesArray['" + productInfo.getSkuCode() + "'] = new Array();\n");
			
			for(int j=0; j<matPrices.length; j++) {
				buf.append("  document.materialPricesArray['" + productInfo.getSkuCode() + "']["+j+"] = new MaterialPrice("+ matPrices[j].getPrice() +", '"+ matPrices[j].getPricingUnit() +"', "+ matPrices[j].getScaleLowerBound() +", "+ (Double.isInfinite(matPrices[j].getScaleUpperBound()) ? "Number.POSITIVE_INFINITY" : matPrices[j].getScaleUpperBound() ) +", '"+matPrices[j].getScaleUnit()+"');\n");
			}

			buf.append("}\n");
		}
		

		// characteristic value prices
		if (cvPrices.length > 0) {
			buf.append("if (!document.cvPricesArray['" + productInfo.getSkuCode() + "']) {\n");
			buf.append("  document.cvPricesArray['" + productInfo.getSkuCode() + "'] = new Array();\n");
			
			for (int j=0; j<cvPrices.length; j++) { 
				buf.append("  document.cvPricesArray['"+ productInfo.getSkuCode() + "']["+j+"] = new CharValuePrice('"+ cvPrices[j].getCharacteristicName() +"', '"+ cvPrices[j].getCharValueName() +"', "+ cvPrices[j].getPrice() +", '"+ cvPrices[j].getPricingUnit() +"', "+ cvPrices[j].getApplyHow() +");\n");
			}

			buf.append("}\n");
		}
		

		// sales unit ratios
		if (suRatios.length > 0) {
			buf.append("if (!document.salesUnitRatiosArray['" + productInfo.getSkuCode() + "']) {\n");
			buf.append("  document.salesUnitRatiosArray['" + productInfo.getSkuCode() + "'] = new Array();\n");

			for(int j=0; j<suRatios.length; j++) {
				buf.append("  document.salesUnitRatiosArray['"+ productInfo.getSkuCode() +"']["+j+"] = new SalesUnitRatio('"+ suRatios[j].getAlternateUnit() +"', '"+ suRatios[j].getSalesUnit() +"', "+ suRatios[j].getRatio() +");\n");
			}
			
			buf.append("}\n");
		}
		
	}
	

	/**
	 * PART II
	 * Configure pricing script
	 * 
	 * @param buf
	 */
	private static void appendConfiguratorScript(StringBuffer buf, FDUserI customer, Collection<TransactionalProductImpression> _txImpressions, String namespace, String formName, String formNamePostfix, boolean setMinQuantity) {
		final int nConfProd = _txImpressions.size();

		final String nsObj = namespace;
		
		buf.append("<script type=\"text/javascript\">\n");
		buf.append("  "+ nsObj +" = new Object();\n");
		buf.append("\n");
		buf.append("\n");
		buf.append("  "+ nsObj +".useGroupScalePricing = false;\n");
		buf.append("  "+ nsObj +".pricings = new Array("+ nConfProd +");\n");
		buf.append("  "+ nsObj +".updateTotal = function() {\n");
		buf.append("    var total=0;\n");
		buf.append("    var totalQty=0;\n");
		buf.append("    var p;\n");
		buf.append("    var selectedSkuCode = '';\n");
		buf.append("    for(i=0; i<"+ nsObj +".pricings.length; i++ ) {\n");
		buf.append("      if ("+ nsObj +".pricings[i] == undefined)\n");
		buf.append("        continue;\n");
		buf.append("\n");
		buf.append("      if ("+ nsObj +".useGroupScalePricing) { \n");
		buf.append("        p = "+ nsObj +".pricings[i].getPrice();\n");
		buf.append("        if (p!=\"\") {\n");
		buf.append("           totalQty+=Number("+ nsObj +".pricings[i].getQuantity());\n");
		buf.append("        }\n");
		buf.append("      } else { \n");
		buf.append("        p = "+ nsObj +".pricings[i].getPrice();\n");
		buf.append("        if (p!=\"\") {\n");
		buf.append("           total+=Number(p.substring(1));\n");
		buf.append("        }\n");
		buf.append("      }\n");
		buf.append("    }\n");
		buf.append("\n");
		buf.append("    for(i=0; i<"+ nsObj +".pricings.length; i++ ) {\n");
		buf.append("      if ("+ nsObj +".pricings[i] == undefined)\n");
		buf.append("        continue;\n");
		buf.append("\n");
		buf.append("      if ("+ nsObj +".useGroupScalePricing) { selectedSkuCode = "+ nsObj +".pricings[i].selectedSku; }\n");
		buf.append("\n");
		buf.append("      if (selectedSkuCode!='') {\n");

		buf.append("      	for (var j=0; j<document.materialPricesArray[selectedSkuCode].length; j++) {\n");
		buf.append("        	if ( isWithinBounds(totalQty, document.materialPricesArray[selectedSkuCode][j]) == true ){\n");
		buf.append("          		if (hasScales(selectedSkuCode, j) == true) {\n");
		buf.append("						var price = (calculateGroupScalePrice(selectedSkuCode, totalQty, Number("+ nsObj + ".pricings[i].getQuantity()),"+ nsObj +".pricings[i].salesUnit, j)).price;\n");
		buf.append("            		total += price;\n");
		buf.append("            		break;\n");
		buf.append("          		}\n");
		buf.append("        	}\n");
		buf.append("      	}\n");
		buf.append("      }\n");		
		buf.append("    }\n");
		buf.append("\n");
		buf.append("	var totalFields = document.getElementsByName('total');\n");
		buf.append("\n");
		buf.append("    for(i=0; i<totalFields.length; i++ ) {\n");
		buf.append("      totalFields[i].value=\"$\"+currencyFormat(total);\n");
		buf.append("    }\n");
		buf.append("\n");
		buf.append("    if (document.forms['"+ formName +"'][\"totalQty\"])\n");
		buf.append("      document.forms['"+ formName +"'][\"totalQty\"].value=totalQty;\n");
		buf.append("  };\n");
		buf.append("\n");
		buf.append("  "+ nsObj +".changeShopQty = function(idx, delta, min, max, increment, qty_inp) {\n");
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
		buf.append("    } else if ((qty > 0) && (qty < min) && (delta < 0)) {\n");
		buf.append("      qty = 0;\n");
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
		buf.append("    "+ nsObj +".pricings[idx].setQuantity(qty);\n");
		buf.append("  };\n");
		buf.append("\n");		

		buf.append("</script>\n");
		
		// PART III. --
		
		int j = 0;
		for (TransactionalProductImpression tpi : _txImpressions) {
			ProductModel product = tpi.getProductModel();
			SkuModel sku = tpi.getSku();

			// ie. "_dyf_3" or "_3"
			String inpPx = "_" + (formNamePostfix != null ? formNamePostfix+"_"+j : Integer.toString(j));

			buf.append("<script type=\"text/javascript\">\n");
			
			buf.append( nsObj + ".pricings["+j+"] = new Pricing();\n");
			buf.append( nsObj + ".pricings["+j+"].setSKU(\""+ sku + "\");\n");
			if (tpi.getProductModel().isSoldBySalesUnits()) {
				buf.append( nsObj + ".pricings["+j+"].setQuantity("+ tpi.getConfiguration().getQuantity() +");\n");
				buf.append( nsObj + ".pricings["+j+"].setSalesUnit(\"\");\n");
			} else {
				buf.append( nsObj + ".pricings["+j+"].setQuantity("+(setMinQuantity ? Double.toString(tpi.getConfiguration().getQuantity()) : "0")+");\n");
				buf.append( nsObj + ".pricings["+j+"].setSalesUnit(\""+ tpi.getConfiguration().getSalesUnit() +"\");\n");
			}
			
			// options
			for ( Map.Entry<String,String> entry : tpi.getConfiguration().getOptions().entrySet() ) {
				buf.append( nsObj + ".pricings["+j+"].setOption(\""+ entry.getKey() +"\", \""+ entry.getValue() +"\");\n");
			}
			
			// change quantity function
			buf.append( nsObj + ".pricings["+j+"].changeQty = function(delta) {\n");
			buf.append( "  " + nsObj + ".changeShopQty("+j+", delta, "+ product.getQuantityMinimum() +", "+ customer.getQuantityMaximum(product) +", "+ product.getQuantityIncrement() +", document.forms['"+ formName +"']['quantity"+ inpPx +"']);\n");
			buf.append("};\n");

			// bind updater function
			buf.append( nsObj + ".pricings["+j+"].setCallbackFunction( "+nsObj+".updateTotal );\n");

			buf.append("</script>\n");
		
			++j;
		}
	}


	public static class TagEI extends TagExtraInfo {
		@Override
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {};
		}
	}
}

