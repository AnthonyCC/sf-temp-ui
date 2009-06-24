<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>

<%  pageContext.setAttribute("HAS_COPYBUTTON", "true");  
  String dateRangeVal = request.getParameter("daterange") != null ? request.getParameter("daterange") : "";
  String zoneVal = request.getParameter("zone") != null ? request.getParameter("zone") : "";
   if(dateRangeVal == null || dateRangeVal.length() == 0) dateRangeVal = TransStringUtil.getCurrentDate();
%>
<% 
	String pageTitle = "Scrib";
%>

<tmpl:insert template='/common/sitelayout.jsp'>

	<tmpl:put name='title' direct='true'> Routing : <%=pageTitle%></tmpl:put>

  <tmpl:put name='content' direct='true'> 

	<c:if test="${not empty messages}">
		<div class="err_messages">
			<jsp:include page='/common/messages.jsp'/>
		</div>
	</c:if> 

  <div class="contentroot">

		<div class="cont_topleft">
			<div class="cont_row">
				<div class="cont_Litem" id="page_<%=pageTitle%>">
					<span class="scrTitle"><%=pageTitle%></span>
						<span><input maxlength="40" name="daterange" id="daterange" value="<%= dateRangeVal %>" /></span>
						 <span><a href="#" id="trigger_scribDate" style="font-size: 9px;">
                        <img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Select Date" title="Select Date"></a></span>
                    	 <select id="scribDay" name="scribDay">
                          	<option value="All">--All Days</option>
                      		<option value="Monday">Monday</option><option value="Tuesday">Tuesday</option><option value="Wednesday">Wednesday</option><option value="Thurdsay">Thurdsay</option><option value="Friday">Friday</option><option value="Saturday">Saturday</option><option value="Sunday">Sunday</option>
                    	 </select>				
						<span><input id="view_button" type="image" alt="View" src="./images/icons/view.gif"  onclick="javascript:doCompositeLink('daterange','scribDay','scrib.do')" onmousedown="this.src='./images/icons/view_ON.gif'" /></span>
						
				</div>
			</div>
		</div> 
		

		<div class="cont_topright">
			<div class="cont_row">
				<div class="cont_Ritem">

      <form id="scribListForm" action="" method="post">  
        <ec:table items="scriblist"  filterRowsCallback="exactMatch" action="${pageContext.request.contextPath}/scrib.do"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title=""
            width="98%"  view="fd" form="scribListForm" autoIncludeParameters="true" rowsDisplayed="25"  >
            
            <ec:exportPdf fileName="transportationScrib.pdf" tooltip="Export PDF" 
                      headerTitle="Transportation Scrib" />
              <ec:exportXls fileName="transportationscrib.xls" tooltip="Export PDF" />
              <ec:exportCsv fileName="transportationscrib.csv" tooltip="Export CSV" delimiter="|"/>
                
            <ec:row interceptor="obsoletemarker">
              <ec:column title=" " width="5px" 
                    filterable="false" sortable="false" cell="selectcol"
                    property="scribId" />              
              <ec:column cell="date" property="scribDate" sortable="true" title="Date"/>
              <ec:column property="zone.zoneCode" sortable="true" title="Zone"/>
              <ec:column property="region.code" sortable="true" title="Region"/>                           
              <ec:column cell="date" format="hh:mm aaa"  property="startTime" title="Start Time"/>
              <ec:column cell="date" format="hh:mm aaa"  property="firstDlvTime" title="First Dlv Time"/>
              <ec:column cell="date" format="hh:mm aaa"  property="endDlvTime" title="Last Dlv Time"/>
              <ec:column cell="date" format="HH:mm"      property="stemTime" title="To Zone Time"/>
			  <ec:column cell="date" format="hh:mm aaa"  property="prefRuturn" title="Pref Return"/>  
			  <ec:column cell="date" format="hh:mm aaa"  property="maxReturnTimeDisplay" title="Max Return"/>  
              <ec:column property="count" sortable="true" title="No of Trucks"/> 
              <ec:column cell="date" format="hh:mm aaa"  property="waveStart" title="Wave Start"/>     
			  <ec:column cell="date" format="hh:mm"  property="prefTime" title="Pref Time"/>  
			  <ec:column cell="date" format="hh:mm"  property="maxTime" title="Max Time"/>  
            </ec:row>
          </ec:table>
       </form>
	   
					</div></div>
				</div>
     </div>   
     <script>     	 
      Calendar.setup(
                      {
                        showsTime : false,
                        electric : false,
                        inputField : "daterange",
                        ifFormat : "%m/%d/%Y",
                        singleClick: true,
                        button : "trigger_scribDate" 
                       }
                      );
         function doCompositeLink(compId1,compId2, url) 
        {
          var param1 = document.getElementById(compId1).value;         
          var param2 = document.getElementById(compId2).value;
          location.href = url+"?"+compId1+"="+ param1+"&"+compId2+"="+ param2;
        } 
      addRowHandlers('ec_table', 'rowMouseOver', 'editscrib.do','scribId',0, 0);
    </script>   
  </tmpl:put>
</tmpl:insert>
