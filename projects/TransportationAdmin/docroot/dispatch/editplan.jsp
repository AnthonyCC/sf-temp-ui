<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.web.model.WebPlanInfo' %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<% boolean hasErrors = session.getAttribute("apperrors") != null; 
   WebPlanInfo _plan=null;
   if(request.getAttribute("planForm")!=null)
        _plan=(WebPlanInfo)request.getAttribute("planForm");

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
    <script language="javascript" src="js/mootools.v1.11.js"></script>
    <script language="javascript" src="js/nogray_time_picker.js"></script>      
    <script language="javascript">
        window.addEvent("domready", function (){
            var dateObjStartTime = new Date("July 21, 1983 "+document.getElementById('startTime').value);
            var dateObjFirstDlvTime = new Date("July 21, 1983 "+document.getElementById('firstDeliveryTime').value);
            
            var tpStartTime = new TimePicker('timeStart_picker', 'startTime', 'timeStart_toggler', {imagesPath:"images",
                                        startTime: {hour:dateObjStartTime.getHours(), minute: dateObjStartTime.getMinutes()}});
            
            var tpFirstDlvTime = new TimePicker('timeFirstDlv_picker', 'firstDeliveryTime', 'timeFirstDlv_toggler', {imagesPath:"images",
                                        startTime: {hour:dateObjFirstDlvTime.getHours(), minute: dateObjFirstDlvTime.getMinutes()}});
                                           
        });

    </script>     

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Add/Edit Plan</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/> 
    <div align="center">
      <form:form commandName = "planForm" method="post">
      <form:hidden path="planId"/>
      <form:hidden path="ignoreErrors"/>
      <form:hidden path="errorDate"/>
     <form:hidden path="zoneModified"/>
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle">Add/Edit Plan</td>
          </tr>
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
          </tr>
          
          <tr>
            <td class="screencontent">
              <table class="forms1">  
               <tr>
                  <td>Date</td>
                  <td>
                    <form:input maxlength="50" size="24" path="planDate" />
                  
                  &nbsp;<a href="#" id="trigger_planDate" style="font-size: 9px;">
                        <img src="images/calendar.gif"  style="border:0"  alt=">>" />
                        </a>
                     <script language="javascript">                 
                      Calendar.setup(
                      {
                        showsTime : false,
                        electric : false,
                        inputField : "planDate",
                        ifFormat : "%m/%d/%Y",
                        singleClick: true,                                            
                        button : "trigger_planDate",
                        onUpdate : updateDate                        
                     }
                      );
                      function updateDate(cal) {
                          var selIndex = cal.date.getDay();
                          if(selIndex == 0) selIndex = 7;
                         // document.getElementById('dispatchDay').selectedIndex =  selIndex;
                        };
                    </script>
                </td>   
                <td>
                  &nbsp;<form:errors path="planDate" />
                </td>
               </tr>  
               <tr>
                  <td>Zone</td>
                  <td> 
                  <c:if test="${!empty planForm.zoneCode }">
                    <c:set var="hasZone" value="true"/>
                    </c:if>
                    <c:if test="${planForm.isBullpen eq 'Y' }">
                    <c:set var="_isBullpen" value="true"/>
                    </c:if>
                  <form:select path="zoneCode" disabled="${_isBullpen}" onChange="zoneChanged()">
                        <form:option value="null" label="--Please Select Zone"/>
                    <form:options items="${zones}" itemLabel="displayName" itemValue="zoneCode" />
                   </form:select>
                </td>
                <td>
                  &nbsp;<form:errors path="zoneCode" />
                </td>
               </tr>
               <tr>
                  <td>Region</td>
                  <td> 
                  <form:select path="regionCode" disabled="${hasZone}">
                        <form:option value="null" label="--Please Select Region"/>
                    <form:options items="${regions}" itemLabel="code" itemValue="code" />
                   </form:select>
                </td>
                <td>
                  &nbsp;<form:errors path="regionCode" />
                </td>
               </tr>  
               <tr>
                <td>Bullpen</td>         
                <td><form:checkbox path="isBullpen" value="Y" onclick="bullpen()"/></td>               
               <tr>
                  <td>Supervisor</td>
                  <td> 
                  <form:select path="supervisorCode">
                        <form:option value="null" label="--Please Select Supervisor"/>
                    <form:options items="${supervisors}" itemLabel="name" itemValue="employeeId" />
                   </form:select>
                </td>
                <td>
                  &nbsp;<form:errors path="supervisorCode" />
                </td>
               </tr>
            <tr>
                  <td><a id="timeStart_toggler">Start&nbsp;Time</a></td>
                  <!-- <td> <a id="startTime_toggler">&nbsp;</a></td> -->
                  <td>         
                   <form:input maxlength="50" size="24" path="startTime" />  
                   <div id="timeStart_picker" class="time_picker_div"></div>
                 </td>
                <td>
                  &nbsp;<form:errors path="startTime" />
                </td>                 
                </tr>   
                <tr>
                  <td><a id="timeFirstDlv_toggler">First Dlv.&nbsp;Time</a></td>
                  <!-- <td> <a id="firstDeliveryTime_toggler">&nbsp;</a></td> -->
                  <td>     
                  <form:input maxlength="50" size="24" path="firstDeliveryTime" />
                  <div id="timeFirstDlv_picker" class="time_picker_div"></div>               
                 </td>
                <td>
                  &nbsp;<form:errors path="firstDeliveryTime" />
                </td>                 
                </tr>
                 
                <tr>

                    <td>Drivers (Req:<spring:bind path="planForm.driverReq"><c:out value="${planForm.driverReq}"/></spring:bind> Max: <spring:bind path="planForm.driverMax"><c:out value="${planForm.driverMax}"/></spring:bind>)</td>

                    <td> 	
                    <c:forEach items="${planForm.drivers}" var="driver" varStatus="gridRow">
                        <spring:bind path="planForm.drivers[${gridRow.index}].employeeId">
                               <SELECT id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" >
                                <OPTION value="">Select Drivers</OPTION>          
                                <c:forEach var="driverEmp" items="${drivers}">
                                    <option <c:if test='${status.value == driverEmp.employeeId}'> SELECTED </c:if> 
                                value="<c:out value="${driverEmp.employeeId}"/>"><c:out value="${driverEmp.name}"/></option>                         
                                </c:forEach>
                            </SELECT>
                        </spring:bind>
                    </c:forEach>
                    </td>
                    <td> &nbsp;<form:errors path="drivers" /></td>
               </tr>
               
                <tr>
                    <td>Helpers (Req:<spring:bind path="planForm.helperReq"><c:out value="${planForm.helperReq}"/></spring:bind> Max: <spring:bind path="planForm.helperMax"><c:out value="${planForm.helperMax}"/></spring:bind>)</td>
                    
                    <td> 	
                    <c:forEach items="${planForm.helpers}" var="helper" varStatus="gridRow">
                        <spring:bind path="planForm.helpers[${gridRow.index}].employeeId">
                            <SELECT id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" >
                                <OPTION value="">Select Helpers</OPTION>          
                                <c:forEach var="helperEmp" items="${helpers}">
                                    <option <c:if test='${status.value == helperEmp.employeeId}'> SELECTED </c:if> 
                                value="<c:out value="${helperEmp.employeeId}"/>"><c:out value="${helperEmp.name}"/></option>                         
                                </c:forEach>
                            </SELECT>
                        </spring:bind>
                    </c:forEach>
                    </td>
                    <td> &nbsp;<form:errors path="helpers" /></td>
               </tr>               
               <tr>
                    <td>Runners (Req:<spring:bind path="planForm.runnerReq"><c:out value="${planForm.runnerReq}"/></spring:bind> Max: <spring:bind path="planForm.runnerMax"><c:out value="${planForm.runnerMax}"/></spring:bind>)</td>
                    
                    <td> 	
                    <c:forEach items="${planForm.runners}" var="helper" varStatus="gridRow">
                        <spring:bind path="planForm.runners[${gridRow.index}].employeeId">
                            <SELECT id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" >
                                <OPTION value="">Select Runners</OPTION>          
                                <c:forEach var="runnerEmp" items="${runners}">
                                    <option <c:if test='${status.value == runnerEmp.employeeId}'> SELECTED </c:if> 
                                value="<c:out value="${runnerEmp.employeeId}"/>"><c:out value="${runnerEmp.name}"/></option>                         
                                </c:forEach>
                            </SELECT>
                        </spring:bind>
                    </c:forEach>
                    </td>
                    <td> &nbsp;<form:errors path="runners" /></td>
               </tr>   
               
                <tr><td colspan="3">&nbsp;</td></tr>
                <tr>
                              <td>Sequence</td>
							    <td> 								  
							  	 	<form:input maxlength="50" size="30" path="sequence" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="sequence" />
							 	</td>
				 </tr>                                           
              <tr>
                  <% if(hasErrors) { %>
                  <td align="center">
                   <input type= "button" align="center" value="&nbsp;Continue&nbsp;" 
                        onclick="submitData()" />
                </td>
                <td align="center">
                   <input type = "button" value="&nbsp;Cancel&nbsp;" onclick="javascript:location.href ='plan.do'" />
                </td>
                <td/>
                <% } else { %>
                <td colspan="3" align="center">
                   <input type = "submit" value="&nbsp;Save&nbsp;"  />
                </td> 
                <% } %>   
              </tr>

                           
        </table>
        <script language="javascript">   
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
      </form:form>
     </div>
     
  </tmpl:put>
</tmpl:insert>
