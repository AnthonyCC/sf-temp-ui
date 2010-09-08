<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>
<script type="text/javascript" language="javascript" src="js/SelectionHandlers.js"></script>
<%
  pageContext.setAttribute("HAS_COPYBUTTON", "true");	 
  String dateRangeVal = request.getParameter("daterange") != null ? request.getParameter("daterange") : "";
  String zoneVal = request.getParameter("zone") != null ? request.getParameter("zone") : "";
  if(dateRangeVal == null || dateRangeVal.length() == 0) dateRangeVal = TransStringUtil.getCurrentDate();
%>
<% 
	String pageTitle = "Scrib";
%>

<tmpl:insert template='/common/sitelayout.jsp'>
<tmpl:put name='yui-lib'>
	<%@ include file='/common/i_yui.jspf'%>	
</tmpl:put>
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
					<div style="float:left;">
							<span class="scrTitle"><%=pageTitle%></span>
							<span><input style="width:85px;" maxlength="40" name="daterange" id="daterange" value="<%= dateRangeVal %>" /></span>
							<span><a href="#" id="trigger_scribDate" style="font-size: 9px;">
	                        	<img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Select Date" title="Select Date"></a></span>
	                    	 <select id="scribDay" name="scribDay" >
	                          	<option value="All">--All Days</option>
	                      		<option value="2">Monday</option><option value="3">Tuesday</option><option value="4">Wednesday</option><option value="5">Thurdsay</option><option value="6">Friday</option><option value="7">Saturday</option><option value="8">Sunday</option>
	                    	 </select>				
							<span><input id="view_button" type="image" alt="View" src="./images/icons/view.gif"  onclick="javascript:doCompositeLink('daterange','scribDay','scrib.do')" onmousedown="this.src='./images/icons/view_ON.gif'" /></span>
							<span>&nbsp;&nbsp;</span>
							<span> <input style="font-size:11px" type = "button" height="18" value="Generate Plan" onclick="javascript:showGeneratePlanForm();" /></span>
							<span> <input style="font-size:11px" type = "button" height="18"  value="Add Label" onclick="javascript:showLabelForm();" /></span>
							<span> <input style="font-size:11px" type = "button" height="18"  value="View Labels" onclick="javascript:showViewLabelsForm();" /></span>
							<span>&nbsp;&nbsp;</span>
						</div>				
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
              <ec:column property="zoneS" sortable="true" title="Zone"/>
              <ec:column property="regionS" sortable="true" title="Region" /> 
              <ec:column property="supervisorName" sortable="true" title="Supervisor" />                              
              <ec:column cell="date" format="hh:mm aaa"  property="startTime" title="Emp Start Time"/>
              <ec:column cell="date" format="hh:mm aaa"  property="firstDlvTime" title="First Dlv Time"/>
              <ec:column cell="date" format="hh:mm aaa"  property="endDlvTime" title="Last Dlv Time"/>
              <ec:column cell="date" format="HH:mm"      property="stemToTime" title="To Zone Time"/>
              <ec:column cell="date" format="HH:mm"      property="stemFromTime" title="From Zone Time"/>
			  <ec:column cell="date" format="hh:mm aaa"  property="prefRuturn" title="*Pref Return"/>  
			  <ec:column cell="date" format="hh:mm aaa"  property="maxReturnTimeDisplay1" title="Max Return"/>  
              <ec:column property="count" sortable="true" title="No of Trucks"/> 
              <ec:column property="count1" sortable="true" title="No of HandTrucks"/> 
              <ec:column cell="date" format="hh:mm aaa"  property="waveStart" title="*Wave Start"/>     
			  <ec:column cell="date" format="HH:mm"  property="prefTime" title="*Pref Time"/>  
			  <ec:column cell="date" format="HH:mm"  property="maxTime1" title="*Max Time"/>
			  <ec:column property="scribLabel" title="Scrib Label" />  
            </ec:row>
          </ec:table>
       </form>
	   
					</div></div>
				</div>
     </div>   
     <script>

		function showViewLabelsForm(){
			javascript:window.open('viewscriblabel.do','y','height=450,width=400');
		}
          	 
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

        function doCompositeLink(compId1,compId2, url,generatePlan) 
        {
        	if(generatePlan!=null)
        	{
        		var hasConfirmed = confirm ("You are about to perform Generate Plan.  IF PLANS ALREADY EXIST FOR THE DAY, ALL CHANGES WILL BE LOST.  Do you want to continue?")
				if (hasConfirmed) 
				{
			  		 var param1 = document.getElementById(compId1).value;         
			          var param2 = document.getElementById(compId2).value;
			          var param3="";
			          if(generatePlan!=null)param3="&p="+generatePlan;
			          location.href = url+"?"+compId1+"="+ param1+"&"+compId2+"="+ param2+param3;
				} 
        	}
        	else
        	{
	          var param1 = document.getElementById(compId1).value;         
	          var param2 = document.getElementById(compId2).value;
	          var param3="";         
	          location.href = url+"?"+compId1+"="+ param1+"&"+compId2+"="+ param2;
          }
        } 

       	addRowHandlersFilterTest('ec_table', 'rowMouseOver', 'editscrib.do','scribId',0, 0);
      	document.getElementById("scribDay").value='<%=request.getParameter("scribDay")==null?"All":request.getParameter("scribDay")%>';

        function getFilterTestValue()
      	{
	      	var filters=getFilterValue(document.getElementById("scribListForm"),false);
	      	filters+="&daterange="+document.getElementById("daterange").value;
	      	filters+="&scribDay="+document.getElementById("scribDay").value;
	      	return escape(filters);
        }
    </script>
     <%@ include file='i_generateplan.jspf'%>
     <%@ include file='i_addlabel.jspf'%>            
  </tmpl:put>
</tmpl:insert>
