package com.freshdirect.storeapi.rules.configuration;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PromotionRulesConfiguration extends RuleConfiguration {

    public PromotionRulesConfiguration() {
        conditions = new ArrayList<Condition>();
        outcomes = new ArrayList<Outcome>();

        conditions.add(new Condition("com.freshdirect.storeapi.rules.OgnlCondition", "OGNL Condition", "OgnlCondition"));
        conditions.add(new Condition("com.freshdirect.storeapi.rules.RuleRef", "Rule Reference", "rule-ref"));

        outcomes.add(new Outcome("java.lang.String", "Promo Code", "PromoCode"));
    }

    @Value("${com.freshdirect.fdstore.rules.promotion.location}")
    private String xmlFile;

    @Override
    public String subsystemName() {
        return "PROMOTION";
    }

    @Override
    public String serviceName() {
        return "com.freshdirect.fdstore.rules.promotion.Engine";
    }

    @Override
    public String xmlFile() {
        return xmlFile;
    }

}
