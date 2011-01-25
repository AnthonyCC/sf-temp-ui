<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.fdstore.promotion.*" %>
<%@ page import="com.freshdirect.fdstore.promotion.management.*" %>
<%@ page import="com.freshdirect.delivery.model.*" %>

<%@page import="com.freshdirect.framework.util.NVL"%>
<%@page import="com.freshdirect.webapp.util.CCFormatter"%>


<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>

<%@page import="java.text.DecimalFormat"%><tmpl:insert template='/template/top_nav.jsp'>
<%!
	DateFormat DATE_YEAR_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
%>
<%  
	Date today = Calendar.getInstance().getTime();
	String f_today = CCFormatter.formatDateMonth(today);

	List<DlvZoneModel> availableDeliveryZones = (List)FDDeliveryManager.getInstance().getActiveZones();
	String[] discounts = FDStoreProperties.getWSDiscountAmountList().split(",");
	List<String> discountList = new ArrayList<String>();
	for(int i = 0; i < discounts.length ; i++){
		double value = Double.parseDouble(discounts[i]);
		DecimalFormat df = new DecimalFormat("0.00");
		discountList.add(df.format(value));
	}

	String promoId = request.getParameter("promoId");
%>
<fd:GetPromotionNew id="promotion" promotionId="<%=promoId%>">
<%
	String f_effectiveDate = request.getParameter("effectiveDate");
		//NVL.apply(request.getParameter("effectiveDate"), CCFormatter.formatDateYear(today));
	String selectedZoneId = request.getParameter("selectedZoneId");
	String startTime = request.getParameter("startTime");
	String endTime = request.getParameter("endTime");
	String discount = request.getParameter("discount");

	if(promotion != null && promotion.getPromotionCode() != null) {
		if(f_effectiveDate == null)
			f_effectiveDate =  CCFormatter.formatDateYear(promotion.getStartDate());
		if(selectedZoneId == null)
			selectedZoneId = promotion.getWSSelectedZone();
		if(startTime == null)
			startTime = promotion.getWSSelectedStartTime();
		if(endTime == null)
			endTime = promotion.getWSSelectedEndTime();
		if(discount == null)
			discount = promotion.getMaxAmount();
	}
	f_effectiveDate = (f_effectiveDate != null) ? f_effectiveDate : CCFormatter.formatDateYear(today);
	selectedZoneId = (selectedZoneId != null) ? selectedZoneId : "";
	startTime = (startTime != null) ? startTime : "";
	endTime = (endTime != null) ? endTime : "";
	discount = (discount != null) ? discount : "";
	
	Date effectiveDate = DATE_YEAR_FORMATTER.parse(f_effectiveDate);
	String f_displayDate = CCFormatter.formatDateMonth(effectiveDate);
	boolean isToday = f_today.equals(f_displayDate);

%>

