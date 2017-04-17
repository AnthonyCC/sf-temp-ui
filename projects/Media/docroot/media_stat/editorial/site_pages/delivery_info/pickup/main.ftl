<#include "fd-macros.ftl" />
<#setting locale="en_US">
<#assign isPopup=parameters["isPopup"]/>
<#assign fromZipCheck=parameters["fromZipCheck"]/>
<#assign isPopupAndNotFromZipCheck=parameters["isPopupAndNotFromZipCheck"]/>
<#assign isUserEligibleForSignupPromotion=parameters["isUserEligibleForSignupPromotion"]/>
<#assign customerServiceContact=parameters["customerServiceContact"]/>
<#assign minimumOrderAmount=parameters["minimumOrderAmount"]/>
<#assign isUserSignedIn=parameters["isUserSignedIn"]/>
<#if isPopupAndNotFromZipCheck>
<img src="/media_stat/images/template/pickup/lic_pop_hdr.gif" width="402" height="20" alt="Pick Up Your Order at Our Facility">
<br><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8"><br>
Anyone in the Tri-State area (including existing home delivery customers) can now place an order online for pickup at our facility, located just outside the Midtown Tunnel in Long Island City, Queens. You may place your order up to one week in advance and as late as the night before. Order cutoff for next-day pickup is: 11 p.m. for weekday pickup, 9 p.m. for Saturday and Sunday pickup. Orders may be picked up seven days a week, between 1:00 p.m. and 9:00 p.m. on weekdays and on Saturdays and Sundays between 9:00 a.m. and 5:00 p.m.
<table cellpadding="0" cellspacing="3" border="0">
<tr><td colspan="4"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8"></td></tr>
<tr><td width="30">&nbsp;</td><td><b>Monday, Tuesday, Thursday, Friday</b></td><td width="15" rowspan="2"></td><td>1pm to 9pm</td></tr>
<tr><td width="30">&nbsp;</td><td><b>Saturday &amp; Sunday</b></td><td>9am to 5pm</td></tr>
</table>
<#else>
<img src="/media_stat/images/template/help/about_pickup.gif" width="259" height="13" alt="About pickup at FreshDirect"><br><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="14"><br>
You don't have to live in an area where FreshDirect offers home delivery to get the best food at the best prices in New York! Anyone in the Tri-State area (including existing home delivery customers) can now place an order online for pickup at our facility, located just outside the Midtown Tunnel in Long Island City, Queens. It's also very convenient if you're headed out to the Hamptons or Fire Island.
<#if isUserEligibleForSignupPromotion><br><br>
    <font class="text13"><font color="#CC0000"><b>Pickup orders are not eligible for our free, fresh food promotion.</b></font><br>
    The free, fresh food promotion applies to home and depot delivery only. <#if isPopup><#if fromZipCheck><a href="/shared/promotion_popup.jsp?pickup=lic&zipCheck=yes">Click here for details</a><#else><a href="/shared/promotion_popup.jsp?pickup=lic">Click here for details</a></#if><#else><#if fromZipCheck><a href="javascript:popup('/shared/promotion_popup.jsp','large')">Click here for details</a><#else><a href="javascript:popup('/shared/promotion_popup.jsp?zipCheck=yes','large')">Click here for details</a></#if></#if>. When you place your first home or depot delivery order you will be eligible for any promotions in effect at that time.
    </font>
