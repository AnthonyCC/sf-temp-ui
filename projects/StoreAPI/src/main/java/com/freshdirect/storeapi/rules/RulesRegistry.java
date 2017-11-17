package com.freshdirect.storeapi.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshdirect.storeapi.rules.configuration.Condition;
import com.freshdirect.storeapi.rules.configuration.DlvPremiumRulesConfiguration;
import com.freshdirect.storeapi.rules.configuration.DlvRulesConfiguration;
import com.freshdirect.storeapi.rules.configuration.ECheckRulesConfiguration;
import com.freshdirect.storeapi.rules.configuration.MiscRulesConfiguration;
import com.freshdirect.storeapi.rules.configuration.Outcome;
import com.freshdirect.storeapi.rules.configuration.PromotionRulesConfiguration;
import com.freshdirect.storeapi.rules.configuration.ReferrerRulesConfiguration;
import com.freshdirect.storeapi.rules.configuration.RuleConfiguration;
import com.freshdirect.storeapi.rules.configuration.TierRulesConfiguration;
import com.freshdirect.storeapi.rules.configuration.ZonePromotionRulesConfiguration;

/**
 * @author knadeem Date Apr 4, 2005
 */
@Service
public class RulesRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(RulesRegistry.class);

    private List<RuleConfiguration> configurations = new ArrayList<RuleConfiguration>();

    private Map<String, RulesEngineI> engines = new HashMap<String, RulesEngineI>();

    @Autowired
    private MiscRulesConfiguration miscRulesConfiguration;

    @Autowired
    private DlvRulesConfiguration dlvRulesConfiguration;

    @Autowired
    private DlvPremiumRulesConfiguration dlvPremiumRulesConfiguration;

    @Autowired
    private ECheckRulesConfiguration eCheckRulesConfiguration;

    @Autowired
    private PromotionRulesConfiguration promotionRulesConfiguration;

    @Autowired
    private ZonePromotionRulesConfiguration zonePromotionRulesConfiguration;

    @Autowired
    private ReferrerRulesConfiguration referrerRulesConfiguration;

    @Autowired
    private TierRulesConfiguration tierRulesConfiguration;

    @PostConstruct
    private void initialize() {
        configurations.add(miscRulesConfiguration);
        configurations.add(dlvRulesConfiguration);
        configurations.add(dlvPremiumRulesConfiguration);
        configurations.add(eCheckRulesConfiguration);
        configurations.add(promotionRulesConfiguration);
        configurations.add(zonePromotionRulesConfiguration);
        configurations.add(referrerRulesConfiguration);
        configurations.add(tierRulesConfiguration);

        String subsystemName = miscRulesConfiguration.subsystemName();
        try {
            engines.put(miscRulesConfiguration.subsystemName(), new RulesEngineImpl(new RulesStoreCache(
                    new XMLRulesStore(miscRulesConfiguration.xmlFile(), miscRulesConfiguration.subsystemName(), Arrays.asList(convert(miscRulesConfiguration))), 5)));

            subsystemName = dlvRulesConfiguration.subsystemName();
            engines.put(dlvRulesConfiguration.subsystemName(), new RulesEngineImpl(new RulesStoreCache(
                    new XMLRulesStore(dlvRulesConfiguration.xmlFile(), dlvRulesConfiguration.subsystemName(), Arrays.asList(convert(dlvRulesConfiguration))), 5)));

            subsystemName = dlvPremiumRulesConfiguration.subsystemName();
            engines.put(dlvPremiumRulesConfiguration.subsystemName(), new RulesEngineImpl(new RulesStoreCache(
                    new XMLRulesStore(dlvPremiumRulesConfiguration.xmlFile(), dlvPremiumRulesConfiguration.subsystemName(), Arrays.asList(convert(dlvPremiumRulesConfiguration))),
                    5)));

            subsystemName = eCheckRulesConfiguration.subsystemName();
            engines.put(eCheckRulesConfiguration.subsystemName(), new RulesEngineImpl(new RulesStoreCache(
                    new XMLRulesStore(eCheckRulesConfiguration.xmlFile(), eCheckRulesConfiguration.subsystemName(), Arrays.asList(convert(eCheckRulesConfiguration))), 5)));

            subsystemName = promotionRulesConfiguration.subsystemName();
            engines.put(promotionRulesConfiguration.subsystemName(), new RulesEngineImpl(new RulesStoreCache(
                    new XMLRulesStore(promotionRulesConfiguration.xmlFile(), promotionRulesConfiguration.subsystemName(), Arrays.asList(convert(promotionRulesConfiguration))),
                    5)));

            subsystemName = zonePromotionRulesConfiguration.subsystemName();
            engines.put(zonePromotionRulesConfiguration.subsystemName(), new RulesEngineImpl(new RulesStoreCache(new XMLRulesStore(zonePromotionRulesConfiguration.xmlFile(),
                    zonePromotionRulesConfiguration.subsystemName(), Arrays.asList(convert(zonePromotionRulesConfiguration))))));

            subsystemName = referrerRulesConfiguration.subsystemName();
            engines.put(referrerRulesConfiguration.subsystemName(),
                    new RulesEngineImpl(new RulesStoreCache(
                            new XMLRulesStore(referrerRulesConfiguration.xmlFile(), referrerRulesConfiguration.subsystemName(), Arrays.asList(convert(referrerRulesConfiguration))),
                            5)));

            subsystemName = tierRulesConfiguration.subsystemName();
            engines.put(tierRulesConfiguration.subsystemName(), new RulesEngineImpl(new RulesStoreCache(
                    new XMLRulesStore(tierRulesConfiguration.xmlFile(), tierRulesConfiguration.subsystemName(), Arrays.asList(convert(tierRulesConfiguration))), 5)));

        } catch (ClassNotFoundException e) {
            handleClassNotFoundWhileConvert(e, subsystemName);
        }
    }

    public RulesEngineI getRulesEngine(String subsystem) {
        RulesEngineI engine = null;
        if (engines.containsKey(subsystem)) {
            engine = engines.get(subsystem);
        }
        return engine;
    }

    public RulesConfig getRulesConfig(String subsystem) {
        for (RuleConfiguration config : configurations) {
            if (config.subsystemName().equals(subsystem)) {
                try {
                    return convert(config);
                } catch (ClassNotFoundException e) {
                    handleClassNotFoundWhileConvert(e, subsystem);
                }
            }
        }
        return null;
    }

    /**
     *
     * @return Set of String
     */
    public Set<String> getSubsystems() {
        List<RuleConfiguration> configs = configurations;
        Set<String> s = new HashSet<String>();

        for (RuleConfiguration config : configs) {
            s.add(config.subsystemName());
        }

        return s;
    }

    private RulesConfig convert(RuleConfiguration ruleConfiguration) throws ClassNotFoundException {
        RulesConfig rulesConfig = new RulesConfig();
        rulesConfig.setServiceName(ruleConfiguration.serviceName());
        rulesConfig.setSubsystem(ruleConfiguration.subsystemName());

        for (Outcome outcome : ruleConfiguration.outcomes()) {
            ClassDescriptor classDescriptor = new ClassDescriptor();
            classDescriptor.setLabel(outcome.getLabel());
            classDescriptor.setXmlTag(outcome.getXmlTag());
            classDescriptor.setTargetClass(Class.forName(outcome.getClazz()));

            rulesConfig.addOutcomeType(classDescriptor);
        }

        for (Condition condition : ruleConfiguration.conditions()) {
            ClassDescriptor classDescriptor = new ClassDescriptor();
            classDescriptor.setLabel(condition.getLabel());
            classDescriptor.setXmlTag(condition.getXmlTag());
            classDescriptor.setTargetClass(Class.forName(condition.getClazz()));

            rulesConfig.addConditionType(classDescriptor);
        }

        return rulesConfig;
    }

    private void handleClassNotFoundWhileConvert(ClassNotFoundException exception, String subsystem) {
        LOGGER.error("Class not found! Check your rules configuration for the subsystem: " + subsystem, exception);
        throw new RuntimeException(exception);
    }

}