<tmpl:put name='title' direct='true'>Create Windows Steering Promotion</tmpl:put>
	
	<tmpl:put name='content' direct='true'>
	
	<crm:WSPromoController result="result">		

		<%@ include file="/includes/promotions/i_promo_trn_nav.jspf" %>
		<script language="JavaScript" type="text/javascript">
			function doPublish() {
				document.timePick.startTime.value = document.timePick.picked0.value;
				document.timePick.endTime.value = document.timePick.picked1.value;
				document.timePick.selectedZoneId.value = document.timePick.zone.value;
				document.timePick.actionName.value = "publish";
				document.timePick.submit();
			}
		</script>
		
		<form method='POST' name="timePick" id="timePick">
			<div class="errContainer">				
				<fd:ErrorHandler result='<%= result %>' name='effectiveDate' id='errorMsg'>
				   <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result='<%= result %>' name='zone' id='errorMsg'>
				   <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result='<%= result %>' name='startTime' id='errorMsg'>
				   <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result='<%= result %>' name='endTime' id='errorMsg'>
				   <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result='<%= result %>' name='discount' id='errorMsg'>
				   <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result='<%= result %>' name='timeslotError' id='errorMsg'>
				   <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>				
				<fd:ErrorHandler result='<%= result %>' name='actionfailure' id='errorMsg'>
				   <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>		
				<fd:ErrorHandler result='<%= result %>' name='actionsuccess' id='errorMsg'>
				   <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>			
				<!--  to be removed -->
				<fd:ErrorHandler result='<%= result %>' name='startDateEmpty' id='errorMsg'>
				   <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>							
				<fd:ErrorHandler result='<%= result %>' name='endDateEmpty' id='errorMsg'>
				   <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>							
				<fd:ErrorHandler result='<%= result %>' name='usageCountEmpty' id='errorMsg'>
				   <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>							
				<fd:ErrorHandler result='<%= result %>' name='offerTypeEmpty' id='errorMsg'>
				   <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>							
				<fd:ErrorHandler result='<%= result %>' name='wsZoneRequired' id='errorMsg'>
				   <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>							
				<fd:ErrorHandler result='<%= result %>' name='combineOfferRequired' id='errorMsg'>
				   <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>							
				<fd:ErrorHandler result='<%= result %>' name='addressTypeEmpty' id='errorMsg'>
				   <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>							
				<fd:ErrorHandler result='<%= result %>' name='minSubTotalEmpty' id='errorMsg'>
				   <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>							
				<fd:ErrorHandler result='<%= result %>' name='redemptionCodeDuplicate' id='errorMsg'>
				   <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>							
				<%
					String publish = NVL.apply(request.getParameter("publish"), "");
					if(publish != null && publish.length() > 0){
						String action = NVL.apply(request.getParameter("action"), "");
						String actionString = action.equals("create") ? "Created" : "Updated";
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
									<FONT CLASS="text11rbold">Promotion Successfully <%= actionString %> and Published.<br>			
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
			<table width="100%" border="0" cellspacing="5" cellpadding="5">
				<tr class="flatRow">
					<%-- sets the column widths --%>
					<td width="130px"><img width="1" height="0" src="/media_stat/crm/images/clear.gif" alt="" /></td>
					<td><img width="1" height="0" src="/media_stat/crm/images/clear.gif" alt="" /></td>
				</tr>
				<tr class="flatRow">
					<td colspan="2"><img width="1" height="1" src="/media_stat/crm/images/clear.gif" alt="" class="promo_edit_offer-spacer" /></td>
				</tr>
				<% if(promotion != null && promotion.getPromotionCode() != null) { %>
				<tr>
					<td width="3%">&nbsp;</td>
					<td><span><b>Promotion ID:</b> <%= promotion.getPromotionCode() %></span></td>
					<td><span><b>Promotion Name:</b> <%= promotion.getName() %></span></td>
				</tr>
				<% } %>
				<tr>
					<td width="3%">&nbsp;</td>
					<td><b>Effective Date: 
					<% if(isToday) { %>
						today -<%= f_displayDate %> 
					<% } else  {%>
						<%= f_displayDate %>
					<% } %>
					</b>&nbsp;&nbsp;
					<input type="hidden" name="effectiveDate" id="effectiveDate" value="<%=f_effectiveDate%>">
					<input type="hidden" name="selectedZoneId" id="selectedZoneId" value="<%=selectedZoneId%>">
					<input type="hidden" name="startTime" id="startTime" value="<%= startTime %>">
					<input type="hidden" name="endTime" id="endTime" value="<%= endTime %>">
					<input type="hidden" name="promoCode" id="promoCode" value="<%= promoId %>">
					<input type="hidden" name="actionName" id="actionName" value="">
                    <input type="text" name="newEffectiveDate" id="newEffectiveDate" size="10" value="<%=f_effectiveDate%>" disabled="true" onchange="setDate(this);"> &nbsp;<a href="#" id="trigger_dlvDate" style="font-size: 9px;">Change</a>
 		        	<script language="javascript">

					    function setDate(field){
					    	document.getElementById("effectiveDate").value=field.value;
					    	document.timePick.selectedZoneId.value = document.timePick.zone.value;
					    	document.timePick.actionName.value = "changeDate";
							document.timePick.submit();
					    }
		
		
					    Calendar.setup(
					    {
					    showsTime : false,
					    electric : false,
					    inputField : "newEffectiveDate",
					    ifFormat : "%Y-%m-%d",
					    singleClick: true,
					    button : "trigger_dlvDate" 
					    }
					    );
			    
				</script>
							 		
					</td>	
				</tr>
				<tr>
					<td width="3%">&nbsp;</td>
					<td> 
						<select id="zone" name="zone" class="h10px w200px">
							<option value="">Select Zone</option>
		 					<logic:iterate id="zoneModel" collection="<%= availableDeliveryZones %>" type="com.freshdirect.delivery.model.DlvZoneModel" indexId="idx">
		 						<% if(zoneModel.getZoneDescriptor().getZoneCode().equals(selectedZoneId)) {%>
								 <option value="<%= zoneModel.getZoneDescriptor().getZoneCode() %>" selected><%= zoneModel.getZoneDescriptor().getZoneCode() %> <%= zoneModel.getName() %>  </option>
								 <% } else { %>
								 <option value="<%= zoneModel.getZoneDescriptor().getZoneCode() %>"><%= zoneModel.getZoneDescriptor().getZoneCode() %> <%= zoneModel.getName() %>  </option>
								 <% } %>
							</logic:iterate>
						</select>
					</td>
				</tr>	
				<tr>
					<td width="3%">&nbsp;</td>
					<td> 
					<!-- 
						<select id="timeslot" name="timeslot" class="h10px w200px">
							<option value="">Select Timeslot</option>
						</select>
					-->
					<script language="JavaScript" type="text/javascript">
							var howMany=2;//(number of times to be picked)
							var pickerName=new Array('Start Time','End Time');//must contain entries as much as is value of howMany
							var hCol='red';//hour hand color
							var mCol='green';//minute hand color
							var bgCol='#B0F0F0';//background color
							var showMin=1;//possible values: 1,5,10,15,20,30
							var show24=0;//set to 1, if 00:00 o'clock should be displayed as 24:00

							if(document.getElementById){
								document.write(writeAll());
								document.close();
							}
							else{
								document.write('Your browser doesn\'t support getElementById. Due to that, the time picker is not displayed.<br>');
								document.close();
							}
							 document.timePick.picked0.value= document.timePick.startTime.value;
							 document.timePick.picked1.value = document.timePick.endTime.value;
										
						</script>
					</td>
				</tr>	
				<tr>
					<td width="3%">&nbsp;</td>
					<td> 
						<select id="discount" name="discount" class="h10px w200px">
							<option value="">Select Discount</option>
		 					<logic:iterate id="discountAmt" collection="<%= discountList %>" type="java.lang.String" indexId="idx">
		 					<% if(discountAmt.equals(discount)) {%>
								 <option value="<%= discountAmt %>" selected>$<%= discountAmt %></option>
							<% } else { %>				
								<option value="<%= discountAmt %>">$<%= discountAmt %></option>
							<% } %>				 
							</logic:iterate>
						</select>
					</td>
				</tr>												
				<tr>
					<td width="3%">&nbsp;</td>
					<td width="35%">
						<input type="button" onclick="javascript:doPublish();" name="publish" value=" PUBLISH " class="submit">
					</td>
					<td>
						<input name="cancel" type="button" value=" CANCEL " class="submit" onclick="location.href('/promotion/promo_ws_view.jsp')" >	
					</td>
					
				</tr>
															
			</table>			
		</form>
		</crm:WSPromoController>
	
	</tmpl:put>
	</fd:GetPromotionNew>
</tmpl:insert>