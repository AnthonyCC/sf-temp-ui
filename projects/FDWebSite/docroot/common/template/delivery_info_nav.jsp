<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.webapp.util.*' %>

<% //expanded page dimensions
final int W_DELIVERY_INFO_NAV_TOTAL = 970;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title><tmpl:get name='title'/></title>
    <%@ include file="/common/template/includes/metatags.jspf" %>
	<meta http-equiv="X-UA-Compatible" content="IE=8" />
    <%@ include file="/common/template/includes/i_javascripts.jspf" %>
    <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<%@ include file="/shared/template/includes/ccl.jspf" %>

	<fd:javascript src="/assets/javascript/timeslots.js"/>
	<fd:css href="/assets/css/timeslots.css"/>

<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<BODY bgcolor="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333">
<%@ include file="/shared/template/includes/i_body_start.jspf" %>
<%@ include file="/common/template/includes/globalnav.jspf" %>
<%
	Map<String, String> folderMap=new LinkedHashMap<String, String>();

	/* this nav is generated down (so per column) instead of deptnav, which generates ACROSS */
	
	//first column (always nothing in the last spot)
		folderMap.put("Home Delivery","/help/delivery_info.jsp");
		folderMap.put("Corporate Delivery","/help/delivery_info_cos.jsp");
		/* if(user.isDepotUser()){ folderMap.put("Depot Delivery","/help/delivery_info_depot.jsp"); } */
		folderMap.put("","");
		
	//second column onward (fills based on maxRows)
		folderMap.put("Pickup at FreshDirect","/help/delivery_lic_pickup.jsp");
		if (user.getLevel() >= FDUserI.RECOGNIZED) {
			if (user.getLevel() >= FDUserI.RECOGNIZED) {
				folderMap.put("Available Delivery Slots","/your_account/delivery_info_avail_slots.jsp");
			} else {
				folderMap.put("Available Delivery Slots","/your_account/delivery_info_check_slots.jsp");
			}
		} else {
			folderMap.put("Available Delivery Slots","/help/delivery_info_check_slots.jsp");
		}
		if (user.isEligibleForPreReservation()) {
			folderMap.put("Reserve Delivery (in Your Account)","/your_account/reserve_timeslot.jsp");
		}
		if (FDStoreProperties.isSummerServiceEnabled()) {
			folderMap.put("Summer Delivery","/help/delivery_summer_service.jsp");
		}

		//get ref to dlv pass category
		String dpCatId = "gro_gear_dlvpass";
		CategoryModel dpCatRef = null;
		dpCatRef = (CategoryModel) ContentFactory.getInstance().getContentNode("Category", dpCatId);
		//FDStoreProperties.doDpDeliveryInfoLink()
		
		if (FDStoreProperties.doDpDeliveryInfoLink() && dpCatRef != null) {
			folderMap.put(dpCatRef.getFullName(),"/category.jsp?catId="+dpCatId);
		}
		
	
	int maxRows = 3;
	int maxCols = folderMap.size() / maxRows+(folderMap.size()%maxRows!=0?1:0);
	Iterator<String> itr=folderMap.keySet().iterator();
	
	boolean isCorporate = user.getSelectedServiceType().equals(EnumServiceType.CORPORATE);
%>
<center class="text10">
	<table width="<%=W_DELIVERY_INFO_NAV_TOTAL%>" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td width="<%=W_DELIVERY_INFO_NAV_TOTAL%>" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
		</tr>
		<tr valign="top">
			<td width="219" style="padding: 5px 5px 10px 5px;" class="text9_lh12"><a href="/help/delivery_info.jsp"><img src="/media_stat/images/template/help/delivery/delivery_pickup_deptnav.gif" width="219" height="26" border="0" alt="DELIVERY & PICK UP INFO"></a></td>
			<td width="<%=W_DELIVERY_INFO_NAV_TOTAL-219%>">
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<% for ( int colIndex = 0; colIndex < maxCols; colIndex++ ) { %>
							<td class="onePxTall" rowspan="maxRows"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
							<td width="1" bgcolor="#999966" rowspan="maxRows"><img src="/media_stat/images/layout/clear.gif" width="1" height="12" border="0"></td>
							<td class="onePxTall" rowspan="maxRows"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
							<td class="text9_lh12">
								<%
								for ( int rowIndex = 0; rowIndex < maxRows; rowIndex++ ) { %>
										<%
											if(itr.hasNext()){
											String str=itr.next();
										%>
											<a href="<%= folderMap.get(str)%>"><%= str %></a><br />
										<% } else { %>
											&nbsp;<br />
										<% } %>
								<% } %>
							</td>
						<% } %>
					</tr>
				</table>
				<img src="/media_stat/images/layout/clear.gif" width="1" height="6" alt="" />
			</td>
		</tr>
		<tr bgcolor="#999966"><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>
		<tr valign="top">
			<td align="center" colspan="2"><%-- content lands here --%><tmpl:get name='content'/><%-- content ends above here--%></td>
		</tr>
		<%-- This row is to hold the table --%>
		<tr valign="top">
			<td width="<%=W_DELIVERY_INFO_NAV_TOTAL%>" align="center"  colspan="2"><img src="/media_stat/images/layout/clear.gif" height="1" width="<%=W_DELIVERY_INFO_NAV_TOTAL%>"></td>
		</tr>
		
		<tr valign="bottom">
			<td width="<%=W_DELIVERY_INFO_NAV_TOTAL%>" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
		</tr>
	</table>
</center>
<%@ include file="/common/template/includes/footer.jspf" %>
<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
</body>
</html>
