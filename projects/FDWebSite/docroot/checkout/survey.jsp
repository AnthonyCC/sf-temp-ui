<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.framework.webapp.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.customer.ErpSaleInfo'%>
<%@ page import='com.freshdirect.common.pricing.Discount'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>

<fd:CheckLoginStatus id="yuzer" guestAllowed="false" recognizedAllowed="false"  /> 
<%!
	java.text.NumberFormat CURRENCY_FORMATTER = java.text.NumberFormat.getCurrencyInstance(Locale.US);
	
	private static Calendar startPromo = Calendar.getInstance();
        static {
            startPromo.set(2005, Calendar.MAY, 1, 0, 0, 0);
        }

%>
<% 
	boolean promoAvailable = PromotionFactory.getInstance().getPromotion("2ND_ORDER_SURVEY") != null;
	
	boolean multipleOrder = yuzer.getAdjustedValidOrderCount() > 1; 
	boolean incentivize = false;
	//int id = Integer.parseInt(yuzer.getIdentity().getFDCustomerPK());
	FDCustomerModel customer = FDCustomerFactory.getFDCustomer(yuzer.getIdentity());
    boolean isManualRetention = "true".equals(customer.getProfile().getAttribute("ManualRetention-$5")) ;

	ErpSaleInfo firstOrder = yuzer.getOrderHistory().getLastSale();
	
	/*if(promoAvailable && (id % 4 == 0 || isManualRetention) && firstOrder.getRequestedDate().after(startPromo.getTime())) {
		incentivize = true;
	}*/
%>
<tmpl:insert template='/common/template/no_space_border.jsp'>
    <tmpl:put name='title' direct='true'>Survey</tmpl:put>
    <tmpl:put name='content' direct='true'>
    
<fd:SurveyController successPage="/checkout/step_1_choose.jsp" resultName="result" formName="surveyForm">	
<form method="post" name="survey" action="survey.jsp">
<FONT CLASS="space4pix"><BR></FONT>
<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="680">
<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"></td></tr>
<tr>
	<td colspan="2">
	<input type="hidden" name="skipSurvey" value="false">
		<%-- if (incentivize) { %>
		<span class="title18" style="color: #990000;"><b>TELL US WHAT YOU THINK AND GET 10% OFF</b></span><br>
		<font class="space4pix"><br></font>
		Direct customer feedback is the best way for us to gauge how we're doing&mdash;that's why we'd like to offer you 10% off this order in exchange for a few minutes of your time. 
		< % } else { --%>
		<span class="title18" style="color: #990000;"><b>TELL US WHAT YOU THINK OF FRESHDIRECT</b></span><br>
		<font class="space4pix"><br></font>
		Direct customer feedback is the best way for us to gauge how we're doing-that's why we'd like to ask for a few minutes of your time.
		<%-- } --%>
		The survey is completely optional&mdash;just click "Skip Survey" to continue Checkout now. Of course, we keep all of your information private.  Whether you choose to fill out the survey or not, your feedback is always greatly appreciated!
		<br><font class="space8pix"><br></font>
	</td>
</TR>
<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>
<tr><td colspan="2" bgcolor="#999999"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" border="0"></td></tr>
<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>
<tr>
	<td colspan="2">
	<table width="100%" border=0 cellspacing=0 cellpadding=0>
	<tr><td><img src="/media_stat/images/layout/clear.gif" width="560" height="1" alt="" border="0"><br></td></tr>
	<tr>
		<td>&nbsp;</td> 
		<td align=right><input type="image" src="/media_stat/images/template/checkout/skip_survey.gif" name="skip_survey"></td>
	</tr>
	</table>
	</td>
</tr>
<tr><td colspan="3">
<%
String[] checkSurveyForm =	{"question1_prices", "question1_selection", "question1_quality", "question1_convenience", "question1_customerservice", 
											"question2_professional", "question2_prompt", 
											"question3_easeofuse", "question3_information", "question3_speed", 
											"question4_favoritecuisine", 
											"question5_cookingattitude",
											"question6_importantnutrition", 
											"question7_foodallergy", 
											"question8_weeklyspend_store", "question8_weeklyspend_restaurant",
											"question9_numpeopleshopfor", 
											"question10_household"}; 
