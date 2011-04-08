<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<tmpl:insert template='/common/sitelayout.jsp'>

  <tmpl:put name='title' direct='true'>Add/Edit Delivery Building</tmpl:put>

  <tmpl:put name='content' direct='true'>
  	<br/> 
	<style>
		.forms1 tr td span {
			color: #333333;
			font-size:8pt;
		}
	</style>
    <div align="center">
      <form:form commandName = "deliveryBuildingForm" method="post">
      <form:hidden path="buildingId"/>
      
     
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle">
              Add/Edit Delivery Building
            </td>
          </tr>
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
          </tr>
          
          <tr>
            <td class="screencontent">
              <table class="forms1">          
                <tr>
                  <td>Scrubbed Street</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="srubbedStreet" />
                </td>
                <td>
                  &nbsp;<form:errors path="srubbedStreet" />
                </td>
               </tr>
               
               <tr>
                  <td>City</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="city" />
                </td>
                <td>
                  &nbsp;<form:errors path="city" />
                </td>
               </tr>
               
               <tr>
                  <td>State</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="state" />
                </td>
                <td>
                  &nbsp;<form:errors path="state" />
                </td>
               </tr>
                                           
               <tr>
                  <td>Zipcode</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="zip" />
                </td>
                <td>
                  &nbsp;<form:errors path="zip" />
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
                  <td>Geocode Confidence</td>
                  <td> 
                  <form:select path="geocodeConfidence" disabled="true">
                        <form:option value="null" label="--Please Select Geo Confidence"/>                        
                    <form:options items="${confidencetypes}" itemLabel="description" itemValue="name" />
                   </form:select>
                </td>
                <td>
                  &nbsp;<form:errors path="geocodeConfidence" />
                </td>
               </tr>
               
               <tr>
                  <td>Geocode Quality</td>
                  <td> 
                  <form:select path="geocodeQuality" disabled="true">
                        <form:option value="null" label="--Please Select Geo Quality"/>
                        <form:options items="${qualitytypes}" itemLabel="description" itemValue="name" />
                   </form:select>
                </td>
                <td>
                  &nbsp;<form:errors path="geocodeQuality" />
                </td>
               </tr>
                     
                                             
               <tr>
                  <td>Service Time Type</td>
                  <td> 
                  <form:select path="dlvServiceTimeType" disabled="true">
                        <form:option value="null" label="--Please Select Service Time"/>
                    <form:options items="${servicetimetypes}" itemLabel="name" itemValue="code" />
                   </form:select>
                </td>
                <td>
                  &nbsp;<form:errors path="dlvServiceTimeType" />
                </td>
               </tr>
               <tr>
                  <td>Service Time Override</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="serviceTimeOverride" disabled="true"/>
                </td>
                <td>
                  &nbsp;<form:errors path="serviceTimeOverride" />
                </td>
               </tr>
               
               <tr>
                  <td>Service Time Adjustment</td>
                  <td>                  
                    <form:input maxlength="1" size="1" path="serviceTimeOperator" disabled="true"/>
                    <form:input maxlength="50" size="24" path="serviceTimeAdjustable" disabled="true"/>
                  </td>
                  <td>
                  &nbsp;<form:errors path="serviceTimeAdjustable" />&nbsp;<form:errors path="serviceTimeOperator" />
               	  </td>
               </tr>
				 <tr>
                  <td>Choose Groups</td>
                  <td>
	                  <form:select path="buildingGroups" multiple="multiple">
	                        <form:option value="" label="--N/A--"/>
	                    	<form:options items="${deliveryGroups}" itemLabel="groupName" itemValue="groupId"  />
	                   </form:select>
	               </td>
				   <td>
	                  &nbsp;<form:errors path="buildingGroups" />
		           </td>
               </tr>
			   
              <tr><td colspan="3">&nbsp;</td></tr>
              <tr>
                  <td colspan="3" align="center">
                   <input type = "submit" value="&nbsp;Save&nbsp;"  />
				</td>
              </tr>
              </table>
              
            </td>
          </tr>               
        </table>
               
      </form:form>
     </div>
     
  </tmpl:put>
</tmpl:insert>