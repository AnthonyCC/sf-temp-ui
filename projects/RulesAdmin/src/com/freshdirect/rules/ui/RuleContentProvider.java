package com.freshdirect.rules.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sf.tacos.model.ITreeContentProvider;

import com.freshdirect.rules.ConditionI;
import com.freshdirect.rules.Rule;
import com.freshdirect.rules.RuleRef;
import com.freshdirect.rules.RulesEngineI;

/**
 * @author knadeem Date Mar 28, 2005
 */
public class RuleContentProvider implements ITreeContentProvider {

	private final RulesEngineI manager;

	public RuleContentProvider(RulesEngineI manager) {
		this.manager = manager;
	}

	public List getElements() {
		List rules = new ArrayList(manager.getRules().values());

		for (ListIterator i = rules.listIterator(); i.hasNext();) {
			Rule r = (Rule) i.next();
			for (Iterator j = r.getConditions().iterator(); j.hasNext();) {
				ConditionI c = (ConditionI) j.next();
				if (c instanceof RuleRef) {
					i.remove();
					break;
				}
			}
		}
		return rules;
	}

	public Collection getChildren(Object element) {
		Rule rule = (Rule) element;
		Collection dependentRules = manager.getDependentRules(rule.getId());
		return dependentRules;
	}

	public boolean hasChildren(Object element) {
		return !getChildren(element).isEmpty();
	}

	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

}
