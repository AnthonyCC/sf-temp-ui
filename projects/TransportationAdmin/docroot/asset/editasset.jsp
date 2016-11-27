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
		<div class="subs MNM002">
			<div class="subs_left">	
				<div class="sub_tableft sub_tabL_MNM002 <% if(request.getParameter("tAssetType")== null) { %>activeL<% } %>">&nbsp;</div>
				<div class="subtab <%if(request.getParameter("tAssetType")== null) { %>activeT<% } %>">
					<div class="minwidth"><!-- --></div>
					<a href="asset.do?pAssetType=TRUCK" class="<% if(request.getParameter("tAssetType")== null) { %>MNM002<% } %>">Asset</a>
				</div>
				<div class="sub_tabright sub_tabR_MNM002 <% if(request.getParameter("tAssetType")== null) { %>activeR<% } %>">&nbsp;</div>
		
				<div class="sub_tableft sub_tabL_MNM002 <% if(request.getParameter("tAssetType")!= null) { %>activeL<% } %>">&nbsp;</div>
				<div class="subtab <%if(request.getParameter("tAssetType")!= null) { %>activeT<% } %>">
					<div class="minwidth"><!-- --></div>
					<a href="assettemplate.do?tAssetType=TRUCK" class="<% if(request.getParameter("tAssetType")!= null) { %>MNM002<% } %>">Asset Template</a>
				</div>
				<div class="sub_tabright sub_tabR_MNM002 <% if(request.getParameter("tAssetType")!= null) { %>activeR<% } %>">&nbsp;</div>
			</div>
		</div>
    <br/><br/><br/><br/>
    <div align="center">
      <form:form commandName="assetForm" method="post">
        
        <table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td class="screentitle">Add/Edit Asset</td>
			</tr>
			<tr><td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td><div id="errContainer" style='margin:0 auto;'></div></tr>
			<tr>
				<td class="screencontent">
				  	<form:hidden path="assetId"/>
					<table style="height:680px;width:99%;border:1px dotted;background-color:#F7F7F7;">
						<tr>
							<td valign="top">
										<table class="forms1">
											<tr>
												<td colspan="3" align="center"><b>Asset Details</b></td>
											</tr>
											<tr>
												<td colspan="3" align="center">&nbsp;</td>
											</tr>
											<tr>
												<td>Asset Type</td>
												<td>
													<form:select path="assetType">
														<form:option value="null" label="--Please Select Asset Type" />
														<form:options items="${assetTypes}" itemLabel="code" itemValue="code" />
													</form:select></td>
												<td>&nbsp;<form:errors path="assetType" />
												</td>
											</tr>

											<tr>
												<td>Asset No</td>
												<td><form:input maxlength="12" size="15" path="assetNo" />
												</td>
												<td>&nbsp;<form:errors path="assetNo" />
												</td>
											</tr>

											<tr>
												<td>Description</td>
												<td><form:input maxlength="40" size="40"
														path="assetDescription" /></td>
												<td>&nbsp;<form:errors path="assetDescription" />
												</td>
											</tr>
											<tr>
												<td>Barcode</td>
												<td><form:input maxlength="256" size="40"
														path="barcode" /></td>
												<td>&nbsp;<form:errors path="barcode" />
												</td>
											</tr>

											<tr>
												<td>Status</td>
												<td><form:select path="assetStatus">
														<form:option value="null"
															label="--Please Select Asset Status" />
														<form:options items="${assetstatuses}"
															itemLabel="description" itemValue="name" />
													</form:select></td>
												<td>&nbsp;<form:errors path="assetStatus" />
												</td>
											</tr>

											<%-- <tr>
												<td>Asset Template</td>
												<td><form:select path="assetTemplate">
														<form:option value="null" label="--Please Select Template" />
														<form:options items="${assetTemplates}"
															itemLabel="assetTemplateName" itemValue="assetTemplateId" />
													</form:select></td>
												<td>&nbsp;<form:errors path="assetTemplate" />
												</td>
											</tr> --%>
											<tr>
												<td colspan="3" align="center">&nbsp;</td>
											</tr>											
											<tr>
												<td colspan="3" align="center"  rowspan="2">
														<input type = "button" value="&nbsp;Save&nbsp;" onclick="javascript:submitAttributeTable();" />
														&nbsp;&nbsp;&nbsp;<input type = "button" value="&nbsp;Back&nbsp;" onclick="javascript:back();" />
												</td>
											</tr>
										</table>
								</td>
								<td align="center" valign="top" >
										<table width="100%">
											<tr>
												<td align="center" valign="top"><b>Asset Attributes</b></td>
											</tr>
											<tr>
												<td align="right">
													<input type="image" src="./images/icons/tick.gif" />&nbsp;Overriden from Template
													<input type="image" src="./images/icons/unique.gif" />&nbsp;Unique to Asset
												</td>
											</tr>
											<tr>
												<td>&nbsp;</td>
											</tr>
											<tr>
												<td align="center" valign="top">
															  <div class="bd">
															<div id="parentcontattributetable"
																style="height: 650px; overflow-y: auto; background-color: #F2F2F2;">
																<div id="contattributetable" align="center"></div>
															</div>
															</div>
												</td>
											</tr>
										</table>
									</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>	
      
      </form:form>
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
	  
	  var myDynamicEditor;
	  
      YAHOO.util.Event.addListener(window, "load", function() {
               
        attributeDataSource = new YAHOO.util.DataSource(assetData.attributeData);        
        attributeDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY; 
        attributeDataSource.responseSchema = {fields: ["attributeType","attributeValue","attributeMatch","deleteBtn"] };
        
        myDynamicEditor = new YAHOO.widget.TextboxCellEditor({multiple:true});
        			
        			// Custom sort handler to sort by attribute name and then by attribute value 
        	        // where a and b are Record instances to compare 
        	        var sortAttributes = function(a, b, desc) { 
        	            // Deal with empty values 
        	            if(!YAHOO.lang.isValue(a)) { 
        	                return (!YAHOO.lang.isValue(b)) ? 1 : 0; 
        	            } 
        	            else if(!YAHOO.lang.isValue(b)) { 
        	                return -1; 
        	            } 
        	 
        	            // First compare by state 
        	            var comp = YAHOO.util.Sort.compare; 
        	            return comp(a.getData("attributeType"), b.getData("attributeType"), desc); 
        	 
        	            // If states are equal, then compare by areacode 
        	           // return (compState !== 0) ? compState : comp(a.getData("attributeType"), b.getData("attributeType"), desc); 
        	        }; 
        	        
        	       
		var attributeColumns =  [ 
			    {key:"attributeType", label:"Attribute Type", sortable:true, className:"forms1"}, 
			    {key:"attributeValue", label:"Attribute Value", sortable:true, className:"forms1", editor: myDynamicEditor},
				{key:'attributeMatch',label:'Is Overriden?',formatter:function(elCell, oRecord, oColumn, oData) {
												if(oData === 'O'){
        											elCell.innerHTML = '<img src="images/icons/tick.gif" title="Overriden" />';
        										} else if(oData === 'U'){
													elCell.innerHTML = '<img src="images/icons/unique.gif" title="Unique" />';
												}
    			}}
		 ];
			 
		attributeDataTable = new YAHOO.widget.DataTable("contattributetable"
															, attributeColumns
																	, attributeDataSource
																			);
		
		// Set up editing flow 
		var highlightEditableCell = function(oArgs) { 
		      var elCell = oArgs.target; 
		         if(YAHOO.util.Dom.hasClass(elCell, "yui-dt-editable")) { 
		             this.highlightCell(elCell); 
		         } 
		      }; 
		      
		
		attributeDataTable.subscribe("cellMouseoverEvent", highlightEditableCell); 
		attributeDataTable.subscribe("cellMouseoutEvent", attributeDataTable.onEventUnhighlightCell); 
		attributeDataTable.subscribe('cellClickEvent', attributeDataTable.onEventShowCellEditor);

	
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
			        rowsPerPage    : 15
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
      	  	for(i=0; i < records.length; i++) {
      	  		_data[i] = new Array();
      	  		_data[i][0] = records[i].getData('attributeType');
      	  		_data[i][1] = records[i].getData('attributeValue');
      	  		if(records[i].getData('attributeValue') == ''){
      	  			addSysMessage("Attribute value cannot be empty. Should have a value or UNKNOWN", true);
      	  			return;
      	  		}
      	  	}
      	  }
      	  try { 
          	if(document.getElementById('assetNo').value == null || document.getElementById('assetNo').value.length == 0) {
              	addSysMessage("Asset no is a required field", true);
          	} else if(document.getElementById('assetNo').value == null || document.getElementById('assetDescription').value.length == 0) {
          		addSysMessage("Asset Description is a required field", true);
          	} else if(document.getElementById('barcode').value == null || document.getElementById('barcode').value.length == 0) {
          		addSysMessage("Asset barcode is a required field", true);
          	} else if(document.getElementById('assetStatus').value == null 
                  	|| document.getElementById('assetStatus').value.length == 0
                  		|| document.getElementById('assetStatus').value == 'null') {
          		addSysMessage("Asset Status is a required field", true);
          	}  else { 
	     	 	var result = jsonrpcClient.AsyncAssetProvider.saveAsset(document.getElementById('assetId').value
	     													, document.getElementById('assetType').value
	     													, document.getElementById('assetNo').value
	     													, document.getElementById('assetDescription').value
	     													, document.getElementById('assetStatus').value
															, null															
	     	     	 										, _data
	     	     	 										, document.getElementById('barcode').value);
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
    
    function back()
    {
      	var filters = unescape(getParameter("filter"));      	
      	var params = filters.split("&");
      	var assetForm = document.forms["asset"];
      	for(var i=0;i < params.length;i++)
      	{
      		var param = params[i].split("=");         				
      		add_input(assetForm,"hidden",param[0],param[1]);
      	}     	      	
      	assetForm.submit();
    }
 
 </script>
   	<style>
		 .yui-skin-sam .yui-dt table {
			width:98%;	 
		 }
	 </style>
  </tmpl:put>
</tmpl:insert>
<form name="asset" action="asset.do" method="post">  </form>