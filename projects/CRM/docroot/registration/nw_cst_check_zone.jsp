<%@ page import="com.freshdirect.webapp.taglib.fdstore.*"%>
<%@ page import="com.freshdirect.fdstore.customer.*"%>
<%@ page import="com.freshdirect.common.customer.EnumServiceType"%>
<%@ page import="com.freshdirect.framework.util.NVL" %>
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
%>

<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="2" BORDER="0" ALIGN="CENTER" class="sub_nav">
	<TR VALIGN="MIDDLE">
		<TD WIDTH="50%" class="sub_nav_title">&nbsp;Create New Customer: 1. Check Zone</TD>
		<TD WIDTH="50%" ALIGN="RIGHT">
		<a href="/main/clear_session.jsp" class="cancel">CANCEL</a>
            <%
			String depotCodeError = "";
			String depotAccessCodeError = "";
			%>
            <fd:SiteAccessController action='<%= request.getParameter("action") %>' successPage='nw_cst_enter_details.jsp' moreInfoPage='<%=moreInfoPage%>' failureHomePage='<%=failurePage%>' result='zipResult'>
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

        </TD>
	</TR>
</TABLE>

	<script language="JavaScript" type="text/javascript">
		function setfocus() {
			document.zipCheck.zipCode.focus();
		}
	</script>

<div class="content" style="height:30%; padding: 0px; float: left; width: 33%; border-right: 1px solid;">
<div class="register_header">&nbsp;&nbsp;For Home Delivery</div>
<br clear="all">
			<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" class="register">
			<form name="zipCheck" method="POST" action="nw_cst_check_zone.jsp?serviceType=<%=EnumServiceType.HOME.getName()%>">
            <input type="hidden" name="action" value="checkByZipCode">
            <input type="hidden" name="serviceType" value="<%= EnumServiceType.HOME.getName()%>">
				<TR>
					<TD WIDTH="30%" ALIGN="RIGHT">Enter Zip&nbsp;&nbsp;</TD>
					<TD WIDTH="70%"><INPUT TYPE="text" SIZE="10" name="<%=EnumUserInfoName.DLV_ZIPCODE.getCode()%>" value="<%=request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode())%>"> <fd:ErrorHandler result='<%= zipResult %>' name='zipCode' id='errorMsg'><span class="error_detail"><%=errorMsg%></span></fd:ErrorHandler></TD>
				</TR>
				<TR>
					<TD align="right"><BR><input type="reset" value="CLEAR" class="clear"></td><td><br><input type="submit" value="SUBMIT" class="submit"></TD>
				</TR>
			</form>
			</TABLE>
</div>

<div class="content" style="height:30%; padding: 0px; float: left; width: 33%; border-right: 1px solid;">
<div class="register_header">&nbsp;&nbsp;For Corp Delivery</div>
<br clear="all">
			<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" class="register">
			<form name="zipCheck" method="POST" action="nw_cst_check_zone.jsp?serviceType=<%=EnumServiceType.CORPORATE.getName()%>">
            <input type="hidden" name="action" value="checkByZipCode">
            <input type="hidden" name="serviceType" value="<%= EnumServiceType.CORPORATE.getName()%>">

				<TR>
					<TD WIDTH="30%" ALIGN="RIGHT">Enter Zip&nbsp;&nbsp;</TD>
					<TD WIDTH="70%"><INPUT TYPE="text" SIZE="10" name="<%=EnumUserInfoName.DLV_ZIPCODE.getCode()%>" value="<%=request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode())%>"> <fd:ErrorHandler result='<%= zipResult %>' name='zipCode' id='errorMsg'><span class="error_detail"><%=errorMsg%></span></fd:ErrorHandler></TD>
				</TR>
				<TR>
					<TD align="right"><BR><input type="reset" value="CLEAR" class="clear"></td><td><br><input type="submit" value="SUBMIT" class="submit"></TD>
				</TR>
			</form>
			</TABLE>
</div>

<div class="content" style="height:30%; padding: 0px; float: left; width: 33%;">
<div class="register_header">&nbsp;&nbsp;For Depot Customers</div>
<br clear="all">
<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" class="register">
			<form name="depotCheck" method="POST" action="nw_cst_check_zone.jsp">
            <input type="hidden" name="action" value="checkByDepotCode">
				<TR>
					<TD WIDTH="40%" ALIGN="RIGHT">Select Company&nbsp;&nbsp;</TD>
					<TD WIDTH="60%"><SELECT STYLE="width:135px" name="depotCode">
                            <OPTION>Company Name</Option>
                            <fd:GetDepots id="depots">
                            <logic:iterate collection="<%= depots %>" id="depot" type="com.freshdirect.delivery.depot.DlvDepotModel">
                            <option value="<%= depot.getDepotCode() %>"><%= depot.getName() %></option>
                            </logic:iterate>
                            </fd:GetDepots>
                    </SELECT>
					<%= depotCodeError %>
					</TD>
				</TR>
				<TR>
					<TD ALIGN="RIGHT">Enter Access Code&nbsp;&nbsp;</TD>
					<TD>
                        <input class="text11" type="text" size="21" value="<%= request.getParameter("depotAccessCode")!=null?request.getParameter("depotAccessCode"):"" %>"  name="depotAccessCode" required="true">
                    <%= depotAccessCodeError %>
					</TD>
				</TR>
				<TR>
					<TD ALIGN="right"><BR><input type="reset" value="CLEAR" class="clear"></td><td><br><input type="submit" value="SUBMIT" class="submit"></TD>
				</TR>
			</form>
			</TABLE>
</div>
<br clear="all">
</fd:SiteAccessController>

</tmpl:put>

</tmpl:insert>
