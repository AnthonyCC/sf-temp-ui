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
FD_QuickBuy._nextSibling = function(n) {
	x=n.nextSibling;
	while (x.nodeType != 1) {x=x.nextSibling;}
	return x;
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
}



/**
 * Display Quick Buy panel
 * 
 * 
 * @param {Object} deptId Department ID of product
 * @param {Object} catId Parent category ID of product
 * @param {Object} prdId Product ID
 */
FD_QuickBuy.showPanel = function(deptId, catId, prdId) {
	return function() {
		var elementId= prdId+'_'+FD_QuickBuy._randomId(16);
		var ctPanel = new YAHOO.widget.Panel(elementId, {
			fixedcenter: true, 
			constraintoviewport: true, 
			underlay: "matte", 
			close: true, 
			visible: true,
			modal: true,
			draggable: false}
		);
		var isWineDept = ("usq" == deptId);
		
		ctPanel.setHeader( "&nbsp;" );

		var winTitle = document.title.substring(14);

		var content = "";
		content += '<div id="'+elementId+'_ctnt">\n';
		content += '  <div id="'+elementId+'_overbox" class="overbox">\n';
		content += '    <div id="'+elementId+'_nfeat" class="nfeat roundedbox"></div>\n';
		content += '    <div id="'+elementId+'_errors" class="alerts roundedbox"></div>\n';
		content += '  </div>\n';
		content += '  <iframe id="'+elementId+'_frame" frameborder="0" src="/quickbuy/product.jsp?catId='+catId+'&amp;productId='+prdId+'&amp;fdsc.source=quickbuy&amp;refTitle='+escape(winTitle)+'&amp;referer='+escape(window.location.href)+'&amp;uid='+elementId+'" class="prodframe"></iframe>';
		content += '</div>\n';
		
		ctPanel.setBody( content );
		
		ctPanel.render(document.body);
		
		// override .yui-panel hidden setting
		YAHOO.util.Dom.get(elementId).style.overflow = "visible";


		YAHOO.util.Dom.addClass(elementId+'_c','quickbuy-dialog');
		
		if (isWineDept) {
			YAHOO.util.Dom.addClass(ctPanel.header, 'hd_bg_wine');
			YAHOO.util.Dom.addClass( FD_QuickBuy._nextSibling(ctPanel.body), 'container-close_wine' );
		} else {
			YAHOO.util.Dom.addClass(ctPanel.header, 'hd_bg_normal');
			YAHOO.util.Dom.addClass( FD_QuickBuy._nextSibling(ctPanel.body), 'container-close_normal' );
		}
		
		ctPanel.hideEvent.subscribe(function(e){
			YAHOO.util.Dom.get(elementId+'_overbox').style.visibility = "hidden";
		});
		
		document.quickbuyPanel = ctPanel;

		// show panel
		ctPanel.center();
		ctPanel.show();

		// Load New Feature popup content
		FD_QuickBuy.loadNewFeatureInner(elementId);
	};
};


FD_QuickBuy.loadNewFeatureInner = function(panelId) {
	var attrs = "element="+panelId;
	var cObj = YAHOO.util.Connect.asyncRequest('POST', '/ajax/nfeat_inner.jsp', {
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
	var cObj = YAHOO.util.Connect.asyncRequest('POST', '/ajax/nfeat.jsp', {
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
}


/**
 * Refined version of attach function
 * 
 * @param {Object} hotspot Hotspot area where mouse is detected
 * @param {Object} btn Quick Buy button
 * @param {Object} prd Product object with three attributes: departmentId, categoryId and productId
 * 
 */
FD_QuickBuy.decorate = function(hotspot, btn, prd) {
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


	// BUTTONS / click receivers
	//
	var __panel = FD_QuickBuy.showPanel(prd.departmentId, prd.categoryId, prd.productId);

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
