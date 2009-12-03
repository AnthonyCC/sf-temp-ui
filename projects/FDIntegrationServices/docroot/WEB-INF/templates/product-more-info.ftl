<#assign multiNutrition = product.multipleNutrition >
<html>
<head>
	<link type="text/css" rel="stylesheet" href="${request.contextPath}/css/base.css" title="Product Info WebKit View Stylesheet" media="all"/>
</head>

<body>

	<div id="main">
		<div id="header">
			<img src="${mediaPath}${productImage.path}" class"productImage" width="320" />
			<h1>${product.productTitle}</h1>

<#if product.layout != "wine" >
	<#if product.partiallyFrozen?has_content && product.department.name?upper_case == "BAKERY" >
			<div class="notice">
				<img src="${mediaPath}/media_stat/images/template/bakery/parbaked_frozen_prod.gif" />
			</div>
	</#if>
	<#if product.partiallyFrozen?has_content && product.department.name?upper_case == "SEAFOOD" >
			<div class="notice">
				<img src="${mediaPath}/media_stat/images/template/bakery/snowflake_grey.gif" />
			</div>
	</#if>
	<#if moreInfo.kosherSymbol?has_content >
			<div class="productIcons">
				<img src="${moreInfo.kosherSymbol.source}" />
			</div>
	</#if>
<#else>
	<#if product.wineTypeIcons?has_content >
			<div class="productIcons">
		<#list product.wineTypeIcons as wineTypeIcon>
			<img src="${wineTypeIcon}" />
		</#list>				
			</div>
	</#if>
	<#if product.ratingIcons?has_content >	
			<div class="productIcons">
		<#list product.ratingIcons as ratingIcon>
			<img src="${rating}" />
		</#list>				
			</div>
	</#if>			
</#if>
		</div>
		
	</div>
	
		<div class="detail">
		<p>
			${moreInfo.description?replace("\"/media","\"http://www.freshdirect.com/media")}}</p>
		<#if product.platter >
			<p class="warning">
				<b>Cancellation Notice</b><br/>
				* Orders for this item cancelled later than 3PM on the day before delivery (or 12 Noon on Fri., Sat. and Sun. and certain holidays) will be subject to a 50% fee.</p>
			</div>
		</#if>
		</div>
		<div class="detail">
	<#if product.layout != "wine" >
		<#if product.origin?has_content >
			<p><b>Origin:</b><#list product.origin as origin>${origin}</#list></p>
		</#if>
	</#if>	<#if product.layout == "wine" >
		<#if product.wineRegionLabel?has_content >
			<p><b>Region:</b> ${product.wineCountry} &gt; ${product.wineRegionLabel}</p>
		</#if>
		<#if product.grape?has_content >
			<p><b>Grape:</b> ${product.grape}</p>
		</#if>
		<#if product.importer?has_content >
			<p><b>Importer:</b> ${product.importer}</p>
		</#if>
		<#if product.alcohol?has_content >
			<p><b>Alcohol:</b> ${product.alcohol}</p>
		</#if>
	</#if>		
		</div>	
<#list moreInfo.skuCodes as skuCode>
		<#if multiNutrition >
		<div class="detail">
			<h3>${moreInfo.skuNames[skuCode]}</h3>
		</div>
		</#if>
		<#if moreInfo.ingredients[skuCode]?has_content >
		<div class="detail">
		<#if multiNutrition >
			<h4>Ingredients</h4>
		<#else>
			<h3>Ingredients</h3>
		</#if>
			<p>${moreInfo.ingredients[skuCode]}</p>
				<ul>
					<#list product.allergens as allergen>
					<li>${allergen}</li>
					</#list>
				</ul>
		</div>
		</#if>
		
		<#if moreInfo.nutritionFacts[skuCode]?has_content >
		<div class="detail">
		<#if multiNutrition >
			<h4>Nutrition Facts</h4>
		<#else>
			<h3>Nutrition Facts</h3>
		</#if>
			<div class="nutrition">
				${moreInfo.nutritionFacts[skuCode]}
			</div>
		</div>
		</#if>
</#list>		
<#if product.layout == "componentGroupMeal" > 	
<#list moreInfo.componentGroupMealProductMoreInfo as cgpMoreinfo>
	
	<div class="detail">
		<h3>${cgpMoreinfo.fullName}</h3>
		${cgpMoreinfo.description}
	</div>

	<#list cgpMoreinfo.skuCodes as skuCode>		
			<#if cgpMoreinfo.ingredients?size &gt; 0 >
			<div class="detail">
				<h4>Ingredients</h4>
				<p>${cgpMoreinfo.ingredients[skuCode]}</p>
					<ul>
						<#list cgpMoreinfo.allergens as allergen>
						<li>${allergen}</li>
						</#list>
					</ul>
			</div>
			</#if>
			
			<#if cgpMoreinfo.nutritionFacts?size &gt; 0 >
			<div class="detail">
				<h4>Nutrition Facts</h4>
				<div class="nutrition">
					${cgpMoreinfo.nutritionFacts[skuCode]}
				</div>
			</div>
			</#if>
	</#list>		
</#list>
</#if>
		<#if product.layout != "wine" >
		<#if product.partiallyFrozen?has_content>
		<div class="detail">
			<h3>May Arrive Frozen</h3>
			<p>${product.partiallyFrozen?replace("\"/media","\"http://www.freshdirect.com/media")}}</p>
		</div>
		</#if>
		</#if>
		<#if moreInfo.heatingInstructions?has_content >
		<div class="detail">
			<h3>Heating Instructions</h3>
			<p>${moreInfo.heatingInstructions?replace("\"/media","\"http://www.freshdirect.com/media")}</p>
		</div>
		</#if>
	
		<#if product.cutOffTime?has_content && product.platter>
		<div class="detail">
			<h3>Please Note</h3> 
			<p>PLEASE ORDER BY ${product.cutOffTime?string("h:mm a")} FOR DELIVERY TOMORROW </p>
			<p>To assure the highest quality, our chefs prepare this item to order. You must <b>complete checkout by ${product.cutOffTime?string("ha")}</b> to order this item for delivery tomorrow.</p>
		</div>
		</#if>

	</div>
</body>
</html>