<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ page import='com.freshdirect.transadmin.model.ZipCodeModel' %>

<%
	pageContext.setAttribute("HAS_ADDBUTTON", "false");
	pageContext.setAttribute("HAS_DELETEBUTTON", "false");
	String envName = ""; 
	if(request.getRequestURL().toString().indexOf("dev") > -1) { envName = "DEV"; };
%>


<tmpl:insert template='/common/sitelayout.jsp'>
	<tmpl:put name='yui-lib'>
		<%@ include file='/common/i_yui.jspf'%>
	</tmpl:put>	
	<tmpl:put name='yui-skin'>yui-skin-sam</tmpl:put>
	<tmpl:put name='title' direct='true'> Operations : Zip Codes</tmpl:put>

	<tmpl:put name='content' direct='true'>
	
		<div align="center">
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<br><br>
					<tr>
				         <td class="screentitle">
				             List of Delivery ZipCodes
				         </td>
			        </tr> 
			</table>
		</div>
	<div class="contentroot">
		<br/>
		<% if(!"DEV".equalsIgnoreCase(envName)) { %>
			<span>&nbsp;&nbsp;&nbsp;<input type = "button" style="font-size:11px;" value="&nbsp;New Zip Code&nbsp;" onclick="javascript:showZipCodePanel()" /></span> 
		<%}%>
		<table width="100%">
			<tr>
			  <td style="vertical-align: top;">
					
						<ec:table items="zipcodes" action="${pageContext.request.contextPath}/zipcode.do"
							imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title=""
							width="98%"  view="fd" filterable="true" autoIncludeParameters="false" rowsDisplayed="50"  >

							<ec:row interceptor="obsoletemarker">
							      
							  <ec:column alias="zipcode" property="zipCode" title="Zip Code"/>
							  <ec:column property="homeCoverage" title="Home Coverage"/>
							  <ec:column property="cosCoverage" title="COS Coverage"/>
							  <ec:column property="ebtAccepted" title="EBT Accepted"/>
							</ec:row>
							 <div id="zipcode_container" style="display:none;">
							  <%
								Set gridData = (Set)request.getAttribute("zipcodes");
								int _rowIndex = 3;
								if(gridData != null) {
								
									Iterator<ZipCodeModel> _itr = gridData.iterator();
									ZipCodeModel _command = null;
									while(_itr.hasNext()) {
										_command = _itr.next();
								%>
									<div id="panel-<%=_rowIndex %>">
									<div class="hd"><img src="images/icons/edit_ON.gif" width="16" height="16" border="0" align="absmiddle" />&nbsp;&nbsp;&nbsp;<%="Breakdown-"+_command.getZipCode() %></div>
									<div class="bd" style='background-color:#F2F2F2;align:center;'>
										<div id="errContainer"></div>
										<table id="zipcode_table-<%=_rowIndex %>" border="0"  cellspacing="0"  cellpadding="0"  class="forms1" >
											<tbody class="tableBody" >
															<tr>
																   <td align="right">Zip Code</td>
																   <td><input size="30" id="dZipCode" readonly="readonly" value='<%=_command.getZipCode() %>' /></td>
															</tr>
															<tr>
																   <td align="right">Home Coverage</td>
																   <td><input size="30" id="dHomeCoverage" value='<%=_command.getHomeCoverage() %>' /></td>
															</tr>
															<tr>
																   <td align="right">COS Coverage</td>
																   <td><input size="30" id="dCosCoverage" value='<%=_command.getCosCoverage() %>' /></td>
															</tr>
															<tr>
																   <td align="right">EBT Accepted</td>
				   												   <td><input type="checkbox"  id="ebtAccepted" <%="X".equals(_command.getEbtAccepted())?"checked":"" %> /></td>
															</tr>
															<tr>
																<td align="center" colspan="2">
																	<input type="button" id="add" value="&nbsp;Save&nbsp;" onclick="javascript:updateZipCodeCoverage('dZipCode','dHomeCoverage','dCosCoverage','ebtAccepted');" /> &nbsp;&nbsp;
																</td>
															</tr>
											</tbody>
										</table>
									</div>
									</div>
							<%	 _rowIndex++;}
								}
							%>
						 </div>
					
						  </ec:table>
						  
						
				</td>
				
			</tr>
		</table>
		

	<div style='display:none;height:0px;width:0px;'>
      <div id="zipCodePanel-1">
      <div class="hd"><img src="images/icons/edit_ON.gif" width="16" height="16" border="0" align="absmiddle" />&nbsp;&nbsp;&nbsp;Add Zip Code</div>
      <div class="bd">
      	<div id="errContainer"></div>
      	<div style="background-color:#F2F2F2;">
				<table border="0"  cellspacing="0"  cellpadding="0" class="forms1" >
							<tbody class="tableBody" >
											<tr>
												   <td align="right">Zip Code</td>
   												   <td><input size="30" id="dZipCode" value="" /></td>
											</tr>
											<tr>
												   <td align="right">Home Coverage</td>
   												   <td><input size="30" id="dHomeCoverage" value="0" /></td>
											</tr>
											<tr>
												   <td align="right">COS Coverage</td>
   												   <td><input size="30" id="dCosCoverage" value="0" /></td>
											</tr>
											<tr>
												   <td align="right">EBT Accepted</td>
   												   <td><input type="checkbox"  id="ebtAccepted" /></td>
											</tr>
											<tr>
												<td align="center" colspan="2">
													<input type="button" id="add" value="&nbsp;Save&nbsp;" onclick="javascript:addNewZipCodeCoverage('dZipCode','dHomeCoverage','dCosCoverage','ebtAccepted');" /> &nbsp;&nbsp;
												</td>
											</tr>
							</tbody>
				</table>
	      </div>      
        </div>
    </div>
    </div>

	</div>
	<style>
		#contzipcodetable table {
			width: 100%;
		}

	</style>
	<script>
		var errColor = "#FF0000";
		var msgColor = "#00FF00";	
		
		var jsonrpcClient = new JSONRpcClient("domainprovider.ax");
		addZipCodeRowHandlers('ec_table', 'rowMouseOver');	

		 function addZipCodeRowHandlers(tableId, rowClassName) {
					    var previousClass = null;
					    var table = document.getElementById(tableId);
					    
					    if(table != null) {
						    var rows = table.tBodies[0].getElementsByTagName("tr");
						    for (i = 0; i < rows.length; i++) {	    	
						        var cells = rows[i].getElementsByTagName("td");
						        
						        for (j = 0; j < cells.length; j++) {
						        	
						            cells[j].onmouseover = function () {
						            	previousClass = this.parentNode.className;
						            	this.parentNode.className = this.parentNode.className + " " + rowClassName ;
						            };
						        
						            cells[j].onmouseout = function () {
						              	this.parentNode.className = previousClass;
						            };
						        
						            cells[j].onclick = function () {
								      		showZipCodeTable('panel-'+this.parentNode.rowIndex
								      						, findPosX(this.parentNode)
								      						, findPosY(this.parentNode));
								    };
						        }
						    }
						}
		}

		function showZipCodeTable(rowDiv, rowX, rowY) {
                 	var zcPanel = new YAHOO.widget.Panel(rowDiv, {
					                          width: "440px",
					                          close: true, 
					                          fixedcenter: true, 
					                          zindex:4,
					                          modal: true,
					                          visible: false,
					                          xy: [rowX, rowY],
					                          effect:{effect:YAHOO.widget.ContainerEffect.SLIDE,duration:0.25}});
					addSysMessage("", false);
					zcPanel.render(document.body);
          			zcPanel.show();
        }

		 function updateZipCodeCoverage(compId1,compId2,compId3,compId4) { 
			var _zipCode = document.getElementById(compId1).value;
			var _homeCoverage = document.getElementById(compId2).value;
			var _cosCoverage = document.getElementById(compId3).value;
			var _ebtAccepted = ""+document.getElementById(compId4).checked;			
			_ebtAccepted = (_ebtAccepted=="true")?"X":"";
			if(_homeCoverage.trim().length ==0 ||  _cosCoverage.trim().length == 0) {
				 addSysMessage("Please enter required values!", true);
			}else if(_zipCode.trim().length > 0 && _homeCoverage.trim().length > 0 && _cosCoverage.trim().length > 0) {
     			var result = jsonrpcClient.AsyncDomainProvider.updateZipCodeCoverage(zipCodeUpdateCallBack, _zipCode, _homeCoverage, _cosCoverage,_ebtAccepted);
			} 
        }

		function addNewZipCodeCoverage(compId1,compId2,compId3,compId4) { 
			var _zipCode = document.getElementById(compId1).value;
			var _homeCoverage = document.getElementById(compId2).value;
			var _cosCoverage = document.getElementById(compId3).value;
			var _ebtAccepted = ""+document.getElementById(compId4).checked;
			_ebtAccepted = (_ebtAccepted=="true")?"X":"";
			if(_homeCoverage.trim().length == 0 ||  _cosCoverage.trim().length == 0) {
				 addSysMessage("Please enter required values!", true);
			}else if(_zipCode.trim().length > 0 && _homeCoverage.trim().length > 0 && _cosCoverage.trim().length > 0) {
     			var result = jsonrpcClient.AsyncDomainProvider.addNewZipCodeCoverage(zipCodeCallBack, _zipCode, _homeCoverage, _cosCoverage,'<%= envName %>',_ebtAccepted);
			} 
        }

		function zipCodeUpdateCallBack(result, rpcException) {
      	       
          if(result){
          	  addSysMessage("ZipCode coverage updated successfully", false);
  			  addZipCodePanel.destroy();
          }
		  if(rpcException){
			   alert('Unable to add/Update Zip code coverage. Please try to refresh the browser window!\n'+rpcException);
		  }
       }

	   function zipCodeCallBack(result, rpcException) {
      	       
          if(result === 1) {
			 addSysMessage("Unable to add zipcode, Please check zipcode already exists!", true);
		  } else if(result === 2){
			 addSysMessage("Unable to add zipcode, Please check STREET DATA for zipcode!", true);
		  }else {
          	  addSysMessage("ZipCode added successfully", false); 
          }

		  if(rpcException){
			   alert('Unable to add/Update Zip code coverage. Please try to refresh the browser window!\n'+rpcException);
		  }
       }

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

	  var addZipCodePanel;
	  addZipCodePanel = new YAHOO.widget.Panel("zipCodePanel-1", {
                          width: "440px", 
                          fixedcenter: true, 
                          close: true, 
                          draggable: false, 
                          zindex:4,
                          modal: true,
                          visible: false,
                          effect:{effect:YAHOO.widget.ContainerEffect.SLIDE,duration:0.25}});

		function showZipCodePanel(){
			 addSysMessage("", false);
			 addZipCodePanel.render(document.body);
             addZipCodePanel.show();
		}

     </script>
	</tmpl:put>


</tmpl:insert>