%>
<fd:ErrorHandler result='<%=result%>' field='<%=checkSurveyForm%>'>
	<% String errorMsg = SystemMessageList.MSG_MISSING_SURVEY_INFO; %>
	<%@ include file="/includes/i_error_messages.jspf" %>
</fd:ErrorHandler>
</td></tr>
</TABLE>

<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="677">
<tr><td>
<TABLE WIDTH="675" CELLSPACING="0" CELLPADDING="0" BORDER="0">

<TR VALIGN="TOP">
<TD WIDTH="10" ALIGN="RIGHT">&nbsp;<b>1.</b>&nbsp;</TD>
<TD WIDTH="665">
	<% String[] checkQuestion1 = {"question1_prices", "question1_selection", "question1_quality", "question1_convenience", "question1_customerservice"}; %>
	<fd:ErrorHandler result="<%=result%>" field="<%=checkQuestion1%>"><span class="text11rbold"></fd:ErrorHandler><b>What do you think about FreshDirect?</b><fd:ErrorHandler result="<%=result%>" field="<%=checkQuestion1%>"></span></fd:ErrorHandler><br>
	<img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"><br>
	<table border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><img src="/media_stat/images/layout/clear.gif" width="30" height="1" alt="" border="0"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="80" height="1" alt="" border="0"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="80" height="1" alt="" border="0"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="80" height="1" alt="" border="0"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="80" height="1" alt="" border="0"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="80" height="1" alt="" border="0"></td>
		<td></td>
	</tr>
	<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>
	<tr>
		<td></td>
		<td></td>	
		<td align=center><i>Excellent</i></td>
		<td align=center><i>Good</i></td>
		<td align=center><i>Satisfactory</i></td>
		<td align=center><i>Poor</i></td>
		<td></td>
	</tr>
	
	<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>	
	<tr>
		<td></td>
		<td>Prices</td>
		<td align=center><%surveyForm.getElement("question1_prices").print(out, null);%></td>
		<td><fd:ErrorHandler result="<%=result%>" name="question1_prices" id="errorMsg">&nbsp;&nbsp;<span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
	</tr>
	
	<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>	
	<tr >
		<td></td>
		<td bgcolor="#eeeeee">Selection</td>
		<td align=center bgcolor="#eeeeee"><%surveyForm.getElement("question1_selection").print(out, null);%></td>
		<td><fd:ErrorHandler result="<%=result%>" name="question1_selection" id="errorMsg">&nbsp;&nbsp;<span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>		
	</tr>
	
	<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>	
	<tr>
		<td></td>
		<td>Quality</td>
		<td align=center><%surveyForm.getElement("question1_quality").print(out, null);%></td>
		<td><fd:ErrorHandler result="<%=result%>" name="question1_quality" id="errorMsg">&nbsp;&nbsp;<span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>	
	</tr>

	<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>	
	<tr>
		<td></td>
		<td bgcolor="#eeeeee">Convenience</td>
		<td align=center bgcolor="#eeeeee"><%surveyForm.getElement("question1_convenience").print(out, null);%></td>
		<td><fd:ErrorHandler result="<%=result%>" name="question1_convenience" id="errorMsg">&nbsp;&nbsp;<span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
	</tr>
	
	<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>	
	<tr>
		<td></td>
		<td>Customer Service</td>
		<td align=center><%surveyForm.getElement("question1_customerservice").print(out, null);%></td>
		<td><fd:ErrorHandler result="<%=result%>" name="question1_customerservice" id="errorMsg">&nbsp;&nbsp;<span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
	</tr>
	</table>
</TD>
</TR>
<TR><TD WIDTH="675" COLSPAN="2"><FONT CLASS="space8pix"><BR></FONT><FONT CLASS="space2pix"><BR></FONT></TD></TR> 


