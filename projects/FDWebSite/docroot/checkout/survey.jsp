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
<%@ page import='com.freshdirect.fdstore.survey.*' %>
<%@ taglib uri='logic' prefix='logic' %>
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

	//Commented out as it is never used. ErpSaleInfo firstOrder = yuzer.getOrderHistory().getLastSale();
	
	/*if(promoAvailable && (id % 4 == 0 || isManualRetention) && firstOrder.getRequestedDate().after(startPromo.getTime())) {
		incentivize = true;
	}*/
    FDSurvey Usability = FDSurveyCachedFactory.getSurvey(EnumSurveyType.SECOND_ORDER_SURVEY);
	FDSurveyResponse surveyResponse= FDCustomerManager.getCustomerProfileSurveyInfo(yuzer.getIdentity());
    //FDSurveyResponse registrationResponse= FDCustomerManager.getSurveyResponse(yuzer.getIdentity(),FDSurveyConstants.REGISTRATION_SURVEY);
    List questions = Usability.getQuestions();
    int quesCount = 1; 
    
%>
<tmpl:insert template='/common/template/no_space_border.jsp'>
    <tmpl:put name='title' direct='true'>Survey</tmpl:put>
    <tmpl:put name='content' direct='true'>
    
<fd:SurveyController successPage="/checkout/step_1_choose.jsp" resultName="result" formName="surveyForm">	
<form method="post" name="survey" action="survey.jsp">
<FONT CLASS="space4pix"><BR></FONT>

<TABLE BORDER="0" CELLP++++++++++++++++++++++++++++++++++ADDING="0" CELLSPACING="0" WIDTH="680">
<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"></td></tr>
<tr>
	<td colspan="2">
	<input type="hidden" name="skipSurvey" value="false">
		<font class="space8pix"><br></font>
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
	    <logic:iterate id="question" collection="<%= questions %>" type="com.freshdirect.fdstore.survey.FDSurveyQuestion" indexId='index'>
        <tr><td>
            
			    <div class="question">
				    <div class="question_number">
					<%=quesCount%>
				    </div>
			    <div class="question_text">
					<%=SurveyHtmlHelper.getQuestionText(question)%>
				</div>
				    <div class="cleft"><!--  --></div>
			    </div>
		   
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
					        
                              <%=SurveyHtmlHelper.getAnswersHtml(String.valueOf(quesCount),question,surveyResponse)%>
				
                         </div>   
                <%quesCount++;%>
                <div class="cleft"><!--  --></div>
         </tr></td>       
		</logic:iterate>
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