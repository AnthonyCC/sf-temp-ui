/**
 * 
 */
package com.freshdirect.cms;

/**
 * @author zsombor
 *
 */
public interface BidirectionalRelationshipDefI extends RelationshipDefI {

    BidirectionalRelationshipDefI getOtherSide();
    
    boolean isWritableSide();
    
    ContentType getType();
    
}
