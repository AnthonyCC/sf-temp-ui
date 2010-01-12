package com.freshdirect.cms.ui.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ui.model.attributes.ContentNodeAttributeI;

/**
 * 	Represents the contexts for a node. Inherited attributes and context labels indexed by context paths. 
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
}
