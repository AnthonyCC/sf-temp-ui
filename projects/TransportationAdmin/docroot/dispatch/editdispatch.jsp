<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
><%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
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
                            <form:select path="zoneCode">
                                <form:option value="" label="Select Zone"/>
                                <form:options items="${zones}" itemLabel="zoneCode" itemValue="zoneCode" />
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
                    <form:input maxlength="50" size="10" path="regionName" readOnly="true" />                  
                 </td>
                </tr>       
               <tr>
                  <td>Supervisor</td>
                  <td>                  
                  <form:select path="supervisorCode">
                        <form:option value="null" label="--Please Select Supervisor/>
                    <form:options items="${supervisors}" itemLabel="name" itemValue="employeeId" />
                   </form:select>
                 </td>
                <td>
                  &nbsp;<form:errors path="supervisorCode" />
                </td>
                </tr> 

                <tr>
                  <td>Start Time</td>
                  <td>  
                   <form:input maxlength="50" size="24" path="startTime" />
                    <%--                                    
                   <spring:bind path="dispatchForm.startTime"> 
                        <input type=text name="<c:out value="${status.expression}"/>" size="10" value="<fmt:formatDate value="${dispatchForm.startTime}" type="time" pattern="hh:mm a" />" />
                    </spring:bind>                
                    --%>
                 </td>
                <td>
                  &nbsp;<form:errors path="startTime" />
                </td>                 
                </tr>   
                <tr>
                  <td>First Dlv. Time</td>
                  <td>  
                   <form:input maxlength="50" size="24" path="firstDeliveryTime" />
                    <%--
                   <spring:bind path="dispatchForm.firstDeliveryTime"> 
                        <input type="text" name="<c:out value="${status.expression}"/>" size="10" value="<fmt:formatDate value="${dispatchForm.firstDeliveryTime}" type="time" pattern="hh:mm a" />" />
                    </spring:bind>                
                    --%>
                 </td>
                <td>
                  &nbsp;<form:errors path="firstDeliveryTime" />
                </td>                 
                </tr> 
                <tr>
                  <td>Route Number</td>
                  <td>                  
                    <form:select path="route">
                        <form:option value="" label="Select Route"/>
                    <form:options items="${routes}" itemLabel="routeNumber" itemValue="routeNumber" />
                   </form:select>
                   <c:forEach items="${routes}" var="route" varStatus="gridRow">
                        <input type="hidden" id = "route<c:out value="${route.routeNumber}"/>" name="route<c:out value="${route.routeNumber}"/>" value="<c:out value="${route.adHoc}"/>" />
                   </c:forEach>
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
                    <td> 	
                    
                    <c:forEach items="${dispatchForm.drivers}" var="driver" varStatus="gridRow">
                        <spring:bind path="dispatchForm.drivers[${gridRow.index}].employeeId">
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
                <td>
                  &nbsp;<form:errors path="drivers" />
                </td>
                </tr>
                <tr>
                <td>&nbsp;</td>
                <td>                
                    <c:forEach items="${dispatchForm.drivers}" var="driver" varStatus="gridRow">                        
                        <spring:bind path="dispatchForm.drivers[${gridRow.index}].nextelNo">
                            <input type="text" id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" 
                                value="<c:out value="${status.value}"/>" />
                        </spring:bind>
                    </c:forEach> 
<%--
                        <spring:bind path="dispatchForm.drivers">
                            <SELECT name="drivers" multiple="multiple">     
                                <OPTION value="">Please select drivers</OPTION>          
                                <c:forEach  var="driver" items="${drivers}"  >
                                    <c:set var="avail" value="true"/>
                                    <c:forEach var="idriver" items="${dispatchForm.drivers}">         
                                        <c:if test="${idriver.employeeId == driver.employeeId}">
                                        <OPTION selected="<c:out value="${driver.employeeId}"/>" value="<c:out value="${driver.employeeId}"/>"><c:out value="${driver.name}"/></OPTION>
                                        <c:remove var="avail"/>
                                        </c:if>       
                                    </c:forEach>
                                    <c:if test="${avail}">
                                        <OPTION value="<c:out value="${driver.employeeId}"/>"><c:out value="${driver.name}"/></OPTION>          
                                    </c:if>  
                                </c:forEach>
                            </SELECT>
                        </spring:bind>                
