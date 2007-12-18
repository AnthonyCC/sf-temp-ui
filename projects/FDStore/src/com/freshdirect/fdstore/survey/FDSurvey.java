/**
 * @author ekracoff
 * Created on Nov 19, 2003
 */
package com.freshdirect.fdstore.survey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class FDSurvey {
	private final String name;
	private final boolean isOrderSurvey;  //property tells whether or not an order number is required
	private final List questions = new ArrayList();

	public FDSurvey(String name, boolean isOrderSurvey) {
		this.name = name;
		this.isOrderSurvey = isOrderSurvey;
	}
	
	public FDSurvey(String name) {
		this(name, false);
	}
	
	public String getName(){
		return name;
	}
	
	public boolean isOrderSurvey() {
		return isOrderSurvey;
	}
	
	public void addQuestion(FDSurveyQuestion question) {
		questions.add(question);
	}

	public List getQuestions() {
		return Collections.unmodifiableList(questions);
	}


//	/** create a required, single-select question */
//	public FDSurveyQuestion createQuestion(String name, String description) {
//		return this.createQuestion(name, description, true);
//	}
//
//	/** create a single-select question */
//	public FDSurveyQuestion createQuestion(String name, String description, boolean required) {
//		return this.createQuestion(name, description, required, false);
//	}
//	
//	public FDSurveyQuestion createQuestion(String name, String description, boolean required, boolean multiSelect) {
//		FDSurveyQuestion q = new FDSurveyQuestion(name, description, required, multiSelect);
//		this.addQuestion(q);
//		return q;
//	}

	public FDSurveyQuestion getQuestion(String question) {
		for(Iterator i = this.questions.iterator(); i.hasNext();){
			FDSurveyQuestion q = (FDSurveyQuestion) i.next();
			if(q.getName().equals(question)){
				return q;
			}
		}
		return null;
	}
	
	public List getRequiredQuestions(){
		List requiredQuests = new ArrayList();
		for(Iterator i = this.questions.iterator(); i.hasNext();){
			FDSurveyQuestion question = (FDSurveyQuestion) i.next();
			if(question.isRequired())
				requiredQuests.add(question.getName());
		}
		
		return requiredQuests;
	}

}
