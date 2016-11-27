if (typeof window.FD_QuickBuy == "undefined") {
	FD_QuickBuy = {};
}



/*** ANIMATION ***/
FD_QuickBuy.DELAY = 250; // milliseconds
FD_QuickBuy.ANIM_PERIOD = 0.3; // seconds
FD_QuickBuy.animFadeIn = function(button) {
	return new YAHOO.util.Anim(button,
		{
		    opacity: { to: 1 } 
		},
		FD_QuickBuy.ANIM_PERIOD,
		YAHOO.util.Easing.easeIn
	);
};
FD_QuickBuy.animFadeOut = function(button) {
	return new YAHOO.util.Anim(button,
		{
		    opacity: { to: 0 } 
		},
		FD_QuickBuy.ANIM_PERIOD,
		YAHOO.util.Easing.easeOut
	);	
};




// util
FD_QuickBuy._getCloseButton = function(n) {
	var x = n.parentNode;
	var nodes = YAHOO.util.Dom.getElementsByClassName('container-close', 'a', x);
	if (nodes.length)
		return nodes[0];
};

// util
FD_QuickBuy._chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz'.split('');
FD_QuickBuy._randomId = function(length) {
    var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz'.split('');
    
    if (! length) {
        length = Math.floor(Math.random() * this._chars.length);
    }
    
    var str = '';
    for (var i = 0; i < length; i++) {
        str += chars[Math.floor(Math.random() * this._chars.length)];
    }
    return str;
};

//current wineDeptid (set in decorate)
FD_QuickBuy._wineDeptId = null;


/**
 * Method used to decorate url with tracking parameters
 * 
 * @param uri
 * @param tracking Object holding tracking codes
 * @returns
 */
FD_QuickBuy._includeTrackingCodes = function(uri, tracking) {
	var f = function(obj, u, params) {
		var i, key;
		for (i=0; i<params.length; i++) {
			key = params[i];
			if (obj[key])
				u += '&amp;'+key+'='+encodeURIComponent( obj[key] );
		}
		return u;
	};

	if (typeof tracking !== 'undefined') {
		uri = f(tracking, uri,
			['variant', 'ymalSetId', 'originatingProductId', 'impId']);

		if (tracking.trk) {
			uri = f(tracking, uri,
				['trk', 'trkd', 'rank']);
		}
	}

	return uri;
};



/**
 * Display Quick Buy panel
 * 
 * 
 * @param {String} deptId Department ID of product
 * @param {String} catId Parent category ID of product
 * @param {String} prdId Product ID
 * @param {String} iatcNamespace 'instant add to cart' namespace (optional)
 * @param {Object} tracking Object holding various tracking parameters [optional]
 */

FD_QuickBuy.showPanel = function(deptId, catId, prdId, iatcNamespace, tracking) {
	return function() {
		var elementId= prdId+'_'+FD_QuickBuy._randomId(16), oStyle;
		var ctPanel = new YAHOO.widget.Panel(elementId, {
			fixedcenter: true, 
			constraintoviewport: true, 
			underlay: "matte", 
			close: true, 
			visible: false,
			modal: true,
			zIndex: 9900,
			draggable: false}
		);
		var wineDeptId = (FD_QuickBuy._wineDeptId==null) ? '' : FD_QuickBuy._wineDeptId;
		var isWineDept = ((deptId).toUpperCase() == (wineDeptId).toUpperCase());
		
		if(isWineDept) {
			oStyle={
				closeButton:'container-close_wine',
				header:'hd_bg_wine'
			};
		} else {
			oStyle=FD_QuickBuy.style;			
		}
		
		ctPanel.setHeader( "&nbsp;" );

		var winTitle = document.title.substring(14);

		

		var uri = '/quickbuy/product.jsp?catId='+encodeURIComponent(catId)+'&amp;productId='+encodeURIComponent(prdId)+'&amp;fdsc.source=quickbuy&amp;refTitle='+escape(winTitle)+'&amp;referer='+escape(window.location.href)+'&amp;uid='+elementId+'&amp;coremetrics.pageid='+encodeURIComponent(FreshDirect.Coremetrics.pageId)+'&amp;coremetrics.pagecontenthierarchy='+encodeURIComponent(FreshDirect.Coremetrics.pageContentHierarchy);

		// store DOM ID
		uri += '&amp;uid='+encodeURIComponent(elementId);

		// include various codes for tracking purposes
		if (tracking && tracking.trk) {
			tracking.trkd = 'qb';
		}
		uri = FD_QuickBuy._includeTrackingCodes(uri, tracking);
		
		// store master page URL and title for back-reference
		uri += '&amp;refTitle='+encodeURIComponent(winTitle)+'&amp;referer='+encodeURIComponent(window.location.href);

		var content = "";
		if (iatcNamespace)
			uri += '&amp;iatcNamespace='+escape(iatcNamespace);
		content += '<div id="'+elementId+'_ctnt">\n';
		content += '  <div id="'+elementId+'_overbox" class="overbox">\n';
		content += '    <div id="'+elementId+'_nfeat" class="nfeat roundedbox"></div>\n';
		content += '    <div id="'+elementId+'_errors" class="alerts roundedbox"></div>\n';
		content += '  </div>\n';
		content += '<div class="quickbuy-loading">Loading product...</div>\n';		
		content += '<iframe id="'+elementId+'_frame" frameborder="0" src="'+uri+'" class="prodframe" style="height:1px"></iframe>';
		content += '</div>\n';
		
		ctPanel.setBody( content );
		
		ctPanel.render(document.body);
		
		// override .yui-panel hidden setting
		YAHOO.util.Dom.get(elementId).style.overflow = "visible";

		YAHOO.util.Dom.addClass(elementId+'_c','quickbuy-dialog');
		
		YAHOO.util.Dom.addClass( ctPanel.header, oStyle.header );
		YAHOO.util.Dom.addClass( FD_QuickBuy._getCloseButton(ctPanel.body), oStyle.closeButton );
		
		ctPanel.hideEvent.subscribe(function(e){
			ctPanel.setBody(" ");
			setTimeout(function() {ctPanel.destroy();}, 0);
		});
		
		document.quickbuyPanel = ctPanel;

		// show panel
		ctPanel.show();

		// Load New Feature popup content
		FD_QuickBuy.loadNewFeatureInner(elementId);
	};
};


