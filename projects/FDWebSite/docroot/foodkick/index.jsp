<%@ include file="includes/fk_presenter_vars.jspf" %>
<%@ page import="java.util.Calendar" %>
<tmpl:insert template='includes/fklayout_tmpl.jsp'>
	<tmpl:put name='title'>Same-Day Food Delivery NYC | FoodKick</tmpl:put>
	<tmpl:put name='content'>
		<fd:IncludeMedia name="/media/editorial/foodkick/landing.html" />
	</tmpl:put>
	<tmpl:put name='special_disclaimer'>
	<!-- begin special_disclaimer -->
		*This Offer is for free delivery on qualifying orders for a thirty (30) day period. Offer applies to first-time customers only. Free delivery means <span>no delivery or service fees</span>. <span>Eligible orders</span>
must (a) exceed minimum purchase requirements before taxes & fees, (b) be within eligible <span>delivery areas</span>, and (c) have a delivery window greater than one (1) hour. Delivery is subject to
availability. No promotion code necessary and the Offer will automatically apply starting with your first purchase and will continue for 30 days. This is a limited time Offer. All standard customer
terms and conditions apply. FoodKick reserves the right to cancel or modify this Offer at any time. Offer is nontransferable. Void where prohibited. All right reserved, Fresh Direct, LLC.
	<br /><br />
	You must be twenty-one or older to purchase alcohol. IT IS A VIOLATION PUNISHABLE UNDER LAW FOR ANY PERSON UNDER THE AGE OF TWENTY-ONE TO PRESENT ANY WRITTEN EVIDENCE OF AGE WHICH IS FALSE, FRAUDULENT 
	OR NOT ACTUALLY HIS OWN FOR THE PURPOSE OF ATTEMPTING TO PURCHASE ANY ALCOHOLIC BEVERAGE. Wines &amp; Spirits are sold by FreshDirect Wines &amp; Spirits, an independently owned store with NY State 
	License #1277181 | 620 5th Ave, Brooklyn, NY 11215, Tel: (718) 768-1521, M-W 1-10pm, Th-Sa 11am-10pm, Su Noon-8pm &copy;<%= Calendar.getInstance().get(Calendar.YEAR) %> FreshDirect, LLC dba FreshDirect Wines & Spirits.
	<!-- end special_disclaimer -->
	</tmpl:put>
	
	<tmpl:put name='special_js'>
	<!-- begin special_js -->
		<!--  <script src="<%=ANGULAR_DIR %>/angular.min.js"></script>
		<script src="<%=ANGULAR_DIR %>/angular-animate.min.js"></script>
		<script src="<%=ANGULAR_DIR %>/angular-sanitize.min.js"></script>
		<script src="<%=BOOTSTRAP_DIR %>/js/ui-bootstrap-tpls-0.14.3.min.js"></script>

		<link href="<%=BOOTSTRAP_DIR %>/css/bootstrap.min.css" rel="stylesheet" />
		-->
	
		<script src="<%=JS_DIR %>/jquery.slides.min.js"></script>
		<script type="text/javascript">
			window.IMAGES_DIR = "<%=IMAGES_DIR%>";
			window.CMS_IMAGES_DIR_LP = "<%=CMS_IMAGES_DIR_LP%>";
		</script>
		<script src="/media/editorial/foodkick/lp_top_carousel.js"></script>
	<!-- end special_js -->
	</tmpl:put>
</tmpl:insert>