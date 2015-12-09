<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<%@ include file="includes/fk_core_settings.jspf" %>
<tmpl:insert template='includes/fklayout_tmpl.jsp'>
	<tmpl:put name='title'>Same-Day Food Delivery NYC | FoodKick: Changed your password</tmpl:put>
	<tmpl:put name='content'>
		<section id="section_forget_password_changed" class="forgot_password_section">
			<img src="<%=SVG_SRC %>checkmark.svg" class="" /> <span>Your password has been reset.</span>
		</section>
	</tmpl:put>
</tmpl:insert>