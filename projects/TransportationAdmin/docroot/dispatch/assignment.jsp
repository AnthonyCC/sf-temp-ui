<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Dispatch Assignment</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/> 
    <div class="contentroot">
      <form method="post">
      <spring:bind path="assignmentForm.submitAssignment">  
        <input type="hidden" id="submitAssignment" name="<c:out value="${status.expression}"/>"
                      id="<c:out value="${status.expression}"/>"
                            value="<c:out value="${status.value}"/>" />     
      </spring:bind>  
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle">Dispatch Assignment</td>
          </tr>
          <c:if test="${not empty messages}">
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
          </tr>
          </c:if>         
          <tr>
            <td class="screencontent">
              <table class="forms1">          
                <tr>
                  <td>Select Date</td>
                  <td> 
                    <spring:bind path="assignmentForm.planDate"> 
                                     
                    <input maxlength="10" size="10" name="<c:out value="${status.expression}"/>"
                      id="<c:out value="${status.expression}"/>"
                            value="<c:out value="${status.value}"/>" />                          
                    </spring:bind>
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
                        button : "trigger_planDate" 
                       }
                      );
                                
                      
                      function submitSearch() {
                        document.getElementById('submitAssignment').value = '0';                        
                        document.forms[0].submit();
                      }
                      
                      function submitSelectedAssignment() {
                        var table = document.getElementById('dispatch_table');
                        var checkboxList = table.getElementsByTagName("input");    
                        var hasValues = false;
                        for (i = 0; i < checkboxList.length; i++) {
                          if (checkboxList[i].type=="checkbox" && checkboxList[i].checked) {                            
                            hasValues = true;
                          }
                        }
                        if(hasValues) {
                          document.getElementById('submitAssignment').value = '1';                        
                          document.forms[0].submit();
                        } else {
                          alert('Please Select a Row!');
                        }
                      }
              
                  </script>
                </td>
                <td>
                  &nbsp;
                  <spring:bind path="assignmentForm.planDate">
                  <span id="planDate.errors">      
                      <c:forEach items="${status.errorMessages}" var="error">
                  <c:out value="${error}"/>&nbsp;
                </c:forEach>
             </span>
           </spring:bind>   
                </td>
               
                  <td>Select Zone</td>
                  <td>
                    <spring:bind path="assignmentForm.zoneId">
                      <select name="<c:out value="${status.expression}"/>">
                      <option value="">--Please Select Zone</option> 
                      <c:forEach var="zoneFilterRow" items="${zones}">                             
                          <c:choose>
                            <c:when test="${status.value == zoneFilterRow.zoneId}" > 
                              <option selected value="<c:out value="${zoneFilterRow.zoneId}"/>"><c:out value="${zoneFilterRow.zoneNumber}"/></option>
                            </c:when>
                            <c:otherwise> 
                              <option value="<c:out value="${zoneFilterRow.zoneId}"/>"><c:out value="${zoneFilterRow.zoneNumber}"/></option>
                            </c:otherwise> 
                          </c:choose>      
                        </c:forEach>   
                  </select>
                   </spring:bind>
                </td>
                <td>
                  &nbsp;
                   <spring:bind path="assignmentForm.zoneId">
                  <span id="planDate.errors">      
                      <c:forEach items="${status.errorMessages}" var="error">
                  <c:out value="${error}"/>&nbsp;
                </c:forEach>
             </span>
           </spring:bind>
                </td>
              
              
                <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                    <td colspan="3" align="center">
                     <input type = "button" id="searchButton" value="&nbsp;Go&nbsp;" onclick="javascript:submitSearch()" />
                  </td>
                <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                    <td colspan="3" align="center">
                     <input type = "button" id="assignButton" value="&nbsp;Save Assignment&nbsp;" onclick="javascript:submitSelectedAssignment()"  />
                  </td>       
              </tr>
              </table>        
              
            </td>
          </tr>               
        </table>    
      
     </div>
     
    <div align="center" class="eXtremeTable" >

      <table id="dispatch_table"  border="0"  cellspacing="0"  cellpadding="0"  class="tableRegion"  width="98%" >
        <thead> 
        <tr>
          <td class="tableHeader" width="5px">Zone</td>
          <td class="tableHeader" width="5px">Start Time</td>
          <td class="tableHeader" width="5px">End Time</td>
          <td class="tableHeader">Driver</td>
          <td class="tableHeader">Helper1</td>
          <td class="tableHeader">Helper2</td>  
          <td class="tableHeader">Route</td>
          <td class="tableHeader">Supervisor</td> 
          <td class="tableHeader">Truck</td>  
          <td class="tableHeader">Nextel</td>
        </tr>
        </thead>
        <tbody class="tableBody" >                
          <c:forEach items="${assignmentForm.planDataList}" varStatus="gridRow">
            <spring:bind path="assignmentForm.planDataList[${gridRow.index}].planId">
                <input type="hidden" name="<c:out value="${status.expression}"/>"
                  id="<c:out value="${status.expression}"/>"
                  value="<c:out value="${status.value}"/>" />
             </spring:bind>
            <tr class="odd">
              <td width="5px">
                <spring:bind path="assignmentForm.planDataList[${gridRow.index}].selected">                                 
                   <input type="checkbox" name="<c:out value="${status.expression}"/>"
                      id="<c:out value="${status.expression}"/>"
                            >
                  </spring:bind>
                  <spring:bind path="assignmentForm.planDataList[${gridRow.index}].zoneId">
                  <c:out value="${status.value}"/></input>
                  <input type="hidden" name="<c:out value="${status.expression}"/>"
                    id="<c:out value="${status.expression}"/>"
                    value="<c:out value="${status.value}"/>" />
                  </spring:bind>
              </td>             
              <td>
                <spring:bind path="assignmentForm.planDataList[${gridRow.index}].slotId">                                 
                    <select class="editselect" id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>">
                      <option value="">--Start Time</option>  
                     <c:forEach var="timeRow" items="${timeslots}">       
                        <option <c:if test='${status.value == timeRow.slotId}'> SELECTED </c:if>
                          value="<c:out value="${timeRow.slotId}"/>"><c:out value="${timeRow.slotName}"/></option>                         
                      </c:forEach>
                    </select>
                  </spring:bind>
              </td>
               <td>
                <spring:bind path="assignmentForm.planDataList[${gridRow.index}].endSlotId">                                 
                    <select class="editselect" id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>">
                      <option value="">--End Time</option>  
                     <c:forEach var="timeRow" items="${timeslots}">       
                        <option <c:if test='${status.value == timeRow.slotId}'> SELECTED </c:if>
                          value="<c:out value="${timeRow.slotId}"/>"><c:out value="${timeRow.slotName}"/></option>                         
                      </c:forEach>
                    </select>
                  </spring:bind>
              </td>
              <td>
                <spring:bind path="assignmentForm.planDataList[${gridRow.index}].driverId">                                 
                    <select class="editselect" id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>">
                      <option value="">--Select Driver</option>  
                      <c:forEach var="driverRow" items="${drivers}"> 
                        <option <c:if test='${status.value == driverRow.employeeId}'> SELECTED </c:if>
                          value="<c:out value="${driverRow.employeeId}"/>"><c:out value="${driverRow.name}"/></option>                              
                      </c:forEach>
                    </select>
                  </spring:bind>
              </td>
              <td>
                <spring:bind path="assignmentForm.planDataList[${gridRow.index}].primaryHelperId">                                  
                    <select class="editselect" id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>">
                      <option value="">--Select Helper1</option>  
                     <c:forEach var="h1Row" items="${helpers}"> 
                        <option <c:if test='${status.value == h1Row.employeeId}'> SELECTED </c:if> 
                          value="<c:out value="${h1Row.employeeId}"/>"><c:out value="${h1Row.name}"/></option>                             
                      </c:forEach>
                    </select>
                  </spring:bind>
              </td>
              <td>
                <spring:bind path="assignmentForm.planDataList[${gridRow.index}].secondaryHelperId">                                  
                    <select class="editselect" id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>">
                      <option value="">--Select Helper2</option>  
                     <c:forEach var="h2Row" items="${helpers}"> 
                        <option <c:if test='${status.value == h2Row.employeeId}'> SELECTED </c:if>  
                          value="<c:out value="${h2Row.employeeId}"/>"><c:out value="${h2Row.name}"/></option>                            
                      </c:forEach>
                    </select>
                  </spring:bind>
              </td>
              <td>
                <spring:bind path="assignmentForm.planDataList[${gridRow.index}].routeId">                                  
                    <select class="editselect" id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>">
                      <option value="">--Select Route</option>
                     <c:forEach var="routeRow" items="${routes}"> 
                        <option <c:if test='${status.value == routeRow.routeId}'> SELECTED </c:if>
                          value="<c:out value="${routeRow.routeId}"/>"><c:out value="${routeRow.routeNumber}"/></option>
                      </c:forEach>
                    </select>
                  </spring:bind>
              </td>
              <td>
                <spring:bind path="assignmentForm.planDataList[${gridRow.index}].supervisorId">                                 
                    <select class="editselect" id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>">
                      <option value="">--Select Supervisor</option>
                     <c:forEach var="supRow" items="${supervisors}"> 
                        <option <c:if test='${status.value == supRow.employeeId}'> SELECTED </c:if>  
                          value="<c:out value="${supRow.employeeId}"/>"><c:out value="${supRow.name}"/></option>                          
                      </c:forEach>
                    </select>
                  </spring:bind>
              </td>
              <td>
                <spring:bind path="assignmentForm.planDataList[${gridRow.index}].truckId">                                  
                    <select class="editselect" id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>">
                      <option value="">--Select Truck</option>
                     <c:forEach var="truckRow" items="${trucks}">  
                        <option <c:if test='${status.value == truckRow.truckId}'> SELECTED </c:if>  
                          value="<c:out value="${truckRow.truckId}"/>"><c:out value="${truckRow.truckNumber}"/></option>                         
                      </c:forEach>
                    </select>
                  </spring:bind>
              </td>
              <td>
                <spring:bind path="assignmentForm.planDataList[${gridRow.index}].nextelId">                                 
                    <input class="editinput" maxlength="20" size="20" type="text" name="<c:out value="${status.expression}"/>"
                      id="<c:out value="${status.expression}"/>"
                            value="<c:out value="${status.value}"/>" />
                  </spring:bind>
              </td>                 
              
            </tr>
          </c:forEach>
        </tbody>
      
      </table>
      </form>
    </div>
    
  </tmpl:put>
</tmpl:insert>