<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.delivery.model.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>

<%@page import="com.freshdirect.fdstore.FDDeliveryManager"%>
<%@page import="com.freshdirect.framework.util.GenericSearchCriteria"%>
<%@page import="com.freshdirect.common.pricing.MunicipalityInfoWrapper"%>
<%@page import="com.freshdirect.common.pricing.MunicipalityInfo"%>
<%@page import="com.freshdirect.webapp.util.CCFormatter"%>


<%@page import="com.freshdirect.delivery.restriction.EnumDlvRestrictionType"%>
<%@page import="com.freshdirect.framework.util.NVL"%>
<%@page import="com.freshdirect.delivery.restriction.EnumDlvRestrictionReason"%>
	<tmpl:insert template='/template/top_nav.jsp'>

	<tmpl:put name='title' direct='true'>Add Alcohol Restriction</tmpl:put>
	
	<tmpl:put name='content' direct='true'>
	
	<jsp:include page="/includes/admintools_nav.jsp" />
<% 
	Calendar cal1 = Calendar.getInstance();
	cal1.set(Calendar.YEAR, 1970);
	cal1.set(Calendar.MONTH, 01);
	cal1.set(Calendar.DATE, 01);
	Calendar cal2 = Calendar.getInstance();
	cal2.set(Calendar.YEAR, 2099);
	cal2.set(Calendar.MONTH, 01);
	cal2.set(Calendar.DATE, 01);

%>
<script type="text/javascript">
		var dowShort = new Array();
		dowShort[0] = 'SUN';
		dowShort[1] = 'MON';
		dowShort[2] = 'TUE';
		dowShort[3] = 'WED';
		dowShort[4] = 'THU';
		dowShort[5] = 'FRI';
		dowShort[6] = 'SAT';
		function toggleDate(resType){
			if(document.getElementById('newStartDate') == null) return;
			if(resType == 'OTR') {
				document.getElementById('newStartDate').value = '';
				document.getElementById('newEndDate').value = '';
				document.getElementById('startDate').value = '';
				document.getElementById('endDate').value = '';
				document.getElementById('imgStartDate').style.display = "";
				document.getElementById('imgEndDate').style.display = "";
			} else {
				document.getElementById('startDate').value = '<%= CCFormatter.formatDateYear(cal1.getTime())%>';
				document.getElementById('endDate').value = '<%= CCFormatter.formatDateYear(cal2.getTime())%>';
				document.getElementById('newStartDate').value = '<%= CCFormatter.formatDateYear(cal1.getTime())%>';
				document.getElementById('newEndDate').value = '<%= CCFormatter.formatDateYear(cal2.getTime())%>';
				document.getElementById('imgStartDate').style.display = "none";
				document.getElementById('imgEndDate').style.display = "none";
			 }	
		 }	
		 function disableCheckBoxes(id) {
			 cbToggle(id);
			 if(id != 'edit_dlvreq_chkANY') return;
			 var cbAny = document.getElementById(id);
			 if(cbAny.checked) {
				 for(var i=0;i<dowShort.length;i++) {
					 var cbName = 'edit_dlvreq_chk'+dowShort[i];
					 var cb = document.getElementById(cbName);
					 cb.disabled = true;
				 }
			 } else {
				 for(var i=0;i<dowShort.length;i++) {
					 var cbName = 'edit_dlvreq_chk'+dowShort[i];
					 var cb = document.getElementById(cbName);
					 cb.disabled = false;
				 }			 
		 	}
		 }
</script>
	
	<div class="promo_page_header" style="height:91%;">
	<crm:AdminToolsController result="result">
		
		<% //quick fix because of broken build : availableDeliveryZones is alreday declared in previous include file %>
		<% //List availableDeliveryZones  = (List)pageContext.getAttribute("availableDeliveryZones"); %>
		<form method='POST' name="frmAddAclRestriction" id="frmAddAclRestriction">
		
<%
           String successMsg=(String)request.getAttribute("successMsg");
            if(successMsg==null) successMsg="";
            
            String startDate = null;
            String endDate = null;
            String message = null;
            String name = null;
    		String state = null;
    		String county = null;
    		EnumDlvRestrictionReason reason = null;
    		EnumDlvRestrictionType restrictedType = null;
            String restrictionId = null;
        	AlcoholRestriction restriction = (AlcoholRestriction)request.getAttribute("restriction");
        	if(restriction != null) {
        		startDate = CCFormatter.formatDateYear(restriction.getDateRange().getStartDate());
        		endDate = CCFormatter.formatDateYear(restriction.getDateRange().getEndDate());
        		message = restriction.getMessage();
        		name = restriction.getName();
    			state = restriction.getState();
    			county =restriction.getCounty();
    			reason = restriction.getReason();
    			restrictedType = restriction.getType();
    			restrictionId = restriction.getId();
        		
        	}
        	startDate = 
        		NVL.apply(startDate, CCFormatter.formatDateYear(cal1.getTime()));
          	endDate = 
        		NVL.apply(endDate, CCFormatter.formatDateYear(cal2.getTime()));
        	message = NVL.apply(message, "");
        	
            name=NVL.apply(name, "");
			state = NVL.apply(state, "NY");
			county = NVL.apply(county, "ALL");
			reason = EnumDlvRestrictionReason.getEnum(NVL.apply(reason != null ? reason.getName() : null, "WIN"));
			restrictedType = EnumDlvRestrictionType.getEnum(NVL.apply(restrictedType != null ? restrictedType.getName() : null, "RRN"));
			restrictionId = NVL.apply(restrictionId, "");

