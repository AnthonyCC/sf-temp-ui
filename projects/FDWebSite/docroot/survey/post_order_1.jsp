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

<fd:CheckLoginStatus id="user" guestAllowed="false" recognizedAllowed="true" /> 

<tmpl:insert template='/common/template/no_space_border.jsp'>
    <tmpl:put name='title' direct='true'>Order Feedback Survey</tmpl:put>
    <tmpl:put name='content' direct='true'>

<%
FDSurvey postOrder = FDSurveyFactory.getInstance().getSurvey(EnumSurveyType.POST_ORDER, FDSessionUser.getFDSessionUser(session));
List questions = postOrder.getQuestions(); 

List shownQuestions = new ArrayList();
shownQuestions.add(questions.get(0));

boolean isGood  = "good".equalsIgnoreCase(request.getParameter("post_order_response"));
boolean isProblem  = "problem".equalsIgnoreCase(request.getParameter("post_order_response"));
boolean isRemove  = "remove".equalsIgnoreCase(request.getParameter("post_order_response"));
boolean submitted = request.getParameter("sale") == null || "".equals(request.getParameter("sale"));
boolean unknown = !isGood && !isProblem && !isRemove;

String successPage = "/survey/post_order_";
if (isGood) { successPage += "1.jsp?post_order_response=good"; }
if (isProblem) { 
	successPage += "2.jsp?"; 
	shownQuestions.add(questions.get(1));
	shownQuestions.add(questions.get(2));
	shownQuestions.add(questions.get(3));
}
if (isRemove) { successPage += "1.jsp?post_order_response=remove"; }

String[] checkSurveyForm = new String[shownQuestions.size()];
    int c = 0;
    for(Iterator it = shownQuestions.iterator();it.hasNext();c++){
        FDSurveyQuestion question = (FDSurveyQuestion)it.next();
        checkSurveyForm[c] = question.getName();
    }
	
List severityAnswers = ((FDSurveyQuestion)questions.get(3)).getAnswers();
%>

<fd:ReceiptSurvey actionName="submitSurvey" result="result" successPage="<%=successPage%>" survey="<%=postOrder%>">
<table width="700" cellpadding="0" cellspacing="0" border="0" class="text12">
<form name="post_order_1" method="POST">
	<fd:ErrorHandler result='<%=result%>' field='<%=checkSurveyForm%>'>
		<% String errorMsg = SystemMessageList.MSG_MISSING_SURVEY_INFO; %>
		<tr><td colspan="3" class="text12"><br><%@ include file="/includes/i_error_messages.jspf" %></td></tr>
	</fd:ErrorHandler>

