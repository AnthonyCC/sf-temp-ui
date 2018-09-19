<#include "fd-macros.ftl" />
<#setting locale="en_US"/>
<#assign isPageNull=parameters["isPageNull"]/>
<#if isPageNull>
<#assign tableWidth=375/>
<#assign hdrImgName="faq_hdr_pop_what.gif"/>
<#assign hdrImgWidth=380/>
<#assign fruitDeptLink="javascript:linkTo('/department.jsp?deptId=fru&trk=faq')"/>
<#assign vegetableDeptLink="javascript:linkTo('/department.jsp?deptId=veg&trk=faq')"/>
<#assign meatDeptLink="javascript:linkTo('/department.jsp?deptId=mea&trk=faq')"/>
<#assign seafoodDeptLink="javascript:linkTo('/department.jsp?deptId=sea&trk=faq')"/>
<#assign deliDeptLink="javascript:linkTo('/department.jsp?deptId=del&trk=faq')"/>
<#assign cheeseDeptLink="javascript:linkTo('/department.jsp?deptId=che&trk=faq')"/>
<#assign dairyDeptLink="javascript:linkTo('/department.jsp?deptId=dai&trk=faq')"/>
<#assign coffeeDeptLink="javascript:linkTo('/department.jsp?deptId=cof&trk=faq')"/>
<#assign teaDeptLink="javascript:linkTo('/department.jsp?deptId=tea&trk=faq')"/>
<#assign pastaDeptLink="javascript:linkTo('/department.jsp?deptId=pas&trk=faq')"/>
<#assign bakeryDeptLink="javascript:linkTo('/department.jsp?deptId=bak&trk=faq')"/>
<#assign kitchenDeptLink="javascript:linkTo('/department.jsp?deptId=hmr&trk=faq')"/>
<#assign groceryDeptLink="javascript:linkTo('/department.jsp?deptId=gro&trk=faq')"/>
<#assign frozenDeptLink="javascript:linkTo('/department.jsp?deptId=fro&trk=faq')"/>
<#assign specialtyDeptLink="javascript:linkTo('/department.jsp?deptId=spe&trk=faq')"/>
<#else>
<#assign tableWidth=500>
<#assign hdrImgName="faq_hdr_what.gif"/>
<#assign hdrImgWidth=453/>
<#assign fruitDeptLink="/department.jsp?deptId=fru&trk=faq"/>
<#assign vegetableDeptLink="/department.jsp?deptId=veg&trk=faq"/>
<#assign meatDeptLink="/department.jsp?deptId=mea&trk=faq"/>
<#assign seafoodDeptLink="/department.jsp?deptId=sea&trk=faq"/>
<#assign deliDeptLink="/department.jsp?deptId=del&trk=faq"/>
<#assign cheeseDeptLink="/department.jsp?deptId=che&trk=faq"/>
<#assign dairyDeptLink="/department.jsp?deptId=dai&trk=faq"/>
<#assign coffeeDeptLink="/department.jsp?deptId=cof&trk=faq"/>
<#assign teaDeptLink="/department.jsp?deptId=tea&trk=faq"/>
<#assign pastaDeptLink="/department.jsp?deptId=pas&trk=faq"/>
<#assign bakeryDeptLink="/department.jsp?deptId=bak&trk=faq"/>
<#assign kitchenDeptLink="/department.jsp?deptId=hmr&trk=faq"/>
<#assign groceryDeptLink="/department.jsp?deptId=gro&trk=faq"/>
<#assign frozenDeptLink="/department.jsp?deptId=fro&trk=faq"/>
<#assign specialtyDeptLink="/department.jsp?deptId=spe&trk=faq"/>
</#if>
<script>
function linkTo(url){
	redirectUrl = "http://" + location.host + url;
	parent.opener.location = redirectUrl;
}
</script>


<A NAME="top"></A>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
		<TD>
		<img src="/media_stat/images/template/help/${hdrImgName}" width="${hdrImgWidth}" height="30" alt="" border="0">
	   </TD>
	</TR>
</TABLE>

<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
		<TD WIDTH="${tableWidth}" class="bodyCopy">
			<IMG src="/media_stat/images/layout/cccccc.gif" ALT="" WIDTH="375" HEIGHT="1"><BR>
			<img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"><br>
			<ul>
				<li><A HREF="#question1">What is FreshDirect?</A></li>
				<li><A HREF="#question2">Why is FreshDirect fresher AND less expensive than a supermarket?</A></li>
				<li><A HREF="#question4">What kind of food does FreshDirect sell?</A></li>
				<li><A HREF="#question8">Where is my order prepared?</A></li>
				<li><A HREF="#question5">Does FreshDirect offer organic products?</A></li>
				<li><A HREF="#question6">How can I find out more about FreshDirect?</A></li>
			</ul>
	   </TD>
	</TR>
