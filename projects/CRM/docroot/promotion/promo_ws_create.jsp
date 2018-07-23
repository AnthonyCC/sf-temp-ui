<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.fdstore.promotion.*" %>
<%@ page import="com.freshdirect.fdstore.promotion.management.*" %>
<%@ page import="com.freshdirect.delivery.model.*" %>
<%@ page import="com.freshdirect.smartstore.fdstore.VariantSelection" %>
<%@ page import="com.freshdirect.fdstore.promotion.EnumPromotionProfileAttribute"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="com.freshdirect.webapp.taglib.callcenter.WSPromoControllerTag"%>
<%@ page import="com.freshdirect.delivery.EnumPromoFDXTierType" %>
<%
	//sort them
	List<String> profileAttributeNamesSorted = new ArrayList<String>();
	Collections.sort(profileAttributeNamesSorted);
%>
<tmpl:insert template='/template/top_nav.jsp'>

<%@page import="com.freshdirect.framework.util.NVL"%>
<%@page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@page import="com.freshdirect.delivery.EnumDeliveryOption"%>


<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<fd:javascript src="/assets/javascript/promo.js"/>
<%@page import="java.text.DecimalFormat"%>

<%!
	DateFormat DATE_YEAR_FORMATTER = new SimpleDateFormat("MM/dd/yyyy");
%>
<%  
	Date today = Calendar.getInstance().getTime();
	String f_today = CCFormatter.formatDateMonth(today);

	List<DlvZoneModel> availableDeliveryZones = (List)FDDeliveryManager.getInstance().getAllActiveZones();//getActiveZones();
	String[] discounts = FDStoreProperties.getWSDiscountAmountList().split(",");
	List<String> discountList = new ArrayList<String>();
	for(int i = 0; i < discounts.length ; i++){
		double value = Double.parseDouble(discounts[i]);
		DecimalFormat df = new DecimalFormat("0.00");
		discountList.add(df.format(value));
	}

	String promoId = request.getParameter("promoId");
%>
<SCRIPT TYPE="text/javascript">
<!--
// copyright 1999 Idocs, Inc. http://www.idocs.com
// Distribute this script freely but keep this notice in place
function numbersonly(myfield, e, dec)
{
	var key;
	var keychar;

	if (window.event)
   		key = window.event.keyCode;
	else if (e)
   		key = e.which;
	else
   		return true;
	keychar = String.fromCharCode(key);

	// control keys
	if ((key==null) || (key==0) || (key==8) || 
    	(key==9) || (key==13) || (key==27) )
   	return true;

	// numbers
	else if ((("0123456789").indexOf(keychar) > -1))
   		return true;
	else
   		return false;
}

