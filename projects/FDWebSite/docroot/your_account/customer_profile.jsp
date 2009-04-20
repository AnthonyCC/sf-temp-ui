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
String successPage = "/your_account/customer_profile_summary.jsp";
String redirectPage = "/login/login.jsp?successPage=" + successPage;
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
	FDSurvey Usability = FDSurveyCachedFactory.getSurvey(EnumSurveyType.CUSTOMER_PROFILE_SURVEY);
	FDSurveyResponse surveyResponse= FDCustomerManager.getCustomerProfileSurveyInfo(customerIdentity);
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
<script type="text/javascript">
	/*
	 *	this probably isn't a good idea usabilty-wise
	 *
	*/
	function clearSection(pId, isChecked){
		var store = pId+'store';
		if (isChecked)
		{
			/* checking none */
			/* if not stored, create store */
			if (!window[store])
			{
				window[store] = new Array();
			}
			/* grab all inputs to store and then clear them  */
			var selections = (document.getElementById(pId)).getElementsByTagName('input');
			for (var n=0; n < (selections.length-1); n++) {
				switch (selections[n].type)
				{
					case 'radio':
					case 'checkbox' :
						window[store][n] = selections[n].checked;
						selections[n].checked = false;
						selections[n].disabled = true;
						break;
					case 'text' :
						window[store][n] = selections[n].value;
						selections[n].value = '';
						selections[n].disabled = true;
						break;
				
				}
			}
		}else{
			/* unchecking none */
			/* grab all selections */
			var selections = (document.getElementById(pId)).getElementsByTagName('input');
			/* check if stored already */
			if (window[store])
			{
				/* replace them */
				for (var n=0; n < (selections.length-1); n++) {
					switch (selections[n].type)
					{
					case 'radio':
					case 'checkbox' :
							selections[n].disabled = false;
							selections[n].checked = window[store][n];
							break;
						case 'text' :
							selections[n].disabled = false;
							selections[n].value = window[store][n];
							break;
					
					}
				}
			}
		}
	}
	/* if we don't want the values, just disabling would be the way to go */
	function disableForNone(pId, noneVal){
		if(document.getElementById(pId)) {
			var inputs = (document.getElementById(pId)).getElementsByTagName('input');
		}else{ return false; }

		/* check if we're dealing with radio buttons. if so, toggle the noneVal  */
		if ((inputs[inputs.length-1].disabled || inputs[0].disabled) && inputs[0].type=='radio') {
			(noneVal) ? noneVal = false : noneVal = true;
		}
		for (var n=0; n < inputs.length; n++) {
			if (inputs[n].className.indexOf('none_choice') < 0)
			{
				/* disable all but the none choice */
				(noneVal) ? inputs[n].disabled = true :	inputs[n].disabled = false;
			}else{
				/* toggle the none choice */
				(noneVal) ? inputs[n].checked = true : inputs[n].checked = false;
			}
		}
	}
