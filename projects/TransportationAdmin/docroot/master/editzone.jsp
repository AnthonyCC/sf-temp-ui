<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<%   
  pageContext.setAttribute("HAS_DELETEBUTTON", "false");
  pageContext.setAttribute("HAS_ADDBUTTON", "false"); 
%>

<% 
	String pageTitle = "Add/Edit Zone";
%>
<script type="text/javascript"> 
	function enableDisable() { 
	var myForm="zoneForm";
  if(document.getElementById(myForm).smsEta.checked){ 
	 document.getElementById(myForm).dlvWindow.checked=false;
     document.getElementById(myForm).dlvWindow.disabled = true; 
  } else if(document.getElementById(myForm).dlvWindow.checked){ 
	 document.getElementById(myForm).smsEta.checked=false;
     document.getElementById(myForm).smsEta.disabled = true; 
  } else{
  document.getElementById(myForm).smsEta.disabled=false;
  document.getElementById(myForm).dlvWindow.disabled=false;
  }
} 
 window.onload=enableDisable;
	</script>
<tmpl:insert template='/common/sitelayout.jsp'>

<tmpl:put name='yui-lib'>
	<%@ include file='/common/i_yui.jspf'%>	
</tmpl:put>	

<tmpl:put name='title' direct='true'>Operations : Zones : Active : <%=pageTitle%></tmpl:put>
  <tmpl:put name='content' direct='true'>

