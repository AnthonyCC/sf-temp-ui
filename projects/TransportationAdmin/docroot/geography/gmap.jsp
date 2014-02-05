<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<%    
  pageContext.setAttribute("HAS_ADDBUTTON", "false");
  pageContext.setAttribute("HAS_DELETEBUTTON", "false");  
%>

<tmpl:insert template='/common/sitelayout.jsp'>

<tmpl:put name='yui-lib'>
	<%@ include file='/common/i_yui.jspf'%>
</tmpl:put>	

<tmpl:put name='gmap-lib'>
	<%@ include file='/common/i_gmap.jspf'%>
</tmpl:put>	

<tmpl:put name='yui-skin'>yui-skin-sam</tmpl:put>	

<tmpl:put name='title' direct='true'>Operations : GMaps </tmpl:put>

<tmpl:put name='hasSubs' direct='true'>subs</tmpl:put>

  <tmpl:put name='content' direct='true'>
 
  <c:if test="${not empty messages}">
		<div class="err_messages">
			<jsp:include page='/common/messages.jsp'/>
		</div>
	</c:if>
	
  <table>  
  		<tr>
  			<td align="left">
  				<br/>
  				<b>Start Date:<input maxlength="40" name="startDate" id="startDate" value='<c:out value="${startDate}"/>' style="width:90px"/></b>
						 	<a href="#" id="trigger_startDate" style="font-size: 9px;">
                        			<img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Start Date" title="Start Date"></a>
  			</td>
  			<script>
  				Calendar.setup(
  	               {
  	                 showsTime : false,
  	                 electric : false,
  	                 inputField : "startDate",
  	                 ifFormat : "%m/%d/%Y",
  	                 singleClick: true,
  	                 button : "trigger_startDate" 
  	                }
  	               );
  			</script>
   			<td colspan="3" align="right">
			&nbsp;&nbsp;<input id="view_button1" type="image" alt="Google Maps Viewer" src="./images/googlemaps.gif" 
   			onclick="javascript:doBoundary(true)"/>
   			
   			&nbsp;&nbsp;<input id="view_button2" type="image" alt="Export Map" src="./images/export_map.gif"  
   			onclick="javascript:doBoundary(false)" />
			&nbsp;&nbsp;<input id="view_button3" type="image" alt="Clear" src="./images/clear_all.gif"  
   			onclick="javascript:doClear()" />&nbsp;&nbsp;
			</td>
   		</tr> 
    <tr>    
	  <td style="vertical-align: top;" width="35%">
	      <ec:table items="zoneboundaries"   action="${pageContext.request.contextPath}/gmap.do"
	            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="Zones"
	            tableId="zone_boundaries"  showPagination="false" showExports="false" 
	            showStatusBar="false" sortable="false" rowsDisplayed="1000" view="fd" >
             	                
	            <ec:row interceptor="obsoletemarker"> 
	            <ec:column title=" " width="5px" 
									sortable="false" filtercell="selectcol" cell="selectcol"
									property="zoneCode" alias="bzoneCode" />   
	              <ec:column sortable="true" property="zoneCode" title="Code"/>
				  <ec:column sortable="true" property="name" title="Name"/>              	              									  	                           
	            </ec:row>	          
	          </ec:table>
	    </td>
	   <td>&nbsp;&nbsp;&nbsp;</td>
	    <td style="vertical-align: top;" width="35%">
	    
	      <ec:table items="georestrictionboundaries"   action="${pageContext.request.contextPath}/gmap.do"
	            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="Geo Restrictions"
	             filterable="true"  showPagination="false" showExports="false" showStatusBar="false" sortable="false" 
	             tableId="georestriction_boundaries" rowsDisplayed="1000" view="fd" >
	            
	            
	            <ec:row interceptor="obsoletemarker">
	             <ec:column title=" " width="5px" 
									sortable="false" filtercell="selectcol" cell="selectcol"
									property="code" alias="bcode" />                  
	             <ec:column sortable="true" property="code" title="Code"/>
				 <ec:column sortable="true" property="name" title="Name"/>									  	                           
	            </ec:row>
	          </ec:table>
	    
	  	</td>
		 <td style="vertical-align: top;" width="30%">
	    
	      <ec:table items="geoSectors"   action="${pageContext.request.contextPath}/gmap.do"
	            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="Sectors"
	            filterable="true"  showPagination="false" showExports="false" showStatusBar="false" sortable="false" 
	             tableId="neighbourhood_boundaries" rowsDisplayed="1000" view="fd" >
	            <ec:row interceptor="obsoletemarker">
	             <ec:column title=" " width="5px" 
									sortable="false" filtercell="selectcol" cell="selectcol"
									property="name" alias="bnhname" />                  
	             <ec:column sortable="true" property="name" title="Name"/>
				 <ec:column sortable="true" property="description" title="Description"/>
				 </ec:row>
	          </ec:table>
	    
	  	</td>
	    </tr> 
    </table>
	<%@ include file='/common/i_gmapviewer.jspf'%>
	<script>
		function doBoundary(doShow) {
			var startDate = document.getElementById("startDate").value;
			var table_zone = document.getElementById("zone_boundaries_table");
			var table_georestriction = document.getElementById("georestriction_boundaries_table");
			var table_neighbourhood = document.getElementById("neighbourhood_boundaries_table");
            var checkboxList_Zone = table_zone.getElementsByTagName("input");
            var checkboxList_GeoRestriction = table_georestriction.getElementsByTagName("input");
			var checkboxList_Neighbourhood = table_neighbourhood.getElementsByTagName("input");
            var checked = "";
            
            for (var i = 1; i < checkboxList_Zone.length; i++) {
              if (checkboxList_Zone[i].type=="checkbox" && !checkboxList_Zone[i].disabled && !checkboxList_Zone[i].id)  {
              	if(checkboxList_Zone[i].checked) {
              		checked += checkboxList_Zone[i].name+",";
              	}                          	
              }
            }
            for (var i = 1; i < checkboxList_GeoRestriction.length; i++) {            
                if (checkboxList_GeoRestriction[i].type=="checkbox" && !checkboxList_GeoRestriction[i].disabled && !checkboxList_GeoRestriction[i].id)  {
                	if(checkboxList_GeoRestriction[i].checked) {
                		checked += "$_"+checkboxList_GeoRestriction[i].name+",";
                	}                          	
                }
           }
		    for (var i = 1; i < checkboxList_Neighbourhood.length; i++) {            
              if (checkboxList_Neighbourhood[i].type=="checkbox" && !checkboxList_Neighbourhood[i].disabled && !checkboxList_Neighbourhood[i].id)  {
            	if(checkboxList_Neighbourhood[i].checked) {
                	checked += "NH_"+checkboxList_Neighbourhood[i].name+",";
                }
              }
            }
            if(checked.length == 0) {
             	alert('Please Select a Row!');
            }
            else {
                if(doShow) {
            		showBoundary(checked.substring(0,checked.length-1), null, startDate, true);
                } else {
                	location.href = "gmapexport.do?code="+checked.substring(0,checked.length-1)+"&startDate="+startDate;	
                }
            }
		}

		function doClear() {
			var table_zone = document.getElementById("zone_boundaries_table");
			var table_georestriction = document.getElementById("georestriction_boundaries_table");
			var table_neighbourhood = document.getElementById("neighbourhood_boundaries_table");
            var checkboxList_Zone = table_zone.getElementsByTagName("input");
            var checkboxList_GeoRestriction = table_georestriction.getElementsByTagName("input");
            var checkboxList_Neighbourhood = table_neighbourhood.getElementsByTagName("input");
            
			for (var i = 0; i < checkboxList_Zone.length; i++) {            
              if (checkboxList_Zone[i].type=="checkbox" && !checkboxList_Zone[i].disabled)  {
            	  checkboxList_Zone[i].checked = false;                       	
              }
            }
            for (var i = 0; i < checkboxList_GeoRestriction.length; i++) {            
                if (checkboxList_GeoRestriction[i].type=="checkbox" && !checkboxList_GeoRestriction[i].disabled)  {
                	checkboxList_GeoRestriction[i].checked = false;                       	
                }
            }
		    for (var i = 0; i < checkboxList_Neighbourhood.length; i++) {            
              if (checkboxList_Neighbourhood[i].type=="checkbox" && !checkboxList_Neighbourhood[i].disabled)  {
            	  checkboxList_Neighbourhood[i].checked = false;                       	
              }
            }
           
		}
		
	</script>	
	<style>
		.eXtremeTable .filter input {
	 		width: 98%;
		}
	</style>
  </tmpl:put>
</tmpl:insert>
