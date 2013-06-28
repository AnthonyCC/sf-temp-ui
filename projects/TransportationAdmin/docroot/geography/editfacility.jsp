<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'> Geography : Facility : Add/Edit Facility</tmpl:put>

	<tmpl:put name='content' direct='true'>
		<br/>	
		<div align="center">
			<form:form commandName = "facilityForm" method="post">
			
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="screentitle">Add/Edit Facility</td>
					</tr>
					<tr>
						<td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
					</tr>
					
					<tr>
						<td class="screencontent">
                        <table class="forms1">
							  <tr>
                              <td>Facility Name</td>
							    <td>
							  	 	<form:input maxlength="50" size="30" path="name" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="name" />
							 	</td>
							 </tr>
							 
							 <tr>
							    <td>Facility Description</td>
							    <td> 								  
							  	 	<form:input maxlength="50" size="30" path="description" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="description" />
							 	</td>
							 </tr>  
							 <tr>
								<td>Facility Type</td>
								<td> 
									<form:select path="trnFacilityType" onChange="showRoutingSelection(this);">
										<form:option value="" label="--Please Select Facility Type"/>
										<form:options items="${facilityTypes}" itemLabel="name" itemValue="name" />
									</form:select> 
								</td>
								<td><form:errors path="trnFacilityType" />&nbsp;</td>
							</tr>
							 <tr>
							    <td>Routing Code</td>
							    <td> 								  
							  	 	<form:input maxlength="3" size="30" path="routingCode" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="routingCode" />
							 	</td>
							 </tr>
							 <tr>
								<td>Facility Location</td>
								<td> 
									<form:select path="facilityLocation" >
										<form:option value="" label="--Please Select Facility Location"/>
										<form:options items="${facilityLocations}" itemLabel="code" itemValue="code" />
									</form:select> 
								</td>
								<td><form:errors path="facilityLocation" />&nbsp;</td>
							 </tr> 
							  <tr>
							    <td>Prefix</td>
							    <td> 
							  	 	<form:input maxlength="1" size="30" path="prefix" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="prefix" />
							 	</td>
							 </tr>
							 <tr>
				                  <td>Latitude</td>
				                  <td>                  
				                    <form:input maxlength="50" size="30" path="latitude" />
				                </td>
				                <td>
				                  &nbsp;<form:errors path="latitude" />
				                </td>
				             </tr>
				              <tr>
				                  <td>Longitude</td>
				                  <td>                  
				                    <form:input maxlength="50" size="30" path="longitude" />
				                  </td>
				                  <td>
				                   &nbsp;<form:errors path="longitude" />
				                  </td>
				              </tr> 
							  <tr>
							    <td>From Time</td>
							    <td> 								  
							  	 	<form:input maxlength="50" size="30" path="leadFromTime" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="leadFromTime" />
							 	</td>
							 </tr> 
							  <tr>
							    <td>To Time</td>
							    <td> 								  
							  	 	<form:input maxlength="50" size="30" path="leadToTime" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="leadToTime" />
							 	</td>
							 </tr> 
							  <tr>
							    <td>TOD Restriction</td>
							    <td> 								  
							  	 	<form:input maxlength="50" size="50" path="todrestriction" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="todrestriction" />
							 	</td>
							 </tr> 
							 
                            <tr><td colspan="3">&nbsp;</td></tr>
							<tr>
							    <td colspan="3" align="center">
								   <input type = "submit" value="&nbsp;Save&nbsp;"  />
								   <input type = "button" value="&nbsp;Back&nbsp;" onclick="javascript:back();" />
								</td>
							</tr>
							</table>
						</td>
					</tr>
				</table>
			</form:form>
		 </div>
		 <script language="javascript">
		 	showRoutingSelection(document.getElementById("trnFacilityType"));

		  	function showRoutingSelection(refVar) {
				var ref = refVar.value || '';
				if( ref === 'SIT'){
					document.getElementById("routingCode").value="";
					document.getElementById("prefix").value="";
					document.getElementById("routingCode").disabled=true;
					document.getElementById("prefix").disabled=true;
				}else{
					document.getElementById("routingCode").disabled=false;
					document.getElementById("prefix").disabled=false;
				}
		  	} 

		  	function back()
		    {
		      	var filters = unescape(getParameter("filter"));
		      	var params = filters.split("&");
		      	var trnFacilityForm = document.forms["facility"];
		      	for(var i=0;i<params.length;i++)
		      	{
		      		var param = params[i].split("=");
		      		add_input(trnFacilityForm, "hidden",param[0],param[1]);
		      	}     	      	
		      	trnFacilityForm.submit();
		    }       
		</script>
	</tmpl:put>
</tmpl:insert>
<form name="facility" action="facility.do" method="post">  </form>