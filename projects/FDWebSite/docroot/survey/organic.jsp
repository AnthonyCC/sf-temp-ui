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
	String actionName = "submitSurvey";
	boolean submitted = "thankyou".equalsIgnoreCase(request.getParameter("info"));
	boolean hasTaken = false;
	FDCustomerModel customer = FDCustomerFactory.getFDCustomer(user.getIdentity());
	if ("FILL".equals(customer.getProfile().getAttribute("Organic2"))) {
		if (!"thankyou".equalsIgnoreCase(request.getParameter("info"))) hasTaken = true;
		submitted = true;
		actionName = "xxx";
	}
	
	FDSurvey Organic = FDSurveyFactory.getInstance().getSurvey(EnumSurveyType.ORGANIC_2, user);
    List questions = Organic.getQuestions();
	
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
    <tmpl:put name='title' direct='true'>Organic Food Survey</tmpl:put>
    <tmpl:put name='content' direct='true'>
<table width="700" cellpadding="0" cellspacing="0" border="0" class="text12">
<% 
System.out.println("submitted? " + submitted);
System.out.println(request.getParameter("info"));
if (submitted) {
%>
<tr>
	<td colspan="11" class="text12" align="center"><br>
	<span class="title18">Thank you for your feedback.</span><br><%= hasTaken?"Your information has been submitted.<br>":""%>We greatly appreciate your time and interest.<br>
<br><a href="/index.jsp"><img src="/media_stat/images/template/help/help_home.gif" width="71" height="26" border="" alt="BACK HOME"></a><br>Go to <a href="/index.jsp">Home Page</a><br><br></td></tr>
<% } else { %>
<fd:ReceiptSurvey actionName="<%=actionName%>" result="result" successPage="/survey/organic.jsp?info=thankyou" survey="<%=Organic%>">
<form name="organicSurvey" method="POST">
	<tr>
		<td colspan="11" class="text12"><br><span class="title18">Organic Food Survey</span><br><span class="space4pix"><br></span>
		We thank you for your interest in helping us develop our Organic & All-Natural department at FreshDirect. With your help and feedback we will be able to create an experience that will best suit all of your Organic shopping needs.
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
				String surveyQuestion = "text12";
                if(question.isMultiselect())input = "checkbox";
                
                List prevAnswers = new ArrayList();
                String[] paramValues = request.getParameterValues(question.getName());
                if(paramValues != null)
                    for (int j = 0; j < paramValues.length; j++) {
					prevAnswers.add(paramValues[j]);
				}
            %>
            <fd:ErrorHandler result='<%=result%>' name='<%=question.getName()%>'>
                    <% surveyQuestion = "text12rbold"; %>
            </fd:ErrorHandler>
			<% if (question.getName().indexOf("q7_type_other") > -1) { %>
				<input type="text" name="<%=question.getName()%>" value="<%=request.getParameter(question.getName())%>"></td></tr>
				</table></td></tr>
			<% } else if ("other".equalsIgnoreCase(question.getDescription())) { %>
				<% if (index.intValue() == 2) {%>
				</td></tr>
				<tr><td></td><td colspan="10" style="padding-left: 25px;"><textarea wrap="virtual" cols="50" rows="2" style="margin-top: 4px;" class="text13" name="<%=question.getName()%>"><%=request.getParameter(question.getName())%></textarea></td></tr>
				<% } else { %>
					<input type="text" name="<%=question.getName()%>" value="<%=request.getParameter(question.getName())%>"></td></tr>
				<% } %>
			<% } else { %>
				<% if  (question.getDescription().indexOf("why") >-1) { %>
					<tr valign="top">
						<td></td>
						<td colspan="10" class="text12" style="padding-left: 25px;"><br><%=question.getDescription()%> <br><textarea wrap="virtual" cols="50" rows="2" class="text13" name="<%=question.getName()%>"><%=request.getParameter(question.getName())%></textarea></td>
					</tr>
				<% } else { %>
					<% quesCount++; %>
				 	<tr><td colspan="11" class="<%=surveyQuestion%>"><%=quesCount > 1 ? "<br><br>": ""%><b><%=quesCount%>. <%=question.getDescription()%></b> <%= question.isRequired() ? "":" (optional)" %><%= question.isMultiselect() ? "(check all that apply)":""%><span class="space4pix"><br><br></span></td></tr>
					<% if (question.isOpenEnded()) { //textarea %>
						<tr>
							<td></td>
							<td colspan="10" class="text12"><textarea wrap="virtual" cols="80" rows="4" class="text13" name="<%=question.getName()%>"><%=request.getParameter(question.getName())%></textarea>
						<br><span class="space8pix"><br></span></td>
						</tr>
					<% } else { 
					int col = 0; 
					%>
					<logic:iterate id="answer" collection="<%= question.getAnswers() %>" type="com.freshdirect.fdstore.survey.FDSurveyAnswer" indexId="i">
					<% if (question.getName().indexOf("q7_type") > -1) { %>
						<% if (i.intValue() == 0) { %>
						<tr><td colspan="11">
						<table width="100%" cellpadding="0" cellspacing="0" border="0">
							<tr valign="top"><td width="1%"><img src="/media_stat/images/layout/clear.gif" width="25" height="1"></td>
						<% } %>
							<% if (!answer.getName().equalsIgnoreCase("other")) { %>
								<td width="33%" class="text12"><input type="<%=input%>" name="<%=question.getName()%>" value="<%=answer.getDescription()%>" <%=prevAnswers.contains(answer.getDescription()) ? "CHECKED" : "" %>> <%=answer.getDescription()%></td>
								<% col++; 
									if (col == 3) {
								%>
								</tr><tr><td width="1%"></td>
								<% col = 0;
								} %>
							<% } else { %>
								<tr><td width="1%"></td><td colspan="3" class="text12"><input type="<%=input%>" name="<%=question.getName()%>" value="<%=answer.getDescription()%>" <%=prevAnswers.contains(answer.getDescription()) ? "CHECKED" : "" %>> <%=answer.getDescription()%>  (please specify)
							<% } %>
					<% } else { %>
							<tr><td></td><td><input type="<%=input%>" name="<%=question.getName()%>" value="<%=answer.getDescription()%>" <%=prevAnswers.contains(answer.getDescription()) ? "CHECKED" : "" %>></td><td colspan="9" class="text12"><%=answer.getDescription()%> <%="other".equalsIgnoreCase(answer.getName()) ? " (please specify)" : "" %>
							<% if (!"other".equalsIgnoreCase(answer.getName())) { %>
								</td></tr>
							<% } else { continue; } %>
					<% } %>
	        		</logic:iterate>
					<% } %>
				<% } %>
			<% } %>
		</logic:iterate>
	<tr><td colspan="11" align="center"><img src="/media_stat/images/layout/999966.gif" width="700" height="1" vspace="12"><br><input type="image" src="/media_stat/images/buttons/survey_submit.gif" width="91" height="21"alt="SUBMIT"><br><br></td></tr>
</form>
</fd:ReceiptSurvey>
<% } %>
</table>
</tmpl:put>
</tmpl:insert>