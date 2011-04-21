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
    <tmpl:put name='title' direct='true'> Asset : Add/Edit Template for Asset (<%= request.getParameter("tAssetType") %>)</tmpl:put>
  
  <tmpl:put name='content' direct='true'>
   <div class="subs MNM004">
		<div class="subs_left">	
			<div class="sub_tableft sub_tabL_MNM004 <% if(request.getParameter("pAssetType")!= null) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <%if(request.getParameter("pAssetType")!= null) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="asset.do?pAssetType=GPS" class="<% if(request.getParameter("pAssetType")!= null) { %>MNM004<% } %>">Asset</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM004 <% if(request.getParameter("pAssetType")!= null) { %>activeR<% } %>">&nbsp;</div>
		
			<div class="sub_tableft sub_tabL_MNM004 <% if(request.getParameter("tAssetType")!= null || request.getParameter("assetType")!= null) { %>activeL<% } %>">&nbsp;</div>
				<div class="subtab <%if(request.getParameter("tAssetType")!= null || request.getParameter("assetType")!= null) { %>activeT<% } %>">
					<div class="minwidth"><!-- --></div>
					<a href="assettemplate.do?tAssetType=GPS" class="<% if(request.getParameter("tAssetType")!= null || request.getParameter("assetType")!= null) { %>MNM004<% } %>">Asset Template</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM004 <% if(request.getParameter("tAssetType")!= null || request.getParameter("assetType")!= null) { %>activeR<% } %>">&nbsp;</div>
		</div>
	  </div>
	<br/> <br/> <br/> <br/> 
    <div align="center">
      <form:form commandName="assetTemplateForm" method="post">
           
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle">Add/Edit Asset Template</td>
          </tr>
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/><div id="errContainer" style='margin:0 auto;'></div></td>
          </tr>
          
          <tr>
            <td class="screencontent">
              <form:hidden path="assetTemplateId"/>
              	
              <table class="forms1">  
     			<tr>
                  <td>Asset Type</td>
                  <td>                  
                    <form:input readOnly="true" maxlength="15" size="15" path="assetType.code" />
                </td>
                <td>
                  &nbsp;<form:errors path="assetType" />
                </td>
               </tr>
				<tr>
                  <td>Template Name</td>
                  <td>                  
                    <form:input maxlength="40" size="40" path="assetTemplateName" />
                </td>
                <td>
                  &nbsp;<form:errors path="assetTemplateName" />
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
      
	  var assetTemplateData = { 
            templateAttributeData: [
  	    <% 
            		AssetTemplate coreAssetTemplate = (AssetTemplate)request.getAttribute("assetTemplateForm");
            		StringBuffer dataBuffer = new StringBuffer();
        			if(coreAssetTemplate != null) {
        				if(coreAssetTemplate.getAssetTemplateAttributes() != null) {
        					Iterator _itr = coreAssetTemplate.getAssetTemplateAttributes().iterator();
        					while(_itr.hasNext()) {
        						AssetTemplateAttribute _attribute = (AssetTemplateAttribute)_itr.next();
        						if(_attribute != null) {
        							AssetTemplateAttributeId _attrId = _attribute.getId();
        							if(_attrId != null) {
        								dataBuffer.append("{attributeType:\"").append(_attrId.getAttributeType()).append("\",");
        								dataBuffer.append("attributeValue:\"").append(_attribute.getAttributeValue()).append("\",");
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
           
      YAHOO.util.Event.addListener(window, "load", function() {
                 
        attributeDataSource = new YAHOO.util.DataSource(assetTemplateData.templateAttributeData);
        attributeDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY; 
        attributeDataSource.responseSchema = {fields: ["attributeType","attributeValue","deleteBtn"] }; 
                            		  
		var attributeColumns =  [ 
			    {key:"attributeType", label:"Attribute Type", sortable:false, width: 250, className:"forms1"}, 
			    {key:"attributeValue", label:"Attribute Value", sortable:false, width: 400, className:"forms1"},
			    {key:'deleteBtn',label:'Delete',formatter:function(elCell) {
        										elCell.innerHTML = '<img src="images/icons/delete.gif" title="delete row" />';
        										elCell.style.cursor = 'pointer';
    			}}
		 ];
				 	 
		attributeDataTable = new YAHOO.widget.DataTable("contattributetable"
															, attributeColumns
																	, attributeDataSource
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
     
     function doAdd() {
     		var _attributeType = document.getElementById('dAttributeType').value;
     		var _attributeValue = document.getElementById('dAttributeValue').value;
     		
     		if(_attributeType.trim().length > 0 && _attributeValue.trim().length > 0) {
     			attributeDataTable.addRow({attributeType:_attributeType, attributeValue:_attributeValue, deleteBtn:''});
     		} else {
     			addSysMessage("Please enter the required values Attribute Type & Attribute Value!", true);
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
          	if(document.getElementById('assetTemplateName').value == null || document.getElementById('assetTemplateName').value.length == 0) {
          		addSysMessage("Asset Template name is required", true);
          	} else { 
	     	 	var result = jsonrpcClient.AsyncAssetProvider.saveAssetTemplate(document.getElementById('assetTemplateId').value
															, document.getElementById('assetType.code').value
															, document.getElementById('assetTemplateName').value
	     													, _data);
	     	 	document.getElementById('assetTemplateId').value = result;
	     	 	if(result != null) {
					addSysMessage("Asset Template added successfully", false);
	     	 	}
          	}
      	  }  catch(rpcException) {
          	  if(rpcException != null && rpcException.name != null 
                  	  && rpcException.name == ("org.springframework.dao.DataIntegrityViolationException")) {
	          	 addSysMessage("Unable to save asset template. Asset Template with same name already exists.", true);
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

   <br/><br/>
   <div style='background-color:#F2F2F2;border:1px solid #000;width: 60%; margin:0 auto;'>  
       <div class="bd">
      	 	<table cellpadding="1" cellspacing="0">
		    	<tr>
                   <td><b>&nbsp;&nbsp;&nbsp;&nbsp;Attribute Type: </b> &nbsp;&nbsp;&nbsp;                 
                      <select id="dAttributeType" style="width: 40;" name="dAttributeType"> 
                      	<option value="">--Select a Type</option> 
                    	<c:forEach var="aType" items="${assetAttributeTypes}">       
		              		<option value="<c:out value="${aType.code}"/>"><c:out value="${aType.code}"/></option>
			        	</c:forEach>                                      
			          </select>
                   </td>                               
                   <td>                 
	                   <b>&nbsp;&nbsp;&nbsp;&nbsp;Attribute Value:</b>&nbsp;&nbsp;&nbsp;
	                   			<input size="40" id="dAttributeValue" value="" /> 
	               </td>                                      
                  
	               <td>                 
	                   &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" id="add" value="&nbsp;ADD&nbsp;" onclick="javascript:doAdd();" /> 
	               </td>
               </tr>
            </table>
            <hr/>
            <div id="parentcontattributetable" style="height:100%;overflow-y:auto;background-color:#F2F2F2;">
	      	 	<div id="contattributetable" align="center"></div> <br/>
	      	 </div>	
	      	      
        </div>
   	 </div>
   	 <br/> <br/>
   	 <table width="100%" cellpadding="0" cellspacing="0" border="0">
   	 <tr>
   	 		<td colspan="3" align="center">
				<input type = "button" value="&nbsp;Save&nbsp;" onclick="javascript:submitAttributeTable();" />
				&nbsp;&nbsp;&nbsp;<input type = "button" value="&nbsp;Back&nbsp;" onclick="javascript:back();" />
			</td>
	</tr>		
   	 </table>
  </tmpl:put>
</tmpl:insert>
