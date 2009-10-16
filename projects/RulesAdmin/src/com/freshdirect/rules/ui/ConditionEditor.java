package com.freshdirect.rules.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.fdstore.rules.ServiceTypeCondition;
import com.freshdirect.rules.ConditionI;
import com.freshdirect.rules.Rule;
import com.freshdirect.rules.RuleRef;
import com.freshdirect.rules.RulesRegistry;


public abstract class ConditionEditor extends BaseComponent {
	
	public IPropertySelectionModel getRuleRefSelectionModel() {
		RulesAdminVisit visit = (RulesAdminVisit) this.getPage().getVisit();
		List rules = new ArrayList(RulesRegistry.getRulesEngine(visit.getSubsystem()).getRules().values());
		Collections.sort(rules, RulesComparator.INSTANCE);
		Map types = new LinkedHashMap();
		for (Iterator i = rules.iterator(); i.hasNext();) {
			Rule r = (Rule) i.next();
			if (!r.getId().equals(this.getSelectedRule().getId())) {
				types.put(r.getName(), r.getId());
			}
		}
		return new LabelPropertySelectionModel(types, true);
	}
	
    public IPropertySelectionModel getServiceTypeSelectionModel() {
        Map types = new HashMap();
        for (EnumServiceType st : EnumServiceType.values()) {
            types.put(st.getName(), st.getName());
        }
        return new LabelPropertySelectionModel(types, true);
    }
	
	public String getCurrServiceType() {
		ServiceTypeCondition st = (ServiceTypeCondition) getCondition();
		return st.getType() ;
	}
	
	public void setCurrServiceType (String serviceType) {
		ServiceTypeCondition st = (ServiceTypeCondition) getCondition();
		st.setType(serviceType);
		this.setCondition(st);
	}

	public String getCurrRuleRefId() {
		RuleRef rr = (RuleRef) getCondition();
		return rr.getId();
	}

	public void setCurrRuleRefId(String id) {
		RuleRef rr = (RuleRef) getCondition();
		rr.setId(id);
	}
	
	public abstract ConditionI getCondition();
	
	public abstract void setCondition(ConditionI condition);
	
	public abstract Rule getSelectedRule();

}
