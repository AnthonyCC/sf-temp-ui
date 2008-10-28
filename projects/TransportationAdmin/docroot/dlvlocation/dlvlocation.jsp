<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%	 
	pageContext.setAttribute("HAS_UPDATEBUTTON", "true");
	pageContext.setAttribute("HAS_ADDBUTTON", "false");
	pageContext.setAttribute("HAS_SENDBUTTON", "true");
%>

<tmpl:insert template='/common/sitelocation.jsp'>

    <tmpl:put name='title' direct='true'>Delivery Location</tmpl:put>

	<tmpl:put name='content' direct='true'>
		<br/>
		<div class="contentroot">               
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle">Delivery Locations</td>
          </tr>
          <c:if test="${not empty messages}">
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
          </tr>
          </c:if>         
          <tr>
            <td class="screencontent">
              <table class="forms1">          
                <tr>
                  <td>Street</td>
                  <td> 
                                
                    <input maxlength="50" size="20" name="srubbedAddress"
                      id="srubbedAddress" value="" />                    
               	 </td>
                
                <td>ZipCode</td>
                  <td> 
                                
                    <input maxlength="20" size="20" name="zipCode"
                      id="zipCode" value="" />                    
                  </td>
                  
                  <td>Select Confidence</td>
                  <td>
                    <select name='confidence' id='confidence'>
                      <option value="">--Please Select Geo Confidence</option> 
	                      <c:forEach var="confidenceTypeRow" items="${confidencetypes}">                             	                          
	                            <option value='<c:out value="${confidenceTypeRow.name}"/>'><c:out value="${confidenceTypeRow.description}"/></option>
	                      </c:forEach>   
                  		</select>
                </td>
                
                <td>Select Quality</td>
                  <td>
                    	<select name='quality' id='quality'>
                     	 <option value="">--Please Select Geo Quality</option> 
	                      <c:forEach var="qualityTypeRow" items="${qualitytypes}">                             	                          
	                            <option value='<c:out value="${qualityTypeRow.name}"/>'><c:out value="${qualityTypeRow.description}"/></option>
	                      </c:forEach>   
                  		</select>
                </td>
                                     
                   <td colspan="7" align="center">
                     <input type = "button" value="&nbsp;Go&nbsp;" 
                     	onclick="javascript:doCompositeLink('srubbedAddress','zipCode','confidence','quality','dlvlocation.do')" />
                </td>     
                      
              </tr>
              </table>        
              
            </td>
          </tr>               
        </table>    
       <script>
       function doCompositeLink(compId1,compId2, compId3, compId4, url) {
    		var param1 = document.getElementById(compId1).value;
    		var param2 = document.getElementById(compId2).value;
    		var param3 = document.getElementById(compId3).value;
    		var param4 = document.getElementById(compId4).value;
    		location.href = url+"?"+compId1+"="+ param1+"&"+compId2+"="+param2+"&"+compId3+"="+param3+"&"+compId4+"="+param4;
  		} 
  		</script>  
     </div>	
		<div align="center">
			<form id="deliveryLocationForm" action="" method="post">	
				<ec:table items="dlvlocations"   action="${pageContext.request.contextPath}/dlvlocation.do"
				    imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="&nbsp;"
				    width="98%"  view="fd" form="deliveryLocationForm" autoIncludeParameters="true" rowsDisplayed="25"  >
				    
				    <ec:exportPdf fileName="transportationdlvlocations.pdf" tooltip="Export PDF" 
				        			headerTitle="Delivery Locations" />
		       		<ec:exportXls fileName="transportationdlvlocations.xls" tooltip="Export PDF" />
		       		<ec:exportCsv fileName="transportationdlvlocations.csv" tooltip="Export CSV" delimiter="|"/>
				        
				    <ec:row interceptor="obsoletemarker">
				    	<ec:column title=" " width="5px" 
					          sortable="false" cell="selectcol" filtercell="selectcol"
					          property="locationId" />				    	
				      <ec:column property="locationId" title="ID" alias="locId" width="10px"/>
				      <ec:column property="building.srubbedStreet" title="Scrubbed Street" width="35px"/>
				      <ec:column property="apartment" title="Apt"/>
				      <ec:column property="building.city" title="City" width="12px" />
				      <ec:column property="building.state" title="State" width="10px" />
				      <ec:column property="building.zip" title="Zip" width="10px" />
				      <ec:column property="building.country" title="Country" width="10px"/>
				      <ec:column property="latitude" title="Latitude" width="10px"/>
				      <ec:column property="longitude" title="Longitude" width="10px"/>
				      <ec:column property="geocodeConfidence" title="Confidence" width="10px"/>
				      <ec:column property="geocodeQuality" title="Quality" width="10px"/>				      
				      <ec:column alias="serviceTimeType" property="serviceTimeType.name" title="Service Time Type" width="15px"/>
				      <ec:column alias="geocode" property="id" title=" " filterable="false" cell="geocodecol" width="5px" />
				    </ec:row>
				  </ec:table>
			 </form> 	
		 </div>
		 <script>
			addHandlers('ec_table', 'rowMouseOver', 'editdlvlocation.do','id',0, 0, true);
		</script> 	
	</tmpl:put>
</tmpl:insert>
