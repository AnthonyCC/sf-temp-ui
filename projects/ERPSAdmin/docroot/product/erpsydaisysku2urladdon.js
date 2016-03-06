(function($){
			/* parse value from reqParams in current window.location
	 * usage: var value = $.QueryString["PARAM_TO_GETVALUE_OF"];
	 */
	(function($) {
		$.QueryString = (function(a) {
			if (a == "") return {};
			var b = {};
			for (var i = 0; i < a.length; ++i)
			{
				var p=a[i].split('=');
				if (p.length != 2) continue;
				b[p[0]] = decodeURIComponent(p[1].replace(/\+/g, " "));
			}
			return b;
		})(window.location.search.substr(1).split('&'))
	})(jQuery);

	/* take a value, type and return a link to the erpsy daisy search */
	function getSearchUrl(searchTerm, type) {
		if (type == 'sku' || type == 'WEBID') {
			return '/ERPSAdmin/product/product_search.jsp?searchterm='+searchTerm+'&searchtype=WEBID';
		}else if (type == 'SAPID') {
			return '/ERPSAdmin/product/product_search.jsp?searchterm='+searchTerm+'&searchtype=SAPID';
		}
		return searchTerm; //no type, just return original value
	}

	/* return an anchor tag linking to product view */
	function getLinkedProductView(aText) {
		return '<a href="/ERPSAdmin/product/product_view.jsp?skuCode='+aText+'">'+aText+'</a>';
	}

	/* return an anchor tag linking to www product page */
	function getLinkedWWW(base, aText, catId, prodId, isGrocery) {
		var urlMain = FreshDirect.sku2url.base[base];
		var d = new Date();
		if (isGrocery && isGrocery != 'false') {
			return '<a href="'+urlMain+'/category.jsp?catId='+catId+'&prodCatId='+catId+'&productId='+prodId+'" target="_new'+(Math.floor(d.getTime()*Math.random()))+'">'+aText+'</a>';
		}
		return '<a href="'+urlMain+'/product.jsp?catId='+catId+'&productId='+prodId+'" target="_new'+(Math.floor(d.getTime()*Math.random()))+'">'+aText+'</a>';
	}

	/* return an anchor tag linking to erpsy daisy search */
	function getLinkededSearch(aText, type) {
		return '<a href="'+getSearchUrl(aText, type)+'">'+aText+'</a>';
	}

	/* parse skuInfo */
	function parseSkuInfo(skuInfo, base) {		
		var skuInfoObj = JSON.parse(skuInfo);

		//run through skuInfo and get the objects as we need them
		skuInfo = skuInfoObj.productModels;

		for (var objName in skuInfo) {
			var prefSku = skuInfo[objName].prefSku;
			var defSku = skuInfo[objName].defSku;
			var skuCodes = skuInfo[objName].skuCodes;
			var curObj = skuInfo[objName];
			var linkText = objName.split('@'); //0=prodId, 1=catId
			var linkHtml = getLinkedWWW(base, '@'+linkText[1], linkText[1], linkText[0], skuInfo[objName].isGrocery);
			var tempHtml = '';
			
			if (skuInfo[objName].isOrphan) {
				linkHtml = '<span class="orphan">'+linkHtml+'</span>';
			}

			if (skuInfo[objName].isPrimary) {
				//primary product model, add to table
				//insert each virtual into our content table

				tempHtml = '';
				tempHtml += '<div>';
					tempHtml += '<div>Primary Product: '+linkHtml+'</div>';
					tempHtml += '<div>Sibling SKU(s):</div>';
					tempHtml += '<div class="siblingSkus">';
						for (var i =0; i < skuCodes.length; i++) {
							tempHtml += '<div>';
								if (skuCodes[i] != FreshDirect.sku2url.sku) {
									tempHtml += '<span class="siblingSKU">'+getLinkedProductView(skuCodes[i])+'</span> ('+skuCodes[i+1]+')';
								}else{
									tempHtml += skuCodes[i]+ ' ('+skuCodes[i+1]+')';
								}
								if (skuCodes[i] == defSku) {
									tempHtml += '<span class="defSKU">*</span>';
								}
								if (skuCodes[i] == defSku) {
									tempHtml += '<span class="prefSKU">*</span>';
								}
							tempHtml += '</div>';
							i++;
						}
				tempHtml += '</div>';
				$('.'+base+' .skuCont_tdRelatedPrimary').append(tempHtml);
			} else {
				
				//secondary
				$('.'+base+' .skuCont_tdRelatedSecondary').append('<div>Related Product: '+linkHtml+'<br /></div>');
			}
		}

		//now do components if we have any
		skuInfo = skuInfoObj.componentGroups;

		
		for (var objName in skuInfo) {
			var curObj = skuInfo[objName];
			var curObjPms = curObj.productModels || null;

			if (curObjPms != null) {
				//optional products instead
				tempHtml = '';
				tempHtml += '<div>Opt. Product(s):</div>';
				tempHtml += '<div class="optProds">';
					for (var i =0; i < curObjPms.length; i++) {
						var linkText = curObjPms[i].split('@'); //0=prodId, 1=catId
						var linkHtml = getLinkedWWW(base, curObjPms[i], linkText[1], linkText[0], curObjPms[i+1]); //this needs fixing, grocery
						tempHtml += '<div>'+linkHtml+'</div>';
						i++;
					}
				tempHtml += '</div>';

				$('.'+base+' .skuCont_tdComponentsOpt').append(tempHtml);
			} else {
				//components
				
				tempHtml = '';
				tempHtml += '<div>Component:</div>';
				
				for (objVarName in curObj) {
					var curVarObj = curObj[objVarName];
					var skuCodes = curVarObj.skuCodes;
					
					tempHtml += '<div class="component">';
						tempHtml += '<span class="component-label">'+objVarName+':</span>';
						tempHtml += '<a href="#" class="skuToggle" onclick="jQuery(this).parent().find(\'.component-skus\').toggle(); (jQuery(this).html()==\'show SKUs\') ? jQuery(this).html(\'hide SKUs\') : jQuery(this).html(\'show SKUs\'); return false;">show SKUs</a>';
						tempHtml += '<div class="component-skus" style="display: none;">';
							for (var i =0; i < skuCodes.length; i++) { //sku, status, material
								tempHtml += '<div><span class="siblingSKU">'+getLinkedProductView(skuCodes[i])+'</span> ('+skuCodes[i+1]+') : <span class="siblingSKU">'+getLinkededSearch(skuCodes[i+2], 'SAPID')+'</span></div>';
								i++;i++;
							}
						tempHtml += '</div>';
					tempHtml += '</div>';

				}

				$('.'+base+' .skuCont_tdComponents').append(tempHtml);

			}
		}

	}

	/* fetch sku info */
	function fetchSkuInfo(skuCode, base, baseUrl) {

		/* virtual sku code removed */

		$.get('/ERPSAdmin/product/product_info.jsp?sku='+skuCode+'&uurl='+baseUrl, function(data) {
			
			$('#'+FreshDirect.sku2url.skucodeLinksContId+' .basesCont').append(
				'<div class="skuContTableCont baseCont '+base+'">'+
					'<div class="skuContTableBase">'+base+'</div>'+
					FreshDirect.sku2url.linksHtml+
				'</div>'
			);
			
			$('.'+base+' .skuCont_tdPrimary').html(FreshDirect.sku2url.sku);

			parseSkuInfo(data, base);
			
			$('#'+FreshDirect.sku2url.skucodeLinksContId+' .keyTable').show();
		});

	}

	function getSkuCode(qParam, elemId) {
		var sku = $.QueryString[qParam];
		if (sku == '') {
			//no sku from URL? hm, did you just save? let's try to find it anyway...
			sku = $(elemId).html();
		}

		if (sku != '') { //we have a sku
			return sku;
		}else{
			console.log('[sku2url] Error... no SKU code to act on, exiting.'); 
		}
	}

	$(document).on('ready', function() {
		/* to add additional urls, add them to base, defined in the jsp */
		$.extend(FreshDirect.sku2url, {
			'sku': '',
			'skucodeContId': 'skuCode',
			'skucodeLinksContId': 'skuCode_links',
			'linksHtml': '<table class="skuContTable"><tr class="skuCont_trPrimary"><td colspan="2" class="skuCont_tdPrimary"></td></tr><tr class="skuCont_trRelated"><td class="skuCont_tdRelatedPrimary"></td><td class="skuCont_tdRelatedSecondary"></td></tr><tr class="skuCont_components_tr"><td class="skuCont_tdComponents"></td><td class="skuCont_tdComponentsOpt"></td></tr></table>',
			'keyHtml': '<table class="keyTable" style="display: none;"><tr class="keyTableTR0"><td class="keyTableTHKey">Display Key:</td></tr><tr class="keyTableTR1"><td class="keyTableTD0"><div><div class="www1LinkBG">&nbsp;</div>WWW Primary Product Link</div><div><div class="www2LinkBG">&nbsp;</div>WWW Related Product Link</div><br style="clear: both;"><div><div class="localLinkBG">&nbsp;</div>ERPSy-Daisy Link</div><div><div class="defSKUBG">&nbsp;</div>Default SKU</div><div><div class="prefSKUBG">&nbsp;</div>Preferred SKU</div><div class="orphan">Orphan Product</div></td></tr></table>'
		});

		FreshDirect.sku2url.sku = getSkuCode(FreshDirect.sku2url.skucodeContId, '#'+FreshDirect.sku2url.skucodeContId);

		if (FreshDirect.sku2url.sku !== '') {
			$('#'+FreshDirect.sku2url.skucodeLinksContId).append('<div class="basesCont"></div>')
			//add links container(s)
			for (var base in FreshDirect.sku2url.base) {

				//get sku info
				fetchSkuInfo(FreshDirect.sku2url.sku, base, FreshDirect.sku2url.base[base]);
			}


			//add display key
			$('#'+FreshDirect.sku2url.skucodeLinksContId).append(FreshDirect.sku2url.keyHtml);

		}

	});
})(jQuery);