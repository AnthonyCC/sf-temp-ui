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
<%
	//fetch profiles
	Map profileAttributeNames = FDCustomerManager.loadProfileAttributeNames();
	//sort them
	List<String> profileAttributeNamesSorted = new ArrayList<String>(profileAttributeNames.keySet());
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

<fd:GetPromotionNew id="promotion" promotionId="<%=promoId%>">
<%
	String f_effectiveDate = request.getParameter("effectiveDate");
	//NVL.apply(request.getParameter("effectiveDate"), CCFormatter.formatDateYear(today));
	String startDate = request.getParameter("startDate"); 		
	String endDate = request.getParameter("endDate");
	String selectedZoneId = request.getParameter("selectedZoneId");
	String startTime = request.getParameter("startTime");
	String endTime = request.getParameter("endTime");
	String discount = request.getParameter("discount");
	String redeemLimit = request.getParameter("redeemlimit");
	String radius = request.getParameter("radius");
	

	String deliveryDayType = request.getParameter("deliveryDayType");
	EnumDeliveryOption dlvOption = EnumDeliveryOption.getEnum(deliveryDayType);
	
	if(promotion != null && promotion.getPromotionCode() != null) {
		if(f_effectiveDate == null)
			f_effectiveDate =  CCFormatter.formatDateYear(promotion.getWSSelectedDlvDate());
		if(startDate == null)
			startDate =  CCFormatter.formatDateYear(promotion.getStartDate());
		if(endDate == null)
			endDate =  CCFormatter.formatDateYear(promotion.getExpirationDate());
		if(selectedZoneId == null)
			selectedZoneId = promotion.getWSSelectedZone();
		if(startTime == null)
			startTime = promotion.getWSSelectedStartTime();
		if(endTime == null)
			endTime = promotion.getWSSelectedEndTime();
		if(discount == null)
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
	}
	Date defaultDate = DateUtil.addDays(today, 1); //Today + 1
	f_effectiveDate = (f_effectiveDate != null) ? f_effectiveDate : CCFormatter.formatDateYear(defaultDate);
	startDate = (startDate != null) ? startDate : CCFormatter.formatDateYear(today);
	endDate = (endDate != null) ? endDate : CCFormatter.formatDateYear(defaultDate);	
	selectedZoneId = (selectedZoneId != null) ? selectedZoneId : "";
	startTime = (startTime != null) ? startTime : "";
	endTime = (endTime != null) ? endTime : "";
	discount = (discount != null) ? discount : "";
	
	Date effectiveDate = DATE_YEAR_FORMATTER.parse(f_effectiveDate);
	String f_displayDate = CCFormatter.formatDateMonth(effectiveDate);
	boolean isToday = f_today.equals(f_displayDate);
	String radiusChecked = (radius != null && !"".equalsIgnoreCase(radius)) ? "checked" : "";
	List<FDPromotionAttributeParam> attrList = (promotion.getAttributeList() != null && promotion.getAttributeList().size()>0) ? promotion.getAttributeList() : new ArrayList();

%>

