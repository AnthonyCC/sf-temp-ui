package com.freshdirect.smartstore.scoring;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.smartstore.SessionInput;

public class DataGenerator {

    Set<String> factors;

    public List<ContentNodeModel> generate(SessionInput sessionInput, DataAccess input) {
        return Collections.emptyList();
    }

    public void setFactors(Set<String> factors) {
        this.factors = factors;
    }

    public Set<String> getFactors() {
        return factors;
    }
}
