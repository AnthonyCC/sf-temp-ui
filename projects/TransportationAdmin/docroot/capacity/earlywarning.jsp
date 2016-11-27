<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.*' %>
<%@ page import= 'java.util.*' %>
<%@ page import= 'com.freshdirect.transadmin.web.model.*' %>
<%@ page import= 'com.freshdirect.routing.model.*' %>
<%  
   pageContext.setAttribute("HAS_ADDBUTTON", "false");
   pageContext.setAttribute("HAS_DELETEBUTTON", "false");
   pageContext.setAttribute("IS_USERADMIN_OR_PLANNING", ""+com.freshdirect.transadmin.security.SecurityManager.isUserAdminOrPlanning(request));
   String dateRangeVal = request.getParameter("rDate") != null ? request.getParameter("rDate") : "";
   if(dateRangeVal == null || dateRangeVal.length() == 0) dateRangeVal = TransStringUtil.getNextDate();
   String scenariotitle = "<span style=\"font-size: 11pt;\">Service Time Scenario : ";
   IServiceTimeScenarioModel srvScenario = (IServiceTimeScenarioModel)request.getAttribute("srcscenario");
   if(srvScenario != null) {
	   scenariotitle += srvScenario.getDescription();
   }
   scenariotitle += "</span>";
 %>
  