<TR VALIGN="TOP">
<TD WIDTH="10" ALIGN="RIGHT">&nbsp;<b>2.</b>&nbsp;</TD>
<TD WIDTH="665">
	<% String[] checkQuestion2 = {"question2_professional", "question2_prompt"}; %>
	<fd:ErrorHandler result="<%=result%>" field="<%=checkQuestion2%>"><span class="text11rbold"></fd:ErrorHandler><b>How would you rate our delivery?</b><fd:ErrorHandler result="<%=result%>" field="<%=checkQuestion2%>"></span></fd:ErrorHandler><br>
	<img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"><br>
	<table border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><img src="/media_stat/images/layout/clear.gif" width="30" height="1" alt="" border="0"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="80" height="1" alt="" border="0"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="80" height="1" alt="" border="0"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="80" height="1" alt="" border="0"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="80" height="1" alt="" border="0"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="80" height="1" alt="" border="0"></td>
		<td></td>
	</tr>
	<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>
	<tr>
		<td></td>
		<td></td>	
		<td align=center><i>Excellent</i></td>
		<td align=center><i>Good</i></td>
		<td align=center><i>Satisfactory</i></td>
		<td align=center><i>Poor</i></td>
		<td></td>
	</tr>

	<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>	
	<tr>
		<td></td>
		<td>Professional</td>
		<td align=center><%surveyForm.getElement("question2_professional").print(out, null);%></td>
		<td><fd:ErrorHandler result="<%=result%>" name="question2_professional" id="errorMsg">&nbsp;&nbsp;<span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>	
	</tr>
	
	<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>	
	<tr>
		<td></td>
		<td bgcolor="#eeeeee">Prompt</td>
		<td align=center bgcolor="#eeeeee"><%surveyForm.getElement("question2_prompt").print(out, null);%></td>
		<td><fd:ErrorHandler result="<%=result%>" name="question2_prompt" id="errorMsg">&nbsp;&nbsp;<span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>	
	</tr>
	</table>
</TD>
</TR>
<TR><TD WIDTH="675" COLSPAN="2"><FONT CLASS="space8pix"><BR></FONT><FONT CLASS="space2pix"><BR></FONT></TD></TR> 


<TR VALIGN="TOP">
<TD WIDTH="10" ALIGN="RIGHT">&nbsp;<b>3.</b>&nbsp;</TD>
<TD WIDTH="665">
	<% String[] checkQuestion3 = {"question3_easeofuse", "question3_information", "question3_speed"}; %>
	<fd:ErrorHandler result="<%=result%>" field="<%=checkQuestion3%>"><span class="text11rbold"></fd:ErrorHandler><b>How would you rate the FreshDirect Web Site?</b><fd:ErrorHandler result="<%=result%>" field="<%=checkQuestion3%>"></span></fd:ErrorHandler><br>
	<img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"><br>
	<table border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><img src="/media_stat/images/layout/clear.gif" width="30" height="1" alt="" border="0"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="80" height="1" alt="" border="0"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="80" height="1" alt="" border="0"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="80" height="1" alt="" border="0"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="80" height="1" alt="" border="0"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="80" height="1" alt="" border="0"></td>
		<td></td>
	</tr>
	<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>
	<tr>
		<td></td>
		<td></td>	
		<td align=center><i>Excellent</i></td>
		<td align=center><i>Good</i></td>
		<td align=center><i>Satisfactory</i></td>
		<td align=center><i>Poor</i></td>
		<td></td>
	</tr>
	<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>	
	<tr>
		<td></td>
		<td>Ease of Use</td>
		<td align=center><%surveyForm.getElement("question3_easeofuse").print(out, null);%></td>
		<td><fd:ErrorHandler result="<%=result%>" name="question3_easeofuse" id="errorMsg">&nbsp;&nbsp;<span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>	
	</tr>
	<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>	
	<tr>
		<td></td>
		<td bgcolor="#eeeeee">Information</td>
		<td align=center bgcolor='#eeeeee'><%surveyForm.getElement("question3_information").print(out, null);%></td>
		<td><fd:ErrorHandler result="<%=result%>" name="question3_information" id="errorMsg">&nbsp;&nbsp;<span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>	
	</tr>
	<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>	
	<tr>
		<td></td>
		<td>Speed</td>
		<td align=center><%surveyForm.getElement("question3_speed").print(out, null);%></td>
		<td><fd:ErrorHandler result="<%=result%>" name="question3_speed" id="errorMsg">&nbsp;&nbsp;<span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>	
	</tr>
	</table>
</TD>
</TR>
<TR><TD WIDTH="675" COLSPAN="2"><FONT CLASS="space8pix"><BR></FONT><FONT CLASS="space2pix"><BR></FONT></TD></TR> 

