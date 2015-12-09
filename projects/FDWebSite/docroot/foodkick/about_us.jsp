<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<%@ include file="includes/fk_core_settings.jspf" %>
<tmpl:insert template='includes/fklayout_tmpl.jsp'>
	<tmpl:put name='title'>A fresh kick: About Us</tmpl:put>
	<tmpl:put name='content'>
		<section id="section_about_us" class="section_plain_copy">
			<fd:IncludeMedia name="/media/editorial/foodkick/about_us.html" />
		</section>
	</tmpl:put>
</tmpl:insert>