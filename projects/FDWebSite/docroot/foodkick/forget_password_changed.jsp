<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="java.util.*"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<%@ include file="includes/fk_core_settings.jspf" %>
<%
String userAgent = request.getHeader("user-agent");
String open_app_button = "";

if(userAgent.matches(".*iPhone.*") || userAgent.matches(".*iPad.*")){
	open_app_button = "<button class='download_button purple' onclick='location.href=" + FK_IOSAPP_OPEN + "'>Open APP</button>";
}
%>
<tmpl:insert template='includes/fklayout_tmpl.jsp'>
	<tmpl:put name='title'>Same-Day Food Delivery NYC | FoodKick: Changed your password</tmpl:put>
	<tmpl:put name='content'>
		<section id="section_forget_password_changed" class="forgot_password_section">
			<img src="<%=SVG_SRC %>checkmark.svg" class="" /> <span>Your password has been reset.</span><br/>
			<%=open_app_button %>
		</section>
	</tmpl:put>
</tmpl:insert>