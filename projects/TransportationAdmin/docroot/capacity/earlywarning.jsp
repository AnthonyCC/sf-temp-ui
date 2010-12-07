<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.*' %>
<%@ page import= 'java.util.*' %>
<%@ page import= 'com.freshdirect.transadmin.web.model.*' %>

<%  
   pageContext.setAttribute("HAS_ADDBUTTON", "false");
   pageContext.setAttribute("HAS_DELETEBUTTON", "false");
   pageContext.setAttribute("IS_USERADMIN", ""+com.freshdirect.transadmin.security.SecurityManager.isUserAdmin(request));
   String dateRangeVal = request.getParameter("rDate") != null ? request.getParameter("rDate") : "";
   if(dateRangeVal == null || dateRangeVal.length() == 0) dateRangeVal = TransStringUtil.getNextDate();
 %>
  
  <link rel="stylesheet" href="css/transportation.css" type="text/css" />		
<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Early Warning View</tmpl:put>
	<tmpl:put name='yui-lib'>
		<%@ include file='/common/i_yui.jspf'%>
	</tmpl:put>	
	<tmpl:put name='yui-skin'>yui-skin-sam</tmpl:put>
	
  <tmpl:put name='content' direct='true'>
    <br/> 
    <div class="contentroot">               
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <c:if test="${not empty messages}">
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
          </tr>
          </c:if>         
          <tr>
            <td>

              <table border = "0">
                <tr>
                <td> 
                    <span style="font-size: 18px;font-weight: bold;">Early Warning</span>
                </td>                
                  <td> 
                    <span><input maxlength="10" size="10" name="rDate" id="rDate" value='<c:out value="${rDate}"/>' /></span>
                     <span><a href="#" id="trigger_rptDate" style="font-size: 9px;">
                        <img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Select Date" title="Select Date">
                    </a></span>
                     <script language="javascript">                 
                      Calendar.setup(
                      {
                        showsTime : false,
                        electric : false,
                        inputField : "rDate",
                        ifFormat : "%m/%d/%Y",
                        singleClick: true,
                        button : "trigger_rptDate" 
                       }
                      );
                      
                                   
                     var jsonrpcClient = new JSONRpcClient("capacityprovider.ax");

                     var currentUpdateSource;
                      
                    function doCompositeLink(compId1,compId2,compId3,compId4, url) {
			          var param1 = document.getElementById(compId1).value;
			          var param2 = document.getElementById(compId2).value;
			          var param3 = document.getElementById(compId3).value;
			          var param4 = document.getElementById(compId4).checked;
			          			          
			          if(param1.length == 0) { // || param2.length == 0) {
			          		alert("Please select the required filter param (Date)");
			          } else {
			          	location.href = url+"?"+compId1+"="+ param1+"&"+compId2+"="+param2+"&"+compId3+"="+param3+"&"+compId4+"="+param4;
			          }
        			} 
        			
        			function addTSRowHandlers(tableId, rowClassName) {
					    var previousClass = null;
					    var table = document.getElementById(tableId);
					    
					    if(table != null) {
						    var rows = table.tBodies[0].getElementsByTagName("tr");	 	       
						    for (i = 0; i < rows.length; i++) {	    	
						        var cells = rows[i].getElementsByTagName("td");
						        
						        for (j = 0; j < cells.length-2; j++) {
						        	
						            cells[j].onmouseover = function () {
						            	previousClass = this.parentNode.className;
						            	this.parentNode.className = this.parentNode.className + " " + rowClassName ;
						            };
						        
						            cells[j].onmouseout = function () {
						              	this.parentNode.className = previousClass;
						            };
						        
						            cells[j].onclick = function () {  										      		
								      		showTimeslot('panel-'+this.parentNode.rowIndex
								      						, findPosX(this.parentNode)
								      						, findPosY(this.parentNode));									      		
								    };
						        }
						    }
						}
				}
                    
                 function showTimeslot(rowDiv, rowX, rowY) {
                 	var tsPanel = new YAHOO.widget.Panel(rowDiv, {       
					                          width: "540px",					                           
					                          close: true, 
					                          draggable: true, 
					                          zindex:4,
					                          modal: true,
					                          visible: false,
					                          xy: [rowX, rowY],
					                          effect:{effect:YAHOO.widget.ContainerEffect.SLIDE,duration:0.25}});
					tsPanel.render(document.body);
          			tsPanel.show();					                          
                 }  

                 function updateTimeslot(sourceObj, referenceId, type) {
                	 currentUpdateSource = sourceObj;
                 	 var result = jsonrpcClient.AsyncCapacityProvider.updateTimeslotForStatus(updateTimeslotCallback
																		, referenceId, (sourceObj.value == 'O'), type
																		, document.getElementById('rDate').value
																		, document.getElementById('cutOff').value);			                          
                 }  

                 function updateTimeslotCallback(result, exception) {
               	  
                     if(exception) {               
                         alert('Unable to connect to host system. Please contact system administrator!'+exception);               
                         return;
                     }
                     if(result == 0) {
                    	 alert('Update Failed');
                     } else {                    	 
                     	if(currentUpdateSource.value == 'C') {
                     		currentUpdateSource.value = 'O';
                     		currentUpdateSource.className = "dynamic_enabled";
                     	} else {
                     		currentUpdateSource.value = 'C';
                     		currentUpdateSource.className = "dynamic_disabled";
                     	}
                     	window.location.reload();
                     }                           
                 }

                 function updateDynamicTimeslot(sourceObj, referenceId, type) {
                	 var accesscode = prompt("You are about to enable/disable dynamic routing. Do you want to continue?","<Please enter access code>");
                	 if (accesscode != null && accesscode != "")  {
                		 currentUpdateSource = sourceObj;
	                 	 var result = jsonrpcClient.AsyncCapacityProvider.updateTimeslotForDynamicStatus(updateDynamicTimeslotCallback
																			, referenceId, (sourceObj.value == 'S'), type
																			, document.getElementById('rDate').value
																			, document.getElementById('cutOff').value
																			, accesscode);	
                	 }	                          
                 }  

                 function updateDynamicTimeslotCallback(result, exception) {
               	  
                     if(exception) {               
                         alert('Unable to connect to host system. Please contact system administrator!'+exception);               
                         return;
                     }
                     if(result == 0) {
                    	 alert('Update Failed. Please check your access code.');
                     } else {                    	 
                     	if(currentUpdateSource.value == 'D') {
                     		currentUpdateSource.value = 'S';
                     		currentUpdateSource.className = "dynamic_disabled";
                     	} else {
                     		currentUpdateSource.value = 'D';
                     		currentUpdateSource.className = "dynamic_enabled";
                     	}
                     	window.location.reload();
                     }                           
                 }
                          
                  </script>
                </td>
                <td>
                  &nbsp;<form:errors path="rDate" />
                </td>
                <td> 
                  <select id="cutOff" name="cutOff">
                      <option value="">--All Cut Off</option> 
                      <c:forEach var="cutoff" items="${cutoffs}">                             
                          <c:choose>
                            <c:when test="${cutOff == cutoff.cutOffId}" > 
                              <option selected value="<c:out value="${cutoff.cutOffId}"/>"><c:out value="${cutoff.name}"/></option>
                            </c:when>
                            <c:otherwise> 
                              <option value="<c:out value="${cutoff.cutOffId}"/>"><c:out value="${cutoff.name}"/></option>
                            </c:otherwise> 
                          </c:choose>      
                        </c:forEach>   
                   </select>
                
                </td>
                
                <td> 
                  <select id="rType" name="rType">                      
                      <option <c:choose> <c:when test="${rType == 'T'}" >selected </c:when> </c:choose> value="T">Time</option>  
                      <option <c:choose> <c:when test="${rType == 'O'}" >selected </c:when> </c:choose> value="O">Order</option>
                   </select>
                
                </td>
                <td><span style="font-size: 12px">&nbsp;Auto Refresh :</span><input type="checkbox" name="autorefresh" id="autorefresh" <%= ("on".equalsIgnoreCase(request.getParameter("autorefresh")) ? "checked=\"true\"" : "false") %>  /></td>
                   <td>
                     <input type = "button" value="&nbsp;View&nbsp;" onclick="javascript:doCompositeLink('rDate','cutOff','rType','autorefresh','earlywarning.do')" />
                  </td>  
                  
              </tr>
              </table>        
              
            </td>
          </tr>               
        </table>    
           
      </div>
    <table>
    <tr>    
	  <td style="vertical-align: top;" width="68%">
	      <ec:table items="earlywarnings"   action="${pageContext.request.contextPath}/earlywarning.do"
	            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="&nbsp;"
	            width="100%" filterable="false" showPagination="false" rowsDisplayed="1000" view="fd" >
	            
	            <ec:exportPdf fileName="earlywarnings.pdf" tooltip="Export PDF" 
	                      headerTitle="Early Warning" />
	            <ec:exportXls fileName="earlywarnings.xls" tooltip="Export PDF" />
	            <ec:exportCsv fileName="earlywarnings.csv" tooltip="Export CSV" delimiter="|"/>
	                
	            <ec:row interceptor="earlywarningmarker">                
	              <ec:column property="name"  title="Name"/> 
	              <ec:column property="code" title="Zone" />             
	              <ec:column property="totalCapacity" title="Planned" />
	              <ec:column property="confirmedCapacity" title="Confirmed" />
	              <ec:column property="percentageConfirmed" title="% Confirmed" />
	              <ec:column property="allocatedCapacity" title="Allocated" />
				  <ec:column property="percentageAllocated" title="% Allocated" />
				  <ec:column title=" " width="5px" 
									filterable="false" sortable="false" cell="capacityCloseButtonCell" property="referenceId" />
				  <ec:column title=" " width="5px" 
									filterable="false" sortable="false" cell="capacityDynamicButtonCell" property="dynamicActive" />
				  <ec:column title=" " width="5px" 
									filterable="false" sortable="false" cell="discount" property="isDiscounted" />					
																			  	                           
	            </ec:row>
	          </ec:table>
	    </td>
	    <td>&nbsp;&nbsp;&nbsp;</td>
	    <td style="vertical-align: top;" >
	    
	      <ec:table items="earlywarnings_region"   action="${pageContext.request.contextPath}/earlywarning.do"
	            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="&nbsp;"
	            width="100%"  filterable="false"  showPagination="false" showExports="false" showStatusBar="false" sortable="false" 
	            tableId="region_earlywarning" rowsDisplayed="1000" view="fd" >
	            
	            
	            <ec:row interceptor="earlywarningmarker">                
	              <ec:column property="name"  title="Name"/>                           
	              <ec:column property="totalCapacity" title="Planned" />
	              <ec:column property="confirmedCapacity" title="Confirmed" />
	              <ec:column property="percentageConfirmed" title="% Confirmed" />
	              <ec:column property="allocatedCapacity" title="Allocated" />
	              <ec:column property="percentageAllocated" title="% Allocated" />
				  <ec:column title=" " width="5px" 
									filterable="false" sortable="false" cell="capacityCloseButtonCell"  property="referenceId" />
				  <ec:column title=" " width="5px" 
									filterable="false" sortable="false" cell="capacityDynamicButtonCell"  property="dynamicActive" />
				  <ec:column title=" " width="5px" 
									filterable="false" sortable="false" cell="discount" property="isDiscounted" />															  	                           
	            </ec:row>
	          </ec:table>
	    
	  	</td> 
	    </tr> 
    </table>
	    <div id="timeslot_container" style="display:none;">
	    <%
	    	List gridData = (List)request.getAttribute("earlywarnings");
	    	int _rowIndex = 2;	    	
	    	if(gridData != null) {
	    		
	    		Iterator<EarlyWarningCommand> _itr = gridData.iterator();
	    		EarlyWarningCommand _command = null;
	    		while(_itr.hasNext()) {
	    			_command = _itr.next();
	    			  %>
			    	<div id="panel-<%=_rowIndex %>">
			    	<div class="hd"><img src="images/icons/edit_ON.gif" width="16" height="16" border="0" align="absmiddle" />&nbsp;&nbsp;&nbsp;<%="Breakdown-"+_command.getName() %></div>
			    	<div class="bd">
			    	<div class="eXtremeTable" >
			    	<table id="timeslot_table-<%=_rowIndex %>"  border="0"  cellspacing="0"  cellpadding="0"  class="tableRegion"  width="98%" >
				    	<thead>
						<tr>
							<td class="tableHeader" >Name</td>
							<td class="tableHeader" >Planned</td>
							<td class="tableHeader" >Confirmed</td>
							<td class="tableHeader" >% Confirmed</td>
					
							<td class="tableHeader" >Allocated</td>
							<td class="tableHeader" >% Allocated</td>
							<td class="tableHeader" >#Trucks</td>
							<td class="tableHeader" >&nbsp;</td>
							<td class="tableHeader" >&nbsp;</td>
							<td class="tableHeader" >&nbsp;</td>							
						</tr>
						</thead>
						<tbody class="tableBody" >
	    			<% 
	    			
	    			List<EarlyWarningCommand> timeslotDetails =  _command.getTimeslotDetails();
	    			String rType = request.getParameter("rType");
	    			
	    			if(timeslotDetails != null) {
	    				Iterator<EarlyWarningCommand> _itrTimeslot = timeslotDetails.iterator();
	    				EarlyWarningCommand _commandTS = null;
	    				while(_itrTimeslot.hasNext()) {
	    					_commandTS = _itrTimeslot.next();%>
	    					<tr>
								<td><%=_commandTS.getName() %>&nbsp;</td>						
								<td><%=_commandTS.getTotalCapacity() %></td>
								<td><%=_commandTS.getConfirmedCapacity() %></td>
								<td><%=_commandTS.getPercentageConfirmed() %></td>
								<td><%=_commandTS.getAllocatedCapacity()%></td>
								<td><%=_commandTS.getPercentageAllocated()%></td>								
								<td><%= rType != null && rType.equalsIgnoreCase("O") ? "n/a" : ""+_commandTS.getNoOfResources() %></td>
								<td><input type="button" 
										class="<%= _commandTS.getClosedCount() > 0 ? "timeslot_closed" : "timeslot_open" %>" 
												value="<%= (_commandTS.getClosedCount() > 0 ? "C" : "O") %>" 
														onclick="updateTimeslot(this, '<%= _commandTS.getReferenceId() %>', '0')"
														<%= (com.freshdirect.transadmin.security.SecurityManager.isUserAdmin(request) ?  " " : " disabled=\"disabled\"") %> /></td>
								<td><input type="button" 
										class="<%= _commandTS.getDynamicActiveCount() > 0 ? "dynamic_enabled" : "dynamic_disabled" %>" 
												value="<%= (_commandTS.getDynamicActiveCount() > 0 ? "D" : "S") %>" 
														onclick="updateDynamicTimeslot(this, '<%= _commandTS.getReferenceId() %>', '0')"
														<%= (com.freshdirect.transadmin.security.SecurityManager.isUserAdmin(request) ?  " " : " disabled=\"disabled\"") %> /></td>
														
								<td><%= _commandTS.isDiscounted() ? "<img src=\"images/dollar.gif\" style=\"border:0\"/>" : "&nbsp;" %></td>																												
							</tr>
	    					
	    			   <%}
	    			} %>
	    			</tbody>
	    	</table>
	    	</div>
	    	</div>
	    	</div>
	    	<% _rowIndex++;}	    		
	    	} %>
	    	
	        </div>
	        <script>
		      addTSRowHandlers('ec_table', 'rowMouseOver');
		      <% if(request.getParameter("cutOff") != null && request.getParameter("rType") != null && "true".equalsIgnoreCase(request.getParameter("autorefresh"))) { %>
		      		doRefresh(<%= TransportationAdminProperties.getCapacityRefreshTime() %>);
		      <% } %>
		    </script>
	     </div>     
	  
  </tmpl:put>
  
</tmpl:insert>
