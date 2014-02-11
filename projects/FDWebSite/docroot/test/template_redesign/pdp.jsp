<%@ page import="com.freshdirect.fdstore.FDStoreProperties"%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<fd:CheckLoginStatus id="user" guestAllowed='true' recognizedAllowed='true' />
<pre><fd:ProductGroup id='p' categoryId='${param.catId}' productId='${param.productId}'>
Page Title: ${p.fullName}

Note: if FDStoreProperties.isAdServerEnabled() OAS_AD('ProductNote') else include i_product_quality_note.jspf

Names
 name: ${p.fullName} <%-- currently <display:AnnotatedProductName product="${p}" wineLayout="<%= isWineLayout %>" quickBuy="<%= isQuickBuy %>" />--%>
 aka: ${p.aka}

Images
 TODO

Freshness Guarantee - if FDStoreProperties.IsFreshnessGuaranteedEnabled() and see perishable_product.jsp:86 for weird sanity check
 value: ${p.freshnessGuaranteed}
 image: /media/images/site_pages/shelflife/days_${p.freshnessGuaranteed}.gif <%-- css would be better --%>
 link: javascript:pop('/shared/brandpop.jsp?brandId=bd_fd_fresh_guarantee',400,585)

Social Share: see i_product_soc_buttons.jspf

Product Description Accordion
 product description: ${p.productDescription.path}
 allergens: see allergens.jspf
 product note: ${p.productDescriptionNote.path}
 product about: ${p.productAbout.path}
 organic claims: see organic_claims.jspf
 claims: see claims.jspf
 kosher sign and popup link: see kosher.jspf
 heating instructions: see i_heating_instructions.jspf
 partially frozen/parbaked if ${p.hasPartiallyFrozen} and
  ${p.department.fullName} eq equalsIgnoreCase("SEAFOOD"): /media_stat/images/template/bakery/snowflake_grey.gif
  ${p.department.fullName} eq equalsIgnoreCase("BAKERY"): /media_stat/images/template/bakery/parbaked_frozen_prod.gif
 seafood origin: ${p.seafoodOrigin} - can't find it in production now
 season text: ${p.seasonText}
 buying guide: see i_deli_buy_guide.jspf
 cheese 101: see i_cheese_101.jspf
 brand information and link to brand page: see i_product_initialize.jspf and i_product.jspf - do we need brand category link?

Customer Reviews Accordion: see i_product_descriptions.jspf:95

Nutrition Accordion: see i_product_descriptions.jspf:166

How To Cook It Accordion
 doneness guide: see i_doneness_guide.jspf
 how to cook it: see i_how_to_cook.jspf - might contain more
 fresh tips: $ {p.freshTips.path} - can't find it in production now/throws ClassCastException
 ratings: see perishable_product.jsp:47
 usage list: used in i_how_to_cook.jspf
 storage guide: see i_product_descriptions.jspf:414

Ingredients Accordion: see i_product_descriptions.jspf:220

Serving Suggestion Accordion: see i_serving_suggestion.jspf
 
Origin Accordion: for 39-42 see i_product_multiple_skus_box.jspf:227

Related Recipes Accordion: see i_related_recipes.jspf

Wine Information Accordion
 country, region, city, type, classification text, importer, aging notes, alcohol grade (wine_alch_content), grape (wine_type) : see usq_wine_info.jspf
 varietal: ${p.wineVarietal} (couldn't find it used explicitly) 
 vintages: ${p.wineVintage} (used in AnnotatedProductNameTag and WineRegionLabelTag)
 reviews and texts 1,2,3: see i_wine_rating_review.jspf
 other reviews- if p.hasWineOtherRatings(): /media/editorial/win_usq/other_ratings_key.html

Explanatory Accordion
 Heat rating explanation (25): new in cms
 etc: ? TODO

==============================================

Availability messages - all moved from page top, TODO confirm each
 Lead time: see product.jsp:183
 Platter messaging: see cutoff_notice.jspf - see BRD for rules
 Limited availability: see i_dayofweek_notice.jspf

Descriptive content
 Price, was price, deal: see i_product_single_sku_box.jspf -  TODO: anything else here?
 Source label (15): see somewhere i_product_multiple_skus_box.jspf - TODO
 Web description (16): TODO
 Package description: ${p.packageDescription}
 Group scale info: see ProductPriceTag (used by i_product_single_sku_box.jspf)
 Expert rating: see i_product_skus_rating.jspf (maybe i_product_descriptions.jspf:357 is needed too)
 Sustainability rating: see i_product_skus_rating.jspf:64 (maybe i_product_descriptions.jspf:318 and i_product_methods.jspf:29 is needed too)
 Customer reviews: see i_product.jspf:226
 Wine ratings: TODO couldn't find example yet
 Wine type images: see i_wine_rating_review.jspf:87
 Parbaked/partially frozen: see Product Description Accordion
 Heat rating explanation: see Explanatory Accordion

Customize area
 39-42: see Origin Accordion
 Secondary quantity text: ${p.quantityTextSecondary} - not sure how this is used currently
 E-coupons: TODO
 Other stuff (minmax, quantity, atc, lists, price): TODO

Recommenders
 Even better - TODO
 Like that - TODO

Product Request: see right_side_nav.jspf:204
</fd:ProductGroup></pre>