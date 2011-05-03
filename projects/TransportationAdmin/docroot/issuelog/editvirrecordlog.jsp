<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import='com.freshdirect.transadmin.model.*' %>
<% 
	String pageTitle = "";
	String issueLog = request.getParameter("issueLog");
	if((issueLog == null) || ("V".equalsIgnoreCase(issueLog))) {
		pageTitle = "VIR Record";
	}else if("I".equalsIgnoreCase(issueLog)) {
		pageTitle = "Issue Types";
	}else if("S".equalsIgnoreCase(issueLog)) {
		pageTitle = "Issue Sub Types";
	}

	String truckNumberVal = request.getParameter("truckNumber") != null ? request.getParameter("truckNumber") : "";
%>

<tmpl:insert template='/common/sitelayout.jsp'>
	<tmpl:put name='yui-lib'>
		<%@ include file='/common/i_yui.jspf'%>	
	</tmpl:put>
	

<tmpl:put name='title' direct='true'>Create VIR Record</tmpl:put>

	<tmpl:put name='content' direct='true'>
		
		<script src="js/jsonrpc.js" language="javascript" type="text/javascript"></script>
		<script src="js/maintenancelog.js" language="javascript" type="text/javascript"></script>

	<div class="MNM001 subsub or_999">
		<div class="subs_left">	
			<div class="sub_tableft sub_tabL_MNM001 <% if(!"M".equalsIgnoreCase(request.getParameter("issueLog"))&&!"I".equalsIgnoreCase(request.getParameter("issueLog"))
			&&!"S".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeL<% } %>">&nbsp;</div>
			
			<div class="subtab <% if(!"M".equalsIgnoreCase(request.getParameter("issueLog"))&&!"I".equalsIgnoreCase(request.getParameter("issueLog")) &&!"S".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="virrecordlog.do" class="<% if(!"M".equalsIgnoreCase(request.getParameter("issueLog"))&&!"I".equalsIgnoreCase(request.getParameter("issueLog")) &&!"S".equalsIgnoreCase(request.getParameter("issueLog"))) { %>MNM001<% } %>">VIR Record</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if(!"M".equalsIgnoreCase(request.getParameter("issueLog"))&&!"I".equalsIgnoreCase(request.getParameter("issueLog")) &&!"S".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeR<% } %>">&nbsp;</div>		
		
			<div class="sub_tableft sub_tabL_MNM001 <% if("M".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("M".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="maintenancelog.do?issueLog=M" class="<% if("M".equalsIgnoreCase(request.getParameter("issueLog"))) { %>MNM001<% } %>">Maintenance Record</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if("M".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeR<% } %>">&nbsp;</div>
			
			<div class="sub_tableft sub_tabL_MNM001 <% if("I".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("I".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="virrecordlog.do?issueLog=I" class="<% if("I".equalsIgnoreCase(request.getParameter("issueLog"))) { %>MNM001<% } %>">Issue Type</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if("I".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeR<% } %>">&nbsp;</div>

			<div class="sub_tableft sub_tabL_MNM001 <% if("S".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("S".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="virrecordlog.do?issueLog=S" class="<% if("S".equalsIgnoreCase(request.getParameter("issueLog"))) { %>MNM001<% } %>">Issue SubType</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if("S".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeR<% } %>">&nbsp;</div>		
		</div>
	</div>
	<div class="cont_row_bottomline"><!--  --></div>
		<br/> 
		<div align="center" style='background-color:#F2F2F2;border:1px solid #000; margin-left:10px;margin-right:10px;'>
		  <form:form commandName = "virRecordForm" method="post">
		 		<form:hidden path="id"/>
				<table width="98%" cellpadding="0" cellspacing="0" border="0">
				  <tr>
					<td class="screentitle" colspan="2">
					  Create VIR Record
					</td>
				  </tr>
		          <tr>
				    <td class="screenmessages" colspan="2">
						<jsp:include page='/common/messages.jsp'/>
					</td>
				 </tr>
                 <tr>
					 <td class="screencontent" style="align:center;">
						  <table class="forms1">
							<tr>
								<td>Create Date</td>
								<td>
									<input maxlength="10" size="28" id="createDate" name="createDate" value='<c:out value="${currentDate}"/>' disabled="true"/>&nbsp;
								</td>
								<td>
									<form:errors path="createDate" />&nbsp;
								</td>
							</tr>
							 <tr>
								  <td>Truck Number</td>
								  <td>                  
									<form:select path="truckNumber" onChange="javascript:getVendorInfo();">
							                <form:option value="" label="--Please Select Truck"/>
										    <form:options items="${truckAssets}" itemLabel="assetNo" itemValue="assetNo" />
					                </form:select>
								  </td>
								  <td>
									  &nbsp;<form:errors path="truckNumber" />
								  </td>
							 </tr>
							 <tr>
								  <td>Vendor</td>
								  <td>                  
										<form:input maxlength="50" size="28" path="vendor" disabled="true"></form:input>
								  </td>
								  <td>
									  &nbsp;<form:errors path="vendor" />
								  </td>
							 </tr>
      					     <tr>
								  <td>Reporting Driver</td>
								  <td>                  
										<form:select path="reportingDriver">
							                <form:option value="N/A" label="--N/A--"/>
										    <form:options items="${drivers}" itemLabel="name" itemValue="employeeId" />
					                   </form:select>
								  </td>
								  <td>
									  &nbsp;<form:errors path="reportingDriver" />
								  </td>
							 </tr>
							 <tr>
								<td>Entered By</td>
								<td>
									<input maxlength="50" size="28" id="createdBy" name="createdBy" value='<c:out value="${userId}"/>' disabled="true"/>
								</td>
								<td>
									<form:errors path="createdBy" />&nbsp;
								</td>
							</tr>
						  </table>
					</td>
				    <td class="screencontent" style="align:center;">
							<div id="errContainer"></div>
							<table class="forms1">
											<tr>
												<td>Issue Type</td>
												<td>
													<select id="issueType" style="width: 70;" name="issueType" onChange="javascript:getIssueSubTypes();"> 
													<option value="">--Please Select IssueType</option> 
													<c:forEach var="issueType" items="${issueTypes}">
														<option value="<c:out value="${issueType.issueTypeName}"/>"><c:out value="${issueType.issueTypeName}"/></option>
													</c:forEach>
													</select>
												</td>
											</tr>
											<tr>
												<td>Issue SubType</td>
												<td><select id="issueSubType"></select></td>
											</tr>
											<tr>
												 <td>Comments</td>
												  <td><textarea id="comments" name="comments" rows="2" cols="45"></textarea></td>
											</tr>
											<tr>
												<td>Front/Back</td>
												<td><select id="damageLocation" style="width: 70;" name="damageLocation"> 
												<option value="N/A">--N/A--</option> 
												<c:forEach var="dLocation" items="${damageLocations}">
													<option value="<c:out value="${dLocation}"/>"><c:out value="${dLocation}"/></option>
												</c:forEach>
												</select></td>
											</tr>
											<tr>
											  <td>Driver/Passenger</td>
												  <td><select id="issueSide" style="width: 70;" name="issueSide"> 
												<option value="N/A">--N/A--</option> 
												<c:forEach var="iSide" items="${issueSides}">
													<option value="<c:out value="${iSide}"/>"><c:out value="${iSide}"/></option>
												</c:forEach>
											</select></td>
											<td>
											   &nbsp;&nbsp;<input type="button" id="add" value="&nbsp;ADD&nbsp;" onclick="javascript:doAdd();" /> 
										   </td>
											</tr>
								</table>
					</td>
				</tr>
   			</table>
		  </form:form>
		
	<style>
		.yui-skin-sam .yui-dt-body { cursor:pointer; } 
		#single { margin-top:2em; }

		.screencontent {
			width:689px;
		}
	</style>
	
