package com.freshdirect.storeapi.rules.configuration;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DlvPremiumRulesConfiguration extends RuleConfiguration {

    public DlvPremiumRulesConfiguration() {
        conditions = new ArrayList<Condition>();
        outcomes = new ArrayList<Outcome>();

        conditions.add(new Condition("com.freshdirect.storeapi.rules.OgnlCondition", "OGNL Condition", "OgnlCondition"));
        conditions.add(new Condition("com.freshdirect.storeapi.rules.RuleRef", "Rule Reference", "rule-ref"));
        conditions.add(new Condition("com.freshdirect.fdstore.rules.ProfileCondition", "Profile Value", "ProfileCondition"));
        conditions.add(new Condition("com.freshdirect.fdstore.rules.OrderAmount", "Order Amount", "OrderAmount"));
        conditions.add(new Condition("com.freshdirect.fdstore.rules.ServiceTypeCondition", "Service Type", "serviceType"));
        conditions.add(new Condition("com.freshdirect.fdstore.rules.CountyCondition", "County", "County"));

        outcomes.add(new Outcome("com.freshdirect.fdstore.rules.DlvPremium", "Premium", "DlvPremium"));
    }

    @Value("${com.freshdirect.fdstore.rules.dlvpremium.location}")
    private String xmlFile;

    @Override
    public String subsystemName() {
        return "DLVPREMIUM";
    }

    @Override
    public String serviceName() {
        return "com.freshdirect.fdstore.rules.dlvpremium.Engine";
    }

    @Override
    public String xmlFile() {
        return xmlFile;
    }

}
