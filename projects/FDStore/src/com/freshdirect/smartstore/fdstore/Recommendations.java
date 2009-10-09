package com.freshdirect.smartstore.fdstore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.YmalSource;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;

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
	public Recommendations(Variant variant, List<ContentNodeModel> contentNodes, boolean isRefreshable, boolean isSmartSavings) {
		this.variant = variant;
		this.products = new ArrayList<ProductModel>(contentNodes.size());
		// quick and dirty hack to turn content nodes to products
		for (ContentNodeModel m : contentNodes) {
			this.products.add( (ProductModel)m);
		}



		final int S = getNumberOfPages();
		logged = new boolean[S];
		for (int i=0; i<S; i++)
			logged[i] = false;


		this.isRefreshable = isRefreshable;
		this.isSmartSavings = isSmartSavings;
	}

	public Recommendations(Variant variant, List<ContentNodeModel> contentNodes) {
		this(variant, contentNodes, true, false);
	}

	/**
	 * Creates recommendations from serialized content keys
	 * @param variant variant ID
	 * @param input serialized form of content keys. Can be null.
	 * 
	 * @throws InvalidContentKeyException Invalid content key in the list
	 */
	public Recommendations(Variant variant, String input, String currentNodeId, String ymalSourceId,
			boolean isRefreshable, boolean isSmartSavings) throws InvalidContentKeyException {
		this.variant = variant;
		
		if (input != null && !"".equalsIgnoreCase(input.trim())) {
			String s[] = input.split(";");
			if (s.length == 3) {
				this.wsize = Integer.parseInt(s[0]);
				this.offset = Integer.parseInt(s[1]);
				this.products = deserializeProducts(s[2]);
			}
		}

		// irrelevant attributes, ignored
		this.sessionInput = new SessionInput("", EnumServiceType.HOME);
		this.sessionInput.setCurrentNode(ContentFactory.getInstance().getContentNode(currentNodeId));
		try {
			this.sessionInput.setYmalSource((YmalSource) ContentFactory.getInstance().getContentNode(ymalSourceId));
		} catch (ClassCastException e) {
		}
		this.isRefreshable = isRefreshable;
		this.isSmartSavings = isSmartSavings;
	}
	
	
	public Recommendations(Variant variant, List<ContentNodeModel> products, SessionInput sessionInput,
			boolean isRefreshable, boolean isSmartSavings) {
		this(variant, products);
		this.sessionInput = sessionInput;
		this.isRefreshable = isRefreshable;
		this.isSmartSavings = isSmartSavings;
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
	
	public String serializeRecommendation() {
		StringBuffer buf = new StringBuffer();
		
		buf.append(wsize);
		buf.append(";");
		buf.append(offset);
		buf.append(";");
		buf.append(Recommendations.getSerializedProducts(products));
		
		return buf.toString();
	}

	public void setImpressionIds(Map<ContentKey,String> impressionIds) {
		this.impressionIds = impressionIds;
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
	

	/**
	 * Serialize products to String
	 * 
	 * @param products<ProductModel>
	 * @return
	 */
	public static String getSerializedProducts(List<ProductModel> products) {
		StringBuffer buffer = new StringBuffer();
		Iterator<ProductModel> it = products.iterator();
		if (it.hasNext())
		{
			buffer.append(it.next().getContentKey().getId());
		}
		while (it.hasNext())
		{
			buffer.append(',');
			buffer.append(it.next().getContentKey().getId());
		}
		return buffer.toString();
	}
	
	/**
     * 
     * @param input Product keys joined with comma characters
     * @return
     * @throws InvalidContentKeyException
     */
    public static List<ProductModel> deserializeProducts(String input) throws InvalidContentKeyException {
        if (input != null && !"".equals(input)) {
            List<ProductModel> nodes = new ArrayList<ProductModel>();

            String[] ids = input.split(",");
            for (int i = 0; i < ids.length; i++) {
                ContentKey key = ContentKey.create(FDContentTypes.PRODUCT, ids[i]);
                nodes.add((ProductModel) ContentFactory.getInstance().getContentNodeByKey(key));
            }

            return nodes;
        } else {
            // Empty case
            return Collections.emptyList();
        }
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
}
