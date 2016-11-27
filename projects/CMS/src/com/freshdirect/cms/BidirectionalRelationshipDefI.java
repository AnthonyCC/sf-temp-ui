/**
 * 
 */
package com.freshdirect.cms;

import java.util.Collection;

/**
 * @author zsombor
 *
 */
public interface BidirectionalRelationshipDefI extends RelationshipDefI {
    
    Collection<RelationshipDefI> getOtherSide();

    RelationshipDefI getOtherSide(ContentType type);
    
}
