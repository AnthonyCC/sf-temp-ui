<%@ include file="includes/fk_presenter_vars.jspf" %>
<%@ include file="includes/password_flow_vars.jspf" %>
<tmpl:insert template='includes/fklayout_tmpl.jsp'>
	<tmpl:put name='title'>Same-Day Food Delivery NYC | FoodKick: Create New Password</tmpl:put>
	<tmpl:put name='content'>
		<fd:CheckLoginStatus id="user" />
		<script type="text/javascript">
			cmCreatePageviewTag("FORGETPASSRETPASS: retrieve_password.jsp","FDX_FORGETPASSRETPASS",null,null,"-_--_-FORGETPASSRETPASS: retrieve_password.jsp-_--_--_--_-FORGETPASSRETPASS: retrieve_password.jsp");
		</script>
		<% String fName = user.getFirstName(); %>
		<section id="forgot_password_retrieve_section" class="forgot_password_section">
			<fd:ForgotPasswordController results="result" successPage='${FKAPP_DIR}/forget_password_changed.jsp' password="password">	
				<%@ include file="/login/includes/retrieve_password.jspf" %>	
			</fd:ForgotPasswordController>
		</section>
	</tmpl:put>
</tmpl:insert>