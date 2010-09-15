package com.freshdirect.fdstore.content;

import com.freshdirect.content.attributes.ErpsAttributes;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;

public class MockFDVariation extends FDVariation {

    ErpsAttributes attribs;

    public MockFDVariation(ErpsAttributes attribs, String name, FDVariationOption[] variationOptions) {
        super(name, variationOptions);
    }
    
    @Override
    protected ErpsAttributes getAttributes() {
        return attribs;
    }

}
