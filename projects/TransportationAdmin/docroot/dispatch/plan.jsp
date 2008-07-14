<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<%	pageContext.setAttribute("HAS_COPYBUTTON", "true");  %>
	
<tmpl:insert template='/common/site.jsp'>

    <tmpl:put name='title' direct='true'>Transportation Planning</tmpl:put>

	<tmpl:put name='content' direct='true'>		
		<div class="contentroot">               
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle">Transportation Planning</td>
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
                  <td>Date</td>
                  <td> 
                                
                    <input maxlength="40" size="40" name="daterange"
                      id="daterange" value="" />                    
               	 </td>
                
                <td>Zone</td>
                  <td> 
                                
                    <input maxlength="40" size="40" name="zone"
                      id="zone" value="" />                    
                  </td>
                                   
                                     
                   <td colspan="4" align="center">
                     <input type = "button" value="&nbsp;Go&nbsp;" 
                     	onclick="javascript:doCompositeLink('daterange','zone','plan.do')" />
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
			<form id="planListForm" action="" method="post">	
				<ec:table items="planlist"  filterRowsCallback="exactMatch" action="${pageContext.request.contextPath}/plan.do"
				    imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title=""
				    width="98%"  view="fd" form="planListForm" autoIncludeParameters="true" rowsDisplayed="25"  >
				    
				    <ec:exportPdf fileName="transportationplan.pdf" tooltip="Export PDF" 
				        			headerTitle="Transportation Plan" />
		       		<ec:exportXls fileName="transportationplan.xls" tooltip="Export PDF" />
		       		<ec:exportCsv fileName="transportationplan.csv" tooltip="Export CSV" delimiter="|"/>
				        
				    <ec:row interceptor="obsoletemarker">
				    	<ec:column title=" " width="5px" 
					          filterable="false" sortable="false" cell="selectcol"
					          property="planId" />				    	
				      <ec:column cell="date" property="planDate" sortable="true" title="Date"/>
				      <ec:column property="dispatchDay" sortable="true" title="Day"/>
				      <ec:column alias="trnZonezoneNumber" property="trnZone.zoneNumber" title="Zone"/>
				      <ec:column alias="trnTimeslotslotName" property="trnTimeslot.slotName" title="Start Time"/>
				      <ec:column alias="trnEndTimeslotslotName" property="trnEndTimeslot.slotName" title="End Time"/>
				      <ec:column alias="trnDrivername" property="trnDriver.name" title="Driver"/>
				      <ec:column alias="trnPrimaryHelpername" property="trnPrimaryHelper.name" title="Helper1"/>
				      <ec:column alias="trnSecondaryHelpername" property="trnSecondaryHelper.name" title="Helper2"/>
				    </ec:row>
				  </ec:table>
			 </form> 	
		 </div>
		 <script>
			addRowHandlers('ec_table', 'rowMouseOver', 'editplan.do','id',0, 0);
		</script> 	
	</tmpl:put>
</tmpl:insert>