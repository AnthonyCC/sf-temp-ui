(new YAHOO.util.YUILoader({
    require: ["event-delegate", "selector", "dom"],
    loadOptional: true,
    onSuccess: function() {
		YAHOO.util.Event.addListener(window,'load',function(){
			var YD=YAHOO.util.Dom,
				YE=YAHOO.util.Event;
			
			YE.delegate('filters','click',function(e){
				var target=e.target || e.srcElement,
					filterbox=YD.getAncestorByClassName(target,'filterbox');
				
				if(filterbox) {
					if(YD.hasClass(filterbox,'filterbox-showall')) {
						YD.removeClass(filterbox,'filterbox-showall');
					} else {
						YD.addClass(filterbox,'filterbox-showall');
					}
				}
				
			},'.filterbox .see');
		});
    },
    timeout: 10000
})).insert();
 




