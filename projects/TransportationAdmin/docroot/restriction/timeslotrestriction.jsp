<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Timeslot Restriction</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/>
    <div class="contentroot">               
     </div> 
    <div align="left">
      <form id="timeslotRestrictionForm" action="" method="post">  
        <ec:table items="timeslotRestrictions"   action="${pageContext.request.contextPath}/timeslotrestriction.do"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="&nbsp;"
            width="60%"  view="fd" form="timeslotRestrictionForm" autoIncludeParameters="true" rowsDisplayed="25"  >
            
            <ec:exportPdf fileName="timeslotRestrictions.pdf" tooltip="Export PDF" 
                      headerTitle="Timeslot Restrictions" />
              <ec:exportXls fileName="timeslotRestrictions.xls" tooltip="Export PDF" />
              <ec:exportCsv fileName="timeslotRestrictions.csv" tooltip="Export CSV" delimiter="|"/>
                 
            <ec:row >
              <ec:column title=" " width="5px" 
                    sortable="false" cell="selectcol" filtercell="selectcol"
                    property="id" />              
              <ec:column property="dayOfWeek" title="Day Of Week" width="15px"/>
              <ec:column property="zoneCode" title="Zone Code" width="15px"/>
              <ec:column property="condition" title="Condition" width="15px"/>
              <ec:column property="startTime" title="Start Time" width="10px"/>             
              <ec:column property="endTime" title="End Time" width="10px" />
              <ec:column property="active" title="Active"/>
            </ec:row>
          </ec:table>
       </form>  
     </div>
     </div> 
     <script>
     addRowHandlersFilterTest('ec_table', 'rowMouseOver', 'edittimeslotrestriction.do','id',0, 0);

    function getFilterTestValue() {
          var filters = getFilterValue(document.getElementById("timeslotRestrictionForm"), false);          
          return escape(filters);
     }
    </script>   
  </tmpl:put>
</tmpl:insert>
