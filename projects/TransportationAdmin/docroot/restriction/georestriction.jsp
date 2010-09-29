<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Geography Restriction</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/>
    <div class="contentroot">               
      
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
              if(!(needKeyPress && (j > (cells.length-1)))) {             
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
      <form id="geoRestrictionForm" action="" method="post">  
        <ec:table items="geoRestrictions"   action="${pageContext.request.contextPath}/georestriction.do"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="&nbsp;"
            width="98%"  view="fd" form="geoRestrictionForm" autoIncludeParameters="true" rowsDisplayed="25"  >
            
            <ec:exportPdf fileName="geoRestriction.pdf" tooltip="Export PDF" 
                      headerTitle="Geo Restrictions" />
              <ec:exportXls fileName="georestrictions.xls" tooltip="Export PDF" />
              <ec:exportCsv fileName="georestrictions.csv" tooltip="Export CSV" delimiter="|"/>
                 
            <ec:row >
              <ec:column title=" " width="5px" 
                    sortable="false" cell="selectcol" filtercell="selectcol"
                    property="restrictionId" />              
              <ec:column property="name" title="Name" width="35px"/>
              <ec:column property="boundaryCode" title="Boundary Code" width="25px"/>
              <ec:column property="startDate" title="Start Date" width="5px"/>             
              <ec:column property="endDate" title="End Date" width="10px" />
              <ec:column property="message" title="Message" width="10px"/>
              <ec:column property="active" title="Active"/>
              <ec:column property="comments" title="Comments"/>
              <ec:column property="showMessage" title="Show Message"/>
              <ec:column property="serviceType" title="Service Type"/>
              <ec:column property="viewTypeDesc" title="View Type"/>
            </ec:row>
          </ec:table>
       </form>  
     </div>
     <script>
      addCustomRowHandlers('ec_table', 'rowMouseOver', 'editgeorestriction.do','id',0, 0, true);
    </script>   
  </tmpl:put>
</tmpl:insert>
