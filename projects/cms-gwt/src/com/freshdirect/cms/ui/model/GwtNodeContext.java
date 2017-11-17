package com.freshdirect.cms.ui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.extjs.gxt.ui.client.store.ListStore;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ui.client.CmsGwt;
import com.freshdirect.cms.ui.client.nodetree.TreeContentNodeModel;
import com.freshdirect.cms.ui.model.attributes.ContentNodeAttributeI;

/**
 * 	Represents the contexts for a node. Inherited attributes and context labels indexed by context paths.
 * 
 *  An example for a valid context of product Lady Apple 
 * 
 *  Path:  "|Store:FDX|Department:fdx_fru|Category:fdx_other|Product:apl_lady"
 *  Label: "FDX > FDX FRU > FDX OTHER > Lady Apple"
 */
public class GwtNodeContext implements Serializable {

	private static final long	serialVersionUID	= 7669920517444756433L;
	
	Map< String, String > contextLabels = new HashMap<String, String> ();	// path => label
	Map< String, String > contextPaths = new HashMap<String, String> ();	// label => path 
	Map< String, Map<String,ContentNodeAttributeI> > inheritedAttributes = new HashMap<String, Map<String,ContentNodeAttributeI>> ();
	
	/**
	 * 	Adds a new context.
	 * 
	 * @param path	context path
	 * @param label	context label
	 * @param inheritedAttr	map of inherited attributes
	 */
	public void addContext( String path, String label, Map<String, ContentNodeAttributeI> inheritedAttr ) {
		contextLabels.put( path, label );
		contextPaths.put( label, path );
		inheritedAttributes.put( path, inheritedAttr );
	}
	
	/**
	 * 	Returns the number of contexts.
	 * @return
	 */
	public int size() {
		return contextLabels.size();
	}

	/**
	 * 	Returns a Set of the context paths, used as map keys.
	 * 
	 * @return
	 */
	public Set<String> getPaths() {
		return contextLabels.keySet();		
	}

	
	/**
	 * 	Returns a context label by path.
	 * 
	 * @param path
	 * @return
	 */
	public String getLabel( String path ) {
		if ( path == null ) 
			return null;
		
		return contextLabels.get( path );
	}

	/**
	 * 	Returns a context path by label.
	 * 
	 * @param otherContextPath
	 * @return
	 */
	public String getPathByLabel( String label ) {
		if ( label == null ) 
			return null;
		
		return contextPaths.get( label );
	}

	/**
	 * 	Returns a map of inherited attributes by path.
	 * 
	 * @param path
	 * @return
	 */
	public Map<String, ContentNodeAttributeI> getInheritedAttributes( String path ) {
		if ( path == null ) 
			return null;
		
		return inheritedAttributes.get( path );
	}
	
	/**
	 * 	Returns an attribute by path and attribute key.
	 * 
	 * @param path
	 * @param key
	 * @return
	 */
	public ContentNodeAttributeI getInheritedAttribute( String path, String key ) {
		if ( path == null || key == null )
			return null;
		
		Map<String, ContentNodeAttributeI> inheritedAttr = inheritedAttributes.get( path );
		if ( inheritedAttr == null )
			return null;
		
		return inheritedAttr.get( key );
	}
		
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( "GwtNodeContext[" );
		sb.append( size() );
		sb.append( "]:[" );
		for ( String s : contextPaths.values() ) {
			sb.append( s );
			sb.append( ';' );
		}
		sb.append( "]" );
		return sb.toString();
	}
	
	
	/**
	 * Utility method that transforms context paths to selectable entries
	 *  
	 * @param ctx
	 * 
	 * @return
	 *
	 * @see ListStore
	 */
	public ListStore<ContentNodeModel> toStore() {

		ListStore<ContentNodeModel> store = new ListStore<ContentNodeModel>();
		// if (ctx != null) {
			for (String path : this.getPaths()) {
				String label = this.getLabel(path);
				String[] fragments = path.split(TreeContentNodeModel.pathSeparator);

				if (fragments.length > 1) {
					String parentKey = fragments[fragments.length - 2];
					ContentNodeModel bm = new ContentNodeModel(null, label, parentKey);
					store.add(bm);
				}
			}
		// }
		return store;
	}



	public List<ContentNodeModel> toModels(String rootKey) {
		List<ContentNodeModel> store = new ArrayList<ContentNodeModel>();
		// if (ctx != null) {
			for (String path : this.getPaths()) {
				String label = this.getLabel(path);
				
				String[] fragments = path.split(TreeContentNodeModel.pathSeparator);
				
				if (fragments.length <= 1)
					continue;

				if (rootKey != null && !rootKey.equalsIgnoreCase(fragments[0]))
					continue;
				
				String parentKey = fragments[fragments.length - 2];
				// String[] labs = label.split(">");
				ContentNodeModel bm = new ContentNodeModel(null, label /* labs[labs.length-2].trim() */, parentKey);
				store.add(bm);
			}
		// }
		return store;
	}


	/**
	 * Extract Store key from pat
	 * Example : "|Store:FreshDirect|...|Product:apl_lady"
	 * 
	 * @param contextPath see example
	 * @return Store content key serialized in string
	 * 
	 * @see ContentKey
	 */
	public static String extractRootKey(String contextPath) {
		String storeKey = null;
		if (contextPath != null) {
			int i = contextPath.indexOf(TreeContentNodeModel.pathSeparator);
			int j = contextPath.indexOf(TreeContentNodeModel.pathSeparator,
					i + 1);
			if (i > -1) {
				if (j > -1) {
					storeKey = contextPath.substring(i + 1, j);
				} else {
					storeKey = contextPath.substring(i + 1);
				}
			}

			CmsGwt.debug("ContentEditorPanel.extractRootKey('"+contextPath+"') -> '" + storeKey + "'");
		} else {
			CmsGwt.debug("ContentEditorPanel.extractRootKey('"+contextPath+"') -> <nil>");
		}
		return storeKey;
	}
}
