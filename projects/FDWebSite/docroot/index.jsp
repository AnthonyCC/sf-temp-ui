<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.*'%>
<%@ page import='com.freshdirect.fdstore.FDReservation'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='java.text.*' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<fd:CheckLoginStatus guestAllowed='true' />
<%
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	String custFirstName = user.getFirstName();
	int validOrderCount = user.getAdjustedValidOrderCount();
	boolean mainPromo = user.getLevel() < FDUserI.RECOGNIZED && user.isEligibleForSignupPromotion();
        
        request.setAttribute("sitePage", "www.freshdirect.com/index.jsp");
        request.setAttribute("listPos", "SystemMessage,HPLeftTop,HPLeftMiddle,HPLeftBottom");
%>

<tmpl:insert template='/common/template/no_shell.jsp'>
	<tmpl:put name='title' direct='true'>Welcome to FreshDirect</tmpl:put>
	<tmpl:put name='content' direct='true'>
	
	<table width="745" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td><img src="/media_stat/images/layout/top_left_curve.gif" width="6" height="6"></td>
    <td style="border-top: solid 1px #999966;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
    <td><img src="/media_stat/images/layout/top_right_curve.gif" width="6" height="6"></td>
    <td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
    <td><img src="/media_stat/images/layout/top_left_curve.gif" width="6" height="6"></td>
    <td style="border-top: solid 1px #999966;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
    <td><img src="/media_stat/images/layout/top_right_curve.gif" width="6" height="6"></td>
  </tr>
  <tr> 
    <td colspan="3" align="center" style="border-left: solid 1px #999966; border-right: solid 1px #999966;"><%-- PROMO 1 --%><%if ( mainPromo ) {%>
				<%@ include file="includes/home/i_main_promo.jspf" %>
			<%} else if (FDStoreProperties.isAdServerEnabled()) {%>
                <SCRIPT LANGUAGE=JavaScript>
                <!--
                OAS_AD('HPLeftTop');
                //-->
      </SCRIPT>
    		<%}else {%>
            	<%@ include file="includes/home/i_current_promo.jspf" %>
			<%}%><%-- END PROMO 1 --%></td>
    <td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
    
    <!-- put home page letter logic here -->
    <%
          if(FDStoreProperties.IsHomePageMediaEnabled() && !user.isHomePageLetetrVisited()){
          
          String mediaPath=null;
          
          if(validOrderCount<1){
             
             mediaPath=FDStoreProperties.getHPLetterMediaPathForNewUser();
          
          }else{
          
            mediaPath=FDStoreProperties.getHPLetterMediaPathForOldUser();
          }
          
     %>
        <td colspan="3" rowspan="5" align="center" style="border-left: solid 1px #999966; border-right: solid 1px #999966;">
        <fd:IncludeMedia name="<%=mediaPath%>" />
        </td>
     <%    
            // update user already visited home page letter
            user.setHomePageLetetrVisited(true);
            // not sure we need to do this here because saveing cart too often is not recomended
          
              if(user instanceof FDSessionUser){                
                FDSessionUser sessionUser=(FDSessionUser)user;
                sessionUser.saveCart(true);          
              }
          
          }else{          
    %>
    
    <!-- else show regular -->
    <td colspan="3" rowspan="5" align="center" style="border-left: solid 1px #999966; border-right: solid 1px #999966;"><%-- MAIN CONTENT--%><%@ include file="includes/home/i_intro_hdr.jspf"%>
		<% if (user.getLevel() >= FDUserI.RECOGNIZED) { %>
			<%@ include file="includes/home/i_pending_order.jspf" %>
		<% } %>
		<%if(user.isEligibleForPreReservation() && user.getReservation() != null){
			FDReservation rsv = user.getReservation();
		%>
		<img src="/media_stat/images/layout/cccccc.gif" width="490" height="1" vspace="8"><table width="490" cellpadding="0" cellspacing="0" border="0"><tr><td><font class="text9"><b>You have a delivery slot reserved for:</b></font> <a href="/your_account/reserve_timeslot.jsp"><%=CCFormatter.formatReservationDate(rsv.getStartTime())%> @ <%=CCFormatter.formatTime(rsv.getStartTime())%> - <%=CCFormatter.formatDeliveryTime(rsv.getEndTime())%></a></td>
			</tr></table>
		<%}%>
		<img src="/media_stat/images/layout/cccccc.gif" width="490" height="1" vspace="8"><br>
		<%@ include file="/includes/i_departments.jspf" %>
	<%-- END MAIN CONTENT--%></td>
    <%   }  %> 
    
  </tr>
  <tr height="6"> 
    <td height="6"><img src="/media_stat/images/layout/bottom_left_curve.gif" width="6" height="6" vspace="0"></td>
    <td height="6" style="border-bottom: solid 1px #999966;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" vspace="0"></td>
    <td height="6"><img src="/media_stat/images/layout/bottom_right_curve.gif" width="6" height="6" vspace="0"></td>
    <td height="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" vspace="0"></td>
  </tr>
  <tr> 
    <td height="5" colspan="4"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" vspace="0"></td>
  </tr>
  <tr valign="bottom" height="6"> 
    <td height="6"><img src="/media_stat/images/layout/top_left_curve.gif" width="6" height="6" vspace="0"></td>
    <td height="6" style="border-top: solid 1px #999966;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" vspace="0"></td>
    <td height="6"><img src="/media_stat/images/layout/top_right_curve.gif" width="6" height="6" vspace="0"></td>
    <td height="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" vspace="0"></td>
  </tr>
  <tr> 
    <td colspan="3" align="center" style="border-left: solid 1px #999966; border-right: solid 1px #999966;"><%-- PROMO 2--%><%if (FDStoreProperties.isAdServerEnabled()) {%>
				<SCRIPT LANGUAGE=JavaScript>
                <!--
                OAS_AD('HPLeftMiddle');
                //-->
      </SCRIPT>
    <%}else {%>
      <a href="/about/index.jsp"><img src="/media_stat/images/template/homepages/promos/farm_fresh_hdr.gif" width="170" height="42" border="0"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br>Click here to learn more<br>about FreshDirect!<br><img src="/media_stat/images/layout/clear.gif" width="1" height="12" border="0"><br>
	  <img src="/media_stat/images/template/homepages/promos/corn.jpg" width="195" height="85" border="0" vspace="0"></a><%}%><%-- END PROMO 2--%></td>
    <td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
  </tr>
  <tr height="6"> 
    <td><img src="/media_stat/images/layout/bottom_left_curve.gif" width="6" height="6"></td>
    <td style="border-bottom: solid 1px #999966;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
    <td><img src="/media_stat/images/layout/bottom_right_curve.gif" width="6" height="6"></td>
    <td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
    <td><img src="/media_stat/images/layout/bottom_left_curve.gif" width="6" height="6"></td>
    <td style="border-bottom: solid 1px #999966;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
    <td><img src="/media_stat/images/layout/bottom_right_curve.gif" width="6" height="6"></td>
  </tr>
  <tr> 
    <td><img src="/media_stat/images/layout/clear.gif" width="6" height="5"></td>
    <td><img src="/media_stat/images/layout/clear.gif" width="204" height="1"></td>
    <td><img src="/media_stat/images/layout/clear.gif" width="6" height="1"></td>
    <td><img src="/media_stat/images/layout/clear.gif" width="5" height="1"></td>
    <td><img src="/media_stat/images/layout/clear.gif" width="6" height="1"></td>
    <td><img src="/media_stat/images/layout/clear.gif" width="512" height="1"></td>
    <td><img src="/media_stat/images/layout/clear.gif" width="6" height="1"></td>
  </tr>
  <tr valign="top"> 
    <td colspan="7">
    <%
      if(FDStoreProperties.IsHomePageMediaEnabled() && !user.isHomePageLetetrVisited()){
    %>
    <%--MEDIA INCLUDE--%><fd:IncludeMedia name="/media/editorial/home/home_bottom.html" /><%-- END MEDIA INCLUDE --%>
      <% } %>
    </td>
  </tr>
</table>
</tmpl:put>
</tmpl:insert>