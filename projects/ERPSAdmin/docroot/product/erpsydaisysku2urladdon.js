// ==UserScript==
// @name           ERPSy-Daisy SKU2URL Addon
// @namespace      freshdirect.com
// @description    ERPSy-Daisy SKU2URL Addon: Attempt to link a sku back to it's product page
// @include        http://*.freshdirect.com*/ERPSAdmin/product/product_view.jsp*
// ==/UserScript==

/*-----------------------------------------------------------------------------
*/ var version='1.04.01' /*					Last Edit: 2011.03.16_03.28.54.PM
-----------------------------------------------------------------------------*/

/*
 *
 **	1.04.01 : 2011.03.16_03.28.54.PM
 *		Added display for SAP IDs to the component groups
 **	1.04.00 : 2011.03.16_11.59.38.AM
 *		Added Component Groups and Variations functionality
 **	1.03.02 : 2011.03.14_12.01.11.PM
 *		Fixed invalid links for non-grocery items
 **	1.03.01 : 2011.03.14_10.17.51.AM
 *		Changed 'Linked' to 'Virtual' and fixed display (one per line)
 **	1.03.00 : 2011.03.13_12.39.50.AM
 *		!! THIS VERSION INVALIDATES PREVIOUS VERSION'S REMOTE SKU INFO !!
 *		Added multi-home products cleaned up (layout + colors)
 *			cleanup covers single home products as well
 *		Added check for virtual skus (linked skus)
 *		Added sibling skus (under primary product)
 *		Added display for orphan products (strikethrough)
 *		Fixed product links for grocery items, now working
 *		Added check to find SKU when it's not in the URL (like after a save)
 **	1.02.00 : 2011.03.10_02.15.25.AM
 *		Added check for multiple product homes
 *		Added check for dev[0-9] (anything > 9 will need a code change)
 *		Added new app version in auto update msg
 **	1.01.04 : 2010.10.06_06.03.44.PM
 *		Changed new tab id to use a random number instead of always using "_new"
 **	1.01.02/1.01.03 : 2010.04.06_03.16.17.PM
 *		Fixed for stage/stagepreview
 **	1.01.01 : 2010.04.06_10.30.21.AM
 *		Updated URLs to preview pages
 **	1.01.00 : 2010.03.21_08:00:41.PM
 *		Added Auto Update functionality
 *		Added Auto Update (force)
 **	1.00.00 : 2010.03.20_01:39:17.AM
 *		Initial version
 *
 */

/*-----------------------------------------------------------------------------


-----------------------------------------------------------------------------*/


/* -- GLOBAL variables -- */

	/* Menu variables */
		
		//holds sku code
			var sku = '';

	/* URL variables */
		//url operating on for dev/stage compatibility
			var urlMain = '';
			
			var urllookup = '';

/* -- GLOBAL variables -- */

