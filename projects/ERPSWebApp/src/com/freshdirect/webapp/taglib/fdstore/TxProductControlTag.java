package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.util.TransactionalProductImpression;


/**
 * Tag to render the quantity / SU control widget for transactional product.
 * 
 * @author segabor
 *
 */
public class TxProductControlTag extends BodyTagSupport {
	private static final long serialVersionUID = -4050448427496643482L;

	final java.text.DecimalFormat QUANTITY_FORMATTER = new java.text.DecimalFormat("0.##");

	private TransactionalProductImpression impression;

	// JavaScript namespace
	private String namespace;

	private String inputNamePostfix;

	private int txNumber;



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

	public int doStartTag() {
		JspWriter out = pageContext.getOut();

		StringBuffer buf = new StringBuffer();

		String txPostfix = (inputNamePostfix != null ? "_"+inputNamePostfix+"_" : "_")+txNumber;

		ProductModel product = impression.getProductModel();
		FDConfigurableI configuration = impression.getConfiguration();


		buf.append("<table align=\"center\" style=\"border-collapse: collapse; border-spacing: 0px;\">\n");
		buf.append("  <tr>\n");
		buf.append("    <td style=\"height: 28px; margin: 0px; padding: 0px;\">\n");
		buf.append("      <input type=\"hidden\" name=\"productId"+txPostfix+"\" value=\""+product.getContentName()+"\">\n");
		buf.append("      <input type=\"hidden\" name=\"catId"+txPostfix+"\" value=\""+product.getParentNode().getContentName()+"\">\n");
		buf.append("      <input type=\"hidden\" name=\"skuCode"+txPostfix+"\" value=\""+impression.getSku()+"\">\n");

		if (impression.getProductModel().isSoldBySalesUnits()) {
			buf.append("      <input type=\"hidden\" name=\"quantity"+txPostfix+"\" value=\"1\">\n");

			// render options
			for (Iterator iit = configuration.getOptions().entrySet().iterator(); iit.hasNext(); ) {
				Map.Entry   entry = (Map.Entry) iit.next();
				buf.append("      <input type=\"hidden\" name=\""+entry.getKey()+txPostfix+"\" value=\""+entry.getValue()+"\">\n");
			}
			
			// render sales units
			buf.append("      <select name=\"salesUnit"+txPostfix+"\" style=\"width: 60px\" class=\"text10\" onChange=\""+ namespace +".pricings["+ txNumber +"].setSalesUnit(this.value);\">\n");
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
		} else {
			buf.append("      <input type=\"hidden\" name=\"salesUnit"+txPostfix+"\" value=\""+configuration.getSalesUnit()+"\">\n");

			for (Iterator it_opts = configuration.getOptions().entrySet().iterator(); it_opts.hasNext();) {
				Map.Entry entry = (Map.Entry) it_opts.next();
				buf.append("      <input type=\"hidden\" name=\""+entry.getKey()+txPostfix+"\" value=\""+entry.getValue()+"\">\n");
			}
			buf.append("      <input type=\"text\" name=\"quantity"+txPostfix+"\" value=\"\" style=\"width: 36px\" size=\"3\" maxlength=\"4\" class=\"text10\" onChange=\""+ namespace +".pricings["+ txNumber +"].changeQty(0);\" onBlur=\""+ namespace +".pricings["+ txNumber +"].setQuantity(this.value);\"/>\n");
			buf.append("    </td>\n");
			buf.append("    <td style=\"height: 28px; margin: 0px; padding: 0px;\">\n");
			
			buf.append("      <img onclick=\""+namespace+".pricings["+ txNumber +"].changeQty("+ product.getQuantityIncrement() + ");\" src=\"/media_stat/images/template/quickshop/grn_arrow_up.gif\" style=\"width: 10px; height: 9px; border: 0;  margin-bottom: 1px; margin-top: 1px; cursor: pointer;\" alt=\"Increase quantity\"><br/>\n");
			buf.append("      <img onclick=\""+namespace+".pricings["+ txNumber +"].changeQty("+ (-product.getQuantityIncrement()) + ");\" src=\"/media_stat/images/template/quickshop/grn_arrow.gif\" style=\"width: 10px; height: 9px; border: 0;  margin-bottom: 1px; margin-top: 1px; cursor: pointer;\" alt=\"Decrease quantity\">\n");
		}
		
		buf.append("    </td>\n");
		buf.append("  </tr>\n");
		buf.append("</table>\n");

		try {
			// write out
			out.write(buf.toString());
		} catch (IOException e) {
		}

		return EVAL_BODY_INCLUDE;
	}

	

	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {};
		}
	}
}
