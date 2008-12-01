<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Edit Delivery Location</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/> 
    <div align="center">
      <form:form commandName = "deliveryLocationForm" method="post">
      <form:hidden path="locationId"/>
      <form:hidden path="buildingId"/>
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle">Edit Delivery Location</td>
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
                    <form:input disabled="true" maxlength="50" size="30" path="building.srubbedStreet" />
                </td>
                <td>
                  &nbsp;<form:errors path="building.srubbedStreet" />
                </td>
               </tr>
               
               <tr>
                  <td>Apartment</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="apartment" />
                </td>
                <td>
                  &nbsp;<form:errors path="apartment" />
                </td>
               </tr>
               
               <tr>
                  <td>City</td>
                  <td>                  
                    <form:input disabled="true" maxlength="50" size="30" path="building.city" />
                </td>
                <td>
                  &nbsp;<form:errors path="building.city" />
                </td>
               </tr>
                                             
               <tr>
                  <td>State</td>
                  <td> 
                  <form:select disabled="true" path="building.state">
                        <form:option value="null" label="--Please Select State"/>
                    <form:options items="${states}" />
                   </form:select>
                </td>
                <td>
                  &nbsp;<form:errors path="building.state" />
                </td>
               </tr>
                             
               <tr>
                  <td>Zipcode</td>
                  <td>                  
                    <form:input disabled="true" maxlength="50" size="30" path="building.zip" />
                </td>
                <td>
                  &nbsp;<form:errors path="building.zip" />
                </td>
               </tr>
                                           
               <tr>
                  <td>Latitude</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="latitude" disabled="true"/>
                </td>
                <td>
                  &nbsp;<form:errors path="latitude" />
                </td>
               </tr>
               
               <tr>
                  <td>Longitude</td>
                  <td>                  
                    <form:input maxlength="50" size="30" path="longitude" disabled="true" />
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
                  <form:select path="dlvServiceTimeType">
                        <form:option value="null" label="--Please Select Service Time"/>
                    <form:options items="${servicetimetypes}" itemLabel="name" itemValue="code" />
                   </form:select>
                </td>
                <td>
                  &nbsp;<form:errors path="dlvServiceTimeType" />
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