package com.freshdirect.cms.contentvalidation.domain;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.core.domain.Attribute;

public class ConditionalValidatorFieldRules {

    private List<Attribute> requiredFields = new ArrayList<Attribute>();
    private List<Attribute> optionalFields = new ArrayList<Attribute>();
    private List<List<Attribute>> requiredFieldGroups = new ArrayList<List<Attribute>>(); // at least one is required from one group
    private List<Attribute> usedFields = new ArrayList<Attribute>(); // automatically maintained collection of declared fields

    public ConditionalValidatorFieldRules() {
    }

    public ConditionalValidatorFieldRules(ConditionalValidatorFieldRules source) {
        this.requiredFields = new ArrayList<Attribute>(source.requiredFields);
        this.optionalFields = new ArrayList<Attribute>(source.optionalFields);
        this.requiredFieldGroups = new ArrayList<List<Attribute>>(source.requiredFieldGroups);
        this.usedFields = new ArrayList<Attribute>(source.usedFields);
    }

    public List<Attribute> getRequiredFields() {
        return requiredFields;
    }

    public List<Attribute> getOptionalFields() {
        return optionalFields;
    }

    public List<List<Attribute>> getRequiredFieldGroups() {
        return requiredFieldGroups;
    }

    public List<Attribute> getUsedFields() {
        return usedFields;
    }

    public ConditionalValidatorFieldRules addRequired(Attribute field) {
        requiredFields.add(field);
        usedFields.add(field);
        return this;
    }

    public ConditionalValidatorFieldRules addOptional(Attribute field) {
        optionalFields.add(field);
        usedFields.add(field);
        return this;
    }

    public ConditionalValidatorFieldRules addRequiredGroup(List<Attribute> requiredGroup) {
        requiredFieldGroups.add(requiredGroup);
        usedFields.addAll(requiredGroup);
        return this;
    }
}