</TABLE>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>
<A NAME="question1"></A>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
		<TD WIDTH="${tableWidth}" class="bodyCopy">
			<b>What is FreshDirect?</b><BR>
			FreshDirect is the new way to shop for food. We've hired New York's best food experts, built the perfect environment for the food, and found the shortest distance from farms, dairies, and fisheries to your table. We have all the irresistibly fresh foods you could want, plus popular grocery brands, all for less than you're paying now. And we bring it to your door.<BR>
			 
	   </TD>
	</TR>
</TABLE>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>
<A NAME="question2"></A>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
		<TD WIDTH="${tableWidth}" class="bodyCopy">
			<b>Why is FreshDirect fresher AND less expensive than a supermarket?</b><BR>
			Buying our fresh foods directly from the source, bypassing the usual layers of distributors and middlemen, means the food on your table will be four to seven days fresher (and significantly less expensive) than if you bought it from a typical supermarket. Our food-friendly facility lets us do much of our food preparation ourselves, like roasting our own green coffee beans, dry-aging our own prime beef, and baking our own breads and pastries. And because we don't have a retail location, we don't pay expensive rent for retail space. These factors help to keep our food fresh and our costs low, and we pass the savings, and the quality, on to you.<br>
			 
	   </TD>
	</TR>
</TABLE>

<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>
<A NAME="question4"></A>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
		<TD WIDTH="${tableWidth}" class="bodyCopy">
			<b>What kind of food does FreshDirect sell?</b><BR>
			We specialize in perishables, offering over 3,000 varieties of high-quality fresh food from the best sources of the season.
			<br><br>
			Browse around in our departments and see what we have &#151; irresistibly fresh <a href="${fruitDeptLink}">fruit</a>, <a href="${vegetableDeptLink}">vegetables</a>, <a href="${meatDeptLink}">meat</a>, and <a href="${seafoodDeptLink}">seafood</a>, fine <a href="${cheeseDeptLink}">cheese</a> and <a href="${deliDeptLink}">deli</a> goods, <a href="${dairyDeptLink}">dairy</a>, freshly roasted <a href="${coffeeDeptLink}">coffee</a>, and loose <a href="${teaDeptLink}">tea</a>, fresh <a href="${bakeryDeptLink}">breads</a> and <a href="${bakeryDeptLink}">pastries</a> daily. We also make a full line of <a href="${kitchenDeptLink}">meals</a> prepared by New York restaurant chefs, which you can heat and enjoy at home in minutes.
			<br><br>
			Of course we have <a href="${specialtyDeptLink}">specialty</a> foods like fine chocolate, olive oil, and aged balsamico. But we also carry a full selection of the most popular <a href="${frozenDeptLink}">frozen</a> foods and <a href="${groceryDeptLink}">packaged</a> brands of beverages, household items, canned goods, and cereal, so you can do all of your shopping in one stop.			
			<br>
			 
	   </TD>
	</TR>
</TABLE>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>
<A NAME="question8"></A>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
		<TD WIDTH="${tableWidth}" class="bodyCopy">
			<b>Where is my order prepared?</b> <BR>
			We prepare and package all orders in our fully refrigerated, state-of-the-art facility just east of Manhattan in Long Island City. This clean, cool environment is designed solely for food and staffed with specialized experts who know their products inside and out. We follow USDA guidelines and the HACCP food safety system in all our fresh storage and production rooms. In other words, we adhere to strict health and safety regulations exceeding national standards of food excellence.
	   </TD>
	</TR>
</TABLE>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>
<A NAME="question5"></A>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
		<TD WIDTH="${tableWidth}" class="bodyCopy">
			<b>Does FreshDirect offer organic products?</b><BR>
			Yes. We offer a variety of organic fruit, vegetables, dairy, meat, poultry, and grocery items and we are expanding our selection all the time.<BR>
			 
	   </TD>
	</TR>
</TABLE>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>
<A NAME="question6"></A>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
		<TD WIDTH="${tableWidth}" class="bodyCopy">
			<b>How can I find out more about FreshDirect?</b><BR>
			<a href="/about/index.jsp">Click here</a> for more information on how we're able to bring you the freshest, best-tasting, lowest-priced food around.<BR>
			 
	   </TD>
	</TR>
</TABLE>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
	<TR VALIGN="TOP">
		<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
		<TD WIDTH="${tableWidth}" class="bodyCopy">
			<A HREF="#top"><img src="/media_stat/images/template/help/up_arrow.gif" width="17" alt="" height="9" hspace="0" vspace="4" border="0" align="left"><img src="/media/images/layout/clear.gif" width="6" height="1" border="0">top of page</A>
			<br><br><BR>			
	   </TD>
	</TR>
</TABLE>

