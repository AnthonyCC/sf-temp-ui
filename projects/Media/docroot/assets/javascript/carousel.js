var fd_carousel=function(id,numItems) {
	if(YAHOO.widget.Carousel===undefined) { YAHOO.util.Get.css('/assets/yui/carousel/assets/carousel-core.css');YAHOO.util.Get.script('/assets/yui/carousel/carousel-min.js',{
		onSuccess:function() {
			fd_carousel.start(id,numItems);
		}
	})} else {
		fd_carousel.start(id,numItems);
	}
};

fd_carousel.start=function(container,numItems) {
	YAHOO.util.Event.onContentReady(container,function() {
		var carousel, lineItems, i,l,maxHeight=0, region;
		lineItems = YAHOO.util.Dom.getElementsBy(function(){return true;},'li',container);
		l=lineItems.length;
		for(i=0;i<l;i++) {
			region=YAHOO.util.Dom.getRegion(lineItems[i]);
			if(maxHeight<region.height) {
				maxHeight=region.height;
			}
		}
		for(i=0;i<l;i++) {
			YAHOO.util.Dom.setStyle(lineItems[i],'height',(maxHeight+5)+'px');
		}
		
		carousel = new YAHOO.widget.Carousel(container,{numVisible:numItems,animation:{speed: 0.5}});
		carousel.CONFIG.MAX_PAGER_BUTTONS=8;
		carousel.CONFIG.HORZ_MIN_WIDTH=100;
		carousel.render();
		carousel.show();
	});
};