--%>                        
                    </td>
               </tr>
               
               <tr>
                  <td>Helpers</td>
                    <td> 	
                    <%--
                        <spring:bind path="dispatchForm.helpers">
                            <SELECT name="helpers" multiple="multiple">     
                                <OPTION value="">Please select helpers</OPTION>          
                                <c:forEach  var="helper" items="${helpers}"  >
                                    <c:set var="avail" value="true"/>
                                    <c:forEach var="ihelper" items="${dispatchForm.helpers}">         
                                        <c:if test="${ihelper.employeeId == helper.employeeId}">
                                        <OPTION selected="<c:out value="${helper.employeeId}"/>" value="<c:out value="${helper.employeeId}"/>"><c:out value="${helper.name}"/></OPTION>
                                        <c:remove var="avail"/>
                                        </c:if>       
                                    </c:forEach>
                                    <c:if test="${avail}">
                                        <OPTION value="<c:out value="${helper.employeeId}"/>"><c:out value="${helper.name}"/></OPTION>          
                                    </c:if>  
                                </c:forEach>
                            </SELECT>
                        </spring:bind>    
            --%>                        
                    <c:forEach items="${dispatchForm.helpers}" var="driver" varStatus="gridRow">
                        <spring:bind path="dispatchForm.helpers[${gridRow.index}].employeeId">
                            <SELECT id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" >
                                <OPTION value="">Select Helpers</OPTION>          
                                <c:forEach var="driverEmp" items="${helpers}">
                                    <option <c:if test='${status.value == driverEmp.employeeId}'> SELECTED </c:if> 
                                value="<c:out value="${driverEmp.employeeId}"/>"><c:out value="${driverEmp.name}"/></option>                         
                                </c:forEach>
                            </SELECT>
                        </spring:bind>
                    </c:forEach>
                </td>
                <td>
                  &nbsp;<form:errors path="helpers" />
                </td>                
                </tr>                
                <tr>
                <td>&nbsp;</td>
                <td>                
                    <c:forEach items="${dispatchForm.helpers}" var="driver" varStatus="gridRow">                        
                        <spring:bind path="dispatchForm.helpers[${gridRow.index}].nextelNo">
                            <input type="text" id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" 
                                value="<c:out value="${status.value}"/>" />
                        </spring:bind>
                    </c:forEach> 
                    
                    </td>

               </tr>
               
                <tr>
                  <td>Runners</td>
                    <td> 	
                    <%--
                        <spring:bind path="dispatchForm.runners">
                            <SELECT name="runners" multiple="multiple">     
                                <OPTION value="">Please select runners</OPTION>          
                                <c:forEach  var="runner" items="${runners}"  >
                                    <c:set var="avail" value="true"/>
                                    <c:forEach var="irunner" items="${dispatchForm.runners}">         
                                        <c:if test="${irunner.employeeId == runner.employeeId}">
                                        <OPTION selected="<c:out value="${runner.employeeId}"/>" value="<c:out value="${runner.employeeId}"/>"><c:out value="${runner.name}"/></OPTION>
                                        <c:remove var="avail"/>
                                        </c:if>       
                                    </c:forEach>
                                    <c:if test="${avail}">
                                        <OPTION value="<c:out value="${runner.employeeId}"/>"><c:out value="${runner.name}"/></OPTION>          
                                    </c:if>  
                                </c:forEach>
                            </SELECT>
                        </spring:bind>   
--%>                        
                    <c:forEach items="${dispatchForm.runners}" var="driver" varStatus="gridRow">
                        <spring:bind path="dispatchForm.runners[${gridRow.index}].employeeId">
                            <SELECT id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" >
                                <OPTION value="">Select Runners</OPTION>          
                                <c:forEach var="driverEmp" items="${runners}">
                                    <option <c:if test='${status.value == driverEmp.employeeId}'> SELECTED </c:if> 
                                value="<c:out value="${driverEmp.employeeId}"/>"><c:out value="${driverEmp.name}"/></option>                         
                                </c:forEach>
                            </SELECT>
                        </spring:bind>
                    </c:forEach>
                </td>
                <td>
                  &nbsp;<form:errors path="runners" />
                </td>                
                </tr>                
                <tr>
                <td>&nbsp;</td>
                <td>                
                    <c:forEach items="${dispatchForm.runners}" var="driver" varStatus="gridRow">                        
                        <spring:bind path="dispatchForm.runners[${gridRow.index}].nextelNo">
                            <input type="text" id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" 
                                value="<c:out value="${status.value}"/>" />
                        </spring:bind>
                    </c:forEach> 
                    
                    </td>

               </tr>
                <tr>
                  <td>Status</td>
                  <td>                  
                    <form:select path="status">
                        <form:option value="null" label="Select Status"/>
                    <form:options items="${statuses}" itemLabel="name" itemValue="code" />
                   </form:select>
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
