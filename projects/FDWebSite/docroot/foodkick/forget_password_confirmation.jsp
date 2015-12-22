<%@ include file="includes/fk_presenter_vars.jspf" %>
<%@ include file="includes/password_flow_vars.jspf" %>
<tmpl:insert template='includes/fklayout_tmpl.jsp'>
	<tmpl:put name='title'>Same-Day Food Delivery NYC | FoodKick: Forgot Password Confirmation</tmpl:put>
	<tmpl:put name='content'>
		<section id="forgot_password_confirmation_section" class="forgot_password_section">
			<%@ include file="/login/includes/forget_password_confirmation.jspf" %>
		</section>
	</tmpl:put>
</tmpl:insert>