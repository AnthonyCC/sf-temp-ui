package com.freshdirect.fdstore.survey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author ekracoff
 */
public class FDSurveyQuestion implements java.io.Serializable {
	private final String name;
	private final String description;
	private final String shortDescr;
	private List answers = new ArrayList();
	private final boolean multiselect;
	private final boolean required;
	private final boolean openEnded;
	private final boolean rating;
	private final boolean subQuestion;
	private boolean showOptionalText;
	private final boolean pulldown;
	private EnumFormDisplayType formDisplayType;
	private EnumViewDisplayType viewDisplayType;
	
	public FDSurveyQuestion(String name, String description, boolean required, boolean multiselect, boolean openEnded, boolean rating, boolean subQuestion){
		this.name = name;
		this.description = description;
		this.shortDescr=description;
		this.multiselect = multiselect;
		this.required = required;
		this.openEnded = openEnded;
		this.rating = rating;
		this.subQuestion = subQuestion;
		this.showOptionalText = !required;
		this.pulldown=false;
		this.formDisplayType=EnumFormDisplayType.SINGLE_ANS_PER_ROW;
		this.viewDisplayType=EnumViewDisplayType.SINGLE_ANS_PER_ROW;
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

	public FDSurveyQuestion(String name, String description,String shortDescr, boolean required, boolean multiselect, boolean openEnded, boolean rating,boolean pulldown, boolean subQuestion,EnumFormDisplayType formDisplayType,EnumViewDisplayType viewDisplayType) {
		this.name = name;
		this.description = description;
		this.shortDescr=shortDescr;
		this.multiselect = multiselect;
		this.required = required;
		this.openEnded = openEnded;
		this.rating = rating;
		this.subQuestion = subQuestion;
		this.showOptionalText = !required;;
		this.pulldown=pulldown;
		this.formDisplayType=formDisplayType;
		this.viewDisplayType=viewDisplayType;
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

	public boolean isPulldown() {
		return pulldown;
	}

	public EnumFormDisplayType getFormDisplayType() {
		return formDisplayType;
	}
	
	public EnumViewDisplayType getViewDisplayType() {
		return viewDisplayType;
	}
	
	public List getAnswerGroups() {
		List answerGroup=new ArrayList();
		List answers=getAnswers();
		FDSurveyAnswer answer=null;
		for(Iterator it=answers.iterator();it.hasNext();) {
			answer=(FDSurveyAnswer)it.next();
			if(answer.getGroup()!=null && !"".equals(answer.getGroup()) && !answerGroup.contains(answer.getGroup()))
				answerGroup.add(answer.getGroup());
		}
		return answerGroup;
	}
	
	public List getAnswersByGroup(String answerGroup) {
		
		if(answerGroup==null ||"".equals(answerGroup))
			return new ArrayList();
		List answers=getAnswers();
		List filteredList=new ArrayList(2);
		FDSurveyAnswer answer=null;
		for(Iterator it=answers.iterator();it.hasNext();) {
			answer=(FDSurveyAnswer)it.next();
			if(answerGroup.equals(answer.getGroup()))
				filteredList.add(answer);
		}
		return filteredList;
		
	}
	public String getShortDescr() {
		return (shortDescr==null ||"".equals(shortDescr))? description:shortDescr;
	}
	
}
