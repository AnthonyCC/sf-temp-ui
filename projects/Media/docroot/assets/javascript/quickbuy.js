if (typeof FD_QuickBuy == "undefined" || !FD_QuickBuy) {
	FD_QuickBuy = {};
}




FD_QuickBuy.DELAY = 250; // milliseconds
FD_QuickBuy.ANIM_PERIOD = 0.3; // seconds
FD_QuickBuy.animFadeIn = function(frame) {
	var _self = this;
	return new YAHOO.util.Anim(frame,
		{
		    opacity: { to: 1 } 
		},
		_self.ANIM_PERIOD,
		YAHOO.util.Easing.easeIn
	);
};
FD_QuickBuy.animFadeOut = function(frame) {
	var _self = this;
	return new YAHOO.util.Anim(frame,
		{
		    opacity: { to: 0 } 
		},
		_self.ANIM_PERIOD,
		YAHOO.util.Easing.easeOut
	);	
};




FD_QuickBuy.postForm = function(formObject, frameId) {
	YAHOO.util.Connect.setForm(formObject);
	var cObj = YAHOO.util.Connect.asyncRequest('POST', '/ajax/qb.jsp', {
		success: function(resp) {
			$(frameId+'_inner').innerHTML = resp.responseText;
			$(frameId+'_errors').style.display = "none";

			updateYourCartPanel();
		},
		failure:function(resp) {
			var i;
			var ea=YAHOO.lang.JSON.parse(resp.responseText);
			var	l=ea.length,alertBox=$(frameId+'_errors'),errorDiv;
			
			alertBox.innerHTML = '<div class="title16 boxTitle">ERROR!</div>';
			for(i=0;i<l;i++) {
				errorDiv=document.createElement('div');
				errorDiv.innerHTML=ea[i].errorDesc;
				alertBox.appendChild(errorDiv);
				YAHOO.util.Dom.addClass(errorDiv, 'text13gr');
			}

			alertBox.style.display = "block";
		}
		
	});

	return cObj;
};



FD_QuickBuy.showPopup = function(frameId, namespace, catId, prdId, img, imgW, imgH) {
	return function() {
		var elementId='d_'+frameId;
		var ctPanel = new YAHOO.widget.Panel(elementId, {
			fixedcenter: false, 
			constraintoviewport: true, 
			underlay: "matte", 
			close: true, 
			visible: false,
			modal: true,
			draggable: false}
		);
		
		var contentFrame = $(frameId+'_ctnt');
		ctPanel.setHeader( "&nbsp;" );
		ctPanel.setBody( '<form name='+frameId+'>' + contentFrame.innerHTML + '</form>');

		ctPanel.render(document.body);
		
		// override .yui-panel hidden setting
		$(elementId).style.overflow = "visible";

		ctPanel.body.setAttribute("id", "bod_"+frameId);
		ctPanel.body.style.width = "410px";
		ctPanel.body.style.height = "300px";
		
		eval(namespace+".updateTotal();");
		
		ctPanel.show();
		ctPanel.center();
		YAHOO.util.Dom.addClass(elementId+'_c','quickbuy-dialog');
		
		
		ctPanel.hideEvent.subscribe(function(e){
			$(frameId+'_overbox').style.visibility = "hidden";
		});
		
		document.quickbuyPanel = ctPanel;

		// Load New Feature popup content
		FD_QuickBuy.loadNewFeatureInner(frameId, true);
	};
};


FD_QuickBuy.loadNewFeatureInner = function(frameId, decr) {
	var attrs = "frameId="+frameId;
	if (false == decr)
		attrs += "&test=1";
	var cObj = YAHOO.util.Connect.asyncRequest('POST', '/ajax/nfeat_inner.jsp', {
		success: function(resp) {
			if (resp.status == 200) {
				var obj = $(frameId+"_nfeat");
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
			$(nfeatId).style.display = "none";
		}
	});
};


/**
 * 
 * @param {Object} frameId
 * @param {Object} catId
 * @param {Object} prdId
 * @param {Object} img
 * @param {Object} imgW
 * @param {Object} imgH
 */
FD_QuickBuy.attach = function(frameId, namespace, catId, prdId, img, imgW, imgH) {
	var _self = this;

	var frame = $(frameId);
	var btn = $('qb_'+frameId);
	var prodImgBtn = $('prdImgAncr_'+frameId);

	frame.animFadeIn = this.animFadeIn(btn);
	frame.animFadeOut = this.animFadeOut(btn);
	frame.showPopup = FD_QuickBuy.showPopup(frameId, namespace, catId, prdId, img, imgW, imgH);
	frame.canClick = false;


	if (FD_QuickBuy.DELAY == 0) {
	
		$E.on(frame, "mouseenter", function(e){
			this.canClick = true;
			this.animFadeIn.animate();
		});
		
		$E.on(frame, "mouseleave", function(e){
			this.animFadeOut.animate();
			this.canClick = true;
		});
	} else {
		frame.animFadeIn.onComplete.subscribe(function() {
			this.canClick = true;
		});
		
		$E.on(frame, "mouseenter", function(e) {
			if (frame.later != undefined && frame.later != null) {
				frame.later.cancel();
				frame.later = null;
			}
	
			frame.later = YAHOO.lang.later(FD_QuickBuy.DELAY, frame, function() {
				frame.later = null;
	
				this.canClick = true;
				this.animFadeIn.animate();
			}, null, false );
		});

		$E.on(frame, "mouseleave", function(e) {
			if (frame.later != undefined && frame.later != null) {
				frame.later.cancel();
				frame.later = null;
			}
	
			this.animFadeOut.animate();
			this.canClick = false;
		});
	}


	// click event on button		
	$E.on(btn, "click", function(e){
		if (frame.canClick == true) {
			frame.showPopup();
			
			return false;
		}
		
		return true;
	});
	
	// click event on product image
	$E.on(prodImgBtn, "click", function(e){
		$E.preventDefault(e);

		frame.showPopup();
			
		return false;
	});
};
