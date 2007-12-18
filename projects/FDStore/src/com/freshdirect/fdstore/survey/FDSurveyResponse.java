package com.freshdirect.fdstore.survey;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.core.PrimaryKey;

public class FDSurveyResponse implements Serializable {

	private final FDIdentity identity;
	private final String name;
	private final Map answers = new HashMap();
	private final PrimaryKey salePk;

	public FDSurveyResponse(FDIdentity identity, String name) {
		this(identity, name, null);
	}

	public FDSurveyResponse(FDIdentity identity, String name, PrimaryKey salePk) {
		this.identity = identity;
		this.name = name;
		this.salePk = salePk;
	}

	public FDIdentity getIdentity() {
		return identity;
	}

	public String getName() {
		return name;
	}

	public PrimaryKey getSalePk() {
		return salePk;
	}

	public void addAnswer(String question, String answer) {
		this.addAnswer(question, new String[] { answer });
	}

	public void addAnswer(String question, String[] answer) {
		this.answers.put(question, answer);
	}

	/** @param map Map of String -> String[] */
	public void setAnswers(Map map) {
		this.answers.clear();
		this.answers.putAll(map);
	}

	/** @return map of String -> String[] */
	public Map getAnswers() {
		return Collections.unmodifiableMap(answers);
	}


}
