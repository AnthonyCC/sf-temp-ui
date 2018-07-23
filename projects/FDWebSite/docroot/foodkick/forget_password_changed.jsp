<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ include file="includes/fk_presenter_vars.jspf" %>
<%
String userAgent = request.getHeader("user-agent");
String open_app_button = "";

if(userAgent.matches(".*iPhone.*") || userAgent.matches(".*iPad.*")){
	//open_app_button = "<button class='download_button purple' onclick='location.href=" + FK_IOSAPP_OPEN + "'>Open APP</button>";
	open_app_button = iosapp_button(FK_IOSAPP_OPEN, "Open APP", "purple");
}
%>
<tmpl:insert template='includes/fklayout_tmpl.jsp'>
<%-- 	<tmpl:put name='title'>Same-Day Food Delivery NYC | FoodKick: Changed your password</tmpl:put> --%>
   <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="Same-Day Food Delivery NYC | FoodKick: Changed your password"/>
   </tmpl:put>
	<tmpl:put name='content'>
		<section id="section_forget_password_changed" class="forgot_password_section">
			<img src="<%=IMAGES_DIR %>/checkmark.png" class="" width="100%" height="100%" /> <span>Your password has been reset.</span><br/>
			<%=open_app_button %>
		</section>
	</tmpl:put>
</tmpl:insert>