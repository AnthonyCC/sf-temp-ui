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
        return refs.getInverseRelation();
    }

    /* (non-Javadoc)
     * @see com.freshdirect.cms.AttributeI#getName()
     */
    @Override
    public String getName() {
        return refs.getInverseRelation().getName();
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
