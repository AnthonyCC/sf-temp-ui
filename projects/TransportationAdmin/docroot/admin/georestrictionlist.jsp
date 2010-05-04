<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import='com.freshdirect.common.customer.*' %>
<%@ page import='java.util.*' %>


<%    
  pageContext.setAttribute("HAS_ADDBUTTON", "false");
  pageContext.setAttribute("HAS_DELETEBUTTON", "false");
  
  String pageTitle = "List of Geo Restrictions";
	
%>

<tmpl:insert template='/common/sitelayout.jsp'>
<tmpl:put name='yui-lib'>
	<%@ include file='/common/i_yui.jspf'%>	
</tmpl:put>	
	<tmpl:put name='title' direct='true'>Geo Restrictions</tmpl:put>
	
  	<tmpl:put name='content' direct='true'>
		<script>
			var jsonrpcClient = new JSONRpcClient("geographyprovider.ax");

			 function doGeoRestrictions() {          
				 var selectedZones = new Array();
				 var formElem = document.getElementById("ec_table");

				 var cbs = formElem.getElementsByTagName('input');				 	
				 var cnt = 0;
				 var checked=false;
				for (var i=0; i<cbs.length; i++) {
					if (cbs[i].type == 'checkbox' && cbs[i].checked) {
						//alert('FFFFFFFFFF');
						//checked=true;
						selectedZones[cnt] = new Array();
						selectedZones[cnt][0] = cbs[i].name;
						selectedZones[cnt][1] = "X";
						cnt++;					
					}
					//alert('Please Select a Row!');
				 }         
		          //alert(selectedZones+' -> '+selectedZones.length);
		    	  var result = jsonrpcClient.AsyncGeographyProvider.doGeoRestriction(doGeoRestrictionCallback, selectedZones); 
		    	  
		      }
		      
		      function doGeoRestrictionCallback(result, exception) {		      	  
		          if(exception) {		       
		              alert('Unable to connect to host system. Please contact system administrator!');              
		       
		          }
		          if(!result){
			          alert('Adding or updating Geo Restrictions Unsuccessfull!!');
		          }else{
			          alert('Adding or updating Geo Restrictions Successfull!!');
		          }
		      }


		      function showExpansion(){
				 var formElem = document.getElementById("ec_table");
			
				 var cbs = formElem.getElementsByTagName('input');
				 var checked=false;
				 for (var i=0; i<cbs.length; i++) {		
					if(cbs[i].type=='checkbox' && cbs[i].checked){
						checked=true;
						break;
					}
				 }
				 if(checked){
					 document.getElementById('submitButton').style.display='';					 
				 }else{
				  	 document.getElementById('submitButton').style.display='none';				   	
				 }
			 }
		     		 
		      
		</script>
		
		<div align="center">
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<br><br>
					<tr>
				         <td class="screentitle">
				             List of Geo Restrictions             
				         </td>
			        </tr> 
			</table>
		</div>
		<br>
		<table width="98%" cellpadding="0" cellspacing="0" border="0">
				<tr>
				     <td class="scrTitle" align="right">		
							<%if(request.getAttribute("rightEnvironment")!=null && (true==(Boolean)request.getAttribute("rightEnvironment"))){ 
							
								if(request.getAttribute("environment")!=null){ %>
									<%if("DEV".equalsIgnoreCase((String)request.getAttribute("environment"))){%>
									   DEV Environment
									<%}else if("STAGE".equalsIgnoreCase((String)request.getAttribute("environment"))){ %>
										STAGE Environment
									<%}else if("PROD".equalsIgnoreCase((String)request.getAttribute("environment"))){ %>
										PROD Environment   
							<%} } }%>				
					</td>
				</tr>
		</table>
	<!--
		 Content display		
	-->
		
	 		
				<div class="cont_row">
					<span class="scrTitle"></span>
				</div>
				<table width="52%" cellpadding="0" cellspacing="0" border="0">
								<tr>
									<td align="right" width="90%">
										<input id="submitButton" type ="button" value="&nbsp; Submit &nbsp;" onClick="javascript:doGeoRestrictions()" style="display:none;" />&nbsp;
									</td>
								</tr>
				</table>
				<div class="cont_topright">
					<div class="cont_row">
						<div class="cont_Ritem">
													
									<ec:table items="zones" action="${pageContext.request.contextPath}/geographyrestriction.do"
									imagePath="${pageContext.request.contextPath}/images/table/*.gif" title=""
									width="98%" view="fd" autoIncludeParameters="false" showPagination="false" showExports="false" 
		            				 sortable="false">
									
									   <ec:row interceptor="obsoletemarker">
											  <ec:column title=" " width="2px" 
														filtercell="selectcol" sortable="false" alias="zonecode" property="code" cell="fdzoneexpansionselcol"/>
											  <ec:column property="code" title="Zone Code"/>
									   		  <ec:column property="name" title="Name"/>
									   		  <ec:column property="newRestriction" title="IsNewRestriction"/>
									   		  <ec:column property="commonInboth" title="IsCommon"/>
									   		  <ec:column property="boundaryTblOnly" title="IsBoundaryTblOnly"/>
									   </ec:row>
									
									</ec:table>
								
								
							</div>
					</div>
				</div>
				
			</div>
		
	 
	</tmpl:put>
</tmpl:insert>