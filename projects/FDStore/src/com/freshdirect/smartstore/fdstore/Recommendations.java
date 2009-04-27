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
	
	private static final long serialVersionUID = 8230385944777453868L;
	private Variant variant;
	private List products;
	
	private SessionInput sessionInput;
	private Map impressionIds;
	
	/**
	 * Constructor.
	 * @param variant 
	 * @param contentKeys List<{@link ProductModel}>
	 */
	public Recommendations(Variant variant, List contentNodes) {
		this.variant = variant;
		this.products = contentNodes;
	}
	
	/**
	 * Creates recommendations from serialized content keys
	 * @param variant variant ID
	 * @param input serialized form of content keys. Can be null.
	 * 
	 * @throws InvalidContentKeyException Invalid content key in the list
	 */
	public Recommendations(Variant variant, String input, String currentNodeId, String ymalSourceId) throws InvalidContentKeyException {
		this.variant = variant;
		this.products = deserializeContentNodes(input);
		// irrelevant attributes, ignored
		this.sessionInput = new SessionInput("", EnumServiceType.HOME);
		this.sessionInput.setCurrentNode(ContentFactory.getInstance().getContentNode(currentNodeId));
		try {
			this.sessionInput.setYmalSource((YmalSource) ContentFactory.getInstance().getContentNode(ymalSourceId));
		} catch (ClassCastException e) {
		}
	}
	
	
	public Recommendations(Variant variant, List products, SessionInput sessionInput) {
		this(variant, products);
		this.sessionInput = sessionInput;
	}

	/**
	 * Get recommended product nodes.
	 * @return List<{@link ProductModel}>
	 */
	public List getProducts() {
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
	
	public String serializeContentNodes() {
		return Recommendations.getSerializedProducts(products);
	}

	public void setImpressionIds(Map impressionIds) {
            this.impressionIds = impressionIds;
        }
	
	public String getImpressionId(ContentKey key) {
	    Object obj =  impressionIds!=null ? impressionIds.get(key) : null;
	    if (obj instanceof String) {
	        return ((String)obj);
	    }
	    return null;
	}

        public String getImpressionId(ProductModel model) {
            return model != null ? getImpressionId(model.getContentKey()) : null;
        }
	

	/**
	 * Serialize products to String
	 * 
	 * @param products<ProductModel>
	 * @return
	 */
	public static String getSerializedProducts(List products) {
		StringBuffer buffer = new StringBuffer();
		Iterator it = products.iterator();
		if (it.hasNext())
		{
			buffer.append(((ProductModel) it.next()).getContentKey().getId());
		}
		while (it.hasNext())
		{
			buffer.append(',');
			buffer.append(((ProductModel) it.next()).getContentKey().getId());
		}
		return buffer.toString();
	}
	
	/**
     * 
     * @param input
     * @return
     * @throws InvalidContentKeyException
     */
    public static List deserializeContentNodes(String input) throws InvalidContentKeyException {
        if (input != null && !"".equals(input)) {
            List nodes = new ArrayList();

            String[] ids = input.split(",");
            for (int i = 0; i < ids.length; i++) {
                ContentKey key = ContentKey.create(FDContentTypes.PRODUCT, ids[i]);
                nodes.add((ProductModel) ContentFactory.getInstance().getContentNodeByKey(key));
            }

            return nodes;
        } else {
            // Empty case
            return Collections.EMPTY_LIST;
        }
    }
}
