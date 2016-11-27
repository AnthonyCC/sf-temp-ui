<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.fdstore.promotion.*" %>
<%@ page import="com.freshdirect.fdstore.promotion.management.*" %>
<%@ page import="com.freshdirect.delivery.model.*" %>
<%@ page import="com.freshdirect.webapp.taglib.promotion.WSPromoFilterCriteria" %>
<%@ page import="com.freshdirect.fdstore.promotion.EnumPromotionStatus"%>

<%@page import="com.freshdirect.framework.util.NVL"%><tmpl:insert template='/template/top_nav.jsp'>

	<tmpl:put name='title' direct='true'>View Windows Steering Promotions</tmpl:put>
	
	<tmpl:put name='content' direct='true'>
	<crm:GetCurrentAgent id='currentAgent'>
	<%	
		WSPromoFilterCriteria  wsFilter =  (WSPromoFilterCriteria)request.getSession().getAttribute("wsFilter"); 
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String fromDateStr = null;
		String toDateStr = null;
		String dlvDateStr = null;
		String zone = null;
		String status = "LIVE";
		
		if(null == wsFilter || wsFilter.isEmpty()){			
			Calendar cal = Calendar.getInstance();
			wsFilter = new WSPromoFilterCriteria();
			cal.add(Calendar.DATE, 1);
			wsFilter.setDlvDate(cal.getTime());
			wsFilter.setDlvDateStr(sdf.format(cal.getTime()));
			wsFilter.setStatus(status);
		}
		if(null != request.getParameter("ws_filter_submit")){
			fromDateStr = request.getParameter("wsFromDate");
			toDateStr = request.getParameter("wsToDate");
			dlvDateStr = request.getParameter("wsDlvDate");
			zone = request.getParameter("zone");
			status = request.getParameter("promoStatus");
			
			wsFilter = new WSPromoFilterCriteria();
			wsFilter.setFromDateStr(fromDateStr);
			wsFilter.setToDateStr(toDateStr);
			wsFilter.setStatus(status);
			wsFilter.setDlvDateStr(dlvDateStr);
			wsFilter.setZone(zone);
		} else if(null != wsFilter && !wsFilter.isEmpty()){
			fromDateStr = wsFilter.getFromDateStr();
			toDateStr = wsFilter.getToDateStr();
			status = wsFilter.getStatus();
			dlvDateStr = wsFilter.getDlvDateStr();
			zone = wsFilter.getZone();
		}	
	%>
	
		<%@ include file="/includes/promotions/i_promo_trn_nav.jspf" %>
		<fd:GetWSPromotions id="promotions" filter="<%= wsFilter %>">
		<form method='POST' name="frmWSpromo" id="frmWSPromo"  action="/promotion/promo_ws_view.jsp">
			<div class="BG_live">
		
			<table class="BG_live" width="90%" style="border-bottom:2px solid #000000;border-top:1px solid #000000;">
			
				<% if(pageContext.getAttribute("dateErr")!=null) {%>
		<tr><td><span class="error"><%= pageContext.getAttribute("dateErr")%></span>
				<%	}	%>
			
			<tr>
					<td>Dates From:
					<input type="text" id="wsFromDate" value="<%= fromDateStr %>" name="wsFromDate" size="10" maxlength="10" /> 
					<a href="#" onclick="return false;" class="promo_ico_cont" id="wsFromDate_trigger" name="wsFromDate_trigger">
					<img src="/media_stat/crm/images/calendar.gif" width="16" height="16" alt="" /></a></td>
					<td>To:<input type="text" id="wsToDate" value="<%= toDateStr %>" name="wsToDate" size="10" maxlength="10" /> 
					<a href="#" onclick="return false;" class="promo_ico_cont" id="wsToDate_trigger" name="wsToDate_trigger">
					<img src="/media_stat/crm/images/calendar.gif" width="16" height="16" alt="" /></a></td>
					<td>Delivery Date:
					<input type="text" id="wsDlvDate" value="<%= dlvDateStr %>" name="wsDlvDate" size="10" maxlength="10" /> 
					<a href="#" onclick="return false;" class="promo_ico_cont" id="wsDlvDate_trigger" name="wsDlvDate_trigger">
					<img src="/media_stat/crm/images/calendar.gif" width="16" height="16" alt="" /></a></td>					
					<td>Zone:
					<input type="text" id="zone" value="<%= zone %>" name="zone" size="10" maxlength="10" />
					</td> 
					<td>&nbsp;</td>
					<td>Status:
						<select id="promoStatus" name="promoStatus" class="promo_filter">
							<option value="ALL">ALL</option>
							<option value="CANCELLED" <%= (status != null && status.equals("CANCELLED")) ? "selected":"" %>>Cancelled</option>
							<option value="LIVE" <%= (status != null && status.equals("LIVE")) ? "selected":"" %>>Live</option>
						</select>
					</td>					
					<td>&nbsp;</td>
					<td><input type="submit" value="FILTER" onclick="" id="ws_filter_submit" name="ws_filter_submit" class="promo_btn_grn" /></td>
					<td>
						<div class="promo_sub_nav_export">
							<a href="/promotion/export_ws_promo_list.jsp?actionName=export">Export .xls</a>
						</div>
					</td>
			</tr>	
			</table>
			</div>
		</form>	
		<crm:WSPromoController result="result">
		<form method='POST' name="frmPromoWSView" id="frmPromoWSView">
			<div class="errContainer">
				<fd:ErrorHandler result='<%= result %>' name='actionfailure' id='errorMsg'>
				   <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>		
				<fd:ErrorHandler result='<%= result %>' name='actionsuccess' id='errorMsg'>
				   <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>			
				<%
					String publish = NVL.apply(request.getParameter("publish"), "");
					if(publish != null && publish.length() > 0){
				%>	
				<table border="0" cellspacing="0" cellpadding="0" width="690">
						<tr valign="TOP">
							<td><img src="../images/clear.gif" width="20" height="1" alt="" border="0"></td>
							<td width="18" rowspan="2" bgcolor="#CC3300"><img src="../images/CC3300_tp_lft_crnr.gif" width="18" height="5" border="0"><br></td>
							<td width="100%" bgcolor="#CC3300" colspan="2"><img src="../images/CC3300.gif" width="1" height="1"></td>
							<td width="6" rowspan="2" colspan="2"><img src="../images/CC3300_tp_rt_crnr.gif" width="6" height="5" border="0"></td>
							<td><img src="../images/clear.gif" width="20" height="1" alt="" border="0"></td>
						</tr>
						<tr valign="TOP">
							<td></td>
							<td width="10" rowspan="3"><img src="../images/dot_clear.gif" width="10" height="1" border="0"></td>
							<td width="366"><img src="../images/dot_clear.gif" width="1" height="4" border="0"></td>
							<td></td>					
						</tr>
						<tr valign="TOP">
							<td></td>
							<td width="18" bgcolor="#CC3300"><img src="../images/exclaim_CC3300.gif" width="18" height="22" border="0" alt="!"><br></td>
							<td width="100%" valign="middle">
								<img src="../images/clear.gif" width="1" height="3" alt="" border="0"><br>
									<FONT CLASS="text11rbold">Promotion Successfully Cancelled and Published.<br>			
									</font>
								<img src="../images/clear.gif" width="1" height="3" alt="" border="0"><br>
							</td>
							<td width="5">&nbsp;</td>
							<td width="1" bgcolor="#CC3300"><img src="../images/CC3300.gif" width="1" height="1"></td>
							<td></td>						
						</tr>
						<tr valign="TOP">
							<td></td>
							<td width="18" rowspan="2"><img src="../images/CC3300_bt_lft_crnr.gif" width="18" height="5" border="0"><br></td>
							<td width="100%"><img src="../images/dot_clear.gif" width="1" height="4"></td>
							<td width="6" rowspan="2" colspan="2"><img src="../images/CC3300_bt_rt_crnr.gif" width="6" height="5" border="0"></td>
							<td></td>					
						</tr>
						<tr valign="TOP">
							<td></td>
							<td width="100%" bgcolor="#CC3300" colspan="2"><img src="../images/CC3300.gif" width="1" height="1" border="0"></td>
							<td></td>					
						</tr>
					</table>
				<%					
					}
				%>		
			</div>	
			<input type="hidden" name="promoCode" id="promoCode" value="">
			<input type="hidden" name="actionName" id="actionName" value="">
			<div style="width: 100%;overflow-x: scroll">
			<%@ include file="/includes/promotions/ws_promotions.jspf" %>
			</div>
		</form>
		</crm:WSPromoController>
		</fd:GetWSPromotions>
	</crm:GetCurrentAgent>
	</tmpl:put>
</tmpl:insert>
<script language="javascript">
Calendar.setup(
		{
			showsTime : false,
			electric : false,
			inputField : "wsFromDate",
			ifFormat : "%m/%d/%Y",
			singleClick: true,
			button : "wsFromDate_trigger"
		}
	);
Calendar.setup(
		{
			showsTime : false,
			electric : false,
			inputField : "wsToDate",
			ifFormat : "%m/%d/%Y",
			singleClick: true,
			button : "wsToDate_trigger"
		}
	);
Calendar.setup(
		{
			showsTime : false,
			electric : false,
			inputField : "wsDlvDate",
			ifFormat : "%m/%d/%Y",
			singleClick: true,
			button : "wsDlvDate_trigger"
		}
	);
</script>