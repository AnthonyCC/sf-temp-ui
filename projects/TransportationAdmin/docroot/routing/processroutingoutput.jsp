<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'> Routing : Routing Out </tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/> 
    <div align="center">
      <form method="post" enctype="multipart/form-data">        
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle">Process Output from Routing System</td>
          </tr>
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
          </tr>
          
          <tr>
            <td class="screencontent">
              <table class="forms1">          
                <tr>
                  <td>Routing Ouput</td>
                  <td>                  
                    <input type="file" size="50" name="file"/>
                    <spring:bind path="command.file">
                      <c:forEach items="${status.errorMessages}" var="error">
                                      &nbsp;<span id="file"><c:out value="${error}"/></span>
                                    </c:forEach> 
                            </spring:bind>
                </td>
                <td>
                  &nbsp;
                </td>
               </tr>
              
              <tr>
               <td>Cut Off</td>
                        <td>
                          <spring:bind path="command.cutOff">
                            <select name="<c:out value="${status.expression}"/>">
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
                  <spring:bind path="command.outputFile1">
                    <c:if test="${!empty status.value}">
                                      <a href='<c:out value="${status.value}"/>' >Order Info</a>                                                                
                                    </c:if> 
                                </spring:bind>
                              </td>                               
                              <td>&nbsp;</td>
                              <td> 
                  <spring:bind path="command.outputFile2">
                    <c:if test="${!empty status.value}">
                                      <a href='<c:out value="${status.value}"/>' >Truck Info</a> 
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
  </tmpl:put>
</tmpl:insert>