<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ page import='com.freshdirect.transadmin.model.*' %>
<%@ page import='com.freshdirect.transadmin.util.*' %>
<%@ page import='com.freshdirect.framework.util.*' %>
<%@ page import='java.util.*' %>

<tmpl:insert template='/common/sitelayout.jsp'>
<tmpl:put name='yui-lib'>
	<%@ include file='/common/i_yui.jspf'%>	
</tmpl:put>	

<tmpl:put name='yui-skin'>yui-skin-sam</tmpl:put>
    <tmpl:put name='title' direct='true'> Admin : Asset : Add/Edit Asset (<%= request.getParameter("pAssetType") %>)</tmpl:put>
  
  <tmpl:put name='content' direct='true'>
		<div class="subs MNM004">
			<div class="subs_left">	
				<div class="sub_tableft sub_tabL_MNM004 <% if(request.getParameter("tAssetType")== null) { %>activeL<% } %>">&nbsp;</div>
				<div class="subtab <%if(request.getParameter("tAssetType")== null) { %>activeT<% } %>">
					<div class="minwidth"><!-- --></div>
					<a href="asset.do?pAssetType=GPS" class="<% if(request.getParameter("tAssetType")== null) { %>MNM004<% } %>">Asset</a>
				</div>
				<div class="sub_tabright sub_tabR_MNM004 <% if(request.getParameter("tAssetType")== null) { %>activeR<% } %>">&nbsp;</div>
		
				<div class="sub_tableft sub_tabL_MNM004 <% if(request.getParameter("tAssetType")!= null) { %>activeL<% } %>">&nbsp;</div>
				<div class="subtab <%if(request.getParameter("tAssetType")!= null) { %>activeT<% } %>">
					<div class="minwidth"><!-- --></div>
					<a href="assettemplate.do?tAssetType=GPS" class="<% if(request.getParameter("tAssetType")!= null) { %>MNM004<% } %>">Asset Template</a>
				</div>
				<div class="sub_tabright sub_tabR_MNM004 <% if(request.getParameter("tAssetType")!= null) { %>activeR<% } %>">&nbsp;</div>
			</div>
		</div>
    <br/><br/><br/><br/>
    <div align="center">
      <form:form commandName="assetForm" method="post">
           
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle">Add/Edit Asset</td>
          </tr>
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/> <div id="errContainer" style='margin:0 auto;'></div></td>
          </tr>
          <tr>
            <td class="screencontent">
              <form:hidden path="assetId"/>
              	
              <table class="forms1">  
               
                <tr>
                  <td>Asset Type</td>
                  <td>                  
                    <form:input readOnly="true" maxlength="15" size="15" path="assetType.code" />
                </td>
                <td>
                  &nbsp;<form:errors path="assetNo" />
                </td>
               </tr>
                     
               <tr>
                  <td>Asset No</td>
                  <td>                  
                    <form:input maxlength="15" size="15" path="assetNo" />
                </td>
                <td>
                  &nbsp;<form:errors path="assetNo" />
                </td>
               </tr>
                           
               <tr>
                  <td>Description</td>
                  <td>                  
                    <form:input maxlength="40" size="40" path="assetDescription" />
                </td>
                <td>
                  &nbsp;<form:errors path="assetDescription" />
                </td>
               </tr>
               
               <tr>
                  <td>Status</td>
                  <td> 
                  <form:select path="assetStatus">
                        <form:option value="null" label="--Please Select Asset Status"/>
                    	<form:options items="${assetstatuses}" itemLabel="description" itemValue="name" />
                   </form:select>
                </td>
                <td>
                  &nbsp;<form:errors path="assetStatus" />
                </td>
               </tr>

			    <tr>
                  <td>Asset Template</td>
                  <td> 
					   <form:select path="assetTemplate">
							<form:option value="null" label="--Please Select Template"/>
							<form:options items="${assetTemplates}" itemLabel="assetTemplateName" itemValue="assetTemplateId" />
					   </form:select>
                </td>
                <td>
                  &nbsp;<form:errors path="assetTemplate" />
                </td>
               </tr>
              
              </table>        
              
            </td>
          </tr>               
        </table>
      
      </form:form>
     </div>     
     &nbsp;&nbsp;&nbsp;
	<div style="float:right;font-weight:bold;"><br/>
			<span><input type="image" src="./images/icons/tick.gif" />&nbsp;Overriden from Template</span>
			<span><input type="image" src="./images/icons/unique.gif" />&nbsp;Unique to Asset</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	</div>
    <style>
		.yui-skin-sam .yui-dt-body { cursor:pointer; } 
		#single { margin-top:2em; }
	</style>
