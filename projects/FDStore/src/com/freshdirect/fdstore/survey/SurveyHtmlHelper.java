package com.freshdirect.fdstore.survey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.framework.util.StringUtil;

public class SurveyHtmlHelper {
	
	
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
					if(response.contains(_validAns.getName()+ansGroup)) {
						hasActiveAns=true;
					}
				}
			}
		}
		return hasActiveAns;
	}
	
	
	public static String getQuestionText(FDSurveyQuestion quest) {
		
		return quest.isPulldown()? quest.getDescription():quest.isMultiselect()?quest.getDescription()+FDSurveyConstants.MULTIPLE_CHOICE:quest.getDescription()+FDSurveyConstants.SINGLE_CHOICE;
	}
	public static String getAnswers(FDSurveyQuestion question, List answers) {
		
		
		EnumViewDisplayType view=question.getViewDisplayType();
		if ((view==null)||(answers==null)||answers.isEmpty())
			return "";
		
		StringBuffer response=new StringBuffer(200);
		
		if(EnumViewDisplayType.SINGLE_ANS_PER_ROW.equals(view)) {
			for (int i=0;i<answers.size();i++) {
				
				response.append(getRow(getAnsDesc(answers.get(i).toString(),question.getAnswers())));
		    }
			return response.toString();
		} else if(EnumViewDisplayType.NUMBERED_LIST.equals(view)) {
			for (int i=0;i<answers.size();i++) {
				
				response.append(getRow((i+1)+" "+answers.get(i).toString()));
		    }
			return response.toString();
		} else if (EnumViewDisplayType.GROUPED_COMMA_SEPARATED.equals(view)) {
			Collections.sort(answers);
			List ansGroup=question.getAnswerGroups();
			String _ansGroup="";
			for(int i=0;i<ansGroup.size();i++) {
				StringBuffer temp=new StringBuffer(300);
				_ansGroup=ansGroup.get(i).toString();
				//temp.append("<DIV style=\"font-weight:bold\" >"+_ansGroup+": </DIV>");
				temp.append("<b>"+_ansGroup+": </b>");
				String _ans="";
				boolean needsDisplay=false;
				for(int j=0;j<answers.size();j++) {
					_ans=answers.get(j).toString();
					if(_ans.indexOf(_ansGroup)!=-1) {
						temp.append(getAnsDesc(_ans.substring(0, _ans.indexOf(_ansGroup)),question.getAnswers())).append(", ");
						needsDisplay=true;
					}
				}
				if(needsDisplay) {
					response.append(getRow(temp.substring(0, temp.length()-2)));
				}
				
			}
			return response.toString();
		} else if(EnumViewDisplayType.COMMA_SEPARATED.equals(view)) {
			Collections.sort(answers);
			StringBuffer temp=new StringBuffer(300);
			for(int j=0;j<answers.size();j++) {
				temp.append(getAnsDesc(answers.get(j).toString(),question.getAnswers())).append(", ");
			}
			response.append(getRow(temp.substring(0, temp.length()-2)));
			return response.toString();
		} else if(EnumViewDisplayType.GROUPED_LIST.equals(view)) {
			
			List ansGroup=question.getAnswerGroups();
			String _ansGroup="";
			for(int i=0;i<ansGroup.size();i++) {
				StringBuffer temp=new StringBuffer(300);
				_ansGroup=ansGroup.get(i).toString();
				
				temp.append("<b>"+_ansGroup+": </b>");
				String _ans="";
				boolean needsDisplay=false;
				for(int j=0;j<answers.size();j++) {
					_ans=answers.get(j).toString();
					if(_ans.indexOf(_ansGroup)!=-1) {
						temp.append(getAnsDesc(_ans,question.getAnswers())).append(FDSurveyConstants.MEAL_COUNT_SUMMARY);
						needsDisplay=true;
					}
				}
				if(needsDisplay) {
					response.append(getRow(temp.substring(0, temp.length())));
				}
				
			}
			return response.toString();		
		}
		
		return "";
	}
	
	private static String getAnsDesc(String ansId, List answers) {
		
		if (StringUtil.isEmpty(ansId)||answers==null || answers.isEmpty()) return "";
		if(FDSurveyConstants.NONE.equals(ansId)) return FDSurveyConstants.NONE;
		
		boolean match=false;
		Iterator it=answers.iterator();
		String desc="";
		while(!match && it.hasNext()) {
			FDSurveyAnswer ans=(FDSurveyAnswer)it.next();
			if (ansId.equals(ans.getName())) {
				match=true;
				desc=ans.getDescription();
			}
		}
		return desc; 
	}
	private static String getRow(String data) {
		
		StringBuffer response=new StringBuffer(400);
		response.append("<tr><td style=\"padding-bottom: 12px;\">");
		response.append("<div>").append(data).append("</div>");
		response.append("</td>");
		response.append("</tr>");
		return response.toString();
	}

	public static final String getAnswersHtml(String id,FDSurveyQuestion question, List  previousAnswers) {
		
		StringBuffer response = new StringBuffer(200);
		if(previousAnswers==null)
			previousAnswers=new ArrayList();
		if (question.isPulldown()) {
			if(EnumFormDisplayType.DISPLAY_PULLDOWN_GROUP.equals(question.getFormDisplayType())) {
				return getPulldownHtml(question,previousAnswers,true);
			} else {
				return getPulldownHtml(question,previousAnswers,false);
			}
		} else {
			List displayElements = getDisplayElements(id, question,
					previousAnswers);
			
			if (EnumFormDisplayType.SINGLE_ANS_PER_ROW.equals(question
					.getFormDisplayType())) {

				String data = "";
				for (int i = 0; i < displayElements.size(); i++) {
					data = (String) displayElements.get(i);
					response.append(getDivTag(getRowStyle(i + 1), "", data));
				}
			} else if (EnumFormDisplayType.TWO_ANS_PER_ROW.equals(question
					.getFormDisplayType())) {
				String data = "";
				int ansCount = displayElements.size();
				StringBuffer tmp1 = new StringBuffer(200);
				StringBuffer tmp2 = new StringBuffer(200);
				int rowStyle = 1;
				for (int i = 0; i < ansCount; i++) {
					data = (String) displayElements.get(i);
					if (i % 2 == 0)
						tmp1.append(getDivTag(getRowStyle(rowStyle), "", data));
					else {
						tmp2.append(getDivTag(getRowStyle(rowStyle), "", data));
						rowStyle=(rowStyle == 0)?1:0;
					}
				}
				response.append(getDivTag("q05_container col49per", "", tmp1
						.toString()));
				response.append(getDivTag("q05_container col50per", "", tmp2
						.toString()));
				
			} else if(EnumFormDisplayType.GROUPED_RADIO_BUTTON.equals(question.getFormDisplayType())) {
				return getGroupedRadio(question,previousAnswers);
			}else if(EnumFormDisplayType.GROUPED_MULTI_SELECTION.equals(question.getFormDisplayType())) {
				return getGroupedSelection(question,previousAnswers);
			}else if(EnumFormDisplayType.IMAGE_DISPLAY.equals(question.getFormDisplayType())) {
				return getImageDisplay(question,previousAnswers);
			}
		}
		return response.toString();

	}
	
	public static final String getAnswersHtml(String id,
			FDSurveyQuestion question, FDSurveyResponse previousResponse) {

		
		List previousAnswers = getPreviousAnswers(previousResponse, question);
		return getAnswersHtml(id,question, previousAnswers);
	}

    private static String getImageDisplay(FDSurveyQuestion question, List previousAnswers) {
		StringBuffer response=new StringBuffer(200);
		List answers=question.getAnswers();
		FDSurveyAnswer answer=null;
		for(int i=0;i<answers.size();i++) {
			answer=(FDSurveyAnswer)answers.get(i);
			StringBuffer temp=new StringBuffer(200);
			temp.append("<div style=\"width: 110px;\" class=\"rb_image\">");
			temp.append(getDivTag(getImageTag(answer.getDescription())));
			if(previousAnswers.contains(answer.getName())) {
				temp.append(getDivTag(getInputTag(FDSurveyConstants.SINGLE_SELECT_INPUT, question.getName(), "", answer.getName(), true, false, "")));
			} else {
				temp.append(getDivTag(getInputTag(FDSurveyConstants.SINGLE_SELECT_INPUT, question.getName(), "", answer.getName(), false, false, "")));
			}
			temp.append("</div>");
			response.append(getDivTag("q12_container","",temp.toString()));
		}
		
		return response.toString();
	}

    private static String getImageTag(String path) {
    	return "<img src=\""+path+"\"/>";
    }
	private static String getGroupedSelection(FDSurveyQuestion question, List previousAnswers) {
		StringBuffer response=new StringBuffer(200);
		List answerGroups=question.getAnswerGroups();
    	if(answerGroups==null || answerGroups.size()==0)
    		return "";
    	List answers=question.getAnswersByGroup(answerGroups.get(0).toString());
    	response.append(getGroupedSelectionHeader(answerGroups));
    	FDSurveyAnswer answer=null;
		for(int i=0;i<answers.size();i++) {
			StringBuffer temp=new StringBuffer(200);
			answer=(FDSurveyAnswer)answers.get(i);
			temp.append(getDivTag("q08_text","",answer.getDescription()));
			for(int j=0;j<answerGroups.size();j++) {
				String answerGroup=answerGroups.get(j).toString();
				String value = answer.getName()+answerGroup;
				String input = FDSurveyConstants.MULTI_SELECT_INPUT;
				boolean checked = previousAnswers.contains(answer.getName()+answerGroup) ? true: false;
				temp.append(getDivTag("q08_cb","",getInputTag(input, question.getName()+FDSurveyConstants.NAME_SEPERATOR+answerGroup, "", value, checked, false, "")));
			}
			temp.append(getDivTag("cboth","","<!--  -->"));
			response.append(getDivTag(getRowStyle(i + 1),"",temp.toString()));
		}
		return response.toString();
	}

	private static String getGroupedSelectionHeader(List answerGroups) {
		StringBuffer response=new StringBuffer(200);
		response.append(getDivTag("q08_text","","<!-- header -->"));
		for(Iterator it=answerGroups.iterator();it.hasNext();) {
			response.append(getDivTag("q08_cb","",it.next().toString()));
		}
		response.append(getDivTag("cboth","","<!--  -->"));
		return response.toString();
	}

	private static String getGroupedRadio(FDSurveyQuestion question, List previousAnswers) {
    	StringBuffer response=new StringBuffer(200);
    	List answerGroups=question.getAnswerGroups();
    	String group="";
    	int counter=0;
		for(Iterator it=answerGroups.iterator();it.hasNext();) {
			StringBuffer temp=new StringBuffer(200);
			group=it.next().toString();
			List answers=question.getAnswersByGroup(group);
			FDSurveyAnswer answer=null;
			
			for(int i=0;i<answers.size();i++) {
				
				answer=(FDSurveyAnswer)answers.get(i);
				String value = answer.getName();
				boolean checked = previousAnswers.contains(value) ? true: false;
				temp.append(getDivTag("q09_rb","",getInputTag(FDSurveyConstants.SINGLE_SELECT_INPUT, question.getName()+FDSurveyConstants.NAME_SEPERATOR+group, "", value, checked, false, "")));
				temp.append(getDivTag("q09_text","",answer.getDescription()));
				
			}
			String container=(counter%2==0)?"q09_container even":"q09_container odd";
			response.append(getDivTag(container,"",getDivTag("box","",temp.toString())));
			counter++;
		}
		return response.toString();
    }
    
	private static String getPulldownHtml(FDSurveyQuestion question, List previousAnswers, boolean group) {

		StringBuffer response = new StringBuffer(200);
		List answerGroups=question.getAnswerGroups();
		String _group="";
		if(answerGroups!=null && answerGroups.size()>0) {
			for(Iterator it=answerGroups.iterator();it.hasNext();) {
				Object obj=it.next();
				if(obj!=null)
					_group=obj.toString();
				if(group) {
					response.append(getSpanTag(_group)+getDivTag(getSelectTag(question,_group,previousAnswers)));
				} else {				
					response.append(getDivTag(getSelectTag(question,_group,previousAnswers)));
				}
			}
		} else {
			response.append(getDivTag(getSelectTag(question,"",previousAnswers)));
		}
		return response.toString();
	}

	private static String getPulldownHtml(FDSurveyQuestion question,String group, List previousAnswers) {

		StringBuffer response = new StringBuffer(200);
		List answerGroups=question.getAnswerGroups();
		for(Iterator it=answerGroups.iterator();it.hasNext();) {
			response.append(getDivTag(getSelectTag(question,it.next().toString(),previousAnswers)));
		}
		return response.toString();
	}

	private static String getSpanTag(String data) {
		return "<span>"+data+"</span>";
	}
	private static String getSelectTag(FDSurveyQuestion question,String group, List selectedValues) {
		
		StringBuffer response=new StringBuffer(200);
		List answers=null;
		if(!"".equals(group))
			answers=question.getAnswersByGroup(group);
		else
			answers=question.getAnswers();
		response.append("<select name=\"").append(question.getName()).append("\" >");
		response.append("<option value=\"\" selected=\"selected\">Please choose one</option>");
		FDSurveyAnswer answer=null;
		for(int i=0;i<answers.size();i++) {
			
			answer=(FDSurveyAnswer)answers.get(i);
			response.append("<option value=\"").append(answer.getName()).append("\" ");
			if(selectedValues!=null && !selectedValues.isEmpty()&& selectedValues.contains(answer.getName())) {
				response.append(" SELECTED ");
			}
			response.append(">").append(answer.getDescription()).append("</option>");
		}
		response.append("</select>");
		return response.toString();
	}

	private static boolean isNoneOption(String answer) {
		//return (FDSurveyConstants.NONE.equalsIgnoreCase(answer))?true:false;
		return (FDSurveyConstants.NONE.equalsIgnoreCase(answer));
	}

	private static boolean isOtherOption(String answer) {
		//return (FDSurveyConstants.OTHER.equalsIgnoreCase(answer))?true:false;
		return (FDSurveyConstants.OTHER.equalsIgnoreCase(answer));
	}


	private static String getInputTag(String type, String name,
			String styleName, String value, boolean checked, boolean disabled,
			String script) {

		StringBuffer response = new StringBuffer(200);
		response.append("<input ");
		if (!StringUtil.isEmpty(script)) {
			response.append(script);
		}
		if (checked)
			response.append(FDSurveyConstants.CHECKED);
		if (disabled)
			response.append(FDSurveyConstants.DISABLED);
		if (!StringUtil.isEmpty(styleName)) {
			response.append(" class=\"").append(styleName).append("\" ");
		}

		response.append(" type=\"").append(type).append("\" name=\"").append(
				StringUtil.escapeHTML(name)).append("\" value=\"")
				.append(value).append("\" />");
		return response.toString();
	}

	private static String getDivTag(String className, String id, String data) {
		StringBuffer response = new StringBuffer(200);
		response.append("<div");
		if (!StringUtil.isEmpty(className)) {
			response.append(" class=\"").append(className).append("\"");
		}
		if (!StringUtil.isEmpty(id)) {
			response.append(" id=\"").append(id).append("\"");
		}
		response.append(">").append(data).append("</div>");
		return response.toString();
	}

	private static String getDivTag(String data) {

		return "<div>" + data + "</div>";
	}

	private static List getCustomResponse(List previousAnswers,
			Collection probableAnswers) {

		if (previousAnswers.isEmpty())
			return null;
		List validAnswers = new ArrayList(probableAnswers.size());
		for (Iterator it = probableAnswers.iterator(); it.hasNext();) {
			FDSurveyAnswer answer = (FDSurveyAnswer) it.next();
			validAnswers.add(answer.getName());
		}
		List customResponse = new ArrayList();
		String _ans = "";
		for (int i = 0; i < previousAnswers.size(); i++) {
			_ans = previousAnswers.get(i).toString();
			if (!validAnswers.contains(_ans)) {
				customResponse.add(_ans);
			}
		}
		return customResponse;
	}


	private static List getDisplayElements(String id,
			FDSurveyQuestion question, List previousAnswers) {
		List displayElements = new ArrayList(question.getAnswers().size());
		Iterator it = question.getAnswers().iterator();
		FDSurveyAnswer answer = null;

		boolean disable = isNoneOptionSelected(previousAnswers);
		List customResponseList = getCustomResponse(previousAnswers, question
				.getAnswers());
		while (it.hasNext()) {
			answer = (FDSurveyAnswer) it.next();
			String value = answer.getName();
			String input = FDSurveyConstants.SINGLE_SELECT_INPUT;
			boolean checked = previousAnswers.contains(value) ? true: false;
			if (question.isMultiselect()) {
				input = FDSurveyConstants.MULTI_SELECT_INPUT;
			}
			if (isOtherOption(answer.getName())) {
				if (customResponseList != null && !customResponseList.isEmpty())
					value = customResponseList.remove(0).toString();
				else
					value = "";
				displayElements.add(answer.getDescription()
						+ getInputTag(FDSurveyConstants.TEXT_INPUT,
								question.getName(),
								FDSurveyConstants.OTHER_INPUT_STYLE, value,
								false, disable, ""));
			} else if (isNoneOption(answer.getName())) {
				String script = "onclick=\" " + FDSurveyConstants.CLEAR_SCRIPT
						+ "(\'" + id + "\',this.checked)\" ";
				displayElements.add(getDivTag(getInputTag(input, question
						.getName(), FDSurveyConstants.NONE_INPUT_STYLE, value,
						checked, false, script))
						+ answer.getDescription());
			} else {
				displayElements.add(getDivTag(getInputTag(input, question
						.getName(), "", value, checked, disable, ""))
						+ answer.getDescription());
			}
		}
		return displayElements;

	}

	

	private static boolean isNoneOptionSelected(List previousAnswers) {
		return previousAnswers.contains(FDSurveyConstants.NONE);
	}


	private static String getRowStyle(int rowIndex) {
		return (rowIndex % 2 == 0)? FDSurveyConstants.EVEN_ROW_STYLE: FDSurveyConstants.ODD_ROW_STYLE;
	}

	private static List getPreviousAnswers(FDSurveyResponse previousResponse,
			FDSurveyQuestion question) {
		return previousResponse != null?previousResponse.getAnswerAsList(question.getName()):new ArrayList();
	}
}
