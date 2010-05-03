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
  String expansionType = request.getParameter("type");
  String zoneWorkTable = request.getParameter("zoneWorkTable");
  List apperrors = (List) request.getSession().getAttribute("apperrors");
  //System.out.println("App errors >>"+apperrors);
  boolean errors=false;
 
  System.out.println("expansionType >>"+expansionType);
  String pageTitle = "8 Day Rollout";
	if(!"rollout".equalsIgnoreCase(expansionType)) { 
		pageTitle = "Zone Expansion";
	}
%>

<tmpl:insert template='/common/sitelayout.jsp'>
<tmpl:put name='yui-lib'>
	<%@ include file='/common/i_yui.jspf'%>	
</tmpl:put>	
	<tmpl:put name='title' direct='true'>Zone Expansion</tmpl:put>
	
  	<tmpl:put name='content' direct='true'>
		<script>
			var jsonrpcClient = new JSONRpcClient("geographyprovider.ax");
			 function doExpansion() {          
				 var selectedZones = new Array();
				 var formElem = document.getElementById("ec_table");

				 var worktable= document.getElementById('zoneWorkTable').value;

				 var deliveryFee = document.getElementById('deliveryFee').value;

				 var expansionType = document.getElementById('type').value;
					
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
		    	  var result = jsonrpcClient.AsyncGeographyProvider.doZoneExpansion(doExpansionCallback, worktable, selectedZones, deliveryFee, expansionType); 
		    	  
		      }
		      
		      function doExpansionCallback(result, exception) {		      	  
		          if(exception) {		       
		              alert('Unable to connect to host system. Please contact system administrator!');              
		       
		          }
		          if(!result){
			          alert('Expansion Unsuccessfull!!');
		          }else{
			          alert('Zone Expansion Successfull!! Please go to DlvAdmin to add Timeslots to test & hit the Rollout timeslots button.');
		          }
		      }


		      
		      function generateTimeslotsCallback(result, exception) {		      	  
		    	  if(exception) {		       
		              alert('Unable to connect to host system. Please contact system administrator!');              
		       
		          }
		          if(!result){
			          alert('Rollback Unsuccessfull!!');
		          }else{
			          alert('Rollback Successfull !!');
		          }
		      }

		      function doGenerateTimeSlots() {     
		    	  var selectedZones = new Array();
					 var formElem = document.getElementById("ec_table");

					 var worktable= document.getElementById('zoneWorkTable').value;

					 var deliveryFee = document.getElementById('deliveryFee').value;

					 var expansionType = document.getElementById('type').value;
						
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
			    	  var result = jsonrpcClient.AsyncGeographyProvider.generateTimeslots(generateTimeslotsCallback, selectedZones, worktable); 
			    	  	 
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
					 document.getElementById('expansionButton').style.display='';
					 document.getElementById('generateTimeslots').style.display='';
				 }else{
				  	 document.getElementById('expansionButton').style.display='none';
				   	 document.getElementById('generateTimeslots').style.display='none';
				 }
			 }
		     		 
		      
		</script>
		<div class="cont_topleft">
			<div class="cont_row">
				<div class="cont_Litem">
						<span class="scrTitle">
							<%if(request.getAttribute("rightEnvironment")!=null && (true==(Boolean)request.getAttribute("rightEnvironment"))){ 
							
								if(request.getAttribute("environment")!=null){ %>
									<%if("DEV".equalsIgnoreCase((String)request.getAttribute("environment"))){%>
									   DEV Environment
									<%}else if("STAGE".equalsIgnoreCase((String)request.getAttribute("environment"))){ %>
										STAGE Environment
									<%}else if("PROD".equalsIgnoreCase((String)request.getAttribute("environment"))){ %>
										PROD Environment   
							<%} } }%>				
						</span>
				</div>
			</div>
		</div>
		
		<div align="center">
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<br><br>
					<tr>
				         <td class="screentitle">
				             Zone Expansion             
				         </td>
			        </tr> 
			</table>
		</div>
			
		<div align="center">
				<form:form commandName = "zoneExpansionForm" method="post">
						<table width="50%" cellpadding="0" cellspacing="0" border="0">
									  <tr>
            								<td class="screenmessages" colspan="2"><jsp:include page='/common/messages.jsp'/></td>            
          							  </tr>
									  
									  <tr><td>&nbsp;</td></tr>
									  <tr><td class="screentitle" colspan="2"><b>Work Table Selection</b></td></tr>	
							          <tr><td>&nbsp;</td></tr>
							          <tr>
											<td align="right"><b>Please select Zone WorkTable: &nbsp;&nbsp; </b></td>
											<td>
												<form:select path="zoneWorkTable">
							                    	<form:option value="null" label="--Please Select Zone WorkTable--"/>
							                    	<form:options items="${zoneWorkTables}"/>
							                    </form:select>
						                   	</td>
						                   	<td><form:errors path="zoneWorkTable" /></td>
											
									  </tr>
									  <tr><td>&nbsp;</td></tr>
									  <tr><td align="right"><b>Please Select a Expansion Type:&nbsp;&nbsp;&nbsp;</b></td>
									  	  <td>
									  	  		<form:radiobutton id="type" path="type" value="rollout"/>8 Day Rollout
									  	  	    <form:radiobutton id="type" path="type" value="zExpansion"/>Zone Expansion
									  	  </td>
									  	  <td>
									  	  		<form:errors path="type" />
									  	  </td>
									  	  
									  </tr>
									  <tr><td>&nbsp;</td></tr>
									  <tr>
									  	  <%if("true".equals(request.getAttribute("secondView"))){ %>
									   		
									  	  <td align="right"><b>Please enter Delivery fee:&nbsp;&nbsp;&nbsp;</b></td>
									  	  <td>								  
									  	  		<form:input path="deliveryFee" ></form:input>
									  	  </td>
									  	  <td>
									  	  		<form:errors path="deliveryFee" />
									  	  </td>
									  	  <% }%>
									  </tr>
									  <tr><td><br></td></tr>
									  <tr>
									  		<td></td>
								    		<td  align="left">								    		
									   			<input id="submit" type ="submit" value="&nbsp;Submit &nbsp;" />
									   		</td>
												
								 	  </tr>
								</table>
							
				</form:form>
		</div>
	<!--
		 Content display		
	-->
	 <%if("rollout".equalsIgnoreCase(expansionType) && !"null".equalsIgnoreCase(zoneWorkTable)) { %>
	 	<br/>
		<form:form commandName="zoneExpansionForm" action="" method="post">
				
			<div class="contentroot">
				
				<div class="cont_topleft">
					<div class="cont_row">
						<div class="cont_Litem">
							<span class="scrTitle">
								<%=pageTitle%>
							</span>
						</div>
					</div>
				</div>
				<div class="cont_row">
					<span class="scrTitle"></span>
				</div>
				<table width="95%" cellpadding="0" cellspacing="0" border="0">
					<tr>
					
						<td align="right" width="90%">
							<input id="expansionButton" type ="button" value="&nbsp;Expansion &nbsp;" onClick="javascript:doExpansion()" style="display:none;" />&nbsp;
						</td>
						<%if(request.getAttribute("rightEnvironment")!=null && (true==(Boolean)request.getAttribute("rightEnvironment"))&& request.getAttribute("environment")!=null && "DEV".equalsIgnoreCase((String)request.getAttribute("environment"))){ %>
						<td align="right">
							<input id="generateTimeslots" type ="button" value="&nbsp;Rollback Timeslots &nbsp;" onClick="javascript:doGenerateTimeSlots()" style="display:none;"/>
						</td>
						<%}%>
					</tr>						
				</table>					
				
				<div class="cont_topright">
					<div class="cont_row">
						<div class="cont_Ritem">
									<input id="expansionButton" type ="button" value="&nbsp;Expansion &nbsp;" onClick="javascript:doExpansion()" style="display:none;" />						
									<ec:table items="zones" action="${pageContext.request.contextPath}/zoneexpansion.do"
									imagePath="${pageContext.request.contextPath}/images/table/*.gif" title=""
									width="98%" view="fd" form="zoneExpansionForm" autoIncludeParameters="false" showPagination="false" showExports="false" 
		            				 sortable="false">
									
									<ec:exportPdf fileName="zonerollout.pdf" tooltip="Export PDF" 
											  headerTitle="8 Day Rollout" />
									<ec:exportXls fileName="zonerollout.xls" tooltip="Export PDF" />
									<ec:exportCsv fileName="zonerollout.csv" tooltip="Export CSV" delimiter="|"/>
									   <ec:row interceptor="obsoletemarker">
											  <ec:column title=" " width="2px" 
														filtercell="selectcol" sortable="false" alias="zonecode" property="code" cell="fdzoneexpansionselcol"/>
											  <ec:column property="code" title="Zone Code"/>
									   		  <ec:column property="name" title="Name"/>
									   		  <ec:column property="newZone" title="IsNew" />
									   		  <ec:column property="commonInboth" title="IsCommon" />
									   		  <ec:column property="zoneTblOnly" title="IsZoneOnly" />
									   </ec:row>
									
									</ec:table>
								
								 <input type="hidden" name="zoneWorkTable" value="<%= request.getAttribute("zoneWorkTable") %>">           
					   			 <input type="hidden" name="type" value="<%= request.getAttribute("type") %>">
								 <!--<input type="hidden" name="cbsVals" value="">-->
						</div>
					</div>
				</div>
				
			</div>
		</form:form>
	 
	 
	 <% } else if("zExpansion".equalsIgnoreCase(expansionType) && !"null".equalsIgnoreCase(zoneWorkTable)) { %>
	 	
	 		<br/>
	 		<form:form commandName="zoneExpansionForm" action="" method="post">
		
	 		<div class="contentroot">
			
				<div class="cont_topleft">
					<div class="cont_row">
						<div class="cont_Litem">
							<span class="scrTitle">
								<%=pageTitle%>
							</span>		
							
						</div>
					</div>
				</div>
				<div class="cont_row">
					<span class="scrTitle"></span>
				</div>
				
				<div class="cont_topright">
					<div class="cont_row">
						<div class="cont_Ritem">
													
									<ec:table items="zones" action="${pageContext.request.contextPath}/zoneexpansion.do"
									imagePath="${pageContext.request.contextPath}/images/table/*.gif" title=""
									width="98%" view="fd" form="zoneExpansionForm" autoIncludeParameters="false" showPagination="false" showExports="false" 
		            				 sortable="false">
									
									   <ec:row interceptor="obsoletemarker">
											  <ec:column title=" " width="2px" 
														filtercell="selectcol" sortable="false" alias="zonecode" property="code" cell="fdzoneexpansionselcol"/>
											  <ec:column property="code" title="Zone Code"/>
									   		  <ec:column property="name" title="Name"/>
									   		  <ec:column property="commonInboth" title="IsCommon"/>
									   </ec:row>
									
									</ec:table>
								
								 <input type="hidden" name="zoneWorkTable" value="<%= request.getAttribute("zoneWorkTable") %>">           
					   			 <input type="hidden" name="type" value="<%= request.getAttribute("type") %>">
								 <!--<input type="hidden" name="cbsVals" value="">	-->
							</div>
					</div>
				</div>
				
			</div>
		</form:form>
	 <%} %>	
	</tmpl:put>
</tmpl:insert>