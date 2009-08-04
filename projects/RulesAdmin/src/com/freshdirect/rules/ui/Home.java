package com.freshdirect.rules.ui;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.tacos.ajax.components.tree.ITreeManager;
import net.sf.tacos.ajax.components.tree.TreeManager;
import net.sf.tacos.model.IKeyProvider;
import net.sf.tacos.model.ITreeContentProvider;

import org.apache.commons.lang.SerializationUtils;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.valid.IValidationDelegate;

import com.freshdirect.rules.ClassDescriptor;
import com.freshdirect.rules.ConditionI;
import com.freshdirect.rules.Rule;
import com.freshdirect.rules.RulesConfig;
import com.freshdirect.rules.RulesEngineI;
import com.freshdirect.rules.RulesRegistry;

public abstract class Home extends AppPage implements PageDetachListener {

	private boolean updateOutcomeType = false;
	private Class outcomeType;

	public ITreeContentProvider getContentProvider() {
		return new RuleContentProvider(RulesRegistry.getRulesEngine(this.getSelectedSubsystem()));
	}

	public String getRuleIcon() {
		Rule rule = getRule();
		String name = "default";
		if (rule.getOutcome() != null) {
			name = rule.getOutcome().getClass().getName();
			name = name.substring(name.lastIndexOf('.') + 1);
		}
		return "img/icons/" + name + ".gif";
	}

	public IPropertySelectionModel getOutcomeSelectionModel() {
		RulesConfig config = RulesRegistry.getRulesConfig(this.getSelectedSubsystem());
		if(config == null){
			throw new ApplicationRuntimeException("No configuration found for subsyste: "+this.getSelectedSubsystem());
		}
		Map types = new HashMap();
		for(Iterator i = config.getOutcomeTypes().iterator(); i.hasNext(); ){
			ClassDescriptor cd = (ClassDescriptor) i.next();
			types.put(cd.getLabel(), cd.getTargetClass());
		}
		
		return new LabelPropertySelectionModel(types, true);
	}

	public Class getOutcomeType() {
		Object outcome = getSelectedRule().getOutcome();
		return outcome == null ? null : outcome.getClass();
	}

	public void setOutcomeType(Class klazz) {
		Object outcome = getSelectedRule().getOutcome();
		if (klazz == null && outcome == null) {
			return;
		}
		if (klazz != null && outcome != null && klazz.isAssignableFrom(outcome.getClass())) {
			return;
		}
		updateOutcomeType = true;
		outcomeType = klazz;
	}

	public void pageDetached(PageEvent event) {
		updateOutcomeType = false;
		outcomeType = null;
	}

	public void formSubmit(IRequestCycle cycle) {
		if (updateOutcomeType) {
			if (outcomeType == null) {
				getSelectedRule().setOutcome(null);
			} else {
				Object outcome = newInstance(outcomeType);
				getSelectedRule().setOutcome(outcome);
			}
		}
	}

	public IPropertySelectionModel getConditionTypeModel() {
		RulesConfig config = RulesRegistry.getRulesConfig(this.getSelectedSubsystem());
		if(config == null){
			throw new ApplicationRuntimeException("could not find configuration for subsystem: "+this.getSelectedSubsystem());
		}
		Map conditions = new HashMap();
		for(Iterator i = config.getConditionTypes().iterator(); i.hasNext(); ){
			ClassDescriptor cd = (ClassDescriptor) i.next();
			conditions.put(cd.getLabel(), cd.getTargetClass());
			
		}
		return new LabelPropertySelectionModel(conditions, true);
	}

	public void deleteRule(IRequestCycle cycle) {
		Rule r = this.getSelectedRule();
		RulesEngineI engine = RulesRegistry.getRulesEngine(this.getSelectedSubsystem());
		Collection c = engine.getDependentRules(r.getId());
		if (!c.isEmpty()) {
			StringBuffer error = new StringBuffer("Cannot Delete Rule ").append(r.getName());
			error.append(" because following rules have a reference to it : ");
			for(Iterator i = c.iterator(); i.hasNext(); ){
				r = (Rule) i.next();
				error.append(r.getName()).append(" ");
			}
			getDelegate().record(error.toString().trim(), null);
			return;
		}
		engine.deleteRule(this.getSelectedRule().getId());
	}

	public void selectRule(IRequestCycle cycle) {
		String id = (String) cycle.getServiceParameters()[0];
		if (id == null || "".equals(id)) {
			throw new IllegalArgumentException("No id of the selected Rule provided");
		}
		Rule r = RulesRegistry.getRulesEngine(this.getSelectedSubsystem()).getRule(id);
		
		r = r.deepCopy();
		
		setSelectedRule(r);
	}

	public void addCondition(IRequestCycle cycle) {
		Class c = this.getConditionType();
		if (c == null) {
			return;
		}
		ConditionI cond = (ConditionI) newInstance(c);
		this.getSelectedRule().addCondition(cond);
	}

	private Object newInstance(Class klazz) {
		try {
			return klazz.newInstance();
		} catch (InstantiationException e) {
			throw new ApplicationRuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new ApplicationRuntimeException(e);
		}
	}

	public void removeCondition(IRequestCycle cycle) {
		int idx = this.getCurrConditionIndex().intValue();
		this.getSelectedRule().removeCondition(idx);
	}

	private IValidationDelegate getDelegate() {
		return (IValidationDelegate) getBeans().getBean("delegate");
	}

	public void saveRule(IRequestCycle cycle) {
		if (getDelegate().getHasErrors()) {
			return;
		}
		Rule r = getSelectedRule();
		if (!r.validate()) {
			StringBuffer error = new StringBuffer("Rule is not valid");
			this.getDelegate().record(error.toString(), null);
			return;
		}
		RulesRegistry.getRulesEngine(this.getSelectedSubsystem()).storeRule(r);
		setSelectedRule(null);
	}

	public void cancelEdit(IRequestCycle cycle) {
		this.setSelectedRule(null);
	}

	public void expandAll(IRequestCycle cycle) {
		getTreeManager().expandAll();
	}

	public void collapseAll(IRequestCycle cycle) {
		getTreeManager().collapseAll();
	}

	public ITreeManager getTreeManager() {
		System.out.println(">>> Home.getTreeManager()");
		System.out.println(getTreeState());
		System.out.println(getContentProvider());
		System.out.println(getKeyProvider());
		return new TreeManager(getTreeState(), getContentProvider(), getKeyProvider());
	}

	public IKeyProvider getKeyProvider() {
		return RuleKeyProvider.INSTANCE;
	}

	public void revealSelected(IRequestCycle cycle) {
		getTreeManager().reveal(getSelectedRule());
	}

	public String getSelectedSubsystem() {
		RulesAdminVisit visit = (RulesAdminVisit) this.getPage().getVisit();
		return visit.getSubsystem();
	}

	public abstract Set getTreeState();

	public abstract Rule getRule();

	public abstract Rule getSelectedRule();

	public abstract void setSelectedRule(Rule rule);

	public abstract ConditionI getCondition();

	public abstract void setCondition(ConditionI condition);

	public abstract Class getConditionType();

	public abstract void setConditionType(Class klazz);

	public abstract Integer getCurrConditionIndex();

	public abstract void setCurrConditionIndex(Integer idx);

}
