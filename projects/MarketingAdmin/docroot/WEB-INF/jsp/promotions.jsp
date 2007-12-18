<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.fdstore.promotion.management.FDPromotionModel' %>

<%@ taglib uri='template' prefix='tmpl' %>

<tmpl:insert template='site.jsp'>

    <tmpl:put name='title' direct='true'>Home Page</tmpl:put>

	<tmpl:put name='content' direct='true'>
		
        
        <script language="JavaScript">
  
      function deleteRestriction(url)
      {
        var config='width=300,height=400,menubar=yes,status=yes,scrollbars=yes,resizable=yes';

        //alert("url"+url);
        var doCancel = confirm ("Are you sure you want to delete these Restriction?");
        if(doCancel == false){
                return;
        } 
        
        pop = window.open (url,"deleteRestriction",config);
        return;
     }
    </script>
        
        
		<br/>	
		<div align="center">
			<form id="promotionListForm" action="" method="post">	
				<ec:table items="promotions" var="item"  action="${pageContext.request.contextPath}/promotions.do"
				    imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="Promotions List"
				    width="98%" form="promotionListForm" autoIncludeParameters="false" rowsDisplayed="25"  >
				    
				        
				    <ec:row>				    				    	                      
				      <ec:column property="name" title="Name"/>
                      <ec:column property="description" title="Description"/>
				      <ec:column property="maxAmount" title="Amount"/>
                      <ec:column property="startDate" title="Start Date"/>
                      <ec:column property="expirationDate" title="Expiry Date"/>
                      <ec:column property="redemptionCode" title="Redemption Code"/>                                                                                    
                      <ec:column property="id" title="EDIT" filterable="false" sortable="false"><a href="${pageContext.request.contextPath}/editRestriction.do?promotionCode=${pageScope.item.promotionCode}">EDIT</a></ec:column>                                          
                      <ec:column property="id" title="SEARCH" filterable="false" sortable="false"><a href="${pageContext.request.contextPath}/searchRestriction.do?promotionCode=${pageScope.item.promotionCode}">SEARCH</a></ec:column>                      
                      <ec:column property="id" title="ADD" filterable="false" sortable="false"><a href="${pageContext.request.contextPath}/appendRestriction.do?promotionCode=${pageScope.item.promotionCode}">ADD</a></ec:column>                      
                      <ec:column property="id" title="DELETE" filterable="false" sortable="false">                                            
                      <a href="javascript:deleteRestriction('${pageContext.request.contextPath}/deleteRestriction.do?promotionCode=${pageScope.item.promotionCode}');">
                      DELETE</a></ec:column>                      
				    </ec:row>
				  </ec:table>
			 </form> 	
		 </div>
		 <script>         
			//editRowHandlers('ec_table', 'rowMouseOver', 'editRestriction.do','promotionCode',0, 0);
		</script> 	
</tmpl:put>
</tmpl:insert>

