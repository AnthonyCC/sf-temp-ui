package com.freshdirect.rules;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.freshdirect.framework.util.DateRange;

public class Rule implements ConditionI {
	private static final long serialVersionUID = -3251442872254483789L;

	private String id;
	private String name;
	private Date startDate;
	private Date endDate;
	private int priority;
	private List<ConditionI> conditions;
	private Object outcome;
	private String subsystem;
	private String conditionStr;
	private String outcomeStr;

	public Rule() {
		this.conditions = new ArrayList<ConditionI>();
	}

	public Rule(String id, String name, int priority, ConditionI condition) {
		this(id, name, priority, condition, null);
	}

	public Rule(String id, String name, int priority, ConditionI condition, Object outcome) {
		this();
		this.id = id;
		this.name = name;
		this.priority = priority;
		this.conditions.add(condition);
		this.outcome = outcome;
	}

	public Rule(String id, String name, int priority, List<ConditionI> conditions, Object outcome) {
		this.id = id;
		this.name = name;
		this.priority = priority;
		this.conditions = conditions;
		this.outcome = outcome;
	}

	@Override
	public boolean evaluate(Object target, RuleRuntimeI ctx) {
		//Check that the rule has not expired
		DateRange range = new DateRange(this.startDate, this.endDate);
		boolean active = range.contains(new Date());
		if(!active){
			return false;
		}
		boolean r = true;
		for (ConditionI c : this.conditions) {
			r &= c.evaluate(target, ctx);
			if (!r) {
				break;
			}
		}
		return r;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int prioprity) {
		this.priority = prioprity;
	}

	public Object getOutcome() {
		return this.outcome;
	}

	public void setOutcome(Object outcome) {
		this.outcome = outcome;
	}

	public List<ConditionI> getConditions() {
		return this.conditions;
	}

	public void setConditions(List<ConditionI> conditions) {
		this.conditions = conditions;
	}

	public void addCondition(ConditionI condition) {
		this.conditions.add(condition);
	}

	public void removeCondition(int idx) {
		this.conditions.remove(idx);
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getSubsystem() {
		return this.subsystem;
	}

	public void setSubsystem(String subsystem) {
		this.subsystem = subsystem;
	}

	@Override
	public boolean validate() {
		boolean valid = true;
		for (ConditionI c : this.conditions) {
			valid &= c.validate();
		}
		return valid;
	}

	public String toString() {
		return "Rule[" + id + ", '" + name + "', " + priority + ", " + outcome + "]";
	}

	
	public Rule deepCopy() {
        // do a serialization / de-serialization cycle as a trick
        // against explicit deep cloning

        // serialization
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        ObjectOutputStream    oas  = null;
        try {
            oas = new ObjectOutputStream(baos);
            oas.writeObject(this);
            oas.close();
        } catch (IOException e) {
            return null;
        }

        // de-serialization
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream    oin = null;
        try {
            oin = new ObjectInputStream(bais);
            return (Rule) oin.readObject();
        } catch (ClassNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }		
	}

	public String getConditionStr() {
		return conditionStr;
	}

	public void setConditionStr(String conditionStr) {
		this.conditionStr = conditionStr;
	}

	public String getOutcomeStr() {
		return outcomeStr;
	}

	public void setOutcomeStr(String outcomeStr) {
		this.outcomeStr = outcomeStr;
	}
}
