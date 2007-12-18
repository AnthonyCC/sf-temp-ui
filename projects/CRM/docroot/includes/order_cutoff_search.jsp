<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.DateFormatSymbols" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter" %>
<%@ page import="com.freshdirect.webapp.taglib.callcenter.CutoffTimeSearchControllerTag" %>
<%@ page import="com.freshdirect.fdstore.customer.FDCutoffTimeInfo" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='logic' prefix='logic' %>

<%! DateFormatSymbols symbols = new DateFormatSymbols();    %>
<%
    Calendar today = Calendar.getInstance();
    int month = today.get(Calendar.MONTH);
    int date  = today.get(Calendar.DATE);
    int year  = today.get(Calendar.YEAR);
%>
<script>
<!--
    function changeAction(myForm, target){
        myForm.action=target;
        myForm.submit();
    }
-->
</script>

		<fd:CutoffSearchController actionName="cutoffReport" result="result" cutoffReportId="cutoffReport">
				<table width="100%">
					<tr>
						<td width="30%" align="right">Date of Delivery&nbsp;</td>
						<td width="70%">
							<% month = Integer.parseInt(NVL.apply(request.getParameter("month"), String.valueOf(today.get(Calendar.MONTH))));%>
							<% date = Integer.parseInt(NVL.apply(request.getParameter("day"), String.valueOf(today.get(Calendar.DATE))));%>
							<% year = Integer.parseInt(NVL.apply(request.getParameter("year"), String.valueOf(today.get(Calendar.YEAR))));%>
							<SELECT name="month" required="true" onChange="">
								<option value="">Month</option>
								<%  for (int i=0; i<12; i++) {  %>
								<option value="<%= i %>" <%= (i==month)?"SELECTED":"" %>><%= symbols.getShortMonths()[i] %></option>
								<%  }   %>
							</SELECT>
							<SELECT name="day" required="true">
								<option value="">Date</option>
								<% 	for (int i=1; i<32; i++) { %>
								<option value="<%= i %>" <%= (i==date)?"SELECTED":"" %>><%= i %></option>
								<%	} %>
							</SELECT>
							<SELECT name="year" required="true">
								<option value="">Year</option>
								<% 	for (int i=2001; i<2011; i++) { %>
								<option value="<%= i %>" <%= (i==year)?"SELECTED":"" %>><%= i %></option>
								<%	} %>
							</SELECT>
						</td>
					</tr>
					<tr>
						<td align="right">Cutoff Time&nbsp;</td>
						<td><select name="cutoffTime">
							<option value="">All Cutoff Times</option>
							<logic:iterate collection="<%=cutoffTimes%>" id="cutoffTime" type="java.util.Date">
								<%  
									String cutoffvalue = CCFormatter.formatDeliveryTime(cutoffTime); 
								%>
								<option value="<%=cutoffvalue%>"><%=cutoffvalue%></option>
							</logic:iterate>
							</select>
						</td>
					</tr>
					<tr>
						<td align="right">&nbsp;</td>
						<td><img src="/media_stat/crm/images/clear.gif" width="1" height="8"><br><input type="submit" value="GET SUMMARY OF ORDERS" class="submit"><br><img src="/media_stat/crm/images/clear.gif" width="1" height="8"></td>
					</tr>
				<%if(cutoffReport != null && !cutoffReport.isEmpty()){%>
					<tr>
						<td colspan="2" align="center">
							<table width="80%" cellpadding="0" cellspacing="2">
								<tr>
									<td width="33%"><b>Cutoff Time</b></td>
									<td width="35%"><b>Sale Status</b></td>
									<td width="32%" align="right"><b>Order Count</b></td>
								</tr>
								<logic:iterate collection="<%= cutoffReport%>" id="cReport" type="com.freshdirect.fdstore.customer.FDCutoffTimeInfo">
									<tr>
										<td><%=CCFormatter.formatDeliveryTime(cReport.getCutoffTime())%></td>
										<td><a href='javascript:changeAction(document.forms["searchByCutoff"], "order_quicksearch_results.jsp?status=<%=cReport.getStatus().getStatusCode()%>&cutoffTime=<%=cReport.getCutoffTime()%>&day=<%=request.getParameter("day")%>&month=<%=request.getParameter("month")%>&year=<%=request.getParameter("year")%>");'><%=cReport.getStatus()%></a></td>
										<td align="right"><%=cReport.getOrderCount()%></td>
									</tr>
									<tr class="list_separator" style="padding: 0px;"><td colspan="3"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr>
								</logic:iterate>
							</table>
						</td>
					</tr>
				<%}%>
				</table>
				</fd:CutoffSearchController>
