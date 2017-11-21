package com.freshdirect.storeapi.rules.configuration;

import java.util.Collections;
import java.util.List;

public abstract class RuleConfiguration {

    protected List<Outcome> outcomes;
    protected List<Condition> conditions;

    public abstract String subsystemName();

    public abstract String serviceName();

    public abstract String xmlFile();

    public List<Outcome> outcomes() {
        return Collections.unmodifiableList(outcomes);
    }

    public List<Condition> conditions() {
        return Collections.unmodifiableList(conditions);
    }

}
