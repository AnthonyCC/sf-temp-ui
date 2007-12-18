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
	boolean submitted = false;
	boolean hasTaken = false;
	FDCustomerModel customer = FDCustomerFactory.getFDCustomer(user.getIdentity());
	if ("FILL".equals(customer.getProfile().getAttribute("Hamptons05"))) {
		if (!"thankyou".equalsIgnoreCase(request.getParameter("info"))) hasTaken = true;
		submitted = true;
	}
	FDSurvey Hamptons05 = FDSurveyFactory.getInstance().getSurvey("Hamptons05");
    List questions = Hamptons05.getQuestions();
	
	List shownQuestions = new ArrayList();
	shownQuestions.add(questions.get(0));
	shownQuestions.add(questions.get(1));
	shownQuestions.add(questions.get(2));
	shownQuestions.add(questions.get(3));
	shownQuestions.add(questions.get(4));
	
   String[] checkSurveyForm = new String[shownQuestions.size()];
    int c = 0;
    for(Iterator it = shownQuestions.iterator();it.hasNext();c++){
        FDSurveyQuestion question = (FDSurveyQuestion)it.next();
        checkSurveyForm[c] = question.getName();
    }
%>

<tmpl:insert template='/common/template/no_space_border.jsp'>
    <tmpl:put name='title' direct='true'>Hamptons Delivery Survey</tmpl:put>
    <tmpl:put name='content' direct='true'>
<fd:ReceiptSurvey actionName="validateSurvey" result="result" successPage="/survey/hamptons_2.jsp" survey="<%=Hamptons05%>">
<table width="700" cellpadding="0" cellspacing="0" border="0" class="text12">
<% if (submitted) {%>
<tr>
	<td colspan="7" class="text12" align="center"><br>
	<span class="title18">Thank you for your feedback.</span><br><%= hasTaken?"Your information has been submitted.<br>":""%>We greatly appreciate your time and interest.<br>
<br><a href="/index.jsp"><img src="/media_stat/images/template/help/help_home.gif" width="71" height="26" border="" alt="BACK HOME"></a><br>Go to <a href="/index.jsp">Home Page</a><br><br></td></tr>
<% } else { %>
<form method="POST" name="hamptonsSurvey1" action="/survey/hamptons_1.jsp">
	<tr>
		<td colspan="7" class="text12"><br><span class="title18">Summertime Hamptons Delivery Survey</span><br><span class="space4pix"><br></span>
		FreshDirect knows that our customers love our service and the fresh food we deliver. We are currently working hard to try to extend our service to make deliveries to the Hamptons this summer. Your responses to this brief survey will help us understand our customers' needs. Of course, in accordance with the FreshDirect Privacy Policy, all of your answers will be kept confidential.
<br><img src="/media_stat/images/layout/999966.gif" width="700" height="1" vspace="8"><br>
		</td>
	</tr>
	<tr>
		<td><img src="/media_stat/images/layout/clear.gif" width="25" height="1"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="25" height="1"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="200" height="1"></td>
    	<td><img src="/media_stat/images/layout/clear.gif" width="25" height="1"></td>
    	<td><img src="/media_stat/images/layout/clear.gif" width="200" height="1"></td>
    	<td><img src="/media_stat/images/layout/clear.gif" width="25" height="1"></td>
   		<td><img src="/media_stat/images/layout/clear.gif" width="200" height="1"></td>
	</tr>
	<% int quesCount = 0; %>
	    <logic:iterate id="question" collection="<%= shownQuestions %>" type="com.freshdirect.fdstore.survey.FDSurveyQuestion" indexId='index'>
            <% 
				String input = "radio";
				String surveyQuestion = "text13";
                if(question.isMultiselect())input = "checkbox";
                
                List prevAnswers = new ArrayList();
                String[] paramValues = request.getParameterValues(question.getName());
                if(paramValues != null)
                    for (int j = 0; j < paramValues.length; j++) prevAnswers.add(paramValues[j]);
            %>
            <fd:ErrorHandler result='<%=result%>' name='<%=question.getName()%>'>
                    <% surveyQuestion = "text13rbold"; %>
            </fd:ErrorHandler>
			<% if ("other".equalsIgnoreCase(question.getDescription())) { %>
				<input type="text" name="<%=question.getName()%>" value="<%=request.getParameter(question.getName())%>"></td></tr>
			<% } else { %>
				<% quesCount++; %>
	            <tr><td colspan="7" class="<%=surveyQuestion%>"><%=quesCount > 1 ? "<br>": ""%><b><%=quesCount%>. <%=question.getDescription()%></b> <%= "checkbox".equalsIgnoreCase(input) ? "(check all that apply)" : ""%><span class="space4pix"><br><br></span></td></tr>
			<% } %>
				<% 
				int numCol = 0; 
				String prevLocation = "";
				%>
				<logic:iterate id="answer" collection="<%= question.getAnswers() %>" type="com.freshdirect.fdstore.survey.FDSurveyAnswer" indexId="i">
				<% if (question.getName().indexOf("VacationDestination") > -1) { //3column %>
					<% if (numCol == 0) { %>
						<% if ("".equals(prevLocation)) { %>
							<tr><td></td><td colspan="6" class="text13"><i>Eastern Long Island:</i><span class="space2pix"><br><br></span></td></tr>
						<% } else if (prevLocation.indexOf("East LI") > -1 && answer.getName().indexOf("East LI") < 0) { %>
							<tr><td></td><td colspan="6" class="text13"><img src="/media_stat/images/layout/999999.gif" width="675" height="1" vspace="6"><br><i>Elsewhere:</i><span class="space2pix"><br><br></span></td></tr>
						<% } %>
						<tr><td></td>
					<% } %>
						<td><input type="<%=input%>" name="<%=question.getName()%>" value="<%=answer.getName()%>" <%=prevAnswers.contains(answer.getName()) ? "CHECKED" : "" %>></td>
						<% if ("other".equalsIgnoreCase(answer.getDescription())) { %>
							<td colspan="5" class="text13"><%=answer.getDescription()%>
						<% } else if (answer.getName().indexOf("East LI") < 0) { %> 
							<td colspan="5" class="text13"><%=answer.getDescription()%></td>
						<% } else { %>
							<td class="text13"><%=answer.getDescription()%></td>
						<% numCol++;%>
						<% } %>
					<% 
					prevLocation = answer.getName();
					if (numCol ==3) numCol = 0; %>
				<% } else { //standard %>
					<tr><td></td><td><input type="<%=input%>" name="<%=question.getName()%>" value="<%=answer.getDescription()%>" <%=prevAnswers.contains(answer.getDescription()) ? "CHECKED" : "" %>></td><td colspan="5" class="text13"><%=answer.getDescription()%></td></tr>
				<% } %>
        		</logic:iterate>
		</logic:iterate>
	<tr><td colspan="7" align="center"><img src="/media_stat/images/layout/999966.gif" width="700" height="1" vspace="12"><br><input type="image" src="/media_stat/images/buttons/survey_submit.gif" width="91" height="21" <%--onClick="javascript:checkForm(hamptonsSurvey1); return false;"--%> alt="SUBMIT"><br><br><br></td></tr>
</form>
<% } %>
</table>
</fd:ReceiptSurvey>
</tmpl:put>
</tmpl:insert>