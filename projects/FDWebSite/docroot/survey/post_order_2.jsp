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

<% 
	FDSurvey postOrderDetail = FDSurveyFactory.getInstance().getSurvey("PostOrderDetail");
    List questions = postOrderDetail.getQuestions();
	
	List shownQuestions = new ArrayList();
	List shownQuestionsText = new ArrayList();
	
    boolean submitted = "thankyou".equalsIgnoreCase(request.getParameter("info"));
   	boolean q_missing_item = "show".equalsIgnoreCase(request.getParameter("q1_missing_item"));
		if (q_missing_item) {
			shownQuestions.add(questions.get(0));
			shownQuestions.add(questions.get(1));
			shownQuestions.add(questions.get(2));
			shownQuestionsText.add(questions.get(0));
		}
	boolean q_product_damage = "show".equalsIgnoreCase(request.getParameter("q1_product_damage"));
		if (q_product_damage) {
			shownQuestions.add(questions.get(3));
			shownQuestionsText.add(questions.get(3));
		}
	boolean q_package_damage = "show".equalsIgnoreCase(request.getParameter("q1_package_damage"));
		if (q_package_damage) {
			shownQuestions.add(questions.get(4));
			shownQuestionsText.add(questions.get(4));
		}
	boolean q_quality_issue = "show".equalsIgnoreCase(request.getParameter("q1_quality_issue"));
		if (q_quality_issue) {
			shownQuestions.add(questions.get(5));
			shownQuestionsText.add(questions.get(5));
		}
	boolean q_quantity_issue = "show".equalsIgnoreCase(request.getParameter("q1_quantity_issue"));
		if (q_quantity_issue) {
			shownQuestions.add(questions.get(6));
			shownQuestions.add(questions.get(7));
			shownQuestions.add(questions.get(8));
			shownQuestionsText.add(questions.get(6));
		}
	boolean q_product_expire = "show".equalsIgnoreCase(request.getParameter("q1_product_expire"));
		if (q_product_expire) {
			shownQuestions.add(questions.get(9));
			shownQuestionsText.add(questions.get(9));
		}
	boolean q_wrong_item = "show".equalsIgnoreCase(request.getParameter("q1_wrong_item"));
		if (q_wrong_item) {
			shownQuestions.add(questions.get(10));
			shownQuestionsText.add(questions.get(10));
		}
	boolean q_delivery = "show".equalsIgnoreCase(request.getParameter("q1_delivery"));
		if (q_delivery) {
			shownQuestions.add(questions.get(11));
			shownQuestions.add(questions.get(12));
			shownQuestionsText.add(questions.get(11));
		}
	boolean q_customer_service = "show".equalsIgnoreCase(request.getParameter("q1_customer_service"));
		if (q_customer_service) {
			shownQuestions.add(questions.get(13));
			shownQuestions.add(questions.get(14));
			shownQuestionsText.add(questions.get(13));
		}
	String q_other_text = request.getParameter("q1_other");
	boolean q_other = q_other_text != null && !"".equals(q_other_text); 
		if (q_other) {
			shownQuestions.add(questions.get(15));
		}
	
	String q_severity = request.getParameter("q1_severity");
	
	shownQuestions.add(questions.get(16));
	shownQuestions.add(questions.get(17));
	shownQuestionsText.add(questions.get(16));
	shownQuestionsText.add(questions.get(17));
   
    String[] checkSurveyForm = new String[shownQuestionsText.size()];
    int c = 0;
    for(Iterator it = shownQuestionsText.iterator();it.hasNext();c++){
        FDSurveyQuestion question = (FDSurveyQuestion)it.next();
        checkSurveyForm[c] = question.getName();
    }
%>

<tmpl:insert template='/common/template/no_space_border.jsp'>
    <tmpl:put name='title' direct='true'>Order Feedback Survey</tmpl:put>
    <tmpl:put name='content' direct='true'>
    
