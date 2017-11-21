package com.freshdirect.storeapi.rules.configuration;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DlvRulesConfiguration extends RuleConfiguration {

    public DlvRulesConfiguration() {
        conditions = new ArrayList<Condition>();
        outcomes = new ArrayList<Outcome>();

        conditions.add(new Condition("com.freshdirect.storeapi.rules.OgnlCondition", "OGNL Condition", "OgnlCondition"));
        conditions.add(new Condition("com.freshdirect.storeapi.rules.RuleRef", "Rule Reference", "rule-ref"));
        conditions.add(new Condition("com.freshdirect.fdstore.rules.ProfileCondition", "Profile Value", "ProfileCondition"));
        conditions.add(new Condition("com.freshdirect.fdstore.rules.OrderAmount", "Order Amount", "OrderAmount"));
        conditions.add(new Condition("com.freshdirect.fdstore.rules.ServiceTypeCondition", "Service Type", "serviceType"));
        conditions.add(new Condition("com.freshdirect.fdstore.rules.CountyCondition", "County", "County"));

        outcomes.add(new Outcome("com.freshdirect.fdstore.rules.BasePrice", "Base Price", "BasePrice"));
        outcomes.add(new Outcome("com.freshdirect.fdstore.rules.Adjustment", "Adjustment", "Adjustment"));
        outcomes.add(new Outcome("com.freshdirect.fdstore.rules.DlvPremium", "Premium", "DlvPremium"));
    }

    @Value("${com.freshdirect.fdstore.rules.dlv.location}")
    private String xmlFile;

    @Override
    public String subsystemName() {
        return "DLV";
    }

    @Override
    public String serviceName() {
        return "com.freshdirect.fdstore.rules.dlv.Engine";
    }

    @Override
    public String xmlFile() {
        return xmlFile;
    }

}
