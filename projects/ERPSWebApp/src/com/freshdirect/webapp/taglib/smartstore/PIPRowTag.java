package com.freshdirect.webapp.taglib.smartstore;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.util.ProductImpression;


/**
 * Product row iterator tag.
 * 
 * @author segabor
 *
 *
 * Sample:
 * <fd:PIPRow id="pi" impressionRow="<%= piRow %>" isBlankCell="isBlankCell" productImage="prodImage">
 *
 *
 * Attributes
 * ==========
 * 
 * impressionRow          (IN, List)                  - list of ProductImpression instances
 * id                     (OUT, ProductImpression)    - ProductImpression instance.
 * shouldRenderBlankCells (IN, boolean, default=true) - if true the tag will iterate over empty cells
 * isBlankCell            (OUT, Boolean, optional)    - signals current cell is empty, no ProductImpression assigned to 'id'
 * productImage           (OUT, Image, optional)      - Product image <{@link com.freshdirect.fdstore.content.Image}>. Not set if cell is empty.
 * 
 */
public class PIPRowTag extends AbstractGetterTag {
	private static Category LOGGER = LoggerFactory.getInstance( PIPRowTag.class );

	// List of ProductImpression items
	private List impressionRow;

	// render empty cells if impression count is less than max items
	private boolean shouldRenderBlankCells = true;

	// offset in row
	private int offset = 0;
	
	// IN
	public void setImpressionRow(List row) {
		this.impressionRow = row;
	}

	// IN [optional, default=false]
	public void setShouldRenderBlankCells(boolean flag) {
		this.shouldRenderBlankCells = flag;
	}


	private String blankCellVarName;
	public void setIsBlankCell(String varName) {
		this.blankCellVarName = varName;
	}
	
	private String productImageVarName;
	public void setProductImage(String varName) {
		this.productImageVarName = varName;
	}


	protected boolean doIt() throws JspException {
		Object result;
		
		try {
			result = this.getResult();
		} catch (Exception ex) {
			LOGGER.warn("Exception occured in getResult", ex);
			throw new JspException(ex);
		}

		if (this.blankCellVarName != null)
			pageContext.setAttribute(this.blankCellVarName, new Boolean(result == null));

		if (result != null) {
			ProductImpression pi = (ProductImpression) result;

			// store impression in JSP context
			pageContext.setAttribute(this.id, pi);
			
			ProductModel productNode = pi.getProductModel();

			// retrieve product image
			Image prodImage = productNode.getSourceProduct().getAlternateImage();
			if (prodImage == null) {
				prodImage = productNode.getSourceProduct().getCategoryImage();
			}


			// pass image to JSP
			if (this.productImageVarName != null)
				pageContext.setAttribute(this.productImageVarName, prodImage);
		}

		return result != null;
	}






	public int doStartTag() throws JspException {
		offset = 0;
		
		doIt();

		return EVAL_BODY_INCLUDE;
	}


	// returns the actual ProductImpression
	protected Object getResult() throws Exception {
		return offset < impressionRow.size() ? impressionRow.get(offset) : null;
	}


	public int doAfterBody() throws JspException {
		offset++;
		
		PIPLayoutTag layout = (PIPLayoutTag) pageContext.getAttribute("pip_layout");

		// repeat getResult()
		if (shouldRenderBlankCells ? offset < layout.getRowSize() : offset < impressionRow.size() ) {
			doIt(); return EVAL_BODY_AGAIN;
		}

		return SKIP_BODY;
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
					"com.freshdirect.webapp.util.ProductImpression",
					true,
					VariableInfo.NESTED ),
				new VariableInfo(
					data.getAttributeString("isBlankCell"),
					"java.lang.Boolean",
					true,
					VariableInfo.NESTED ),
				new VariableInfo(
					data.getAttributeString("productImage"),
					"com.freshdirect.fdstore.content.Image",
					true,
					VariableInfo.NESTED )
			};
		}
	}
}

