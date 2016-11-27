<%@tag 	import="com.freshdirect.fdlogistics.model.FDReservation"
		import="com.freshdirect.webapp.util.CCFormatter"
		import="com.freshdirect.fdlogistics.model.FDTimeslot"%><%@ 
	
	attribute name="user" required="true" rtexprvalue="true" type="com.freshdirect.fdstore.customer.FDUserI"
%><% 
	if(user.isEligibleForPreReservation() && user.getReservation() != null) {
		FDReservation rsv = user.getReservation();
%>	<img src="/media_stat/images/layout/cccccc.gif" width="100%" height="1" vspace="8" alt="" />
			   		
	<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td><font class="text9"><b>You have a delivery slot reserved for:</b></font> <a href="/your_account/reserve_timeslot.jsp"><%=CCFormatter.formatReservationDate(rsv.getStartTime())%> @ <%=FDTimeslot.format(rsv.getStartTime(), rsv.getEndTime())%></a></td>
	</tr>
	</table>
<% } %>