<script>
      var issueLogDataTable;
      var errColor = "#FF0000";
	  var msgColor = "#0066CC";
     
      var virRecordData = { 
            issueLogData: [
  	    <% 
            		VIRRecord coreVIRRecord = (VIRRecord)request.getAttribute("virRecordForm");
            		StringBuffer dataBuffer = new StringBuffer();
        			if(coreVIRRecord != null) {
        				if(coreVIRRecord.getVirRecordIssues() != null) {
        					Iterator _itr = coreVIRRecord.getVirRecordIssues().iterator();
        					while(_itr.hasNext()) {
        						IssueLog _issueLog = (IssueLog)_itr.next();
        						if(_issueLog != null) {
        							
										dataBuffer.append("{issueType:\"").append(_issueLog.getIssueType()).append("\",");
        								dataBuffer.append("issueSubType:\"").append(_issueLog.getIssueSubType()).append("\",");
										dataBuffer.append("damageLocation:\"").append(_issueLog.getDamageLocation()).append("\",");
										dataBuffer.append("issueSide:\"").append(_issueLog.getIssueSide()).append("\",");
										dataBuffer.append("comments:\"").append(_issueLog.getComments() !=null ? _issueLog.getComments(): "").append("\",");
        								dataBuffer.append("deleteBtn:\"\"}");
        								if(_itr.hasNext()) {
        									dataBuffer.append(",");
        								}
        							
        						}
        					}
        				}
        			}
            	%> 
            	<%= dataBuffer.toString() %>
            ]};

      YAHOO.util.Event.addListener(window, "load", function() {
                 
        issueLogDataSource = new YAHOO.util.DataSource(virRecordData.issueLogData);
        issueLogDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY; 
        issueLogDataSource.responseSchema = {fields: ["issueType","issueSubType","damageLocation","issueSide","comments","deleteBtn"] }; 

		var attributeColumns =  [ 
			    {key:"issueType", label:"Issue Type", sortable:true, sortOptions: { defaultDir: YAHOO.widget.DataTable.CLASS_ASC }, className:"forms1"}, 
			    {key:"issueSubType", label:"Issue SubType", sortable:true, className:"forms1"},
				{key:'damageLocation',label:'Damage Location',sortable:false, className:"forms1"},
				{key:'issueSide',label:'Issue Side',sortable:false, className:"forms1"},
				{key:'comments',label:'Comments',sortable:false, className:"forms1"},
				{key:'deleteBtn',label:' ',formatter:function(elCell) {
        										elCell.innerHTML = '<img src="images/icons/delete.gif" title="delete row" />';
        										elCell.style.cursor = 'pointer';
    			}}
		 ];
				 	 
		 var sMyConfigs = { 
			    paginator : new YAHOO.widget.Paginator({ 
			        rowsPerPage    : 10
			    }) 
		  }; 
		 	 
		issueLogDataTable = new YAHOO.widget.DataTable("contissuetable"
															, attributeColumns
																	, issueLogDataSource
																				, sMyConfigs
																					, { selectionMode:"single" });
  		
  		issueLogDataTable.subscribe('cellClickEvent', function(ev) {
	    	var target = YAHOO.util.Event.getTarget(ev);
	    	var column = issueLogDataTable.getColumn(target);
	    		if (column.key == 'deleteBtn') {
	        		issueLogDataTable.deleteRow(target);
	    		} 
			});
  		 return { 
  			            oDS: issueLogDataSource, 
  			            oDT: issueLogDataTable 
  			};
     });
     
     function doAdd() {
		 YAHOO.util.Dom.get("errContainer").value = "";
     		var _issueType = document.getElementById('issueType').value;
     		var _issueSubType = document.getElementById('issueSubType').value;
     		var _damageLocation = document.getElementById('damageLocation').value;
     		var _issueSide = document.getElementById('issueSide').value;
     		var _comments = document.getElementById('comments').value;
     		
     		var hasError;
     		if(_issueType.trim().length > 0 && _issueSubType.trim().length > 0) {

				if(issueLogDataTable.getRecordSet() != null 
     	  			&& issueLogDataTable.getRecordSet().getRecords()) {
            		var _data = new Array();
  	  				var records = issueLogDataTable.getRecordSet().getRecords();
      	  			for(i=0; i<records.length; i++) {
      	  				_data[i] = new Array();
      	  				_data[i][0] = records[i].getData('issueType');
      	  			}

      	  			for(k=0; k<_data.length; k++){
						if(_issueType ==_data[k][0]){
							alert('IssueType selected already added to VIR.');
							hasError = true;
						}
          	  		}
      	  		}
				if(!hasError){
     				issueLogDataTable.addRow({issueType:_issueType, issueSubType:_issueSubType,damageLocation:_damageLocation,issueSide:_issueSide,comments:_comments, deleteBtn:''});
					
					document.getElementById('issueType').value='';
     				document.getElementById('issueSubType').length=0;
     				document.getElementById('damageLocation').value='N/A';
     				document.getElementById('issueSide').value='N/A';
     				document.getElementById('comments').value='';
     			}
			} else {
     			addSysMessage("Please enter the required values (Issue Type & IssueSub Value)",true);
     		}
     }
         
     function submitIssueLogTable() { 
     	  var _data = new Array();
     	  if(issueLogDataTable.getRecordSet() != null 
     	  			&& issueLogDataTable.getRecordSet().getRecords()) {
      	  	var records = issueLogDataTable.getRecordSet().getRecords();
      	  	for(i=0; i<records.length; i++) {
      	  		_data[i] = new Array();
      	  		_data[i][0] = records[i].getData('issueType');
      	  		_data[i][1] = records[i].getData('issueSubType');
				_data[i][2] = records[i].getData('damageLocation');
				_data[i][3] = records[i].getData('issueSide');
				_data[i][4] = records[i].getData('comments');
      	  	}
      	  }
      	  try { 
          	if(document.getElementById('truckNumber').value == null || document.getElementById('truckNumber').value.length == 0) {
          		addSysMessage("VIRRecord Truck Number is required field", true);
          	} else { 
	     	 	var result = jsonrpcClient.AsyncDispatchProvider.saveVIRRecord(document.getElementById('createDate').value
	     													, document.getElementById('truckNumber').value
	     													, document.getElementById('vendor').value
	     													, document.getElementById('reportingDriver').value
	     													, document.getElementById('createdBy').value
	     	     	 										, _data);
	     	 	document.getElementById('id').value = result;
	     	 	if(result != null) {
	     	 		addSysMessage("VIRRecord saved successfully", false);
						window.location.href = 'editvirrecordlog.do';
					
	     	 	}
          	}
      	  }  catch(rpcException) {
          	   if(rpcException != null && rpcException.name != null 
                  	  && rpcException.name == ("org.springframework.dao.DataIntegrityViolationException")) {
	          	 addSysMessage("Unable to save VIR Reord.", true);
          	  } else {
				 addSysMessage("There was a problem in communication to the server. Please try to refresh the browser window!", true);
          	  }
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

	  function addNewVIR(){
		window.location.href = 'editvirrecordlog.do'
	  }
 </script>
    <br/>
	
	<table width="98%">
		<tr>
			<td align="center"><b>VIR Record Issues</b></td>
		</tr>
		<tr><td>&nbsp;</tr>
		<tr>
			<td>
				<hr/>
				<div id="parentcontissuetable" style="height:100%;overflow-y:auto;background-color:#F2F2F2;">
					<div id="contissuetable" align="center"></div> 
				</div>	
			</td>
		</tr>
	</table>  
   	<br/>
   	 <table width="98%" cellpadding="0" cellspacing="0" border="0">
		 <tr>
				<td colspan="3" align="center">
					<input type = "button" value="&nbsp;Save&nbsp;" onclick="javascript:submitIssueLogTable();" />
					&nbsp;&nbsp;&nbsp;<input type = "button" value="&nbsp;Back&nbsp;" onclick="javascript:back();" />
					&nbsp;&nbsp;&nbsp;<input type = "button" value="&nbsp;Add New VIR&nbsp;" onclick="javascript:addNewVIR();" />
				</td>
		</tr>
   	 </table>
	<style>
		 .yui-skin-sam .yui-dt table {
			width:98%;	 
		 }
	 </style>

</div>


  </tmpl:put>
</tmpl:insert>
