/**
 * @author ekracoff Created on Mar 23, 2005
 */

package com.freshdirect.cms;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.freshdirect.cms.fdstore.FDContentTypes;

/**
 * Comparator for ordering {@link com.freshdirect.cms.ContentNodeI} instances for display. Orders by a priority assigned
 * to the content type then alphabetically based on the label.
 * 
 * @TODO refactor as a service where ContentTypes can register themselves
 */
public class ContentNodeComparator implements Comparator<ContentNodeI> {

	private final static Map<ContentType, Integer>	DEFAULT_ORDER	= new HashMap<ContentType, Integer>();
	
	//TODO add more content types for sort ordering 
	static {
		DEFAULT_ORDER.put( FDContentTypes.STORE, new Integer( 103 ) );
		DEFAULT_ORDER.put( FDContentTypes.DEPARTMENT, new Integer( 102 ) );
		DEFAULT_ORDER.put( FDContentTypes.FDFOLDER, new Integer( 101 ) );
		DEFAULT_ORDER.put( FDContentTypes.CATEGORY, new Integer( 100 ) );
		
		DEFAULT_ORDER.put( FDContentTypes.PRODUCT, new Integer( 61 ) );
		DEFAULT_ORDER.put( FDContentTypes.CONFIGURED_PRODUCT, new Integer( 60 ) );
		
		DEFAULT_ORDER.put( FDContentTypes.SKU, new Integer( 50 ) );
		
		DEFAULT_ORDER.put( FDContentTypes.RECIPE, new Integer( 40 ) );
		
		DEFAULT_ORDER.put( FDContentTypes.RECOMMMENDER, new Integer( 31 ) );
		DEFAULT_ORDER.put( FDContentTypes.RECOMMENDER_STRATEGY, new Integer( 30 ) );
				
		DEFAULT_ORDER.put( FDContentTypes.MEDIAFOLDER, new Integer( 11 ) );
		DEFAULT_ORDER.put( FDContentTypes.IMAGE, new Integer( 10 ) );
		DEFAULT_ORDER.put( FDContentTypes.HTML, new Integer( 10 ) );
	}

	public final static ContentNodeComparator	DEFAULT	= new ContentNodeComparator( DEFAULT_ORDER );

	private final Map<ContentType, Integer>		order;

	/**
	 * @param order
	 *            Map of ContentType -> Integer
	 */
	public ContentNodeComparator( Map<ContentType, Integer> order ) {
		super();
		this.order = order;
	}

	public int compare( ContentNodeI node1, ContentNodeI node2 ) {
		
		if ( node1 == null )
			return node2 == null ? 0 : -1;
		if ( node2 == null )
			return 1;
		
		ContentKey key1 = node1.getKey();
		ContentKey key2 = node2.getKey();

		int node1Priority = order.get( key1.getType() ) == null ? 0 : ( (Integer)order.get( key1.getType() ) ).intValue();
		int node2Priority = order.get( key2.getType() ) == null ? 0 : ( (Integer)order.get( key2.getType() ) ).intValue();

		int i = node2Priority - node1Priority;

		// if both are 0
		if ( i == 0 && node1Priority == 0 ) {
			i = key1.getType().getName().toLowerCase().compareTo( key2.getType().getName().toLowerCase() );
		}
		
		if ( i == 0 ) {
			i = node1.getLabel().toLowerCase().compareTo( node2.getLabel().toLowerCase() );
		}

		return i;
	}

}
