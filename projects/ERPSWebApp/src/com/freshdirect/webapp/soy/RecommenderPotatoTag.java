package com.freshdirect.webapp.soy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.webapp.ajax.DataPotatoField;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.ProductRecommenderUtil;


public class RecommenderPotatoTag extends SimpleTagSupport {

	private static final Logger LOGGER = LoggerFactory.getInstance( RecommenderPotatoTag.class );
		
	private String name;
	private String siteFeature;
	private int maxItems = 10;
	private String currentNodeKey = null;
	private String cmEventSource = null;
	private boolean sendVariant = false;
	
	public String getName() {
		return name;
	}	
	public void setName( String name ) {
		this.name = name;
	}
	public String getSiteFeature() {
		return siteFeature;
	}	
	public void setSiteFeature( String siteFeature ) {
		this.siteFeature = siteFeature;
	}	
	public int getMaxItems() {
		return maxItems;
	}	
	public void setMaxItems( int maxItems ) {
		this.maxItems = maxItems;
	}
	public String getCurrentNodeKey() {
		return currentNodeKey;
	}
	public void setCurrentNodeKey(String currentNodeKey) {
		this.currentNodeKey = currentNodeKey;
	}
	public String getCmEventSource() {
		return cmEventSource;
	}
	public void setCmEventSource(String cmEventSource) {
		this.cmEventSource = cmEventSource;
	}
		
	public boolean isSendVariant() {
		return sendVariant;
	}
	public void setSendVariant(boolean sendVariant) {
		this.sendVariant = sendVariant;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void doTag() throws JspException {
		
		LOGGER.info( "Creating data potato: " + name );
		
		HttpSession session = ((PageContext)getJspContext()).getSession();
		FDUserI user = (FDUserI)session.getAttribute( SessionName.USER );
		
		// get recommended items
		List<ProductModel> products = new ArrayList<ProductModel>();
		Recommendations results = null;
		
		try {
			FDStoreRecommender recommender = FDStoreRecommender.getInstance();	  
			
			ContentNodeModel currentNode = null;
			if( currentNodeKey != null && currentNodeKey.length()!=0) {
				currentNode = ContentFactory.getInstance().getContentNodeByKey(ContentKey.getContentKey( currentNodeKey ));
			}
			
			results = recommender.getRecommendations(EnumSiteFeature.getEnum(siteFeature), user, ProductRecommenderUtil.createSessionInput( session, user, maxItems, currentNode , null ) );

			ProductRecommenderUtil.persistToSession(session, results);
			
			products = results.getAllProducts();
			
			if (products.size()>maxItems){
				products = products.subList(0, maxItems);
			}
			
		} catch ( FDResourceException e ) {
			LOGGER.warn( "Failed to get recommendations for siteFeature:"+siteFeature, e );
		}
		
		Map<String,?> dataMap = null;
		
		if(sendVariant && results!=null){
			dataMap = DataPotatoField.digProductListFromModels( user, products, results.getVariant().getId() );			
		}else{
			dataMap = DataPotatoField.digProductListFromModels( user, products );	
		}
		
		if(cmEventSource!=null){
			((Map<String, Object>) dataMap).put("cmEventSource", cmEventSource);			
		}

		((PageContext)getJspContext()).setAttribute( name, dataMap );
		
	}
	
	public static class TagEI extends TagExtraInfo {
	    /**
	     * Return information about the scripting variables to be created.
	     */
		@Override
	    public VariableInfo[] getVariableInfo(TagData data) {

	        return new VariableInfo[] {
	            new VariableInfo(
	            		data.getAttributeString( "name" ),
	            		"java.util.Map<String,?>",
	            		true, 
	            		VariableInfo.AT_BEGIN )
	        };
	    }
	}
}