FD_QuickBuy.loadNewFeatureInner = function(panelId) {
	var attrs = "element="+panelId;
	YAHOO.util.Connect.asyncRequest('POST', '/ajax/nfeat_inner.jsp', {
		success: function(resp) {
			if (resp.status == 200) {
				var obj = YAHOO.util.Dom.get(panelId+"_nfeat");
				obj.innerHTML = resp.responseText;
				
				obj.style.display = "block";
			}
		}
	}, attrs);
};


// 'close' button handler
FD_QuickBuy.closeNewFeatBox = function(nfeatId) {
	YAHOO.util.Connect.asyncRequest('POST', '/ajax/nfeat.jsp', {
		success: function(resp) {
			YAHOO.util.Dom.get(nfeatId).style.display = "none";
			document.quickbuyPanel.center();
		}
	});
};


FD_QuickBuy._attachHotspot = function(hotspot, btn, multiple) {
	if (btn.animFadeIn == undefined)
		btn.animFadeIn = FD_QuickBuy.animFadeIn(btn);
	if (btn.animFadeOut == undefined)
		btn.animFadeOut = FD_QuickBuy.animFadeOut(btn);
	
	if (FD_QuickBuy.DELAY == 0) {
		// WITHOUT DELAY
		//
		YAHOO.util.Event.on(hotspot, "mouseenter", function(e) {
			btn.animFadeIn.animate();
		});
	
		YAHOO.util.Event.on(hotspot, "mouseleave", function(e) {
			btn.animFadeOut.animate();
		});
	} else {
		// WITH DELAY
		//
		YAHOO.util.Event.on(hotspot, "mouseenter", function(e) {
			if (hotspot.later != undefined && hotspot.later != null) {
				hotspot.later.cancel();
				hotspot.later = null;
			}
	
			hotspot.later = YAHOO.lang.later(FD_QuickBuy.DELAY, hotspot, function() {
				hotspot.later = null;

				btn.animFadeIn.animate();
			}, null, false );
		});

		YAHOO.util.Event.on(hotspot, "mouseleave", function(e) {
			if (hotspot.later != undefined && hotspot.later != null) {
				hotspot.later.cancel();
				hotspot.later = null;
			}

			btn.animFadeOut.animate();
		});
	}
};


/**
 * Refined version of attach function
 * 
 * @param {Object} hotspot Hotspot area where mouse is detected
 * @param {Object} btn Quick Buy button
 * @param {Object} prd Product object with three attributes: departmentId, categoryId and productId
 * 					optional fourth param the current wine dept id
 * @param {Object} tracking Optional parameter. It contains various codes such as variantId, impressionId, etc.
 * 
 */
FD_QuickBuy.decorate = function(hotspot, btn, prd, tracking) {
	var __btns = YAHOO.lang.isArray(btn) ? btn : [btn];
	var __btn = YAHOO.util.Dom.get(__btns[0]); // default button


	// HOTSPOT(s)
	//	
	if (hotspot) {
		if (YAHOO.lang.isArray(hotspot)) {
			for (k in hotspot) {
				this._attachHotspot(YAHOO.util.Dom.get(hotspot[k]), __btn, true);
			}
		} else {
			this._attachHotspot(YAHOO.util.Dom.get(hotspot), __btn, false);
		}
	}

	//check if wineDeptId is specified, and set
	if (prd.hasOwnProperty('wineDeptId')) {
		FD_QuickBuy._wineDeptId = prd.wineDeptId;
	}

	// BUTTONS / click receivers
	//
	var __panel = FD_QuickBuy.showPanel(prd.departmentId, prd.categoryId, prd.productId, null, tracking);

	var k;
	for (k in __btns) {
		__btn = YAHOO.util.Dom.get(__btns[k]);
		if (null != __btn) {
			__btn.showPanel = __panel;
			YAHOO.util.Event.on(__btn, "click", function(e) {
				YAHOO.util.Event.stopEvent(e);
				this.showPanel();
			});
		}
	}
};

FD_QuickBuy.style = {
		closeButton:'container-close_normal',
		header:'hd_bg_normal'
};


(new YAHOO.util.YUILoader({
    require: ["event-delegate", "selector"],
    base:'/assets/yui-2.9.0/',
    loadOptional: true,
    onSuccess: function() {
		YAHOO.util.Event.delegate(document.body, "click", function(e, matchedEl, container) {
			if (document.quickbuyPanel) {
				document.quickbuyPanel.hide();
			}
		}, ".mask");
    },
    timeout: 10000
})).insert();
 

