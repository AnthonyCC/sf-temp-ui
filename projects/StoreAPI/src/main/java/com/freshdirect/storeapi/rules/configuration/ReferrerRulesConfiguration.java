package com.freshdirect.storeapi.rules.configuration;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReferrerRulesConfiguration extends RuleConfiguration {

    public ReferrerRulesConfiguration() {
        conditions = new ArrayList<Condition>();
        outcomes = new ArrayList<Outcome>();

        conditions.add(new Condition("com.freshdirect.storeapi.rules.OgnlCondition", "OGNL Condition", "OgnlCondition"));
        conditions.add(new Condition("com.freshdirect.storeapi.rules.RuleRef", "Rule Reference", "rule-ref"));
        conditions.add(new Condition("com.freshdirect.fdstore.rules.ProfileCondition", "Profile Value", "ProfileCondition"));
        conditions.add(new Condition("com.freshdirect.fdstore.rules.OrderAmount", "Order Amount", "OrderAmount"));
        conditions.add(new Condition("com.freshdirect.fdstore.rules.ServiceTypeCondition", "Service Type", "serviceType"));
        conditions.add(new Condition("com.freshdirect.fdstore.rules.CountyCondition", "County", "County"));
    }

    @Value("${com.freshdirect.fdstore.rules.referrer.location}")
    private String xmlFile;

    @Override
    public String subsystemName() {
        return "REFERRER";
    }

    @Override
    public String serviceName() {
        return "com.freshdirect.fdstore.rules.referrer.Engine";
    }

    @Override
    public String xmlFile() {
        return xmlFile;
    }

}
