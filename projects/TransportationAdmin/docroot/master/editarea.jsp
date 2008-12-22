<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Add/Edit Area</tmpl:put>
  <script>
    function loadBalanceEvent() {
      
      var loadBalanceFld = document.getElementById('needsLoadBalance1');
      if(loadBalanceFld.checked) {        
        document.getElementById('balanceBy').disabled = false;
        document.getElementById('loadBalanceFactor').disabled = false;
      } else {
        document.getElementById('balanceBy').disabled = true;
        document.getElementById('loadBalanceFactor').disabled = true;
        //document.getElementById('balanceBy').value = 'null';
        //document.getElementById('loadBalanceFactor').value = '';
      }
    }
  </script>
  <tmpl:put name='content' direct='true'>
    <br/> 
    <div align="center">
      <form:form commandName = "areaForm" method="post">
      <form:hidden path="isNew"/>     
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle">Add/Edit Area</td>
          </tr>
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
          </tr>
          <c:if test="${!empty areaForm.code && empty areaForm.isNew}">
              <c:set var="hasPK" value="true"/>
          </c:if>
          <tr>
            <td class="screencontent">
              <table class="forms1">  
              <tr>
                  <td>Area Code</td>
                  <td>                  
                    <form:input readonly="${hasPK}" maxlength="50" size="30" path="code" />                     
                </td>
                <td>
                  &nbsp;<form:errors path="code" />
                </td>
               </tr>        
                <tr>
                  <td>Area Name</td>
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
                  <td>Prefix</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="prefix" />
                </td>
                <td>
                  &nbsp;<form:errors path="prefix" />
                </td>
               </tr>
               
               <tr>
                  <td>Delivery Model</td>
                  <td> 
                  <form:select path="deliveryModel">
                        <form:option value="null" label="--Please Select Delivery Model"/>
                    <form:options items="${deliverymodels}" itemLabel="name" itemValue="code" />
                   </form:select>
                </td>
                <td>
                  &nbsp;<form:errors path="deliveryModel" />
                </td>
               </tr>
               
               <tr>
                  <td>Route w/ UPS</td>
                  <td> 
                  <form:checkbox path="active" value="X"/>
                </td>
                <td>
                  &nbsp;<form:errors path="active" />
                </td>
               </tr>
               <tr>
                  <td>Load Balance</td>
                  <td> 
                  <form:checkbox path="needsLoadBalance" value="X" onclick="loadBalanceEvent()"/>
                </td>
                <td>
                  &nbsp;<form:errors path="needsLoadBalance" />
                </td>
               </tr>
               
                <tr>
                  <td>Balance By</td>
                  <td> 
                  <form:select path="balanceBy">
                        <form:option value="null" label="--Please Select Balance By"/>
                    <form:options items="${balancebys}" itemLabel="description" itemValue="name" />
                   </form:select>
                </td>
                <td>
                  &nbsp;<form:errors path="balanceBy" />
                </td>
               </tr>
               
               <tr>
                  <td>Balance By Factor</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="loadBalanceFactor" />
                </td>
                <td>
                  &nbsp;<form:errors path="loadBalanceFactor" />
                </td>
               </tr>  
               
              <tr><td colspan="3">&nbsp;</td></tr>
              <tr>
                  <td colspan="3" align="center">
                   <input type = "submit" value="&nbsp;Save&nbsp;"  />
                </td>     
              </tr>
              </table>        
              
            </td>
          </tr>               
        </table>
      
      </form:form>
     </div>
     <script>loadBalanceEvent();</script>
  </tmpl:put>
</tmpl:insert>