<TR VALIGN="TOP">
<TD WIDTH="10" ALIGN="RIGHT">&nbsp;<b>4.</b>&nbsp;</TD>
<TD WIDTH="665"><fd:ErrorHandler result="<%=result%>" name="question4_favoritecuisine"><span class="text11rbold"></fd:ErrorHandler><b>Which of the following are your favorite cuisines?</b> (Check all that apply)<fd:ErrorHandler result="<%=result%>" name="question4_favoritecuisine"></span></fd:ErrorHandler><fd:ErrorHandler result="<%=result%>" name="question4_favoritecuisine" id="errorMsg">&nbsp;&nbsp;<span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler><br>
<%surveyForm.getElement("question4_favoritecuisine").print(out, null);%>
<%surveyForm.getElement("question4_favoritecuisine_other").print(out, null);%>
</TD>
</TR>
<TR><TD WIDTH="675" COLSPAN="2"><FONT CLASS="space8pix"><BR></FONT></TD></TR>

<TR VALIGN="TOP">
<TD WIDTH="10" ALIGN="RIGHT">&nbsp;<b>5.</b>&nbsp;</TD>
<TD WIDTH="665"><fd:ErrorHandler result="<%=result%>" name="question5_cookingattitude"><span class="text11rbold"></fd:ErrorHandler><b>How would you describe your attitudes toward cooking?</b> (Check all that apply)<fd:ErrorHandler result="<%=result%>" name="question5_cookingattitude"></span></fd:ErrorHandler><fd:ErrorHandler result="<%=result%>" name="question5_cookingattitude" id="errorMsg">&nbsp;&nbsp;<span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler><br>
<%surveyForm.getElement("question5_cookingattitude").print(out, null);%>
</TD>
</TR>
<TR><TD WIDTH="675" COLSPAN="2"><FONT CLASS="space8pix"><BR></FONT></TD></TR>

<TR VALIGN="TOP">
<TD WIDTH="10" ALIGN="RIGHT">&nbsp;<b>6.</b>&nbsp;</TD>
<TD WIDTH="665"><fd:ErrorHandler result="<%=result%>" name="question6_importantnutrition"><span class="text11rbold"></fd:ErrorHandler><b>When you shop for food, what specific nutritional/dietary factors are important to you?</b> (Check all that apply)<fd:ErrorHandler result="<%=result%>" name="question6_importantnutrition"></span></fd:ErrorHandler><fd:ErrorHandler result="<%=result%>" name="question6_importantnutrition" id="errorMsg">&nbsp;&nbsp;<span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler><br>
<%surveyForm.getElement("question6_importantnutrition").print(out, null);%><%surveyForm.getElement("question6_importantnutrition_other").print(out, null);%>
</TD>
</TR>
<TR><TD WIDTH="675" COLSPAN="2"><FONT CLASS="space8pix"><BR></FONT></TD></TR>

<TR VALIGN="TOP">
<TD WIDTH="10" ALIGN="RIGHT">&nbsp;<b>7.</b>&nbsp;</TD>
<TD WIDTH="665"><fd:ErrorHandler result="<%=result%>" name="question7_foodallergy"><span class="text11rbold"></fd:ErrorHandler><b>Does anyone in your household have food sensitivities?</b> (Check all that apply)<fd:ErrorHandler result="<%=result%>" name="question7_foodallergy"></span></fd:ErrorHandler><fd:ErrorHandler result="<%=result%>" name="question7_foodallergy" id="errorMsg">&nbsp;&nbsp;<span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler><br>
<%surveyForm.getElement("question7_foodallergy").print(out, null);%><%surveyForm.getElement("question7_foodallergy_other").print(out, null);%>
</TD>
</TR>
<TR><TD WIDTH="675" COLSPAN="2"><FONT CLASS="space8pix"><BR></FONT></TD></TR>

