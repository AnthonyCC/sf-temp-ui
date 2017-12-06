<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ include file="includes/fk_presenter_vars.jspf" %>
<%@ include file="includes/password_flow_vars.jspf" %>
<tmpl:insert template='includes/fklayout_tmpl.jsp'>
<%-- 	<tmpl:put name='title'>Same-Day Food Delivery NYC | FoodKick: Forgot Password</tmpl:put> --%>
	<tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="Same-Day Food Delivery NYC | FoodKick: Forgot Password"/>
    </tmpl:put>
	<tmpl:put name='content'>
		<script type="text/javascript">
			cmCreatePageviewTag("FORGETPASS: forget_password.jsp","FDX_FORGETPASS",null,null,"-_--_-FORGETPASS: forget_password.jsp-_--_--_--_-FORGETPASS: forget_password.jsp");
		</script>
		<section id="forgot_password_section" class="forgot_password_section">
			<fd:CheckLoginStatus guestAllowed='true' recognizedAllowed='true' id='user' />
			<fd:ForgotPasswordController results="result" successPage='${FKAPP_DIR}/forget_password_confirmation.jsp' password="password">	
				<%@ include file="/login/includes/forget_password.jspf" %>
			</fd:ForgotPasswordController>
		</section>
	</tmpl:put>
</tmpl:insert>