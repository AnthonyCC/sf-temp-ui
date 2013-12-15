<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>

<tmpl:insert template='/common/sitelayout.jsp'>

<% 
	String pageTitle = "Asset";	
	String atrValue = request.getParameter("atrValue") != null ? request.getParameter("atrValue") : "";
	String atrName = request.getParameter("atrName") != null ? request.getParameter("atrName") : "";
	
	if(com.freshdirect.transadmin.security.SecurityManager.isUserAdminOrPlanning(request)){
		pageContext.setAttribute("HAS_ADDBUTTON", "true");	
		pageContext.setAttribute("HAS_DELETEBUTTON", "true");
	} else {
		pageContext.setAttribute("HAS_ADDBUTTON", "false");	
		pageContext.setAttribute("HAS_DELETEBUTTON", "false");
	}
%>
  <tmpl:put name='title' direct='true'> Admin : <%=pageTitle%></tmpl:put>
	<tmpl:put name='yui-lib'>
		<%@ include file='/common/i_yui.jspf'%>
	</tmpl:put>
	
  <tmpl:put name='content' direct='true'>
		<div class="MNM002 subsub or_999">
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

  <div class="contentroot">
			
		<div class="cont_topleft">
			<div class="cont_row">				
				<div class="cont_Litem">
					<c:if test="${not empty messages}">
						<div class="err_messages">
							<jsp:include page='/common/messages.jsp'/>
						</div>
					</c:if> 				
					<div class="scrTitle" style="float:left;padding-top:3px"></div>
						<div style="float:left;text-align:center;font-weight:bold;font-size:11px;">Asset Type<br>
							<select id="assetType" name="assetType" onChange="javascript:getAttributeInfo();">
								<option value="null">--Please Select</option>                       	
		                    	<c:forEach var="assetType" items="${assetTypes}">                             
		                          <c:choose>
		                            <c:when test="${param.pAssetType == assetType.code}" > 
		                              <option selected value="<c:out value="${assetType.code}"/>"><c:out value="${assetType.code}"/></option>
		                            </c:when>
		                            <c:otherwise> 
		                              <option value="<c:out value="${assetType.code}"/>"><c:out value="${assetType.code}"/></option>
		                            </c:otherwise> 
		                          </c:choose>      
		                        </c:forEach>  
                   			 </select>
						</div>
						<div style="float:left;text-align:center;font-weight:bold;font-size:11px;">Attribute Type<br>&nbsp;
							 <select id="atrName" name="atrName" >                       	
			                    	<option value="null">--Please select Attribute</option>			                    	
                    		</select>
                    		<input id=attributeTypeId name="attributeTypeId" type="hidden" value="<%= atrName %>"/> 
						</div>&nbsp;
						<div style="float:left;text-align:center;font-weight:bold;font-size:11px;">Attribute Value<br>&nbsp;
							<input maxlength="80" size="20" name="atrValue" id="atrValue" value="<%= atrValue %>" style="width:150px" />
						</div>&nbsp;					
						<div style="float:left;"><br>
	                   	  <span>&nbsp;<input id="view_button" type="image" alt="View" src="./images/icons/view.gif"  onclick="javascript:doCompositeLink('assetType','atrName','atrValue','asset.do');" onmousedown="this.src='./images/icons/view_ON.gif'" /></span>						
	                   </div>
	                   <div style="float:left;font-size:11px;"><br/>
						&nbsp;&nbsp;<input id="attribute_button" type="button" value="Attribute Type" onclick="javascript:showAssetAttributeForm();" />
						&nbsp;<input id="assettype_button" type="button" value="Asset Type" onclick="javascript:showAssetTypeForm();" />
						&nbsp;<input id="assetscanbutton" type="button" value="Scan Asset" onclick="javascript:showAssetScanForm();" />
						&nbsp;<input id="exportassetbtn" type="button" value="Export Asset" onclick="javascript:exportAssetData();" />						
						</div>
	               </div>
			</div>
		</div>

		<div class="cont_topright">
			<div class="cont_row">
				<div class="cont_Ritem">
					  <form id="assetForm" action="" method="post">  
						<ec:table items="assets"   action="${pageContext.request.contextPath}/asset.do"
							imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title=""
							width="99%"  view="fd" form="assetForm" autoIncludeParameters="true" rowsDisplayed="25"  >
							
							<ec:exportPdf fileName="assets.pdf" tooltip="Export PDF" headerTitle="Transportation Assets" />
							<ec:exportXls fileName="assets.xls" tooltip="Export XLS" />
							<ec:exportCsv fileName="assets.csv" tooltip="Export CSV" delimiter="|"/>
								
							<ec:row interceptor="obsoletemarker">
							  <ec:column title=" " width="5px" filterable="false" sortable="false" cell="selectcol" property="assetId" />							           
							  <ec:column property="assetNo" title="Asset No"/>
							  <ec:column property="assetDescription" title="Description"/>
							  <ec:column property="barcode" title="Barcode"/>
							  <ec:column property="assetStatus.description" title="Status" />
							  <ec:column property="domicile" title="Domicile"/>
							  <ec:column property="vendor" title="Vendor"/>
							  <ec:column property="vendorNumber" title="Vendor Number"/>
							  <ec:column property="bodyLength" title="Body Length" />
							</ec:row>
						  </ec:table>
					   </form> 
				</div>
			</div>
		</div>
	</div>
		<script>		
		 	var jsonrpcClient = new JSONRpcClient("asset.ax");
		 	
		 	<%if(com.freshdirect.transadmin.security.SecurityManager.isUserAdminOrPlanning(request)){%> 
				addMultiRowHandlersColumn('ec_table', 'rowMouseOver', 'editasset.do', 'id', 0, 0, 'pAssetType','assetType');
			<% } %>
			
			function getFilterTestValue() {
				var filters = getFilterValue(document.getElementById("assetForm"),false);
				filters+="&pAssetType="+document.getElementById("assetType").value; 	
				filters+="&atrName="+document.getElementById("atrName").value;
			    filters+="&atrValue="+document.getElementById("atrValue").value;
	            return escape(filters);
	       }

			function doCompositeLink() {
				location.href = "asset.do?pAssetType=" + $('#assetType').val()
						+ "&atrName=" + $('#atrName').val() + "&atrValue="
						+ $('#atrValue').val();
			}

			var atrList;
			function getAttributeInfo() {
				// get selected assetType from dropdown list  
				var assetTypeId = $('#assetType option:selected').val();
				if (assetTypeId.length != 0) {
					atrList = jsonrpcClient.AsyncAssetProvider
							.getAttributeType(attributeCallback, assetTypeId);
				}
			}

			function attributeCallback(atrList, exception) {
				if (exception) {
					alert('Unable to connect to host system. Please contact system administrator!');
					return;
				}
				var aName = '<%= atrName %>';
				if (atrList != null) {
					var attributeTypes = $('#atrName');
					$('#atrName')[0].options.length = 0;
					attributeTypes
							.prepend('<option value="">--Please select Attribute</option>');
					for ( var i = 0; i < atrList.list.length; i++) {
						if(atrList.list[i].id.code === aName){
							attributeTypes
							.prepend("<option selected value='"+ atrList.list[i].id.code +"'>"
									+ atrList.list[i].id.code
									+ "</option>");
						} else {
							attributeTypes
								.prepend("<option value='"+ atrList.list[i].id.code +"'>"
										+ atrList.list[i].id.code
										+ "</option>");
						}
					}
				} else {
					alert("Populating atttribute types failed");
				}
			}

			$(document).ready(function() {
				getAttributeInfo();
			});
			
			function doDelete(tableId, url) 
		    {    
				    var paramValues = getParamList(tableId, url);
				    if (paramValues != null) {
				    	var hasConfirmed = confirm ("Do you want to delete the selected records?")
				    	if (hasConfirmed) 
						{
				    		var param1 = document.getElementById("assetType").value;
				     		var param2 = document.getElementById("atrName").value;
				     		var param3 = document.getElementById("atrValue").value;
				            var filters="&pAssetType="+param1+"&atrName="+param2+"&atrValue="+param3;
						  	location.href = url+"?id="+ paramValues+filters;
						} 
				    } else {
				    	alert('Please Select a Row!');
				    }
		    }
			
			function exportAssetData() {			
				   var assetType = document.getElementById("assetType").value;
				   if(assetType == ''){
						alert('Please select asset type to export.');
				   }else{
					   var confirmed = confirm ("You are about to export asset data for "+assetType);
					   if(confirmed){
				   			location.href = "asset.do?export=y&assetType="+assetType;
				   	   }
				   }
			   }
		</script>
		<%@ include file="i_addassetattribute.jspf"%>
		<%@ include file="i_addassettype.jspf"%>
		<%@ include file="i_scanasset.jspf"%>
	</tmpl:put>
</tmpl:insert>
