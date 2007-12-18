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
	if ("FILL".equals(customer.getProfile().getAttribute("Hamptons05"))) {
		if (!"thankyou".equalsIgnoreCase(request.getParameter("info"))) hasTaken = true;
		submitted = true;
	}
	String actionName = "";
	String successPage = null;
	FDSurvey Hamptons05 = FDSurveyFactory.getInstance().getSurvey("Hamptons05");
    List questions = Hamptons05.getQuestions();
	
	String destination = request.getParameter("VacationDestination");
	boolean submitNow  = destination!= null && destination.indexOf("East LI") < 0;
	
	if (submitNow || ("POST".equals(request.getMethod()) && !submitNow)) {
		actionName="submitSurvey";
	}

	successPage = "/survey/hamptons_2.jsp?info=thankyou";
	
	System.out.println("destination " + destination + " submitNow " + submitNow);
	
	
	String question0 = request.getParameter(((FDSurveyQuestion)questions.get(0)).getName());
	String question1 = request.getParameter(((FDSurveyQuestion)questions.get(1)).getName());
	String[] question2 = request.getParameterValues(((FDSurveyQuestion)questions.get(2)).getName());
	String question3 = request.getParameter(((FDSurveyQuestion)questions.get(3)).getName());
	String question4 = request.getParameter(((FDSurveyQuestion)questions.get(4)).getName());
	
	List shownQuestions = new ArrayList();
	
	if (!submitNow) {
		shownQuestions.add(questions.get(5));
		shownQuestions.add(questions.get(6));
		shownQuestions.add(questions.get(7));
		shownQuestions.add(questions.get(8));
		shownQuestions.add(questions.get(9));
		shownQuestions.add(questions.get(10));
		shownQuestions.add(questions.get(11));
		shownQuestions.add(questions.get(12));
		shownQuestions.add(questions.get(13));
		shownQuestions.add(questions.get(14));
	}
	
   String[] checkSurveyForm = new String[shownQuestions.size()];
   
   if (shownQuestions.size() > 0) {
	   int c = 0;
	   for(Iterator it = shownQuestions.iterator();it.hasNext();c++){
	       FDSurveyQuestion question = (FDSurveyQuestion)it.next();
	       checkSurveyForm[c] = question.getName();
	    }
	}
%>

<tmpl:insert template='/common/template/no_space_border.jsp'>
    <tmpl:put name='title' direct='true'>Hamptons Delivery Survey part 2 of 2</tmpl:put>
    <tmpl:put name='content' direct='true'>
