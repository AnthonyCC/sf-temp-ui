package com.freshdirect.storeapi.rules.configuration;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//TODO: this is the -prd version. If we need the other one too, then create a new config for it
@Component
public class MiscRulesConfiguration extends RuleConfiguration {

    public MiscRulesConfiguration() {
        conditions = new ArrayList<Condition>();
        outcomes = new ArrayList<Outcome>();

        conditions.add(new Condition("com.freshdirect.storeapi.rules.OgnlCondition", "OGNL", "OgnlCondition"));
        conditions.add(new Condition("com.freshdirect.storeapi.rules.RuleRef", "Rule Reference", "rule-ref"));
        conditions.add(new Condition("com.freshdirect.fdstore.rules.ProfileCondition", "Profile Value", "ProfileCondition"));
        conditions.add(new Condition("com.freshdirect.fdstore.rules.OrderAmount", "Order Amount", "OrderAmount"));
        conditions.add(new Condition("com.freshdirect.fdstore.rules.ServiceTypeCondition", "Service Type", "serviceType"));
        conditions.add(new Condition("com.freshdirect.fdstore.rules.CountyCondition", "County", "County"));

        outcomes.add(new Outcome("com.freshdirect.fdstore.rules.BasePrice", "Base Price", "BasePrice"));
        outcomes.add(new Outcome("com.freshdirect.fdstore.rules.Adjustment", "Adjustment", "Adjustment"));
    }

    @PostConstruct
    private void init() {
        System.out.println("  --->>> xmlFilePath = " + xmlFilePath);
    }

    @Value("${com.freshdirect.fdstore.rules.misc.location}")
    private String xmlFilePath;

    @Override
    public String subsystemName() {
        return "MISC";
    }

    @Override
    public String serviceName() {
        return "com.freshdirect.fdstore.rule.misc.Engine";
    }

    @Override
    public String xmlFile() {
        return xmlFilePath;
    }
}
