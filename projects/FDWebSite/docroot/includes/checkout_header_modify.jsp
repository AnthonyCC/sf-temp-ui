<%@ taglib uri='template' prefix='tmpl' %>
<div class="checkout-header modify">
	<div class="leftcell"><b>Modifying order:</b> <tmpl:get name="ordnumb" /></div>
	<div class="rightcell"><span class="delivery-fee"><tmpl:get name="delivery-fee"/></span><a class="imgButtonWhite cancel_updates" href="/your_account/cancel_modify_order.jsp">cancel updates</a><tmpl:get name="next-button"/></div>
	<hr class="modify-order-hr">
	<div class="modify-order-note"><i><tmpl:get name="note" /></i></div>
	<div class="title title18"><tmpl:get name="title" /></div>
</div>
