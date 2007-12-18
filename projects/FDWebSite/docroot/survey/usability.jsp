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

<%
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setDateHeader ("Expires", 0);
%>

<fd:CheckLoginStatus id="user" guestAllowed="false" recognizedAllowed="false" /> 

<% 
	boolean submitted = "thankyou".equalsIgnoreCase(request.getParameter("info"));
	boolean hasTaken = false;
	FDCustomerModel customer = FDCustomerFactory.getFDCustomer(user.getIdentity());
	if ("FILL".equals(customer.getProfile().getAttribute("Usability"))) {
		if (!"thankyou".equalsIgnoreCase(request.getParameter("info"))) hasTaken = true;
		submitted = true;
	}
	
	FDSurvey Usability = FDSurveyFactory.getInstance().getSurvey("Usability");
    List questions = Usability.getQuestions();
	
   String[] checkSurveyForm = new String[questions.size()];
   
   if (questions.size() > 0) {
	   int c = 0;
	   for(Iterator it = questions.iterator();it.hasNext();c++){
	       FDSurveyQuestion question = (FDSurveyQuestion)it.next();
	       checkSurveyForm[c] = question.getName();
	    }
	}
%>

<tmpl:insert template='/common/template/no_space_border.jsp'>
    <tmpl:put name='title' direct='true'>Tell us what you think of the site</tmpl:put>
    <tmpl:put name='content' direct='true'>
