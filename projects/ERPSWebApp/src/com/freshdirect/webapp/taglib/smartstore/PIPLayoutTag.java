package com.freshdirect.webapp.taglib.smartstore;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.util.ProductDisplayUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.Image;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.util.ProductImpression;

/**
 * Product Impression Presentation
 * Layout Tag
 * 
 * @author segabor
 * 
 * This tag splits up impressions into rows allowing to render them as table.
 * 
 *
 * Sample:
 * <fd:PIPLayout id="piRow" rowSize="3" impressions="<%= impressions %>" maxRowHeight="rowHeight">...</fd:PIPLayout>
 * 
 * 
 * Attributes
 * ==========
 * 
 * impressions   (IN, List)                         - list of ProductImpression instances
 * id            (OUT, List)                        - name of variable that receives a row of impressions
 * rowSize       (IN, int, optional, default=5)     - number of items in a row
 * singleRowMode (IN, int, optional, default=false) - stops after the first row processed
 * maxRowHeight  (OUT, Integer, optional)           - the height of the largest product image in pixels
 *
 */
public class PIPLayoutTag extends AbstractGetterTag<List<ProductImpression>> {
	private static final long serialVersionUID = 2416505512415702904L;

	private static Category LOGGER = LoggerFactory.getInstance( PIPLayoutTag.class );

	// List of ProductImpression objects
	private List<ProductImpression> impressions;

	// number of items per row
	private int rowSize = 5;

	// render just a single row
	private boolean singleRowMode = false;
	
	// row number
	private int _row = 0;
	
	// IN
	public void setImpressions(List<ProductImpression> impressions) {
		this.impressions = impressions;
	}

	// IN
	public void setRowSize(int rsize) {
		this.rowSize = rsize;
	}
	
	public int getRowSize() {
		return this.rowSize;
	}
	
	// IN
	public void setSingleRowMode(boolean flag) {
		this.singleRowMode = flag;
	}
	
	public boolean getSingleRowMode() {
		return this.singleRowMode;
	}
	
	
	// returns the actual row number
	public int getRow() {
		return _row;
	}

	// variable name
	private String maxRowHeightVarName;
	public void setMaxRowHeight(String varName) {
		this.maxRowHeightVarName = varName;
	}

	// common iterable result producer method
	protected boolean doIt() throws JspException {
		List<ProductImpression> result;
		try {
			result = this.getResult();
		} catch (Exception ex) {
			LOGGER.warn("Exception occured in getResult", ex);
			throw new JspException(ex);
		}

		if (result != null) {
			// put product impression row to JSP context
			pageContext.setAttribute(this.id, result);

			// calculate max height
			List<ProductModel> pm = new ArrayList<ProductModel>();
			for (ProductImpression pi : result) {
				pm.add( pi.getProductModel() );
			}
			final int maxHeight = ProductDisplayUtil.getMaxHeight(pm);

			pageContext.setAttribute(this.maxRowHeightVarName, new Integer(maxHeight));
		}

		return result != null;
	}


	/**
	 * returns the actual impression row
	 */
	protected List<ProductImpression> getResult() throws Exception {
		int offset = _row*rowSize;
		int rs = rowSize;
		
		List<ProductImpression> imp_row = new ArrayList<ProductImpression>();
		while (rs > 0 && offset < impressions.size()) {
			imp_row.add(impressions.get(offset));
			rs--; offset++;
		}
		
		return imp_row;
	}


	// before action method
	public int doStartTag() throws JspException {
		if (!doIt()) {
			return SKIP_BODY;	
		}

		pageContext.setAttribute("pip_layout", this);

		return EVAL_BODY_INCLUDE;
	}
	

	// after action - repeat
	public int doAfterBody() throws JspException {
		_row++;
		
		// repeat getResult()
		doIt();
		
		return !singleRowMode && (_row*rowSize) < impressions.size() ? EVAL_BODY_AGAIN : SKIP_BODY;
	}
	
	public int doEndTag() throws JspException {
		_row = 0;
		return super.doEndTag();
	}



	/**
	 * Define variables and types in tag's scope
	 * @author segabor
	 */
	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
	
			return new VariableInfo[] {
				new VariableInfo(
					data.getAttributeString("id"),
					"java.util.List<com.freshdirect.webapp.util.ProductImpression>",
					true,
					VariableInfo.NESTED ),
				new VariableInfo(
					data.getAttributeString("maxRowHeight"),
					"java.lang.Integer",
					true,
					VariableInfo.NESTED )
			};
		}
	}
}