<tmpl:put name='title' direct='true'>Create Windows Steering Promotion</tmpl:put>
	
	<tmpl:put name='content' direct='true'>
	<crm:GetCurrentAgent id='currentAgent'>
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
				<fd:ErrorHandler result='<%= result %>' name='minSubTotalEmpty' id='errorMsg'>
				   <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>							
				<fd:ErrorHandler result='<%= result %>' name='redemptionCodeDuplicate' id='errorMsg'>
				   <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result='<%= result %>' name='windowTypeEmpty' id='errorMsg'>
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
					<td width="35%" align="right" colspan="2">
						<input type="button" onclick="javascript:doPublish();" name="publish" value=" PUBLISH " class="submit">
						<input name="cancel" type="button" value=" CANCEL " class="submit" onclick="location.href('/promotion/promo_ws_view.jsp')" >	
					</td>				
				</tr>
				<tr>
					<td width="3%">&nbsp;</td>
					<td><span>Promotion ID: <%= promotion.getPromotionCode() %></span></td>
					<td><span>Promotion Name: <%= promotion.getName() %></span></td>
				</tr>
				<% } %>
				<tr>
					<td width="3%">&nbsp;</td>
					<td>Delivery Date: 
					<% if(isToday) { %>
						today -<%= f_displayDate %> 
					<% } else  {%>
						<%= f_displayDate %>
					<% } %>
					&nbsp;&nbsp;
					<input type="hidden" name="effectiveDate" id="effectiveDate" value="<%=f_effectiveDate%>">
					<input type="hidden" name="startDate" id="startDate" value="<%=startDate%>">
					<input type="hidden" name="endDate" id="endDate" value="<%=endDate%>">
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
							    ifFormat : "%Y-%m-%d",
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
						    ifFormat : "%Y-%m-%d",
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
					<td><span>Radius&nbsp;&nbsp;<input onclick="toggleRadius()" type="checkbox" id="radius" name="radius" value="<%= (radius == null || "".equalsIgnoreCase(radius))  ? "X" : radius %>" <%= radiusChecked %> disabled="disabled"/></span></td>
					
					<script language="javascript">
					
					function toggleRadius(){							
							if(document.getElementById("radius").checked){
								document.timePick.picked0.value = '';
								document.timePick.picked1.value = '';
								document.timePick.picked0.disabled = true;
								document.timePick.picked1.disabled = true;
								document.timePick.datepicker0.disabled = true;
								document.timePick.datepicker1.disabled = true;	
								document.getElementById("window_type").disabled=false;
							} else {								
								document.timePick.picked0.disabled = false;
								document.timePick.picked1.disabled = false;
								document.timePick.datepicker0.disabled = false;
								document.timePick.datepicker1.disabled = false;	
								document.timePick.picked0.value = '';
								document.timePick.picked1.value = '';
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
												<input type="text" id="window_type" name="window_type" class="w200px" /> in Min(s) &nbsp;							
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
							
										List windowTypeList = new ArrayList();
										if(promotion != null) {
											List<FDPromoDlvZoneStrategyModel> zsms = promotion.getDlvZoneStrategies();
											if(zsms != null && zsms.size() > 0) {
												FDPromoDlvZoneStrategyModel zsm = (FDPromoDlvZoneStrategyModel) zsms.get(0);
												if(zsm != null && zsm.getDlvTimeSlots() != null) {
													FDPromoDlvTimeSlotModel tm = (FDPromoDlvTimeSlotModel) zsm.getDlvTimeSlots().get(0);
													if(tm.getWindowTypes() != null && tm.getWindowTypes().length != 0){
														windowTypeList = Arrays.asList(tm.getWindowTypes());
													}
												}								
											}
										}
							
							
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
								document.write(writeAll(false));
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
						<span>Redemption Limit: <INPUT type="text" name="redeemlimit" value="<%= redeemLimit %>" size="5" maxlength="5"  onKeyPress="return numbersonly(this, event)"/></span>
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
					<td width="3%"></td>
					<td class="alignL vTop padL8R16"><b>Delivery Day Type: </b>
						<table class="tableCollapse" id="edit_dlvreq_addTypeParent" name="edit_dlvreq_addTypeParent" width="250px">
							<tr>
								<td><input type="radio" id="regular" value="R" name="deliveryDayType" <%= (null!=dlvOption && (EnumDeliveryOption.REGULAR.equals(dlvOption)))?"checked":"" %>/> <%=EnumDeliveryOption.REGULAR.getDeliveryOption() %></td>
								<td><input type="radio" id="sameday" value="S" name="deliveryDayType" <%= (null!=dlvOption && EnumDeliveryOption.SAMEDAY.equals(dlvOption))?"checked":"" %>/> <%=EnumDeliveryOption.SAMEDAY.getDeliveryOption() %></td>
								<td><input type="radio" id="all" value="A" name="deliveryDayType" <%= (( EnumDeliveryOption.ALL.equals(dlvOption)))?"checked":"" %>/> <%=EnumDeliveryOption.ALL.getDeliveryOption() %></td>
								<td>&nbsp;</td>
							</tr>							
						</table>
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

				
				<tr>
						<td class="promo_page_header_text">Edit&nbsp;Customer&nbsp;Requirement&nbsp;</td>
				</tr>
				<tr>
					<td class="promo_detail_label"><div style="text-align: left;" class="fright">Smart Store cohort:<br />(match any)</div></td>
					<td class="alignL vTop padL8R16">
					<%
					List cohortList = new ArrayList();
					if(promotion != null) {
						List<FDPromoCustStrategyModel> csms = promotion.getCustStrategies();
						if(csms != null && csms.size() > 0) {
							FDPromoCustStrategyModel csm = (FDPromoCustStrategyModel) csms.get(0);
							if(csm != null && csm.getCohorts() != null) {
								cohortList = Arrays.asList(csm.getCohorts());
							}								
						}
					}
					VariantSelection vs = VariantSelection.getInstance();
					List<String> cohorts = vs.getCohortNames();
					if(null != cohorts && !cohorts.isEmpty()){
					%>
						<table class="tableCollapse" id="edit_custreq_ssParent">
						 <% for(int i=0;i<cohorts.size();i++){
								String checked = "";
								if(cohortList.isEmpty() || cohortList.size() == 0) {
									/*if(!cohorts.get(i).equals("C3") && !cohorts.get(i).equals("C4")) {
										checked = "checked";
									}*/
									checked = "checked";
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
													%><td class="bordLgrayDash padL8R16"><%= null!=promotion.getProfileOperator()?promotion.getProfileOperator():"" %></td><%
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
				</tr>
															
			</table>			
		</form>
		</crm:WSPromoController>
	</crm:GetCurrentAgent>
	</tmpl:put>
	</fd:GetPromotionNew>
</tmpl:insert>