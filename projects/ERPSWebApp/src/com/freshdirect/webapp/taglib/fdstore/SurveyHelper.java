package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.survey.EnumFormDisplayType;
import com.freshdirect.fdstore.survey.FDSurvey;
import com.freshdirect.fdstore.survey.FDSurveyAnswer;
import com.freshdirect.fdstore.survey.FDSurveyConstants;
import com.freshdirect.fdstore.survey.FDSurveyQuestion;
import com.freshdirect.fdstore.survey.FDSurveyResponse;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;

public class SurveyHelper {

	
	public static FDSurveyResponse getSurveyResponse(FDIdentity identity, FDSurvey survey,ActionResult result, Map responses) {
		
		FDSurveyResponse surveyResponse=new FDSurveyResponse(identity, survey.getName());
		List reqQuests = null;
		reqQuests = survey.getRequiredQuestions();
       	
		for(Iterator i = responses.entrySet().iterator(); i.hasNext();){
			Entry e = (Entry) i.next();
			String question = (String)e.getKey();
			int seperator=question.indexOf(FDSurveyConstants.NAME_SEPERATOR);
			if(seperator!=-1) {
				question=question.substring(0, seperator);
			}
			String[] answers = (String[])e.getValue();
			FDSurveyQuestion q = survey.getQuestion(question);
			if(q==null)continue;
			if(reqQuests.contains(question)) reqQuests.remove(question);
			if (answers.length == 1 && "".equals(answers[0])) continue; //don't create entry for blank open ended questions.
			if(answers.length>1) {
				answers=getSelectedValues(answers);
			}
			if(seperator==-1) 
				surveyResponse.addAnswer(question, answers);
			else
				surveyResponse.addAnswerEx(question, answers);
		}
		for(Iterator i = reqQuests.iterator(); i.hasNext();){
			String question = (String) i.next();
			result.addError(new ActionError(question, SystemMessageList.MSG_REQUIRED));
		}
		return surveyResponse;
	}
	
	public static String[] getSelectedValues(String[] answers) {
		 
		List _answer=new ArrayList(answers.length);
		for(int j=0;j<answers.length;j++) {
			if("".equals(answers[j]))
				continue;
			_answer.add(answers[j]);
		}
		String[] answer=new String[_answer.size()];
		for(int j=0;j<_answer.size();j++) {
			answer[j]=(String)_answer.get(j);
		}
       return answer;
    }
	
	
	public static int getResponseCoverage(FDSurvey survey, FDSurveyResponse response) {
		
		int coverage=0;
		
		if(survey==null || response==null)
			return 0;
		
		if(survey.getQuestions()==null || survey.getQuestions().isEmpty()||response.getAnswers()==null || response.getAnswers().isEmpty())
			return 0;
		
		int responseCount=0;
		FDSurveyQuestion question=null;
		for (Iterator it=survey.getQuestions().iterator();it.hasNext();) {
			question=(FDSurveyQuestion)it.next();
			System.out.println(question.getName());
			if(response.getAnswers().containsKey(question.getName())) {
				//if(!response.getAnswerAsList(question.getName()).isEmpty())
				if(hasActiveAnswers(question,response.getAnswerAsList(question.getName())))
					responseCount++;
			}
		}
		coverage=(int)((responseCount*100)/survey.getQuestions().size());
		return coverage;
		
		
	}
	
	public static boolean hasActiveAnswers(FDSurveyQuestion question, List response) {
		
		if(response==null || response.isEmpty()) return false;
		List answers=question.getAnswers();
		if(answers==null || answers.isEmpty()) return false;
		boolean hasActiveAns=false;
		Iterator it=answers.iterator();
		List ansGroups=question.getAnswerGroups();

		while(!hasActiveAns && it.hasNext()) {
			FDSurveyAnswer _validAns=(FDSurveyAnswer)it.next();//
			if((EnumFormDisplayType.GROUPED_RADIO_BUTTON.equals(question.getFormDisplayType())||EnumFormDisplayType.DISPLAY_PULLDOWN_GROUP.equals(question.getFormDisplayType())) && response.contains(_validAns.getName())) {
				hasActiveAns=true;
			}else if(ansGroups.size()==0 && response.contains(_validAns.getName())) {
				hasActiveAns=true;
			}else if(ansGroups.size()>0){
				Iterator _it=ansGroups.iterator();
				while(!hasActiveAns&& _it.hasNext()) {
					String ansGroup=_it.next().toString();
					if(response.contains(_validAns.getName())||response.contains(_validAns.getName()+ansGroup)) {
						hasActiveAns=true;
					}
				}
			}
		}
		
		return hasActiveAns;
	}
}
