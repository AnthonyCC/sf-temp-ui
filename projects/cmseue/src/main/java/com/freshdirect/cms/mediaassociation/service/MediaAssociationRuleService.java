package com.freshdirect.cms.mediaassociation.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.freshdirect.cms.mediaassociation.domain.MediaAssociationRule;
import com.google.common.base.Optional;

@Service
public class MediaAssociationRuleService {

    public List<MediaAssociationRule> getRulesByPrefix(String prefix) {

        List<MediaAssociationRule> rulesByPrefix = new ArrayList<MediaAssociationRule>();
        for (MediaAssociationRule rule : MediaAssociationRule.values()) {
            if (rule.getPrefix() != null && prefix != null) {
                if (rule.getPrefix().equals(prefix)) {
                    rulesByPrefix.add(rule);
                }
            } else {
                if (prefix == null && rule.getPrefix() == null) {
                    rulesByPrefix.add(rule);
                }
            }

        }
        return Collections.unmodifiableList(rulesByPrefix);
    }

    public List<MediaAssociationRule> getRulesBySuffix(String suffix) {
        Assert.notNull(suffix, "Suffix parameter can't be null!");

        List<MediaAssociationRule> rulesBySuffix = new ArrayList<MediaAssociationRule>();
        for (MediaAssociationRule rule : MediaAssociationRule.values()) {
            if (rule.getSuffix().equals(suffix)) {
                rulesBySuffix.add(rule);
            }
        }
        return Collections.unmodifiableList(rulesBySuffix);
    }

    public Optional<MediaAssociationRule> getRuleByPrefixAndSuffix(String prefix, String suffix) {
        MediaAssociationRule rule = null;

        for (MediaAssociationRule associationRule : MediaAssociationRule.values()) {
            if (prefix == null) {
                if (associationRule.getPrefix() == null && associationRule.getSuffix().equals(suffix)) {
                    rule = associationRule;
                    break;
                }
            } else {
                if (associationRule.getPrefix().equals(prefix) && associationRule.getSuffix().equals(suffix)) {
                    rule = associationRule;
                    break;
                }
            }
        }

        return Optional.fromNullable(rule);
    }
}
