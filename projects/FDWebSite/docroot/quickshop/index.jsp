<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.fdstore.lists.*'%>
<%@ page import='java.text.*' %>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus id="user" guestAllowed='false' recognizedAllowed='false' redirectPage='/quickshop/index_guest.jsp?successPage=/quickshop/index.jsp' />
<%        
    request.setAttribute("quickshop.level","index");
    request.setAttribute("sitePage", "www.freshdirect.com/quickshop");
    request.setAttribute("listPos", "SystemMessage,QSTopRight,QSBottom,LittleRandy");
%>		

<tmpl:insert template='/common/template/quick_shop.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Quickshop</tmpl:put>
		<tmpl:put name='content' direct='true'>
							
<TABLE WIDTH="535" CELLPADDING="0" CELLSPACING="0" BORDER="0">
<TR>
	<TD WIDTH="535" align="center">
	<img src="/media_stat/images/template/quickshop/quickshop_header.gif" width="535" height="75" alt="" border="0">
	</TD>
</TR>
<TR>
	<TD WIDTH="535"><img src="/media_stat/images/layout/clear.gif" width="1" height="2" alt="" border="0"></TD>
</TR>
<TR>
	<TD WIDTH="535" height="1" bgcolor="#996699"><img src="/media_stat/images/layout/996699.gif" width="535" height="1" alt="" border="0"></TD>
</TR>
<TR>
	<TD WIDTH="530">

		<TABLE WIDTH="530" CELLPADDING="0" CELLSPACING="0" BORDER="0">
		<TR>
			<TD><br>
			<fd:QuickShopController id="quickCart" orderId="" action="">
			<fd:OrderHistoryInfo id='orderHistoryInfo'>
			<% boolean showDetails = true; %>
			<%  //
			    // also need to know how many orders the customer has that are not still pending
			    //
			    int pendingOrderCount = 0;
			    for (Iterator hIter = orderHistoryInfo.iterator(); hIter.hasNext(); ) {
			        FDOrderInfoI orderInfo = (FDOrderInfoI) hIter.next();
			        if (orderInfo.isPending()) {
			            pendingOrderCount++;
			        }
			    }
			    if ((orderHistoryInfo.size() - pendingOrderCount) < 1) { %>
			        <%@ include file="/quickshop/includes/i_index_none.jspf" %>
			<%  } else { %>					
			        <%@ include file="/quickshop/includes/i_index_multiple.jspf" %>								
			<%  } %>		
			</fd:OrderHistoryInfo>
			</fd:QuickShopController>

			</TD>	
		</TR>
		</TABLE>

	</TD>
</TR>

</TABLE>
<br><br>
		</tmpl:put>

</tmpl:insert>