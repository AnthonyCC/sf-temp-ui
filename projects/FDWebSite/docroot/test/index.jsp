<%@ taglib uri='freshdirect' prefix='fd' %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en-US" xml:lang="en-US" xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title> Master Test Page List </title>

		<fd:javascript src="/assets/javascript/prototype.js"/>

	</head>

	<style>
		html { font-size: 100%; }
		body { font-size: 10px; }
		a { color: #b6b; font-weight: bold; }
		a:visited { color: #707; font-weight: bold; }
		hr { clear: both; }
		.content {
			width: 80em;
		}
		.mainTitle {
			width: 20em;
			font-size: 2em;
			text-align: left;
			float: left;
		}
		.pageName {
			font-weight: bold;
			width: 30em;
			float: left;
		}
		.pageName span {
			font-size: 1.4em;
		}
		.pageDesc {
			clear: both;
			font-size: 1.3em;
			font-family: monospace;
			padding: 0.4em 0 1.2em 0;
			text-align: justify;
		}
		.link {
			clear: right;
			float: right;
			width: 50em;
			text-align: left;
		}
		.link a {
			font-size: 1.3em;
			font-style: italic;
			font-family: monospace;
		}
		a .reqVar {
			color: #c00;
		}
		a:visited .reqVar {
			color: #b00;
		}
		#testPageCount, #testPageCountNeedsDesc {
			clear: right;
			float: right;
			text-align: right;
			font-size: 1.2em;
			width: 20em;
			font-family: monospace;
		}
		#tagsFilter {
			font-family: monospace;
			font-size: 1.2em;
			background-color: #88c;
			color: #fff;
			padding: 1em;
			margin: -0.5em 0;
			font-weight: bold;
			line-height: 2.2em;
		}
		a.tagFilter, a.tagFilter:visited {
			text-decoration: none;
			padding: 0.2em;
		}

		div.tag {
			border: 1px dotted #aaa;
		}
	</style>

	<script type="text/javascript">
		if (document.observe) { //make sure prototype is on page
			document.observe("dom:loaded", function() {
				/* generate counts */
				var pages = $$('.pageDesc');
				var pagesNeedDescCount = 0;
				$('testPageCount').innerHTML = pages.length + ' test pages';
				$A(pages).each(function (p) {
					if ((p.innerHTML).toUpperCase() === 'NEEDS DESCRIPTION.') {
						if (p.up('div')) {
							//add a needs descrip tag
							p.up('div').className += ' ALLNeedsDescrip';
						}
						pagesNeedDescCount++;
					}
				});

				$('testPageCountNeedsDesc').innerHTML = pagesNeedDescCount + ' need descriptions';

				/* generate tags */
				var tagged = $$('.tag');
				var tagsObj = {};
				var tags = [];

				$A(tagged).each(function (t) {
					var cn = (t.className).split(' ');
					$A(cn).each(function (tag) {
						if (tag !== 'tag') { tagsObj[tag] = tag; }
					});
				});

				for (tag in tagsObj) {
					tags.push(tag);
				}
				tags.sort();

				if (tags.length > 0) {
					$('tagsFilter').innerHTML += 'Filter: ' + genTagLink('ALL') + ' ';
					$A(tags).each(function (tag, i) {
						$('tagsFilter').innerHTML += genTagLink(tag) + ' ';
					});
					$('tagsFilter').show();
				}
				filterByTag('ALL');
			});
		}

		function genTagLink(tagNameVar) {
			var tagName = tagNameVar || '';
			if (tagName === '') { return ''; }
			return '<a href="#" onclick="filterByTag(\''+tagName+'\'); return false;" class="tagFilter tagFilter'+tagName+'">'+tagName+'</a>';
		}

		function filterByTag(tagNameVar) {
			var tagName = tagNameVar || '';
			if (tagName === '') { return; }
			var hideDivs = [];
			var showDivs = [];

			if (tagName === 'ALL') {
				showDivs = $$('.content .tag');
			}else{
				hideDivs = $$('.content div.tag:not('+tagName+')');
				showDivs = $$('.content div.'+tagName);
			}
			$A(hideDivs).each(function (div) {
				div.hide();
			});
			$A(showDivs).each(function (div) {
				div.show();
			});

			var filterColorOn = $$('#tagsFilter a.tagFilter'+tagName);
			var filterColorOff = $$('#tagsFilter a:not(tagFilter'+tagName+')');
			$A(filterColorOff).each(function (a) {
				a.style.color = '#eee';
				a.style.fontWeight = 'normal';
				a.style.backgroundColor = 'transparent';
				a.style.border = '2px outset #447';
			});
			$A(filterColorOn).each(function (a) {
				a.style.color = '#fff';
				a.style.fontWeight = 'bold';
				a.style.backgroundColor = '#447';
				a.style.border = '2px inset #88c';
			});
		}
	</script>
