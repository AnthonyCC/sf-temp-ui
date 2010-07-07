

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tmpl:insert template='/common/sitelayout.jsp'>
<tmpl:put name='yui-lib'>
	<%@ include file='/common/i_yui.jspf'%>	
</tmpl:put>	
<% String pageTitle = "Add/Edit Delivery Service Time Scenario"; %>

    <tmpl:put name='title' direct='true'>Routing : ... : <%=pageTitle%></tmpl:put>
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


    var rollingIndex = 0;
    var isEdit=false;
    var currentRow;
    
      function addOrUpdateProfile(){
          if(isEdit){
              
          }else{
              addProfile();        
          }
          
      }    
      
      function modifyRestrictionLink(num,actionType){
           var oldval=document.getElementById('restrictionLinkStr').value; 
           //console.log("oldval :"+oldval);         
           //console.log("num :"+num);         
           //console.log("actionType :"+actionType);         
           var newval;
           if(actionType=='append'){            
               if(num<10){
                 num='0'+num;
               }
               newval=oldval+'$'+num;
               document.getElementById('restrictionLinkStr').value=newval;             
           }else{             
               if(num<10){
                 num='0'+num;
               }
               newval=oldval.replace('$'+num,'');
               document.getElementById('restrictionLinkStr').value=newval;
           }
      
      }
      
      
      
          function validateUserFields(){

        	  var sType     =  document.getElementById('ServiceTimeTypeList').selectedIndex;
        	  var sTimeOver =  document.getElementById('serviceTimeOverride').value;
        	  var sTimeOptr =  document.getElementById('ServiceTimeOperatorList').selectedIndex;
        	  var sTimeAdj  =  document.getElementById('serviceTimeAdjustment').value;
        	  
        	  if(sType==0 && sTimeOver==''&& sTimeOptr==0 && sTimeAdj==''){ 
                  alert("Service Time group cannot be empty. Please choose one from the group!!");   
                  return false;
              } 
        	  if((sType!=0 && sTimeOver!=''&& sTimeOptr==0 && sTimeAdj=='') || (sType!=0 && sTimeOver!=''&& sTimeOptr!=0 && sTimeAdj!='')
                	  ||(sType!=0 && sTimeOver==''&& sTimeOptr==0 && sTimeAdj!='')||(sType==0 && sTimeOver!=''&& sTimeOptr==0 && sTimeAdj!='')
                	  ||(sType==0 && sTimeOver==''&& sTimeOptr!=0 && sTimeAdj=='')||(sType!=0 && sTimeOver==''&& sTimeOptr!=0 && sTimeAdj=='')
                	  ||(sType==0 && sTimeOver!=''&& sTimeOptr!=0 && sTimeAdj=='')||(sType!=0 && sTimeOver!=''&& sTimeOptr==0 && sTimeAdj!='')
                	  ||(sType!=0 && sTimeOver!=''&& sTimeOptr!=0 && sTimeAdj=='')){ 
                  alert("Please choose only one from ServiceTime group!!");   
                  return false;
              }
              return true;
          }
      
      
          function editProfile() {
                              
                      if(!validateUserFields()){ return false; }
                      
                    
  	                  var profileTableFld = document.getElementById('profileListTB');
  						
  					  var allTrElements = document.getElementById(currentRow);
                         
                      allTrElements.getElementsByTagName("td")[0].innerHTML=document.getElementById('ZoneList')[document.getElementById('ZoneList').selectedIndex].text;   
        			  if(document.getElementById('ServiceTimeTypeList')[document.getElementById('ServiceTimeTypeList').selectedIndex].text=='-Please Select')	{
        				  allTrElements.getElementsByTagName("td")[1].innerHTML='';
        			  }else{
            			  allTrElements.getElementsByTagName("td")[1].innerHTML=document.getElementById('ServiceTimeTypeList')[document.getElementById('ServiceTimeTypeList').selectedIndex].text;   													                       
        			  }
                      allTrElements.getElementsByTagName("td")[2].innerHTML=document.getElementById('serviceTimeOverride').value;   
                      if(document.getElementById('ServiceTimeOperatorList')[document.getElementById('ServiceTimeOperatorList').selectedIndex].text=='-Please Select')	{
        				  allTrElements.getElementsByTagName("td")[3].innerHTML='';
        			  }else{
            			  allTrElements.getElementsByTagName("td")[3].innerHTML=document.getElementById('ServiceTimeOperatorList')[document.getElementById('ServiceTimeOperatorList').selectedIndex].text;
            		  } 
                      allTrElements.getElementsByTagName("td")[4].innerHTML=document.getElementById('serviceTimeAdjustment').value;   
                                           
                      document.getElementById(currentRow+'.zoneCode').value=document.getElementById('ZoneList')[document.getElementById('ZoneList').selectedIndex].value;
                      document.getElementById(currentRow+'.sTimeType').value=document.getElementById('ServiceTimeTypeList')[document.getElementById('ServiceTimeTypeList').selectedIndex].value;
					  document.getElementById(currentRow+'.sTimeOverride').value=document.getElementById('serviceTimeOverride').value;
					  document.getElementById(currentRow+'.sTimeOperator').value=document.getElementById('ServiceTimeOperatorList')[document.getElementById('ServiceTimeOperatorList').selectedIndex].value;
                      document.getElementById(currentRow+'.sTimeAdjustment').value=document.getElementById('serviceTimeAdjustment').value;
                                                                    
                      document.getElementById('ZoneList').selectedIndex=0;
                      document.getElementById('ServiceTimeTypeList').selectedIndex=0;                                                                                         
                      document.getElementById('serviceTimeOverride').value='';
                      document.getElementById('ServiceTimeOperatorList').selectedIndex=0;
                      document.getElementById('serviceTimeAdjustment').value='';                         
      
                     isEdit=false;                      
          }    

      	   function addProfile() {
                
        	        if(!validateUserFields()){ return false; }                      
                           
                      	var zonecode = document.getElementById('ZoneList')[document.getElementById('ZoneList').selectedIndex];
  	                  	var sTimeType = document.getElementById('ServiceTimeTypeList')[document.getElementById('ServiceTimeTypeList').selectedIndex];
  	        			var sTimeOverride = document.getElementById('serviceTimeOverride').value;
  	        			var sTimeOptr = document.getElementById('ServiceTimeOperatorList')[document.getElementById('ServiceTimeOperatorList').selectedIndex];
  	        			var sTimeAdjustment = document.getElementById('serviceTimeAdjustment').value;
                              
  						addProfleRow(zonecode,sTimeType,sTimeOverride,sTimeOptr,sTimeAdjustment);	
                                  
                        document.getElementById('ZoneList').selectedIndex=0;                                                                                         
                        document.getElementById('ServiceTimeTypeList').selectedIndex=0;
                        document.getElementById('serviceTimeOverride').value='';
                        document.getElementById('ServiceTimeOperatorList').selectedIndex=0;
                        document.getElementById('serviceTimeAdjustment').value='';          
  											
  		   }
                        
                                     
                        
  					 					  
  					  function addProfleRow(zonecode,sTimeType,sTimeOverride,sTimeOptr,sTimeAdjustment) {
                          
  					  		if( document.createElement && document.childNodes ) {
  								var profileTableFld = document.getElementById('profileListTB').tBodies[0];																
  								if(profileTableFld != null) {									
  										var row = document.createElement('tr');
  										rollingIndex++;
  										var tmpID = 'attributeList['+rollingIndex+']';
  										row.id = tmpID;
  									    var td1 = document.createElement('td');
  									    td1.appendChild(document.createTextNode(zonecode.text));
  									    var td2 = document.createElement('td');
										if(sTimeType.text!=null && sTimeType.text=='-Please Select'){
											sTimeType.text='';	
										}
                                        td2.appendChild(document.createTextNode(sTimeType.text));    
   									    var td3 = document.createElement('td');
   									    td3.appendChild (document.createTextNode(sTimeOverride));
  									    var td4 = document.createElement('td');
  									    if(sTimeOptr.text!=null && sTimeOptr.text=='-Please Select'){
  									    	sTimeOptr.text='';	
										}
  	                                    td4.appendChild(document.createTextNode(sTimeOptr.text)); 
  									    var td5 = document.createElement('td');
  									    td5.appendChild (document.createTextNode(sTimeAdjustment));
  									    var td6 = document.createElement('td');
									    var tdDelete = document.createElement('a');
									    tdDelete.innerHTML = 'Delete';
									    tdDelete.href = "javascript:deleteProfile('"+tmpID+"')";
                                    	var tdEdit = document.createElement('a');     
                                   		tdEdit.innerHTML = 'Edit';
									    tdEdit.href = "javascript:copyProfile('"+tmpID+"')";     
                                        
                                   		var tdSpacer = document.createElement('span');
									    tdSpacer.innerHTML = ' / ';     
                                        
									    td6.appendChild (tdDelete);
                                        td6.appendChild (tdSpacer);
                                        td6.appendChild (tdEdit);                                        
	  									    row.appendChild(td1);
	  									    row.appendChild(td2);
	  									    row.appendChild(td3);
	  									    row.appendChild(td4);
	                                        row.appendChild(td5);
	                                        row.appendChild(td6);
  									    profileTableFld.appendChild(row);
  									    createHiddenInput(rollingIndex,zonecode,sTimeType,sTimeOverride,sTimeOptr,sTimeAdjustment);
                                     var size = document.getElementById('restrictionListSize'); 
                                     size.value=""+rollingIndex;  
                                     modifyRestrictionLink(rollingIndex,'append');                                   
  								}								
  							}
  					 }
                       
                    
  					function createHiddenInput(indexVal,zonecode,sTimeType,sTimeOverride,sTimeOptr,sTimeAdjustment) {
  						
  						var tmpId = 'attributeList['+indexVal+']'+'.';
  						var newElementValue1 = document.createElement("input");
  						newElementValue1.setAttribute("type", "hidden");
                      	newElementValue1.setAttribute("name", tmpId+'zoneCode');
  						newElementValue1.setAttribute("id", tmpId+'zoneCode');
  						newElementValue1.setAttribute("value", zonecode.value);
  						
                    	var newElementValue2 = document.createElement("input");
  						newElementValue2.setAttribute("type", "hidden");
  						newElementValue2.setAttribute("name", tmpId+'sTimeType');
  						newElementValue2.setAttribute("id", tmpId+'sTimeType');
  						newElementValue2.setAttribute("value", sTimeType.value);

  						var newElementValue3 = document.createElement("input");
  						newElementValue3.setAttribute("type", "hidden");
  						newElementValue3.setAttribute("name", tmpId+'sTimeOverride');
  						newElementValue3.setAttribute("id", tmpId+'sTimeOverride');
  						newElementValue3.setAttribute("value", sTimeOverride);

  						var newElementValue4 = document.createElement("input");
  						newElementValue4.setAttribute("type", "hidden");
  						newElementValue4.setAttribute("name", tmpId+'sTimeOperator');
  						newElementValue4.setAttribute("id", tmpId+'sTimeOperator');
  						newElementValue4.setAttribute("value", sTimeOptr.value);
  						
                       	var newElementValue5 = document.createElement("input");
  						newElementValue5.setAttribute("type", "hidden");
  						newElementValue5.setAttribute("name", tmpId+'sTimeAdjustment');
  						newElementValue5.setAttribute("id", tmpId+'sTimeAdjustment');
  						newElementValue5.setAttribute("value", sTimeAdjustment);

     					document.forms['serviceTimeScenarioForm'].appendChild(newElementValue1);
  						document.forms['serviceTimeScenarioForm'].appendChild(newElementValue2);						
                        document.forms['serviceTimeScenarioForm'].appendChild(newElementValue3);						
                        document.forms['serviceTimeScenarioForm'].appendChild(newElementValue4);
                        document.forms['serviceTimeScenarioForm'].appendChild(newElementValue5);  
                       
  												
  					}

  					function deleteProfile(theCell) {
  						var profileTableFld = document.getElementById('profileListTB');
  						var profileTableRow = document.getElementById(theCell);
  						
  						if( document.createElement && document.childNodes 
  								&& profileTableFld != null 
  								&& profileTableRow != null) {
  														
  							var rowIndex = profileTableRow.rowIndex;
  							var hiddenElement1 = document.getElementById(profileTableRow.id+'.zoneCode');
                            var hiddenElement2 = document.getElementById(profileTableRow.id+'.sTimeType');						    
  						    var hiddenElement3 = document.getElementById(profileTableRow.id+'.sTimeOverride');						    
                            var hiddenElement4 = document.getElementById(profileTableRow.id+'.sTimeOperator');
                            var hiddenElement5 = document.getElementById(profileTableRow.id+'.sTimeAdjustment');							    
  						    var parentElementNode = hiddenElement1.parentNode;
  						   						   				    									    				
  						      parentElementNode.removeChild(hiddenElement1);
  						      parentElementNode.removeChild(hiddenElement2);							
                              parentElementNode.removeChild(hiddenElement3);							
                              parentElementNode.removeChild(hiddenElement4);
                              parentElementNode.removeChild(hiddenElement5);							
  							profileTableFld.deleteRow(rowIndex);
                            modifyRestrictionLink(rowIndex,'delete');                                   							
                            rollingIndex=rollingIndex-1; 
                            var size = document.getElementById('restrictionListSize'); 
                            size.value=""+rollingIndex;      
                          
  						}
  					}
                      
                      
                    function copyProfile(theCell) {                                    											                     
                    
                           currentRow=theCell;
                           
                           document.getElementById('ZoneList').value=document.getElementById(theCell+'.zoneCode').value;
                           document.getElementById('ServiceTimeTypeList').value=document.getElementById(theCell+'.sTimeType').value;
                           document.getElementById('serviceTimeOverride').value=document.getElementById(theCell+'.sTimeOverride').value;
                           document.getElementById('ServiceTimeOperatorList').value=document.getElementById(theCell+'.sTimeOperator').value;
                           document.getElementById('serviceTimeAdjustment').value=document.getElementById(theCell+'.sTimeAdjustment').value;
                                                      
                           isEdit=true;                        
  					}  
  					    
  </script>
  <tmpl:put name='content' direct='true'>
    <br/> 
    <div align="center">
      <form:form commandName = "serviceTimeScenarioForm" method="post">
       <input type="hidden" id="restrictionListSize" name="restrictionListSize" class="input" value="0" />
       <input type="hidden" id="restrictionLinkStr" name="restrictionLinkStr" class="input" value="" />
      <form:hidden path="isNew"/>   
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle">
              			<span>
              				<a href="javascript:showForm()">
                        		<img src="./images/info.gif" border="0" alt="Info" title="Info" />
                        	</a>
                        </span>
                        
            <%=pageTitle%>
            </td>
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
               
               <tr>
                  <td>Late Delivery Factor</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="lateDeliveryFactor" />
                </td>
                <td>
                  &nbsp;<form:errors path="lateDeliveryFactor" />
                </td>
               </tr>
               
              <tr><td colspan="3">&nbsp;</td></tr>
              <tr>
              	<td colspan="3">
		             	<table cellpadding="1" cellspacing="0">
		            	  
			              <tr>
			                    <td colspan="3"><b>Scenario To Zone Details:</b> <br/><br/> </td>
			              </tr>            
			              <tr>
			                    <th align="center">Zone</th>
			                    <th align="center" width="106">ST Type</th>
			                    <th align="center" width="105">ST Override</th>
			                    <th align="center" width="78">ST Optr</th>
			                    <th align="center" width="129">ST Adjustment</th>
			                    <th rowspan="2" style="vertical-align: bottom;">
			                        <input class="submit" type="button" value="Add/Update Scenario" onclick="javascript:(isEdit)?editProfile():addProfile();"/>                                            
			                    </th>
			                   
			               </tr>
		                   <tr>
			                   <td>                   
			                      <select id="ZoneList" style="width: 50;" name="ZoneList">  
			                    	<option value="All">All</option>
			                   		<c:forEach var="zone" items="${zones}">       
					              		<OPTION value="<c:out value="${zone.zoneCode}"/>"><c:out value="${zone.zoneCode}"/></OPTION>
						        	</c:forEach>                                      
						          </select>
			                   </td>                               
			                   <td>                 
				                   <select name="ServiceTimeTypeList" id="ServiceTimeTypeList" style="width: 90%;">                     
				                  	 <option value="">-Please Select</option>
				                  	 <c:forEach var="serviceTimeType" items="${servicetimetypes}">       
						             		<OPTION value="<c:out value="${serviceTimeType.code}"/>"><c:out value="${serviceTimeType.name}"/></OPTION>
							         </c:forEach>                                      
							       </select>
				               </td>                                      
			                   <td><input type="text" id="serviceTimeOverride" name="serviceTimeOverride" value="" style="width: 95px;" ></td>
			                   <td>                 
				                   <select name="ServiceTimeOperatorList" id="ServiceTimeOperatorList" style="width: 100%;">                     
				                  	 <option value="">-Please Select</option>
				                  	 <c:forEach var="serviceTimeOperator" items="${serviceTimeOperators}">       
						             		<OPTION value="<c:out value="${serviceTimeOperator.name}"/>"><c:out value="${serviceTimeOperator.description}"/></OPTION>
							         </c:forEach>                                      
							       </select>
				               </td>    
			                   <td><input type="text" id="serviceTimeAdjustment" name="serviceTimeAdjustment" value="" style="width: 88px;" ></td>
		                </tr>  
		                
		               </table>  <br /><br />   
		
		                <table id="profileListTB" border="1" cellpadding="1" cellspacing="0" >
							<thead>
			                    <tr>
				                    <th>Zone</th>
				                    <th>ST Type</th>
				                    <th>ST Override</th>
				                    <th>ST Operator</th>
				                    <th>ST Adjustment</th>
				                    <th style="border: none"> </th>
			                    </tr>
		                    </thead>
							<tbody>		
		                     <% 
		                     	int intRowIndex = 0;
		                  	 	StringBuffer strProfileHidBuf = new StringBuffer();
		                   	 %>
		                    <c:forEach var="sZone" items="${serviceTimeScenarioForm.scenarioZones}">
		                   		 <%                      
		                          intRowIndex++;
		                         %>
		                            <input type="hidden" name="attributeList[<%=intRowIndex%>].zoneCode" id="attributeList[<%=intRowIndex%>].zoneCode" value="<c:out value="${sZone.scenarioZonesId.zoneCode}"/>" />
		                            <input type="hidden" name="attributeList[<%=intRowIndex%>].sTimeType" id="attributeList[<%=intRowIndex%>].sTimeType" value="<c:out value="${sZone.serviceTimeType}"/>" />
                           			<input type="hidden" name="attributeList[<%=intRowIndex%>].sTimeOverride" id="attributeList[<%=intRowIndex%>].sTimeOverride" value="<c:out value="${sZone.serviceTimeOverride}"/>" />
                            		<input type="hidden" name="attributeList[<%=intRowIndex%>].sTimeOperator" id="attributeList[<%=intRowIndex%>].sTimeOperator" value="<c:out value="${sZone.serviceTimeOperator}"/>" />
                          			<input type="hidden" name="attributeList[<%=intRowIndex%>].sTimeAdjustment" id="attributeList[<%=intRowIndex%>].sTimeAdjustment" value="<c:out value="${sZone.serviceTimeAdjustment}"/>" />
                           			
		                            
		                            <tr id='attributeList[<%=intRowIndex%>]'> 
						        		<td><c:out value="${sZone.scenarioZonesId.zoneCode}"/></td> 
		                                <td><c:out value="${sZone.serviceTimeType}"/></td> 
		                                <td><c:out value="${sZone.serviceTimeOverride}"/></td>
		                                <td><c:out value="${sZone.serviceTimeOperator.description}"/></td> 
		                                <td><c:out value="${sZone.serviceTimeAdjustment}"/></td> 
		                                <td><a href="javascript:deleteProfile('attributeList[<%=intRowIndex %>]')">Delete</a>&nbsp;&nbsp;/&nbsp;&nbsp;<a href="javascript:copyProfile('attributeList[<%=intRowIndex %>]')">Edit</a></td>
		                            </tr> 
		                            <script>
		                            if(<%=intRowIndex%><10)
		                               document.getElementById('restrictionLinkStr').value=document.getElementById('restrictionLinkStr').value+'$'+'0'+<%=intRowIndex %>;    
		                            else
		                               document.getElementById('restrictionLinkStr').value=document.getElementById('restrictionLinkStr').value+'$'+<%=intRowIndex %>;    
		                         </script>
		                             
		                    </c:forEach>
		                    	<script>
		                          rollingIndex=<%=intRowIndex%>;
		                           document.getElementById('restrictionListSize').value=rollingIndex; 
		                        </script>		
						     </tbody> 				
						</table>
						</td>
   					</tr>
              <tr>
                  <td colspan="3" align="center">
                   	<input type = "submit" value="&nbsp;Save&nbsp;"  />
                  </td>     
              </tr>
              <tr>
                  <td colspan="3" align="center">
                  		<table cellpadding="0" cellspacing="0" border="1" width="50%">
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
                  </td>
                  </tr>
              </table>        
              
            </td>
          </tr>               
        </table>
      	<br/>				
      </form:form>
         
     </div>
      
      <%@ include file='i_scenariodaymapping.jspf'%>
     <script>loadBalanceEvent();</script>
  </tmpl:put>
</tmpl:insert>