package com.freshdirect.smartstore.fdstore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Trigger;
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
	private List categories;
	private List recipes;
	
	private SessionInput sessionInput;
	
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
	public Recommendations(Variant variant, String input) throws InvalidContentKeyException {
		this.variant = variant;

		if (input != null && !"".equals(input)) {
			List nodes = new ArrayList();

			String[] ids = input.split(",");
			for (int i = 0; i < ids.length; i++) {
	            ContentKey key = ContentKey.create(FDContentTypes.PRODUCT, ids[i]);
	            nodes.add((ProductModel) ContentFactory.getInstance().getContentNodeByKey(key));
			}

			products = nodes;
		} else {
			// Empty case
			products = Collections.EMPTY_LIST;
		}
	}
	
	
	public Recommendations(Variant variant, List products,
			List categories, List recipes, SessionInput sessionInput) {
		this(variant, products);
		this.categories = categories;
		this.recipes = recipes;
		this.sessionInput = sessionInput;
	}

	/**
	 * Get recommended product nodes.
	 * @return List<{@link ProductModel}>
	 */
	public List getProducts() {
		return products;
	}
	
	public List getCategories() {
		return categories != null ? categories : Collections.EMPTY_LIST;
	}
	
	public List getRecipes() {
		return recipes != null ? recipes : Collections.EMPTY_LIST;
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
	 * @deprecated
	 * 
	 * @param input
	 * @return
	 * @throws InvalidContentKeyException
	 */
	public List deserializeContentNodes(String input) throws InvalidContentKeyException {
		if (input == null || "".equals(input))
			return Collections.EMPTY_LIST;

		List nodes = new ArrayList();
		
		String[] ids = input.split(",");
		for (int i = 0; i < ids.length; i++) {
            ContentKey key = ContentKey.create(FDContentTypes.PRODUCT, ids[i]);
            nodes.add((ProductModel) ContentFactory.getInstance().getContentNodeByKey(key));
		}
		return products = nodes;
	}
}
