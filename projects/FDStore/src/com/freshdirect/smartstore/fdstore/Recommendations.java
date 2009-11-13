package com.freshdirect.smartstore.fdstore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.impl.AbstractRecommendationService;

/**
 * A list of recommended contents tagged with a variant.
 * @author istvan
 *
 */
public class Recommendations implements Serializable {
	public static final int MAX_PRODS = 5;
	
	private static final long serialVersionUID = 8230385944777453868L;
	private Variant variant;
	
	/**
	 * List of all recommended products
	 */
	private List<ProductModel> products;

	private SessionInput sessionInput;
	private Map<ContentKey,String> impressionIds;
	private Map<String,String> prd2recommender;
	private Map<String,String> prd2recommenderStrat;
	private String requestId;
	
	private boolean isRefreshable = true;
	private boolean isSmartSavings = false;

	// products window
	int	offset	= 0;
	int	wsize	= MAX_PRODS;
	
	// array of logged products
	boolean logged[];
	

	/**
	 * Constructor.
	 * @param variant 
	 * @param contentKeys List<{@link ProductModel}>
	 */
	public Recommendations(Variant variant, List<ContentNodeModel> contentNodes, boolean isRefreshable, boolean isSmartSavings, int wsize) {
		this.variant = variant;
		this.products = new ArrayList<ProductModel>(contentNodes.size());
		// quick and dirty hack to turn content nodes to products
		for (ContentNodeModel m : contentNodes) {
			this.products.add( (ProductModel)m);
		}

		this.wsize = wsize;

		final int S = getNumberOfPages();
		logged = new boolean[S];
		for (int i=0; i<S; i++)
			logged[i] = false;


		this.isRefreshable = isRefreshable;
		this.isSmartSavings = isSmartSavings;

		if (AbstractRecommendationService.RECOMMENDER_SERVICE_AUDIT.get() != null) {
			prd2recommender = (Map<String,String>)
					AbstractRecommendationService.RECOMMENDER_SERVICE_AUDIT.get();
	        
	        AbstractRecommendationService.RECOMMENDER_SERVICE_AUDIT.set(null);
		} else {
			prd2recommender = Collections.EMPTY_MAP;
		}

		if (AbstractRecommendationService.RECOMMENDER_STRATEGY_SERVICE_AUDIT.get() != null) {
			prd2recommenderStrat = (Map<String,String>)
					AbstractRecommendationService.RECOMMENDER_STRATEGY_SERVICE_AUDIT.get();
	        AbstractRecommendationService.RECOMMENDER_STRATEGY_SERVICE_AUDIT.set(null);
		} else {
			prd2recommenderStrat = Collections.EMPTY_MAP;			
		}
	}

	public Recommendations(Variant variant, List<ContentNodeModel> contentNodes) {
		this(variant, contentNodes, true, false, MAX_PRODS);
	}
	
	/**
	 * This constructor is called from FDStoreRecommender
	 * 
	 * @param variant
	 * @param products
	 * @param sessionInput
	 * @param isRefreshable
	 * @param isSmartSavings
	 */
	public Recommendations(Variant variant, List<ContentNodeModel> products, SessionInput sessionInput,
			boolean isRefreshable, boolean isSmartSavings) {
		this(variant, products, isRefreshable, isSmartSavings, sessionInput != null && sessionInput.getMaxRecommendations() > 0 ? sessionInput.getMaxRecommendations() : MAX_PRODS );
		this.sessionInput = sessionInput;
	}

	/**
	 * Get recommended products.
	 * @return List<{@link ProductModel}>
	 */
	public List<ProductModel> getProducts() {
		if (products.size() == 0)
			return Collections.emptyList();
		
		if (offset < 0 || offset * wsize >= products.size())
			throw new IndexOutOfBoundsException();
		final int p = offset * wsize;
		// DEBUG System.err.println("pos: " + p  + " num: " + Math.min(wsize, products.size()-p) + " / max products: " + products.size());
		return products.subList(p, Math.min(p+wsize, products.size()) );
	}
	
	
	public Collection<ProductModel> getAllProducts() {
		return products;
	}

	/**
	 * Get variant.
	 * @return variant
	 */
	public Variant getVariant() {
		return variant;
	}
	
	/**
	 * Return the session input.
	 * @return
	 */
	public SessionInput getSessionInput() {
        return sessionInput;
    }
	
    public void addImpressionIds(Map<ContentKey, String> impressionIds) {
        if (this.impressionIds == null) {
            this.impressionIds = impressionIds;
        } else {
            this.impressionIds.putAll(impressionIds);
        }
    }
	
	String getImpressionId(ContentKey key) {
	    Object obj =  impressionIds!=null ? impressionIds.get(key) : null;
	    if (obj instanceof String) {
	        return ((String)obj);
	    }
	    return null;
	}

	public String getImpressionId(ProductModel model) {
		return model != null ? getImpressionId(model.getSourceProduct().getContentKey()) : null;
	}
	
    public boolean isRefreshable() {
    	return this.isRefreshable;
    }

    public boolean isSmartSavings() {
		return isSmartSavings;
	}


    
    /* PAGING MODULE */

    public void pageForward() {
    	if ( (offset+1)*wsize < products.size() ) {
    		offset++;
    	}
    }

    public void pageBackward() {
    	if ( offset > 0 )
    		offset--;
    }

    public boolean isFirstPage() {
    	return offset == 0;
    }
    
    public boolean isLastPage() {
    	return offset == getNumberOfPages()-1;
    }

    public int getOffset() {
    	return this.offset;
    }

    public void setOffset(int newOffset) throws IndexOutOfBoundsException {
    	if (newOffset < 0 || (newOffset*wsize)>= products.size())
    		throw new IndexOutOfBoundsException();
    	this.offset = newOffset;
    }

    public int getNumberOfPages() {
    	return (int) Math.ceil( ( (double) products.size() )/wsize );
    }


    /**
     * Tells if the current product subset logged and sets to true.
     * @return
     */
    public boolean isLogged() {
    	final boolean x = logged[offset];
    	logged[offset] = true;
    	return x;
    }
    
    public String getRecommenderIdForProduct(String productId) {
    	return prd2recommender.get(productId);
    }
    
    public String getRecommenderStrategyIdForProduct(String productId) {
    	return prd2recommenderStrat.get(productId);
    }
    
    public String getRequestId() {
		return requestId;
	}
    
    public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
}