<table width="700" cellpadding="0" cellspacing="0" border="0" class="text12">
<% if (submitted) {%>
<tr>
	<td colspan="10" class="text12" align="center"><br>
	<span class="title18">Thank you for your feedback.</span><br><%= hasTaken?"Your information has been submitted.<br>":""%>We greatly appreciate your time and interest.<br>
<br><a href="/index.jsp"><img src="/media_stat/images/template/help/help_home.gif" width="71" height="26" border="" alt="BACK HOME"></a><br>Go to <a href="/index.jsp">Home Page</a><br><br></td></tr>
<% } else { %>
<fd:ReceiptSurvey actionName="submitSurvey" result="result" successPage="/survey/usability.jsp?info=thankyou" survey="<%=Usability%>">
<form name="usabilitySurvey" method="POST">
	<tr>
		<td colspan="10" class="text12"><br><span class="title18"><span style="color:#6699CC;">Tell us what you think of the site!</span></span><br><span class="space4pix"><br></span>
We're always looking for ways to make your shopping experience better and your answers to this survey will help us to do so. It should take less than 5 minutes of your time. No one knows the FreshDirect web site as well as our customers, and we value your expert opinion highly. Of course, in accordance with the FreshDirect <a href="javascript:popup('/help/privacy_policy.jsp?type=popup','large')">Privacy Policy</a>, all of your answers will be kept confidential. Thank you in advance for your feedback &mdash; it's greatly appreciated.
<br><br>
If you'd rather not take this survey now, <a href="/index.jsp">click here</a>.
<br><img src="/media_stat/images/layout/999966.gif" width="700" height="1" vspace="8"><br>
		<%  if (questions.size() > 0) { %>
			<fd:ErrorHandler result='<%=result%>' field='<%=checkSurveyForm%>'>
				<% String errorMsg = SystemMessageList.MSG_MISSING_SURVEY_INFO; %>
				<br><%@ include file="/includes/i_error_messages.jspf" %><span class="space8pix"><br></span>
			</fd:ErrorHandler>
		<% } %>
		</td>
	</tr>
	<tr>
		<td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="140" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="120" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="170" height="1"></td>
	</tr>
	<tr>
		<td colspan="10">
		</td>
	</tr>
	<% int quesCount = 0; %>
	    <logic:iterate id="question" collection="<%= questions %>" type="com.freshdirect.fdstore.survey.FDSurveyQuestion" indexId='index'>
            <% 
				String input = "radio";
				//String surveyQuestion = subQuestion? "text12":"text13";
				String surveyQuestion = "text13";
                if(question.isMultiselect())input = "checkbox";
                
                List prevAnswers = new ArrayList();
                String[] paramValues = request.getParameterValues(question.getName());
                if(paramValues != null)
                    for (int j = 0; j < paramValues.length; j++) {
					prevAnswers.add(paramValues[j]);
				}
            %>
            <fd:ErrorHandler result='<%=result%>' name='<%=question.getName()%>'>
                    <% surveyQuestion = "text13rbold"; %>
            </fd:ErrorHandler>
			<% if ("other".equalsIgnoreCase(question.getDescription())) { %>
				<input type="text" name="<%=question.getName()%>" value="<%=request.getParameter(question.getName())%>"></td></tr>
			<% } else if (question.isRating()) { %>
				<% if (!question.isSubQuestion()) { quesCount++; %>
					<tr><td colspan="10"
				<% } else { %>
					<tr><td></td><td colspan="9"
				<% } %>
				 class="<%=surveyQuestion%>"><%=quesCount > 1 ? "<br><br>": ""%><b><%= !question.isSubQuestion() ? quesCount +". " : ""%><%=question.getDescription()%></b> <%= "checkbox".equalsIgnoreCase(input) ? "(check all that apply)" : ""%><span class="space4pix"><br><br></span></td></tr>
			<% } else { %>
				<% quesCount++; %>
	            <tr><td colspan="10" class="<%=surveyQuestion%>"><%=quesCount > 1 ? "<br><br>": ""%><b><%=quesCount%>. <%=question.getDescription()%></b> <%= "checkbox".equalsIgnoreCase(input) ? "(check all that apply)" : ""%><span class="space4pix"><br><br></span></td></tr>
				<% if (question.isOpenEnded()) { //textarea %>
					<tr>
						<td></td>
						<td colspan="9" class="text13"><textarea wrap="virtual" cols="80" rows="4" class="text13" name="<%=question.getName()%>"><%=request.getParameter(question.getName())%></textarea></td>
					</tr>
				<% } %>
			<% } %>
				<% int numCol = 0; %>
				<logic:iterate id="answer" collection="<%= question.getAnswers() %>" type="com.freshdirect.fdstore.survey.FDSurveyAnswer" indexId="i">
				<% if (question.isRating() && question.getAnswers().size() > 5) { //rating style w n/a %>
					<% if (numCol == 0) { %>
					<tr>
					    <td colspan="3" align="right" class="text13"><% if (question.getDescription().indexOf("useful") > -1) { %>Not at all useful<% } else { %>Very difficult<% } %></td>
					<% numCol = 3;
					} %>
					    <td <%= numCol != 9 ? "align=\"center\"" : ""%>><input type="<%=input%>" name="<%=question.getName()%>" value="<%=answer.getName()%>" <%=prevAnswers.contains(answer.getName()) ? "CHECKED" : "" %>></td>
						<% numCol++; %>
					<% if (numCol == 8) { %>
					    <td class="text13"><% if (question.getDescription().indexOf("useful") > -1) { %>Very useful<% } else { %>Very easy<% } %></td>
						<% numCol ++; %>
					<% } else if (numCol == 10) { %>
					  </tr>
					  <tr align="center"><td colspan="3"></td><td class="text12">1</td><td class="text12">2</td><td class="text12">3</td><td class="text12">4</td><td class="text12">5</td><td></td><td class="text12" align="left">n/a</td></tr>
					<% } %>
				
				<% } else if (question.isRating() && question.getAnswers().size() == 5) { //rating style %>
					<% if (numCol == 0) { %>
					<tr>
					    <td colspan="3" align="right" class="text13"><% if (question.getDescription().indexOf("useful") > -1) { %>Not at all useful<% } else { %>Very difficult<% } %></td>
					<% numCol = 3;
					} %>
					    <td align="center"><input type="<%=input%>" name="<%=question.getName()%>" value="<%=answer.getName()%>" <%=prevAnswers.contains(answer.getName()) ? "CHECKED" : "" %>></td>
						<% numCol++; %>
					<% if (numCol == 8) { %>
					    <td class="text13"><% if (question.getDescription().indexOf("useful") > -1) { %>Very useful<% } else { %>Very easy<% } %></td>
					  </tr>
					  <tr align="center"><td colspan="3"></td><td class="text12">1</td><td class="text12">2</td><td class="text12">3</td><td class="text12">4</td><td class="text12">5</td><td colspan="2"></td></tr>
					<% } %>
				
				<% } else { //standard %>
						<tr><td colspan="3"></td><td><input type="<%=input%>" name="<%=question.getName()%>" value="<%=answer.getDescription()%>" <%=prevAnswers.contains(answer.getDescription()) ? "CHECKED" : "" %>></td><td colspan="6" class="text13"><%=answer.getDescription()%>
						<% if (!"other".equalsIgnoreCase(answer.getDescription())) { %>
							</td></tr>
						<% } %>
				<% } %>
        		</logic:iterate>
		</logic:iterate>
	<tr><td colspan="10" align="center"><img src="/media_stat/images/layout/999966.gif" width="700" height="1" vspace="12"><br><input type="image" src="/media_stat/images/buttons/survey_submit.gif" width="91" height="21" onClick="usabilitySurvey.submit()" alt="SUBMIT"><br><br><br></td></tr>
</form>
</fd:ReceiptSurvey>
<% } %>
</table>
</tmpl:put>
</tmpl:insert>