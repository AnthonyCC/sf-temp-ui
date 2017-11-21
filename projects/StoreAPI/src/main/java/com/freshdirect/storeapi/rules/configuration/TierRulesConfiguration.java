package com.freshdirect.storeapi.rules.configuration;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TierRulesConfiguration extends RuleConfiguration {

    public TierRulesConfiguration() {
        conditions = new ArrayList<Condition>();
        outcomes = new ArrayList<Outcome>();

        conditions.add(new Condition("com.freshdirect.storeapi.rules.OgnlCondition", "OGNL Condition", "OgnlCondition"));
        conditions.add(new Condition("com.freshdirect.storeapi.rules.RuleRef", "Rule Reference", "rule-ref"));
        conditions.add(new Condition("com.freshdirect.fdstore.rules.ProfileCondition", "Profile Value", "ProfileCondition"));
        conditions.add(new Condition("com.freshdirect.fdstore.rules.OrderAmount", "Order Amount", "OrderAmount"));
        conditions.add(new Condition("com.freshdirect.fdstore.rules.ServiceTypeCondition", "Service Type", "serviceType"));
        conditions.add(new Condition("com.freshdirect.fdstore.rules.CountyCondition", "County", "County"));
        conditions.add(new Condition("com.freshdirect.fdstore.rules.TierCondition", "Tier", "TierCondition"));

        outcomes.add(new Outcome("com.freshdirect.fdstore.rules.TieredPrice", "Tier Price", "TieredPrice"));
        outcomes.add(new Outcome("com.freshdirect.fdstore.rules.BasePrice", "Base Price", "BasePrice"));
        outcomes.add(new Outcome("com.freshdirect.fdstore.rules.Adjustment", "Adjustment", "Adjustment"));
        outcomes.add(new Outcome("com.freshdirect.fdstore.rules.DlvPremium", "Premium", "DlvPremium"));
    }

    @Value("${com.freshdirect.fdstore.rules.tier.location}")
    private String xmlFile;

    @Override
    public String subsystemName() {
        return "TIER";
    }

    @Override
    public String serviceName() {
        return "com.freshdirect.fdstore.rules.tier.Engine";
    }

    @Override
    public String xmlFile() {
        return xmlFile;
    }

}
