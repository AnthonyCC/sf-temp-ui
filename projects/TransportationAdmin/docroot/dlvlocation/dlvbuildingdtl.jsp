<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%  pageContext.setAttribute("HAS_GEOCODEBUTTON", "true");  
  
%>

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Delivery Building Detail</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/>
    <div class="contentroot">               
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle">Delivery Buildings Detail</td>
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
                                    
                                     
                   <td colspan="7" align="center">
                     <input type = "button" value="&nbsp;Go&nbsp;" 
                      onclick="javascript:doCompositeLink('srubbedAddress','zipCode','dlvbuildingdtl.do')" />
                </td>     
                      
              </tr>
              </table>        
              
            </td>
          </tr>               
        </table>    
       <script>
       function doCompositeLink(compId1,compId2, url) {
        var param1 = document.getElementById(compId1).value;
        var param2 = document.getElementById(compId2).value;
        
        location.href = url+"?"+compId1+"="+ param1+"&"+compId2+"="+param2;
      } 
      
      
      </script>  
     </div> 
    <div align="center">
      <form id="deliveryBuildingDtlForm" action="" method="post"> 
        <ec:table items="dlvbuildingdtl"   action="${pageContext.request.contextPath}/dlvbuildingdtl.do"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="&nbsp;"
            width="98%"  view="fd" form="deliveryBuildingDtlForm" autoIncludeParameters="true" rowsDisplayed="25"  >
            
            <ec:exportPdf fileName="transportationdlvbuildingdtls.pdf" tooltip="Export PDF" 
                      headerTitle="Delivery Locations" />
              <ec:exportXls fileName="transportationdlvbuildingdtls.xls" tooltip="Export PDF" />
              <ec:exportCsv fileName="transportationdlvbuildingdtls.csv" tooltip="Export CSV" delimiter="|"/>

  
                
            <ec:row interceptor="obsoletemarker">
              <ec:column title=" " width="5px" 
                    sortable="false" cell="selectcol" filtercell="selectcol"
                    property="building.buildingId" />             
              <ec:column property="building.srubbedStreet" title="Scrubbed Street" width="25px"/>                           
              <ec:column property="building.zip" title="Zip" width="5px" />
              <ec:column property="doorman" title="Doorman" width="5px"/>                           
              <ec:column property="walkup" title="Walk-up" width="5px"/>                            
                <ec:column property="elevator" title="Elevator" width="5px"/>                           
                <ec:column property="svcEnt" title="SvcEnt" width="5px"/> 
              <ec:column property="house" title="House" width="5px"/> 
              <ec:column property="difficultToDeliver" title="Difficult" width="5px"/>  
              <ec:column property="difficultReason" title="Reason" width="35px"/>             
            </ec:row>
          </ec:table>
       </form>  
     </div>
     <script>
      addRowHandlers('ec_table', 'rowMouseOver', 'editdlvbuildingdtl.do','id',0, 0, true);
    </script>   
  </tmpl:put>
</tmpl:insert>



