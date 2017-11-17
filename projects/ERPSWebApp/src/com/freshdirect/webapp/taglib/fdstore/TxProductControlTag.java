package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.util.TransactionalProductImpression;


/**
 * Tag to render the quantity / SU control widget for transactional product.
 * 
 * @author segabor
 *
 */
public class TxProductControlTag extends BodyTagSupport {
	private static final long serialVersionUID = -4050448427496643482L;

	static final DecimalFormat QUANTITY_FORMATTER = new DecimalFormat("0.##");

	private TransactionalProductImpression impression;

	// JavaScript namespace
	private String namespace;

	private String inputNamePostfix;

	private int txNumber;

	boolean disabled = false;
	
	boolean setMinimumQt = false;

	public void setImpression(TransactionalProductImpression impression) {
		this.impression = impression;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}


	public void setInputNamePostfix(String inputNamePostfix) {
		this.inputNamePostfix = inputNamePostfix;
	}

	public void setTxNumber(int txNumber) {
		this.txNumber = txNumber;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
	public void setSetMinimumQt(boolean setMinimumQt) {
		this.setMinimumQt = setMinimumQt;
	}
	public void setSetMinimumQt(String setMinimumQt) {
		this.setMinimumQt = Boolean.parseBoolean(setMinimumQt);
	}


	@Override
	public int doStartTag() {
		try {
			// write out
			pageContext.getOut().write( TxProductControlTag.getHTMLFragment(impression, inputNamePostfix, txNumber, namespace, disabled, setMinimumQt) );
		} catch (IOException e) {
		}

		return EVAL_BODY_INCLUDE;
	}


	/**
	 * @param impression Product impression
	 * @param inputNamePostfix 
	 * @param jsNamespace JavaScript namespace
	 * @param seqNum Sequence number of value
	 * @param disabled is control disabled
	 * @param setMinimumQt If enabled minimum value will be set
	 * 
	 * @return
	 */
	public static String getHTMLFragment(TransactionalProductImpression impression, String inputNamePostfix, int seqNum, String jsNamespace, boolean disabled, boolean setMinimumQt) {
		StringBuffer buf = new StringBuffer();

		String txPostfix = (inputNamePostfix != null ? "_"+inputNamePostfix+"_" : "_")+seqNum;

		ProductModel product = impression.getProductModel();
		// The 'real' one (product behind a proxy or itself if it's simple)
		ProductModel realProduct = product.getSourceProduct();
		FDConfigurableI configuration = impression.getConfiguration();

		if (disabled) {
			// Hidden case: render empty container and
			//   populate with the necessary input fields
			buf.append("<div style=\"height: 28px; margin: 0px; padding: 0px; visibility: hidden;\">\n");

			buf.append("  <input type=\"hidden\" name=\"productId"+txPostfix+"\" value=\""+realProduct.getContentName()+"\" data-ATCAttribute=\"productId\">\n");
			buf.append("  <input type=\"hidden\" name=\"catId"+txPostfix+"\" value=\""+realProduct.getParentNode().getContentName()+"\" data-ATCAttribute=\"catId\">\n");
			buf.append("  <input type=\"hidden\" name=\"skuCode"+txPostfix+"\" value=\""+impression.getSku()+"\" data-ATCAttribute=\"skuCode\">\n");
			buf.append("  <input type=\"hidden\" name=\"quantity"+txPostfix+"\" value=\"0\" data-ATCAttribute=\"quantity\">\n");

			buf.append("</div>\n");
		} else {
			buf.append("<table align=\"center\" style=\"border-collapse: collapse; border-spacing: 0px; margin: 0 auto\">\n");
			buf.append("  <tr>\n");
			buf.append("    <td style=\"height: 28px; margin: 0px; padding: 0px;\">\n");
			buf.append("      <input type=\"hidden\" name=\"productId"+txPostfix+"\" value=\""+realProduct.getContentName()+"\" data-ATCAttribute=\"productId\">\n");
			buf.append("      <input type=\"hidden\" name=\"catId"+txPostfix+"\" value=\""+realProduct.getParentNode().getContentName()+"\" data-ATCAttribute=\"catId\">\n");
			buf.append("      <input type=\"hidden\" name=\"skuCode"+txPostfix+"\" value=\""+impression.getSku()+"\" data-ATCAttribute=\"skuCode\">\n");
	
			if (product.isSoldBySalesUnits()) {
				buf.append("      <input type=\"hidden\" name=\"quantity"+txPostfix+"\" value=\"1\" data-ATCAttribute=\"quantity\">\n");
	
				// render options
				for (Iterator<Map.Entry<String,String>> iit = configuration.getOptions().entrySet().iterator(); iit.hasNext(); ) {
					Map.Entry<String,String>   entry = iit.next();
					buf.append("      <input type=\"hidden\" name=\""+entry.getKey()+txPostfix+"\" value=\""+entry.getValue()+"\" data-ATCAttribute=\""+entry.getKey()+"\">\n");
				}
				
				// render sales units
				buf.append("      <select data-ATCAttribute=\"salesUnit\" name=\"salesUnit"+txPostfix+"\" style=\"width: 60px\" class=\"text10\" onChange=\""+ jsNamespace +".pricings["+ seqNum +"].setSalesUnit(this.value);\">\n");
				buf.append("        <option value=\"\" selected=\"selected\"></option>\n");
				FDSalesUnit     salesUnits[] = impression.getFDProduct().getSalesUnits();
	            for (int ii = 0; ii < salesUnits.length; ++ii) {
	                FDSalesUnit salesUnit      = salesUnits[ii];
	                String      salesUnitDescr = salesUnit.getDescription();
	
	                // clean parenthesis
	                int ppos = salesUnitDescr.indexOf("(");
	                if (ppos > -1) {
	                    salesUnitDescr = salesUnitDescr.substring(0, ppos).trim();
	                }
	                
	                buf.append("        <option value=\""+ salesUnit.getName() +"\">"+ salesUnitDescr +"</option>\n");
	            }
				buf.append("      </select>\n");
				buf.append("    </td>\n");
			} else {
				final String defaultQuantity = QUANTITY_FORMATTER.format(configuration.getQuantity());

				buf.append("      <input type=\"hidden\" name=\"salesUnit"+txPostfix+"\" value=\""+configuration.getSalesUnit()+"\">\n");
	
				for (Iterator<Map.Entry<String,String>> it_opts = configuration.getOptions().entrySet().iterator(); it_opts.hasNext();) {
					Map.Entry<String,String>   entry = (Map.Entry<String,String>) it_opts.next();
					buf.append("      <input type=\"hidden\" name=\""+entry.getKey()+txPostfix+"\" value=\""+entry.getValue()+"\" data-ATCAttribute=\""+entry.getKey()+"\">\n");
				}
				buf.append("      <div class=\"qtyinput\" data-component=\"quantity\">");
				buf.append("        <span class=\"quantity_minus\" onclick=\""+jsNamespace+".pricings["+ seqNum +"].changeQty("+ (-product.getQuantityIncrement()) + ");\"><span>Decrease quantity</span></span>\n");
				buf.append("        <input data-ATCAttribute=\"quantity\" aria-label=\"quantity\" class=\"qty text10\" type=\"text\" id=\"quantity"+txPostfix+"\" name=\"quantity"+txPostfix+"\" value=\""+(setMinimumQt ? defaultQuantity: "")+"\" size=\"3\" maxlength=\"4\" onChange=\""+ jsNamespace +".pricings["+ seqNum +"].changeQty(0);\" onBlur=\""+ jsNamespace +".pricings["+ seqNum +"].setQuantity(this.value);\">\n");
				buf.append("        <span class=\"quantity_plus\" onclick=\""+jsNamespace+".pricings["+ seqNum +"].changeQty("+ product.getQuantityIncrement() + ");\"><span>Increase quantity</span></span>\n");
				buf.append("      </div>");
				buf.append("    </td>\n");
			}
	
			buf.append("  </tr>\n");
			buf.append("</table>\n");

			
	}
		
		return buf.toString();
	}

	

	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {};
		}
	}
}
