<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.web.model.WebPlanInfo' %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<% boolean hasErrors = session.getAttribute("apperrors") != null; 
 
%>
<script src="js/jsonrpc.js" language="javascript" type="text/javascript"></script>
<script src="js/activeZone.js" language="javascript" type="text/javascript"></script>
<style>
	* {font-family:Arial, Helvetica, sans-serif;
		font-size:9pt;}
		
	/* table list */
	.table_list {border-collapse:collapse;
		border:solid #cccccc 1px;
		width:100%;}
	
	.table_list td {padding:5px;
		border:solid #efefef 1px;}
	
	.table_list th {background:#75b2d1;
		padding:5px;
		color:#ffffff;}
	
	.table_list tr.odd {background:#e1eff5;}
	
	.time_picker_div {padding:5px;
		border:solid #999999 1px;
		background:#ffffff;}
		
	.time_picker_div {
	   padding:5px;
            border:solid #999999 1px;
            background:#ffffff;}
      </style>
    
<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Add/Edit Scrib</tmpl:put>

	<tmpl:put name='content' direct='true'>
	<br/> 
	<div align="center">
		<form:form commandName = "scribForm" method="post">		
		<form:hidden path="dispatchGroupModified"/>
		<form:hidden path="zoneModified"/>		
		<table width="100%" height="90%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td class="screentitle">Add/Edit Scrib</td>
			</tr>
			<tr>
				<td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
			</tr>      
			<tr>
				<td class="screencontent">
					<table class="forms1" style="height:100%;width:98%;align:center;border:1px dotted;background-color:#F7F7F7;">
						<tr>
							<td align="right">Date *</td>
							<td>
								<form:input maxlength="50" size="24" path="scribDate" onchange="javascript:getActiveZoneInfo(this.value,scribForm.zoneS)"/>&nbsp;
								<a href="#" id="trigger_scribDate" style="font-size: 9px;">
									<img src="images/calendar.gif" style="border: 0;" alt=">>" />
								</a>
                     
							</td>   
							<td>
								<form:errors path="scribDate" />&nbsp;
							</td>
						</tr>  
						<tr>
							<td align="right">Origin Facility *</td>
							<td> 
								<form:select path="originFacility" onChange="showZoneSelection(this, scribForm.destinationFacility)">
									<form:option value="" label="--Please Select Origin Facility"/>
									<form:options items="${trnFacilitys}" itemLabel="name" itemValue="facilityId" />
								</form:select> 
							</td>
							<td><form:errors path="originFacility" />&nbsp;</td>
						</tr>
						<tr>
							<td align="right">Destination Facility *</td>
							<td> 
								<form:select path="destinationFacility" onChange="showZoneSelection(scribForm.originFacility,this)">
									<form:option value="" label="--Please Select Destination Facility"/>
									<form:options items="${trnFacilitys}" itemLabel="name" itemValue="facilityId" />
								</form:select> 
							</td>
							<td><form:errors path="destinationFacility" />&nbsp;</td>
						</tr>
						<tr id="zoneRow">
							<td align="right">Zone</td>
							<td>
								<c:if test="${scribForm.destinationFacility.trnFacilityType.name ne 'SIT' and scribForm.destinationFacility.trnFacilityType.name ne 'DPT'}">
									<c:set var="_disableZone" value="true"/>
								</c:if>
								<form:select path="zoneS" disabled="${_disableZone}" onChange="zoneChanged()">
									<form:option value="null" label="--Please Select Zone"/>
									<form:options items="${zones}" itemLabel="displayName" itemValue="zoneCode" />
								</form:select>
							</td>
							<td><form:errors path="zoneS" />&nbsp;</td>
						</tr>
						<tr id="regionRow">
							<td align="right">Region</td>
								<td>
									<c:if test="${scribForm.destinationFacility.trnFacilityType.name eq 'SIT' or scribForm.destinationFacility.trnFacilityType.name eq 'DPT'}">
										<c:set var="_disableRegion" value="true"/>
									</c:if>
									<form:select path="region" disabled="${_disableRegion}">
										<form:option value="null" label="--Please Select Region"/>
										<form:options items="${regions}" itemLabel="code" itemValue="code" />
									</form:select>
								</td>
							<td><form:errors path="region" />&nbsp;</td>
						</tr>
						<tr>
							<td align="right">Dispatch Group Time</td>
							<td> 
								<form:select path="dispatchGroupS">
									<form:option value="" label="--Please Select Dispatch Group"/>
									<form:options items="${dispatchGroups}" itemLabel="name" itemValue="groupTime" />
								</form:select> 
							</td>
							<td><form:errors path="dispatchGroupS" />&nbsp;</td>
						</tr>  
						<tr>
							<td align="right">Truck&nbsp;Dispatch&nbsp;Time</td>
							<td>         
								<form:input maxlength="50" size="24" path="startTimeS" onblur="this.value=time(this.value);" /> 
							</td>
							<td><form:errors path="startTimeS" />&nbsp;</td>                 
						</tr>
						<tr>
							<td align="right">Truck&nbsp;End&nbsp;Time</td>
							<td>         
								<form:input maxlength="50" size="24" path="endTimeS" onblur="this.value=time(this.value);" /> 
							</td>
							<td><form:errors path="endTimeS" />&nbsp;</td>
						</tr
						<tr>
							<td align="right">Max Return Time</td>
							<td>         
								<form:input maxlength="50" size="24" path="maxReturnTimeS" onblur="this.value=time(this.value);" /> 
							</td>
							<td><form:errors path="maxReturnTimeS" />&nbsp;</td>                 
						</tr>
						<tr>
							<td align="right">No of Trucks/CD Trailers </td>
							<td>         
								<form:input maxlength="50" size="24" path="truckCnt"  /> 
							</td>
							<td><form:errors path="truckCnt" />&nbsp;</td>
						</tr>  
						<tr>
							<td align="right">No of HandTrucks</td>
							<td>         
								<form:input maxlength="50" size="24" path="handTruckCnt"  /> 
							</td>
							<td><form:errors path="handTruckCnt" />&nbsp;</td>
						</tr>
						<tr> 
						<tr>
							<td align="right">Equipment Type</td>
							<td>
								<form:select path="equipmentTypeS" disabled="${_disableZone}">
									<form:option value="" label="--Please Select Equipment Type"/>
									<form:options items="${scribForm.equipmentTypes}" itemLabel="id" itemValue="id" />
								</form:select>
							</td>
							<td><form:errors path="equipmentTypeS" />&nbsp;</td>
						</tr>
						
						<td align="right">Supervisor</td>
							<td> 
								<form:select path="supervisorCode">
									<form:option value="" label="--Please Select Supervisor"/>
									<form:options items="${supervisors}" itemLabel="name" itemValue="employeeId" />
								</form:select>
							</td>
							<td><form:errors path="supervisorCode" />&nbsp;</td>   
						</tr>
						<tr>
							<td align="right">Handoff&nbsp;Time</td>
							<td> 
								<form:select path="cutOffTimeS">
									<form:option value="" label="--Please Select Handoff"/>
									<form:options items="${cutoffs}" itemLabel="name" itemValue="cutOffTimeEx" />
								</form:select>
							</td>
							<td><form:errors path="cutOffTimeS" />&nbsp;</td>
						</tr>						
						<tr>
							<td colspan="3" align="center">
								<input type = "button" value="&nbsp;Back&nbsp;" onclick="javascript:back();" /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type = "button" onclick="validateForm();" value="&nbsp;Save&nbsp;" />&nbsp;&nbsp;&nbsp;&nbsp;
								<input type = "button" value="&nbsp;Cancel&nbsp;" onclick="javascript:location.href ='scrib.do'" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							</td>
						</tr>
					</table>
							   </td>          
						</tr>  
			<tr><td>&nbsp;</td></tr>
		</table>
	</form:form>
	</div>
     
	<script language="javascript">             

		var jsonrpcClient = new JSONRpcClient("dispatchprovider.ax");

		Calendar.setup(
			{
				showsTime : false,
				electric : false,
				inputField : "scribDate",
				ifFormat : "%m/%d/%Y",
				singleClick: true,
				button : "trigger_scribDate",
				onUpdate : updateDate
			}
		);

		function updateDate(cal) {
			var selIndex = cal.date.getDay();
			if(selIndex == 0) selIndex = 7;
		};
		
		function validateForm(){
			var _starttime = document.getElementById('startTimeS').value;
	     	var _endtime = document.getElementById('endTimeS').value;
	     			
			if(_starttime.trim().length > 0 && _endtime.trim().length > 0 && checkTime(_endtime, _starttime))
					if(!confirm("Truck end time is before truck dispatch time. The end time will be considered past midnight.")){
						return;
					}
			submitData();
		}
			
		function showZoneSelection(originRefVar, destRefVar) {

			var originRef = originRefVar.value || '';
			var destRef = destRefVar.value || '';
			
			var result = jsonrpcClient.AsyncDispatchProvider.getFacilityInfo(sendFormCallback, originRef, destRef); 
			
			function sendFormCallback(result, exception) {
			  if(exception) {
				  alert('Unable to connect to host system. Please contact system administrator!');               
				  return;
			  }

			  if( result[0] === 'SIT'){
				  alert('Origin facility cannot be delivery zone.');
				  originRefVar.selectedIndex = 0;
			  } else if( result[1] === 'PLANT'){
				  alert('Destination facility cannot be main plant.');
				  destRefVar.selectedIndex = 0;
			  } else if((result[1] === result[0]) && (originRef != '' && destRef != '')){
				  alert('Both origin & desination facility cannot be same.');
				  originRefVar.selectedIndex = 0;
				  destRefVar.selectedIndex = 0;
			  } else {
					if( result[1] === 'SIT'){
						document.getElementById("zoneS").disabled=false;
						document.getElementById('region').disabled= true;
					}else if(result[1] === 'DPT'){
						document.getElementById("zoneS").disabled=false;
						document.getElementById('region').disabled= false;
					}
					else{
						document.getElementById("zoneS").disabled=true;
						document.getElementById("zoneS").selectedIndex=0;
						document.getElementById('region').disabled= false;
					}
			  }
			}
		}

		function zoneChanged() {
			document.getElementById("zoneModified").value = "true";
			submitData();
		} 
		function submitData() {
			document.getElementById("scribForm").submit();
		}

		function  dispatchGroupTimeChanged() {
			document.getElementById("dispatchGroupModified").value = "true";
			submitData();			
		}
		function back()
	    {
	      	var filters=unescape(getParameter("filter"));	      	     	
	      	var params=filters.split("&");
	      	var planForm=document.forms["scrib"];
	      	for(var i=0;i<params.length;i++)
	      	{
	      		var param=params[i].split("=");         				
	      		add_input(planForm,"hidden",param[0],param[1]);
	      	}     	      	
	      	planForm.submit();
	    }
       //update the active zone for the particular day
     /*   function getActiveZoneInfo()
        {
         if(scribForm.scribDate.value!='')
         {
        	var jsonrpcClient = new JSONRpcClient("dispatchprovider.ax");
        	var scribDate=scribForm.scribDate.value;        	
        	jsonrpcClient.AsyncDispatchProvider.getActiveZones(getActiveZoneInfoCallback,scribDate);
         }
        }
        function getActiveZoneInfoCallback(result, exception) 
        {
      	  
          if(exception) 
          {               
              alert('Unable to connect to host system. Please contact system administrator!');               
              return;
          }
         
		  var selectedZone=scribForm.zoneS.value;	  
		  for(var i=scribForm.zoneS.options.length-1;i>=1;i--)
		  {
			  scribForm.zoneS.remove(i);
		  }		 
		  var selected=false;
		  var results=result.list;
		  for(var i=0;i<results.length;i++)
		  {			
			  	var optn = document.createElement("OPTION");
			  	optn.text = results[i].name;
	          	optn.value = results[i].zoneCode;
	          	if(optn.value==selectedZone)
	          	{
	          		optn.selected=true;	          		
	          	}	          	
	          	scribForm.zoneS.options.add(optn);	        
          }                               
      }   */
	    
	</script>
  </tmpl:put>
</tmpl:insert>
<form name="scrib" action="scrib.do" method="post">  </form>