</#if><!-- isUserEligibleForSignupPromotion -->
<br><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="18"><br>
<img src="/media_stat/images/template/help/delivery/lic_how_it_works.gif" width="97" height="10" alt="How it works">
<font class="space4px"><br></font>
<#if isPopup || isUserSignedIn>Sign up<#else><a href="/registration/signup.jsp">Sign up</a></#if>, browse our site for your favorite foods, and complete Checkout to place your order. When you check out, select our pickup location and the day you'd like to pick up your order. Anytime during our pickup hours, park your car by the "Order Pickup" sign at our facility. Go through the orange door below the sign, where a uniformed attendant will be waiting to check your ID and have your order loaded into your car. See below for directions to FreshDirect.
<br><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="18"><br>
<img src="/media_stat/images/template/help/delivery/lic_our_pickup_times.gif" width="120" height="10" alt="Our Pick Up Times">
<font class="space4px"><br></font>
You may place your order up to one week in advance and as late as the night before. Order cutoff for next-day pickup is 11 p.m. for weekday pickup, 9 p.m. for Saturday and Sunday pickup. Orders may be picked up between 1 p.m. and 9 p.m. on weekdays and on Saturdays and Sundays between 9 a.m. and 5 p.m.
<br><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="18"><br>
<img src="/media_stat/images/template/help/delivery/fd_box.jpg" width="150" height="77" align="right" hspace="6" alt="FreshDirect Box">
<img src="/media_stat/images/template/help/delivery/lic_handled_extra_care.gif" width="172" height="10" alt="Handled with Extra Care">
<font class="space4px"><br></font>
Fresh foods need extra care in handling, and we do all the right things to make sure your food gets to you in top shape. After we prepare everything to fill your order, our trained packers carefully assemble it in sturdy boxes. We keep the boxes in our refrigerated environment, keeping everything cool and dry until you arrive to pick it up.
<br><br>
<b>Please note:</b> Prepaid credit card orders only. There is no charge for pickup. The minimum order is &#36;${minimumOrderAmount}.
</#if><!-- isPopupAndNotFromZipCheck -->
<br><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="18"><br>
<img src="/media_stat/images/template/help/delivery/lic_how_to_get_to_FD.gif" width="182" height="10" alt="Handled with Extra Care">
<#if isPopupAndNotFromZipCheck ><br><br><img src="/media_stat/images/template/pickup/lic_pop_map.gif" width="499" height="266"><br><br><#else><font class="space4px"><br></font></#if>
<b>From Manhattan - </b><a href="/media_stat/pdf/map_manhattan.pdf" target="fd_pdf">download printable map &amp; directions</a>
<ul>
<li>Take the Queens Midtown Tunnel -- stay right after exiting the tunnel and choose the far right toll both.</li>
<li>Exit sharp right IMMEDIATELY after the toll, then make a quick left onto Borden Avenue.</li>
<li>Follow Borden Avenue for 1/4 mile to FreshDirect, the large yellow building on the right side of the street -- the pickup area is in the middle of the building, just past the truck bays, under the "Order Pickup" sign.</li>
</ul>
<b>From Long Island/Queens - </b><a href="/media_stat/pdf/map_li_queens.pdf" target="fd_pdf">download printable map &amp; directions</a>
<ul>
<li>Take the LIE/495 West toward the Queens Midtown Tunnel.</li>
<li>Take the last exit before the toll, Exit #15, Van Dam Street.</li>
<li>Continue straight through the light onto Borden Avenue -- it will jog left under the LIE overpass, then quickly right again. You'll cross over a small bridge and pass under a railway trestle (you'll also see our large Jumbotron with a red pole).</li>
<li>FreshDirect is the large yellow building on the left side of the street -- the pickup area is in the middle of the building, just in front of the truck bays, under the "Order Pickup" sign.</li>
</ul>
<b>From Brooklyn - </b><a href="/media_stat/pdf/map_brooklyn.pdf" target="fd_pdf">download printable map &amp; directions</a>
<ul>
<li>Take the BQE/I-278 East toward the Triboro Bridge.</li>
<li>Take Exit #35, Midtown Tunnel/LIE, and bear left immediately, toward the Queens Midtown Tunnel (you'll go under an underpass).</li>
<li>Bear left onto the LIE and almost immediately take Exit #15, Van Dam Street, the last exit before the toll.</li>
<li>Continue straight through the light onto Borden Avenue -- it will jog left under the LIE overpass, then quickly right again. You'll cross over a small bridge and pass under a railway trestle  (you'll also see our large Jumbotron with a red pole).</li>
<li>FreshDirect is the large yellow building on the left side of the street -- the pickup area is in the middle of the building, just in front of the truck bays, under the "Order Pickup" sign.</li>
</ul>
<b>From the Bronx - </b><a href="/media_stat/pdf/map_bronx.pdf" target="fd_pdf">download printable map &amp; directions</a>
<ul>
<li>Cross the Triboro Bridge toward Queens.</li>
<li>Take Exit #4, BQE West.</li>
<li>Follow the BQE to Exit #35, Queens Midtown Tunnel.</li>
<li>Bear left onto the LIE and almost immediately take Exit #15, Van Dam Street, the last exit before the toll.</li>
<li>Continue straight through the light onto Borden Avenue -- it will jog left under the LIE overpass, then quickly right again. You'll cross over a small bridge and pass under a railway trestle  (you'll also see our large Jumbotron with a red pole).</li>
<li>FreshDirect is the large yellow building on the left side of the street -- the pickup area is in the middle of the building, just in front of the truck bays, under the "Order Pickup" sign.</li>
</ul>
<br>
<#if !isPopupAndNotFromZipCheck><div align="center"><b>New!</b> Call our Order Pickup desk at 718-928-1575 five to ten minutes before you arrive and we'll have your order ready and waiting when you pull up. If you get lost - or have questions about your account or previous orders - call our Customer Service department at ${customerServiceContact}.</b></div><br></#if>
<#if isPopup><img src="/media_stat/images/layout/clear.gif" alt="" width="24" height="1"><img src="/media_stat/images/template/pickup/lic_pop_facility.jpg" width="408" height="225" vspace="10"></#if>
