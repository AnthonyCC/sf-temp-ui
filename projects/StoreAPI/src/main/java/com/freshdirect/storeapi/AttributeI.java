/*
 * Created on Oct 28, 2004
 *
 */
package com.freshdirect.storeapi;

import java.io.Serializable;

import com.freshdirect.cms.core.domain.Attribute;

/**
 * Binding class for a named attribute of a specific
 * {@link com.freshdirect.storeapi.ContentNodeI} instance.
 */
@CmsLegacy
public interface AttributeI extends Serializable {

    /**
     * Get the value bound to this attribute.
     *
     * @return the value of the binding (may be null)
     * @deprecated calling ContentNodeI.getAttributeValue(name) is more efficient.
     */
    @Deprecated
    public Object getValue();

    /**
     * Set the value of the attribute binding.
     *
     * @param o
     *            binding value (may be null)
     * @deprecated calling ContentNodeI.getAttributeValue(name) is more efficient.
     */
    @Deprecated
    public void setValue(Object o);

    /**
     * Get the {@link ContentNodeI} that this attribute binds to.
     *
     * @return content node (never null)
     * @deprecated this method prevents some optimization on the generated content nodes, it's planned for removal.
     */
    // @Deprecated
    // public ContentNodeI getContentNode();

    /**
     * @return schema definition of this attribute
     */
    public Attribute getDefinition();

    /**
     * @return name of attribute binding (never null)
     */
    public String getName();

}
