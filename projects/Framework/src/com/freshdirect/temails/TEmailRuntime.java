package com.freshdirect.temails;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TEmailRuntime implements TemailRuntimeI {

	private final Map templates;
	private final Map evaluations;
	private final Set evalStack;

	public TEmailRuntime(Map templates) {
		this.templates = templates;
		this.evaluations = new HashMap();
		this.evalStack = new HashSet();
	}

	public String formatTemplate(Object target, Template r) {
		if (evalStack.contains(r.getId())) {
			throw new TEmailRuntimeException("Circular Reference to Template.id: " + r.getId());
		}

		String result = (String) evaluations.get(r.getId());
		if (result == null) {
			evalStack.add(r.getId());
			result=r.parse(target, this);
			//result = new Boolean(r.evaluate(target, this));
			this.evaluations.put(r.getId(), result);
			evalStack.remove(r.getId());
		}

		return result;
	}

	public String getEvaluationResults(String id) {
		return (String) this.evaluations.get(id);
	}

	public Template getTemplate(String id) {
		return (Template) this.templates.get(id);
	}

}