<TR VALIGN="TOP">
<TD WIDTH="10" ALIGN="RIGHT">&nbsp;<b>8.</b>&nbsp;</TD>
<% String[] checkQuestion8 = {"question8_weeklyspend_store", "question8_weeklyspend_restaurant"}; %>
<TD WIDTH="665"><fd:ErrorHandler result="<%=result%>" field="<%=checkQuestion8%>"><span class="text11rbold"></fd:ErrorHandler><b>How much do you typically spend each week at:</b><fd:ErrorHandler result="<%=result%>" field="<%=checkQuestion8%>"></span></fd:ErrorHandler><fd:ErrorHandler result="<%=result%>" field="<%=checkQuestion8%>" id="errorMsg">&nbsp;&nbsp;<span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler><br>
<table width="100%">
<tr>
<td width="50%">A) <fd:ErrorHandler result="<%=result%>" name="question8_weeklyspend_store" id="errorMsg"><span class="text11rbold"></fd:ErrorHandler><i>grocery stores, specialty food stores, and delis</i><fd:ErrorHandler result="<%=result%>" name="question8_weeklyspend_store" id="errorMsg"></span></fd:ErrorHandler></td>
<td width="50%">B) <fd:ErrorHandler result="<%=result%>" name="question8_weeklyspend_restaurant" id="errorMsg"><span class="text11rbold"></fd:ErrorHandler><i>restaurants, including takeout</i><fd:ErrorHandler result="<%=result%>" name="question8_weeklyspend_restaurant" id="errorMsg"></span></fd:ErrorHandler></td>
</tr>
<tr>
<td width="50%"><%surveyForm.getElement("question8_weeklyspend_store").print(out, null);%></td>
<td width="50%"><%surveyForm.getElement("question8_weeklyspend_restaurant").print(out, null);%></td>
</tr>
</table>
</TD>
</TR>
<TR><TD WIDTH="675" COLSPAN="2"><FONT CLASS="space8pix"><BR></FONT></TD></TR>

<TR VALIGN="TOP">
<TD WIDTH="10" ALIGN="RIGHT">&nbsp;<b>9.</b>&nbsp;</TD>
<TD WIDTH="665"><fd:ErrorHandler result="<%=result%>" name="question9_numpeopleshopfor"><span class="text11rbold"></fd:ErrorHandler><b>When you shop for food, how many people do you typically shop for?</b><fd:ErrorHandler result="<%=result%>" name="question9_numpeopleshopfor"></span></fd:ErrorHandler><fd:ErrorHandler result="<%=result%>" name="question9_numpeopleshopfor" id="errorMsg">&nbsp;&nbsp;<span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler><br>
<%surveyForm.getElement("question9_numpeopleshopfor").print(out, null);%>
</TD>
</TR>
<TR><TD WIDTH="675" COLSPAN="2"><FONT CLASS="space8pix"><BR></FONT></TD></TR>

<TR VALIGN="TOP">
<TD WIDTH="10" ALIGN="RIGHT">&nbsp;<b>10.</b>&nbsp;</TD>
<TD WIDTH="665"><fd:ErrorHandler result="<%=result%>" name="question10_household"><span class="text11rbold"></fd:ErrorHandler><b>Which best describes your household?</b><fd:ErrorHandler result="<%=result%>" name="question10_household"></span></fd:ErrorHandler><fd:ErrorHandler result="<%=result%>" name="question10_household" id="errorMsg">&nbsp;&nbsp;<span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler><br>
<%surveyForm.getElement("question10_household").print(out, null);%>
</TD>
</TR>
<TR><TD WIDTH="675" COLSPAN="2"><FONT CLASS="space8pix"><BR></FONT></TD></TR>

<TR VALIGN="TOP">
<TD WIDTH="10" ALIGN="RIGHT">&nbsp;<b>11.</b>&nbsp;</TD>
<TD WIDTH="665"><b>When is your birthday?</b> (Optional)<br><FONT CLASS="space4pix"><BR></FONT>
&nbsp;&nbsp;<%surveyForm.getElement("question11_birthday").print(out, null);%><%surveyForm.getElement("question11_birthmonth").print(out, null);%><%surveyForm.getElement("question11_birthyear").print(out, null);%>
</TD>
</TR>
<TR><TD WIDTH="675" COLSPAN="2"><FONT CLASS="space8pix"><BR></FONT><FONT CLASS="space2pix"><BR></FONT></TD></TR>

<TR VALIGN="TOP">
<TD WIDTH="10" ALIGN="RIGHT">&nbsp;<b>12.</b>&nbsp;</TD>
<TD WIDTH="665"><b>What is your gender?</b> (Optional)<br><FONT CLASS="space4pix"><BR></FONT>
<%surveyForm.getElement("question12_gender").print(out, null);%>
</TD>
</TR>
<TR><TD WIDTH="675" COLSPAN="2"><FONT CLASS="space8pix"><BR></FONT><FONT CLASS="space2pix"><BR></FONT></TD></TR>