//-->
</SCRIPT>
<body onload="javascript: toggleFdxSection();">
<fd:GetPromotionNew id="promotion" promotionId="<%=promoId%>">
<%
	String f_effectiveDate = request.getParameter("effectiveDate");	
	String startDate = request.getParameter("startDate"); 		
	String endDate = request.getParameter("endDate");
	String selectedZoneId = request.getParameter("selectedZoneId");
	String startTime = request.getParameter("startTime");
	String endTime = request.getParameter("endTime");
	String discount = request.getParameter("discount");
	String redeemLimit = request.getParameter("redeemlimit");
	String radius = request.getParameter("radius");
	String profileOperator = NVL.apply(request.getParameter("custreq_profileAndOr"),"").trim();
	String fdxTierType = request.getParameter("fdxTierType");
	String rollingType = request.getParameter("rollingType");
	String rolling_days_induction =request.getParameter("rolling_days_induction");
	String rolling_days_firstorder = request.getParameter("rolling_days_firstorder");
	Enumeration paramNames = request.getParameterNames();
	
	List<FDPromotionAttributeParam> attrList = new ArrayList<FDPromotionAttributeParam>();
	attrList = WSPromoControllerTag.getAttributeParamList(request);	

	String deliveryDayType = request.getParameter("deliveryDayType");
	EnumDeliveryOption dlvOption = EnumDeliveryOption.getEnum(deliveryDayType);
	
	List windowTypeList = new ArrayList();
	windowTypeList = WSPromoControllerTag.getWindowTypeParamList(request);
	
	List cohortList = new ArrayList();
	cohortList = request.getParameterValues("cohorts") != null ? Arrays.asList(request.getParameterValues("cohorts")) : new ArrayList() ;		
	if(promotion != null && promotion.getPromotionCode() != null) {
		if(f_effectiveDate == null && promotion.getWSSelectedDlvDate() != null)
			f_effectiveDate =  CCFormatter.defaultFormatDate(promotion.getWSSelectedDlvDate());
		if(startDate == null)
			startDate =  CCFormatter.defaultFormatDate(promotion.getStartDate());
		if(endDate == null)
			endDate =  CCFormatter.defaultFormatDate(promotion.getExpirationDate());
		if(selectedZoneId == null)
			selectedZoneId = promotion.getWSSelectedZone();
		if(startTime == null)
			startTime = promotion.getWSSelectedStartTime();
		if(endTime == null)
			endTime = promotion.getWSSelectedEndTime();
		if(discount == null||"".equals(discount)){
			discount = promotion.getWaiveChargeType();
		}
		if(rollingType ==null){
			if(promotion.isRollingExpDayFrom1stOrder()){
				rolling_days_firstorder = ""+promotion.getRollingExpirationDays();
			}else{
				rolling_days_induction = ""+promotion.getRollingExpirationDays();
			}
		}
		if(discount == null ||"".equals(discount))
			discount = promotion.getMaxAmount();
		if(redeemLimit == null)
			redeemLimit = String.valueOf(promotion.getRedeemCount());
		if(radius == null)
			radius = promotion.getRadius();
		if(deliveryDayType == null){
			dlvOption =promotion.getCustStrategies().get(0).getDeliveryDayType();
			if(null !=dlvOption){
				deliveryDayType = dlvOption.getName(); 
			}
		}
		if(profileOperator == null || "".equals(profileOperator)) {
			profileOperator = promotion.getProfileOperator();
		}
		if(windowTypeList.isEmpty()) {
			List<FDPromoDlvZoneStrategyModel> zsms = promotion.getDlvZoneStrategies();
			if(zsms != null && zsms.size() > 0) {
				FDPromoDlvZoneStrategyModel zsm = (FDPromoDlvZoneStrategyModel) zsms.get(0);
				if(zsm != null && zsm.getDlvTimeSlots() != null) {
					FDPromoDlvTimeSlotModel tm = (FDPromoDlvTimeSlotModel) zsm.getDlvTimeSlots().get(0);
					if(tm.getWindowTypes() != null && tm.getWindowTypes().length != 0){
						windowTypeList = Arrays.asList(tm.getWindowTypes());
					}
					if("0".equalsIgnoreCase(redeemLimit) && zsm.getDlvDayRedemtions() != null && zsm.getDlvDayRedemtions().size() > 0 && zsm.getDlvDayRedemtions().get(0) != null) {
						redeemLimit = String.valueOf(zsm.getDlvDayRedemtions().get(0).getRedeemCount());
					}
				}								
			}
		}
		
		
			List<FDPromoCustStrategyModel> csms = promotion.getCustStrategies();
			if(csms != null && csms.size() > 0) {
				FDPromoCustStrategyModel csm = (FDPromoCustStrategyModel) csms.get(0);
				if(cohortList.isEmpty()) {
					if(csm != null && csm.getCohorts() != null) {
						cohortList = Arrays.asList(csm.getCohorts());
					}							
				}
				if(csm !=null && fdxTierType == null){
					fdxTierType = null !=csm.getFdxTierType()? csm.getFdxTierType().getName():null;
				}
			}
		
		
	}
	Date defaultDate = DateUtil.addDays(today, 1); //Today + 1
	Date defaulEndDate =  DateUtil.addDays(today, 7); //Today + 1
	f_effectiveDate = (f_effectiveDate != null) ? f_effectiveDate : CCFormatter.defaultFormatDate(defaultDate);
	startDate = (startDate != null) ? startDate : CCFormatter.defaultFormatDate(today);
	endDate = (endDate != null) ? endDate : CCFormatter.defaultFormatDate(defaulEndDate);	
	selectedZoneId = (selectedZoneId != null) ? selectedZoneId : "";	
	discount = (discount != null) ? discount : "";
	
	Date effectiveDate = DATE_YEAR_FORMATTER.parse(f_effectiveDate);
	String f_displayDate = CCFormatter.formatDateMonth(effectiveDate);
	boolean isToday = f_today.equals(f_displayDate);
	String radiusChecked = (radius != null && !"".equalsIgnoreCase(radius)) ? "checked" : "";
	attrList = !attrList.isEmpty()?attrList:((promotion.getAttributeList() != null && promotion.getAttributeList().size()>0) ? promotion.getAttributeList() : new ArrayList());
	
%>

