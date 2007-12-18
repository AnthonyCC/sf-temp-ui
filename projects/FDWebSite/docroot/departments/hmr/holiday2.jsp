<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.attributes.Attribute' %>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed="true" />
<tmpl:insert template='/common/template/dnav.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Our Valentine's Day Menu</tmpl:put>
    <tmpl:put name='content' direct='true'>
	<table cellpadding="0" cellspacing="0" border="0" width="710">
	<tr><td colspan="3" align="center" class="title17"><img src="/media_stat/images/template/kitchen/valentine_title.gif" width="601" height="61" alt="Our Valentine's Day Menu"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4"><br><font face="Arial, Helvetica, sans-serif">Let the former Executive Chef of One If by Land, Two If by Sea<br>prepare you a romantic and sexy Valentine's Day Dinner</font><br><img src="/media_stat/images/layout/clear.gif" width="1" height="18"></td></tr>
	<tr>
	<td rowspan="3"><img src="/media_stat/images/template/kitchen/cherub_l.gif" width="99" height="78"></td>
	<td><img src="/media_stat/images/template/kitchen/hearts2.gif" width="488" height="8"></td>
	<td rowspan="3" align="right"><img src="/media_stat/images/template/kitchen/cherub_r.gif" width="101" height="75"></td>
	</tr>
	<tr>
	<td class="text12"><img src="/media_stat/images/layout/clear.gif" width="1" height="6"><br>
	<b>Forget about last minute reservations, over-priced restaurants, or preparing dinner for Valentine's Day</b>. This year stay at home and let our chef <a href="javascript:pop('/media/editorial/manager_bios/david_mcInerney.jsp',400,585)"><b>David McInerney</b></a> prepare a romantic fondue dinner for you and your Valentine. What could be more sexy and fun than teasing your partner with long stem strawberries dipped in melted chocolate? We'll bring everything to your door (even the next morning's breakfast). All you have to do is heat, dip and enjoy.<br><img src="/media_stat/images/layout/clear.gif" width="1" height="8"></td>
	</tr>
	<tr>
	<td><img src="/media_stat/images/template/kitchen/hearts2.gif" width="488" height="8"></td>
	</tr>
	<%-- spacer --%>
	<tr>
	<td><img src="/media_stat/images/layout/clear.gif" width="111" height="22"></td>
	<td><img src="/media_stat/images/layout/clear.gif" width="488" height="22"></td>
	<td><img src="/media_stat/images/layout/clear.gif" width="111" height="22"></td>
	</tr>
	<tr><td align="center" class="text12" colspan="3">
	<img src="/media_stat/images/template/kitchen/title_cheese.gif" width="328" height="22" alt="Classic Cheese Fondue for Two  $64.99"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="12">
	<b>A classic blend of Gruyere, Emmental Swiss and white wine,<br>with a touch of garlic and spices.</b><br>
	Served with French baguettes, rye bread, ham, creamer potatoes, broccoli,<br>cauliflower and apples for dipping.  White and red grapes on the side.<br>
	<b>Includes Caesar Salad, Chocolate Fondue Dessert, and Breakfast in Bed.</b>
	<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10"><br>
	<i>Price also includes fondue pot, forks and heating element.<br>($39.99 for only the food.)</i>
	<br><img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br>
	<img src="/media_stat/images/template/kitchen/xoxoxo.gif" width="47" height="9">
	<br><img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br>
	<img src="/media_stat/images/template/kitchen/title_seafood.gif" width="273" height="23" alt="Seafood Fondue for Two  $84.99"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="12">
	<b>An assortment of lobster, salmon, sea scallops, shrimp and striped bass</b><br>
	with fennel, cremini mushrooms and creamer potatoes.<br>
	Your choice of cooking method: Bouillabaisse Broth or Traditional Fondue in Oil.<br>
	<b>Includes 5 dipping sauces, Caesar Salad, Chocolate Fondue Dessert,<br>and Breakfast in Bed.</b>
	<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10"><br>
	<i>Price also includes fondue pot, forks and heating element.<br>($59.99 for only the food.)</i>
	<br><img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br>
	<img src="/media_stat/images/template/kitchen/xoxoxo.gif" width="47" height="9">
	<br><img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br>
	<img src="/media_stat/images/template/kitchen/title_meat.gif" width="254" height="23" alt="Meat Fondue for Two  $94.99"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="12">
	<b>An assortment of cubed filet mignon, lamb loin and prime rib</b><br>
	with crimini mushrooms, sweet potatoes, and cauliflower.<br>
	Your choice of cooking method: Seasoned Beef Broth or Traditional Fondue in Oil<br>
	<b>Includes 5 dipping sauces, Caesar Salad, Chocolate Fondue Dessert,<br>and Breakfast in Bed</b>
	<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10"><br>
	<i>Price also includes fondue pot, forks and heating element.<br>($69.99 for only the food.)</i>
	</td></tr>
	</table>
	<table cellpadding="0" cellspacing="0" border="0" width="621">
	<%-- spacer --%>
	<tr>
	<td><img src="/media_stat/images/layout/clear.gif" width="199" height="20"></td>
	<td><img src="/media_stat/images/layout/clear.gif" width="12" height="20"></td>
	<td><img src="/media_stat/images/layout/clear.gif" width="199" height="20"></td>
	<td><img src="/media_stat/images/layout/clear.gif" width="12" height="20"></td>
	<td><img src="/media_stat/images/layout/clear.gif" width="199" height="20"></td>
	</tr>
	<tr><td colspan="5"><table cellpadding="0" cellspacing="0" border="0" width="100%"><tr valign="middle"><td><img src="/media_stat/images/template/kitchen/hearts1.gif" width="109" height="7"></td><td class="text13" align="center"><font face="Arial, Helvetica, sans-serif"><b>ALL OF OUR VALENTINE'S DAY FONDUE DINNERS ALSO INLUDE:</b></font></td><td align="right"><img src="/media_stat/images/template/kitchen/hearts1.gif" width="109" height="7"></td></tr><tr><td colspan="3"><img src="/media_stat/images/layout/clear.gif" width="1" height="14"></td></tr></table></td></tr>
	<tr align="center" valign="top">
	<td><img src="/media_stat/images/template/kitchen/title_salad.gif" width="102" height="14" alt="Caesar Salad"></td>
	<td rowspan="2"></td>
	<td><img src="/media_stat/images/template/kitchen/title_dessert.gif" width="197" height="14" alt="Chocolate Fondue Dessert"></td>
	<td rowspan="2"></td>
	<td><img src="/media_stat/images/template/kitchen/title_breakfast.gif" width="137" height="19" alt="Breakfast in Bed"></td>
	</tr>
	<tr align="center" valign="top">
	<td class="text12">Crisp hearts of romaine lettuce<br>with our special Caesar dressing,<br>a bold blend of garlic, lemon,<br>Dijon mustard, anchovy and<br>olive oil. Topped with croutons<br>and Parmesan cheese.</td>
	<td class="text12">Melted semi-sweet chocolate and Grand Marnier poured into a hollow freshly baked chocolate boule. This perfect Valentine's Day dessert includes long stem strawberries, golden pineapples, baby bananas and pound cake.</td>
	<td class="text12">Our breakfast includes assorted par-baked butter, chocolate and almond croissants, freshly roasted coffee, freshly squeezed orange juice and a fresh fruit salad.</td>
	</tr>
	<tr><td colspan="5" align="center"><img src="/media_stat/images/layout/clear.gif" width="1" height="20">
	<br><img src="/media_stat/images/template/kitchen/hearts3.gif" width="621" height="7"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="3"></td></tr>
	<tr><td colspan="5" bgcolor="#CC3333" align="center">
	<img src="/media_stat/images/layout/clear.gif" width="1" height="6"><br>
	<span class="title24"><font face="Arial Narrow, Arial, Helvetica, sans-serif" color="#FFFFFF"><b>To order or get more information call us toll free 1-866-283-7374.</b></font></span><br>
	<span class="title17"><font face="Arial Narrow, Arial, Helvetica, sans-serif" color="#FFFFFF">
	<b>(Sunday through Friday from 9 a.m. to midnight and Saturday from 9 a.m to 9 p.m.)</b></font></span>
	<br><img src="/media_stat/images/layout/clear.gif" width="1" height="8"></td></tr>
	<tr><td colspan="5" class="text15" align="center"><img src="/media_stat/images/layout/clear.gif" width="1" height="8"><br><b><span class="title16"><font face="Arial, Helvetica, sans-serif">PHONE ORDERS ONLY. $4.95 DELIVERY FEE.</font></span><br><font face="Arial, Helvetica, sans-serif">ORDERS MUST BE PLACED BY 6PM, FEBRUARY 11 FOR DELIVERY FEBRUARY 13 OR 14.</b></font><br><img src="/media_stat/images/layout/clear.gif" width="1" height="10"></td></tr>
	<tr><td colspan="5"><img src="/media_stat/images/template/kitchen/hearts3.gif" width="621" height="7"></td></tr>
	<tr><td colspan="5" align="center"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="10"><br>
	<img src="/media_stat/images/template/kitchen/fondue_made_easy.gif" width="402" height="61"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td></tr>
	<tr valign="top">
	<td><img src="/media_stat/images/template/kitchen/step1.gif" width="196" height="143"></td>
	<td rowspan="3"></td>
	<td><img src="/media_stat/images/template/kitchen/step2.gif" width="196" height="143"></td>
	<td rowspan="3"></td>
	<td><img src="/media_stat/images/template/kitchen/step3.gif" width="196" height="143"></td>
	</tr>
	<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="8"></td><td></td><td></td></tr>
	<tr valign="top">
	<td class="text12"><font face="Arial Black, Arial, sans-serif">STEP 1 : HEAT</font><br><img src="/media_stat/images/layout/clear.gif" width="1" height="5"><br>
	Heat cheese, oil or stock in a<br>
	small saucepot over low heat<br>
	until melted or simmering.</td>
	<td class="text12"><font face="Arial Black, Arial, sans-serif">STEP 2 : SET THE TABLE</font><br><img src="/media_stat/images/layout/clear.gif" width="1" height="5"><br>
	Carefully transfer cooking liquid into the included fondue pot and light heating element.
	<br><img src="/media_stat/images/layout/clear.gif" width="1" height="8"><br>
	Serve bread, meat or seafood on plates with dipping sauces.</td>
	<td class="text12"><font face="Arial Black, Arial, sans-serif">STEP 3 : FEEL THE LOVE</font><br><img src="/media_stat/images/layout/clear.gif" width="1" height="5"><br>
	Start enjoying the romantic, hands-on experience of eating fondue for two.</td>
	</tr>
	<tr><td colspan="5" class="text9"><br><br><font face="Arial Black, Arial, sans-serif">PLEASE NOTE: THESE DIRECTIONS ARE FOR ILLUSTRATION PURPOSES ONLY. A COMPLETE LIST OF INSTRUCTIONS FOR THE REHEATING AND COOKING OF THE FONDUE DINNER WILL BE INCLUDED WITH EACH ORDER.</font></td></tr>
	<tr><td colspan="5"><img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br><img src="/media_stat/images/template/kitchen/hearts3.gif" width="621" height="7"></td></tr>
	</table>
    </tmpl:put>	
</tmpl:insert>