%>
		<input type="hidden" name="actionName" id="actionName" value="">
		<input type="hidden" name="restrictionId" id="restrictionId" value="<%= restrictionId %>">
		<div>
		   	<span><font color="red"> <%=successMsg%> </font> </span>
			<br />
<%--			
			<table width="100%" cellpadding="5" cellspacing="5" border="0" class="case_content_text">
				<tr>
					<td colspan="12" class="promo_page_header_text">Restriction Information</td>
				</tr>
				<tr>
					<td>TYPE</td>
					<td><input type="text" name="restrictedType" value="<%= type.getName()  %>" readonly="readonly"></td>
					<td>REASON</td>
					<td><input type="text" name="reason" value="<%= reason.getName()   %>" readonly="readonly"></td>
					<td>CRITERION</td>
					<td><input type="text" name="criterion" value="DELIVERY" readonly="readonly"></td>					<td>STATE</td>
					<td><input type="text" name="state" value="<%= state %>" readonly="readonly"></td>
					<td>COUNTY</td>
					<td><input type="text" name="county" value="<%= county  %>" readonly="readonly"></td>
					<td>ALCOHOL RESTRICTED</td>
					<td><input type="checkbox" id="restricted" name="restricted" <%= muniInfo.isAlcoholRestricted() ? "checked" : ""%>></td>					
				</tr>			
							
				<tr>
					<td colspan="12" align="center">
						<input type="button" value="UPDATE RESTRICTION FLAG" class="submit" onclick="javascript:doAction('updateAlcoholRestrictionFlag');">
					</td>
				</tr>
			</table>
 --%>			
			<%@ include file="/includes/admintools/i_alcohol_restriction_search.jspf"%>
		</div><br>
		<%-- Promotion edit, edit delivery requirement --%>
		<div>
			<div class="errContainer">
			
			</div>
		<table width="90%" cellpadding="0" cellspacing="5" border="0" class="case_content_text">
				<tr>
					<td class="case_content_field">RESTRICTION ID:</td>
					<td>			            
			            <%=restrictionId%>        
					</td>
				</tr>
		
				<tr>
					<td class="case_content_field">RESTRICTION NAME:</td>
					<td>			            
			            <input type="text" length="50" size="65" name="name" value="<%=name%>">        
						<fd:ErrorHandler result='<%=result%>' name='name' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
					</td>
				</tr>
			    <tr>
					<td class="case_content_field">MESSAGE:</td>
					<td>			
						<textarea id="message" name="message" rows="3" cols="50"><%= message%></textarea>        
			            <fd:ErrorHandler result='<%=result%>' name='message' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>         
					</td>		
				</tr>    
				<tr>
					<td class="case_content_field">START DATE:</td>
					<td>            
						<input type="hidden" name="startDate" id="startDate" value="<%=startDate%>">                        
			            <input type="text" name="newStartDate" id="newStartDate" size="10" value="<%=startDate%>" disabled="true" onchange="setDate1(this);">
			            <% if(EnumDlvRestrictionType.ONE_TIME_RESTRICTION.getName().equals(restrictedType.getName())) {%> 
			            	&nbsp;<a href="#" id="trigger_startDate" ><img id="imgStartDate" src="/media_stat/crm/images/calendar.gif" width="16" height="16" alt="" /></a>
			            <% } else  {%>		
			            	&nbsp;<a href="#" id="trigger_startDate"><img style="display:none;" id="imgStartDate" style="display:none;" src="/media_stat/crm/images/calendar.gif" width="16" height="16" alt="" /></a>
			            <% } %>	            	
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
					<td class="case_content_field">END DATE:</td>
					<td>			
						<input type="hidden" name="endDate" id="endDate" value="<%=endDate%>">                        
			            <input type="text" name="newEndDate" id="newEndDate" size="10" value="<%=endDate%>" disabled="true" onchange="setDate2(this);">
			            <% if(EnumDlvRestrictionType.ONE_TIME_RESTRICTION.getName().equals(restrictedType.getName())) {%> 
			            			&nbsp;<a href="#" id="trigger_endDate"><img id="imgEndDate" src="/media_stat/crm/images/calendar.gif" width="16" height="16" alt="" /></a>
			            <% } else  {%>		
			            	&nbsp;<a href="#" id="trigger_endDate"><img id="imgEndDate" style="display:none;" src="/media_stat/crm/images/calendar.gif" width="16" height="16" alt="" /></a>
			            <% } %>				
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
					<td class="case_content_field">CRITERION:</td>
					<td><input type="text" name="criterion" value="DELIVERY" readonly="readonly"></td>
				</tr>
			</table>	
			
		</div>
		
		<div>
			<div class="errContainer">
				<fd:ErrorHandler result="<%=result%>" name="timeslotFormatError" id="errorMsg">
					<%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="timeslotError" id="errorMsg">
					<%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
			</div>
			<%@ include file="/includes/admintools/i_acl_restriction_day_time.jspf" %>
		</div>
		<table width="100%" cellpadding="0" cellspacing="5" border="0" class="case_content_text">
			<tr>
				<td align="center">
					<input type="button" value="SAVE RESTRICTION" class="submit" onclick="javascript:doAction('saveAlcoholRestriction');">&nbsp;&nbsp;&nbsp;
					<input type="button" value="BACK TO SEARCH" class="submit" onclick="location.href='/admintools/alcohol_restrictions.jsp';">
				</td>
			</tr>
		</table>
		</form>
	</crm:AdminToolsController>
	</div>
	</tmpl:put>
</tmpl:insert>