<tmpl:put name='title' direct='true'>Create Windows Steering Promotion</tmpl:put>
	
	<tmpl:put name='content' direct='true'>
	<crm:GetCurrentAgent id='currentAgent'>
	<crm:WSPromoController result="result">		

		<%@ include file="/includes/promotions/i_promo_trn_nav.jspf" %>
		<script language="JavaScript" type="text/javascript">
			function doPublish() {
				//document.timePick.selectedZoneId.value = document.timePick.zone.value;
				document.timePick.actionName.value = "publish";
				document.timePick.submit();
			}
		</script>
		
		<form method='POST' name="timePick" id="timePick">
			<div class="errContainer">				
				<fd:ErrorHandler result='<%= result %>' name='effectiveDate' id='errorMsg'>
				   <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result='<%= result %>' name='newBlkEndDate' id='errorMsg'>
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
				<fd:ErrorHandler result='<%= result %>' name='deliveryDayType' id='errorMsg'>
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
				<fd:ErrorHandler result='<%= result %>' name='cohortsEmpty' id='errorMsg'>
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
				<fd:ErrorHandler result='<%= result %>' name='fdxTierTypeEmpty' id='errorMsg'>
				   <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>						
				<fd:ErrorHandler result='<%= result %>' name='minSubTotalEmpty' id='errorMsg'>
				   <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>							
				<fd:ErrorHandler result='<%= result %>' name='redemptionCodeDuplicate' id='errorMsg'>
				   <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="rafPromoCodeDuplicate" id="errorMsg">
				   <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result='<%= result %>' name='windowTypeEmpty' id='errorMsg'>
				   <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="daysEmpty" id="errorMsg">
					<%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="redeemlimit" id="errorMsg">
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
				<tr>
						<td class="promo_page_header_text" colspan="3">Edit&nbsp;Delivery&nbsp;Requirement&nbsp;</td>
				</tr>
				<% if(promotion != null && promotion.getPromotionCode() != null) { %>
				
				<tr>
					<td width="3%">&nbsp;</td>
					<td><span>Promotion ID: <%= promotion.getPromotionCode() %></span></td>
					<td><span>Promotion Name: <%= promotion.getName() %></span></td>
				</tr>
				<% } %>
				<tr>
					<td class="promo_detail_label">Delivery restriction:</td>
					<td class="alignL vTop padL8R16">
						<table width="100%">
							<tr>
								<% 
										 List list = promotion.getDlvZoneStrategies();
										 String selectedDays = "";
										 if(null != list && !list.isEmpty()){
											 FDPromoDlvZoneStrategyModel zoneStrgyModel =(FDPromoDlvZoneStrategyModel)list.get(0);											
											 selectedDays = null != zoneStrgyModel.getDlvDays() ? zoneStrgyModel.getDlvDays() : "";
										 } 
										 Date selectedDlvdate = promotion.getWSSelectedDlvDate();
								%>
								<td class="alignL vTop padL8R16">
								<input type="radio" id="edit_dlvreq_Rest_date" name="edit_dlvreq_Rest" onclick="toggleDeliveryRadio()" value="DELIVERYDATE" <%= ("".equals(selectedDays.trim()) || (selectedDlvdate != null) ) ? "checked":""  %>/>Delivery Date
								<div><img width="1" height="1" src="/media_stat/crm/images/clear.gif" alt="" class="promo_edit_offer-spacer" /></div>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Date:<b> 
								<% if(isToday) { %>
									<%-- today -<%= f_displayDate %> --%> 
								<% } else  {%>
									<%-- <%= f_displayDate %> --%>
								<% } %></b>
								&nbsp;&nbsp;
								<input type="hidden" name="effectiveDate" id="effectiveDate" value="<%=f_effectiveDate%>">
								<input type="hidden" name="startDate" id="startDate" value="<%=startDate%>">
								<input type="hidden" name="endDate" id="endDate" value="<%=endDate%>">
								<input type="hidden" name="selectedZoneId" id="selectedZoneId" value="<%=selectedZoneId%>">
								<input type="hidden" name="promoCode" id="promoCode" value="<%= promoId %>">
								<input type="hidden" name="actionName" id="actionName" value="">
			                    <input type="text" name="newEffectiveDate" id="newEffectiveDate" size="10" value="<%=f_effectiveDate%>" disabled="true" onchange="setDate(this);"> &nbsp;<a href="#" id="trigger_dlvDate" style="font-size: 9px;">Change</a>
			 		        	<script language="javascript">
			
								    function setDate(field){
								    	document.getElementById("effectiveDate").value=field.value;
								    	//document.timePick.selectedZoneId.value = document.timePick.zone.value;
								    }
					
					
								    Calendar.setup(
								    {
									    showsTime : false,
									    electric : false,
									    inputField : "newEffectiveDate",
									    ifFormat : "%m/%d/%Y" ,
									    singleClick: true,
									    button : "trigger_dlvDate" 
								    });
								    
								 
									
									function toggleDeliveryRadio(){							
										if(document.getElementById("edit_dlvreq_Rest_date").checked){
											selectNCB('edit_dlvreq_dayOfWeekParent', '', false, '', true);
											enableOrDisableCheckbok('edit_dlvreq_dayOfWeekParent', true);
										} else {
											enableOrDisableCheckbok('edit_dlvreq_dayOfWeekParent', false);
										}
									}
						    
							</script>										 		
								</td>
							</tr>
							<tr>								
								<td class="alignL vTop padL8R16">
									<input type="radio" id="edit_dlvreq_Rest_day" name="edit_dlvreq_Rest" onclick="toggleDeliveryRadio()" value="DELIVERYDAY" <%= (!"".equals(selectedDays.trim()) && (selectedDlvdate == null) ) ? "checked":""  %> />Delivery Day
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<div><img width="1" height="1" src="/media_stat/crm/images/clear.gif" alt="" class="promo_edit_offer-spacer" /></div>
										<div class="gray" id="timeslotDisp">Eligible Day(s):</div>
										<table class="tableCollapse" width="100%" id="edit_dlvreq_dayOfWeekParent" name="edit_dlvreq_dayOfWeekParent">
											<tr>
												<%-- sets the column widths --%>
												<td width="50%"><img width="1" height="0" src="/media_stat/crm/images/clear.gif" alt="" /></td>
												<td width="50%"><img width="1" height="0" src="/media_stat/crm/images/clear.gif" alt="" /></td>
											</tr>
											<%
												//create week
												List dayOfWeek = new ArrayList();
													dayOfWeek.add("SUNDAY");
													dayOfWeek.add("MONDAY");
													dayOfWeek.add("TUESDAY");
													dayOfWeek.add("WEDNESDAY");
													dayOfWeek.add("THURSDAY");
													dayOfWeek.add("FRIDAY");
													dayOfWeek.add("SATURDAY");

												boolean bgGray = false;

												for (int i = 0; i < dayOfWeek.size(); i++) {
													String dowLong = dayOfWeek.get(i).toString();
													String dowShort = dowLong.substring(0,3);//.toLowerCase();
													boolean even = (i%2 == 0) ? true:false;

													if (i == 0) {
														%><tr><%
													}

													if (i!=0 && even) { %><tr><% }
													%>

														<td class="alignL vTop padL8R16 bord1px999<%=(bgGray)?" BG_exp":""%>" id="edit_dlvreq_chk<%=dowShort%>_parent" name="edit_dlvreq_chk<%=dowShort%>_parent" >
															<input type="checkbox" id="edit_dlvreq_chk<%=dowShort%>" name="edit_dlvreq_chk<%=dowShort%>" onclick="cbToggle(this.id);" onchange="cbToggle(this.id);" <%= (selectedDays.indexOf(""+(i+1)) >-1 && selectedDlvdate == null)  ? "checked": "" %> /> <%=dowLong%>
														</td>

													<%
													if (!even) { %></tr><% }

													if (even) { bgGray = !bgGray; }
												}
											%>
												<td class="alignL vTop padL8R16 gray8pt">Please choose atleast a single day</td>
											</tr>
											<tr>
												<td colspan="2" class="alignR">
													<a href="#" onclick="selectNCB('edit_dlvreq_dayOfWeekParent', '', true, '', true); return false;" class="greenLink">(Select All)</a>&nbsp;
													<a href="#" onclick="selectNCB('edit_dlvreq_dayOfWeekParent', '', false, '', true); return false;" class="greenLink">(Select None)</a>
												</td>
											</tr>
										</table>
								</td>
							</tr>
							</table>
				</tr>
				
				<tr>
					<td width="3%">&nbsp;</td>
					<td>Start Date: &nbsp;&nbsp;           
						<input type="hidden" name="startDate" id="startDate" value="<%=startDate%>">                        
			            <input type="text" name="newStartDate" id="newStartDate" size="10" value="<%=startDate%>" disabled="true" onchange="setDate1(this);">
			            	&nbsp;<a href="#" id="trigger_startDate" ><img id="imgStartDate" src="/media_stat/crm/images/calendar.gif" width="16" height="16" alt="" /></a>
			            	<fd:ErrorHandler result='<%=result%>' name='startDate' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler> 
			 		        <script language="javascript">
						    function setDate1(field){
						    	document.getElementById("startDate").value=field.value;
			
						    }
						    Calendar.setup(
						    {
							    showsTime : false,
							    electric : false,
							    inputField : "newStartDate",
							    ifFormat : "%m/%d/%Y" ,
							    singleClick: true,
							    button : "trigger_startDate" 
						    }
						    );
						    
						    function clearAll(){
						    	 var d = new Date();
							    var date = d.getDate();
							    var month = d.getMonth()+1;
							    var year = d.getFullYear();
						    	var fd = year + "-" + month + "-" + date;                    
						    	document.getElementById("startDate").value = fd;
			                  	document.getElementById("newStartDate").value = fd;
						    	document.getElementById("reason").value = "";
						    	document.getElementById("message").value = "all";
						    	document.getElementById("restrictedType").value = "";
						    }
						    
						    function openURL(inLocationURL) {
						    	popResize(inLocationURL, 400,400,'');
			
						    }
			
						</script>						
					</td>
					
				</tr>
				<tr>
					<td width="3%">&nbsp;</td>
					<td>End Date: &nbsp;&nbsp;			
						<input type="hidden" name="endDate" id="endDate" value="<%=endDate%>">                        
			            <input type="text" name="newEndDate" id="newEndDate" size="10" value="<%=endDate%>" disabled="true" onchange="setDate2(this);">
            			&nbsp;<a href="#" id="trigger_endDate"><img id="imgEndDate" src="/media_stat/crm/images/calendar.gif" width="16" height="16" alt="" /></a>
			            <fd:ErrorHandler result='<%=result%>' name='endDate' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler> 
			 		        <script language="javascript">
						    function setDate2(field){
						    document.getElementById("endDate").value=field.value;
			
						    }
						    Calendar.setup(
						    {
						    showsTime : false,
						    electric : false,
						    inputField : "newEndDate",
						    ifFormat : "%m/%d/%Y" ,
						    singleClick: true,
						    button : "trigger_endDate" 
						    }
						    );			    									                
						    
						    function openURL(inLocationURL) {
			
						    	popResize(inLocationURL, 400,400,'');
			
						    }
			
						</script>
						
					</td>		
				</tr>
				
				<tr>
					<td width="3%">&nbsp;</td>
					<td> Zone: &nbsp;&nbsp;
						<select id="zone" name="zone" class="h10px w200px" onchange="javascript: toggleFdxSection();">
							<option value="">Select Zone</option>
		 					<logic:iterate id="zoneModel" collection="<%= availableDeliveryZones %>" type="com.freshdirect.logistics.delivery.model.DlvZoneModel" indexId="idx">
		 						<% if(zoneModel.getZoneDescriptor().getZoneCode().equals(selectedZoneId)) {%>
								 <option value="<%= zoneModel.getCompanyCode()+"/"+zoneModel.getZoneDescriptor().getZoneCode() %>" selected><%= zoneModel.getZoneDescriptor().getZoneCode() %> <%= zoneModel.getName() %>  </option>
								 <% } else { %>
								 <option value="<%= zoneModel.getCompanyCode()+"/"+zoneModel.getZoneDescriptor().getZoneCode() %>"><%= zoneModel.getZoneDescriptor().getZoneCode() %> <%= zoneModel.getName() %>  </option>
								 <% } %>
							</logic:iterate>
						</select>
					</td>
				</tr>
				<tr><td width="3%">&nbsp;</td>
				<td>
				<div id="fdxSection" style="display:block;">
				<table>
				<tr>
					<td><span>FDX Timeslot Type:&nbsp;&nbsp;<input  type="radio" id="nextHour" name="fdxTierType" value="<%= EnumPromoFDXTierType.NEXT_HOUR.getName() %>" <%= EnumPromoFDXTierType.NEXT_HOUR.getName().equals(fdxTierType)?"checked":"" %> >Next Hour Only</input>
					<input  type="radio" id="postNextHour" name="fdxTierType" value="<%= EnumPromoFDXTierType.POST_NEXT_HOUR.getName() %>" <%= EnumPromoFDXTierType.POST_NEXT_HOUR.getName().equals(fdxTierType)?"checked":"" %>>Post Next Hour Only</input>
					<input type="radio" id="allHours" name="fdxTierType" value="<%= EnumPromoFDXTierType.ALL.getName() %>" <%= EnumPromoFDXTierType.ALL.getName().equals(fdxTierType)?"checked":"" %>>All</input></span></td>
				</tr>
				<tr>
				<td>Rolling Expiration Days: 
				<input type="radio" id="rolling_firstorder" name="rollingType" <%= promotion.isRollingExpDayFrom1stOrder()?"checked":"" %> value="rolling_firstorder" /><input type="text" id="rolling_days_firstorder" name="rolling_days_firstorder" class="w30px alignC" value="<%= rolling_days_firstorder %>" /> <span class="gray">days from 1st order</span>
				<input type="radio" id="rolling_induction" name="rollingType" value="rolling_induction" <%= !promotion.isRollingExpDayFrom1stOrder()?"checked":"" %> /><input type="text" id="rolling_days_induction" name="rolling_days_induction" class="w30px alignC" <%= !promotion.isRollingExpDayFrom1stOrder()?"checked":"" %> value="<%=rolling_days_induction %>" /> <span class="gray">days after eligibility induction</span>
				</td>
				</tr>
				</table>
				</div>
				<script language="javascript">
				function toggleFdxSection(){
					var zone = document.getElementById("zone").value;
					if(null !=zone && ""!=zone && zone.substring(0,3)=="fdx"){
						document.getElementById("fdxSection").style.display="block";
						document.getElementById("WAIVECHARGE").style.display="block";
					}else{
						document.getElementById("fdxSection").style.display="none";
						document.getElementById("WAIVECHARGE").style.display="none";
					}
					var zoneId = zone.split("/")[1];
					document.timePick.selectedZoneId.value=zoneId;
				}
				</script>
				</td></tr>
				<tr>
					<td width="3%">&nbsp;</td>
					<td><span>Radius&nbsp;&nbsp;<input onclick="toggleRadius()" type="checkbox" id="radius" name="radius" value="<%= (radius == null || "".equalsIgnoreCase(radius))  ? "X" : radius %>" <%= radiusChecked %> /></span></td>
					
					<script language="javascript">
					
					function toggleRadius(){							
							if(document.getElementById("radius").checked){
								document.getElementById("startTime").disabled = true;
								document.getElementById("endTime").disabled = true;	
								document.getElementById("startTime").value = '';
								document.getElementById("endTime").value = '';	
								document.getElementById("window_type").disabled=false;
							} else {								
								document.getElementById("startTime").disabled = false;
								document.getElementById("endTime").disabled = false;
								deleteAllWindowType('windowTypeListTB');
								document.getElementById("window_type").disabled=true;
							}
						}
					</script>
					
					<td>
						<table>
							
							<tr>
								<td class="promo_edit_offer_label">Window Type:</td>
								<td class="alignL vTop padL8R16">
									<table class="tableCollapse">							
										<tr>
											<td>									
												<input type="text" id="window_type" name="window_type" class="w200px" onKeyPress="return numbersonly(this, event)" /> in Min(s) &nbsp;							
												<input type="button" value="ADD/UPDATE" onclick="javascript:addWindowType('timePick');" class="promo_btn_gry" />
											</td>
										</tr>
									</table>
								</td>
							</tr>	
				<tr class="flatRow">
						<td>
							<img width="1" height="1" src="/media_stat/crm/images/clear.gif" alt="" class="promo_edit_offer-spacer" />
						</td>
				</tr>
				<tr>
					<td class="promo_edit_offer_label">
						<img width="1" height="0" src="/media_stat/crm/images/clear.gif" alt="" />
					</td>
					<td class="alignL vTop padL8R16" colspan="2">
						
						<table style="border-collapse: collapse;" id="windowTypeListTB">
							<thead>
								<tr>
									<th class="gray8pt padL8R16">&nbsp;</th>
									<th class="bordLgrayDash gray8pt padL8R16">Window Duration (in mins)</th>
									<th class="gray8pt padL8R16"><a href="#" onclick="deleteAllWindowType('windowTypeListTB'); return false;" class="greenLink clickAllExclude">(Delete All)</a></th>
								</tr>
							</thead>
							<tbody>
							<%						
										
										for (int n = 0; n < windowTypeList.size(); n++) {
											String tempAttr = (String) windowTypeList.get(n);
											if (tempAttr != null) {
												%><tr id="windowTypeList[<%=n%>]"><%
												if (n == 0) {
													%><td class="padL8R16">&nbsp;</td><%
												}else{
													%><td class="bordLgrayDash padL8R16"> OR </td><%
												}
												%>																										
													<td class="bordLgrayDash padL8R16 alignC"><%= tempAttr %></td>
													<td class="bordLgrayDash padL8R16"><a href="#" onclick="deleteWindowType('windowTypeList[<%=n%>]'); return false;" class="greenLink">Delete</a></td>
												</tr>
												<%
											}
										}
									%>
							</tbody>
						</table>
						<%-- Add existing windowTypeList in hidden fields --%>
						<%
						
							for (int n = 0; n < windowTypeList.size(); n++) {
								String tempAttr = (String) windowTypeList.get(n);
								%>
									<input type="hidden" name="windowTypeList[<%=n%>].desiredValue" id="windowTypeList[<%=n%>].desiredValue" value="<%= tempAttr %>">
								<%
							} %>
							<script type="text/javascript">
							<!--
								rollingIndex01 = <%=windowTypeList.size()%>;
							//-->
							</script>
					</td>
				</tr>
							
							
						</table>
					</td>
					
				</tr>
					
				<tr>
					<td width="3%">&nbsp;</td>
					<td>
						<span>Start Time <input type="text" id="startTime" name="startTime" value="<%= startTime %>" size="10" maxlength="8" onblur="this.value=time(this.value);"/></span>
					    <span>End Time <input type="text" id="endTime" name="endTime" value="<%= endTime %>" size="10" maxlength="8" onblur="this.value=time(this.value);"/></span>
					</td>
				</tr>	
				<tr>
					<td width="3%">&nbsp;</td>
					<td>
						<span>Redemption Limit: <INPUT type="text" name="redeemlimit" value="<%= redeemLimit %>" size="5" maxlength="5"  onKeyPress="return numbersonly(this, event)"/></span>
					</td> 
				</tr>										
				<tr>
					<td width="3%">&nbsp;</td>
					<td> 
						<select id="discount" name="discount" class="h10px w200px">
							<option value="">Select Discount</option>
		 					<logic:iterate id="discountAmt" collection="<%= discountList %>" type="java.lang.String" indexId="idx">
		 					<% if((discount.equals("") && idx == 0) || discountAmt.equals(discount)) {%>
								 <option value="<%= discountAmt %>" selected>$<%= discountAmt %></option>
							<% } else { %>				
								<option value="<%= discountAmt %>">$<%= discountAmt %></option>
							<% } %>						
							</logic:iterate>
							<% if(null !=selectedZoneId){ %>
							<option id="WAIVECHARGE" value="DLV" style="display: none;" <%="DLV".equals(discount) ?"selected":""%>>FREE DELIVERY</option>
							<% } %>
						</select>
					</td>
				</tr>	
				<tr>
					<td width="3%"></td>
					<td class="alignL vTop padL8R16"><b>Delivery Day Type: </b>
						<table class="tableCollapse" id="edit_dlvreq_addTypeParent" name="edit_dlvreq_addTypeParent" width="450px">
							<tr>
								
								<td><input type="radio" id="regular" value="R" name="deliveryDayType" <%= (dlvOption == null || (null!=dlvOption && (EnumDeliveryOption.REGULAR.equals(dlvOption))))?"checked":"" %>/> <%=EnumDeliveryOption.REGULAR.getDeliveryOption() %></td>
								<td><input type="radio" id="sameday" value="S" name="deliveryDayType" <%= (null!=dlvOption && EnumDeliveryOption.SAMEDAY.equals(dlvOption))?"checked":"" %>/> <%=EnumDeliveryOption.SAMEDAY.getDeliveryOption() %></td>								
								<td><input type="radio" id="so" value="SO" name="deliveryDayType" <%= (( EnumDeliveryOption.SO.equals(dlvOption)))?"checked":"" %>/> <%=EnumDeliveryOption.SO.getDeliveryOption() %></td>
								<td><input type="radio" id="all" value="A" name="deliveryDayType" <%= (( EnumDeliveryOption.ALL.equals(dlvOption)))?"checked":"" %>/> <%=EnumDeliveryOption.ALL.getDeliveryOption() %></td>
								<td>&nbsp;</td>
							</tr>							
						</table>
					</td>
				</tr>
				
				<tr>
						<td class="promo_page_header_text">Edit&nbsp;Customer&nbsp;Requirement&nbsp;</td>
				</tr>
				<tr>
					<td class="promo_detail_label"><div style="text-align: left;" class="fright">Smart Store cohort:<br />(match any)</div></td>
					<td class="alignL vTop padL8R16">
					<%
					
					VariantSelection vs = VariantSelection.getInstance();
					List<String> cohorts = vs.getCohortNames();
					if(null != cohorts && !cohorts.isEmpty()){
					%>
						<table class="tableCollapse" id="edit_custreq_ssParent">
						 <% for(int i=0;i<cohorts.size();i++){
								String checked = "";
								if(cohortList.isEmpty() || cohortList.size() == 0) {
									if(!cohorts.get(i).equals("C5") && !cohorts.get(i).equals("C10") && !cohorts.get(i).equals("C15")) {
										checked = "checked";
									}
									
								} else {
									if(cohortList.contains(cohorts.get(i))) {
										checked = "checked";
									}
								}
						  %>
						  <%if(i%5 ==0){ %>
						     <% if(i!=0){ %></tr>	<%} %>
						  	<tr>
								<td><input type="checkbox" id="cohorts" name="cohorts" value="<%= cohorts.get(i)%>" <%= checked %>/> <%= cohorts.get(i)%></td>
													
							<%} else { %>
								<td><input type="checkbox" id="cohorts" name="cohorts" value="<%= cohorts.get(i)%>" <%= checked %>/> <%= cohorts.get(i)%></td>
							<%} %>
							<%} %>
							<tr>
								<td colspan="4" class="alignR">
									<a href="#" onclick="selectAllCB('edit_custreq_ssParent'); return false;" class="greenLink">(Select All)</a>&nbsp;
									<a href="#" onclick="selectNoneCB('edit_custreq_ssParent'); return false;" class="greenLink">(Select None)</a>
								</td>
							</tr>
							<tr>
								<%-- sets the column widths --%>
								<td class="w75px"><img width="1" height="0" src="/media_stat/crm/images/clear.gif" alt="" /></td>
								<td class="w75px"><img width="1" height="0" src="/media_stat/crm/images/clear.gif" alt="" /></td>
								<td class="w75px"><img width="1" height="0" src="/media_stat/crm/images/clear.gif" alt="" /></td>
								<td class="w75px"><img width="1" height="0" src="/media_stat/crm/images/clear.gif" alt="" /></td>
							</tr>
						</table>
						<%} %>
					</td>
				</tr>
				
				<tr>
					<td class="promo_edit_offer_label">Profile Information:</td>
					<td class="alignL vTop padL8R16">
						<table class="tableCollapse">
							<tr>
								<td>
									<input type="radio" id="profile_condition0" name="custreq_profileAndOr" value="AND" <%="AND".equals(promotion.getProfileOperator())?"checked":"" %>/> <strong>Match All (AND)</strong>
									<input type="radio" id="profile_condition1" name="custreq_profileAndOr" value="OR" <%="OR".equals(promotion.getProfileOperator())?"checked":"" %>/> <strong>Match Any (OR)</strong>
									<input type="hidden" value="AND" class="input" name="profileOperator" id="profileOperator" />
								</td>
							</tr>
							<tr>
								<td><img width="1" height="1" src="/media_stat/crm/images/clear.gif" alt="" class="promo_edit_offer-spacer" /></td>
							</tr>
							<tr>
								<td>
									<select id="profile_value" id="profile_value" class="w200px">
									<option value=""></option>
									<%for(Iterator i = profileAttributeNamesSorted.iterator(); i.hasNext(); ){
										String iName = (String) i.next();
									 %>
										<option value="<%=iName%>"><%=iName%></option>
									<%	} %>
									</select>
									with Value: <input type="text" id="profile_name" name="profile_name" class="w200px" />
								</td>
							</tr>
							<tr>
								<td><img width="1" height="1" src="/media_stat/crm/images/clear.gif" alt="" class="promo_edit_offer-spacer" /></td>
							</tr>
							<tr>
								<td>
									<table id="edit_custreq_profsParent">
										<tr>
											<%-- sets the column widths --%>
											<td class="w150px"><img width="1" height="0" src="/media_stat/crm/images/clear.gif" alt="" /></td>
											<td class="w150px"><img width="1" height="0" src="/media_stat/crm/images/clear.gif" alt="" /></td>
											<td class="w150px"><img width="1" height="0" src="/media_stat/crm/images/clear.gif" alt="" /></td>
										</tr>
										<tr>
											<td><input type="checkbox" id="sc_chefstable" />Chefs Table</td>
											<td><input type="checkbox" id="sc_vip" />VIP/Reserved Dlv</td>
											<td><input type="checkbox" id="sc_inactive" />Inactive</td>
										</tr>
										<tr>
											<td><input type="checkbox" id="sc_gold" />Gold</td>
											<td><input type="checkbox" id="sc_silver" />Silver</td>
											<td><input type="checkbox" id="sc_bronze" />Bronze</td>
										</tr>
										<tr>
											<td><input type="checkbox" id="sc_copper" />Copper</td>
											<td><input type="checkbox" id="sc_tin" />Tin</td>
											<td><input type="checkbox" id="sc_new" />New</td>
										</tr>
										<tr>
											<td colspan="3" class="alignR">
												<a href="#" onclick="selectAllCB('edit_custreq_profsParent'); return false;" class="greenLink">(Select All)</a>&nbsp;
												<a href="#" onclick="selectNoneCB('edit_custreq_profsParent'); return false;" class="greenLink">(Select None)</a>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td><img width="1" height="1" src="/media_stat/crm/images/clear.gif" alt="" class="promo_edit_offer-spacer" /></td>
							</tr>
							<tr>
								<td><input type="button" value="ADD PROFILE/UPDATE OPERATOR" onclick="javascript:addProfile('timePick');" class="promo_btn_gry" /></td>
							</tr>
						</table>
					</td>
					
							<tr class="flatRow">
					<td><img width="1" height="1" src="/media_stat/crm/images/clear.gif" alt="" class="promo_edit_offer-spacer" /></td>
				</tr>
				<tr>
					<td class="promo_edit_offer_label"><img width="1" height="0" src="/media_stat/crm/images/clear.gif" alt="" /></td>
					<td class="alignL vTop padL8R16" colspan="2">
						<%--
							Make sure to look at:
								/includes/promotion_fields.jspf
							
							All the old java logic is there, but the JS has changed
						--%>
						<table style="border-collapse: collapse;" id="profileListTB">
							<thead>
							<tr>
								<th class="gray8pt padL8R16">&nbsp;</th>
								<th class="bordLgrayDash gray8pt padL8R16">Name</th>
								<th class="bordLgrayDash gray8pt padL8R16">Attribute</th>
								<th class="bordLgrayDash gray8pt padL8R16">Value</th>
								<th class="gray8pt padL8R16"><a href="#" onclick="deleteAllProfiles('profileListTB'); return false;" class="greenLink clickAllExclude">(Delete All)</a></th>
							</tr>
							</thead>
							<tbody>
							<%
										for (int n = 0; n < attrList.size(); n++) {
											FDPromotionAttributeParam tempAttr = (FDPromotionAttributeParam)attrList.get(n);
											if (tempAttr != null) {
												%><tr id="attributeList[<%=n%>]"><%
												if (n == 0) {
													%><td class="padL8R16">&nbsp;</td><%
												}else{
													%><td class="bordLgrayDash padL8R16"><%= null != profileOperator ? profileOperator : "" %></td><%
												}
												%>
													<td class="bordLgrayDash padL8R16"><%= ( "".equals(EnumPromotionProfileAttribute.getName(tempAttr.getAttributeName(),tempAttr.getDesiredValue())) ? tempAttr.getAttributeName() : EnumPromotionProfileAttribute.getName(tempAttr.getAttributeName(),tempAttr.getDesiredValue()) )%></td>
													<td class="bordLgrayDash padL8R16"><%= tempAttr.getAttributeName() %></td>
													<td class="bordLgrayDash padL8R16 alignC"><%= tempAttr.getDesiredValue() %></td>
													<td class="bordLgrayDash padL8R16"><a href="#" onclick="deleteProfile('attributeList[<%=n%>]'); return false;" class="greenLink">Delete</a></td>
												</tr>
												<%
											}
										}
									%>
							</tbody>
						</table>
						<%-- Add existing attrs in hidden fields --%>
						<%
						
							for (int n = 0; n < attrList.size(); n++) {
								FDPromotionAttributeParam tempAttr = (FDPromotionAttributeParam)attrList.get(n);
								%>
									<input type="hidden" name="attributeList[<%=n%>].attributeName" id="attributeList[<%=n%>].attributeName" value="<%= tempAttr.getAttributeName() %>">
									<input type="hidden" name="attributeList[<%=n%>].desiredValue" id="attributeList[<%=n%>].desiredValue" value="<%= tempAttr.getDesiredValue() %>">
								<%
							} %>
							<script type="text/javascript">
							<!--
								rollingIndex = <%=attrList.size()%>;
							//-->
							</script>
					</td>
				</tr>
				
				<tr>
					<td width="3%">&nbsp;</td>
					<td width="35%">
						<input type="button" onclick="javascript:doPublish();" name="publish" value=" PUBLISH " class="submit">
					</td>
					<td>
						<input name="cancel" type="button" value=" CANCEL " class="submit" onclick="location.href='/promotion/promo_ws_view.jsp'" >	
					</td>
					
				</tr>
				
															
			</table>			
		</form>
		<script>
			document.observe('dom:loaded', function() {				
				toggleRadius();
				toggleDeliveryRadio();
			});
			


			String.prototype.trim = function() {
				return this.replace(/^\s+|\s+$/g, "");
			}

			function time(time_string) {

				var ampm = 'a';
				var hour = -1;
				var minute = 0;
				var temptime = '';
				time_string = time_string.trim();
				var ampmPatEnd = (/AM|PM$/);
				if (time_string.length == 8 && time_string.match(ampmPatEnd)) {
					return time_string;
				}
				for ( var n = 0; n < time_string.length; n++) {

					var ampmPat = (/a|p|A|P/);
					if (time_string.charAt(n).match(ampmPat)) {
						ampm = time_string.charAt(n);
						break;
					} else {
						ampm = 'a';
					}

					var digPat = (/\d/);
					if (time_string.charAt(n).match(digPat)) {
						temptime += time_string.charAt(n);
					}
				}
				if (temptime.length > 0 && temptime.length <= 2) {
					hour = temptime;
					minute = 0;
				} else if (temptime.length == 3 || temptime.length == 4) {
					if (temptime.length == 3) {
						hour = time_string.charAt(0);
						minute = time_string.charAt(1);
						minute += time_string.charAt(2);
					} else {
						hour = time_string.charAt(0);
						hour += time_string.charAt(1);
						minute = time_string.charAt(2);
						minute += time_string.charAt(3);
					}
				} else {
					return '';
				}

				if ((hour <= 12) && (minute <= 59)) {

					if (hour.toString().length == 1) {
						hour = '0' + hour;
					}

					if (minute.toString().length == 1) {
						minute = '0' + minute;
					}
					if (hour == '00') {
						ampm = 'a'
					}
					//if (hour == '12' ) { ampm='p' }
					temptime = hour + ':' + minute + ' ' + ampm.toUpperCase() + 'M';
				} else {
					temptime = '';
				}

				return temptime

			}

			function hour(time) {

				var result = parseFloat(time.trim());
				if (result == null || isNaN(result))
					return "";
				if (result > 24)
					return "";
				var result1 = "" + result;
				if (result1.indexOf(".") == -1)
					result1 += ".00";
				else {
					var index = result1.indexOf(".");
					var le = result1.trim().length;
					if (le - index > 3)
						result1 = result1.substring(0, index + 3);
				}
				if (time.trim().length == 1)
					result1 = "0" + result1;
				return result1;

			}
			
			function enableOrDisableCheckbok(parentId, truefalse) {
				var curCount = 0;
				var totalCount = 0;
				var parent = document.getElementById(parentId);
				if (parent == null) { return -1; }
				var children = parent.getElementsByTagName('input');
					
					//ALL
					for (var i=0;i<children.length;i++) {
						if (children[i].type == "checkbox" && (children[i].className).indexOf("clickAllExclude") < 0) {
							children[i].disabled = truefalse;
						}
					}			
				
				return totalCount;
			}
		</script>
		</crm:WSPromoController>
	</crm:GetCurrentAgent>
	</tmpl:put>
	</fd:GetPromotionNew>
	</body>
</tmpl:insert>