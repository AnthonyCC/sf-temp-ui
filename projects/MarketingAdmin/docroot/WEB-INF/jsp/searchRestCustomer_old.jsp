<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.fdstore.promotion.management.FDPromotionModel' %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<%@ taglib uri='template' prefix='tmpl' %>

<tmpl:insert template='site.jsp'>

    <tmpl:put name='title' direct='true'>Home Page</tmpl:put>

	<tmpl:put name='content' direct='true'>
	
<div align="center">
			<form:form commandName = "searchRestForm" method="post">			
			
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="screentitle">Search Restticted Customers </td>
					</tr>
					<tr>
						<td class="screenmessages"></td>
					</tr>
					
					<tr>
						<td class="screencontent">
							<table class="forms1">			  	
							  <tr>							    
							    <td> 								  
							  	 	<form:input maxlength="50" size="30" path="serachKey" />&nbsp;<form:errors path="serachKey" />
							 	</td>
							 	<td>
							 		
							 	</td>
							 
							    <td colspan="3" align="center">
								   <input type = "submit" value="&nbsp;Find Customers&nbsp;"  />
								</td>			
							</tr>
							</table>				
							
						</td>
					</tr>								
				</table>
			
			
		 </div>

	
		<br/>	
		<div align="center">
			
				<ec:table items="${searchRestForm.promotionList}" var="item" action="${pageContext.request.contextPath}/searchRestriction.do?promotionCode=${searchRestForm.promotion.promotionCode}"
				    imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="Promotions List"
				    width="98%" form="searchRestForm" autoIncludeParameters="false" rowsDisplayed="25"  >				    				        
				    <ec:row>				    				    	
				      <ec:column property="customerId" title="Customer Id"/>
                      <ec:column property="custEmailAddress" title="Email Address"/>				      
                      <ec:column property="promotionId" title="DELETE" filterable="false" sortable="false"><a href="${pageContext.request.contextPath}/searchRestriction.do?promotionId=${pageScope.item.promotionId}">Delete</a></ec:column>                      
				    </ec:row>
				  </ec:table>
			  	
		 </div>
		 <script>
			addRowHandlers('ec_table', 'rowMouseOver', 'searchRestriction.do','id',0, 0);
		</script> 	
        </form:form>
</tmpl:put>
</tmpl:insert>