<script>
      var attributeDataTable;
      var jsonrpcClient = new JSONRpcClient("asset.ax");
      var errColor = "#FF0000";
	  var msgColor = "#0066CC";
     
      var assetData = { 
            attributeData: [
  	    <% 
            		Asset coreAsset = (Asset)request.getAttribute("assetForm");
            		StringBuffer dataBuffer = new StringBuffer();
        			if(coreAsset != null) {
        				if(coreAsset.getAssetAttributes() != null) {
        					Iterator _itr = coreAsset.getAssetAttributes().iterator();
        					while(_itr.hasNext()) {
        						AssetAttribute _attribute = (AssetAttribute)_itr.next();
        						if(_attribute != null) {
        							AssetAttributeId _attrId = _attribute.getId();
        							if(_attrId != null) {
        								dataBuffer.append("{attributeType:\"").append(_attrId.getAttributeType()).append("\",");
        								dataBuffer.append("attributeValue:\"").append(_attribute.getAttributeValue()).append("\",");
										dataBuffer.append("attributeMatch:\"").append(_attribute.getAttributeMatch()).append("\",");
        								dataBuffer.append("deleteBtn:\"\"}");
        								if(_itr.hasNext()) {
        									dataBuffer.append(",");	
        								}	
        							}
        						}
        					}
        				}
        			}
            	%> 
            	<%= dataBuffer.toString() %>
            ]};
	  
	  var assetTemplateData = { 
            attributeTemplateData: [
  	    <% 
            		Asset asset = (Asset)request.getAttribute("assetForm");
            		StringBuffer templateDataBuffer = new StringBuffer();
        			if(asset != null) {
						if(asset.getAssetTemplate()!=null){
							if(asset.getAssetTemplate().getAssetTemplateAttributes() != null) {
								Iterator _itr = asset.getAssetTemplate().getAssetTemplateAttributes().iterator();
								while(_itr.hasNext()) {
									AssetTemplateAttribute _attribute = (AssetTemplateAttribute)_itr.next();
									if(_attribute != null) {
										AssetTemplateAttributeId _attrId = _attribute.getId();
										if(_attrId != null) {
											templateDataBuffer.append("{attributeType:\"").append(_attrId.getAttributeType()).append("\",");
											templateDataBuffer.append("attributeValue:\"").append(_attribute.getAttributeValue()).append("\"}");
											if(_itr.hasNext()) {
												templateDataBuffer.append(",");	
											}	
										}
									}
								}
							}
						}
        			}
            	%> 
            	<%= templateDataBuffer.toString() %>
            ]}; 
           
      YAHOO.util.Event.addListener(window, "load", function() {
                 
        attributeDataSource = new YAHOO.util.DataSource(assetData.attributeData);        
        attributeDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY; 
        attributeDataSource.responseSchema = {fields: ["attributeType","attributeValue","attributeMatch","deleteBtn"] }; 
                            		  
		var attributeColumns =  [ 
			    {key:"attributeType", label:"Attribute Type", sortable:true, sortOptions: { defaultDir: YAHOO.widget.DataTable.CLASS_ASC }, className:"forms1"}, 
			    {key:"attributeValue", label:"Attribute Value", sortable:false, className:"forms1"},
				{key:'attributeMatch',label:'Is Overriden?',formatter:function(elCell, oRecord, oColumn, oData) {
												console.log(oData);
												if(oData === 'O'){
        											elCell.innerHTML = '<img src="images/icons/tick.gif" title="Overriden" />';
        										}else if(oData === 'U'){
													elCell.innerHTML = '<img src="images/icons/unique.gif" title="Unique" />';
												}
    			}},
				{key:'deleteBtn',label:'Delete',formatter:function(elCell) {
        										elCell.innerHTML = '<img src="images/icons/delete.gif" title="delete row" />';
        										elCell.style.cursor = 'pointer';
    			}}
		 ];
				 	 
		 var sMyConfigs = { 
			    paginator : new YAHOO.widget.Paginator({ 
			        rowsPerPage    : 10
			    }) 
		  }; 
		 	 
		attributeDataTable = new YAHOO.widget.DataTable("contattributetable"
															, attributeColumns
																	, attributeDataSource
																				, sMyConfigs
																					, { selectionMode:"single" });
  		
  		attributeDataTable.subscribe('cellClickEvent', function(ev) {
	    	var target = YAHOO.util.Event.getTarget(ev);
	    	var column = attributeDataTable.getColumn(target);
	    		if (column.key == 'deleteBtn') {
	        		attributeDataTable.deleteRow(target);
	    		} 
			});
  		 return { 
  			            oDS: attributeDataSource, 
  			            oDT: attributeDataTable 
  			};
     });
     
	  YAHOO.util.Event.addListener(window, "load", function() {
                 
        attributeTemplateDataSource = new YAHOO.util.DataSource(assetTemplateData.attributeTemplateData);
        attributeTemplateDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY; 
        attributeTemplateDataSource.responseSchema = {fields: ["attributeType","attributeValue"] }; 
                            		  
		var attributeTemplateColumns =  [ 
			    {key:"attributeType", label:"Attribute Type", sortable:true, sortOptions: { field: "attributeType" }, className:"forms1"}, 
			    {key:"attributeValue", label:"Attribute Value", sortable:false, className:"forms1"},
				{key:'deleteBtn',label:'Delete',formatter:function(elCell) {
        										elCell.innerHTML = '<img src="images/icons/delete.gif" title="delete row" />';
    			}}
		 ];
				 	 
		 var sTemplateConfigs = { 
			    paginator : new YAHOO.widget.Paginator({ 
			        rowsPerPage    : 10
			    }) 
		 };

		attributeTemplateDataTable = new YAHOO.widget.DataTable("conttemplateattributetable"
															, attributeTemplateColumns
																, attributeTemplateDataSource
																	, sTemplateConfigs
																		, {selectionMode:"single"});
  		
  		 return { 
  			            oDS: attributeTemplateDataSource, 
  			            oDT: attributeTemplateDataTable 
  			};
     });

     function doAdd() {
     		var _attributeType = document.getElementById('dAttributeType').value;
     		var _attributeValue = document.getElementById('dAttributeValue').value;
     		
     		if(_attributeType.trim().length > 0 && _attributeValue.trim().length > 0) {
     			attributeDataTable.addRow({attributeType:_attributeType, attributeValue:_attributeValue, deleteBtn:''});
     		} else {
     			alert("Please enter the required values Attribute Type & Attribute Value!");
     		}
     }
         
     function submitAttributeTable() { 
     	  var _data = new Array();
     	  if(attributeDataTable.getRecordSet() != null 
     	  			&& attributeDataTable.getRecordSet().getRecords()) {
      	  	var records = attributeDataTable.getRecordSet().getRecords();
      	  	for(i=0; i<records.length; i++) {
      	  		_data[i] = new Array();
      	  		_data[i][0] = records[i].getData('attributeType');
      	  		_data[i][1] = records[i].getData('attributeValue');	
      	  	}
      	  }
      	  try { 
          	if(document.getElementById('assetNo').value == null || document.getElementById('assetNo').value.length == 0) {
              	addSysMessage("Asset no is a required field", true);
          	} else if(document.getElementById('assetNo').value == null || document.getElementById('assetDescription').value.length == 0) {
          		addSysMessage("Asset Description is a required field", true);
          	} else if(document.getElementById('assetStatus').value == null 
                  	|| document.getElementById('assetStatus').value.length == 0
                  		|| document.getElementById('assetStatus').value == 'null') {
          		addSysMessage("Asset Status is a required field", true);
          	}  else { 
	     	 	var result = jsonrpcClient.AsyncAssetProvider.saveAsset(document.getElementById('assetId').value
	     													, document.getElementById('assetType.code').value
	     													, document.getElementById('assetNo').value
	     													, document.getElementById('assetDescription').value
	     													, document.getElementById('assetStatus').value
															, document.getElementById('assetTemplate').value
	     	     	 										, _data);
	     	 	document.getElementById('assetId').value = result;
	     	 	if(result != null) {
	     	 		addSysMessage("Asset saved successfully", false);
					<%if(request.getParameter("id")!=null){%>
						window.location.href = window.location.href;
					<%}%>
	     	 	}
          	}
      	  }  catch(rpcException) {
          	   if(rpcException != null && rpcException.name != null 
                  	  && rpcException.name == ("org.springframework.dao.DataIntegrityViolationException")) {
	          	 addSysMessage("Unable to save asset. Asset with same name already exists.", true);
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
 </script>
    <br/>
	
	<table width="80%" align="center">
		<tr>
			<td align="center" valign="top"><b>Asset Attributes</b></td>
		</tr>
		<tr><td>&nbsp;</tr>
		<tr>
			<td align="center" valign="top">
				 <div style='background-color:#F2F2F2;border:1px solid #000; margin-left:10px;'>  
				   <div class="bd">
						<table cellpadding="1" cellspacing="0">
							<tr>
							   <td><b>&nbsp;&nbsp;&nbsp;Attribute Type: </b> &nbsp;&nbsp;&nbsp;                 
								  <select id="dAttributeType" style="width: 40;" name="dAttributeType"> 
									<option value="">--Select a Type</option> 
									<c:forEach var="aType" items="${assetAttributeTypes}">       
										<option value="<c:out value="${aType.code}"/>"><c:out value="${aType.code}"/></option>
									</c:forEach>
								  </select><br>
								   <b>&nbsp;&nbsp;&nbsp;Attribute Value:</b>&nbsp;&nbsp;&nbsp;
									<input size="30" id="dAttributeValue" value="" /> 
							   </td>
							   <td>                 
								   &nbsp;&nbsp;&nbsp;<input type="button" id="add" value="&nbsp;ADD&nbsp;" onclick="javascript:doAdd();" /> 
							   </td>
						   </tr>
						</table>
						<hr/>
						<div id="parentcontattributetable" style="height:100%;overflow-y:auto;background-color:#F2F2F2;">
							<div id="contattributetable" align="center"></div> 
						 </div>	
							  
					</div>
				 </div>
			</td>
			<td style="display:none;"align="center" valign="top">
				 <div style='background-color:#F2F2F2;border:1px solid #000; margin-left:10px;'>  
				 <div class="bd">
					<table cellpadding="1" cellspacing="0">
						<tr>
						   <td><b>&nbsp;&nbsp;&nbsp;Attribute Type: </b> &nbsp;&nbsp;&nbsp;                 
							  <select id="dAttributeType" style="width: 40;" name="dAttributeType" disabled="true"> 
								<option value="">--Select a Type</option> 
								<c:forEach var="aType" items="${assetAttributeTypes}">       
									<option value="<c:out value="${aType.code}"/>"><c:out value="${aType.code}"/></option>
								</c:forEach>                                      
							  </select><br>
							   <b>&nbsp;&nbsp;&nbsp;Attribute Value:</b>&nbsp;&nbsp;&nbsp;
								<input size="30" id="dAttributeValue" value="" disabled="true"/> 
						   </td>
						   <td>                 
							   &nbsp;&nbsp;&nbsp;<input type="button" id="add" disabled="true" value="&nbsp;ADD&nbsp;" onclick="javascript:doAdd();" /> 
						   </td>	               
					   </tr>
					</table>
					<hr/>
					<div id="parentconttemplateattributetable" style="height:100%;overflow-y:auto;background-color:#F2F2F2;">
						<div id="conttemplateattributetable" align="center">
					</div> 
				 </div>
				</div>
			 </div>
			</td>
		</tr>
	</table>
  
   	<br/>
   	 <table width="100%" cellpadding="0" cellspacing="0" border="0">
   	 <tr>
   	 		<td colspan="3" align="center">
				<input type = "button" value="&nbsp;Save&nbsp;" onclick="javascript:submitAttributeTable();" />
				&nbsp;&nbsp;&nbsp;<input type = "button" value="&nbsp;Back&nbsp;" onclick="javascript:back();" />
			</td>
	</tr>		
   	 </table>
	<style>
		 .yui-skin-sam .yui-dt table {
			width:98%;	 
		 }
	 </style>
  </tmpl:put>
</tmpl:insert>
