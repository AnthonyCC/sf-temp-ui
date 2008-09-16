<%@ page import="com.freshdirect.fdstore.mail.*"%>
<%@ page import='com.freshdirect.fdstore.content.*'  %>
<%@ page import='com.freshdirect.fdstore.attributes.*'  %>
<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*"%>
<%@ page import="com.freshdirect.customer.*"%>
<%@ page import="java.net.URLEncoder" %>
<%@ page import='com.freshdirect.fdstore.survey.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%
String successPage = "/request_wine.jsp?"+request.getQueryString();
String redirectPage = "/login/login_popup.jsp?successPage=" + successPage;
%>
<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false' redirectPage='<%=redirectPage%>'/>
<% 
    FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
    FDIdentity customerIdentity = null;
    ErpCustomerInfoModel customerInfo = null;
    if (user!=null && user.getLevel() == 2){
        customerIdentity = user.getIdentity();
        customerInfo = FDCustomerFactory.getErpCustomerInfo(customerIdentity);	
    }
	boolean submitted = "thankyou".equalsIgnoreCase(request.getParameter("info"));
	boolean hasTaken = false;
	FDCustomerModel customer = FDCustomerFactory.getFDCustomer(user.getIdentity());
	if ("FILL".equals(customer.getProfile().getAttribute("Usability"))) {
		if (!"thankyou".equalsIgnoreCase(request.getParameter("info"))) hasTaken = true;
		submitted = true;
	}
	FDSurvey Usability = FDSurveyFactory.getInstance().getSurvey("Product Request Feedback");
	
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
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control", "no-cache");
String department = request.getParameter("department");

%>
<script language="JavaScript">
<!--
function linkTo(url){
	redirectUrl = "http://" + location.host + url;
	parent.opener.location = redirectUrl;
}
//-->
</script>
<%


%>
<tmpl:insert template='/common/template/large_pop.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Request a Wine</tmpl:put>
		<tmpl:put name='content' direct='true'>

<fd:RequestAWineTag actionName="wineRequest" result="result" successPage="request_product_conf.jsp?department=Wine" survey="<%=Usability%>">
	<table width="500" cellpadding="0" cellspacing="0" border="0" class="text12">
	<form name="request_product" method="post">
	<tr>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="60" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="280" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="20" height="8"></td>
	</tr>
	<tr>
	    <td colspan="3" class="text12">
			<img src="/media_stat/images/template/newproduct/wine_req_hdr_request.gif"><br />
			<img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br />
			Please describe the wines you'd like to see us carry in as much detail as possible including specific brands as well as regions and varietals if you know them.
	    </td>
	</tr>
	<tr>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="60" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="280" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="20" height="8"></td>
	</tr>
    <tr>
     	<td colspan="3" class="text12">
            <fd:ErrorHandler result='<%=result%>' name='error' id='errorMsg'>
            <%@ include file="/includes/i_error_messages.jspf" %>
            </fd:ErrorHandler>
        </td>    
    </tr>
    <% for( int i=1;i<=3;i++) {%>
    <tr>
        <td colspan="2" class="text12">
            <b><%="Wine Request #"+i%></b>
        </td>
        <td colspan="2" class="text12">
        <fd:GetDomainValues domainID='wine_country_usq' id='domainValues'>
        <SELECT NAME='<%="category"+i%>'>
            <OPTION VALUE="">Choose Country
            <logic:iterate id="domainValue" collection="<%= domainValues %>" type="java.lang.String">
            <OPTION VALUE='<%=domainValue%>'><%=domainValue%>
            </logic:iterate>
        </fd:GetDomainValues>
        </SELECT>
        <img src="/media_stat/images/layout/clear.gif" width="45" height="1">
        <fd:GetDomainValues domainID='wine_varietal' id='domainValues'>
        <SELECT NAME='<%="subCategory"+i%>'>
            <OPTION VALUE="">Choose Grape/Varietal
            <logic:iterate id="domainValue" collection="<%= domainValues %>" type="java.lang.String">
            <OPTION VALUE='<%=domainValue%>'><%=domainValue%>
            </logic:iterate>
        </fd:GetDomainValues>
        </SELECT>
        </td>
    </tr>
        <tr>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="4"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="60" height="4"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="280" height="4"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="20" height="4"></td>
	    </tr>
    <tr>
        <td colspan="2" class="text12">
            &nbsp;
        </td>
        <td colspan="3" class="text12">
            <input name='<%="product"+i%>' size="58">
        </td>
    </tr>
    	<tr>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="60" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="280" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="20" height="8"></td>
	    </tr>
        <% if(i<3){%>
        <tr><td colspan="4"><img src="/media_stat/images/layout/999966.gif" width="510" height="1" vspace="1"></td></tr>
        <%}%>
    	<tr>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="60" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="280" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="20" height="8"></td>
	</tr>
    <% }%>
	
    <input type="hidden" name="department" value="<%=department%>">
	
	
	<script language="JavaScript"><!--
		document.request_product.product1.focus();
	//-->
	</script>
	</table>
    
    



