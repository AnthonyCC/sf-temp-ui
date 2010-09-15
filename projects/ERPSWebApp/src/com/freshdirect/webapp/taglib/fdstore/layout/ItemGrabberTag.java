package com.freshdirect.webapp.taglib.fdstore.layout;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Category;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.util.ItemGrabber;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;


public class ItemGrabberTag extends com.freshdirect.framework.webapp.BodyTagSupport {

	private static final long	serialVersionUID	= -8310578679108946007L;

	private static Category LOGGER = LoggerFactory.getInstance( ItemGrabberTag.class );
	
	private boolean ignoreShowChildren = false;
	private String id = null;
	private ContentNodeModel rootNode = null;
	private boolean returnHiddenFolders = false;
	private boolean returnSecondaryFolders = false;
	private boolean filterDiscontinued = true;
    private boolean ignoreDuplicateProducts=false;
    private boolean returnSkus=false;
	private boolean returnInvisibleProducts = false;  // return prods that are flagged as invisible
	private int depth = 1;
		
	private List<ContentNodeModel> workSet = new ArrayList<ContentNodeModel>();

	public void setReturnHiddenFolders(boolean returnHiddenFlag) {
		this.returnHiddenFolders = returnHiddenFlag;
	}
	public void setIgnoreShowChildren(boolean ignoreFlag) {
		this.ignoreShowChildren = ignoreFlag;
	}
    public void setIgnoreDuplicateProducts(boolean ignoreFlag) {
        this.ignoreDuplicateProducts = ignoreFlag;        
    }
    public void setWorkSet(List<ContentNodeModel> workSet) {
    	this.workSet=workSet;
    }        
    public void setReturnSkus(boolean flag) {
        this.returnSkus = flag;
    }        
	public void setId(String id) {
		this.id = id;
	}
	public void setCategory(ContentNodeModel category) {
		this.rootNode = category;
	}		
	public void setDepth(int depth){
		this.depth = depth;
	}
	public void setReturnSecondaryFolders(boolean rsf) {
		this.returnSecondaryFolders = rsf;
	}		
	public void setReturnInvisibleProducts(boolean setting) {
		this.returnInvisibleProducts = setting;
	}
	public void setFilterDiscontinued(boolean filterDiscontinued) {
		this.filterDiscontinued = filterDiscontinued;
	}
	
	
	public int doStartTag() throws JspException {
		if (rootNode == null || id == null) {
			throw new JspException("Attribute for ItemGrabberTag was null!");
		}

		try {
			FDUserI user = FDSessionUser.getFDSessionUser(pageContext.getSession());

			ItemGrabber grabber = new ItemGrabber();
			grabber.setIgnoreShowChildren( ignoreShowChildren );
			grabber.setRootNode( rootNode );
			grabber.setReturnHiddenFolders( returnHiddenFolders );
			grabber.setReturnSecondaryFolders( returnSecondaryFolders );
			grabber.setFilterDiscontinued( filterDiscontinued );
			grabber.setIgnoreDuplicateProducts( ignoreDuplicateProducts );
			grabber.setReturnSkus( returnSkus );
			grabber.setReturnInvisibleProducts( returnInvisibleProducts );
			grabber.setDepth( depth );
			grabber.setPricingCtx( user != null ? user.getPricingContext() : PricingContext.DEFAULT );
			grabber.setWorkSet( workSet );

			workSet = grabber.grabTheItems();

		} catch (FDSkuNotFoundException ex) {
			throw new JspException(ex);
		} catch (FDResourceException ex) {
			throw new JspException(ex);
		}

		//sort the set
		LOGGER.debug(">>>>>>>>>> workSet size before Sort = "+workSet.size());

		pageContext.setAttribute(id, workSet);

		return (EVAL_BODY_BUFFERED);
	} // method doStartTag


	public static class TagEI extends TagExtraInfo {
	    /**
	     * Return information about the scripting variables to be created.
	     *
	     */
	    public VariableInfo[] getVariableInfo(TagData data) {
	        return new VariableInfo[] {
	            new VariableInfo(data.getAttributeString("id"),
	                "java.util.List<ContentNodeModel>",true, VariableInfo.NESTED),
	        };
	    }
	}
}
