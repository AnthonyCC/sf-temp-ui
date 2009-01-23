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
import com.freshdirect.smartstore.Variant;

/**
 * A list of recommended contents tagged with a variant.
 * @author istvan
 *
 */
public class Recommendations implements Serializable {
	
	private static final long serialVersionUID = 8230385944777453868L;
	private Variant variant;
	private List contentNodes;
	
	
	/**
	 * Constructor.
	 * @param variant 
	 * @param contentKeys List<{@link ProductModel}>
	 */
	public Recommendations(Variant variant, List contentNodes) {
		this.variant = variant;
		this.contentNodes = contentNodes;
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

			contentNodes = nodes;
		} else {
			// Empty case
			contentNodes = Collections.EMPTY_LIST;
		}
	}
	
	
	/**
	 * Get recommended content keys.
	 * @return List<{@link ProductModel}>
	 */
	public List getContentNodes() {
		return contentNodes;
	}
	
	/**
	 * Get variant.
	 * @return variant
	 */
	public Variant getVariant() {
		return variant;
	}
	
	public String serializeContentNodes() {
		StringBuffer buffer = new StringBuffer();
		Iterator it = contentNodes.iterator();
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
	
	public List deserializeContentNodes(String input) throws InvalidContentKeyException {
		if (input == null || "".equals(input))
			return Collections.EMPTY_LIST;

		List nodes = new ArrayList();
		
		String[] ids = input.split(",");
		for (int i = 0; i < ids.length; i++) {
            ContentKey key = ContentKey.create(FDContentTypes.PRODUCT, ids[i]);
            nodes.add((ProductModel) ContentFactory.getInstance().getContentNodeByKey(key));
		}
		return contentNodes = nodes;
	}
}
