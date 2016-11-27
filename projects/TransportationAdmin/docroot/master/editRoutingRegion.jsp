<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'> Geography : Routing Region : Add/Edit Routing Region</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/> 
    <div align="center">
      <form:form commandName = "routingRegionForm" method="post">
      <form:hidden path="isNew"/>     
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle">Add/Edit Routing Region</td>
          </tr>
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
          </tr>
          <c:if test="${!empty routingRegionForm.code && empty routingRegionForm.isNew}">
              <c:set var="hasPK" value="true"/>
          </c:if>
          <tr>
            <td class="screencontent">
              <table class="forms1">  
              <tr>
                  <td>Region Code</td>
                  <td>                  
                    <form:input readonly="${hasPK}" maxlength="50" size="30" path="code" />                     
                </td>
                <td>
                  &nbsp;<form:errors path="code" />
                </td>
               </tr>        
                <tr>
                  <td>Region Name</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="name" />
                </td>
                <td>
                  &nbsp;<form:errors path="name" />
                </td>
               </tr>
                             
               <tr>
                  <td>Description</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="description" />
                </td>
                <td>
                  &nbsp;<form:errors path="description" />
                </td>
               </tr>
               
              
               <tr>
                  <td>Is Depot</td>
                  <td> 
                  <form:checkbox path="isDepot" value="X"/>
                </td>
                <td>
                  &nbsp;<form:errors path="isDepot" />
                </td>
               </tr>
             
               
              <tr><td colspan="3">&nbsp;</td></tr>
              <tr>
                  <td colspan="3" align="center">
                   <input type = "button" value="&nbsp;Back&nbsp;" onclick="javascript:doBack('routingRegion');" /> &nbsp;
                   <input type = "submit" value="&nbsp;Save&nbsp;"  />
                </td>     
              </tr>
              </table>        
              
            </td>
          </tr>               
        </table>
      
      </form:form>
     </div>
     <form name="routingRegion" action="routingRegion.do" method="post">  </form>
  </tmpl:put>
</tmpl:insert>
