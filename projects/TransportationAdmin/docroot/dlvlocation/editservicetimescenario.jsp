<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Add/Edit Delivery Service Time Type</tmpl:put>
	<script>
		function loadBalanceEvent() {
			
			var loadBalanceFld = document.getElementById('needsLoadBalance1');
			if(loadBalanceFld.checked) {				
				document.getElementById('balanceBy').disabled = false;
				document.getElementById('loadBalanceFactor').disabled = false;
			} else {
				document.getElementById('balanceBy').disabled = true;
				document.getElementById('loadBalanceFactor').disabled = true;
				document.getElementById('balanceBy').value = 'null';
				document.getElementById('loadBalanceFactor').value = '';
			}
		}
	</script>
  <tmpl:put name='content' direct='true'>
    <br/> 
    <div align="center">
      <form:form commandName = "serviceTimeScenarioForm" method="post">
      <form:hidden path="isNew"/>   
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle">Add/Edit Delivery Service Time Scenario</td>
          </tr>
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
          </tr>
          <c:if test="${!empty serviceTimeScenarioForm.code  && empty serviceTimeScenarioForm.isNew}">
              <c:set var="hasPK" value="true"/>
          </c:if>
          <tr>
            <td class="screencontent">
              <table class="forms1">          
                <tr>
                  <td>Code</td>
                  <td>                  
                    <form:input readonly="${hasPK}" maxlength="50" size="30" path="code" />
                </td>
                <td>
                  &nbsp;<form:errors path="code" />
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
                  <td>Use As Default</td>
                  <td> 
                  <form:checkbox path="isDefault" value="X"/>
                </td>
                <td>
                  &nbsp;<form:errors path="isDefault" />
                </td>
               </tr>    
               
               <tr>
                  <td>Service Time Factor Formula <b>(Use x, y, z)</b></td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="serviceTimeFactorFormula" />
                </td>
                <td>
                  &nbsp;<form:errors path="serviceTimeFactorFormula" />
                </td>
               </tr>  
               
               <tr>
                  <td>Service Time Formula <b>(Use a, b, m)</b></td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="serviceTimeFormula" />
                </td>
                <td>
                  &nbsp;<form:errors path="serviceTimeFormula" />
                </td>
               </tr>  
               
               <tr>
                  <td>Order Size Formula <b>(Use x, y, z)</b></td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="orderSizeFormula" />
                </td>
                <td>
                  &nbsp;<form:errors path="orderSizeFormula" />
                </td>
               </tr>  
               
               <tr>
                  <td>Carton Count Default</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="defaultCartonCount" />
                </td>
                <td>
                  &nbsp;<form:errors path="defaultCartonCount" />
                </td>
               </tr>
               
               <tr>
                  <td>Case Count Default</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="defaultCaseCount" />
                </td>
                <td>
                  &nbsp;<form:errors path="defaultCaseCount" />
                </td>
               </tr>  
               
               <tr>
                  <td>Freezer Count Default</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="defaultFreezerCount" />
                </td>
                <td>
                  &nbsp;<form:errors path="defaultFreezerCount" />
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
                  <td>Zone Type</td>
                  <td> 
                  <form:select path="zoneType">
                        <form:option value="null" label="--Please Select zone Type"/>
                    <form:options items="${zonetypes}"  itemLabel="name" itemValue="zoneTypeId" />
                   </form:select>
                </td>
                <td>
                  &nbsp;<form:errors path="zoneType" />
                </td>
               </tr>              
               
               <tr>
                  <td>Load Balance</td>
                  <td> 
                  <form:checkbox path="needsLoadBalance" value="X"  onclick="loadBalanceEvent()" />
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
      <table width="25%" cellpadding="0" cellspacing="0" border="1">
                    
                  <tr>
                    <td class="legend">x</td>
                    <td>No of Cartons</td>
                  </tr>
                  <tr>
                    <td class="legend">y</td>
                    <td>No of Cases</td>
                  </tr>
                  <tr>
                    <td class="legend">z</td>
                    <td>No of Freezers</td>
                  </tr>
                  <tr>
                    <td class="legend">a</td>
                    <td>Fixed Service Time</td>
                  </tr>
                  <tr>
                    <td class="legend">b</td>
                    <td>Variable Service Time</td>
                  </tr>
                  <tr>
                    <td class="legend">m</td>
                    <td>Service Time Factor</td>
                  </tr>                 
              </table>
     </div>
     <script>loadBalanceEvent();</script>
  </tmpl:put>
</tmpl:insert>