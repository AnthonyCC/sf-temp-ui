<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.framework.webapp.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.fdstore.survey.*' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<%
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setDateHeader ("Expires", 0);
%>

<%
FDSurvey cosFeedback = FDSurveyFactory.getInstance().getSurvey(EnumSurveyType.COS_FEEDBACK_SURVEY, FDSessionUser.getFDSessionUser(session));
List questions = cosFeedback.getQuestions(); 

String successPage = "/checkout/step_1_choose.jsp";

String[] checkSurveyForm = new String[questions.size()];
    int c = 0;
    for(Iterator it = questions.iterator();it.hasNext();c++){
        FDSurveyQuestion question = (FDSurveyQuestion)it.next();
        checkSurveyForm[c] = question.getName();
    }
%>

<fd:CheckLoginStatus id="user" guestAllowed="false" recognizedAllowed="true" /> 

<tmpl:insert template='/common/template/no_space_border.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect At The Office Feedback Survey</tmpl:put>
    <tmpl:put name='content' direct='true'>

<fd:IntermediateSurvey actionName="submitSurvey" result="result" successPage="<%=successPage%>" survey="<%=cosFeedback%>">

<table width="700" cellpadding="0" cellspacing="0" border="0">
<form method="post" name="cosFeedbackSurvey" action="survey_cos.jsp">
<input type="hidden" name="skipSurvey" value="false">
	<tr>
		<td colspan="7" class="text12"><div align="center"><img src="/media_stat/images/template/checkout/survey_hdr_generic.gif" width="230" height="16" border="0" alt="TELL US WHAT YOU THINK" vspace="10"></div>
Now that you have had several orders from FreshDirect At The Office, we'd like to know about your experience. Your direct feedback is the most valuable way for us to ensure that your FreshDirect food shopping experience is the best it can be.
<br><br>
</td>
	</tr>
<tr>
		<td colspan="6" class="text12">
		This survey is completely optional - just click "SKIP SURVEY" to continue Checkout right now.
		</td>
		<td><input type="image" src="/media_stat/images/template/checkout/skip_survey.gif" name="skip_survey" align="right"> </td>
		</tr>
		<fd:ErrorHandler result='<%=result%>' field='<%=checkSurveyForm%>'>
			<% String errorMsg = SystemMessageList.MSG_MISSING_SURVEY_INFO; %>
			<tr>
<td colspan="7" class="text12">
			<br><%@ include file="/includes/i_error_messages.jspf" %><span class="space2pix"><br></span>
</td></tr>
		</fd:ErrorHandler>
