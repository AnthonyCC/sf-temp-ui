package com.freshdirect.fdstore.survey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * @author ekracoff
 */
public class FDSurveyQuestion {
	private final String name;
	private final String description;
	private List answers = new ArrayList();
	private final boolean multiselect;
	private final boolean required;
	private final boolean openEnded;
	private final boolean rating;
	private final boolean subQuestion;
	private boolean showOptionalText;
	
	public FDSurveyQuestion(String name, String description, boolean required, boolean multiselect, boolean openEnded, boolean rating, boolean subQuestion){
		this.name = name;
		this.description = description;
		this.multiselect = multiselect;
		this.required = required;
		this.openEnded = openEnded;
		this.rating = rating;
		this.subQuestion = subQuestion;
		this.showOptionalText = !required;
	}
	
	public FDSurveyQuestion(String name, String description, boolean required, boolean multiselect, boolean openEnded, boolean rating){
		this(name, description, required, multiselect, openEnded, rating, false);
	}
	
	public FDSurveyQuestion(String name, String description, boolean required, boolean multiselect, boolean openEnded){
		this(name, description, required, multiselect, openEnded, false, false);
	}
	
	public FDSurveyQuestion(String name, String description, boolean required, boolean multiselect){
		this(name, description, required, multiselect, false, false, false);
	}

	public void addAnswer(FDSurveyAnswer answer){
		answers.add(answer);
	}

	public List getAnswers() {
		return Collections.unmodifiableList(answers);
	}
	
	public void setAnswers(List answers){
		this.answers = answers;
	}
	
	public void setShowOptionalText(boolean showOptionalText) {
		this.showOptionalText = showOptionalText;
	}
	
	public boolean isShowOptionalText() {
		return showOptionalText;
	}
	
//	public FDSurveyAnswer createAnswer(String name, String description){
//		FDSurveyAnswer answer = new FDSurveyAnswer(name, description);
//		this.addAnswer(answer);
//		return answer;
//	}

	public String getDescription() {
		return description;
	}

	public boolean isMultiselect() {
		return multiselect;
	}

	public boolean isRating() {
		return rating;
	}
	
	public boolean isSubQuestion() {
		return subQuestion;
	}
	
	public String getName() {
		return name;
	}

	public boolean isRequired() {
		return required;
	}
	
	public boolean isOpenEnded(){
		return openEnded;
	}
	
	public void sortAnswers(Comparator c){
		Collections.sort(answers, c);
	}
	
	public void sortAnswers(){
		this.sortAnswers(ANSWER_COMAPARATOR);
	}
	
	public boolean isValidAnswer(String[] answer) {

		if (this.required && answer.length==0) {
			return false;
		}
		if (!this.multiselect && answer.length>1){
			return false;
		}
		
		//check to see if answer is an expected answer
		if(!this.openEnded){
			HashSet names = new HashSet();
			for (Iterator i=this.answers.iterator(); i.hasNext(); ) {
				names.add( ((FDSurveyAnswer)i.next()).getName() );
			}
	
			for (int i=0; i<answer.length; i++) {
				if (!names.remove(answer[i])) {
					return false;
				}
			}
		}
			
		return true;
		
	}
	
	public final static Comparator ANSWER_COMAPARATOR = new Comparator() {
		public int compare(Object obj1, Object obj2) {
			FDSurveyAnswer an1 = (FDSurveyAnswer) obj1;
			FDSurveyAnswer an2 = (FDSurveyAnswer) obj2;
			return an1.getDescription().compareTo(an2.getDescription());
		}
	};
	
}
