/*
 * Created on Dec 13, 2004
 *
 */
package com.freshdirect.storeapi.attributes;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.storeapi.CmsLegacy;

/**
 * @author mrose
 *
 */
@CmsLegacy
public interface FDAttributeBuilderI {

    public Object constructValue(Attribute cmsAttrDef, Object value);

}
