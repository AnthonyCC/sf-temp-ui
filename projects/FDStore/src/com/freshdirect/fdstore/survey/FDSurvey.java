/**
 * @author ekracoff
 * Created on Nov 19, 2003
 */
package com.freshdirect.fdstore.survey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FDSurvey implements java.io.Serializable {
	private final String name;
	private final boolean isOrderSurvey; // property tells whether or not an order number is required
	private final List<FDSurveyQuestion> questions = new ArrayList<FDSurveyQuestion>();
	private final SurveyKey key;

	private int acceptableCoverage;

	public int getAcceptableCoverage() {
		return acceptableCoverage;
	}

	@Deprecated
	public FDSurvey(String name, boolean isOrderSurvey) {
		this.key = null;
		this.name = name;
		this.isOrderSurvey = isOrderSurvey;
		this.acceptableCoverage = 0;
	}

	@Deprecated
	public FDSurvey(String name) {
		this(name, false);
	}

	public FDSurvey(EnumSurveyType type, boolean isOrderSurvey) {
		this.key = new SurveyKey(type, null);
		this.name = type.getLabel();
		this.isOrderSurvey = isOrderSurvey;
		this.acceptableCoverage = 0;
	}

	public FDSurvey(EnumSurveyType type) {
		this(type, false);
	}

	public FDSurvey(SurveyKey key, boolean isOrderSurvey) {
		this.key = key;
		this.name = key.getSurveyType().getLabel();
		this.isOrderSurvey = isOrderSurvey;
		this.acceptableCoverage = 0;
	}

	public FDSurvey(@JsonProperty("key") SurveyKey key, @JsonProperty("orderSurvey") boolean orderSurvey,
			@JsonProperty("acceptableCoverage") int acceptableCoverage) {
		this.key = key;
		this.name = key.getSurveyType().getLabel();
		this.isOrderSurvey = orderSurvey;
		this.acceptableCoverage = acceptableCoverage;
	}

	public FDSurvey(SurveyKey key) {
		this(key, false);
	}

	public String getName() {
		return name;
	}

	public boolean isOrderSurvey() {
		return isOrderSurvey;
	}

	public void addQuestion(FDSurveyQuestion question) {
		questions.add(question);
	}

	public List<FDSurveyQuestion> getQuestions() {
		return Collections.unmodifiableList(questions);
	}

	// /** create a required, single-select question */
	// public FDSurveyQuestion createQuestion(String name, String description) {
	// return this.createQuestion(name, description, true);
	// }
	//
	// /** create a single-select question */
	// public FDSurveyQuestion createQuestion(String name, String description,
	// boolean required) {
	// return this.createQuestion(name, description, required, false);
	// }
	//
	// public FDSurveyQuestion createQuestion(String name, String description,
	// boolean required, boolean multiSelect) {
	// FDSurveyQuestion q = new FDSurveyQuestion(name, description, required,
	// multiSelect);
	// this.addQuestion(q);
	// return q;
	// }

	public FDSurveyQuestion getQuestion(String question) {
		for (Iterator<FDSurveyQuestion> i = this.questions.iterator(); i.hasNext();) {
			FDSurveyQuestion q = i.next();
			if (q.getName().equals(question)) {
				return q;
			}
		}
		return null;
	}
	@JsonIgnore
	public List<String> getRequiredQuestions() {
		List<String> requiredQuests = new ArrayList<String>();
		for (Iterator<FDSurveyQuestion> i = this.questions.iterator(); i.hasNext();) {
			FDSurveyQuestion question = i.next();
			if (question.isRequired())
				requiredQuests.add(question.getName());
		}

		return requiredQuests;
	}

	public SurveyKey getKey() {
		return key;
	}

	@JsonIgnore
	public String getHtmlFriendlyKey() {
		if (key != null) {
			return key.getSurveyType().name().toLowerCase() + '_' + key.getUserType().name().toLowerCase();
		} else {
			return name.replace(' ', '_').toLowerCase();
		}
	}

	public String toSqlString(int id) {
		String def = "INSERT INTO CUST.SURVEY_DEF (ID,NAME,DESCR,CREATE_DATE,IS_ORDER_SURVEY,IS_CUSTOMER_PROFILE_SURVEY,MIN_COVERAGE,SERVICE_TYPE) values ("
				+ id + ',' + escape(key.surveyType.getLabel()) + ',' + escape(name) + ",SYSDATE,NULL,'Y', NULL, "
				+ escape(key.getUserType().name()) + ");\n";

		StringBuilder s = new StringBuilder(def);
		int sequence = 1;
		for (Iterator<FDSurveyQuestion> i = this.questions.iterator(); i.hasNext();) {
			FDSurveyQuestion q = i.next();
			s.append("\n").append(q.toSqlInserts());
			s.append('\n');
			s.append('\n').append(
					"insert into CUST.SURVEY_SETUP (SURVEY,SEQUENCE,ACTIVE,QUESTION, ID, DATE_MODIFIED) values (" + id
							+ "," + sequence + ",'Y'," + q.toSelectSqlId()
							+ ",(select max(to_number(id))+1 from CUST.SURVEY_SETUP),SYSDATE);");
			s.append('\n');
			sequence++;
		}
		return s.toString();
	}

	private static String escape(String name2) {
		return "'" + name2 + '\'';
	}

}
