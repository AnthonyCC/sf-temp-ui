<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
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
	
  <table >  
  	<tr>  		   			
   			<td colspan="2" align="center">&nbsp;&nbsp;<input id="view_button1" type="image" alt="Google Maps Viewer" src="./images/googlemaps.gif" 
   			onclick="javascript:doBoundary(true)"/>
   			<td align="center">&nbsp;&nbsp;<input id="view_button3" type="image" alt="Clear" src="./images/clear_all.gif"  
   			onclick="javascript:doClear()" />&nbsp;&nbsp;
   			&nbsp;&nbsp;<input id="view_button3" type="image" alt="Export Map" src="./images/export_map.gif"  
   			onclick="javascript:doBoundary(false)" />&nbsp;&nbsp;</td>
   			</tr> 
    <tr>    
	  <td style="vertical-align: top;" width="50%">
	      <ec:table items="zoneboundaries"   action="${pageContext.request.contextPath}/gmap.do"
	            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="Zones"
	            tableId="zone_boundaries"  width="100%" filterable="false" showPagination="false" showExports="false" 
	            showStatusBar="false" sortable="false" rowsDisplayed="1000" view="fd" >
             	                
	            <ec:row> 
	            <ec:column title=" " width="5px" 
									filterable="false" sortable="false" cell="selectcol"
									property="zoneCode" alias="bzoneCode" />   
	              <ec:column filterable="false" sortable="false" alias="trnZoneCode" property="zoneCode" title="Code"/>
				  <ec:column filterable="false" sortable="false" property="name" title="Name"/>              	              									  	                           
	            </ec:row>
	          </ec:table>
	    </td>
	   <td>&nbsp;&nbsp;&nbsp;</td>
	    <td style="vertical-align: top;" width="50%">
	    
	      <ec:table items="georestrictionboundaries"   action="${pageContext.request.contextPath}/gmap.do"
	            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="Geo Restrictions"
	            width="100%"  filterable="false"  showPagination="false" showExports="false" showStatusBar="false" sortable="false" 
	             tableId="georestriction_boundaries" rowsDisplayed="1000" view="fd" >
	            
	            
	            <ec:row>
	             <ec:column title=" " width="5px" 
									filterable="false" sortable="false" cell="selectcol"
									property="code" alias="bcode" />                  
	             <ec:column filterable="false" sortable="false" alias="trnBCode" property="code" title="Code"/>
				 <ec:column filterable="false" sortable="false" property="name" title="Name"/>									  	                           
	            </ec:row>
	          </ec:table>
	    
	  	</td> 
	    </tr> 
    </table>
	<%@ include file='/common/i_gmapviewer.jspf'%>
	<script>
		function doBoundary(doShow) {
			var table_zone = document.getElementById("zone_boundaries_table");
			var table_georestriction = document.getElementById("georestriction_boundaries_table");			
            var checkboxList_Zone = table_zone.getElementsByTagName("input");
            var checkboxList_GeoRestriction = table_georestriction.getElementsByTagName("input");
            var checked = "";
            
            for (i = 0; i < checkboxList_Zone.length; i++) {            
              if (checkboxList_Zone[i].type=="checkbox" && !checkboxList_Zone[i].disabled)  {
              	if(checkboxList_Zone[i].checked) {
              		checked += checkboxList_Zone[i].name+",";
              	}                          	
              }
            }
            for (i = 0; i < checkboxList_GeoRestriction.length; i++) {            
                if (checkboxList_GeoRestriction[i].type=="checkbox" && !checkboxList_GeoRestriction[i].disabled)  {
                	if(checkboxList_GeoRestriction[i].checked) {
                		checked += "$_"+checkboxList_GeoRestriction[i].name+",";
                	}                          	
                }
           }
            if(checked.length == 0) {
             	alert('Please Select a Row!');
            }
            else {
                if(doShow) {
            		showBoundary(checked.substring(0,checked.length-1));
                } else {
                	location.href = "gmapexport.do?code="+checked.substring(0,checked.length-1);	
                }
            }
		}

		function doClear() {
			var table_zone = document.getElementById("zone_boundaries_table");
			var table_georestriction = document.getElementById("georestriction_boundaries_table");			
            var checkboxList_Zone = table_zone.getElementsByTagName("input");
            var checkboxList_GeoRestriction = table_georestriction.getElementsByTagName("input");
                        
            for (i = 0; i < checkboxList_Zone.length; i++) {            
              if (checkboxList_Zone[i].type=="checkbox" && !checkboxList_Zone[i].disabled)  {
            	  checkboxList_Zone[i].checked = false;                       	
              }
            }
            for (i = 0; i < checkboxList_GeoRestriction.length; i++) {            
                if (checkboxList_GeoRestriction[i].type=="checkbox" && !checkboxList_GeoRestriction[i].disabled)  {
                	checkboxList_GeoRestriction[i].checked = false;                       	
                }
           }
           
		}
		
	</script>	
	
  </tmpl:put>
</tmpl:insert>
