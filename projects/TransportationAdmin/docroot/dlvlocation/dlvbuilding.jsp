<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%  pageContext.setAttribute("HAS_GEOCODEBUTTON", "true");  
  
%>

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Delivery Building</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/>
    <div class="contentroot">               
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle">Delivery Buildings</td>
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
                      onclick="javascript:doCompositeLink('srubbedAddress','zipCode','confidence','quality','dlvbuilding.do')" />
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
      
      function addCustomRowHandlers(tableId, rowClassName, url, paramName, columnIndex, checkCol, needKeyPress) {
  
      var previousClass = null;
        var table = document.getElementById(tableId);
        
        if(table != null) {
          var rows = table.tBodies[0].getElementsByTagName("tr");          
          for (i = 0; i < rows.length; i++) {       
              var cells = rows[i].getElementsByTagName("td");
              
              for (j = 1; j < cells.length; j++) {
                
                  cells[j].onmouseover = function () {
                    previousClass = this.parentNode.className;
                    this.parentNode.className = this.parentNode.className + " " + rowClassName ;
                  };
              
                  cells[j].onmouseout = function () {
                      this.parentNode.className = previousClass;
                  };
              
                  if(checkCol == -1 || checkCol != j ) {
              if(!(needKeyPress && (j > (cells.length-4)))) {             
                  cells[j].onclick = function () {              
                      var cell = this.parentNode.getElementsByTagName("td")[columnIndex];
                      var selectBox = this.parentNode.getElementsByTagName("input")[0];
                      
                      location.href = url+"?"+ paramName + "=" + selectBox.name;                
                  };
                }
              }
              
                      
              }
          }
      }
    }
      </script>  
     </div> 
    <div align="center">
      <form id="deliveryBuildingForm" action="" method="post">  
        <ec:table items="dlvbuildings"   action="${pageContext.request.contextPath}/dlvbuilding.do"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="&nbsp;"
            width="98%"  view="fd" form="deliveryBuildingForm" autoIncludeParameters="true" rowsDisplayed="25"  >
            
            <ec:exportPdf fileName="transportationdlvbuildings.pdf" tooltip="Export PDF" 
                      headerTitle="Delivery Locations" />
              <ec:exportXls fileName="transportationdlvbuildings.xls" tooltip="Export PDF" />
              <ec:exportCsv fileName="transportationdlvbuildings.csv" tooltip="Export CSV" delimiter="|"/>
                
            <ec:row interceptor="obsoletemarker">
              <ec:column title=" " width="5px" 
                    sortable="false" cell="selectcol" filtercell="selectcol"
                    property="buildingId" />              
              <ec:column property="srubbedStreet" title="Scrubbed Street" width="35px"/>
              <ec:column property="city" title="City" width="25px"/>
              <ec:column property="state" title="State" width="5px"/>             
              <ec:column property="zip" title="Zip" width="10px" />
              <ec:column property="country" title="Country" width="10px"/>
              <ec:column property="latitude" title="Latitude"/>
              <ec:column property="longitude" title="Longitude"/>
              <ec:column property="geocodeConfidence" title="Confidence" width="10px"/>
              <ec:column property="geocodeQuality" title="Quality" width="10px"/>             
              <ec:column alias="serviceTimeType" property="buildingDetail.dlvServiceTimeType" title="Service Time Type"/>
              <ec:column alias="serviceTimeOverride" property="buildingDetail.serviceTimeOverride" title="Override ServiceTimeType"/>
              <ec:column alias="serviceTimeAdjustable" property="buildingDetail.serviceTimeAdjustable" title="Service Time Adjustment"/>
              <ec:column sortable="false" alias="geocode" property="buildingId" title=" " filterable="false" cell="geocodecol" width="5px" />
              <ec:column sortable="false" alias="showloc" property="buildingId" title=" " filterable="false" cell="locationcol" width="5px" />
              <ec:column sortable="false" alias="showdtl" property="buildingId" title=" " filterable="false" cell="buildingdtlcol" width="5px" />
            </ec:row>
          </ec:table>
       </form>  
     </div>
     <script>
      addCustomRowHandlers('ec_table', 'rowMouseOver', 'editdlvbuilding.do','id',0, 0, true);
    </script>   
  </tmpl:put>
</tmpl:insert>
