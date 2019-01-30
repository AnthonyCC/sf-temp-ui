<%@ page import="com.freshdirect.customer.ErpCustomerInfoModel"%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="crm" prefix="crm" %>
<%@ taglib uri="freshdirect" prefix="fd" %>

<tmpl:insert template='/template/top_nav_changed_dtd.jsp'>

    <tmpl:put name='title' direct='true'>Account Details > Edit Name & Contact Info</tmpl:put>

	<tmpl:put name='content' direct='true'>
<script src="/assets/javascript/webpurify.jQuery.js" type="text/javascript" charset="utf-8"></script>

		
		<script type="text/javascript">
			$jq(document).ready(function() {
				$jq.webpurify.init("<%=FDStoreProperties.getProfanityCheckURL()%>","<%=FDStoreProperties.getProfanityCheckPass()%>");
			});
			
			function checkForProfanity(){
				if($jq("#displayName").val().length>0)
				{
					$jq.webpurify.check( jQuery("#displayName").val(), function(isProfane){
						if(!isProfane) {
							document.name_contact_info.submit();
						} else {
							$jq("#profaneText").html("That Display Name is invalid. Please enter a different Display Name.");
							return false;
						}
					});
				}
				else
				{
					document.name_contact_info.submit();
				}
			}
		</script>
		<div class="cust_module" style="float: none;">
		
		</div>
	<br clear="all">
	</tmpl:put>

</tmpl:insert>
