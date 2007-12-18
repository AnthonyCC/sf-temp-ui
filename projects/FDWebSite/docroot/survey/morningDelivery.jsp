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
	if ("FILL".equals(customer.getProfile().getAttribute("MorningDelivery"))) {
		if (!"thankyou".equalsIgnoreCase(request.getParameter("info"))) hasTaken = true;
		submitted = true;
	}
	
	FDSurvey MorningDelivery = FDSurveyFactory.getInstance().getSurvey("MorningDelivery");
    List questions = MorningDelivery.getQuestions();
	
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
    <tmpl:put name='title' direct='true'>Morning Delivery Survey</tmpl:put>
    <tmpl:put name='content' direct='true'>
<table width="700" cellpadding="0" cellspacing="0" border="0" class="text12">
<% if (submitted) {%>
<tr>
	<td colspan="11" class="text12" align="center"><br>
	<span class="title18">Thank you for your feedback.</span><br><%= hasTaken?"Your information has been submitted.<br>":""%>We greatly appreciate your time and interest.<br>
<br><a href="/index.jsp"><img src="/media_stat/images/template/help/help_home.gif" width="71" height="26" border="" alt="BACK HOME"></a><br>Go to <a href="/index.jsp">Home Page</a><br><br></td></tr>
<% } else { %>
<fd:ReceiptSurvey actionName="submitSurvey" result="result" successPage="/survey/morningDelivery.jsp?info=thankyou" survey="<%=MorningDelivery%>">
<form name="morningDeliverySurvey" method="POST">
	<tr>
		<td colspan="11" class="text12"><br><span class="title18">Morning Delivery Survey</span><br><span class="space4pix"><br></span>
		FreshDirect knows that our customers love our service and the fresh food we deliver. We are currently researching the concept of expanding our delivery windows to weekday mornings. Your responses to this brief survey will help us understand our customers' needs. Of course, in accordance with the FreshDirect Privacy Policy, all of your answers will be kept confidential.
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
		<td><img src="/media_stat/images/layout/clear.gif" width="25" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="25" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="110" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="50" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="50" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="50" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="50" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="50" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="50" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="50" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="150" height="1"></td>
	</tr>
	<tr>
		<td colspan="11">
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
					<tr><td colspan="11"
				<% } else { %>
					<tr><td></td><td colspan="10"
				<% } %>
				 class="<%=surveyQuestion%>"><%=quesCount > 1 ? "<br><br>": ""%><b><%= !question.isSubQuestion() ? quesCount +". " : ""%><%=question.getDescription()%></b> <%= "checkbox".equalsIgnoreCase(input) ? "(check all that apply)" : ""%><span class="space4pix"><br><br></span></td></tr>
			<% } else { %>
				<% quesCount++; %>
	            <tr><td colspan="11" class="<%=surveyQuestion%>"><%=quesCount > 1 ? "<br><br>": ""%><b><%=quesCount%>. <%=question.getDescription()%></b> <%= "checkbox".equalsIgnoreCase(input) ? "(check all that apply)" : ""%><span class="space4pix"><br><br></span></td></tr>
				<% if (question.isOpenEnded()) { //textarea %>
					<tr>
						<td></td>
						<td colspan="10" class="text13"><textarea wrap="virtual" cols="80" rows="4" class="text13" name="<%=question.getName()%>"><%=request.getParameter(question.getName())%></textarea>
					<br><span class="space8pix"><br><br></span></td>
					</tr>
				<% } %>
			<% } %>
				<% int numCol = 0; %>
				<logic:iterate id="answer" collection="<%= question.getAnswers() %>" type="com.freshdirect.fdstore.survey.FDSurveyAnswer" indexId="i">
				<% if (question.isRating()) { //rating style %>
					<% if (numCol == 0) { %>
					<tr>
					    <td colspan="3" align="right" class="text13"><% if (question.getDescription().indexOf("frequently") > -1) { %>More frequently<% } else if (question.getDescription().indexOf("food") > -1) { %>More food<% } else { %>Very likely<% } %></td>
					<% numCol = 3;
					} %>
					    <td align="center"><input type="<%=input%>" name="<%=question.getName()%>" value="<%=answer.getName()%>" <%=prevAnswers.contains(answer.getName()) ? "CHECKED" : "" %>></td>
						<% numCol++; %>
					<% if (numCol == 8) { %>
					    <td colspan="3"  class="text13"><% if (question.getDescription().indexOf("frequently") > -1) { %>Less frequently<% } else if (question.getDescription().indexOf("food") > -1) { %>Less food<% } else { %>Very unlikely<% } %> </td>
					  </tr>
					  <tr align="center"><td colspan="3"></td><td class="text12">1</td><td class="text12">2</td><td class="text12">3</td><td class="text12">4</td><td class="text12">5</td><td colspan="3"></td></tr>
					<% } %>
				
				<% } else { //standard %>
						<tr><td></td><td><input type="<%=input%>" name="<%=question.getName()%>" value="<%=answer.getDescription()%>" <%=prevAnswers.contains(answer.getDescription()) ? "CHECKED" : "" %>></td><td colspan="9" class="text13"><%=answer.getDescription()%>
						<% if (!"other".equalsIgnoreCase(answer.getDescription())) { %>
							</td></tr>
						<% } %>
				<% } %>
        		</logic:iterate>
		</logic:iterate>
	<tr><td colspan="11" align="center"><img src="/media_stat/images/layout/999966.gif" width="700" height="1" vspace="12"><br><input type="image" src="/media_stat/images/buttons/survey_submit.gif" width="91" height="21" onClick="morningDeliverySurvey.submit()" alt="SUBMIT"><br><br><br></td></tr>
</form>
</fd:ReceiptSurvey>
<% } %>
</table>
</tmpl:put>
</tmpl:insert>