</script>
<style>
	body { font-family:Verdana,Arial,sans-serif; font-size: 10px; height: 100%; }
	.container { border: 2px solid #060; width: 100%; }
	.ctext { text-align: left; margin: 10px 2px; font-size: 11px; }
	.etext { text-align: left; margin: 10px 20px; font-size: 11px; font-weight: bold; }
	.notes { text-align: left; margin: 10px 20px; font-size: 10px; font-weight: normal; }
	.example { border: 1px dashed #4c4; width: 703px; text-align: left; margin: 5px auto 5px auto; }
	.fleft { float: left; }
	.cleft { clear: left; }
	.cright { clear: right; }
	.cboth { clear: both; }
	.question { color: orange; font-size: 12px; font-weight: bold; padding:10px 0px }
	.question_number { float: left; width: 25px; }
	.question_text { float: left; width: 675px; }
	.answer { margin: 10px; }
	.other { padding-left: 32px; }

	.q02_container { float: left; margin: 0 10px 0 0; }
	.q02_input { float: left; padding: 1px; }
	.q02_input input { font-size: 11px; text-align: center; width: 30px; }
	.q02_incr { float: left; }
	.q02_incr div { height: 8px; width: 10px; padding: 1px; }
	.q02_incr div img { height: 9px; width: 10px; margin: 0 2px; }
	.q02_text { float: left; line-height: 22px; padding-left: 4px; }

	.q03_container { float: left; margin: 0 10px 0 0; }

	.q04_container { float: left; margin: 0 10px 0 0; width: 48%; line-height: 22px; }
	.q04_container div { clear: right; }
	.q04_container input { float: left; }
	.q04_container div div { float: right; width: 90%; line-height: 22px; }

	.q05_container { float: left; margin: 0; line-height: 23px; }
	.q05_col50per { width: 50%; }
	.q05_col25per { width: 25%; }
	.q05_col75per { width: 75%; }
	.q05_container div { line-height: 24px; }
	.q05_container div div { float: left; padding-right: 10px; line-height: 22px; width: 22px; }
	.q05_container .other_input { font-size: 11px; text-align: left; width: 100px; margin: 2px 10px 1px 3px; }

	.q06_container { height: 30px; }
	.q06_container span { font-weight: bold; }
	.q06_container select { margin-left: 10px; }

	.q07_container { margin: 0 10px 0 0; width: 100%; line-height: 22px; }
	.q07_container div { padding: 1px 0; line-height: 22px; }
	.q07_container div div { clear: left; float: left; padding-right: 3px; }
    .q07_container .other_input { font-size: 11px; text-align: left; width: 100px; margin: 2px 10px 1px 3px; }
	//.q07_container .other_input { font-size: 11px; text-align: left; width: 100px; margin-left: 10px; }

	.q08_container { margin: 0 10px 0 0; width: 75%; line-height: 22px; }
	.q08_container .q08_text { clear: both; }
	.q08_text { float: left; padding: 1px 0; line-height: 22px; width: 75%; }
	.q08_cb { float: right; padding: 1px 0; line-height: 22px; width: 10%; text-align: center; font-weight: bold; }
	
	.q09_container { float: left; width: 33%; padding: 20px 0; }
	.q09_container .box { width: 100%; }
	.q09_container div div { line-height: 22px; }
	.q09_rb { float: left; width: 35%; text-align: right; }
	.q09_text { float: left; width: 65%; text-align: left; }
	.odd { background-color: #ddd;  line-height: 26px; height: 26px;}
	.even { background-color: #aaa;  line-height: 26px; height: 26px;}

	.q10_container { float: left; width: 49%; }
	.q10_container div { margin: 5px 0; }
	.q10_container select { margin: 0 10px 0 0; width: 97%; }
	
	.q11_container { margin: 0 10px 0 0; line-height: 22px; }
	.q11_colfright { float: right; }
	.q11_col35per { width: 35%; }
	.q11_col65per { width: 65%; }
	.q11_col65per div { width: 50%; }
	.q11_container div { float: left; padding: 1px 0; line-height: 22px; }
	.other_input { font-size: 11px; text-align: left; width: 100px; margin-left: 10px; }

	.q12_container { float: left; padding: 1px; }
	.q12_container input { margin: 5px; }
	.rb_image { text-align: center; width: 110px; }


	/* below is the table styles */
	.tQuestion { color: orange; font-size: 12px; font-weight: bold; width: 100%; border: 0; text-align: left; border-collapse: collapse; line-height: 24px; }
	.tQuestion td { vertical-align: top; }
	td.tIndex { width: 20px; }
	.tAnswer { padding: 6px 0 10px 10px; width: 100%; border-spacing: 0px; border-collapse: collapse; line-height: 24px; }
	.tAnswer td { padding: 0; }
	.tAnswer table { float: left; line-height: 24px; }
	.incr_input { font-size: 11px; text-align: center; width: 30px; }
	.col25per { width: 25%; }
	.col33per { width: 33.3%; }	
	.col49per { width: 49%; }
	.col50per { width: 50%; }
	.col75per { width: 75%; }
	.col100per { width: 100%; }
	.other { padding: 0 0 0 24px; }
	.odd { background-color: #ddd; }
	.even { background-color: #aaa; }
	.bolded { font-weight: bold; }
	.cbHead { width: 10%; text-align: center; font-weight: bold; }
	.other_input { font-size: 11px; text-align: left; width: 100px; margin-left: 10px; }
	.tLeft { text-align: left; }
	.tCen { text-align: center; }
	.pad10px { padding: 10px; }
	.padTop30px { padding-top: 30px; }
	.rb_image { text-align: center; width: 110px; }

</style>
<tmpl:insert template='/common/template/dnav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Your Profile</tmpl:put>
<tmpl:put name='content' direct='true'>

<fd:CustomerProfileSurveyTag actionName="submitSurvey" result="result" successPage="<%=successPage%>" survey="<%=Usability%>">
<fd:IncludeMedia name="/media/editorial/site_pages/survey/cps_intro.html" />	
 	
<table cellpadding="0" cellspacing="0" border="0" class="text12">
<tr><td colspan="10">
    <form name="request_product" method="post">
    <input type="hidden" name="department" value="<%=department%>">
    <br>
    
<% if (submitted) {%>
<tr>
	<td colspan="10" class="text12" align="center">
	<span class="title18">Thank you for your feedback.</span><br><%= hasTaken?"Your information has been submitted.<br>":""%>We greatly appreciate your time and interest.<br>
<br><a href="/index.jsp"><img src="/media_stat/images/template/help/help_home.gif" width="71" height="26" border="" alt="BACK HOME"></a><br>Go to <a href="/index.jsp">Home Page</a><br><br></td></tr>
<% } else { %>


	
		<%  if (questions.size() > 0) { %>
        
			<fd:ErrorHandler result='<%=result%>' field='<%=checkSurveyForm%>'>
            <tr>
				<% String errorMsg = SystemMessageList.MSG_MISSING_SURVEY_INFO; %>
				<br><%@ include file="/includes/i_error_messages.jspf" %><span class="space8pix"><br></span>
                </tr>
			</fd:ErrorHandler>
            
		<% } %>
		
	

	<% int quesCount = 1; 
       String questionPostFix=" (Choose one)";
       List previousAnswers=null;
       
    %>
    <!--<div class="container">-->
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
                
                if(surveyResponse!=null && surveyResponse.getAnswer(question.getName())!=null) {
                    previousAnswers=surveyResponse.getAnswerAsList(question.getName());
                } else {
                   previousAnswers=null;
                }
            %>
            <fd:ErrorHandler result='<%=result%>' name='<%=question.getName()%>'>
                    <% surveyQuestion = "text13rbold"; %>
            </fd:ErrorHandler>
			<!--<div class="example">-->
			    <div class="question">
				    <div class="question_number">
					<%=quesCount%>
				    </div>
				    <div class="question_text">
					<%=SurveyHtmlHelper.getQuestionText(question)%>
				    </div>
				    <div class="cleft"><!--  --></div>
			    </div>
		    <!--</div>-->
                <!--<div class="example">-->
			        <!--<div class="answer">-->
                    <% String container="q07_container";
                       if(EnumFormDisplayType.DISPLAY_PULLDOWN_GROUP.equals(question.getFormDisplayType())){
                            container="q06_container";
                       }else if(question.isPulldown()) {
                            container="q10_container";
                       } else if(EnumFormDisplayType.TWO_ANS_PER_ROW.equals(question.getFormDisplayType())) {
                            container="answer_container";
                       }else if(EnumFormDisplayType.GROUPED_MULTI_SELECTION.equals(question.getFormDisplayType())){
                            container="q8_container";
                       }
                       
                     %>
                     
				        <div class="<%=container%>" id="<%=quesCount%>">
					        
                              <%=SurveyHtmlHelper.getAnswersHtml(String.valueOf(quesCount),question,previousAnswers)%>
				
                            
				        </div>
			        <!--</div>-->
		        <!-- </div>-->
                <%quesCount++;%>
                <div class="cleft"><!--  --></div>
                
		</logic:iterate>
<!--</div>	-->
</form>
</td>
</tr>
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
<br><br>
<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
<FONT CLASS="space4pix"><BR><BR></FONT>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="675">
<tr VALIGN="TOP">
<td WIDTH="35"><a href="/index.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" border="0" alt="CONTINUE SHOPPING" ALIGN="LEFT"></a></td>
<td WIDTH="640"><a href="/index.jsp"><img src="/media_stat/images/buttons/continue_shopping_text.gif"  border="0" alt="CONTINUE SHOPPING"></a>
<BR>from <FONT CLASS="text11bold"><A HREF="/index.jsp">Home Page</A></FONT><BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="340" HEIGHT="1" BORDER="0"></td>
</tr>
</TABLE>
</fd:CustomerProfileSurveyTag>
	</tmpl:put>
</tmpl:insert>
