package com.freshdirect.fdstore.content;

import com.freshdirect.content.attributes.ErpsAttributes;
import com.freshdirect.fdstore.FDSalesUnit;

public class MockFDSalesUnit extends FDSalesUnit {

    ErpsAttributes attribs;

    public MockFDSalesUnit(ErpsAttributes attribs, String name, String description) {
        super(name, description);
        this.attribs = attribs;
    }
    
    @Override
    protected ErpsAttributes getAttributes() {
        return attribs;
    }

}