<table width="500" cellpadding="0" cellspacing="0" border="0" class="text12">
<% if (submitted) {%>
<tr>
	<td colspan="10" class="text12" align="center"><br>
	<span class="title18">Thank you for your feedback.</span><br><%= hasTaken?"Your information has been submitted.<br>":""%>We greatly appreciate your time and interest.<br>
<br><a href="/index.jsp"><img src="/media_stat/images/template/help/help_home.gif" width="71" height="26" border="" alt="BACK HOME"></a><br>Go to <a href="/index.jsp">Home Page</a><br><br></td></tr>
<% } else { %>


	<tr>
			    <td colspan="9" class="text12">
        <img src="/media_stat/images/template/newproduct/wine_req_hdr_feedback.gif"><br>
        <img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br />
		We're working hard to make our wine store as good as it can be. Let us know what you think - of the store, of the selection, of the wines you've tried.
<br><br>


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
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
	</tr>
    <tr>
		<td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
    </tr>
   <tr>
		<td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="60" height="1"></td>
        <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
        <td colspan="1" align="center"><b>Excellent</b></td>
        <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
        <td colspan="1" align="center"><b>Good</b></td>
        <td><img src="/media_stat/images/layout/clear.gif" width="30" height="1"></td>
        <td colspan="1" align="center"><b>Poor</b></td>
    </tr>
    <tr>
		<td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
    </tr>

	<% int quesCount = 0;
       String bgColor="";

     %>
	    <logic:iterate id="question" collection="<%= questions %>" type="com.freshdirect.fdstore.survey.FDSurveyQuestion" indexId='index'>
            <% 
				String input = "radio";
				//String surveyQuestion = subQuestion? "text12":"text13";
				String surveyQuestion = "text13";
                if(question.isMultiselect())input = "checkbox";
                
                List prevAnswers = new ArrayList();
                String[] paramValues = request.getParameterValues(question.getName());
                if(paramValues != null) {
                    for (int j = 0; j < paramValues.length; j++) {
					    prevAnswers.add(paramValues[j]);
                    }
				}
                quesCount++;
                if((quesCount%2)==0) {
                    bgColor="#ffffff";
                    
                } else {
                    bgColor="#eeeeee";
                }
            %>
            <fd:ErrorHandler result='<%=result%>' name='<%=question.getName()%>'>
                    <% surveyQuestion = "text13rbold"; %>
            </fd:ErrorHandler>
					<tr bgcolor='<%=bgColor%>'><td colspan="4" align="right"class="<%=surveyQuestion%>"><b><%=question.getDescription()%></b></td>
				<logic:iterate id="answer" collection="<%= question.getAnswers() %>" type="com.freshdirect.fdstore.survey.FDSurveyAnswer" indexId="i">
					<td colspan="1" align="center" style="padding: 4px 0; width: 55px;"><input type="<%=input%>" name="<%=question.getName()%>" value="<%=answer.getName()%>" <%=prevAnswers.contains(answer.getName()) ? "CHECKED" : "" %>></td>
        		</logic:iterate>
                </tr>
		</logic:iterate>
	
</form>

<% } %>
	<tr>
		<td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	    <td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
	</tr>

	<tr>
		<td colspan="10" align="center">
			<a href="javascript:document.request_product.reset()"><img src="/media_stat/images/template/newproduct/b_clear.gif" width="47" height="17" border="0" alt="Clear"></a>&nbsp;&nbsp;
			<input type="image" name="send_email" src="/media_stat/images/template/newproduct/b_send.gif" width="45" height="15" vspace="1" border="0" alt="Send Request"onClick="javascript:document.request_product.submit()"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="12"><br>
    </td>
    </tr>

</table>
<img src="/media_stat/images/layout/999966.gif" width="510" height="1" vspace="1">   
</fd:RequestAWineTag>
	</tmpl:put>
</tmpl:insert>
