<%@ page import="java.text.*, java.util.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.deliverypass.DlvPassConstants"%>
<%@ page import="com.freshdirect.deliverypass.DeliveryPassInfo"%>
<%@ page import='com.freshdirect.webapp.util.CCFormatter' %>
<%@ page import='com.freshdirect.fdstore.deliverypass.DeliveryPassUtil' %>
<%@ page import="com.freshdirect.deliverypass.EnumDlvPassExtendReason"%>
<%@ page import="com.freshdirect.deliverypass.EnumDlvPassCancelReason"%>

<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri="crm" prefix="crm" %>

<link rel="stylesheet" href="/ccassets/css/crm.css" type="text/css">
<style type="text/css">
body {
	background-color: #FFFFFF;
}
</style>

<script>
function openURL(inLocationURL) {

    self.parent.location.href = inLocationURL;

}
</script>

<table width="100%" cellpadding="3" cellspacing="3" border="0">
<tr>
<% 
	//Retreive the usage info from delivery pass info.
	FDUserI	user = (FDSessionUser) session.getAttribute(SessionName.USER);
	String identity = user.getIdentity().getErpCustomerPK();
	Map passInfo = (Map) session.getAttribute("DLVP" + identity);
	DeliveryPassInfo activeItem = (DeliveryPassInfo) passInfo.get(DlvPassConstants.ACTIVE_ITEM);
	List usageLines = activeItem.getUsageLines();
	if(usageLines == null || usageLines.size() == 0) {
	//No Usage Info	
%>	
	<td valign="top"><span class="info_text"><b><u>Usage Details</u></b></span><br>
		<span class="error">This DeliveryPass currently has no Orders applied.</span>
	</td>
<%
	} else {
	
%>
	<td valign="top"><span class="info_text"><b>Usage Details</b></span><br>
		<table width="175" cellpadding="0" cellspacing="0" border="0">
			<tr class="usage_header">
			<td width="50%" class="border_bold"><span class="detail_text"><b>Date</b></span></td>	
			<td width="50%" class="border_bold"><span class="detail_text"><b>Order #</b></span></td>	
			</tr>
			<logic:iterate id="usage" collection="<%= usageLines %>" type="com.freshdirect.deliverypass.DlvPassUsageLine" indexId="counter">
			<tr>
			<td  class="border_bottom"><span class="detail_text"><%= CCFormatter.defaultFormatDate(usage.getDeliveryDate()) %></span></td>	
			<td  class="border_bottom"><a onclick="javascript:openURL('/main/order_details.jsp?orderId=<%= usage.getOrderIdUsedFor()  %>')" href="#" style="color: #008800;font-size: 8pt;"><%= usage.getOrderIdUsedFor()  %></a></td>
			</tr>
			</logic:iterate>	
		</table>	
	</td>
<%	
      }
    		
%>	
<crm:GetDlvPassActivity deliveryPassId="<%= activeItem.getDlvPassId() %>" id="activities">
<%
	if(activities == null || activities.size() == 0) {
	//No Activities Info	
%>	
	<td valign="top" ><span class="info_text"><b><u>Activity Log</u></b></span><br>
		<span class="error">This DeliveryPass currently has no Activities logged.</span>
	</td>
<%
	} else {
	
%>	
	<td valign="top" ><span class="info_text"><b>Activity Log</b></span><br>
		<table width="900" cellpadding="0" cellspacing="0" border="0">
			<tr class="activity_header">
			<td valign="bottom" width="15%" class="border_bold"><span class="detail_text"><b>Date | Time</b></span></td>	
			<td class="border_bold">&nbsp;</td>
			<td valign="bottom" width="17%" class="border_bold"><span class="detail_text"><b>Action</b></span></td>	
			<td class="border_bold">&nbsp;</td>
			<td valign="bottom" width="13%" class="border_bold"><span class="detail_text"><b>By</b></span></td>	
			<td class="border_bold">&nbsp;</td>
			<td valign="bottom" width="13%"  align="center"  class="border_bold"><span class="detail_text"><b>Order # related</b></span></td>	
			<td class="border_bold">&nbsp;</td>
			<td valign="bottom" width="22%" class="border_bold"><span class="detail_text"><b>Reason</b></span></td>	
			<td class="border_bold">&nbsp;</td>
			<td valign="bottom" width="20%" class="border_bold"><span class="detail_text"><b>Notes</b></span></td>	
			</tr>
			<logic:iterate id="activity" collection="<%= activities %>" type="com.freshdirect.customer.ErpActivityRecord" indexId="counter">
			<tr>
			<td class="border_bottom"><span class="detail_text"><%= CCFormatter.formatDateTime(activity.getDate()) %></span></td>	
			<td class="border_bottom">&nbsp;</td>
			<td class="border_bottom"><span class="detail_text"><%= activity.getActivityType().getName() %></span></td>
			<td class="border_bottom">&nbsp;</td>
			<td class="border_bottom"><span class="detail_text"><%= activity.getInitiator() %></span></td>
			<td class="border_bottom">&nbsp;</td>
			<td align="center"  class="border_bottom"><span class="detail_text"><a onclick="javascript:openURL('/main/order_details.jsp?orderId=<%= activity.getChangeOrderId()  %>')" href="#" style="color: #008800;font-size: 8pt;"><%= activity.getChangeOrderId()  %></a></span></td>
			<td class="border_bottom">&nbsp;</td>
			<td class="border_bottom">
				<span class="detail_text">
				<% 
					String reason = null;
					if(activity.getActivityType() == EnumAccountActivityType.CREDIT_DLV_PASS ||
						activity.getActivityType() == EnumAccountActivityType.EXTEND_DLV_PASS) {
						reason = EnumDlvPassExtendReason.getEnum(activity.getReason()).getDisplayName();
					} else if(activity.getActivityType() == EnumAccountActivityType.CANCEL_DLV_PASS) {
						reason = EnumDlvPassCancelReason.getEnum(activity.getReason()).getDisplayName();
					}
				%>	
				<%= reason %>
				</span>
			</td>
			<td class="border_bottom">&nbsp;</td>
			<td class="border_bottom"><span class="detail_text"><%= activity.getNote() != null ? activity.getNote() : "&nbsp;" %></span></td>
			<td class="border_bottom">&nbsp;</td>
			</tr>
			</logic:iterate>	
		</table>
	</td>	
<%
	}
%>	
</crm:GetDlvPassActivity>
 
</tr>
</table>
