<%@ page import='java.util.*' %>
<%@ page import="com.freshdirect.fdstore.mail.*"%>
<%@ page import='com.freshdirect.storeapi.content.*'  %>
<%@ page import='com.freshdirect.storeapi.attributes.*'  %>
<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*"%>
<%@ page import="com.freshdirect.customer.*"%>
<%@ page import='com.freshdirect.fdstore.survey.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.common.customer.EnumServiceType"%>
<%@ page import="java.net.URLEncoder" %>
<%@ page import='java.text.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>

<% //expanded page dimensions
final int W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL = 970;
%>
<%
String successPage = "/your_account/customer_profile_summary.jsp";
String redirectPage = "/login/login.jsp?successPage=" + successPage;
request.setAttribute("sitePage", "www.freshdirect.com/your_account/customer_profile_summary.jsp");
request.setAttribute("listPos", "HPLeftTop");
%>

<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false' redirectPage='<%=redirectPage%>'/>
<%
    DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy EEEE");
    FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
    FDIdentity customerIdentity = null;
    ErpCustomerInfoModel customerInfo = null;
    if (user!=null && user.getLevel() == 2){
        customerIdentity = user.getIdentity();
        customerInfo = FDCustomerFactory.getErpCustomerInfo(customerIdentity);
    }

	FDCustomerModel customer = FDCustomerFactory.getFDCustomer(user.getIdentity());
	EnumServiceType serviceType = FDSurveyFactory.getServiceType(user, request);

	FDSurvey customerProfileSurvey = FDSurveyFactory.getInstance().getSurvey(EnumSurveyType.CUSTOMER_PROFILE_SURVEY, serviceType);
	FDSurveyResponse surveyResponse= FDSurveyFactory.getCustomerProfileSurveyInfo(customerIdentity, serviceType);
    int coverage=SurveyHtmlHelper.getResponseCoverage(customerProfileSurvey,surveyResponse);
    %>
    <% if(coverage==0) {%>
    <jsp:forward page='<%="/your_account/customer_profile.jsp?"+request.getQueryString()%>' />

    <%}%>

    <%
    List questions = null;
    String profileImagePath="";
    String[] birthDay=null;
    if(customerProfileSurvey!=null) {
        questions=customerProfileSurvey.getQuestions();
        if(surveyResponse!=null && surveyResponse.getAnswer(FDSurveyConstants.PROFILE)!=null) {
            profileImagePath=surveyResponse.getAnswer(FDSurveyConstants.PROFILE)[0].toLowerCase();
        }
        if(surveyResponse!=null && surveyResponse.getAnswer(FDSurveyConstants.BIRTHDAY)!=null) {
            birthDay=surveyResponse.getAnswer(FDSurveyConstants.BIRTHDAY);
        }

    }


response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control", "no-cache");
%>


<tmpl:insert template='/common/template/dnav.jsp'>
<tmpl:put name="seoMetaTag" direct="true">
	<fd:SEOMetaTag title="FreshDirect - Your Profile"  pageId="customer_profile_summary"></fd:SEOMetaTag>
