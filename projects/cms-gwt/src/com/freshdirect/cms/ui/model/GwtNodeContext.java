package com.freshdirect.cms.ui.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

	public static final String COS_CONTEXTOVERRIDE_COLOR_GREEN = "OverrideGreen";

	public static final String COS_CONTEXTOVERRIDE_COLOR_RED = "OverrideRed";

	public static final String COS_CONTEXTOVERRIDE_COLOR_NOOVERRIDE = "NoOverride";

	private static final long	serialVersionUID = 7669920517444756433L;

	private Map< String, String > contextLabels = new HashMap<String, String> ();	// path => label
	private Map< String, String > contextPaths = new HashMap<String, String> ();	// label => path
	private Map< String, Map<String,ContentNodeAttributeI> > inheritedAttributes = new HashMap<String, Map<String,ContentNodeAttributeI>> ();
	private Map<String, String> cosContext = new HashMap<String, String>();

    public void addContext(String path, String label) {
		addContext(path, label, new HashMap<String, ContentNodeAttributeI>(),
				COS_CONTEXTOVERRIDE_COLOR_NOOVERRIDE);
    }
	/**
	 * 	Adds a new context.
	 *
	 * @param path	context path
	 * @param label	context label
	 * @param inheritedAttr	map of inherited attributes
	 */
	public void addContext(String path, String label, Map<String, ContentNodeAttributeI> inheritedAttr,
			String cosOverride) {
		contextLabels.put( path, label );
		contextPaths.put( label, path );
		inheritedAttributes.put( path, inheritedAttr );
		cosContext.put(path, cosOverride);
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

	public String getCosContext(String path) {
		if (path == null)
			return null;

		return cosContext.get(path);
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
	 * Extract Store key from path
	 * Example : "|Store:FreshDirect|...|Product:apl_lady"
	 *
	 * @param contextPath see example
	 * @return Store content key serialized in string
	 */
	public static String extractRootKey(String contextPath) {
		String storeKey = null;
		if (contextPath != null) {
			int i = contextPath.indexOf(TreeContentNodeModel.PATH_SEPARATOR);
			int j = contextPath.indexOf(TreeContentNodeModel.PATH_SEPARATOR,
					i + 1);
			if (i > -1) {
				if (j > -1) {
					storeKey = contextPath.substring(i + 1, j);
				} else {
					storeKey = contextPath.substring(i + 1);
				}
                storeKey = storeKey.trim();
			}

			CmsGwt.debug("ContentEditorPanel.extractRootKey('"+contextPath+"') -> '" + storeKey + "'");
		} else {
			CmsGwt.debug("ContentEditorPanel.extractRootKey('"+contextPath+"') -> <nil>");
		}
		return storeKey;
	}
}
