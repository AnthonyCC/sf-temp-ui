/**
 * 
 */
package com.freshdirect.cms.reverse;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.RelationshipI;

/**
 * @author zsombor
 *
 */
public class BidirectionalReference implements RelationshipI {

    ContentNodeI node;
    BidirectionalReferenceHandler refs;
    
    
    
    public BidirectionalReference(ContentNodeI node, BidirectionalReferenceHandler refs) {
        super();
        this.node = node;
        this.refs = refs;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.cms.AttributeI#getContentNode()
     */
    @Override
    @Deprecated
    public ContentNodeI getContentNode() {
        return node;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.cms.AttributeI#getDefinition()
     */
    @Override
    public AttributeDefI getDefinition() {
        return refs.getRelation();
    }

    /* (non-Javadoc)
     * @see com.freshdirect.cms.AttributeI#getName()
     */
    @Override
    public String getName() {
        return refs.getRelation().getName();
    }

    /* (non-Javadoc)
     * @see com.freshdirect.cms.AttributeI#getValue()
     */
    @Override
    @Deprecated
    public Object getValue() {
        return refs.getReference(node.getKey());
    }

    /* (non-Javadoc)
     * @see com.freshdirect.cms.AttributeI#setValue(java.lang.Object)
     */
    @Override
    @Deprecated
    public void setValue(Object o) {
        refs.addRelation(node.getKey(), (ContentKey) o);
    }

}
