/**
 * 
 */
package com.freshdirect.cms.reverse;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.RelationshipI;

/**
 * @author zsombor
 *
 */
public class BackReference implements RelationshipI {

    ContentNodeI node;
    BidirectionalReferenceHandler refs;
    AttributeDefI reverse;

    public BackReference(ContentNodeI node, BidirectionalReferenceHandler refs) {
        assert node != null : "Node is null!";
        assert refs != null : "Refs is null!";
        this.node = node;
        this.refs = refs;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.cms.AttributeI#getContentNode()
     */
    @Override
    public ContentNodeI getContentNode() {
        return node;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.cms.AttributeI#getDefinition()
     */
    @Override
    public AttributeDefI getDefinition() {
        if (reverse == null) { 
            reverse = refs.getInverseRelation(node.getKey().getType());
        }
        return reverse;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.cms.AttributeI#getName()
     */
    @Override
    public String getName() {
        return getDefinition().getName();
    }

    /* (non-Javadoc)
     * @see com.freshdirect.cms.AttributeI#getValue()
     */
    @Override
    public Object getValue() {
        return refs.getInverseReference(node.getKey());
    }

    /* (non-Javadoc)
     * @see com.freshdirect.cms.AttributeI#setValue(java.lang.Object)
     */
    @Override
    public void setValue(Object o) {

    }
    
    

}
