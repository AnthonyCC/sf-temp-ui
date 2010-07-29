<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.fdstore.promotion.management.FDPromotionNewModel' %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<%@ taglib uri='template' prefix='tmpl' %>

<tmpl:insert template='site.jsp'>

    <tmpl:put name='title' direct='true'>Home Page</tmpl:put>

	<tmpl:put name='content' direct='true'>

<spring:bind path="command">
  <FONT color="red">
    <B><c:out value="${status.errorMessage}"/></B>
  </FONT>
</spring:bind>
<div align="center">		

<spring:bind path="command">
            <form id="searchRestForm" method="post" action="<c:out value="${pageContext.request.contextPath}"/>/searchRestriction.do?promotionCode=<c:out value="${command.promotion.promotionCode}"/>">			
</spring:bind>
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="screentitle">Search Restricted Customers </td>
					</tr>
					<tr>
						<td class="screenmessages"></td>
					</tr>
					
					<tr>
						<td class="screencontent">
							<table class="forms1">			  	
							  <tr>							                                  
							    <td> 							
                                <spring:bind path="command.serachKey">
                                <input type="text" name="serachKey" value="<c:out value="${status.value}"/>" >                                
                                <FONT color="red">
                                    <c:forEach items="${status.errorMessages}" var="error">
                                    <li><c:out value="${error}"/></li>
                                    </c:forEach>    
                                </FONT>
                                </spring:bind> 	            							  	
							 	</td>
							 	<td>
							 		
							 	</td>
							 
							    <td colspan="3" align="center">
								   <input type = "submit" value="&nbsp;Find Customers&nbsp;"  />
								</td>			
                                <td colspan="3" align="center">
								   <a href="<c:out value="${pageContext.request.contextPath}"/>/downloadRestriction.do?promotionCode=<c:out value="${command.promotion.promotionCode}"/>">Download full List</a>
								</td>			
							</tr>
							</table>				
							
						</td>
					</tr>								
				</table>
			
			
		 </div>

	
		<br/>	
		<div align="center">
			
				<ec:table items="${command.promotionList}" var="item" action="${pageContext.request.contextPath}/searchRestriction.do?promotionCode=${command.promotion.promotionCode}"
				    imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="Promotions List"
				    width="98%" form="searchRestForm" autoIncludeParameters="false" rowsDisplayed="25"  >				    				        
				    <ec:row>				    				    	
				      <ec:column property="customerId" title="Customer Id"/>
                      <ec:column property="custEmailAddress" title="Email Address"/>				      
                      <ec:column property="promotionId" title="DELETE" filterable="false" sortable="false"><a href="${pageContext.request.contextPath}/searchRestriction.do?promotionCode=${command.promotion.promotionCode}&customerId=${pageScope.item.customerId}&action_type=DELETE">Delete</a></ec:column>                      
				    </ec:row>
				  </ec:table>
			  	
		 </div>
		 <script>
			addRowHandlers('ec_table', 'rowMouseOver', 'searchRestriction.do','id',0, 0);
		</script> 	
        </form>
</tmpl:put>
</tmpl:insert>

