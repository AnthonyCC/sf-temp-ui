<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.*' %>
<%@ page import= 'java.util.*' %>
<%@ page import= 'com.freshdirect.transadmin.web.model.*' %>
<%@ page import= 'com.freshdirect.routing.model.*' %>
<%  
   pageContext.setAttribute("HAS_DELETEBUTTON", "false");
   pageContext.setAttribute("HAS_ADDBUTTON", "true");
   pageContext.setAttribute("IS_USERADMIN", ""+com.freshdirect.transadmin.security.SecurityManager.isUserAdmin(request));
   String dateRangeVal = request.getParameter("rDate") != null ? request.getParameter("rDate") : "";
   if(dateRangeVal == null || dateRangeVal.length() == 0) dateRangeVal = TransStringUtil.getNextDate();
   String scenariotitle = "<span style=\"font-size: 11pt;\"> ";
   IServiceTimeScenarioModel srvScenario = (IServiceTimeScenarioModel)request.getAttribute("srcscenario");
   if(srvScenario != null) {
	   scenariotitle += srvScenario.getDescription();
   }
   scenariotitle += "</span>";
 %>
  
  <link rel="stylesheet" href="css/transportation.css" type="text/css" />		
<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Early Warning View</tmpl:put>
	<tmpl:put name='yui-lib'>
		<%@ include file='/common/i_yui.jspf'%>
	</tmpl:put>
	<tmpl:put name='yui-skin'>yui-skin-sam</tmpl:put>
	
  <tmpl:put name='content' direct='true'>
    <br/>
	 <div class="contentroot">               
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
          <c:if test="${not empty messages}">
          <tr>
            <td class="screenmessages">
				<jsp:include page='/common/messages.jsp'/>
			</td>
          </tr>
          </c:if>
        </table>
      </div>
	   <table>
    <tr>    
	  <td style="vertical-align: top;" width="45%">
	       <form id="facilityListForm" action="" method="post">  
	      <ec:table items="facilitys"  action="${pageContext.request.contextPath}/facility.do"
	            imagePath="${pageContext.request.contextPath}/images/table/*.gif"  title="Facility"
	            width="100%" showPagination="false" form="facilityListForm" rowsDisplayed="100" view="fd" tableId="facility">
	            
	            <ec:exportPdf fileName="earlywarnings.pdf" tooltip="Export PDF" 
	                      headerTitle="Early Warning" />
	            <ec:exportXls fileName="earlywarnings.xls" tooltip="Export PDF" />
	            <ec:exportCsv fileName="earlywarnings.csv" tooltip="Export CSV" delimiter="|"/>
	                
	            <ec:row interceptor="obsoletemarker">
	              <ec:column title=" " width="5px" 
                    filterable="false" sortable="false" cell="selectcol"
                    property="facilityId" />
				  <ec:column property="name" title="Facility Code" width="8"/>
				  <ec:column property="description" title="Facility Description"  width="15"/>
				  <ec:column property="trnFacilityType" title="Facility Type" width="10"/>
				  <ec:column property="routingCode" title="Routing Code" width="10"/>
				  <ec:column property="facilityLocation" title="Facility Location" width="8"/>
				  <ec:column property="prefix" title="Prefix" width="5"/>
				  <ec:column property="latitude" title="Latitude" width="8"/>
              	  <ec:column property="longitude" title="Longitude" width="8"/>
           		  <ec:column property="leadFromTime" title="From Time" width="5"/>
				  <ec:column property="leadToTime" title="To Time" width="5"/>
				  <ec:column property="todrestriction" title="TOD Restriction" width="10"/>
	            </ec:row>
	          </ec:table>
	           </form>  
	    </td>
	    <td>&nbsp;&nbsp;&nbsp;</td>
	    <td style="vertical-align: top;"  width="35%">
	      <ec:table items="facilityTypes"  action="${pageContext.request.contextPath}/facility.do"
	            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="Facility Type"
	            width="100%" showPagination="false" sortable="false" 
	            tableId="facilitytype" rowsDisplayed="100" view="fd" >
			            
	            <ec:row>	               
				  <ec:column property="name"  title="Type Code"/>
				  <ec:column property="description" title="Type Description" />
	            </ec:row>
	       </ec:table>
	       <br/>
	       <ec:table items="facilityLocations"  action="${pageContext.request.contextPath}/facility.do"
	            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="Facility Location"
	            width="100%" filterable="false" showPagination="false" sortable="false" 
	            tableId="facilitylocation" rowsDisplayed="100" view="fd" >
			            
	            <ec:row>	               
				  <ec:column property="code"  title="Location Code"/>
				  <ec:column property="description" title="Description" />
	            </ec:row>
	       </ec:table>
	  	</td> 
	    </tr> 
    </table>

	<script>
	 	addRowHandlersFilterTest('facility_table','rowMouseOver','editfacility.do','facilityId',0,0);
	 	//addRowHandlersFilterTest('facilitytype_table','rowMouseOver','editfacilitytype.do','name',0,0);
 		function getFilterTestValue() {
             var filters = getFilterValue(document.getElementById("facilityListForm"),false);
             return escape(filters);
        }

		function doDelete(tableId, url) 
        {    
			if(tableId === 'facilitytype_table'){
				url = '/TrnAdmin/deletefacilitytype.do';
			}
  		    var paramValues = getParamList(tableId, url);
  		    if (paramValues != null) {
  		    	var hasConfirmed = confirm ("Do you want to delete the selected records?")
  		    	if (hasConfirmed) 
  				{
  		    		location.href = url+"?id="+ paramValues;
  				} 
  		    } else {
  		    	alert('Please Select a Row!');
  		    }
  		}

		function doAddNew(tableId, url) {
			if(tableId === 'facilitytype_table'){
				url = '/TrnAdmin/editfacilitytype.do';
			}
			if(url.indexOf('?') == -1) {
				location.href = url+"?filter="+getFilterTestValue();
			} else {
				location.href = url+"&filter="+getFilterTestValue();
			}
		}
	</script>
  </tmpl:put>
</tmpl:insert>
