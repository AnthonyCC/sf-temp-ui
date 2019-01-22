<%@ page import='com.freshdirect.webapp.util.FormElementNameHelper' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.erp.security.SecurityManager' %>
<%@ page import='java.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<tmpl:insert template='/common/templates/main.jsp'>

    <tmpl:put name='title' content='class view page' direct='true'/>

    <tmpl:put name='leftnav' direct='true'>
    	
        <div id="leftnav">
        	<%-- 
			<!-- SAP class tree goes here -->
			<fd:ClassTree id="classTree">
				<logic:iterate id="erpClass" collection="<%= classTree %>" type="com.freshdirect.erp.model.ErpClassModel">
					<a href="class_view.jsp?sapId=<%= erpClass.getSapId() %>"><%= erpClass.getSapId() %></a><br>
				</logic:iterate>
			</fd:ClassTree>
			--%>
		</div>
		
    </tmpl:put>


    <tmpl:put name='content' direct='true'>
           
    </tmpl:put>




</tmpl:insert>