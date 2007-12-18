<%@ page import='com.freshdirect.fdstore.promotion.*' %>
<%@ taglib uri="logic" prefix="logic" %>
<html>
<body>

<h2>Promotions</h2>

<logic:iterate id="promoType" collection="<%= EnumPromotionType.getEnumList() %>" type="com.freshdirect.fdstore.promotion.EnumPromotionType">
	<h3><%= promoType.getDescription() %></h3>
	
	<logic:iterate id="promoCode" collection="<%= PromotionFactory.getInstance().getPromotionCodesByType(promoType) %>" type="java.lang.String">
	
		<% PromotionI promo = PromotionFactory.getInstance().getPromotion(promoCode); %>
		<h4><%= promo.getDescription() %></h4>
		<pre><%= promo %></pre>
	
	</logic:iterate>
</logic:iterate>

</body>
</html>