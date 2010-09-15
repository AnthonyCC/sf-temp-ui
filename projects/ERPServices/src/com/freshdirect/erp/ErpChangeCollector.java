package com.freshdirect.erp;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.content.attributes.ErpsAttributes;

public class ErpChangeCollector implements ErpVisitorI {

    List<ErpsAttributes> result = new ArrayList<ErpsAttributes>();
    
    @Override
    public void pushModel(ErpModelSupport model) {
        if (model.getChangedAttributes() != null) {
            result.add(model.getChangedAttributes());
        }

    }

    @Override
    public void popModel() {

    }
    
    public List<ErpsAttributes> getResult() {
        return result;
    }

}
