<%@ page import="com.freshdirect.fdstore.mail.*"%>
<%@ page import='com.freshdirect.fdstore.content.*'  %>
<%@ page import='com.freshdirect.fdstore.attributes.*'  %>
<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*"%>
<%@ page import="com.freshdirect.customer.*"%>
<%@ page import='com.freshdirect.fdstore.survey.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>

<%@ page import="java.net.URLEncoder" %>
<%@ page import='java.text.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%
String successPage = "/your_account/customer_profile_summary.jsp";
String redirectPage = "/login/login.jsp?successPage=" + successPage;
request.setAttribute("sitePage", "www.freshdirect.com/your_account/customer_profile_summary.jsp");
request.setAttribute("listPos", "SystemMessage,HPLeftTop");
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
	FDSurvey customerProfileSurvey = FDSurveyCachedFactory.getSurvey(EnumSurveyType.CUSTOMER_PROFILE_SURVEY);
	FDSurveyResponse surveyResponse= FDCustomerManager.getCustomerProfileSurveyInfo(customerIdentity);
    int coverage=com.freshdirect.webapp.taglib.fdstore.SurveyHelper.getResponseCoverage(customerProfileSurvey,surveyResponse);
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
	.col39per { width: 39%; }	
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

<tmpl:insert template='/common/template/dnav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Your Profile</tmpl:put>
<tmpl:put name='content' direct='true'>

<!-- * start the actual summary info * -->

	<table cellpadding="0" cellspacing="0" border="0" width="693px">
	<tr>
	<!-- left column -->
		<td class="col39per padLR10px vTop">
			<!-- profile image / edit profile link -->
            <%if(!"".equals(profileImagePath)){%>
				<table class="col100per noBorder">
				<tr>
					<td class="rb_image">
						<img src=<%="/media_stat/images/profile/"+profileImagePath+".jpg"%>><br />
					</td>
					<!--<td class="ico">
						<img src="edit.gif" width="16" height="16" border="0" alt="" title="">
					</td>-->
					<td class="t12px bolded tLeft"><a href="/your_account/customer_profile.jsp" title="">Edit my profile</a></td>
				</tr>
				</table>
                <%} else {%>
                <table class="col100per noBorder">
				<tr>
					<td class="t12px bolded tLeft"><a href="/your_account/customer_profile.jsp" title="">Edit my profile</a></td>
				</tr>
				</table>
                
                <%}%>
			<!-- NAME profile - customer info -->
				<table class="col100per noBorder tLeft">
				<tr>
					<td class="t20px bolded">
						<%=user.getFirstName()+"'s Profile"%>
					</td>
				</tr>
                <%if( user.getOrderHistory().getFirstNonPickupOrderDate()!=null &&
                      user.getOrderHistory().getLastOrderDlvDate()!=null
                  ){%>
				<tr>
					<td>
						<span class="bolded">Customer Since: </span><%= TimeslotPageUtil.formatFirstOrderYear(user.getOrderHistory().getFirstNonPickupOrderDate()) %>
					</td>
				</tr>
				<tr>
					<td>
						<span class="bolded">Last Ordered: </span><%=dateFormatter.format(user.getOrderHistory().getLastOrderDlvDate())%>
					</td>
				</tr>
                <%}%>
                <% if(birthDay!=null){%>
				<tr>
					<td>
                        
						<span class="bolded">Birthday: </span><%=birthDay[0]+" "+birthDay[1]%>
					</td>
				</tr>
                <%}%>
				</table>
				<br /><br />

			<!-- CHEF'S TABLE member -->
            <% if(user.isChefsTable()) { %>
	        <table class="col100per noBorder tLeft">
	         <tr><td class="t12px bolded">CHEF'S TABLE MEMBER</td></tr>
	        </table>
	        <% } %>
				<br /><br />


			<!-- OAS ad -->
				<table>
				<tr>
					<td style="text-align: right;">
						<table class="clean">
							<tr> 
								<td height="5"><img height="6" width="6" src="top_left_curve.gif"/></td>
								<td height="5" style="border-top: 1px solid #996;"><img height="1" width="204" src="clear.gif"/></td>
								<td height="5"><img height="6" width="6" src="top_right_curve.gif"/></td>
							</tr>
							<tr>
								<td align="center" style="border-left: 1px solid #996; border-right: 1px solid #996;" colspan="3">
									<!-- fake oas ad -->
									<% if (FDStoreProperties.isAdServerEnabled()) { %>
						                <SCRIPT LANGUAGE=JavaScript>
						                    <!--
						                    OAS_AD('HPTopLeft');
                                        //-->
						             </SCRIPT>
					                <% } %>
								</td>
							  </tr>
							<tr>
								<td height="5"><img height="6" width="6" vspace="0" src="bottom_left_curve.gif"/></td>
								<td height="5" style="border-bottom: 1px solid #996;"><img height="1" width="1" src="clear.gif"/></td>
								<td height="5"><img height="6" width="6" vspace="0" src="bottom_right_curve.gif"/></td>
							</tr>
						</table>
					</td>
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
			<table cellpadding="0" cellspacing="0" border="0" width="100%">

			<!-- header row -->
            
            <% if(questions!=null){%>
            <logic:iterate id="question" collection="<%= questions %>" type="com.freshdirect.fdstore.survey.FDSurveyQuestion" indexId='index'>
            
            <% if(FDSurveyConstants.PROFILE.equals(question.getName()))
               continue;
            %>
            <% if(surveyResponse!=null && !surveyResponse.getAnswerAsList(question.getName()).isEmpty()) {%>
                <tr>
                    <td class="padB6px">
                        <span class="t12px bolded tOrange"> <%=question.getShortDescr()%> </span>
                        <a href="/your_account/customer_profile.jsp" title="">Edit</a></td>
                </tr>
                <!-- answer row -->
               
                <logic:iterate id="answer" collection="<%= surveyResponse.getAnswerAsList(question.getName()) %>" type="java.lang.String">
                <tr>
                    <td class="padB12px">
                        <div><%=answer%></div>
                    </td>
                </tr>
            </logic:iterate>
            <%}%>
            </logic:iterate>
            <%}%>
			</table>
			
		</td>
	<!-- end right column -->
    	</tr>
	</table>
	


<!-- * end the actual summary info * -->
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


	</tmpl:put>
</tmpl:insert>
