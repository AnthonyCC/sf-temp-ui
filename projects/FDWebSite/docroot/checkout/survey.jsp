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

<fd:CheckLoginStatus id="yuzer" guestAllowed="false" recognizedAllowed="false"  /> 
<%!
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
    
    
%>
<tmpl:insert template='/common/template/no_space_border.jsp'>
    <tmpl:put name='title' direct='true'>Survey</tmpl:put>
    <tmpl:put name='content' direct='true'>
    
<fd:SurveyController successPage="/checkout/step_1_choose.jsp" resultName="result" formName="surveyForm">	
<form method="post" name="survey" action="survey.jsp">
<FONT CLASS="space4pix"><BR></FONT>

<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="700">
<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"></td></tr>
<input type="hidden" name="skipSurvey" value="false">
<tr>
 <td colspan="2" style="padding-bottom:6px;">
 <fd:IncludeMedia name="/media/editorial/site_pages/survey/sos_intro.html" />
 </td>
</tr>

<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>
<tr><td colspan="2" bgcolor="#999999"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" border="0"></td></tr>
<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>
<tr>
		<td>&nbsp;</td> 
		<td align=right><input type="image" src="/media_stat/images/template/checkout/skip_survey.gif" name="skip_survey"></td>
	</tr>
</TABLE>

<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="677">
<tr><td>
<TABLE WIDTH="675" CELLSPACING="0" CELLPADDING="0" BORDER="0">
<%
request.setAttribute("Survey",EnumSurveyType.SECOND_ORDER_SURVEY.getLabel());
FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
%>
<%@ include file="/includes/your_account/i_customer_profile.jspf" %>

</TABLE>
	</td>
</tr>
</table>
<br>
<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="700">
<tr>
 <td bgcolor="#999999" colspan=3><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" border="0"></td>
</tr>
<tr>
 <td colspan="3"><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"></td>
</tr>
<tr>
 <td colspan="3">
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
 <br>
 </td>
</tr>
 
</table>
</form>
</fd:SurveyController>
</tmpl:put>
</tmpl:insert>