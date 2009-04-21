package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.common.pricing.CharacteristicValuePrice;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.Pricing;
import com.freshdirect.common.pricing.SalesUnitRatio;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
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
 * @author segabor
 *
 */
public class TxProductPricingSupportTag extends BodyTagSupport {
	private static final long serialVersionUID = 2149022868536471972L;

	
	// JavaScript namespace to avoid conflicts with another pricing scripts
	private String namespace; 

	private String formName;

	// optional
	private String inputNamePostfix;
	
	// List<ProductImpression> of transactional product skus
	private List impressions;

	private FDUserI customer;

	private List txImpressions;


	public void setCustomer(FDUserI customer) {
		this.customer = customer;
	}

	// [mandatory]
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}


	// [optional] required when action is 'common'
	public void setImpressions(List impressions) {
		this.impressions = impressions;
	}


	// [mandatory]
	public void setFormName(String formName) {
		this.formName = formName;
	}

	// [optional]
	public void setInputNamePostfix(String inputNamePostfix) {
		this.inputNamePostfix = inputNamePostfix;
	}

	public int doStartTag() throws JspException {
		txImpressions = new ArrayList();
		
		for (Iterator it=impressions.iterator(); it.hasNext();) {
			ProductImpression pi = (ProductImpression) it.next();
			if (pi instanceof TransactionalProductImpression) {
				txImpressions.add(pi);
			}
		}

		String content = doIncludePricing();

		if (content != null) {
			try {
				JspWriter out = pageContext.getOut();
				out.print(content);
			} catch (Exception e) {
			}
		}

		return EVAL_BODY_INCLUDE;
	}



	private String doIncludePricing() {
		StringBuffer buf = new StringBuffer();

		
		// PART I. -- Generate pricing basics
		// NOTE: this code replaces i_pricing_script.jsp
		
		appendScriptInclude(buf, "/assets/javascript/pricing.js");
		
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


		for (Iterator it=txImpressions.iterator(); it.hasNext();) {
			TransactionalProductImpression tpi = (TransactionalProductImpression) it.next();
			SkuModel sku = (SkuModel) tpi.getSku();

			// collect pricing info
			FDProductInfo productInfo = tpi.getProductInfo();
			if (productInfo == null)
				continue;

			FDProduct product = tpi.getFDProduct();
			if (product == null)
				continue;
			
			
			Pricing pricing = product.getPricing();
			MaterialPrice[] matPrices = pricing.getMaterialPrices();
			CharacteristicValuePrice[] cvPrices = pricing.getCharacteristicValuePrices();
			SalesUnitRatio[] suRatios = pricing.getSalesUnitRatios();

			
			// material prices array
			if (matPrices.length > 0) {
				buf.append("if (document.materialPricesArray['" + productInfo.getSkuCode() + "'] == null) {\n");
				buf.append("  document.materialPricesArray['" + productInfo.getSkuCode() + "'] = new Array();\n");
				
				for(int j=0; j<matPrices.length; j++) {
					buf.append("  document.materialPricesArray['" + productInfo.getSkuCode() + "']["+j+"] = new MaterialPrice("+ matPrices[j].getPrice() +", '"+ matPrices[j].getPricingUnit() +"', '"+ matPrices[j].getScaleLowerBound() +"', '"+ matPrices[j].getScaleUpperBound() +"', '"+matPrices[j].getScaleUnit()+"');\n");
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
		
		buf.append("</script>\n");
		// buf.append("<%-- end of javascript pricing structures --%>\n");




		// PART II. -- Configure pricing script
		int nConfProd = txImpressions.size();

		final String nsObj = "document."+namespace;
		
		buf.append("<script type=\"text/javascript\">\n");
		buf.append("  "+ nsObj +" = new Object();\n");
		buf.append("\n");
		buf.append("\n");
		buf.append("  "+ nsObj +".pricings = new Array("+ nConfProd +");\n");
		buf.append("  "+ nsObj +".updateTotal = function() {\n");
		buf.append("    var total=0;\n");
		buf.append("    var p;\n");
		buf.append("    for(i=0; i<"+ nsObj +".pricings.length; i++ ) {\n");
		buf.append("      if ("+ nsObj +".pricings[i] == undefined)\n");
		buf.append("        continue;\n");
		buf.append("\n");
		buf.append("        p = "+ nsObj +".pricings[i].getPrice();\n");
		buf.append("        if (p!=\"\") {\n");
		buf.append("        total+=Number(p.substring(1));\n");
		buf.append("      }\n");
		buf.append("    }\n");
		buf.append("    document.forms['"+ formName +"'][\"total\"].value=\"$\"+currencyFormat(total);\n");
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

		buf.append("  // update 'total' form field\n");
		buf.append("  if (qty_inp != null)\n");
		buf.append("	  qty_inp.value = qty;\n");
		buf.append("\n");
		buf.append("    "+ nsObj +".pricings[idx].setQuantity(qty);\n");
		buf.append("  };\n");
		buf.append("\n");		

		buf.append("</script>\n");
		
		// PART III. --
		
		int j = 0;
		for (Iterator it=txImpressions.iterator(); it.hasNext();) {
			TransactionalProductImpression tpi = (TransactionalProductImpression) it.next();
			ProductModel product = tpi.getProductModel();
			SkuModel sku = (SkuModel) tpi.getSku();

			// ie. "_dyf_3" or "_3"
			String inpPx = "_" + (inputNamePostfix != null ? inputNamePostfix+"_"+j : Integer.toString(j));

			buf.append("<script type=\"text/javascript\">\n");
			
			buf.append( nsObj + ".pricings["+j+"] = new Pricing();\n");
			buf.append( nsObj + ".pricings["+j+"].setSKU(\""+ sku + "\");\n");
			if (tpi.getProductModel().isSoldBySalesUnits()) {
				buf.append( nsObj + ".pricings["+j+"].setQuantity("+ tpi.getConfiguration().getQuantity() +");\n");
				buf.append( nsObj + ".pricings["+j+"].setSalesUnit(\"\");\n");
			} else {
				buf.append( nsObj + ".pricings["+j+"].setQuantity(0);\n");
				buf.append( nsObj + ".pricings["+j+"].setSalesUnit(\""+ tpi.getConfiguration().getSalesUnit() +"\");\n");
			}
			
			// options
			for (Iterator iit = tpi.getConfiguration().getOptions().entrySet().iterator(); iit.hasNext(); ) {
				Map.Entry entry = (Map.Entry) iit.next();
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
		
		
		return buf.toString();
	}


	private void appendScriptInclude(StringBuffer buf, String path) {
		buf.append("<script type=\"text/javascript\" src=\"");
		buf.append(path);
		buf.append("\"></script>\n");
	}



	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {};
		}
	}
}
