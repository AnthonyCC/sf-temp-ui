<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%
 String id = request.getParameter("id") != null ? request.getParameter("id") : "";
 String dispDate = request.getParameter("dispDate") != null ? request.getParameter("dispDate") : "";
 %>
<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Add/Edit Dispatch</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/> 
    <div align="center">
      <form:form commandName = "dispatchForm" method="post">
      <form:hidden path="dispatchId"/>
      <form:hidden path="dispatchDate" />
      <input type=hidden name="routeNo" value="" />
      <input type=hidden name="zoneId" value="" />
      <input type=hidden name="dispDate" value="<%=dispDate %>" />
      
       <script language="javascript">         
        function getTruckNumber(routeNo) {
            var adHoc = eval("dispatchForm.route"+routeNo+".value");
            if(adHoc=="false" && routeNo.length > 0) {
                dispatchForm.routeNo.value = routeNo;
                dispatchForm.submit();         
            }
        }
        function getResourceInfo(zoneId) {
            alert(zoneId);
            if(zoneId.length > 0) {
                dispatchForm.zoneId.value = zoneId;
                dispatchForm.submit();         
            }
        }
       function resetDetails() {
             document.location.href='<c:out value="${pageContext.request.contextPath}" />/editdispatch.do?id=<%= id %>&dispDate=<%= dispDate %>';
        }
       function init_clock(){
            var tpStartTime = new TimePicker('timeStart_picker', 'startTime', 'timeStart_toggler', {imagesPath:"images"});
            var tpFirstDlvTime = new TimePicker('timeFirstDlv_picker', 'firstDeliveryTime', 'timeFirstDlv_toggler', {imagesPath:"images"});
	    }
        
        function $() {
            var elements = new Array();
            for (var i = 0; i < arguments.length; i++) {
                var element = arguments[i];
                if (typeof element == 'string')
                    element = document.getElementById(element);
                if (arguments.length == 1)
                    return element;
                elements.push(element);
            }
            return elements;
        }
        function getResourcesInfo() {
            dispatchForm.submit();
        }
      </script>
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
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle">Add/Edit Dispatch</td>
          </tr>
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
          </tr>
          
          <tr>
            <td class="screencontent">
              <table class="forms1" border="0">                                  
               <tr>
                  <td>Zone</td>
                  <td>         
                  
                    <spring:bind path="dispatchForm.zoneCode">   
                        <c:choose>                    
                        <c:when test='${status.value == ""}'> 
                            <form:select path="zoneCode" onChange="javascript:getResourcesInfo();">
                                <form:option value="" label="Select Zone"/>
                                <form:options items="${zones}" itemLabel="displayName" itemValue="zoneCode" />
                            </form:select>
                        </c:when>
                         <c:otherwise> 
                               <form:input maxlength="50" size="10" path="zoneCode" readOnly="true" />
                        </c:otherwise> 
                         </c:choose>
                     </spring:bind>  
                 </td>
                </tr>
                <tr>
                  <td>Confirmed</td>
                  <td> 
                  <form:checkbox path="confirmed" />
                </td>
              </tr>
               <tr>
                  <td>Region</td>
                  <td>       
                    <form:hidden path="regionCode"/>                 
                    <form:input maxlength="50" size="10" path="regionName" readOnly="true" cssClass="noborder"/>                  
                 </td>
                </tr>       
               <tr>
                  <td>Supervisor</td>
                  <td>      
                    <spring:bind path="dispatchForm.confirmed"> 
                        <c:choose>                    
                        <c:when test='${status.value == "false"}'> 
                          <form:select path="supervisorCode" disabled='<c:out value="${status.value}" />'>
                                <form:option value="" label="Select Supervisor"/>
                            <form:options items="${supervisors}" itemLabel="name" itemValue="employeeId" />
                           </form:select>
                        </c:when>
                         <c:otherwise> 
                               <form:input maxlength="50" size="30" path="supervisorName" readOnly="true" />
                        </c:otherwise> 
                         </c:choose>
                     </spring:bind>  
                 </td>                   
                 </td>
                <td>
                  &nbsp;<form:errors path="supervisorCode" />
                </td>
                </tr> 

                <tr>
                  <td><a id="timeStart_toggler">Start&nbsp;Time</a></td>
                  <td>  
                    <spring:bind path="dispatchForm.confirmed"> 
                        <c:choose>                    
                        <c:when test='${status.value == "false"}'> 
                            <form:input maxlength="50" size="8" path="startTime" />
                            <div id="timeStart_picker" class="time_picker_div"></div>
                        </c:when>
                         <c:otherwise> 
                         <form:input maxlength="50" size="8" path="startTime" readOnly="true"/>
                        </c:otherwise> 
                         </c:choose>
                     </spring:bind> 
                     
                      

                 </td>
                <td>
                  &nbsp;<form:errors path="startTime" />
                </td>                 
                </tr>   
                <tr>
                  <td><a id="timeFirstDlv_toggler">First Dlv.&nbsp;Time</a></td>
                  <td>  
                     <spring:bind path="dispatchForm.confirmed"> 
                        <c:choose>                    
                        <c:when test='${status.value == "false"}'> 
                            <form:input maxlength="50" size="8" path="firstDeliveryTime" />
                            <div id="timeFirstDlv_picker" class="time_picker_div"></div>
                        </c:when>
                         <c:otherwise> 
                         <form:input maxlength="50" size="8" path="firstDeliveryTime" readOnly="true"/>
                        </c:otherwise> 
                         </c:choose>
                     </spring:bind> 
                 </td>
                <td>
                  &nbsp;<form:errors path="firstDeliveryTime" />
                </td>                 
                </tr> 
                <tr>
                  <td>Route Number</td>
                  <td>          
                    <spring:bind path="dispatchForm.confirmed"> 
                        <c:choose>                    
                        <c:when test='${status.value == "false"}'> 
                            <form:select path="route">
                                <form:option value="" label="Select Route"/>
                            <form:options items="${routes}" itemLabel="routeNumber" itemValue="routeNumber" />
                           </form:select>
                           <c:forEach items="${routes}" var="route" varStatus="gridRow">
                                <input type="hidden" id = "route<c:out value="${route.routeNumber}"/>" name="route<c:out value="${route.routeNumber}"/>" value="<c:out value="${route.adHoc}"/>" />
                           </c:forEach>
                        </c:when>
                         <c:otherwise> 
                               <form:input maxlength="50" size="8" path="route" readOnly="true" />
                        </c:otherwise> 
                         </c:choose>
                     </spring:bind> 

                 </td>
                <td>
                  &nbsp;<form:errors path="route" />
                </td>                  
                </tr> 
                <tr>
                  <td>Truck Number</td>
                  <td>  
                    <spring:bind path="dispatchForm.truck">   
                     <c:choose>                    
                        <c:when test='${status.value == ""}'> 
                          <form:select path="truck" >
                                <form:option value="" label="Select Truck"/>
                            <form:options items="${trucks}" />
                           </form:select>
                        </c:when>
                         <c:otherwise> 
                             <form:input maxlength="50" size="10" path="truck" readOnly="true" />
                        </c:otherwise> 
                     </c:choose>  
                    </spring:bind>                          
                 </td>
                <td>
                  &nbsp;<form:errors path="truck" />
                </td>                  
                </tr> 
               <tr>
                  <td>Drivers</td>
                    <td align="left"> 	
                    <table cellpadding="0" cellspacing="0" border="0">
                    <tr> 
                    <c:forEach items="${dispatchForm.drivers}" var="driver" varStatus="gridRow">
                        <spring:bind path="dispatchForm.drivers[${gridRow.index}].employeeId">
                     <td align="left">   
                         <c:choose>                    
                           <c:when test='${dispatchForm.confirmed == "false"}'> 
                            <SELECT id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" >
                                <OPTION value="">Select Drivers</OPTION>          
                                <c:forEach var="driverEmp" items="${drivers}">
                                    <option <c:if test='${status.value == driverEmp.employeeId}'> SELECTED </c:if> 
                                value="<c:out value="${driverEmp.employeeId}"/>"><c:out value="${driverEmp.name}"/></option>                         
                                </c:forEach>
                            </SELECT>
                           </c:when>
                           <c:otherwise> 
                            <SELECT id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" disabled="true">
                                <OPTION value="">Select Drivers</OPTION>          
                                <c:forEach var="driverEmp" items="${drivers}">
                                    <option <c:if test='${status.value == driverEmp.employeeId}'> SELECTED </c:if> 
                                value="<c:out value="${driverEmp.employeeId}"/>"><c:out value="${driverEmp.name}"/></option>                         
                                </c:forEach>
                            </SELECT>    
                           </c:otherwise> 
                         </c:choose>
                      </td>      
                        </spring:bind>
                     </c:forEach> 
                        <td>
                          &nbsp;<form:errors path="drivers" />
                        </td>
                </tr>
                <tr>
                    <c:forEach items="${dispatchForm.drivers}" var="driver" varStatus="gridRow">                        
                        <spring:bind path="dispatchForm.drivers[${gridRow.index}].nextelNo">
                        <td>
                            <img src="./images/icons/nextel.gif" width="16" height="16" border="0" alt="Nextel Number" title="Nextel Number">&nbsp;<input type="text" size="8" id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" 
                                value="<c:out value="${status.value}"/>" />
                        </td>        
                        </spring:bind>
                    </c:forEach> 
                 </tr>
                 </table>
                </td>
                </tr>
               <tr>
                  <td>Helpers</td>
                    <td> 	
                    <table cellpadding="0" cellspacing="0">
                    <tr>
                    <c:forEach items="${dispatchForm.helpers}" var="helper" varStatus="gridRow">
                        <spring:bind path="dispatchForm.helpers[${gridRow.index}].employeeId">
                        <td>
                         <c:choose>                    
                           <c:when test='${dispatchForm.confirmed == "false"}'> 
                            <SELECT id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" >
                                <OPTION value="">Select Helpers</OPTION>          
                                <c:forEach var="helperEmp" items="${helpers}">
                                    <option <c:if test='${status.value == helperEmp.employeeId}'> SELECTED </c:if> 
                                value="<c:out value="${helperEmp.employeeId}"/>"><c:out value="${helperEmp.name}"/></option>                         
                                </c:forEach>
                            </SELECT>
                           </c:when>
                           <c:otherwise> 
                            <SELECT id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" disabled="true">
                                <OPTION value="">Select Helpers</OPTION>          
                                <c:forEach var="helperEmp" items="${helpers}">
                                    <option <c:if test='${status.value == helperEmp.employeeId}'> SELECTED </c:if> 
                                value="<c:out value="${helperEmp.employeeId}"/>"><c:out value="${helperEmp.name}"/></option>                         
                                </c:forEach>
                             </SELECT>   
                           </c:otherwise> 
                         </c:choose>
                        </td>
                        </spring:bind>
                    </c:forEach>
                        <td>
                          &nbsp;<form:errors path="helpers" />
                        </td>                
                    </tr>                
                    <tr>
                    <c:forEach items="${dispatchForm.helpers}" var="helper" varStatus="gridRow">                        
                        <spring:bind path="dispatchForm.helpers[${gridRow.index}].nextelNo">
                        <td>
                            <img src="./images/icons/nextel.gif" width="16" height="16" border="0" alt="Nextel Number" title="Nextel Number">&nbsp;<input type="text" size="8" id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" 
                                value="<c:out value="${status.value}"/>" />
                        </td>        
                        </spring:bind>
                      </c:forEach>  
                    </tr>
                </table>
                </td>
                </tr>                
                <tr>
                  <td>Runners</td>
                    <td> 	
                    <table cellpadding="0" cellspacing="0">
                    <tr>
                    <c:forEach items="${dispatchForm.runners}" var="runner" varStatus="gridRow">
                        <spring:bind path="dispatchForm.runners[${gridRow.index}].employeeId">
                        <td>
                         <c:choose>                    
                           <c:when test='${dispatchForm.confirmed == "false"}'> 
                            <SELECT id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" >
                                <OPTION value="">Select Runners</OPTION>          
                                <c:forEach var="runnerEmp" items="${runners}">
                                    <option <c:if test='${status.value == runnerEmp.employeeId}'> SELECTED </c:if> 
                                value="<c:out value="${runnerEmp.employeeId}"/>"><c:out value="${runnerEmp.name}"/></option>                         
                                </c:forEach>
                            </SELECT>
                           </c:when>
                           <c:otherwise> 
                            <SELECT id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" disabled="true">
                                <OPTION value="">Select Runners</OPTION>          
                                <c:forEach var="runnerEmp" items="${runners}">
                                    <option <c:if test='${status.value == runnerEmp.employeeId}'> SELECTED </c:if> 
                                value="<c:out value="${runnerEmp.employeeId}"/>"><c:out value="${runnerEmp.name}"/></option>                         
                                </c:forEach>
                             </SELECT>   
                           </c:otherwise> 
                         </c:choose>
                            </SELECT>
                         </td>   
                        </spring:bind>
                    </c:forEach>
                        <td>
                          &nbsp;<form:errors path="runners" />
                        </td>                
                    </tr>                
                    <tr>
                    <c:forEach items="${dispatchForm.runners}" var="runner" varStatus="gridRow">                        
                        <spring:bind path="dispatchForm.runners[${gridRow.index}].nextelNo">
                        <td>
                            <img src="./images/icons/nextel.gif" width="16" height="16" border="0" alt="Nextel Number" title="Nextel Number">&nbsp;<input type="text" size="8" id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" 
                                value="<c:out value="${status.value}"/>" />
                        </td>        
                        </spring:bind>
                    </c:forEach> 
                    </tr>
                    </table>
                    </td>
                </tr>
                <tr>
                  <td>Status</td>
                  <td>                  
                    <spring:bind path="dispatchForm.confirmed"> 
                        <c:choose>                    
                        <c:when test='${status.value == "true"}'> 
                            <form:select path="status">
                                <form:option value="null" label="Select Status"/>
                                <form:options items="${statuses}" itemLabel="name" itemValue="code" />
                           </form:select>
                        </c:when>
                         <c:otherwise> 
                            <form:select path="status" disabled="true">
                                <form:option value="null" label="Select Status"/>
                                <form:options items="${statuses}" itemLabel="name" itemValue="code" />
                           </form:select>
                        </c:otherwise> 
                         </c:choose>
                     </spring:bind>                   
                 </td>
                </tr> 
                <tr>
                  <td>Comments</td>
                  <td>                  
                    <form:textarea path="comments" rows="5" cols="45" />
                </td>
                <td>
                  &nbsp;<form:errors path="comments" />
                </td>
               </tr>                
              <tr><td colspan="3">&nbsp;</td></tr>
              <tr>
                  <td colspan="3" align="center">
                   <input type = "submit" value="&nbsp;Save&nbsp;"  />
                   <input type = "button" value="&nbsp;Reset&nbsp;" onclick="javascript:resetDetails();" />
                </td> 
              </tr>
              </table>        

            </td>
          </tr>               
        </table>
      
      </form:form>
     </div>
     
  </tmpl:put>
</tmpl:insert>
