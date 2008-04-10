<%@ page import='com.freshdirect.common.customer.*,com.freshdirect.fdstore.*' %>
<%@ page import ='com.freshdirect.fdstore.customer.*'%>
<%@ page import ='com.freshdirect.fdstore.survey.*'%>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import="java.text.SimpleDateFormat, java.util.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.framework.webapp.*'%>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />

<tmpl:insert template='/common/template/blank.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - Order Placed</tmpl:put>
<tmpl:put name='content' direct='true'>

<%
//--------OAS Page Variables-----------------------
        request.setAttribute("sitePage", "www.freshdirect.com/checkout");
        request.setAttribute("listPos", "ReceiptTop,ReceiptBotLeft,ReceiptBotRight,SystemMessage,CategoryNote");
  
String orderNumber = (String)session.getAttribute(SessionName.RECENT_ORDER_NUMBER);
%>
<jsp:include page="/common/template/includes/ad_server.jsp" flush="false"/>

<fd:GetOrder id='cart' saleId='<%=orderNumber%>'>

<%  

        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MM/dd/yy");

        FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
        FDIdentity identity  = user.getIdentity();
    
%>

<%@include file="/includes/tacoda/tac_receipt.jspf"%>

<script language="JavaScript">
<!-- //TACODA

isSecureProtocol = false;
if(document.location.href.substring(0,5) == 'https'){
  isSecureProtocol = true;
}

if (isSecureProtocol) {
  document.write('<img width="0" height="0" src="'+secure1+'">');
    document.write('<img width="0" height="0" src="'+secure2+'">');
} else {
  document.write('<img width="0" height="0" src="'+notSecure1+'">');
   document.write('<img width="0" height="0" src="'+notSecure2+'">');
}

//-->
</script>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="630">
<TR VALIGN="BOTTOM">      
    <TD colspan="2" WIDTH="630" ALIGN="RIGHT">
    <%
  if (user.isChefsTable()) {        
  %>
        <a href="/your_account/manage_account.jsp"><img src="/media_stat/images/template/checkout/loy_global_member_stars_2008.gif" width="314" height="9" alt="CLICK HERE FOR EXCLUSIVE CHEF'S TABLE OFFERS" vspace="0" border="0"/></a><br />
    <!-- <img src="/media_stat/images/template/checkout/loy_global_member_stars.gif" width="170" height="9" alt="Chef's Table Member" vspace="2"/> -->
  <%
  }
  %>
    </TD>
</TR>
<TR VALIGN="BOTTOM">
  <TD WIDTH="430"><A HREF="/index.jsp"><img src="/media_stat/images/logos/fd_logo_md.gif" width="216" height="42" border="0" alt="FreshDirect"></A><img src="/media_stat/images/layout/clear.gif" width="95" height="1"><img src="/media_stat/images/navigation/egplnt_reg_checkout.jpg" WIDTH="87" HEIGHT="69" border="0" alt="Eggplant" HSPACE="10"></TD>
  <TD WIDTH="200" ALIGN="right">    
        <A HREF="javascript:window.print();" onMouseOver="swapImage('print','/media/images/navigation/global_nav/print_page_01.gif')" onMouseOut="swapImage('print','/media/images/navigation/global_nav/print_page.gif')"><img name="print" src="/media_stat/images/navigation/global_nav/print_page.gif" width="54" height="26" border="0" alt="PRINT PAGE"></A>&nbsp;&nbsp;<IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="25">&nbsp;&nbsp;<A HREF="/index.jsp" onMouseOver="swapImage('home_top','/media/images/navigation/global_nav/go_home_01.gif')" onMouseOut="swapImage('home_top','/media/images/navigation/global_nav/go_home.gif')"><img name="home_top" src="/media_stat/images/navigation/global_nav/go_home.gif" width="56" height="26" border="0" alt="GO HOME"></A>&nbsp;&nbsp;<IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="25">&nbsp;&nbsp;<A HREF="/quickshop/index.jsp" onMouseOver="swapImage('quickshop','/media_stat/images/navigation/global_nav/nav_button_quickshop_r.gif')" onMouseOut="swapImage('quickshop','/media_stat/images/navigation/global_nav/nav_button_quick_shop.gif')"><img name="quickshop" src="/media_stat/images/navigation/global_nav/nav_button_quick_shop.gif" width="54" height="26" border="0" alt="QUICKSHOP"></A>
  </TD>
</TR>
<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>
<tr>
  <td colspan="2"><IMG src="/media_stat/images/layout/669933.gif" HEIGHT="4" WIDTH="630"></td>
</tr>
</TABLE>
<%@include file="/checkout/includes/i_checkout_receipt.jspf"%>
<% if(cart != null) { %>
<iframe src="https://fls.doubleclick.net/activityi;src=1495506;type=sales288;cat=recei992;qty=1;cost=<%= cart.getTotal() %>;ord=<%=orderNumber%>?" width=1 height=1 frameborder=0></iframe>
<% } %>

<!-- Google Code for purchase Conversion Page -->
<script language="JavaScript" type="text/javascript">
<!--
  var google_conversion_id = 1068815947;
  var google_conversion_language = "en_US";
  var google_conversion_format = "1";
  var google_conversion_color = "FFFFFF";
  if (1) {
    var google_conversion_value = 1;
  }
  var google_conversion_label = "purchase";
//-->
</script>
<script language="JavaScript" src="https://www.googleadservices.com/pagead/conversion.js">
</script>
<noscript>
  <img height=1 width=1 border=0 src="https://www.googleadservices.com/pagead/conversion/1068815947/imp.gif?value=1&label=purchase&script=0">
</noscript> 

<SCRIPT language="JavaScript" type="text/javascript">
<!-- Yahoo! Inc.
  window.ysm_customData = new Object();
  window.ysm_customData.conversion = "transId=,currency=,amount=";
  var ysm_accountid  = "1KR4Q5TGJO4AECHT8BN4JFB173O";
  document.write("<SCR" + "IPT language='JavaScript' type='text/javascript' " 
    + "SRC=//" + "srv1.wa.marketingsolutions.yahoo.com" + "/script/ScriptServlet" + "?aid=" + ysm_accountid 
      + "></SCR" + "IPT>");
// -->

</SCRIPT>

</fd:GetOrder>
</tmpl:put>
</tmpl:insert>