<% if (isProblem) { %>
<tr>
		<td colspan="3" class="text12"><br><span class="title18">Order Feedback Survey (Page 1 of 2)</span><br><span class="space4pix"><br></span>
		<input type="hidden" name="<%=((FDSurveyQuestion)questions.get(0)).getName()%>" value="PROBLEM">
		We're sorry that you were not completely satisfied with your order. Your feedback will be used to help us improve our service. If you have a minute, please answer a few quick questions.
<br><img src="/media_stat/images/layout/ff9933.gif" width="700" height="1" vspace="8"><br><span class="space8pix"><br></span>
		</td>
	</tr>
	<logic:iterate id="question" collection="<%= shownQuestions %>" type="com.freshdirect.fdstore.survey.FDSurveyQuestion" indexId='index'>
            <%  String input = "radio";
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
			<% if ("q_problem_area_other".equalsIgnoreCase(question.getName())) { %>
					<tr>
						<td rowspan="2"></td>
						<td></td>
						<td class="text13">Other<span class="text12">, please specify</span> <input type="text" style="width:200px;" name="<%=question.getName()%>" value="<%=request.getParameter(question.getName())%>"></td>
					</tr>
					<tr><td colspan="2"><img src="/media_stat/images/layout/ffffff.gif" width="400" height="1" vspace="2"><br><span class="space8pix"><br><br></span></td></tr>
			<% } else if (!"q_post_order_response".equalsIgnoreCase(question.getName())) { %>
				<tr>
					<td rowspan="2"><img src="/media_stat/images/layout/clear.gif" width="25" height="1"></td>
					<td colspan="2" class="<%=surveyQuestion%>"><b><%=question.getDescription()%></b></td>
				</tr>
	            <tr><td colspan="2"><img src="/media_stat/images/layout/999966.gif" width="620" height="1" vspace="2"></td></tr>
	             <logic:iterate id="answer" collection="<%= question.getAnswers() %>" type="com.freshdirect.fdstore.survey.FDSurveyAnswer" indexId="i">
	                 <% if ("q_problem_severity".equalsIgnoreCase(question.getName()) && i.intValue() == (question.getAnswers()).size()-1 ) { continue; } %>
					 <tr>
						<td rowspan="2"><img src="/media_stat/images/layout/clear.gif" width="25" height="1"></td>
						<td align="center"><input type="<%=input%>" name="<%=question.getName()%>" value="<%=answer.getDescription()%>" <% if ("q_problem_severity".equalsIgnoreCase(question.getName())) { %><%=prevAnswers.contains(answer.getName()) ? "CHECKED" : "" %><% } else { %><%=prevAnswers.contains(answer.getDescription()) ? "CHECKED" : "" %><% } %>></td>
						<td width="450" class="text13"><%=answer.getDescription()%></td>
					</tr>
					<tr><td colspan="2"><img src="/media_stat/images/layout/ffffff.gif" width="400" height="1" vspace="2"></td></tr>
	             </logic:iterate>
			<% } %>
        </logic:iterate>
	<tr><td colspan="3" align="center"><img src="/media_stat/images/layout/ff9933.gif" width="700" height="1" vspace="12"><br><input type="image" src="/media_stat/images/buttons/survey_continue.gif" width="91" height="21" onClick="post_order_1.submit()" alt="CONTINUE"><br><br><br></td></tr>
<% } else { %>
	<tr><td colspan="3" class="text12"><br>
	<% if (isGood) { %>
	<input type="hidden" name="<%=((FDSurveyQuestion)questions.get(0)).getName()%>" value="GOOD">
	<span class="title18">Thank you!</span><br><br>
	We're glad you were happy with your order.  
	<% } else if (isRemove) { %>
	<input type="hidden" name="<%=((FDSurveyQuestion)questions.get(0)).getName()%>" value="REMOVE">
	<span class="title18">Thank you.</span><br><br>
	We have noted your preference not to partake in our order feedback surveys and you will not receive another email on this subject.<br><br>
	<% } %>
	If there's ever anything we can do to help, don't hesitate to contact us toll-free at <%=user.getCustomerServiceContact()%>, or send us email at <a href="mailto:service@freshdirect.com">service@freshdirect.com</a>.
	</td></tr>
	<tr><td colspan="3" class="text12"><br>
	The Customer Service hours of operation are:
	<table cellpadding="2" cellspacing="0">
	<tr>
		<td rowspan="4">&nbsp;&nbsp;&nbsp;&nbsp;</td><td class="text12">Sunday</td><td class="text12">7:30 a.m. - 1 a.m.</td>
	</tr>
	<tr>
		<td class="text12">Monday - Thursday&nbsp;&nbsp;&nbsp;&nbsp;</td><td class="text12">6.30 a.m. - 1 a.m.</td>
	</tr>
	<tr>
		<td class="text12">Friday</td><td class="text12">6.30 a.m. - 10 p.m.</td>
	</tr>
	<tr>
		<td class="text12">Saturday</td><td class="text12">7:30 am - 10 p.m.</td>
	</tr>
	</table>
	<% if (isGood) { %><br>Thank you again for your support and happy eating!<br><% } %>	 
	<br>
	Sincerely,
	<br><br>
	Steve Michaelson<br>
	President, FreshDirect
	</td></tr>
	<input type="hidden" name="<%=((FDSurveyQuestion)questions.get(3)).getName()%>" value="<%=((FDSurveyAnswer)severityAnswers.get(severityAnswers.size()-1)).getDescription()%>">
<% } %>
</form>
	<script language="javascript">
	<% if("GET".equals(request.getMethod())) { %>
		<% if (isGood && !submitted) { %>
			document.post_order_1.submit();
		<% } else if (isRemove && !submitted) {%>
			document.post_order_1.submit();
		<% } %>
	<% } %>
	</script>
<% if (!isProblem || unknown) { %>
<tr><td colspan="3" align="center"><br><a href="/index.jsp"><img src="/media_stat/images/template/help/help_home.gif" width="71" height="26" border="" alt="BACK HOME"></a><br>Go to <a href="/index.jsp">Home Page</a><br><br></td></tr>
<% } %>
</table>
</fd:ReceiptSurvey>
</tmpl:put>
</tmpl:insert>