<tmpl:insert template='/common/sitelayout.jsp'>	

    <tmpl:put name='title' direct='true'>Early Warning View</tmpl:put>
	<tmpl:put name='yui-lib'>
		<%@ include file='/common/i_yui.jspf'%>
	</tmpl:put>	
	<tmpl:put name='yui-skin'>yui-skin-sam</tmpl:put>
	
  <tmpl:put name='content' direct='true'>
  
	<link rel="stylesheet" href="css/transportation.css" type="text/css" />
	<link type="text/css" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/themes/ui-darkness/jquery-ui.css" rel="stylesheet">
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/jquery-ui.min.js"></script>
	
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
                      
                    function doCompositeLink(compId1,compId2,compId3,compId4,compId5, url) {
			          var param1 = document.getElementById(compId1).value;
			          var param2 = document.getElementById(compId2).value;
			          var param3 = document.getElementById(compId3).value;
			          var param4 = document.getElementById(compId4).checked;
			          var param5 = document.getElementById(compId5).value;
			          			          
			          if(param1.length == 0) { // || param2.length == 0) {
			          		alert("Please select the required filter param (Date)");
			          } else {
			          	location.href = url+"?"+compId1+"="+ param1+"&"+compId2+"="+param2+"&"+compId3+"="+param3+"&"+compId4+"="+param4+"&"+compId5+"="+param5;
			          }
        			} 
        			
        			function addTSRowHandlers(tableId, rowClassName) {
					    var previousClass = null;
					    var table = document.getElementById(tableId);
					    
					    if(table != null) {
						    var rows = table.tBodies[0].getElementsByTagName("tr");	 	       
						    for (i = 0; i < rows.length; i++) {	    	
						        var cells = rows[i].getElementsByTagName("td");
						       
						        for (j = 0; j < cells.length-3; j++) {
						        	
						            cells[j].onmouseover = function () {
						            	previousClass = this.parentNode.className;
						            	this.parentNode.className = this.parentNode.className + " " + rowClassName ;
						            };
						        
						            cells[j].onmouseout = function () {
						              	this.parentNode.className = previousClass;
						            };
						        
						            cells[j].onclick = function () {
								      		showTimeslot('panel-'+this.parentNode.getElementsByTagName("td")[1].innerHTML
								      						, findPosX(this.parentNode)
								      						, findPosY(this.parentNode));									      		
								    };
						        }
						    }
						}
				}
                    
                 function showTimeslot(rowDiv, rowX, rowY) {
                 	var tsPanel = new YAHOO.widget.Panel(rowDiv, {       
					                          width: "700px",					                           
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

                 function showUploadTimeslotCapacityForm() {
                	 
                	 $('#result').html('');
            		 addSysMessage("", false);
            		 $('#capacityform').trigger('reset');
            		 
                	 $(function() {
             			$("#dialog").dialog({
            				modal: true,
            				width: 825,
            				height: 400,
            				buttons: {
            					"Cancel": function() {
            						$(this).dialog("close");
            					}
            				}
             			});
             		});
        		 }
                 
                 $(document).ready(function(){

                	 $( "#capacityform" ).bind("submit", function(e) {

                		  e.preventDefault(); //Prevent Default action.

                		  $('#result').html('');
                		  addSysMessage("", false);                		  
                		        
                		  var formObj = $(this);
                		  var formURL = formObj.attr("action");
                		  var formData = new FormData(this);
                		  
                		  $.ajax({
                		         url: formURL,
                		         type: 'POST',
                		         data:  formData,
                		         mimeType:"multipart/form-data",
                		         contentType: false,
                		         cache: false,
                		         processData:false,
                		                success: function(data, textStatus, jqXHR)
                		                {
                		                  
                		                       $('#result').html(data);
                		                       
                		                       if(data != null && data != '') {
                		                                 addSysMessage("Timeslot capacity upload failed", true);       
                		                       } else {
                		                                 addSysMessage("Timeslot capacity upload successful", false);  
                		                       }
                		                },
                		                error: function(jqXHR, textStatus, errorThrown) 
                		                {
                		                       addSysMessage("Timeslot capacity upload failed", true);
                		                }
                		  });
                	});
                });	

                 function uploadTimeslotCapacity() {
                	  
                	 $("#capacityform").submit(); //Submit the form
                 }
                 
                
                 
                 var errColor = "#FF0000";
           	     var msgColor = "#00FF00";
                 
                 function addSysMessage(msg, isError) {
               		var errContObj = YAHOO.util.Dom.get("errContainer");
         		    if(isError) {
         		    	errContObj.style.color = errColor;
         	      	} else {
         	      		errContObj.style.color = msgColor;
         	      	}
         	      	errContObj.style.fontWeight="bold";
               		YAHOO.util.Dom.get("errContainer").innerHTML = msg;
                }
             </script>
                </td>
                <td>
                  &nbsp;<form:errors path="rDate" />
                </td>
                <td> 
                  <select id="condition" name="condition">
                      <option value="">EQUAL</option> 
                      <c:forEach var="_condition" items="${conditions}">
                      	  <c:choose>                          	
	                            <c:when test="${_condition.name != '<>'}" > 
	                              <c:choose>                          	
		                            <c:when test="${condition == _condition.name}" > 
		                              <option selected value="<c:out value="${_condition.name}"/>"><c:out value="${_condition.description}"/></option>
		                            </c:when>
		                            <c:otherwise> 
		                              <option value="<c:out value="${_condition.name}"/>"><c:out value="${_condition.description}"/></option>
		                            </c:otherwise> 
                         		   </c:choose>
                                </c:when>
                            	<c:otherwise/> 
                          </c:choose>                             
                                
                        </c:forEach>   
                   </select>
                
                </td>
                <td> 
                  <select id="cutOff" name="cutOff">
                      <option value="">--All Handoff</option> 
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
                  <%if(com.freshdirect.transadmin.security.SecurityManager.isUserAdminOrPlanning(request)){%>                  
                      <option <c:choose> <c:when test="${rType == 'T'}" >selected </c:when> </c:choose> value="T">Time</option>  
                      <option <c:choose> <c:when test="${rType == 'O'}" >selected </c:when> </c:choose> value="O">Order</option>
                  <% } %>   
                      <option <c:choose> <c:when test="${rType == 'D'}" >selected </c:when> </c:choose> value="D">Display</option>
                   </select>
                
                </td>
                <td><span style="font-size: 11px">&nbsp;Auto Refresh :</span><input type="checkbox" name="autorefresh" id="autorefresh" <%= ("on".equalsIgnoreCase(request.getParameter("autorefresh")) ? "checked=\"true\"" : "false") %>  /></td>
                   <td>
                     <input style="font-size:11px" type = "button" value="&nbsp;View&nbsp;" onclick="javascript:doCompositeLink('rDate','cutOff','rType','autorefresh','condition','earlywarning.do')" />
                  </td> 
                  <%if(com.freshdirect.transadmin.security.SecurityManager.isUserAdminOrPlanning(request)){%>
                  <td>
                     <input style="font-size:11px" type = "button" value="&nbsp;Upload Capacity&nbsp;" onclick="javascript:showUploadTimeslotCapacityForm()" />
                     <div style='display:none;' id="dialog" title="Timeslot Capacity Xls file Upload">
						<form enctype="multipart/form-data" method="POST" action="uploadtimeslotcapacity.do" name="capacityform" id="capacityform">
							<table cellspacing="0" cellpadding="0" class="tableForm">
								<thead>
									<tr>
										<th>Timeslot Capacity File Upload</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td><div id="errContainer"></div></td>
									</tr>
									<tr>
										<td><input type="file" class="input" name="fileToUpload" size="45" id="fileToUpload"></td>
									</tr>
									<tr>
										<td>Please select a Xls file and click Upload button</td>
									</tr>
									<tr>
										<td><input type = "button" id="buttonUpload" value="Upload" onclick="javascript:uploadTimeslotCapacity()"></input></td>
									</tr>
								</tbody>
							</table>
							<div id="result" width="800"></div>
						</form>
					</div>
                  </td> 
                  <% } %>
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
	            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="<%= scenariotitle %>"
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
				  <ec:column property="unassignedCount" title="Unassigned" />				  
				  <ec:column title=" " width="5px" 
									filterable="false" sortable="false" cell="capacityCloseButtonCell" property="referenceId" />
					
					<%if(request.getParameter("cutOff") == null ||"".equals(request.getParameter("cutOff"))){ %>
				  <ec:column title=" " width="5px" 
									filterable="false" sortable="false" cell="capacityDynamicButtonCell" property="dynamicActive" />
					<%} %>
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
					<%if(request.getParameter("cutOff") == null ||"".equals(request.getParameter("cutOff"))){ %>
				  <ec:column title=" " width="5px" 
									filterable="false" sortable="false" cell="capacityDynamicButtonCell" property="dynamicActive" />
					<%} %>
				  <ec:column title=" " width="5px" 
									filterable="false" sortable="false" cell="discount" property="isDiscounted" />															  	                           
	            </ec:row>
	          </ec:table>
	          
	          <br/>
					<table
						style="font-size: 9pt; font-family: Verdana, Arial, Helvetica, sans-serif; background-color: #e7e7d6;">
						<tr>
							<td><b>Unassigned Summary</b></td>
							<td><b>Unassigned</b></td>
							<td><b>Mismatched</b></td>
						</tr>
						<tr>
							<td>Orders:</td>
							<td align="center"><%= ((Integer) request.getAttribute("unassignedOrderCnt")).intValue() %></td>
							<td align="center"><%= ((Integer) request.getAttribute("mismatchOrderCnt")).intValue() %></td>
						</tr>
						<tr>
							<td># of Zones Affected:</td>
							<td align="center"><%= ((Integer) request.getAttribute("unassignedZoneCnt")).intValue()%></td>
							<td align="center"><%= ((Integer) request.getAttribute("mismatchZoneCnt")).intValue()%></td>
						</tr>
						<tr>
							<td># of Zone and Time Windows Affected:</td>
							<td align="center"><%= ((Integer) request.getAttribute("unassignedZonesByWindowCnt")).intValue() %></td>
							<td align="center"><%= ((Integer) request.getAttribute("mismatchZonesByWindowCnt")).intValue() %></td>
						</tr>
					</table>
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
			    	<div id="panel-<%=_command.getCode() %>">
			    	<div class="hd"><img src="images/icons/edit_ON.gif" width="16" height="16" border="0" align="absmiddle" />&nbsp;&nbsp;&nbsp;<%="Breakdown-"+_command.getName() %></div>
			    	<div class="bd">
			    	<div class="eXtremeTable" >
			    	<table id="timeslot_table-<%=_command.getCode() %>"  border="0"  cellspacing="0"  cellpadding="0"  class="tableRegion"  width="98%" >
				    	<thead>
						<tr>
							<td class="tableHeader" >Name</td>
							<td class="tableHeader" >Planned</td>
							<td class="tableHeader" >Confirmed</td>
							<td class="tableHeader" >% Confirmed</td>
					
							<td class="tableHeader" >Allocated</td>
							<td class="tableHeader" >% Allocated</td>
							<td class="tableHeader" >Cut Off</td>
							<td class="tableHeader" >#Trucks</td>
							<td class="tableHeader" >&nbsp;</td>
							<%--<td class="tableHeader" >&nbsp;</td>--%>
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
								<td><%=_commandTS.getWaveCode()%></td>
								<td><%= rType != null && rType.equalsIgnoreCase("O") ? "n/a" : ""+_commandTS.getNoOfResources() %></td>
								<td><input type="button" 
										class="<%= _commandTS.getClosedCount() > 0 ? "timeslot_closed" : "timeslot_open" %>" 
												value="<%= (_commandTS.getClosedCount() > 0 ? "C" : "O") %>" 
														onclick="updateTimeslot(this, '<%= _commandTS.getReferenceId() %>', '0')"
														<%= (com.freshdirect.transadmin.security.SecurityManager.isUserAdminOrPlanning(request) ?  " " : " disabled=\"disabled\"") %> /></td>
								<%-- <td><input type="button" 
										class="<%= _commandTS.getDynamicActiveCount() > 0 ? "dynamic_enabled" : "dynamic_disabled" %>" 
												value="<%= (_commandTS.getDynamicActiveCount() > 0 ? "D" : "S") %>" 
														onclick="updateDynamicTimeslot(this, '<%= _commandTS.getReferenceId() %>', '0')"
														<%= (com.freshdirect.transadmin.security.SecurityManager.isUserAdmin(request) ?  " " : " disabled=\"disabled\"") %> /></td>
								 --%>						
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

	  			<style>

						table.tableForm {
						    border-collapse: collapse;
						    margin: 1em auto;
						}
						
						table.tableForm thead th {
						    color: #FF0000;
						    font: bold 11px Arial,Helvetica,sans-serif;
						    padding: 4px 8px;
						    text-align: left;
						    text-transform: uppercase;
						    white-space: nowrap;
						}
						
						table.tableForm tbody td {
						    padding: 4px 8px;
						}
						
						.button {
						    font: bold 12px Arial,Helvetica,sans-serif;
						}
						
						table.summaryTable th, table.summaryTable td {
							cursor: pointer;
							cursor: hand;
						}
						table.summaryTable th.submenu {
							border: 0px solid #fff;
						}
						table.summaryTable th.first {
							border-right: 1px solid #fff;
							border-left: 1px solid #fff;
						}
						table.summaryTable th.last {
							border-right: 1px solid #fff;
						}
						table.summaryTable th {
							border: 1px solid #fff;							
						}
						table.summaryTable td.first {
							border: 1px dashed #fff;
							border-left: 1px solid #fff;
						}
						table.summaryTable td.last {
							border-right: 1px solid #fff;
							border-bottom: 1px dashed #fff;	
							padding: 2px;
							align: left;
						}
						table.summaryTable td {
							border-right: 1px dashed #fff;
							border-bottom: 1px dashed #fff;			
							color: #ffffff;
						}
						table.summaryTable td.red {
							background-color: red;
						}
						table.summaryTable td.yellow {
							background-color: yellow;			
						}
						
					</style>
  </tmpl:put>
  
</tmpl:insert>
