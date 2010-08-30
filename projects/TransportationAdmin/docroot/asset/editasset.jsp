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
    <tmpl:put name='title' direct='true'> Admin : Asset : Add/Edit Asset</tmpl:put>
  
  <tmpl:put name='content' direct='true'>
    <br/> 
    <div align="center">
      <form:form commandName = "assetForm" method="post">
           
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle">Add/Edit Asset</td>
          </tr>
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
          </tr>
          
          <tr>
            <td class="screencontent">
              <form:hidden path="assetId"/>
              <form:hidden path="assetType.code"/>		
              <table class="forms1">  
                     
               <tr>
                  <td>Asset No</td>
                  <td>                  
                    <form:input maxlength="40" size="40" path="assetNo" />
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
                 
        attributeDataSource = new YAHOO.util.DataSource(assetData.attributeData);        
        attributeDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY; 
        attributeDataSource.responseSchema = {fields: ["attributeType","attributeValue","deleteBtn"] }; 
                            		  
		var attributeColumns =  [ 
			    {key:"attributeType", label:"Attribute Type", sortable:false, width: 250, className:"forms1"}, 
			    {key:"attributeValue", label:"Attribute Value", sortable:false, width: 400, className:"forms1"},
			    {key:'deleteBtn',label:' ',formatter:function(elCell) {
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
     	 	var result = jsonrpcClient.AsyncAssetProvider.saveAsset(document.getElementById('assetId').value
     													, document.getElementById('assetType.code').value
     													, document.getElementById('assetNo').value
     													, document.getElementById('assetDescription').value
     													, document.getElementById('assetStatus').value
     	     	 										, _data);
     	 	document.getElementById('assetId').value = result;
     	 	if(result != null) {
     	 		alert("Asset saved successfully");
     	 	}
      	  }  catch(rpcException) {
				alert("There was a problem in communication to the server. Please try to refresh the browser window!");
		  }
	  
			      	  
     }
 </script>
    <br/>   
   <div style='background-color:#F2F2F2;border:1px solid #000;width: 65%; margin:0 auto;'>  
       <div class="bd">
      	 	<table cellpadding="1" cellspacing="0">
		    	<tr>
                   <td><b>&nbsp;&nbsp;&nbsp;Attribute Type: </b> &nbsp;&nbsp;&nbsp;                 
                      <select id="dAttributeType" style="width: 40;" name="dAttributeType"> 
                      	<option value="">--Select a Type</option> 
                    	<c:forEach var="aType" items="${assettypes}">       
		              		<option value="<c:out value="${aType.code}"/>"><c:out value="${aType.code}"/></option>
			        	</c:forEach>                                      
			          </select>
                   </td>                               
                   <td>                 
	                   <b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Attribute Value:</b>&nbsp;&nbsp;&nbsp;
	                   			<input size="40" id="dAttributeValue" value="" /> 
	               </td>                                      
                  
	               <td>                 
	                   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" id="add" value="&nbsp;ADD&nbsp;" onclick="javascript:doAdd();" /> 
	               </td>	               
               </tr>
            </table>
            <hr/>
            <div id="parentcontattributetable" style="height:250px;overflow-y:auto;background-color:#F2F2F2;">
	      	 	<div id="contattributetable" align="center"></div> 
	      	 </div>	
	      	      
        </div>
   	 </div>
   	<br/>
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
