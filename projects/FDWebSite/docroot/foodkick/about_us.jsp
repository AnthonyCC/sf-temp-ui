<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ include file="includes/fk_presenter_vars.jspf" %>
<tmpl:insert template='includes/fklayout_tmpl.jsp'>
<%-- 	<tmpl:put name='title'>Same-Day Food Delivery NYC | FoodKick: About Us</tmpl:put> --%>
    <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="Same-Day Food Delivery NYC | FoodKick: About Us"/>
    </tmpl:put>
	<tmpl:put name='content'>
		<section id="section_about_us" class="section_plain_copy">
			<fd:IncludeMedia name="/media/editorial/foodkick/about_us.html" />
		</section>
	</tmpl:put>
</tmpl:insert>