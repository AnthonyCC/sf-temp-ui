package com.freshdirect.smartstore.scoring;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.freshdirect.smartstore.SessionInput;

public class DataGenerator {

    Set factors;

    public List generate(SessionInput sessionInput, DataAccess input) {
        return Collections.EMPTY_LIST;
    }

    public void setFactors(Set factors) {
        this.factors = factors;
    }

    public Set getFactors() {
        return factors;
    }

}