<table width="700" cellpadding="0" cellspacing="0" border="0" class="text12">
<% if (submitted) {%>
<tr>
	<td colspan="11" class="text12" align="center"><br>
	<span class="title18">Thank you for your feedback.</span><br><%= hasTaken?"Your information has been submitted.<br>":""%>We greatly appreciate your time and interest.<br>
<br><a href="/index.jsp"><img src="/media_stat/images/template/help/help_home.gif" width="71" height="26" border="" alt="BACK HOME"></a><br>Go to <a href="/index.jsp">Home Page</a><br><br></td></tr>
<% } else { %>
<fd:ReceiptSurvey actionName="<%=actionName%>" result="result" successPage="<%=successPage%>" survey="<%=Hamptons05%>">
<form name="hamptonsSurvey" method="POST">
	<tr>
		<td colspan="11" class="text12"><br><span class="title18">Summertime Hamptons Delivery Survey, part 2 of 2</span><br><span class="space4pix"><br></span>
		Please answer these additional questions regarding Hamptons delivery.
<br><img src="/media_stat/images/layout/999966.gif" width="700" height="1" vspace="8"><br>
		<%  if (shownQuestions.size() > 0) { %>
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
	<% int quesCount = 4; %>
	    <logic:iterate id="question" collection="<%= shownQuestions %>" type="com.freshdirect.fdstore.survey.FDSurveyQuestion" indexId='index'>
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
			<% if (question.getDescription().indexOf(" ...") > -1) { %>
				<tr><td></td><td colspan="10" class="<%=surveyQuestion%>"><%=quesCount > 5 ? "<br><br>": ""%><b><%=question.getDescription()%></b> <%= "checkbox".equalsIgnoreCase(input) ? "(check all that apply)" : ""%><span class="space4pix"><br><br></span></td></tr>
			<% } else { %>
				<% quesCount++; %>
	            <tr><td colspan="11" class="<%=surveyQuestion%>"><%=quesCount > 5 ? "<br><br>": ""%><b><%=quesCount%>. <%=question.getDescription()%></b> <%= "checkbox".equalsIgnoreCase(input) ? "(check all that apply)" : ""%><span class="space4pix"><br><br></span></td></tr>
				<% if ("VacationSuggestion".equalsIgnoreCase(question.getName())) { //textarea %>
					<tr>
						<td></td>
						<td colspan="10" class="text13"><textarea wrap="virtual" cols="80" rows="4" class="text13" name="<%=question.getName()%>"><%=request.getParameter(question.getName())%></textarea>
					<br><span class="space8pix"><br><br></span></td>
					</tr>
				<% } %>
			<% } %>
				<% 
				int numCol = 0; 
				int numRow = 0;
				String prevLocation = "";
				%>
				<logic:iterate id="answer" collection="<%= question.getAnswers() %>" type="com.freshdirect.fdstore.survey.FDSurveyAnswer" indexId="i">
				<% if (question.getName().indexOf("VacationTimeslot") > -1) { //timeslot grid %>
					<% if (numRow == 0 && numCol ==0) { %>
						<tr><td colspan="11"><span class="space4pix"><br></span></td></tr>
						<tr>
						    <td colspan="3">&nbsp;</td>
						    <td align="center" bgcolor="#EEEEEE" class="text13" ><span class="space4pix"><br></span><b>MON</b><br><span class="space4pix"><br></span></td>
						    <td align="center" class="text13" ><span class="space4pix"><br></span><b>TUE</b><br><span class="space4pix"><br></span></td>
						    <td align="center" bgcolor="#EEEEEE" class="text13" ><span class="space4pix"><br></span><b>WED</b><br><span class="space4pix"><br></span></td>
						    <td align="center" class="text13" ><span class="space4pix"><br></span><b>THU</b><br><span class="space4pix"><br></span></td>
						    <td align="center" bgcolor="#EEEEEE" class="text13" ><span class="space4pix"><br></span><b>FRI</b><br><span class="space4pix"><br></span></td>
						    <td align="center" class="text13" ><span class="space4pix"><br></span><b>SAT</b><br><span class="space4pix"><br></span></td>
						    <td align="center" bgcolor="#EEEEEE" class="text13" ><span class="space4pix"><br></span><b>SUN</b><br><span class="space4pix"><br></span></td>
						    <td>&nbsp;</td>
						  </tr>
						  <tr><td></td><td colspan="9"><img src="/media_stat/images/layout/cccccc.gif" width="525" height="1"></td><td></td></tr>
					<% numRow++;
					} %>
						<% if (numCol == 0) { %>
							<tr><td colspan="3" class="text13" align="center"><span class="space4pix"><br></span>
								<% numRow++;
								if (numRow == 2) { %>
									<b>Morning</b><br>9 a.m. - Noon
								<% } else if (numRow == 3) { %>
									<b>Early Afternoon</b><br>Noon - 3 p.m.
								<% } else if (numRow == 4) { %>
									<b>Late Afternoon</b><br>3 p.m. - 6 p.m.
								<% } else if (numRow == 5) { %>
									<b>Evening</b><br>6 p.m. - 9 p.m.
								<% } %><br><span class="space4pix"><br></span>
							</td>
							<% numCol = 3; %>
						<% } %>
						<td align="center" <%= numCol > 2 && numCol %2 != 0 ? " bgcolor=\"#EEEEEE\"":"" %>><input type="<%=input%>" name="<%=question.getName()%>" value="<%=answer.getName()%>" <%=prevAnswers.contains(answer.getName()) ? "CHECKED" : "" %>></td>
						<% numCol++;%>
						<% 
						if (numCol ==10) { numCol = 0;  %>
							<td></td></tr>
							<tr><td></td><td colspan="9"><img src="/media_stat/images/layout/cccccc.gif" width="525" height="1"><%= numRow == 5 ? "<br><span class=\"space4pix\"><br></span>" : "" %></td><td></td></tr>
						<% } %>
					
				<% } else if (question.getDescription().indexOf(" ...") > -1 || "VacationPickup".equalsIgnoreCase(question.getName())) { //rating style %>
					<% if (numCol == 0) { %>
					<tr>
					    <td colspan="3" align="right" class="text13">Very likely</td>
					<% numCol = 3;
					} %>
					    <td align="center"><input type="<%=input%>" name="<%=question.getName()%>" value="<%=answer.getName()%>" <%=prevAnswers.contains(answer.getName()) ? "CHECKED" : "" %>></td>
						<% numCol++; %>
					<% if (numCol == 8) { %>
					    <td colspan="3"  class="text13">Very unlikely </td>
					  </tr>
					  <tr align="center"><td colspan="3"></td><td class="text12">1</td><td class="text12">2</td><td class="text12">3</td><td class="text12">4</td><td class="text12">5</td><td colspan="3"></td></tr>
					<% } %>
				
				<% } else if ("VacationBuy".equalsIgnoreCase(question.getName())) { //2 column %>
					<% if (numCol == 0) { %>
						<tr><td></td>
					<% } %>
							<td <%= numCol%2 == 0 ? "":"align=\"right\"" %>><input type="<%=input%>" name="<%=question.getName()%>" value="<%=answer.getName()%>" <%=prevAnswers.contains(answer.getName()) ? "CHECKED" : "" %>></td><td colspan="<%= numCol%2 == 0 ? "3":"5" %>" class="text13"><%= numCol%2 == 0 ? "":"&nbsp;&nbsp;" %><%=answer.getDescription()%></td>
						<% numCol++;%>
						
					<%  if (numCol ==2) { %>
						</tr>
						<% numCol = 0; %>
					<% } %>
				
				<% } else { //standard %>
					<tr><td></td><td><input type="<%=input%>" name="<%=question.getName()%>" value="<%=answer.getDescription()%>" <%=prevAnswers.contains(answer.getDescription()) ? "CHECKED" : "" %>></td><td colspan="9" class="text13"><%=answer.getDescription()%></td></tr>
				<% } %>
        		</logic:iterate>
		</logic:iterate>
	<tr><td colspan="11" align="center"><img src="/media_stat/images/layout/999966.gif" width="700" height="1" vspace="12"><br><input type="image" src="/media_stat/images/buttons/survey_submit.gif" width="91" height="21" onClick="hamptonsSurvey.submit()" alt="SUBMIT"><br><br><br></td></tr>
</form>

<script language="javascript">
	<% if(("GET".equals(request.getMethod()) || "POST".equals(request.getMethod())) && submitNow) { %>
			document.hamptonsSurvey.submit();
	<% } %>
</script>
</fd:ReceiptSurvey>
<% } %>
</table>
</tmpl:put>
</tmpl:insert>