<fd:ReceiptSurvey actionName="submitSurvey" result="result" successPage="/survey/post_order_2.jsp?info=thankyou" survey="<%=postOrderDetail%>">
<table width="700" cellpadding="0" cellspacing="0" border="0" class="text12">
<% if (submitted) { %>
<tr>
	<td colspan="8" class="text12"><br>
	<span class="title18">Thank you for your feedback.</span><br><br>
Please note that this research will help us improve our service, however, we are not able to individually respond to each customer who completes the survey. <b>If you have a question or issue that requires immediate attention</b>, please contact our Customer Service team toll-free at <%=user.getCustomerServiceContact()%>, or email <a href="mailto:service@freshdirect.com">service@freshdirect.com</a>. 
	<br><br>
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
	<br>
	Thank you again for your support.
	<br><br>
	Sincerely,
	<br><br>
	Steve Michaelson<br>
	President, FreshDirect<br><br>
	</td>
</tr>
<tr><td colspan="8" align="center"><br><a href="/index.jsp"><img src="/media_stat/images/template/help/help_home.gif" width="71" height="26" border="" alt="BACK HOME"></a><br>Go to <a href="/index.jsp">Home Page</a><br><br></td></tr>
<% } else { %>
<form method="post" name="postOrderSurvey">
	<tr>
		<td colspan="8" class="text12"><br><span class="title18">Order Feedback Survey (Page 2 of 2)</span><br><span class="space4pix"><br></span>
		A few details about the problem you experienced will help us improve.
<br><img src="/media_stat/images/layout/ff9933.gif" width="700" height="1" vspace="8"><br>
<span class="text15"><b>Which of the following best describe your problem?</b> (check all that apply) </span><br>
		<fd:ErrorHandler result='<%=result%>' field='<%=checkSurveyForm%>'>
			<% String errorMsg = SystemMessageList.MSG_MISSING_SURVEY_INFO; %>
			<br><%@ include file="/includes/i_error_messages.jspf" %>
		</fd:ErrorHandler>
		<span class="space8pix"><br></span>
		</td>
	</tr>
	<tr>
		<td><img src="/media_stat/images/layout/clear.gif" width="25" height="8"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="25" height="8"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="25" height="8"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="191" height="8"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="25" height="8"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="192" height="8"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="25" height="8"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="192" height="8"></td>
	</tr>
	
	    <logic:iterate id="question" collection="<%= shownQuestions %>" type="com.freshdirect.fdstore.survey.FDSurveyQuestion" indexId='index'>
            <% 
				boolean hasOther = question.getName().indexOf("delivery") > -1 || question.getName().indexOf("customer_service") > -1;
				boolean isHeader = question.getName().indexOf("_hdr") > -1;
				boolean subQuestion = (question.getName().indexOf("missing_item") > -1 || question.getName().indexOf("quantity_issue") > -1) && !isHeader;
				
				String input = "radio";
				String surveyQuestion = subQuestion? "text12":"text13";
                if(question.isMultiselect())input = "checkbox";
                
                List prevAnswers = new ArrayList();
                String[] paramValues = request.getParameterValues(question.getName());
                if(paramValues != null)
                    for (int j = 0; j < paramValues.length; j++) prevAnswers.add(paramValues[j]);
            %>
            <fd:ErrorHandler result='<%=result%>' name='<%=question.getName()%>'>
                    <% surveyQuestion = subQuestion? "text12rbold":"text13rbold"; %>
            </fd:ErrorHandler>
			<% if ("q_other".equalsIgnoreCase(question.getName())) { %>
				<tr>
					<td rowspan="2"><img src="/media_stat/images/layout/clear.gif" width="25" height="1"></td>
					<td colspan="7" class="text13"><%=question.getDescription()%>: <b><%=q_other_text%></b></td>
					<input type="hidden" name="q_other" value="<%=q_other_text%>">
				</tr>
		        <tr><td colspan="7"><img src="/media_stat/images/layout/999966.gif" width="620" height="1" vspace="2"><br><span class="space8pix"><br><br></span></td></tr>
			<% } else if ("q_additional_information".equalsIgnoreCase(question.getName())) { %>
				<tr>
					<td></td>
					<td colspan="7" class="text13"><br><b><%=question.getDescription()%></b><br><img src="/media_stat/images/layout/999966.gif" width="620" height="1" vspace="4"><br>
					<textarea wrap="virtual" cols="80" rows="4" class="text13" name="<%=question.getName()%>"><%=request.getParameter(question.getName())%></textarea>
					<br><span class="space8pix"><br><br></span></td>
				</tr>
			<% } else { %>
	            <%if(question.getName().indexOf("other") > -1 && !"q_other".equalsIgnoreCase(question.getName())){%>
					<tr>
						<td align="center"></td>
						<td class="text12" colspan="6">Other, please specify <input type="text" name="<%=question.getName()%>" value="<%=request.getParameter(question.getName())%>"></td>
					</tr>
					<tr><td colspan="7"><img src="/media_stat/images/layout/ffffff.gif" width="420" height="1" vspace="2"><br><span class="space8pix"><br><br></span></td></tr>
	            <%continue;
	            } else { 
				
				int numAnswers = (question.getAnswers()).size();
				boolean columns = false;
				if (numAnswers >= 6 && !hasOther) { columns = true; }
				boolean split = false;
				boolean _split = false; //2nd col start
				boolean split2 = columns && (numAnswers >= 6 && numAnswers < 9);
				boolean split3 = columns && numAnswers > 8;
				
				int numCols = 0;
				if (split2) {
					numCols = numAnswers/2 + numAnswers%2;
				} else if (split3) {
					numCols = numAnswers/3 + (numAnswers%3 != 0 ? 1 : 0);
				} 
				%>
				<tr>
					<td rowspan="<% if (columns) { %><%=(numCols +1) * 2%><% } else { %><%=(question.getAnswers().size() + (hasOther?2:1)) * 2%><% } %>" <%=subQuestion? "colspan=\"2\"" : ""%>><img src="/media_stat/images/layout/clear.gif" width="25" height="1"></td>
					<td colspan="<%=subQuestion? "6" : "7"%>" class="<%=surveyQuestion%>"><b><%=question.getDescription()%></b></td>
				</tr>
				
	            <tr><td colspan="<%=subQuestion? "6" : "7"%>"><img src="/media_stat/images/layout/<%=subQuestion? "FFFFFF" : "999966"%>.gif" width="<%=subQuestion? "595" : "620"%>" height="1" vspace="2"><%=isHeader?"<br><span class=\"space8pix\"><br></span>":""%></td></tr>
				<% int col = 0; %>
	             <logic:iterate id="answer" collection="<%= question.getAnswers() %>" type="com.freshdirect.fdstore.survey.FDSurveyAnswer" indexId="i">
	                 <% if (split2) { 
					 	col = i.intValue()%2; 
						} else if (split3) {
						col = i.intValue()%3;
						}
					 %>
					 <% if (columns && col == 0 ) { split = true; } %>
					 <% if (columns && split3 && col == 1 ) { _split = true; } %>
					 
					 <% if (split) { %>
					 	<tr>
						<td align="center"><input type="<%=input%>" name="<%=question.getName()%>" value="<%=answer.getName()%>" <%=prevAnswers.contains(answer.getName()) ? "CHECKED" : "" %>></td>
						<td class="text12" <%=subQuestion? "" : "colspan=\"2\""%>><%=answer.getDescription()%></td>

						 <% if (i.intValue()+1 == question.getAnswers().size()) { // only one col, closeout %>
						 		<td <%=split3? "colspan=\"2\"":""%>></td>
								<td <%=split3? "colspan=\"2\"":""%>></td>
								</tr>
								<tr><td colspan="<%=subQuestion? "6" : "7"%>"><img src="/media_stat/images/layout/ffffff.gif" width="300" height="1" vspace="2"><%=!hasOther && question.getName().indexOf("other") < 0 && i.intValue()+1 == question.getAnswers().size()?"<br><span class=\"space8pix\"><br><br></span>":""%></td>
								</tr>
							
						 <% } %>
					 <% 
					 split = false;
					 } else if (_split) { %>
						<td align="center"><input type="<%=input%>" name="<%=question.getName()%>" value="<%=answer.getName()%>" <%=prevAnswers.contains(answer.getName()) ? "CHECKED" : "" %>></td>
						<td class="text12"><%=answer.getDescription()%></td>

						 <% if (i.intValue()+1 == question.getAnswers().size()) { // only two cols, closeout %>
						 		<td></td>
								<td></td>
								</tr>
								<tr><td colspan="<%=subQuestion? "6" : "7"%>"><img src="/media_stat/images/layout/ffffff.gif" width="300" height="1" vspace="2"><%=!hasOther && question.getName().indexOf("other") < 0 && i.intValue()+1 == question.getAnswers().size()?"<br><span class=\"space8pix\"><br><br></span>":""%></td>
								</tr>
						 <% } %>
						 
					 <% 
					 _split = false;
					 } else if (columns) { // regular closeout%>
						<td align="center"><input type="<%=input%>" name="<%=question.getName()%>" value="<%=answer.getName()%>" <%=prevAnswers.contains(answer.getName()) ? "CHECKED" : "" %>></td>
						<td class="text12"><%=answer.getDescription()%></td>
					</tr>
					<tr><td colspan="<%=subQuestion? "6" : "7"%>"><img src="/media_stat/images/layout/ffffff.gif" width="300" height="1" vspace="2"><%=!hasOther && question.getName().indexOf("other") < 0 && i.intValue()+1 == question.getAnswers().size()?"<br><span class=\"space8pix\"><br><br></span>":""%></td>
					</tr>
					 <% } else {//regular 
					 %>
					 	<tr>
						<td align="center"><input type="<%=input%>" name="<%=question.getName()%>" value="<%=answer.getName()%>" <%=prevAnswers.contains(answer.getName()) ? "CHECKED" : "" %>></td>
						<td class="text12" colspan="<%=subQuestion? "6" : "7"%>"><%=answer.getDescription()%></td>
					</tr>
					<tr><td colspan="<%=subQuestion? "6" : "7"%>"><img src="/media_stat/images/layout/ffffff.gif" width="420" height="1" vspace="2"><%=!hasOther && question.getName().indexOf("other") < 0 && i.intValue()+1 == question.getAnswers().size()?"<br><span class=\"space8pix\"><br><br></span>":""%></td></tr>
					 <% } %>
	             </logic:iterate>
				 <% columns = false; %>
				<% } %>
			<% } 
			%>
        </logic:iterate>
	
	<tr>
	<td colspan="8" align="center"><img src="/media_stat/images/layout/ff9933.gif" width="700" height="1" vspace="12"><br><input type="image" src="/media_stat/images/buttons/survey_submit.gif" width="91" height="21" onClick="postOrderSurvey.submit()" alt="SUBMIT"><br><br><br></td></tr>
</form>
<% } %>
</table>

</fd:ReceiptSurvey>
</tmpl:put>
</tmpl:insert>