<tr>
<td colspan="7" class="text12">
<br><img src="/media_stat/images/layout/999966.gif" width="700" height="1" vspace="2"></td>
	</tr>
	<tr align="center">
		<td><img src="/media_stat/images/layout/clear.gif" width="20" height="8" border="0"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="155" height="8" border="0"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="105" height="8" border="0"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="105" height="8" border="0"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="105" height="8" border="0"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="105" height="8" border="0"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="105" height="8" border="0"></td>
	</tr>

	<%  int quesNum = 0; 
		String currQues = "";
	%>
	<logic:iterate id="question" collection="<%= questions %>" type="com.freshdirect.fdstore.survey.FDSurveyQuestion" indexId='index'>
            <%  String input = "radio";
				String surveyQuestion = "text12";
                if(question.isMultiselect())input = "checkbox";
                
                List prevAnswers = new ArrayList();
                String[] paramValues = request.getParameterValues(question.getName());
                if(paramValues != null)
                    for (int j = 0; j < paramValues.length; j++) {
					prevAnswers.add(paramValues[j]);
				}
			
			if (!currQues.equalsIgnoreCase(question.getName().substring(0,2)) || "".equals(currQues)) {
				currQues = question.getName().substring(0,2);
				quesNum++;
			} 
			
			%>
            <fd:ErrorHandler result='<%=result%>' name='<%=question.getName()%>'>
                    <% surveyQuestion = "text12rbold"; %>
            </fd:ErrorHandler>
			<% if (question.getName().indexOf("_additional") > -1) { %>
					<tr>
						<td></td>
						<td class="text12" align="right">Additional comments&nbsp;&nbsp;</td>
						<td colspan="5"><input type="text" class="text12" style="width:200px;" name="<%=question.getName()%>" value="<%=request.getParameter(question.getName())%>"></td>
					</tr>
					<tr><td colspan="7"><br><br></td></tr>
			<% } else if (question.getName().indexOf("_ques") > -1) { %>
				<tr>
					<td colspan="7" class="text12"><%=quesNum%>. <b><%=question.getDescription()%></b></td>
				</tr>
				<tr><td colspan="7"><img src="/media_stat/images/layout/clear.gif" width="1" height="10" border="0"></td></tr>
					<% if (currQues.equals("q2")) { %>
						<tr align="center">
							<td colspan="2" align="left"></td>
							<td><i>Preferred</i></td>
							<td><i>Acceptable</i></td>
							<td><i>Unacceptable</i></td>
							<td></td>
							<td></td>
						</tr>
					<% } else if (currQues.equals("q5")) { %>
						<tr align="center">
							<td></td>
							<td></td>
							<td><i>Much worse</i></td>
							<td><i>Somewhat worse</i></td>
							<td><i>Same</i></td>
							<td><i>Better</i></td>
							<td><i>Much better</i></td>
						</tr>
					<% } else if (currQues.equals("q6")) { %>
						<tr align="center">
							<td></td>
							<td></td>
							<td><i>1-24%</i></td>
							<td><i>25-49%</i></td>
							<td><i>50-74%</i></td>
							<td><i>75-100%</i></td>
							<td></td>
						</tr>
					<% } %>
			<% } else if (question.getName().indexOf("_hdr") > -1) { 
			%>
				<tr align="center">
					<td></td>
					<td align="left" class="text13" style="font-variant: small-caps"><b><%=question.getDescription()%></b><br><span class="space4pix"><br></span></td>
					<td><i>Not satisfied<br>at all</i></td>
					<td><i>Somewhat dissatisfied</i></td>
					<td><i>Satisfied</i></td>
					<td><i>Very<br>satisfied</i></td>
					<td><i>Extremely<br>satisfied</i></td>
				</tr>
				<% } else if (currQues.equals("q3")) { %>
				<tr><td colspan="7"><br><br></td></tr>
				<tr>
					<td colspan="7" class="text12"><%=quesNum%>. <span class="<%=surveyQuestion%>"><b><%=question.getDescription()%></b></span></td>
				</tr>
				<tr>
					<td></td>
					<td colspan="6">
						<table width="80%" class="text12">
							<tr>
								<logic:iterate id="answer" collection="<%= question.getAnswers() %>" type="com.freshdirect.fdstore.survey.FDSurveyAnswer" indexId="i">
							 	<td width="25%" class="text12"><input type="<%=input%>" name="<%=question.getName()%>" value="<%=answer.getDescription()%>" <%=prevAnswers.contains(answer.getDescription()) ? "CHECKED" : "" %>> <%=answer.getDescription()%></td>
	            				</logic:iterate>
							</tr>
						</table>
					</td>
				</tr>
			<% } else if (currQues.equals("q8")) { 
				if (question.getName().indexOf("_other") < 0) {
					int row = 0;
					int col = 0;
					%>
						<tr><td colspan="7"><br><br></td></tr>
						<tr>
							<td colspan="7" class="text12"><%=quesNum%>. <span class="<%=surveyQuestion%>"><b><%=question.getDescription()%>:</b></span></td>
						</tr>
						<tr><td></td><td colspan="6">
							<table width="100%" cellpadding="0" cellpsacing="0" border="0">
								<logic:iterate id="answer" collection="<%= question.getAnswers() %>" type="com.freshdirect.fdstore.survey.FDSurveyAnswer" indexId="i">
									<% if (col == 0) { %>
										<tr <%= row%2 == 0 ? "bgcolor=\"#EEEEEE\"":""  %>>
									<% } %>
											<td width="33%" <%= "other".equalsIgnoreCase(answer.getDescription()) ? "colspan=\"2\"":"" %> class="text12"><input type="<%=input%>" name="<%=question.getName()%>" value="<%=answer.getDescription()%>" <%=prevAnswers.contains(answer.getDescription()) ? "CHECKED" : "" %>> <%=answer.getDescription()%><% if (!"other".equalsIgnoreCase(answer.getDescription())) { %></td>
											<% col++; %><% } %>
									<% if (col == 3) { 
										row++; 
										col = 0;
										%>
										</tr>
									<% } %>
			             </logic:iterate>
				 <% } else { %>
				 		 <input type="text" class="text12" style="width:200px;" name="<%=question.getName()%>" value="<%=request.getParameter(question.getName())%>"></td>
					</table>
				</td></tr>
				<% } %>
			<% } else if (question.getAnswers().size() > 1) { %>
				<tr align="center" <%= index.intValue()%2==0 ?"bgcolor=\"#EEEEEE\"":""%>>
				<td bgcolor="#FFFFFF"></td>
				<td align="left" class="<%=surveyQuestion%>"><%=question.getDescription()%></td>
	             <logic:iterate id="answer" collection="<%= question.getAnswers() %>" type="com.freshdirect.fdstore.survey.FDSurveyAnswer" indexId="i">
					 <td><input type="<%=input%>" name="<%=question.getName()%>" value="<%=answer.getDescription()%>" <%=prevAnswers.contains(answer.getDescription()) ? "CHECKED" : "" %>></td>
					<% if ((i.intValue() + 1) == question.getAnswers().size() && question.getAnswers().size() < 5 ) { %>
						<td bgcolor="#FFFFFF"></td>
						<% if (5-question.getAnswers().size() == 2) { %>
							<td bgcolor="#FFFFFF"></td>
						<% } %>
					<% } %>
	             </logic:iterate>
				 </tr>
			<% } else { %>
				<tr><td colspan="7"><br><br></td></tr>
				<tr>
					<td colspan="7" class="text12"><%=quesNum%>. <b><%=question.getDescription()%></b></td>
				</tr>
				<tr>
					<td></td>
					<td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="10" border="0"><br><textarea wrap="virtual" cols="60" rows="<%= question.getName().indexOf("recommend") > -1?"4":"3"%>" class="text12" name="<%=question.getName()%>"><%=request.getParameter(question.getName())%></textarea></td>
				</tr>
				<tr><td colspan="7"><br><br></td></tr>
			<% } %>
        </logic:iterate>
		<tr>
		<td colspan="7" align="center" class="text12"><img src="/media_stat/images/layout/999966.gif" width="700" height="1" vspace="10"><br><input type="image" src="/media_stat/images/buttons/survey_submit.gif" width="91" height="21" name="submit_survey" alt="SUBMIT"><br><br>
		
		</td>
	</tr>
</form>
</table>
</fd:IntermediateSurvey>

</tmpl:put>
</tmpl:insert>