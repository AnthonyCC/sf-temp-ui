<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.web.model.WebPlanInfo' %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<% boolean hasErrors = session.getAttribute("apperrors") != null; 
 
%>
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
		
</style>
<style>
        .time_picker_div {padding:5px;
            border:solid #999999 1px;
            background:#ffffff;}
      </style>
    

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Add/Edit Scrib</tmpl:put>

	<tmpl:put name='content' direct='true'>
	<br/> 
	<div align="center">
		<form:form commandName = "scribForm" method="post">		
		
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td class="screentitle">Add/Edit Scrib</td>
			</tr>
			<tr>
				<td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
			</tr>      
			<tr>
				<td class="screencontent">
					<table class="forms1" border="0">  
						<tr>
							<td>Date</td>
							<td>
								<form:input maxlength="50" size="24" path="scribDate" />&nbsp;
								<a href="#" id="trigger_scribDate" style="font-size: 9px;">
									<img src="images/calendar.gif" style="border: 0;" alt=">>" />
								</a>
                     
							</td>   
							<td>
								<form:errors path="scribDate" />&nbsp;
							</td>
						</tr>  
						<tr>
							<td>Zone</td>
							<td> 
								
								<form:select path="zoneS" >
									<form:option value="" label="--Please Select Zone"/>
									<form:options items="${zones}" itemLabel="displayName" itemValue="zoneCode" />
								</form:select>
							</td>
							<td><form:errors path="zoneS" />&nbsp;</td>
						</tr>
						<tr>
							<td>Start&nbsp;Time</td>
							<td>         
								<form:input maxlength="50" size="24" path="startTimeS" onblur="this.value=time(this.value);" /> 
							</td>
							<td><form:errors path="startTimeS" />&nbsp;</td>                 
						</tr>
						<tr>
							<td>First Dlv Time</td>
							<td>         
								<form:input maxlength="50" size="24" path="firstDlvTimeS" onblur="this.value=time(this.value);" /> 
							</td>
							<td><form:errors path="firstDlvTimeS" />&nbsp;</td>                 
						</tr>
						<tr>
							<td>Last Dlv Time</td>
							<td>         
								<form:input maxlength="50" size="24" path="endDlvTimeS" onblur="this.value=time(this.value);" /> 
							</td>
							<td><form:errors path="endDlvTimeS" />&nbsp;</td>                 
						</tr>   
						<tr>
							<td>Max Return Time</td>
							<td>         
								<form:input maxlength="50" size="24" path="maxReturnTimeS" onblur="this.value=time(this.value);" /> 
							</td>
							<td><form:errors path="maxReturnTimeS" />&nbsp;</td>                 
						</tr> 
						<tr>
							<td>No of Trucks</td>
							<td>         
								<form:input maxlength="50" size="24" path="count"  /> 
							</td>
							<td><form:errors path="count" />&nbsp;</td>                 
						</tr>  
						<tr>
							<td><input type = "submit" value="&nbsp;Save&nbsp;" />  </td>
							<td>         
								<input type = "button" value="&nbsp;Cancel&nbsp;" onclick="javascript:location.href ='scrib.do'" />
							</td>
							              
						</tr>  
						
						   
						
		</table>
	</form:form>
	</div>
     
	<script language="javascript">             
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
			// document.getElementById('dispatchDay').selectedIndex =  selIndex;
		};
		function zoneChanged() {
			document.getElementById("zoneModified").value = "true";
			submitData();
		} 
		function submitData() {
			document.getElementById("ignoreErrors").value = "true";
			document.getElementById("planForm").submit();
		}
		function bullpen() {
			if(document.getElementById("isBullpen1").checked) {
				document.getElementById("zoneCode").disabled=true;
				document.getElementById("regionCode").disabled=false;
			} else {
				document.getElementById("zoneCode").disabled=false;
				document.getElementById("regionCode").disabled=true;
			}
			document.getElementById("zoneModified").value = "true";
			document.getElementById("ignoreErrors").value = "true";
			document.getElementById("planForm").submit();
		}
	</script>
  </tmpl:put>
</tmpl:insert>