<TR VALIGN="TOP">
<TD WIDTH="10" ALIGN="RIGHT">&nbsp;<b>13.</b>&nbsp;</TD>
<TD WIDTH="665"><fd:ErrorHandler result="<%=result%>" name="question13_recommend"><span class="text11rbold"></fd:ErrorHandler><b>Would you recommend FreshDirect to a friend?</b><fd:ErrorHandler result="<%=result%>" name="question13_recommend"></span></fd:ErrorHandler><fd:ErrorHandler result="<%=result%>" name="question13_recommend" id="errorMsg"> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
	<table border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><img src="/media_stat/images/layout/clear.gif" width="70" height="6" alt="" border="0"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="70" height="6" alt="" border="0"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="70" height="6" alt="" border="0"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="70" height="6" alt="" border="0"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="70" height="6" alt="" border="0"></td>
	</tr>
	<tr></tr>
		<td  colspan="3"><i>Definitely No</i></td>	
		<td colspan="2" align="right"><i>Definitely Yes</i></td>
	</tr>
	<tr>
		<td align="center"><%surveyForm.getElement("question13_recommend").print(out, null);%></td>	
	</tr>
	</table>
</TD>
</TR>
<TR><TD WIDTH="675" COLSPAN="2"><FONT CLASS="space8pix"><BR></FONT><FONT CLASS="space2pix"><BR></FONT></TD></TR>

<TR VALIGN="TOP">
<TD WIDTH="10" ALIGN="RIGHT">&nbsp;<b>14.</b>&nbsp;</TD>
<TD WIDTH="665"><fd:ErrorHandler result="<%=result%>" name="question14_additional_comments"><span class="text11rbold"></fd:ErrorHandler><b>Additional comments about FreshDirect</b> (Optional)<fd:ErrorHandler result="<%=result%>" name="question14_additional_comments"></span></fd:ErrorHandler><fd:ErrorHandler result="<%=result%>" name="question14_additional_comments" id="errorMsg"> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler><BR>
<%surveyForm.getElement("question14_additional_comments").print(out, "cols='40' rows='7' wrap='virtual'");%><BR>
<BR>
</TD>
</TR>
<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="8" alt="" border="0"><br>
<img src="/media_stat/images/layout/cccccc.gif" width="100%" height="1">
<table width="100%" cellpadding="0" cellspacing="0">
	<tr><td colspan="4" valign="top"><img src="/media_stat/images/template/corp/food.jpg" width="229" height="79" vspace="8" align="right"><br><span style="font-size: 11px; font-weight: bold; color:#000099;">We now deliver to the office!</span> If you work for a mid-town or Downtown Manhattan company that either stocks an office pantry/kitchen with food or regularly orders platters/catering for business meetings, we'd like to see if your company is interested in ordering from FreshDirect. We'd appreciate the following information and permission to use your name as a reference:</td></tr>
	<tr>
		<td class="text12" align="right"><strong>Company name:</strong>&nbsp;&nbsp;</td>
		<td width="180"><%surveyForm.getElement("questionCOS1_company").print(out, null);%><%--input type="text" size="25" maxlength="200"--%></td>
		<td class="text12" align="right"><strong>Person ordering food:</strong>&nbsp;&nbsp;</td>
		<td width="180"><%surveyForm.getElement("questionCOS2_contact").print(out, null);%><%--input type="text" size="25" maxlength="200"--%></td>
	</tr>
	</table><br>
</td></tr>
</TABLE>
	</td>
</tr>
</table>
<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="677">
<tr>
	<td bgcolor="#999999" colspan=3><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" border="0"></td>
</tr>
<tr>
	<td colspan=3><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"></td>
</tr>
<tr>
	<td colspan=3>
		
		<table>
		<tr>
			<td valign="top">
				<input type="image"  src="/media_stat/images/buttons/checkout_arrow.gif" WIDTH="29" HEIGHT="29" name="submit_survey">
			</td>
			<td><img src="/media/images/layout/clear.gif" width="2" height="1" alt="" border="0"></td>
			<td valign="top">
				<input type="image"  src="/media_stat/images/template/checkout/submit_survey.gif" name="submit_survey"><br>
				To continue Checkout.<br>
				Thank you for your feedback!
			</td>
		</tr>
		</table>
	
	</td>
</tr>

</table>
</form>
</fd:SurveyController>
</tmpl:put>
</tmpl:insert>