<body>
<div class="content">

	<hr />
	<div class="mainTitle test">Master Test Page List</div>
	<div id="testPageCount"><!-- --></div>
	<div id="testPageCountNeedsDesc"><!-- --></div>
	<hr />
	<div id="tagsFilter" style="display: none"><!-- --></div>
	<hr />

	<div class="tag pricing zonePricing">
		<div class="pageName"><span>Pricing Zone Lookup</span></div>
		<div class="link"><a href="/test/debug/zp.jsp">/test/debug/zp.jsp</a></div>
		<div class="pageDesc">This provides the details on zipcode, service type and pricing zone the user is in.</div>
	</div>

	<div class="tag pricing scalePricing">
		<div class="pageName"><span>Group Scale Lookup</span></div>
		<div class="link">
			<a href="/test/debug/gp.jsp">/test/debug/gp.jsp?groupId=<span class="reqVar">&lt;groupId&gt;</span></a><br />
			<a href="/test/debug/gp.jsp">/test/debug/gp.jsp?groupId=<span class="reqVar">&lt;groupId&gt;</span>&amp;version=<span class="reqVar">&lt;version&gt;</span></a><br />
			<a href="/test/debug/gp.jsp">/test/debug/gp.jsp?matId=<span class="reqVar">&lt;skuCode&gt;</span></a>
		</div>
		<div class="pageDesc">This provides the details on Group info such as Group name, group scale price, eligible quantity, materials in the group, etc. for the given zipcode, service type and pricing zone the logged in user has.</div>
	</div>

	<div class="tag">
		<div class="pageName"><span>Refresh for Promotion and FDStore properties</span></div>
		<div class="link"><a href="/test/debug/index.jsp">/test/debug/index.jsp</a></div>
		<div class="pageDesc">Causes a FORCE refresh of a Promotion and FDStore properties instead of waiting for auto refresh.</div>
	</div>

	<div class="tag giftcard">
		<div class="pageName"><span>Preview and Validate Gift Card</span></div>
		<div class="link"><a href="/test/giftcards/index.jsp">/test/giftcards/index.jsp</a></div>
		<div class="pageDesc">Helps to preview Gift cards in the store and also help debugging rendering of gift cards.</div>
	</div>

	<div class="tag freemarker">
		<div class="pageName"><span>FreeMarker Test Pages - Index</span></div>
		<div class="link"><a href="/test/freemarker_testing/index.jsp">/test/freemarker_testing/index.jsp</a></div>
		<div class="pageDesc">Index of FreeMarker Test pages (with links to sub-pages). All the contents of these pages operate completely in media, and use a same-name ftl file from the /media/testing/ folder.<br />
		<br />
		Uses the media file: /media/testing/index.ftl</div>
	</div>

	<div class="tag freemarker">
		<div class="pageName"><span>FreeMarker Test Pages - Examples</span></div>
		<div class="link"><a href="/test/freemarker_testing/examples.jsp">/test/freemarker_testing/examples.jsp</a></div>
		<div class="pageDesc">Shows some example FreeMarker uses. This page shows code/source and results of different examples. (Also lists the current FreeMarker version in use at the top, right.)<br />
		<br />
		Uses the media file: /media/testing/examples.ftl</div>
	</div>

	<div class="tag freemarker">
		<div class="pageName"><span>FreeMarker Test Pages - All Info</span></div>
		<div class="link">
			<a href="/test/freemarker_testing/all_info.jsp">/test/freemarker_testing/all_info.jsp</a><br />
			<a href="/test/freemarker_testing/all_info.jsp">/test/freemarker_testing/all_info.jsp?sku2url=true&sku=<span class="reqVar">&lt;skuCode&gt;</span></a>
		</div>
		<div class="pageDesc">Attempts to load all data available to an ftl file. Because this is manual, not all info is there, but a heck of a lot of it is. Attempts to auto-identify a content type based on an ID. If it's successful, also shows Java Class of object.<br />
		<br />
		This page is also the backbone of the userscript SKU2URL. When sku2url=true&sku=&lt;skuCode&gt; is passed in, the page will return a JSON object containing the info used in the script. (This info is displayed on /ERPSAdmin/product/product_view.jsp when the userscript is in use.)<br />
		<br />
		Uses the media file: /media/testing/all_info.ftl</div>
	</div>

	<div class="tag freemarker">
		<div class="pageName"><span>FreeMarker Test Pages - Impromptu</span></div>
		<div class="link"><a href="/test/freemarker_testing/impromptu.jsp">/test/freemarker_testing/impromptu.jsp</a></div>
		<div class="pageDesc">Allows you to execute FreeMarker code on the fly. Has error reporting on to (hopefully) help debug FreeMarker errors.<br />
		<br />
		Uses the media file: /media/testing/impromptu.ftl</div>
	</div>

	<div class="tag build">
		<div class="pageName"><span>Build Information</span></div>
		<div class="link"><a href="/test/buildver.jsp">/test/buildver.jsp</a></div>
		<div class="pageDesc">Shows the current info for the code deployed to server.</div>
	</div>

	<div class="tag alcohol">
		<div class="pageName"><span>User Alcohol Warning Agreement Modifier</span></div>
		<div class="link"><a href="/test/content/alcohol.jsp">/test/content/alcohol.jsp</a></div>
		<div class="pageDesc">Manually set a User's Agreement to the Alcohol Warning message. If a user is in the session already, they will be shown. Also recognizes "anonymous" users.</div>
	</div>

	<div class="tag whatsGood">
		<div class="pageName"><span>Dynamic Rows</span></div>
		<div class="link"><a href="/test/content/dynamic_rows.jsp">/test/content/dynamic_rows.jsp</a></div>
		<div class="pageDesc">Test Dynamic Rows functionality/setup. Dynamic Rows are used in What's Good. (Hover over question marks on page for usage.) Page includes full to-page debug prining.</div>
	</div>

	<div class="tag presPicks email">
		<div class="pageName"><span>President's Picks Email content</span></div>
		<div class="link"><a href="/test/content/email.jsp">/test/content/email.jsp</a></div>
		<div class="pageDesc">Parses current pres picks ftl and generates content fit for emailing. (Depreceated with Data-Driven)</div>
	</div>

	<div class="tag productRequest">
		<div class="pageName"><span>Product Request (Stress Test)</span></div>
		<div class="link"><a href="/test/content/product_request.jsp">/test/content/product_request.jsp</a></div>
		<div class="pageDesc">Generates random Dept/Cat sets, javascript, and example dropdowns. Includes execution time. This is the functionality in-use on the Product Request page.</div>
	</div>

	<div class="tag productGrid">
		<div class="pageName"><span>Product Grid Test</span></div>
		<div class="link"><a href="/test/content/grid_test.jsp">/test/content/grid_test.jsp</a></div>
		<div class="pageDesc">Generates Product Grid / List Layout. Use catId query parameter to specify category id.</div>
	</div>

	<div class="tag semPixel">
		<div class="pageName"><span>SEM Pixel Information</span></div>
		<div class="link">
			<a href="/test/content/sem.jsp">/test/content/sem.jsp</a><br />
			<a href="/test/content/sem.jsp">/test/content/sem.jsp?pixelNames=<span class="reqVar">&lt;Pixel1,Pixel2&gt;</span></a></a>
		</div>
		<div class="pageDesc">Displays all info related to SEM Pixels. Displays current SEM properties, SEM Pixel cache, and media(s). Specify pixelNames to dynanmically load them. This page also does a FORCE refresh of the FDStore properties.</div>
	</div>

	<div class="tag cart export">
		<div class="pageName"><span>Cart Export</span></div>
		<div class="link"><a href="/test/cartexport.jsp">/test/cartexport.jsp</a></div>
		<div class="pageDesc">Export current user's cart to a spreadsheet.</div>
	</div>

	<div class="tag browser">
		<div class="pageName"><span>Opacity</span></div>
		<div class="link"><a href="/test/content/opacity.jsp">/test/content/opacity.jsp</a></div>
		<div class="pageDesc">Browser detection and burst overlay test.</div>
	</div>

	<div class="tag media">
		<div class="pageName"><span>Preview</span></div>
		<div class="link"><a href="/test/content/preview.jsp">/test/content/preview.jsp?template=<span class="reqVar">&lt;Path To Media&gt;</span></a></div>
		<div class="pageDesc">Includes any media file passed in to it.</div>
	</div>

	<div class="tag productData">
		<div class="pageName"><span>Product Data</span></div>
		<div class="link"><a href="/test/content/product_data.jsp">/test/content/product_data.jsp</a></div>
		<div class="pageDesc">Display product data (currently ony has "rating" as an option).</div>
	</div>

	<div class="tag productData">
		<div class="pageName productData"><span>Product Data Out</span></div>
		<div class="link"><a href="/test/content/product_data_out.jsp">/test/content/product_data_out.jsp?pId=<span class="reqVar">&lt;productId&gt;</span>&pData=<span class="reqVar">&lt;rating&gt;</span></a></div>
		<div class="pageDesc">Outputs the data for the Product Data page.</div>
	</div>

	<div class="tag obsolete">
		<div class="pageName"><span>Ymal Preview</span></div>
		<div class="link"><a href="/test/content/ymal_preview.jsp">/test/content/ymal_preview.jsp</a></div>
		<div class="pageDesc">Obsolete, redirects to /test/smartstore/compare_variants.jsp</div>
	</div>

	<div class="tag obsolete">
		<div class="pageName"><span>Ymal Set Preview</span></div>
		<div class="link"><a href="/test/content/ymal_set_preview.jsp">/test/content/ymal_set_preview.jsp</a></div>
		<div class="pageDesc">A page used for previewing ymal sets. (Obsolete)</div>
	</div>

	<div class="tag CSS">
		<div class="pageName"><span>CSS</span></div>
		<div class="link"><a href="/test/css.jsp">/test/css.jsp</a></div>
		<div class="pageDesc">Preview CSS classes.</div>
	</div>

	<div class="tag cache sku">
		<div class="pageName"><span>Cache Content</span></div>
		<div class="link"><a href="/test/data/cache_content.jsp">/test/data/cache_content.jsp</a></div>
		<div class="pageDesc">Display cached SKU info.</div>
	</div>

	<div class="tag sku">
		<div class="pageName"><span>Available SKUs</span></div>
		<div class="link"><a href="/test/data/skus.jsp">/test/data/skus.jsp</a></div>
		<div class="pageDesc">Display cached SKU info.</div>
	</div>

	<div class="tag CMS sku">
		<div class="pageName"><span>Available Products with Multiple SKUs</span></div>
		<div class="link"><a href="/test/data/multi_sku_products.jsp">/test/data/multi_sku_products.jsp</a></div>
		<div class="pageDesc">Display available products having stacked SKUs.</div>
	</div>

	<div class="tag CMS">
		<div class="pageName"><span>Data</span></div>
		<div class="link"><a href="/test/data/data.jsp">/test/data/data.jsp</a></div>
		<div class="pageDesc">CMS Test Data Generator</div>
	</div>

	<div class="tag CMS migration">
		<div class="pageName"><span>Store Migration</span></div>
		<div class="link"><a href="/test/migration/index.jsp">/test/migration/index.jsp</a></div>
		<div class="pageDesc">Store Migration Tools and Reports</div>
	</div>

	<div class="tag session">
		<div class="pageName"><span>Debugger</span></div>
		<div class="link"><a href="/test/debug/debugger.jsp">/test/debug/debugger.jsp</a></div>
		<div class="pageDesc">Debug session objects</div>
	</div>

	<div class="tag pricing zonePricing">
		<div class="pageName"><span>GP</span></div>
		<div class="link"><a href="/test/debug/gp.jsp">/test/debug/gp.jsp</a></div>
		<div class="pageDesc">Group Pricing/ Zone Pricing info. <br />
			Requires one of these params:<br />
			sku=<br />
			or<br />
			grpId= AND version=<br />
			or<br />
			matId=
		</div>
	</div>

	<div class="tag properties promotion">
		<div class="pageName"><span>Debug Index Page</span></div>
		<div class="link"><a href="/test/debug/index.jsp">/test/debug/index.jsp</a></div>
		<div class="pageDesc">Top-level page for Session Debugger, Snoop Request, Refresh Promotion and Reload FDStore.properties</div>
	</div>

	<div class="tag properties">
		<div class="pageName"><span>Refresh fdstore properties</span></div>
		<div class="link"><a href="/test/debug/refresh_fdstore_properties.jsp">/test/debug/refresh_fdstore_properties.jsp</a></div>
		<div class="pageDesc">Reload FDStore.properties file</div>
	</div>

	<div class="tag promotion">
		<div class="pageName"><span>refresh promotion</span></div>
		<div class="link"><a href="/test/debug/refresh_promotion.jsp">/test/debug/refresh_promotion.jsp?promoCode=<span class="reqVar">&lt;promoCode&gt;</span></a></div>
		<div class="pageDesc">Refresh Promotion</div>
	</div>

	<div class="tag requestObject">
		<div class="pageName"><span>Snoop request</span></div>
		<div class="link"><a href="/test/debug/snoop.jsp">/test/debug/snoop.jsp</a></div>
		<div class="pageDesc">Show information from Request Object, including session and SSL info.</div>
	</div>

	<div class="tag">
		<div class="pageName XML"><span>XML</span></div>
		<div class="link"><a href="/test/debug/xml.jsp">/test/debug/xml.jsp</a></div>
		<div class="pageDesc">Serializes data into XML (not working?)</div>
	</div>

	<div class="tag">
		<div class="pageName zonePricing"><span>ZP</span></div>
		<div class="link"><a href="/test/debug/zp.jsp">/test/debug/zp.jsp?sku=<span class="reqVar">&lt;skuCode&gt;</span></a></div>
		<div class="pageDesc">Displays zone pricing info for skuCode</div>
	</div>

	<div class="tag browser">
		<div class="pageName"><span>DOM Loaded IE Test</span></div>
		<div class="link"><a href="/test/domloaded/ietest.jsp">/test/domloaded/ietest.jsp</a></div>
		<div class="pageDesc">DOM loaded event tester.</div>
	</div>

	<div class="tag giftcard">
		<div class="pageName"><span>GC INDEX</span></div>
		<div class="link"><a href="/test/giftcards/index.jsp">/test/giftcards/index.jsp</a></div>
		<div class="pageDesc">Gift Card test page. Displays all info related to Gift Cards: all available cards, CMS info, Media info, various display types, unit tests (including failure fallbacks), previews (email and PDF), and data set in properties.</div>
	</div>

	<div class="tag giftcard">
		<div class="pageName"><span>GC PDF Generator</span></div>
		<div class="link"><a href="/test/giftcards/pdf_generator.jsp">/test/giftcards/pdf_generator.jsp</a></div>
		<div class="pageDesc">Used to generate a PDF on the test page.</div>
	</div>

	<div class="tag giftcard">
		<div class="pageName"><span>GC Postback</span></div>
		<div class="link"><a href="/test/giftcards/postback.jsp">/test/giftcards/postback.jsp</a></div>
		<div class="pageDesc">Used to post back the info for Gift Cards (like in the previews).</div>
	</div>

	<div class="tag dynamicImages">
		<div class="pageName"><span>Graphics Testing - Index</span></div>
		<div class="link"><a href="/test/graphics_testing/index.jsp">/test/graphics_testing/index.jsp</a></div>
		<div class="pageDesc">Dynamic Image test frontend. Allows testing of image creation on the fly. Change width, height, quality (compression), background color, product source, product source's image size, using various bursts (store's info or custom), rating image, along with X,Y offsets for all the features. Includes the result and the URL to be able to refer to directly (like in an email).</div>
	</div>

	<div class="tag email presPicks dynamicImages">
		<div class="pageName"><span>Graphics Testing - Email</span></div>
		<div class="link"><a href="/test/graphics_testing/email.jsp">/test/graphics_testing/email.jsp</a></div>
		<div class="pageDesc">Displays a Pres Picks email, but generates all the images on the fly. All bursts are included IN the product image instead of an overlay.</div>
	</div>

	<div class="tag dynamicImages">
		<div class="pageName"><span>Graphics Testing - Generate Image</span></div>
		<div class="link"><a href="/test/graphics_testing/generateImage.jsp">/test/graphics_testing/generateImage.jsp</a></div>
		<div class="pageDesc">Generates images used in dynamic graphics pages (see index file for info).</div>
	</div>

	<div class="tag dynamicImages akami">
		<div class="pageName"><span>Graphics Testing - Akami Image Converter</span></div>
		<div class="link"><a href="/test/graphics_testing/akami_imgcon_test.jsp">/test/graphics_testing/akami_imgcon_test.jsp</a></div>
		<div class="pageDesc">Test dynamic image conversion (Akami side). See page tooltips for specifics.</div>
	</div>

	<div class="tag grid">
		<div class="pageName"><span>Grid Prototype - JSP</span></div>
		<div class="link"><a href="/test/grid_prototype/index.jsp">/test/grid_prototype/index.jsp</a></div>
		<div class="pageDesc">Grid Prototype display (JSP version)</div>
	</div>

	<div class="tag grid">
		<div class="pageName"><span>Grid Prototype -HTML</span></div>
		<div class="link"><a href="/test/grid_prototype/index.html">/test/grid_prototype/index.html</a></div>
		<div class="pageDesc">Grid Prototype display (HTML version)</div>
	</div>

	<div class="tag localDept">
		<div class="pageName"><span>Local Dept Revamp - Index</span></div>
		<div class="link"><a href="/test/localdept/index.jsp">/test/localdept/index.jsp</a></div>
		<div class="pageDesc">Local Dept Revamp top-level test page.</div>
	</div>

	<div class="tag localDept">
		<div class="pageName"><span>Local Dept - Local and Organic Labels</span></div>
		<div class="link"><a href="/test/localdept/local_org.jsp">/test/localdept/local_org.jsp</a></div>
		<div class="pageDesc">Display all SKUs and Product links with Local or Organic labels.</div>
	</div>

	<div class="tag localDept googleMap">
		<div class="pageName"><span>Local Dept - Local Producers Map</span></div>
		<div class="link"><a href="/test/localdept/map.jsp">/test/localdept/map.jsp</a></div>
		<div class="pageDesc">Display Local Producers Google Map</div>
	</div>

	<div class="tag logs">
		<div class="pageName"><span>/test/logging.jsp</span></div>
		<div class="link"><a href="/test/logging.jsp">/test/logging.jsp</a></div>
		<div class="pageDesc">Tests generating various log entries.</div>
	</div>

	<div class="tag mobile">
		<div class="pageName"><span>/test/mobile_testing/index.jsp</span></div>
		<div class="link"><a href="/test/mobile_testing/index.jsp">/test/mobile_testing/index.jsp</a></div>
		<div class="pageDesc">Display various info used for detecting mobile devices. This includes the current User-String, if iPhone/iPod, redirector page, go to store flag, and property.</div>
	</div>

	<div class="tag mobile">
		<div class="pageName"><span>/test/mobile_testing/non-supported.jsp</span></div>
		<div class="link"><a href="/test/mobile_testing/non-supported.jsp">/test/mobile_testing/non-supported.jsp</a></div>
		<div class="pageDesc">No logic. Failed mobile detects end up here.</div>
	</div>

	<div class="tag mobile">
		<div class="pageName"><span>/test/mobile_testing/redirector.jsp</span></div>
		<div class="link"><a href="/test/mobile_testing/redirector.jsp">/test/mobile_testing/redirector.jsp</a></div>
		<div class="pageDesc">
			Redirects to specific page based on mobile detection. Redirects either to:<br />
			/test/mobile_testing/supported.jsp<br />
			or<br />
			/test/mobile_testing/non-supported.jsp
		</div>
	</div>

	<div class="tag mobile">
		<div class="pageName"><span>/test/mobile_testing/supported.jsp</span></div>
		<div class="link"><a href="/test/mobile_testing/supported.jsp">/test/mobile_testing/supported.jsp</a></div>
		<div class="pageDesc">
			On successful redirect to this page, includes media file: <a href="/media/mobile/supported.ftl">/media/mobile/supported.ftl</a><br />
			<br />
			This FTL displays the media for the supported device.
		</div>
	</div>

	<div class="tag serverHealth">
		<div class="pageName serverHealth"><span>/test/monitor/healthcheck.jsp</span></div>
		<div class="link"><a href="/test/monitor/healthcheck.jsp">/test/monitor/healthcheck.jsp</a></div>
		<div class="pageDesc">Display how long the Health Check takes.</div>
	</div>
	<div class="tag order">
		<div class="pageName"><span>/test/order/order_maker.jsp</span></div>
		<div class="link"><a href="/test/order/order_maker.jsp">/test/order/order_maker.jsp</a></div>
		<div class="pageDesc">Add items to the cart from a list of SKUs.</div>
	</div>

	<div class="tag quickBuy">
		<div class="pageName"><span>/test/quickbuy/simple.jsp</span></div>
		<div class="link"><a href="/test/quickbuy/simple.jsp">/test/quickbuy/simple.jsp</a></div>
		<div class="pageDesc">Display a single (hard-coded product), that uses the Quickbuy feature.</div>
	</div>

	<div class="tag report">
		<div class="pageName"><span>/test/reports/clig_unavailability.jsp</span></div>
		<div class="link"><a href="/test/reports/clig_unavailability.jsp">/test/reports/clig_unavailability.jsp</a></div>
		<div class="pageDesc">Display CLIG availability. Includes name, id, recipe, availability status, product links (for each environment), and timestamp.</div>
	</div>

	<div class="tag report">
		<div class="pageName"><span>/test/reports/dcpd.jsp</span></div>
		<div class="link"><a href="/test/reports/dcpd.jsp">/test/reports/dcpd.jsp</a></div>
		<div class="pageDesc">DCPD Products Report</div>
	</div>

	<div class="tag report">
		<div class="pageName"><span>/test/reports/featured_report.jsp</span></div>
		<div class="link"><a href="/test/reports/featured_report.jsp">/test/reports/featured_report.jsp</a></div>
		<div class="pageDesc">Display featured products setup in each Department and it's subcategories.</div>
	</div>

	<div class="tag report">
		<div class="pageName"><span>/test/reports/full_names.jsp</span></div>
		<div class="link"><a href="/test/reports/full_names.jsp">/test/reports/full_names.jsp</a></div>
		<div class="pageDesc">Display product Full Name, category, and Category Full Name based on a list of Product IDs.</div>
	</div>

	<div class="tag report">
		<div class="pageName"><span>/test/reports/hidden_categories.jsp</span></div>
		<div class="link"><a href="/test/reports/hidden_categories.jsp">/test/reports/hidden_categories.jsp</a></div>
		<div class="pageDesc">Display a list of "Broken Products". (List of products having hidden or not searchable category in their primary home.)</div>
	</div>

	<div class="tag report">
		<div class="pageName"><span>/test/reports/product_unavailability.jsp</span></div>
		<div class="link"><a href="/test/reports/product_unavailability.jsp">/test/reports/product_unavailability.jsp</a></div>
		<div class="pageDesc">Display all Unavailable Products, broken down into Dept, and Unavail vs. Total Products.</div>
	</div>

	<div class="tag report">
		<div class="pageName"><span>/test/reports/promotions.jsp</span></div>
		<div class="link"><a href="/test/reports/promotions.jsp">/test/reports/promotions.jsp</a></div>
		<div class="pageDesc">Display Promotions info.</div>
	</div>

	<div class="tag report">
		<div class="pageName"><span>/test/reports/recipe_unavailability.jsp</span></div>
		<div class="link"><a href="/test/reports/recipe_unavailability.jsp">/test/reports/recipe_unavailability.jsp</a></div>
		<div class="pageDesc">Display all Unavailable Products within Recipes.</div>
	</div>

	<div class="tag search">
		<div class="pageName"><span>/test/search/index.jsp</span></div>
		<div class="link"><a href="/test/search/index.jsp">/test/search/index.jsp</a></div>
		<div class="pageDesc">Search top-level page with links to sub test pages.</div>
	</div>

	<div class="tag session">
		<div class="pageName"><span>/test/session/index.jsp</span></div>
		<div class="link"><a href="/test/session/index.jsp">/test/session/index.jsp</a></div>
		<div class="pageDesc">Display a Session info dump.</div>
	</div>
	<div class="tag session kill">
		<div class="pageName"><span>/test/session/killme.jsp</span></div>
		<div class="link"><a href="/test/session/killme.jsp">/test/session/killme.jsp</a></div>
		<div class="pageDesc">Immediately invalidate the current session.</div>
	</div>

	<div class="tag session">
		<div class="pageName"><span>/test/session/serialize.jsp</span></div>
		<div class="link"><a href="/test/session/serialize.jsp">/test/session/serialize.jsp</a></div>
		<div class="pageDesc">Display a serialized Session info dump.</div>
	</div>
	<div class="tag smartStore">
		<div class="pageName"><span>/test/smartstore/index.jsp</span></div>
		<div class="link"><a href="/test/smartstore/index.jsp">/test/smartstore/index.jsp</a></div>
		<div class="pageDesc">Top-level SmartStore page, with links to SmartStore sub-pages.</div>
	</div>

	<div class="tag smartStore">
		<div class="pageName"><span>/test/smartstore/all_ymal_perf_test.jsp</span></div>
		<div class="link"><a href="/test/smartstore/all_ymal_perf_test.jsp">/test/smartstore/all_ymal_perf_test.jsp</a></div>
		<div class="pageDesc">YMAL Performance test page.</div>
	</div>

	<div class="tag smartStore">
		<div class="pageName"><span>/test/smartstore/backdoor_1.jsp</span></div>
		<div class="link"><a href="/test/smartstore/backdoor_1.jsp">/test/smartstore/backdoor_1.jsp?erpId=<span class="reqVar">&lt;erpId&gt;</span>&userId=<span class="reqVar">&lt;userId&gt;</span></a></div>
		<div class="pageDesc">
			Display user info, and log them in via erpId/custId.<br />
			<br />
			Pass in userId at a minimum (erpId will then be determined from it).</div>
	</div>

	<div class="tag">
		<div class="pageName"><span>/test/smartstore/cohort_dist.jsp</span></div>
		<div class="link"><a href="/test/smartstore/cohort_dist.jsp">/test/smartstore/cohort_dist.jsp</a></div>
		<div class="pageDesc">Needs Description.</div>
	</div>

	<div class="tag smartStore">
		<div class="pageName"><span>/test/smartstore/cohorts.jsp</span></div>
		<div class="link"><a href="/test/smartstore/cohorts.jsp">/test/smartstore/cohorts.jsp</a></div>
		<div class="pageDesc">Lookup Cohort info by user id.</div>
	</div>

	<div class="tag smartStore">
		<div class="pageName"><span>/test/smartstore/compare_variants.jsp</span></div>
		<div class="link"><a href="/test/smartstore/compare_variants.jsp">/test/smartstore/compare_variants.jsp</a></div>
		<div class="pageDesc">Compare the results of two Variants on the same user.</div>
	</div>

	<div class="tag">
		<div class="pageName"><span>/test/smartstore/console.jsp</span></div>
		<div class="link"><a href="/test/smartstore/console.jsp">/test/smartstore/console.jsp</a></div>
		<div class="pageDesc">Needs Description.</div>
	</div>

	<div class="tag">
		<div class="pageName"><span>/test/smartstore/dyf.jsp</span></div>
		<div class="link"><a href="/test/smartstore/dyf.jsp">/test/smartstore/dyf.jsp</a></div>
		<div class="pageDesc">Needs Description.</div>
	</div>

	<div class="tag">
		<div class="pageName"><span>/test/smartstore/dyf_cust_ids.jsp</span></div>
		<div class="link"><a href="/test/smartstore/dyf_cust_ids.jsp">/test/smartstore/dyf_cust_ids.jsp</a></div>
		<div class="pageDesc">Needs Description.</div>
	</div>

	<div class="tag">
		<div class="pageName"><span>/test/smartstore/dyf_test.jsp</span></div>
		<div class="link"><a href="/test/smartstore/dyf_test.jsp">/test/smartstore/dyf_test.jsp</a></div>
		<div class="pageDesc">Needs Description.</div>
	</div>

	<div class="tag">
		<div class="pageName"><span>/test/smartstore/factors.jsp</span></div>
		<div class="link"><a href="/test/smartstore/factors.jsp">/test/smartstore/factors.jsp</a></div>
		<div class="pageDesc">Needs Description.</div>
	</div>

	<div class="tag smartStore">
		<div class="pageName"><span>/test/smartstore/fi_debugger.jsp</span></div>
		<div class="link"><a href="/test/smartstore/fi_debugger.jsp">/test/smartstore/fi_debugger.jsp</a></div>
		<div class="pageDesc">Display Featured Items that will be displayed based on customer's SmartStore settings.</div>
	</div>

	<div class="tag smartStore">
		<div class="pageName"><span>/test/smartstore/impression_log.jsp</span></div>
		<div class="link"><a href="/test/smartstore/impression_log.jsp">/test/smartstore/impression_log.jsp</a></div>
		<div class="pageDesc">Generates impression events. (Requires CSV file.)</div>
	</div>

	<div class="tag smartStore">
		<div class="pageName"><span>/test/smartstore/impression_start_stop.jsp</span></div>
		<div class="link"><a href="/test/smartstore/impression_start_stop.jsp">/test/smartstore/impression_start_stop.jsp</a></div>
		<div class="pageDesc">Manage timed flushes for the Impression Log page.</div>
	</div>

	<div class="tag smartStore">
		<div class="pageName"><span>/test/smartstore/my_variant.jsp</span></div>
		<div class="link"><a href="/test/smartstore/my_variant.jsp">/test/smartstore/my_variant.jsp</a></div>
		<div class="pageDesc">Diosplay Variant info for user.</div>
	</div>

	<div class="tag smartStore">
		<div class="pageName"><span>/test/smartstore/new_and_back.jsp</span></div>
		<div class="link"><a href="/test/smartstore/new_and_back.jsp">/test/smartstore/new_and_back.jsp</a></div>
		<div class="pageDesc">Displays all New and Back In Stock info for products.</div>
	</div>

	<div class="tag">
		<div class="pageName"><span>/test/smartstore/ping.jsp</span></div>
		<div class="link"><a href="/test/smartstore/ping.jsp">/test/smartstore/ping.jsp</a></div>
		<div class="pageDesc">Needs Description.</div>
	</div>

	<div class="tag smartStore">
		<div class="pageName"><span>/test/smartstore/product_meta.jsp</span></div>
		<div class="link"><a href="/test/smartstore/product_meta.jsp">/test/smartstore/product_meta.jsp</a></div>
		<div class="pageDesc">Display Scoring info for product (Customer score and Global Score).</div>
	</div>

	<div class="tag">
		<div class="pageName"><span>/test/smartstore/query_scarabrules.jsp</span></div>
		<div class="link"><a href="/test/smartstore/query_scarabrules.jsp">/test/smartstore/query_scarabrules.jsp</a></div>
		<div class="pageDesc">Needs Description.</div>
	</div>

	<div class="tag">
		<div class="pageName"><span>/test/smartstore/query_smartcats.jsp</span></div>
		<div class="link"><a href="/test/smartstore/query_smartcats.jsp">/test/smartstore/query_smartcats.jsp</a></div>
		<div class="pageDesc">Needs Description.</div>
	</div>

	<div class="tag">
		<div class="pageName"><span>/test/smartstore/query_ymalsets.jsp</span></div>
		<div class="link"><a href="/test/smartstore/query_ymalsets.jsp">/test/smartstore/query_ymalsets.jsp</a></div>
		<div class="pageDesc">Needs Description.</div>
	</div>

	<div class="tag smartStore">
		<div class="pageName"><span>/test/smartstore/request_simulator.jsp</span></div>
		<div class="link"><a href="/test/smartstore/request_simulator.jsp">/test/smartstore/request_simulator.jsp</a></div>
		<div class="pageDesc">Diaply results of various SmartStore features based on Customer and a trigger Product.</div>
	</div>

	<div class="tag smartStore">
		<div class="pageName"><span>/test/smartstore/scarab_perf.jsp</span></div>
		<div class="link"><a href="/test/smartstore/scarab_perf.jsp">/test/smartstore/scarab_perf.jsp</a></div>
		<div class="pageDesc">Display Scarab related search results.</div>
	</div>

	<div class="tag smartStore">
		<div class="pageName"><span>/test/smartstore/smart_categories.jsp</span></div>
		<div class="link"><a href="/test/smartstore/smart_categories.jsp">/test/smartstore/smart_categories.jsp</a></div>
		<div class="pageDesc">Display returned products from a Smart Category.</div>
	</div>

	<div class="tag smartStore">
		<div class="pageName"><span>/test/smartstore/tabs_perf_test.jsp</span></div>
		<div class="link"><a href="/test/smartstore/tabs_perf_test.jsp">/test/smartstore/tabs_perf_test.jsp</a></div>
		<div class="pageDesc">Display performance info related to generating a User's SmartStore Tab (Site Features).</div>
	</div>

	<div class="tag smartStore">
		<div class="pageName"><span>/test/smartstore/tabs_test.jsp</span></div>
		<div class="link"><a href="/test/smartstore/tabs_test.jsp">/test/smartstore/tabs_test.jsp</a></div>
		<div class="pageDesc">Display Tab related priorities (by Site Feature).</div>
	</div>

	<div class="tag smartStore">
		<div class="pageName"><span>/test/smartstore/view_cohorts.jsp</span></div>
		<div class="link"><a href="/test/smartstore/view_cohorts.jsp">/test/smartstore/view_cohorts.jsp</a></div>
		<div class="pageDesc">Display Cohort info and dsitribution percentages.</div>
	</div>

	<div class="tag smartStore">
		<div class="pageName"><span>/test/smartstore/view_config.jsp</span></div>
		<div class="link"><a href="/test/smartstore/view_config.jsp">/test/smartstore/view_config.jsp</a></div>
		<div class="pageDesc">Display info for Variant configurations.</div>
	</div>

	<div class="tag">
		<div class="pageName"><span>/test/smartstore/view_promovariant_cfg.jsp</span></div>
		<div class="link"><a href="/test/smartstore/view_promovariant_cfg.jsp">/test/smartstore/view_promovariant_cfg.jsp</a></div>
		<div class="pageDesc">Needs Description.</div>
	</div>

	<div class="tag smartStore">
		<div class="pageName"><span>/test/smartstore/view_tabs.jsp</span></div>
		<div class="link"><a href="/test/smartstore/view_tabs.jsp">/test/smartstore/view_tabs.jsp</a></div>
		<div class="pageDesc">Display results of Tab Strategies.</div>
	</div>

	<div class="tag smartStore">
		<div class="pageName"><span>/test/smartstore/view_ymals.jsp</span></div>
		<div class="link"><a href="/test/smartstore/view_ymals.jsp">/test/smartstore/view_ymals.jsp</a></div>
		<div class="pageDesc">Display a list of Smart YMALs.</div>
	</div>

	<div class="tag smartStore">
		<div class="pageName"><span>/test/smartstore/ymal_display.jsp</span></div>
		<div class="link"><a href="/test/smartstore/ymal_display.jsp">/test/smartstore/ymal_display.jsp</a></div>
		<div class="pageDesc">Display YMAL results (in web style).</div>
	</div>

	<div class="tag smartStore">
		<div class="pageName"><span>/test/smartstore/ymal_perf_test.jsp</span></div>
		<div class="link"><a href="/test/smartstore/ymal_perf_test.jsp">/test/smartstore/ymal_perf_test.jsp</a></div>
		<div class="pageDesc">Display performance info related to generating a User's Smart YMALs.</div>
	</div>

	<div class="tag smartStore zonePricing">
		<div class="pageName"><span>/test/smartstore/zone_pricing.jsp</span></div>
		<div class="link"><a href="/test/smartstore/zone_pricing.jsp">/test/smartstore/zone_pricing.jsp</a></div>
		<div class="pageDesc">Display Zone Pricing related info for a Customer.</div>
	</div>

	<div class="tag">
		<div class="pageName"><span>/test/spammer/index.jsp</span></div>
		<div class="link"><a href="/test/spammer/index.jsp">/test/spammer/index.jsp</a></div>
		<div class="pageDesc">Sends an email to recipients (specified) from service@freshdirect.com.<br />
		<br />
		More details on functionality needed.</div>
	</div>

	<div class="tag">
		<div class="pageName"><span>/test/spammer/recipe.jsp</span></div>
		<div class="link"><a href="/test/spammer/recipe.jsp">/test/spammer/recipe.jsp</a></div>
		<div class="pageDesc">Needs Description.</div>
	</div>

	<div class="tag">
		<div class="pageName"><span>/test/stress/evaluate_promotions.jsp</span></div>
		<div class="link"><a href="/test/stress/evaluate_promotions.jsp">/test/stress/evaluate_promotions.jsp</a></div>
		<div class="pageDesc">Needs Description.</div>
	</div>

	<div class="tag">
		<div class="pageName"><span>/test/stress/login.jsp</span></div>
		<div class="link"><a href="/test/stress/login.jsp">/test/stress/login.jsp</a></div>
		<div class="pageDesc">Needs Description.</div>
	</div>

	<div class="tag">
		<div class="pageName"><span>/test/stress/login_new.jsp</span></div>
		<div class="link"><a href="/test/stress/login_new.jsp">/test/stress/login_new.jsp</a></div>
		<div class="pageDesc">Needs Description.</div>
	</div>

	<div class="tag">
		<div class="pageName"><span>/test/stress/logoff.jsp</span></div>
		<div class="link"><a href="/test/stress/logoff.jsp">/test/stress/logoff.jsp</a></div>
		<div class="pageDesc">Needs Description.</div>
	</div>

	<div class="tag">
		<div class="pageName"><span>/test/stress/save_cart.jsp</span></div>
		<div class="link"><a href="/test/stress/save_cart.jsp">/test/stress/save_cart.jsp</a></div>
		<div class="pageDesc">Needs Description.</div>
	</div>

	<div class="tag standingOrder">
		<div class="pageName"><span>SO Confirmation E-mail</span></div>
		<div class="link"><a href="/test/standingorder/confirm_email.jsp">/test/standingorder/confirm_email.jsp</a></div>
		<div class="pageDesc">Test page to send out a confirmation e-mail that a standing order instance is set to be delivered.</div>
	</div>

	<div class="tag">
		<div class="pageName"><span>/test/warmup/debugconsole.jsp</span></div>
		<div class="link"><a href="/test/warmup/debugconsole.jsp">/test/warmup/debugconsole.jsp</a></div>
		<div class="pageDesc">Needs Description.</div>
	</div>

	<div class="tag warmup">
		<div class="pageName"><span>/test/warmup/index.jsp</span></div>
		<div class="link"><a href="/test/warmup/index.jsp">/test/warmup/index.jsp</a></div>
		<div class="pageDesc">Warmup top-level control page with links to sub-pages. Allows actions like running warmup, reloading store, and loading inventory.</div>
	</div>

	<div class="tag warmup inventory">
		<div class="pageName"><span>/test/warmup/inventory.jsp</span></div>
		<div class="link"><a href="/test/warmup/inventory.jsp">/test/warmup/inventory.jsp?deptId=<span class="reqVar">&lt;deptId&gt;</span></a></div>
		<div class="pageDesc">Load inventory for deptId.</div>
	</div>

	<div class="tag warmup">
		<div class="pageName"><span>/test/warmup/warmup.jsp</span></div>
		<div class="link"><a href="/test/warmup/warmup.jsp">/test/warmup/warmup.jsp</a></div>
		<div class="pageDesc">
			Run Store warmup.
		</div>
	</div>

	<div class="tag">
		<div class="pageName"><span>/test/wine/filtertest.jsp</span></div>
		<div class="link"><a href="/test/wine/filtertest.jsp">/test/wine/filtertest.jsp</a></div>
		<div class="pageDesc">Needs Description.</div>
	</div>

	<div class="tag cart merge">
		<div class="pageName"><span>Merge Cart</span></div>
		<div class="link"><a href="/test/content/mergePendOrder.jsp">/test/content/mergePendOrder.jsp</a></div>
		<div class="pageDesc">Test merging in selected items into a cart.</div>
	</div>

	<div class="tag Gateway">
		<div class="pageName"><span>Test Gateway Transactions</span></div>
		<div class="link"><a href="/test/gateway/index.jsp">/test/gateway/index.jsp</a></div>
		<div class="pageDesc">Helps to test Gateway Transactions .</div>
	</div>

	<div class="tag partialRollout partial rollout featureFlipping feature flipping A/B testing">
	   <div class="pageName"><span>Feature Version Forcing Page</span></div>
	   <div class="link"><a href="/test/features/index.jsp">/test/features/index.jsp</a></div>
	   <div class="pageDesc">For Partial Rollout testing it makes easier feature forcing to do A/B testing.</div>
	</div>

	<div class="tag ineligible_ebt_products">
	   <div class="pageName"><span>Ineligible EBT products</span></div>
	   <div class="link"><a href="/test/reports/ebt_products.jsp">/test/reports/ebt_products.jsp</a></div>
	   <div class="pageDesc">List of ineligible EBT products</div>
	</div>

	<div class="tag CSS express_checkout product_sampling_carousel">
	   <div class="pageName"><span>Product Sampling Carousel Desing at XC/View Cart page</span></div>
	   <div class="link"><a href="/test/express_checkout/product_sample_carousel.jsp">/test/express_checkout/product_sample_carousel.jsp</a></div>
	   <div class="pageDesc">Product Sampling Carousel Desing at XC/View Cart page</div>
	</div>

    <div class="tag giftcard givexnum">
       <div class="pageName"><span>Gift Card Givex Number encrypt and decrypt page</span></div>
       <div class="link"><a href="/test/giftcards/givex.jsp">/test/giftcards/givex.jsp</a></div>
       <div class="pageDesc">Gift Card Givex Number encrypt and decrypt page</div>
    </div>

	<div class="tag javascript widgets">
		<div class="pageName"><span>JavaScript Widgets</span></div>
		<div class="link"><a href="/test/jswidgets/index.jsp">/test/jswidgets/index.jsp</a></div>
		<div class="pageDesc">Demo page for our JavaScript widgets.</div>
	</div>

	<div class="tag CSS buttons">
		<div class="pageName"><span>CSS Buttons</span></div>
		<div class="link"><a href="/test/components/cssbuttons.jsp">/test/components/cssbuttons.jsp</a></div>
		<div class="pageDesc">Demo page for FD CSS buttons</div>
	</div>

	<div class="tag properties buttons">
		<div class="pageName"><span>FD Store Properties Live!</span></div>
		<div class="link"><a href="/test/smartstore/storeproperties.jsp">/test/smartstore/storeproperties.jsp</a></div>
		<div class="pageDesc">Lists/filters all FD Store properties set up in your machine. Also it can force a full refresh.</div>
	</div>

	<div class="tag mobile mobileWeb">
		<div class="pageName"><span>Testing mobileWeb</span></div>
		<div class="link"><a href="/test/mobileWeb.jsp">/test/mobileWeb.jsp</a></div>
		<div class="pageDesc">Show media in the mobileWeb layout for testing.</div>
	</div>

	<div class="tag mobile mobileWeb layouts">
		<div class="pageName"><span>Layout Info</span></div>
		<div class="link"><a href="/test/layoutInfo.jsp">/test/layoutInfo.jsp</a></div>
		<div class="pageDesc">Show layout setup for every dept and cat.</div>
	</div>

	<div class="tag credits masscredits">
		<div class="pageName"><span>Mass Credit Helper</span></div>
		<div class="link"><a href="/test/credits/credits_helper.jsp">/test/credits/credits_helper.jsp</a></div>
		<div class="pageDesc">Generate SQL needed for mass credits</div>
	</div>

	<div class="tag productRequest HTTPS">
		<div class="pageName"><span>Mass Credit Helper</span></div>
		<div class="link"><a href="/test/content/product_request_https.jsp">/test/content/product_request_https.jsp</a></div>
		<div class="pageDesc">Test pop-up vs overlay in HTTP/S<br /><br />Content is in: /media/testing/product_request_https.ftl</div>
	</div>

	<div class="tag">
		<div class="pageName"><span>Collect the product for Microwave link</span></div>
		<div class="link"><a href="/test/microwave.jsp">/test/microwave.jsp</a></div>
		<div class="pageDesc">In order to modify the content of those products which has microwave link shows every product that has "Microwave-only, BPA-free container" text</div>
	</div>

    <div class="tag analytics">
        <div class="pageName"><span>Google analytics</span></div>
        <div class="link"><a href="/test/google_analytics/index.jsp">/test/google_analytics/index.jsp</a></div>
        <div class="pageDesc">Helps to test Google Analytics.</div>
    </div>

    <div class="tag informational ordermodify">
        <div class="pageName"><span>Informational: Order Modify</span></div>
        <div class="link"><a href="/test/google_analytics/index.jsp">/test/inform/ordermodify.jsp</a></div>
        <div class="pageDesc">Informational: Order Modify data and functionality.</div>
    </div>
    
    <div class="tag carousel">
        <div class="pageName"><span>Carousel</span></div>
        <div class="link"><a href="/test/carousel/index.jsp">/test/carousel/index.jsp</a></div>
        <div class="pageDesc">Help to test Carousel</div>
    </div>
    
    <div class="tag coupon">
        <div class="pageName"><span>Coupon Info</span></div>
        <div class="link"><a href="/test/ecoupon/index.jsp">/test/ecoupon/index.jsp</a></div>
        <div class="pageDesc">Show coupon info and refresh cache</div>
    </div>
    <div class="tag coupon">
        <div class="pageName"><span>Coupon Display Tests</span></div>
        <div class="link"><a href="/test/ecoupon/display_tests.jsp">/test/ecoupon/display_tests.jsp?productId=<span class="reqVar">&lt;productId&gt;</span>&catId=<span class="reqVar">&lt;catId&gt;</span></a></div>
        <div class="pageDesc">Test coupon display</div>
    </div>

	<div class="tag javascript">
		<div class="pageName"><span>JavaScript Dependencies</span></div>
		<div class="link"><a href="/test/js/dependencies.jsp">/test/js/dependencies.jsp</a></div>
		<div class="pageDesc">List Javascript Dependencies</div>
	</div>

	<div class="tag css fonts">
		<div class="pageName"><span>Custom Font Comparison</span></div>
		<div class="link"><a href="/test/css/fonts.jsp">/test/css/fonts.jsp</a></div>
		<div class="pageDesc">Display all webfonts and compare versions</div>
	</div>

	<hr />
</div>

</body>
</html>
