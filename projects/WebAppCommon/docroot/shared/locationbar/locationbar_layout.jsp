<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib prefix="fd" uri="freshdirect" %>
<div id="locationbar"><hr class="topbg"/><span class="zipdisplay"><label>Your ZIP Code: </label><span class="address"><tmpl:get name="zipdisplay" /></span></span><span class="hoicon"><tmpl:get name="hoicon" /></span><span class="buttons"><tmpl:get name="buttons" /></span></div>
<div id="location-messages" class="invisible"><tmpl:get name="location_message" /></div>
<div id="unrecognized" class="invisible" data-type="sitemessage">
	<div class="unrecognized error-message">
		<div class="error-message-text">
			<span class="orange">Sorry, we're unable to recognize the ZIP code <b>{{zip}}</b> you entered, please make sure it's entered correctly.</span><br>
			<span><a href="/help/delivery_zones.jsp" class="delivery-popuplink green">Click here</a> to see current delivery zones. To enter a different zip code, please enter in the box to the upper left.</span>
		</div>
	</div>
</div>
<fd:javascript src="/assets/javascript/locationbar.js" />