(new YAHOO.util.YUILoader({
    require: ["dom"],
    loadOptional: true,
    onSuccess: function() {
		window.FDSearch=(function(){
			var YD=YAHOO.util.Dom;
			
			var statusUpdater=function(statusNode, msg){
				var gridItem,
					activeElements,
					i,l,currentItem,
					disabledStatusElements,
					disabledStatusElement;
				
				if(!statusNode) return;

				statusNode.innerHTML=msg;

				YD.addClass(statusNode,'grid-item-status-visible');
				setTimeout(function(){
					YD.removeClass(statusNode,'grid-item-status-visible');
				},3000);
				
				gridItem=YD.getAncestorByClassName(statusNode,'grid-item');	
				if(gridItem) {
					YD.addClass(gridItem,'grid-item-disabled');
				}
				
			};
			
			return {
				statusUpdater:statusUpdater
			};
		})();
	},
    timeout: 10000
})).insert();