</tmpl:put>
<%-- <tmpl:put name='title' direct='true'>FreshDirect - Your Profile</tmpl:put> --%>
<tmpl:put name='content' direct='true'>
<style>

	/* default styles from website css */
	body { font-family: Verdana, Arial, sans-serif; font-size: 10px; height: 100%; }
	a:link, a:visited { color:#360; }
	a:active { color:#f90; }
}

	/* these are summary styles. (combined with table styles) */
	.tQuestion { color: orange; font-size: 12px; font-weight: bold; width: 100%; border: 0; text-align: left; border-collapse: collapse; line-height: 24px; }
	.tQuestion td { vertical-align: top; }
	td.tIndex { width: 20px; }
	.tAnswer { padding: 6px 0 10px 10px; width: 100%; border-spacing: 0px; border-collapse: collapse; line-height: 24px; }
	.tAnswer td { padding: 0; }
	.tAnswer table { float: left; line-height: 24px; }
	.incr_input { font-size: 11px; text-align: center; width: 30px; }
	.vertLine { width: 1px; background-color: #ccc; }
	.vTop { vertical-align: top; }
	.col25per { width: 25%; }
	.col33per { width: 33.3%; }
	.col39per { width: 250px; }
	.col49per { width: 49%; }
	.col50per { width: 50%; }
	.col59per { width: 59%; }
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
	.padLR10px { padding-left: 10px; padding-right: 10px; }
	.padL0px { padding-left: 0; }
	.padL20px { padding-left: 20px; }
	.padL25px { padding-left: 25px; }
	.padB6px { padding-bottom: 6px; }
	.padB12px { padding-bottom: 12px; }
	.padTop0px { padding-top: 0; }
	.padTop30px { padding-top: 30px; }
	.rb_image { text-align: center; width: 110px; }
	.noBorder { border: 0; }
	.t11px { font-size: 11px; }
	.t12px { font-size: 12px; }
	.t20px { font-size: 20px; }
	.tOrange { color: orange; }
	.sumHead { padding: 2px; }
	.marTop0px { margin-top: 0; }
	.olNoInd, .olNoInd li { margin: 0; padding: 0; }
	.olNoInd { padding-left: 23px; }
	.clean, .clean td { padding: 0; margin: 0; border-spacing: 0; border-collapse: collapse; }
	.ico { width: 16px; }

</style>
<!-- * start the actual summary info * -->

	<table role="presentation" cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>
	<!-- left column -->
		<td class="col39per padLR10px vTop">
			<!-- profile image / edit profile link -->
            <table role="presentation" class="col100per noBorder">
				<tr>
				<%if(!"".equals(profileImagePath)){%>
					<td class="rb_image" style="padding-right:5px;">
						<img src=<%="/media_stat/images/profile/"+profileImagePath+".jpg"%> alt=""><br />
					</td>
				<%}%>
				</tr>
				<%if("".equals(profileImagePath)){%><tr><td colspan="2" style="padding-bottom:10px;"><img height="1" width="1" alt="" src="/media_stat/images/layout/clear.gif"/></td></tr><%}%>
			</table>
			<!-- NAME profile - customer info -->
				<table role="presentation" class="col100per noBorder tLeft">
				<tr>
					<td>
            <h1><%=user.getFirstName()+"'s Profile"%></h1>
					</td>
				</tr>
                <%if( user.getOrderHistory().getFirstNonPickupOrderDate()!=null &&
                      user.getOrderHistory().getLastOrderDlvDate()!=null
                  ){%>
				<tr>
					<td class="t11px">
						<span class="t11px bolded">Customer Since: </span><%= TimeslotPageUtil.formatFirstOrderYear(user.getOrderHistory().getFirstNonPickupOrderDate()) %>
					</td>
				</tr>
				<tr>
					<td class="t11px">
						<span class="t11px bolded">Last Ordered: </span><%=dateFormatter.format(user.getOrderHistory().getLastOrderDlvDate())%>
					</td>
				</tr>
                <%}%>
                <% if(birthDay!=null && birthDay.length==2){%>
				<tr>
					<td class="t11px">
						<span class="t11px bolded">Birthday: </span><%=birthDay[0]+" "+birthDay[1]%>
					</td>
				</tr>
                <%}%>
				</table>

			<!-- CHEF'S TABLE member -->
            <% if(user.isChefsTable()) { %>
	        <table role="presentation" class="col100per noBorder tLeft">
	         <tr><td width="15" style="padding-top:12px;"><img src="/media_stat/images/template/youraccount/ct_star.gif" alt="" width="15" height="15"></td><td class="t11px bolded" style="padding-top:12px;">CHEF'S TABLE MEMBER</td></tr>
	        </table>
	        <% } %>

			<!-- OAS ad -->
						<table role="presentation" cellpadding="0" cellspacing="0" border="0">
							<tr><td colspan="3" style="padding-top:12px;"><img height="1" width="1" alt="" src="/media_stat/images/layout/clear.gif"/></td></tr>
							<tr valign="top">
								<td><img height="6" width="6" alt="" src="/media_stat/images/layout/top_left_curve.gif"/></td>
								<td style="border-top: 1px solid #996;"><img height="1" width="204" alt="" src="/media_stat/images/layout/clear.gif"/></td>
								<td><img height="6" alt="" width="6" src="/media_stat/images/layout/top_right_curve.gif"/></td>
							</tr>
							<tr>
								<td align="center" style="border-left: 1px solid #996; border-right: 1px solid #996;" colspan="3">
									<!-- fake oas ad -->
									<%
                                    if (FDStoreProperties.isAdServerEnabled()) { %>
                                      <div id='oas_HPLeftTop'>
                						            <SCRIPT LANGUAGE="JavaScript">
                						              <!--
                						               OAS_AD('HPLeftTop');
                                          //-->
                						             </SCRIPT>
                                       </div>
					                <% } %>
								</td>
							  </tr>
							<tr valign="top">
								<td><img height="6" width="6" vspace="0" alt="" src="/media_stat/images/layout/bottom_left_curve.gif"/></td>
								<td style="border-bottom: 1px solid #996;"><img height="1" width="1" alt="" src="/media_stat/images/layout/clear.gif"/></td>
								<td><img height="6" alt="" width="6" vspace="0" src="/media_stat/images/layout/bottom_right_curve.gif"/></td>
							</tr>
						</table>
				<!-- OAS ad position goes here -->
		</td>
	<!-- end left column -->
	<!-- center column -->
		<td class="vertLine"><!--  --></td>
	<!-- end center column -->
	<!-- right column -->
		<td class="padL20px vTop tLeft t11px">
		    <table role="presentation" id="profileSwitcher">
		    	<tr class="t11px bolded tLeft">
			<%
			boolean needServiceType = false;
			if ((user.hasServiceBasedOnUserAddress(EnumServiceType.HOME) && user.hasServiceBasedOnUserAddress(EnumServiceType.CORPORATE)) || (request.getParameter("KRIKSZKRAKSZ")!=null)) {
			    needServiceType = true;
			%>
		    	 <td nowrap><% if (serviceType!=EnumServiceType.HOME) { %><a href="?serviceType=HOME">PERSONAL</a><% } else { %>PERSONAL<% } %></td>
		    	 <td>|</td>
		    	 <td nowrap><% if (serviceType!=EnumServiceType.CORPORATE) { %><a href="?serviceType=CORPORATE">Business or School</a><% } else { %>CORPORATE<% } %></td>
		    <% } %>
		       <td width="100%"></td>
				 <td  nowrap>
				 	<a href="/your_account/customer_profile.jsp<%= needServiceType ? "?serviceType="+ serviceType.name() : "" %>">Edit my profile</a>
				 </td>

		    	</tr>
		    </table>
		    <img width="120" height="10" alt="" src="/media_stat/images/layout/clear.gif"/>
			<table role="presentation" cellpadding="0" cellspacing="0" border="0" width="100%">

			<!-- header row -->

            <% if(questions!=null){%>
            <logic:iterate id="question" collection="<%= questions %>" type="com.freshdirect.fdstore.survey.FDSurveyQuestion" indexId='index'>

            <% if(!(FDSurveyConstants.PROFILE.equals(question.getName()) || FDSurveyConstants.BIRTHDAY.equals(question.getName())))
               {
            %>
            <% if(surveyResponse!=null && SurveyHtmlHelper.hasActiveAnswers(question,surveyResponse.getAnswerAsList(question.getName()))) {%>
                <tr>
                    <td class="padB6px">
                        <span class="t12px bolded tOrange"> <%=question.getShortDescr()%> </span>
                        <a href="<%="/your_account/customer_profile.jsp"+(needServiceType ? "?serviceType="+serviceType.name() : "")+'#'+question.getName()%>" title="">Edit</a></td>
                </tr>
               <%=SurveyHtmlHelper.getAnswers(question,surveyResponse.getAnswerAsList(question.getName()))%>
			   <tr><td><img height="8" width="1" alt="" src="/media_stat/images/layout/clear.gif"/></td></tr>
            <%} 
               }%>
            </logic:iterate>
            <%}%>
			</table>

		</td>
	<!-- end right column -->
    	</tr>
	</table>



<!-- * end the actual summary info * -->
<br><br>
<IMG src="/media_stat/images/layout/ff9933.gif" ALT="" WIDTH="<%= W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL %>" HEIGHT="1" BORDER="0"><BR>
<FONT CLASS="space4pix"><BR><BR></FONT>
<TABLE role="presentation" BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%= W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL %>">
<tr VALIGN="TOP">
<TD WIDTH="35"><a href="/index.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" border="0" alt="" ALIGN="LEFT">
CONTINUE SHOPPING
<BR>from <FONT CLASS="text11bold">Home Page</A></FONT><BR><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="340" HEIGHT="1" BORDER="0"></TD>
</tr>

</TABLE>


	</tmpl:put>
</tmpl:insert>
