<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.common.customer.EnumServiceType' %>
<%@ page import='com.freshdirect.framework.util.NVL' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/template/top_nav.jsp'>

	<tmpl:put name='title' direct='true'>New Customer > Check Zone</tmpl:put>
	<tmpl:put name='content' direct='true'>

	<%
		String serviceType=NVL.apply(request.getParameter("serviceType"), "");

		String moreInfoPage = "more_info.jsp?successPage=nw_cst_enter_details.jsp";
		String failurePage = "delivery.jsp?successPage=nw_cst_enter_details.jsp&serviceType="+serviceType;
		String successPage = "nw_cst_enter_details.jsp?serviceType="+serviceType;

		//check for new serviceType, and if either GC or RH is enabled
			if ( EnumServiceType.WEB.getName().equalsIgnoreCase(serviceType) ) {
				successPage = "nw_cst_web_enter_details.jsp";
				moreInfoPage = successPage;
				failurePage = successPage;
			}
	%>

	<fd:SiteAccessController action='<%= request.getParameter("action") %>' successPage='<%=successPage%>' moreInfoPage='<%=moreInfoPage%>' failureHomePage='<%=failurePage%>' result='zipResult'>

		<table width="100%" cellpadding="0" cellspacing="2" border="0" align="center" class="sub_nav">
			<tr valign="middle">
				<td width="50%" class="sub_nav_title">&nbsp;Create New Customer: 1. Check Zone</td>
				<td width="50%" align="right">
					<a href="/main/clear_session.jsp" class="cancel">CANCEL</a>
					<%
						String depotCodeError = "";
						String depotAccessCodeError = "";
					%>
					<% if (zipResult.isSuccess()) { %>
						<fd:DepotLoginController actionName='<%= request.getParameter("action") %>' successPage='nw_cst_enter_details.jsp' result='depotResult'>
							<% String[] checkZipForm = {EnumUserInfoName.DLV_ZIPCODE.getCode(), "notInZone", "DeliveryServerDown", "technicalDifficulty"}; %>
							
							<fd:ErrorHandler result='<%= zipResult %>' field='<%=checkZipForm%>' id='errorMsg'>
								<span class="error_detail"><%=errorMsg%></span>
							</fd:ErrorHandler>
							
							<% String[] checkDepotForm = {EnumUserInfoName.DLV_DEPOT_CODE.getCode(), EnumUserInfoName.DLV_DEPOT_REG_CODE.getCode()}; 
								//"wrongAccessCode", "missingDepotCode", 
							%>
						
							<fd:ErrorHandler result='<%= depotResult %>' field='<%=checkDepotForm%>' id='errorMsg'>
								<span class="error_detail"><%=errorMsg%></span>
							</fd:ErrorHandler>
						
							<fd:ErrorHandler result='<%= depotResult %>' name='<%=EnumUserInfoName.DLV_DEPOT_CODE.getCode()%>' id='errorMsg'>
								<%depotCodeError = "<span class=\"error_detail\">"+ errorMsg + "</span>";%>
							</fd:ErrorHandler>
					
							<fd:ErrorHandler result='<%= depotResult %>' name='<%=EnumUserInfoName.DLV_DEPOT_REG_CODE.getCode()%>' id='errorMsg'>
								<%depotAccessCodeError = "<span class=\"error_detail\">"+ errorMsg + "</span>";%>
							</fd:ErrorHandler>
						</fd:DepotLoginController>
					<%	} %>
				</td>
			</tr>
		</table>
		<table width="100%" cellpadding="0" cellspacing="0" border="0" align="center" style="height: 630px; background-color: #fff;">
			<tr valign="top">
				<td colspan="2">
					<%
						// Home delivery form
					%>
					<div class="content" style="height: 200px; padding: 0px; float: left; width: 33%; border-style: solid; border-width: 0px 1px 1px 1px;">
						<div class="register_header" style="width: auto;">&nbsp;&nbsp;For Home Delivery</div>
						<table width="100%" cellpadding="2" cellspacing="0" border="0" class="register">
						<form name="zipCheck" method="post" action="nw_cst_check_zone.jsp?serviceType=<%=EnumServiceType.HOME.getName()%>">
						<input type="hidden" name="action" value="checkByZipCode" />
						<input type="hidden" name="serviceType" value="<%= EnumServiceType.HOME.getName()%>" />
						<tr>
							<td width="30%" align="right">Enter Zip&nbsp;&nbsp;</td>
							<td width="70%">
								<input type="text" maxlength="5" size="10" name="<%=EnumUserInfoName.DLV_ZIPCODE.getCode()%>" value="<%=request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode())%>"> <fd:ErrorHandler result='<%= zipResult %>' name='zipCode' id='errorMsg'><span class="error_detail"><%=errorMsg%></span></fd:ErrorHandler>
							</td>
						</tr>
						<tr>
							<td align="right"><br /><input type="reset" value="CLEAR" class="clear" /></td>
							<td><br /><input type="submit" value="SUBMIT" class="submit" /></td>
						</tr>
						</form>
						</table>
					</div>

					<%
						// Corporate delivery form
					%>
					<div class="content" style="height: 200px; padding: 0px; float: left; width: 33%; border-style: solid; border-width: 0px 1px 1px 0px;">
						<div class="register_header" style="width: auto;">&nbsp;&nbsp;For Corp Delivery</div>
						<table width="100%" cellpadding="2" cellspacing="0" border="0" class="register">
						<form name="zipCheck" method="POST" action="nw_cst_check_zone.jsp?serviceType=<%=EnumServiceType.CORPORATE.getName()%>">
						<input type="hidden" name="action" value="checkByZipCode" />
						<input type="hidden" name="serviceType" value="<%= EnumServiceType.CORPORATE.getName()%>" />
						<tr>
							<td width="30%" align="right">Enter Zip&nbsp;&nbsp;</td>
							<td width="70%">
								<input type="text" maxlength="5" size="10" name="<%=EnumUserInfoName.DLV_ZIPCODE.getCode()%>" value="<%=request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode())%>"> <fd:ErrorHandler result='<%= zipResult %>' name='zipCode' id='errorMsg'><span class="error_detail"><%=errorMsg%></span></fd:ErrorHandler>
							</td>
						</tr>
						<tr>
							<td align="right"><br /><input type="reset" value="CLEAR" class="clear" /></td>
							<td><br /><input type="submit" value="SUBMIT" class="submit" /></td>
						</tr>
						</form>
						</table>
					</div>

					<%
						// Depot delivery form
					%>
					<div class="content" style="height: 200px; padding: 0px; float: left; width: 33%; border-style: solid; border-width: 0px 1px 1px 0px;">
						<div class="register_header" style="width: auto;">&nbsp;&nbsp;For Depot Customers</div>
						<table width="100%" cellpadding="2" cellspacing="0" border="0" class="register">
						<form name="depotCheck" method="POST" action="nw_cst_check_zone.jsp">
						<input type="hidden" name="action" value="checkByDepotCode" />
						<tr>
							<td width="40%" align="right">Select Company&nbsp;&nbsp;</td>
							<td width="60%">
								<select style="width:135px" name="depotCode">
									<option>Company Name</option>
									<fd:GetDepots id="depots">
										<logic:iterate collection="<%= depots %>" id="depot" type="com.freshdirect.delivery.depot.DlvDepotModel">
											<option value="<%= depot.getDepotCode() %>"><%= depot.getName() %></option>
										</logic:iterate>
									</fd:GetDepots>
								</select>
								<%= depotCodeError %>
							</td>
						</tr>
						<tr>
							<td align="right">Enter Access Code&nbsp;&nbsp;</td>
							<td>
								<input class="text11" type="text" size="21" value="<%= request.getParameter("depotAccessCode")!=null?request.getParameter("depotAccessCode"):"" %>"  name="depotAccessCode" required="true" />
								<%= depotAccessCodeError %>
							</td>
						</tr>
						<tr>
							<td align="right"><br /><input type="reset" value="CLEAR" class="clear"></td><td><br /><input type="submit" value="SUBMIT" class="submit"></td>
						</tr>
						</form>
						</table>
					</div>

					<%
						// WEB delivery form
					%>
					<div class="content" style="height: 200px; padding: 0px; float: left; width: 33%; border-style: solid; border-width: 0px 1px 1px 1px;">
						<div class="register_header" style="width: auto;">&nbsp;&nbsp;For WEB Orders</div>
						<table width="100%" cellpadding="2" cellspacing="0" border="0" class="register">
						<form name="zipCheck" method="POST" action="nw_cst_check_zone.jsp?serviceType=<%=EnumServiceType.WEB.getName()%>">
						<input type="hidden" name="action" value="checkByZipCode" />
						<input type="hidden" name="serviceType" value="<%= EnumServiceType.WEB.getName()%>" />
						<tr>
							<td width="30%" align="right">Enter Zip&nbsp;&nbsp;</td>
							<td width="70%">
								<input type="text" maxlength="5" size="10" name="<%=EnumUserInfoName.DLV_ZIPCODE.getCode()%>" value="<%=request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode())%>"> <fd:ErrorHandler result='<%= zipResult %>' name='zipCode' id='errorMsg'><span class="error_detail"><%=errorMsg%></span></fd:ErrorHandler>
							</td>
						</tr>
						<tr>
							<td align="right"><br /><input type="reset" value="CLEAR" class="clear" /></td>
							<td><br /><input type="submit" value="SUBMIT" class="submit" /></td>
						</tr>
						</form>
						</table>
					</div>
				</td>
			</tr>
		</table>




	<script language="JavaScript" type="text/javascript">
		function setfocus() {
			document.zipCheck.zipCode.focus();
		}
	</script>
</fd:SiteAccessController>

</tmpl:put>

</tmpl:insert>