/* -- GENERIC functions -- */

	/*
	 *	decide on web hostname based on erpsy hostname
	 *	pass a url to the function (OPTIONAL)
	 *	default is to use current location
	 */
		function getCluster(loca) {
			var locaV = loca || window.location.hostname;
			if (locaV.indexOf('dev') > -1) {
				var regexS = "crm[0-9]*[\.]stdev[0-9]?([0-9]*)";
				var regex = new RegExp( regexS );
				var results = regex.exec( locaV );
				if (results == null) {
					urllookup = 'http://dev.freshdirect.com';
					urlMain = 'http://devpreview.freshdirect.com';
				} else {
					if (results[1] == '1') {
						results[1] = ''; //clear 1, we don't need it
					}
					urllookup = 'http://dev'+results[1]+'.freshdirect.com';
					urlMain = 'http://dev'+results[1]+'preview.freshdirect.com';
				}
			}else if (locaV.indexOf('stg') > -1 || locaV.indexOf('stage') > -1) {
				urllookup = 'http://stage.freshdirect.com';
				urlMain = 'http://stagepreview.freshdirect.com';
			}

			return true;
		}

	/*
	 *	get URL param
	 */
		function gup(key) {
			key = key.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
			var regexS = "[\\?&]"+key+"=([^&#]*)";
			var regex = new RegExp( regexS );
			var results = regex.exec( window.location.href );
			if (results == null) {
				return "";
			} else {
				return results[1];
			}
		}

	/* magic protoype $ function */
		function $() {
			var elements = new Array();
			for (var i = 0; i < arguments.length; i++) {
				var element = arguments[i];
				if (typeof element == 'string')
					element = document.getElementById(element);
				if (arguments.length == 1)
					return element;
				elements.push(element);
			}
			return elements;
		}

	/*	function to handle inserting elements
	 *
	 *	target [type STRING] : the id of the target element to insert
	 *		new element before or after
	 *
	 *	insert [type STRING] : element type to insert
	 *		i.e. 'div', 'hr', 'p', etc
	 *	BIA [type STRING] : insert element before, inside, or after target
	 *		defaults to AFTER
	 *	insId [type STRING] : id to give new element
	 */
		function insEle(target, insert, BIA, insId) {
			var targetElement, newElement, id;
			(target!='' && $(target))
				?targetElement = $(target)
				:targetElement = document.getElementsByTagName("body")[0];
			
			if (targetElement) {
				newElement = document.createElement(insert);
				newElement.id = insId;

				switch(BIA) {
					case 'B':
					  targetElement.parentNode.insertBefore(newElement, targetElement);
					  break;    
					case 'I':
					  targetElement.appendChild(newElement, targetElement);
					  break;
					case 'A':
					default:
					  targetElement.parentNode.insertBefore(newElement, targetElement.nextSibling);
				}
			}
			return true;
		}
		
	/*
	 *	parse out the current SAP id from page text
	 */
		function getSapId(parseText) {
			
			fdLog.debug('[SEARCH PARSE] Parsing SAP id...'); 
			var regexS = "\<td\>([0-9]{18})\<\/td>";
			var regex = new RegExp( regexS );
			var results = regex.exec( parseText );
			if (results == null) {
				fdLog.debug('[SEARCH PARSE] ...No SAP id found!'); 
				return "";
			} else {
				fdLog.debug('[SEARCH PARSE] ...SAP id found: '+results[1]); 
				return results[1];
			}
		}

	/* take a value, type and return a link to the erpsy daisy search */
		function getSearchUrl(searchTerm, type) {
			if (type == 'sku' || type == 'WEBID') {
				return '/ERPSAdmin/product/product_search.jsp?searchterm='+searchTerm+'&searchtype=WEBID';
			}else if (type == 'SAPID') {
				return '/ERPSAdmin/product/product_search.jsp?searchterm='+searchTerm+'&searchtype=SAPID';
			}
			return searchTerm; //no type, just return original value
		}

	/* return an anchor tag linking to erpsy daisy search */
		function getLinkededSearch(aText, type) {
			return '<a href="'+getSearchUrl(aText, type)+'">'+aText+'</a>';
		}
	
	/* return an anchor tag linking to www product page */
		function getLinkedWWW(aText, catId, prodId, isGrocery) {
			var d = new Date();
			if (isGrocery && isGrocery != 'false') {
				return '<a href="'+urlMain+'/category.jsp?catId='+catId+'&prodCatId='+catId+'&productId='+prodId+'" target="_new'+(Math.floor(d.getTime()*Math.random()))+'">'+aText+'</a>';
			}
			return '<a href="'+urlMain+'/product.jsp?catId='+catId+'&productId='+prodId+'" target="_new'+(Math.floor(d.getTime()*Math.random()))+'">'+aText+'</a>';
		}

	/* return an anchor tag linking to product view */
		function getLinkedProductView (aText) {
			return '<a href="/ERPSAdmin/product/product_view.jsp?skuCode='+aText+'">'+aText+'</a>';
		}

	/*
	 *	get a search result
	 */
		function getSearch(searchTerm, originalSkuCode) {
			fdLog.debug('[GET SEARCH] Getting Search...' +getSearchUrl(searchTerm, 'SAPID')); 
			if (searchTerm === undefined || searchTerm == '') {
				return;
			}
			new Ajax.Request(getSearchUrl(searchTerm, 'SAPID'), {
				method: 'GET',
				onComplete: function(responseDetails) {
					fdLog.debug('[GET SEARCH] ...success, parsing...'); 
					parseGetSearchResult(responseDetails.responseText, originalSkuCode);
				},
				onError: function() {
					fdLog.debug('[GET SEARCH] Search Result: Error! '+responseDetails.status+' '+responseDetails.responseText); 
				}
			});
		}
	
	/*
	 *	parse search result and add to page
	 */
		function parseGetSearchResult(parseText, originalSkuCode) {
			fdLog.debug('[PARSE SEARCH] Parsing Search...'); 
			var regexS = '<td><a.*>([A-Z]*[0-9]*)</a></td>';
			var regex = new RegExp( regexS, 'g' );
			
			var results = [];
			var match;
			while (match = regex.exec(parseText)){
				results.push(match[1]);
			}

			if (results.length == 0) {
				fdLog.debug('[PARSE SEARCH] ...error, could not parse results!'); 
				fdLog.debug(parseText); 
				//do nothing
			} else {
				fdLog.debug('[PARSE SEARCH] ...success, found results, adding virtuals...'); 
				//found sku(s), loop through results and add to page
				for (var i = 0; i < results.length; i++) { //skip first, it's the parse string
					if (results[i] != originalSkuCode) { //skip original search
						fdLog.debug('[PARSE SEARCH] ...found virtual sku, adding...'); 
						//insert each virtual into our content table
						while(!insEle('_skuCont_tdVirtual', 'div', 'I', '_virtual'+results[i]));

						$('_virtual'+results[i]).innerHTML += 'Virtual SKU: '+getLinkedProductView(results[i]);
					}else{
						fdLog.debug('[PARSE SEARCH] ...found original sku, skipping...'); 
					}
				}
			}
			
			fdLog.debug('[PARSE SEARCH] ... done.'); 
		}
	
	/* find sku code in page */
		function findSkuInPage() {
			fdLog.debug('[PARSE SKU] No SKU in URL, trying to locate in page...'); 

			var regexS = '<td>([A-Z]{3}[0-9]*)</td>';
			var regex = new RegExp( regexS );
			match = regex.exec(document.body.innerHTML);
			if (match != null) {
				fdLog.debug('[PARSE SKU] ... done.'); 
				return match[1];
			}else{
				fdLog.debug('[PARSE SKU] Error... Could not locate SKU.'); 
			}
			return '';
		}

	/* construct a container table for the sku's info
	 *	inElem = element id to create table in
	 */
		function constructContainerTable(inElem) {
			//store innerHTML first (sku code)
			var skuCode = $(inElem).innerHTML;
			//and wipe it
			$(inElem).innerHTML = '';

			//create table
			while(!insEle(inElem, 'table', 'I', '_skuContTable'));
				while(!insEle('_skuContTable', 'tr', 'I', '_skuCont_trPrimary'));
					while(!insEle('_skuCont_trPrimary', 'td', 'I', '_skuCont_tdPrimary'));
					//put sku code back in
					$('_skuCont_tdPrimary').innerHTML = skuCode;
					while(!insEle('_skuCont_tdPrimary', 'td', 'A', '_skuCont_tdVirtual'));
				while(!insEle('_skuContTable', 'tr', 'I', '_skuCont_trRelated'));
					while(!insEle('_skuCont_trRelated', 'td', 'I', '_skuCont_tdRelatedPrimary'));
					while(!insEle('_skuCont_tdRelatedPrimary', 'td', 'A', '_skuCont_tdRelatedSecondary'));
				while(!insEle('_skuContTable', 'tr', 'I', '_skuCont_components_tr'));
					while(!insEle('_skuCont_components_tr', 'td', 'I', '_skuCont_tdComponents'));
					while(!insEle('_skuCont_components_tr', 'td', 'I', '_skuCont_tdComponentsOpt'));

			//insert key table
			while(!insEle('_skuContTable', 'table', 'A', '_keyTable'));
				while(!insEle('_keyTable', 'tr', 'I', '_keyTableTR0'));
					while(!insEle('_keyTableTR0', 'td', 'I', '_keyTableTHKey'));
				while(!insEle('_keyTable', 'tr', 'I', '_keyTableTR1'));
					while(!insEle('_keyTableTR1', 'td', 'I', '_keyTableTD0'));
			//key contents
			$('_keyTableTHKey').innerHTML += 'Display Key:';
			$('_keyTableTD0').innerHTML += '<div><div class="www1LinkBG">&nbsp;</div>WWW Primary Product Link</div>';
			$('_keyTableTD0').innerHTML += '<div><div class="www2LinkBG">&nbsp;</div>WWW Related Product Link</div>';
			$('_keyTableTD0').innerHTML += '<br style="clear: both;"/>';
			$('_keyTableTD0').innerHTML += '<div><div class="localLinkBG">&nbsp;</div>ERPSy-Daisy Link</div>';
			$('_keyTableTD0').innerHTML += '<div><div class="defSKUBG">&nbsp;</div>Default SKU</div>';
			$('_keyTableTD0').innerHTML += '<div><div class="prefSKUBG">&nbsp;</div>Preferred SKU</div>';
			$('_keyTableTD0').innerHTML += '<div class="orphan">Orphan Product</div>';

			return true;
		}

	/* fetch sku info */
		function fetchSkuInfo(skuCode) {
			
			fdLog.debug('[FETCH SKU INFO] Fetching SKU from...' + urllookup+'/test/freemarker_testing/all_info.jsp?sku2url=true&sku='+skuCode); 

			new Ajax.Request(urllookup+'/test/freemarker_testing/all_info.jsp?sku2url=true&sku='+skuCode, {
				method: 'GET',
				onComplete: function(responseDetails) {
						var skuInfo = responseDetails.responseText;

						fdLog.debug('[FETCH SKU INFO] success, parsing...'); 
						parseSkuInfo(skuInfo);
				},
				onError: function() {
					fdLog.debug('[FETCH SKU INFO] Error... '+responseDetails.status+' '+responseDetails.responseText); 
				}
			});
		}

	/* parse skuInfo */
		function parseSkuInfo(skuInfo) {
			fdLog.debug('[PARSE SKU INFO] Parsing SKU info...'); 
			
			var skuInfoObj = JSON.parse(skuInfo);				

			//run through skuInfo and get the objects as we need them
			skuInfo = skuInfoObj.productModels;			

			for (var objName in skuInfo) {
				var prefSku = skuInfo[objName].prefSku;
				var defSku = skuInfo[objName].defSku;
				var skuCodes = skuInfo[objName].skuCodes;
				var curObj = skuInfo[objName];
				var linkText = objName.split('@'); //0=prodId, 1=catId
				var linkHtml = getLinkedWWW('@'+linkText[1], linkText[1], linkText[0], skuInfo[objName].isGrocery);
				
				fdLog.debug('prefSku:' + prefSku);
				fdLog.debug('defSku:' + defSku);
				fdLog.debug('skuCodes:' + skuCodes);
				fdLog.debug('curObj:' + curObj);
				fdLog.debug('linkText:' + linkText);
				fdLog.debug('linkHtml:' + linkHtml);

				if (skuInfo[objName].isOrphan) {
					linkHtml = '<span class="orphan">'+linkHtml+'</span>';
				}

				if (skuInfo[objName].isPrimary) {
					//primary product model, add to table
					//insert each virtual into our content table
					while(!insEle('_skuCont_tdRelatedPrimary', 'div', 'I', '_relatedPrimary'+objName));

					$('_relatedPrimary'+objName).innerHTML += 'Primary Product: ('+linkHtml+')<br />';
					$('_relatedPrimary'+objName).innerHTML += 'Sibling SKU(s):<br />';
					for (var i =0; i < skuCodes.length; i++) {
						$('_relatedPrimary'+objName).innerHTML += '&nbsp;&nbsp;&nbsp;';
						if (skuCodes[i] != sku) {
							$('_relatedPrimary'+objName).innerHTML += '<span class="siblingSKU">'+getLinkedProductView(skuCodes[i])+'</span> ('+skuCodes[i+1]+')';
						}else{
							$('_relatedPrimary'+objName).innerHTML += skuCodes[i]+ ' ('+skuCodes[i+1]+')';
						}
						if (skuCodes[i] == defSku) {
							$('_relatedPrimary'+objName).innerHTML += '<span class="defSKU">*</span>';
						}
						if (skuCodes[i] == defSku) {
							$('_relatedPrimary'+objName).innerHTML += '<span class="prefSKU">*</span>';
						}
						$('_relatedPrimary'+objName).innerHTML += '<br />';
						i++;
					}
				}else{
					//secondary
					while(!insEle('_skuCont_tdRelatedSecondary', 'div', 'I', '_relatedSecondary'+objName));
					$('_relatedSecondary'+objName).innerHTML += 'Related Product: ('+linkHtml+')<br />';
				}
			}

			//now do components if we have any
			skuInfo = skuInfoObj.componentGroups;

			
			for (var objName in skuInfo) {
				var curObj = skuInfo[objName];

				var curObjPms = null;

				curObjPms = curObj.productModels;

				if (curObjPms != null) {
					//optional products instead
					for (var i =0; i < curObjPms.length; i++) {
						var linkText = curObjPms[i].split('@'); //0=prodId, 1=catId
						var linkHtml = getLinkedWWW('@'+linkText[1], linkText[1], linkText[0], curObjPms[i+1]); //this needs fixing, grocery
						while(!insEle('_skuCont_tdComponentsOpt', 'div', 'I', '_compProd'+curObjPms[i]));
						$('_compProd'+curObjPms[i]).innerHTML += 'Opt. Product: ('+linkHtml+')<br />';
						i++;
					}
				}else{
					for (objVarName in curObj) {
						var curVarObj = curObj[objVarName];
						var skuCodes = curVarObj.skuCodes;
						while(!insEle('_skuCont_tdComponents', 'div', 'I', '_compVar'+objVarName));
							$('_compVar'+objVarName).innerHTML += objVarName + '<a href="#" class="skuToggle" onclick="$(\'_compVar'+objVarName+'_cont\').toggle(); (this.innerHTML==\'show SKUs\')?this.innerHTML=\'hide SKUs\': this.innerHTML=\'show SKUs\'; return false;">show SKUs</a>';

						while(!insEle('_compVar'+objVarName, 'div', 'I', '_compVar'+objVarName+'_cont'));
							$('_compVar'+objVarName+'_cont').style.width = '99%';
							$('_compVar'+objVarName+'_cont').style.border = '0 none';
							$('_compVar'+objVarName+'_cont').style.display = 'none';
						
						for (var i =0; i < skuCodes.length; i++) { //sku, status, material
							$('_compVar'+objVarName+'_cont').innerHTML +='&nbsp;&nbsp;&nbsp;<span class="siblingSKU">'+getLinkedProductView(skuCodes[i])+'</span> ('+skuCodes[i+1]+') : <span class="siblingSKU">'+getLinkededSearch(skuCodes[i+2], 'SAPID')+'</span><br />';
							i++;i++;
						}
					}
				}
			}

			fdLog.debug('[PARSE SKU INFO] ... done.'); 
		}

/* -- GENERIC functions -- */
	
	function sku_urls() {

		while(!getCluster());

		sku = gup('skuCode');
		fdLog.debug('skuCode:' + sku);
		if (sku == '') {
			//no sku from URL? hm, did you just save? let's try to find it anyway...
			sku = findSkuInPage();
		}

		if (sku != '') {

			var tds = document.body.getElementsByTagName("td");
			var tdFound = false;
			var d = new Date();
			for (var i=0; i<tds.length; i++) {
				if (tds[i].innerHTML == sku) {
					tdFound = true;
					break;
				}
			}
			if (tdFound) {
				//we have the td where the sku is, it's tds[i]
				//add an id to it so we can refer to it easier
				tds[i].id = '_skuCodeContainer';
				while(!constructContainerTable('_skuCodeContainer'));

				//now get and add virtuals
				fdLog.debug('[ADD VIRT] Adding virtuals...'); 
				

				//get sku info
				fetchSkuInfo(sku);

			}else{
				fdLog.debug('[ADD VIRT] Error... cannot find td containing SKU code.'); 
			}

		}else{
			fdLog.debug('[SKU2URL] Error... no SKU code to act on, exiting.'); 
		}
		
	}

/* -- SETUP -- */