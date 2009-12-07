package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.survey.FDSurvey;
import com.freshdirect.fdstore.survey.FDSurveyAnswer;
import com.freshdirect.fdstore.survey.FDSurveyConstants;
import com.freshdirect.fdstore.survey.FDSurveyQuestion;
import com.freshdirect.fdstore.survey.FDSurveyResponse;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;

public class SurveyHelper {

	
	public static FDSurveyResponse getSurveyResponse(FDIdentity identity, FDSurvey survey,ActionResult result, Map responses) {
		
		FDSurveyResponse surveyResponse=new FDSurveyResponse(identity, survey.getKey());
		List reqQuests = null;
		reqQuests = survey.getRequiredQuestions();
       	
                for (Iterator i = responses.entrySet().iterator(); i.hasNext();) {
                    Entry e = (Entry) i.next();
                    String question = (String) e.getKey();
                    int seperator = question.indexOf(FDSurveyConstants.NAME_SEPERATOR);
                    if (seperator != -1) {
                        question = question.substring(0, seperator);
                    }
                    String[] answers = (String[]) e.getValue();
                    FDSurveyQuestion q = survey.getQuestion(question);
                    if (q != null) {
                        if (reqQuests.contains(question)) {
                            reqQuests.remove(question);
                        }
                        //if (answers.length == 1 && "".equals(answers[0]))
                        //    continue; // don't create entry for blank open ended
                                      // questions.
                        // if(answers.length>1) {
                        answers = getSelectedValues(q, answers);
                        // }
                        if (seperator == -1) {
                            surveyResponse.addAnswer(question, answers);
                        } else {
                            surveyResponse.addAnswerEx(question, answers);
                        }
                    }
                }
		for(Iterator i = reqQuests.iterator(); i.hasNext();){
			String question = (String) i.next();
			result.addError(new ActionError(question, SystemMessageList.MSG_REQUIRED));
		}
		return surveyResponse;
	}
	
	public static String[] getSelectedValues(FDSurveyQuestion quest,String[] answers) {
		 
		List _answer=new ArrayList(answers.length);
		for(int j=0;j<answers.length;j++) {
			if(answers[j]==null||"".equals(answers[j]))
				continue;
			if(contains(quest.getAnswers(),quest.getAnswerGroups(),answers[j]))
				_answer.add(answers[j]);
		}
		String[] answer=new String[_answer.size()];
		for(int j=0;j<_answer.size();j++) {
			answer[j]=(String)_answer.get(j);
		}
       return answer;
    }
	private static boolean contains(List validAns,List ansGroup, String userSelection) {
		
		if(validAns==null || validAns.isEmpty()) return false;
		
		boolean hasSelection=false;
		int counter=0;
		while(!hasSelection && counter<validAns.size()) {
			FDSurveyAnswer _ans=(FDSurveyAnswer)validAns.get(counter);
			if(ansGroup!=null && !ansGroup.isEmpty()) {
				for(int i=0;i<ansGroup.size();i++) {
					if(userSelection.endsWith(ansGroup.get(i).toString())) {
						userSelection=userSelection.substring(0,userSelection.indexOf(ansGroup.get(i).toString()));
					}
				}
			}
			if(_ans.getName().equals(userSelection)) {
				hasSelection=true;
			} else {
				
			}
			counter++;
		}
		
		return hasSelection;
		
	}
	
}