<div class="MNM004 subsub or_3c3">
		<div class="subs_left">	
			<div class="sub_tableft sub_tabL_MNM004 activeL">&nbsp;</div>
			<div class="subtab activeT">
				<div class="minwidth"><!-- --></div>
				<a href="zone.do?zoneType=Active" class="MNM004">Active</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM004 activeR">&nbsp;</div>

			<div class="sub_tableft sub_tabL_MNM004">&nbsp;</div>
			<div class="subtab">
				<div class="minwidth"><!-- --></div>
				<a href="zone.do" class="">All</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM004">&nbsp;</div>		
		
		</div>
	</div>

    
	<div class="contentroot">		
		<div class="cont_row">

		<br/>	
		<div align="center">
      <form:form id="zoneForm" commandName = "zoneForm" method="post">
      <form:hidden path="zoneCode"/>
      
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle">Add/Edit Zone</td>
          </tr>
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
          </tr>
          
          <tr>
            <td class="screencontent">
              <table class="forms1">          
                <tr>
                  <td>Name</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="name" readOnly="true" />
                </td>
                <td>
                  &nbsp;<form:errors path="name" />
                </td>
               </tr>
                             
               
               <tr>
                  <td>Zone Type</td>
                  <td> 
                  <form:select path="trnZoneType">
                        <form:option value="null" label="--Please Select Zone Type"/>
                    <form:options items="${zonetypes}" itemLabel="name" itemValue="zoneTypeId" />
                   </form:select>
                </td>
                <td>
                  &nbsp;<form:errors path="trnZoneType" />
                </td>
               </tr>
               
               <tr>
                  <td>Area</td>
                  <td> 
                  <form:select path="area">
                        <form:option value="null" label="--Please Select Area"/>
                    <form:options items="${areas}" itemLabel="name" itemValue="code" />
                   </form:select>
                </td>
                <td>
                  &nbsp;<form:errors path="area" />
                </td>                                
               </tr>  

               <tr>
                  <td>Region</td>
                  <td> 
                  <form:select path="region">
                        <form:option value="null" label="--Please Select Region"/>
                    <form:options items="${regions}" itemLabel="name" itemValue="code" />
                   </form:select>
                </td>
                <td>
                  &nbsp;<form:errors path="region" />
                </td>
               </tr>
               
               <tr>
                  <td>Service Time Type</td>
                  <td> 
                  	<form:select path="serviceTimeType">
                        <form:option value="null" label="--Please Select Service Time Type"/>
                    <form:options items="${servicetimetypes}" itemLabel="name" itemValue="code" />
                   </form:select>
                </td>
                <td>
                  &nbsp;<form:errors path="serviceTimeType" />
                </td>
               </tr>

               <tr>
                  <td>Unattended Delivery</td>
                  <td>
                  <form:checkbox path="unattended" value="X"/>
                  </td>
                <td>
                  &nbsp;<form:errors path="unattended" />
                </td>
               </tr>
               
               <tr>
                  <td>COS Enabled</td>
                  <td>
                  <form:checkbox path="cosEnabled" value="X"/>
                  </td>
                <td>
                  &nbsp;<form:errors path="cosEnabled" />
                </td>
               </tr>
               
               <tr>
                  <td>Priority</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="priority"/>
                </td>
                <td>
                  &nbsp;<form:errors path="priority" />
                </td>
               </tr>
               <tr>
                  <td>Pre-Trip Time (Mins)</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="preTripTime"/>
                </td>
                <td>
                  &nbsp;<form:errors path="preTripTime" />
                </td>
               </tr>
               <tr>
                  <td>Post-Trip Time (Mins)</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="postTripTime"/>
                </td>
                <td>
                  &nbsp;<form:errors path="postTripTime" />
                </td>
               </tr>
               <tr>
                  <td>Loading Priority</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="loadingPriority"/>
                </td>
                <td>
                  &nbsp;<form:errors path="priority" />
                </td>
               </tr>
               <tr>
                  <td>Eco-Friendly Radius</td>
                  <td>
                    <form:input maxlength="50" size="30" path="ecoFriendly"/>
                  </td>
                <td>
                  &nbsp;<form:errors path="ecoFriendly" />
                </td>
               </tr>
                <tr>
                  <td>Steering Radius</td>
                  <td>
                    <form:input maxlength="50" size="30" path="steeringRadius"/>
                  </td>
                <td>
                  &nbsp;<form:errors path="steeringRadius" />
                </td>
               </tr>
               <tr>
                  <td>Service Adjustment Reduction Factor</td>
                  <td>
                    <form:input maxlength="50" size="30" path="svcAdjReductionFactor"/>
                  </td>
                <td>
                  &nbsp;<form:errors path="svcAdjReductionFactor" />
                </td>
               </tr>               
              <tr>
                  <td>ETA Window Enabled</td>
                  <td>
	                  <form:checkbox path="manifestETAEnabled" value="X"/>&nbsp;Manifest
	                  <form:checkbox path="emailETAEnabled" value="X"/>&nbsp;Email
	               </td>
                <td>
                  &nbsp;<form:errors path="manifestETAEnabled" />&nbsp;<form:errors path="emailETAEnabled" />&nbsp;
                </td>
               </tr>
               <tr>
               	<td>SMS Alerts Enabled</td>
               	<td>
               		<form:checkbox path="nextStopSmsEnabled" value="X" />&nbsp;NextStop SMS Alerts
               		<form:checkbox id="smsEta" path="smsETAEnabled" value="X" onchange="enableDisable();" />&nbsp;SMS ETA Window
               		<form:checkbox path="unattendedSmsEnabled" value="X" />&nbsp;Unattended/Doorman Delivery SMS Alerts
               		<form:checkbox path="dlvAttemptedSmsEnabled" value="X" />&nbsp;Delivery Attempted SMS Alerts
               		 <form:checkbox id="dlvWindow" path="dlvWindowReminder" value="X"  onchange="enableDisable();" />&nbsp;Delivery Window Reminder
               	</td>
               	<td>
               		&nbsp;<form:errors path="nextStopSmsEnabled" />&nbsp;&nbsp;<form:errors path="smsETAEnabled" />&nbsp;&nbsp;<form:errors path="unattendedSmsEnabled" />&nbsp;&nbsp;<form:errors path="dlvAttemptedSmsEnabled" />
               	</td>
               </tr>
               
               <tr>
                  <td>ETA Interval</td>
                  <td>
                    <form:input maxlength="50" size="30" path="ETAInterval"/>
                  </td>
                <td>
                  &nbsp;<form:errors path="ETAInterval" />
                </td>
               </tr>
               <tr><td colspan="3">&nbsp;</td></tr>
               <tr>
                  <td colspan="3" align="center">
                   <input type = "button" value="&nbsp;Back&nbsp;" onclick="javascript:doBack('zone');" /> &nbsp;&nbsp;
                   <input type = "submit" value="&nbsp;Save&nbsp;"  />
				   <input type = "button" value="&nbsp;Default Supervisor&nbsp;" onclick="javascript:showZoneSupervisorTable(document.getElementById('zoneCode').value);"/>
                </td>     
               </tr>
              </table>
            </td>
          </tr>               
        </table>
      
      </form:form>
     </div>
     </div>
     </div>
     <%@ include file='i_zonesupervisorinfo.jspf'%>    
	<form name="zone" action="zone.do?zoneType=Active" method="post">  </form>
  </tmpl:put>
</tmpl:insert>
