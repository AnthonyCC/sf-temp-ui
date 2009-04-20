<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'> Routing : Merge RS Files</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/> 
    <div align="center">
      <form method="post" enctype="multipart/form-data">        
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle"> Merge RouteSmart Files</td>
          </tr>
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
          </tr>
          
          <tr>
            <td class="screencontent">
              <table class="forms1">          
                <tr>
                  <td>Route Smart Truck File1</td>
                  <td>                  
                    <input type="file" size="50" name="truckFile1"/>
                    <spring:bind path="command.truckFile1">
                      <c:forEach items="${status.errorMessages}" var="error">
                                      &nbsp;<span id="truckFile1"><c:out value="${error}"/></span>
                                    </c:forEach> 
                            </spring:bind>
                </td>
                <td>&nbsp;</td>
               </tr>
               
               <tr>
                  <td>Route Smart Order File1</td>
                  <td>                  
                    <input type="file" size="50" name="orderFile1"/>
                    <spring:bind path="command.orderFile1">
                      <c:forEach items="${status.errorMessages}" var="error">
                                      &nbsp;<span id="orderFile1"><c:out value="${error}"/></span>
                                    </c:forEach> 
                            </spring:bind>
                </td>
                <td>
                  &nbsp;
                </td>
               </tr>
               
               <tr>
                  <td>Route Smart Truck File2</td>
                  <td>                  
                    <input type="file" size="50" name="truckFile2"/>
                    <spring:bind path="command.truckFile2">
                      <c:forEach items="${status.errorMessages}" var="error">
                                      &nbsp;<span id="truckFile2"><c:out value="${error}"/></span>
                                    </c:forEach> 
                            </spring:bind>
                </td>
                <td>&nbsp;</td>
               </tr>
               
               <tr>
                  <td>Route Smart Order File2</td>
                  <td>                  
                    <input type="file" size="50" name="orderFile2"/>
                    <spring:bind path="command.orderFile2">
                      <c:forEach items="${status.errorMessages}" var="error">
                                      &nbsp;<span id="orderFile2"><c:out value="${error}"/></span>
                                    </c:forEach> 
                            </spring:bind>
                </td>
                <td>
                  &nbsp;
                </td>
               </tr>
               
               <tr>
                  <td>Route Smart Truck File3</td>
                  <td>                  
                    <input type="file" size="50" name="truckFile3"/>
                    <spring:bind path="command.truckFile3">
                      <c:forEach items="${status.errorMessages}" var="error">
                                      &nbsp;<span id="truckFile3"><c:out value="${error}"/></span>
                                    </c:forEach> 
                            </spring:bind>
                </td>
                <td>&nbsp;</td>
               </tr>
               
               <tr>
                  <td>Route Smart Order File3</td>
                  <td>                  
                    <input type="file" size="50" name="orderFile3"/>
                    <spring:bind path="command.orderFile3">
                      <c:forEach items="${status.errorMessages}" var="error">
                                      &nbsp;<span id="orderFile3"><c:out value="${error}"/></span>
                                    </c:forEach> 
                            </spring:bind>
                </td>
                <td>
                  &nbsp;
                </td>
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
                            <td>&nbsp; </td> 
                              <td> 
                  <spring:bind path="command.truckOutputFilePath">
                    <c:if test="${!empty status.value}">
                                      <a href='<c:out value="${status.value}"/>' >Truck Info</a> 
                                    </c:if>                                                                
                                </spring:bind>
                              </td>
                   
                            </tr>
                                                        
              <tr>
                  <td colspan="3" align="center">
                   <input type = "submit" value="&nbsp;Merge&nbsp;"  />
                </td>     
              </tr>
              </table>        
              
            </td>
          </tr>               
        </table>      
      </form>
     </div>
    
  </tmpl:put>
</tmpl:insert>