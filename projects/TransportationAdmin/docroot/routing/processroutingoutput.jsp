<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<tmpl:insert template='/common/sitelayout.jsp'>

<tmpl:put name='yui-lib'>
	<%@ include file='/common/i_yui.jspf'%>	
</tmpl:put>	
<tmpl:put name='yui-skin'>yui-skin-sam</tmpl:put>

    <tmpl:put name='title' direct='true'> Routing : Routing Out </tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/> 
    <div align="center">
      <form method="post" enctype="multipart/form-data">        
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle">
                  <span><a href="javascript:showForm()">
                        	<img src="./images/info.gif" border="0" alt="Info" title="Info" />
                        </a></span>
                  Process Output from Routing System
             </td>
          </tr>
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
          </tr>
          
          <tr>
            <td class="screencontent">
              <table class="forms1">          
                <tr>
                  <td>Routing Truck Output</td>
                  <td>                  
                    <input type="file" size="50" name="truckRoutingFile"/>
                    <spring:bind path="command.truckRoutingFile">
                      <c:forEach items="${status.errorMessages}" var="error">
                                      &nbsp;<span id="truckRoutingFile"><c:out value="${error}"/></span>
                                    </c:forEach> 
                            </spring:bind>
                </td>
                <td>
                  &nbsp;
                </td>
               </tr>
               
               <tr>
                  <td>Truck Session Description</td>
                  <td>                  
                    <input type="text" size="50" name="truckRoutingSessionDesc"/>
                </td>
                <td>
                  &nbsp;
                </td>
               </tr>
               
               <tr>
                  <td>Routing Depot Output</td>
                  <td>                  
                    <input type="file" size="50" name="depotRoutingFile"/>
                    <spring:bind path="command.depotRoutingFile">
                      <c:forEach items="${status.errorMessages}" var="error">
                                      &nbsp;<span id="depotRoutingFile"><c:out value="${error}"/></span>
                                    </c:forEach> 
                            </spring:bind>
                </td>
                <td>
                  &nbsp;
                </td>
               </tr>
               
               <tr>
                  <td>Depot Session Description</td>
                  <td>                  
                    <input type="text" size="50" name="depotRoutingSessionDesc"/>
                </td>
                <td>
                  &nbsp;
                </td>
               </tr>
               
               <tr>
                  <td>Depot Truck Schedule</td>
                  <td>                  
                    <input type="file" size="50" name="depotTruckScheduleFile"/>
                    <spring:bind path="command.depotTruckScheduleFile">
                      <c:forEach items="${status.errorMessages}" var="error">
                                      &nbsp;<span id="depotTruckScheduleFile"><c:out value="${error}"/></span>
                                    </c:forEach> 
                            </spring:bind>
                </td>
                <td>
                  &nbsp;
                </td>
               </tr>
              
              <tr>
                  <td>Depot Zones</td>
                  <td colspan="2" width="350px">                  
                    <spring:bind path="command.routingDepotZones">
                    <c:if test="${!empty status.value}">
                                      <c:out value="${status.value}"/>                                                             
                                    </c:if> 
                                </spring:bind>
                </td>                
               </tr>    
               
              <tr>
               <td>Cut Off</td>
                        <td>
                          <spring:bind path="command.cutOff">
                            <select id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>">
                              <option value="">--Please Select Cut Off</option> 
                              <c:forEach var="cutOffRow" items="${cutoffs}">                             
                                  <c:choose>
                                    <c:when test="${status.value == cutOffRow.cutOffId}" > 
                                      <option selected value="<c:out value="${cutOffRow.cutOffId}"/>"><c:out value="${cutOffRow.name}"/></option>
                                    </c:when>
                                    <c:otherwise> 
                                      <option value="<c:out value="${cutOffRow.cutOffId}"/>"><c:out value="${cutOffRow.name}"/></option>
                                    </c:otherwise> 
                                  </c:choose>      
                                </c:forEach>   
                          </select>
                          <c:forEach items="${status.errorMessages}" var="error">
                                    &nbsp;<span id="cutOff.errors"><c:out value="${error}"/></span>
                                </c:forEach>
                          </spring:bind>
                         </td>
              <td>&nbsp;</td>
                      </tr>
                      
                      <tr>
               <td>Commit Route Numbers</td>
                        <td>
                          <spring:bind path="command.force">
                            <input type="checkbox" name="<c:out value="${status.expression}"/>" value="X" />                                                    
                          </spring:bind>
                         </td>
              <td>&nbsp;</td>
                      </tr>                                     
                
              <tr><td colspan="3">&nbsp;</td></tr>
              
              <tr>
                <td> 
                  <spring:bind path="command.orderOutputFilePath">
                    <c:if test="${!empty status.value}">
                                      <a href='<c:out value="${status.value}"/>' >Order Info</a>                                                                
                                    </c:if> 
                                </spring:bind>
                              </td>                               
                              
                              <td> 
                  <spring:bind path="command.truckOutputFilePath">
                    <c:if test="${!empty status.value}">
                                      <a href='<c:out value="${status.value}"/>' >Truck Info</a> 
                                    </c:if>                                                                
                                </spring:bind>
                              </td>
                      
                      <td> 
                  <spring:bind path="command.cutoffReportFilePath">
                    <c:if test="${!empty status.value}">
                                      <a href='<c:out value="${status.value}"/>' >CutOff Report</a> 
                                    </c:if>                                                                
                                </spring:bind>
                              </td>
                                                                  
                            </tr>                                                                                                                
              <tr>
                  <td colspan="3" align="center">
                   <input type = "submit" value="&nbsp;Process&nbsp;"  />
                </td>     
              </tr>
              </table>        
              
            </td>
          </tr>               
        </table>      
      </form>
     </div> 
     <%@ include file='i_routingmapping.jspf'%>   
  </tmpl:put>
</tmpl:insert>