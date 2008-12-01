<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Service Time</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/> 
    <div align="center">
      <script language="javascript">                 
                      
                      
                    function doDelete(tableId, url) {                       
                        sendRequest(tableId, url, "Do you want to delete the selected records?");                       
                    }
                    
                                       
                    function sendRequest(tableId, url, message) {
                      
                        var table = document.getElementById(tableId);
                        var checkboxList = table.getElementsByTagName("input");                            
                        var paramValues = null;
                        for (i = 0; i < checkboxList.length; i++) {
                          if (checkboxList[i].type=="checkbox" && checkboxList[i].checked) {
                            
                            if (paramValues != null) {
                              paramValues = paramValues+","+checkboxList[i].name;
                            } else {
                              paramValues = checkboxList[i].name;
                            }
                          }
                        }
                        if (paramValues != null) {
                          var hasConfirmed = confirm (message);
                        if (hasConfirmed) {
                            location.href = url+"?id="+ paramValues;
                        } 
                        } else {
                          alert('Please Select a Row!');
                        }
                    }
                    
                    function addServiceTimeRowHandlers(tableId, rowClassName, url, paramName1, columnIndex1, paramName2, columnIndex2) {
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
                    
                        if(j != 0) {
                      cells[j].onclick = function () {                
                          /*var param1 = this.parentNode.getElementsByTagName("td")[columnIndex1];
                          var param2 = this.parentNode.getElementsByTagName("td")[columnIndex2].getElementsByTagName("a")[0];                           
                          location.href = url+"?"+ paramName1 + "=" + param1.innerHTML+"&"
                            +paramName2 + "=" +param2.name;*/
                          //var cell = this.parentNode.getElementsByTagName("td")[columnIndex];
                          var selectBox = this.parentNode.getElementsByTagName("input")[0]; 
                          location.href = url+"?" + "id=" + selectBox.name;
                      };
                    }
                    }
                }
            }
          }
              
                  </script>
      <form id="dlvServiceTimeForm" action="" method="post">  
        <ec:table items="dlvservicetimelist"   action="${pageContext.request.contextPath}/dlvservicetime.do"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="Service Time"
            width="98%"  view="fd" form="dlvServiceTimeForm" autoIncludeParameters="false" rowsDisplayed="25"  >
            
            <ec:exportPdf fileName="transportationdlvservicetimes.pdf" tooltip="Export PDF" 
                      headerTitle="Service Time" />
              <ec:exportXls fileName="transportationdlvservicetimes.xls" tooltip="Export PDF" />
              <ec:exportCsv fileName="transportationdlvservicetimes.csv" tooltip="Export CSV" delimiter="|"/>
                
            <ec:row interceptor="obsoletemarker">
              <ec:column title=" " width="5px" 
                    filterable="false" sortable="false" cell="selectcol"
                    property="compositeId" />             
              <ec:column property="serviceTimeType" title="Service Time Type"/>
            <ec:column property="zoneTypeName" title="Zone Type"/>
              <ec:column property="fixedServiceTime" title="Fixed Service Time(in minutes)"/>
              <ec:column property="variableServiceTime" title="Variable Service Time(in minutes)"/>
            </ec:row>
          </ec:table>
       </form>  
     </div>
     <script>
      addServiceTimeRowHandlers('ec_table', 'rowMouseOver', 'editdlvservicetime.do','servicetimetype',1, 'zonetype',2);
    </script>   
  </tmpl:put>
</tmpl:insert>
