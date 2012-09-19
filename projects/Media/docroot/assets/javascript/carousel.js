var fd_carousel=function(id,numItems,hideContainer,text,cName,parentId,offset,eventHandlers) {
	fd_carousel.start(id,numItems,hideContainer,text,cName,parentId,offset,eventHandlers);
};

fd_carousel._start=function(container, numItems,hideContainer,text,cName,parentId,offset,eventHandlers) {
		var carousel, lineItems, i,l,maxHeight=0, region, reg2;
		var pContainer, mh;
		lineItems = YAHOO.util.Dom.getElementsBy(function(){return true;},'li',container);
		l=lineItems.length;
		for(i=0;i<l;i++) {
			region=YAHOO.util.Dom.getRegion(lineItems[i]);
			if(maxHeight<region.height) {
				maxHeight=region.height;
			}
		}

		// Fix IE7 issue
		if (maxHeight < 30) {
			// retry
			window.setTimeout(function() {fd_carousel._start(container,numItems,hideContainer,text,cName,parentId,offset);}, 1000);

			return;
		}
		
		
		fd_carousel.fixItemHeights(container, maxHeight);
		if (parentId) {
			pContainer = YAHOO.util.Dom.get(parentId);
			reg2 = YAHOO.util.Dom.getRegion(pContainer);
			mh = maxHeight + offset;
			if (reg2.height === undefined || reg2.height < mh)
				YAHOO.util.Dom.setStyle(pContainer, 'height', mh+'px');
		}


		carousel = new YAHOO.widget.Carousel(container,{numVisible:numItems,animation:{speed: 0.5}});
		carousel.CONFIG.MAX_PAGER_BUTTONS=9;
		carousel.CONFIG.HORZ_MIN_WIDTH=100;
		carousel.render();
		carousel.show();
		if (text) {
			var header = document.createElement("div");
			var className = "fd-carousel-bottom-header";
			if (cName)
				className += " " + cName;
			header.className = className;
			header.innerHTML = text;
			document.getElementById(container).appendChild(header);
		}
		if (hideContainer && hideContainer.charAt(0) != '@') {
			var parent = document.getElementById(hideContainer);
			if (parent) {
				parent.style.display = 'none';
			}
		}
		
		if (eventHandlers != null){
			for (var eventKey in eventHandlers) {
				if(eventHandlers.hasOwnProperty(eventKey)) {
					carousel.on(eventKey, eventHandlers[eventKey] ); 				
				}
			}
		}
};

fd_carousel.start=function(container, numItems, hideContainer, text, cName, parentId, offset, eventHandlers ) {
	YAHOO.util.Event.onContentReady(container, function() {fd_carousel._start(container, numItems, hideContainer, text, cName, parentId, offset, eventHandlers);});
};

fd_carousel.fixItemHeights = function(container, maxHeight) {
	var lineItems, i, l;
	lineItems = YAHOO.util.Dom.getElementsBy(function(){return true;},'li',container);
	l=lineItems.length;

	for(i=0;i<l;i++) {
		YAHOO.util.Dom.setStyle(lineItems[i],'height',(maxHeight+5)+'px');
	}
};
