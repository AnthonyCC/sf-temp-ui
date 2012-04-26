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
	
				activeElements = YD.getElementsByClassName('grid-item-disabled');
				l = activeElements.length;
				
				for(i=0;i<l;i++) {
					currentItem = activeElements[i];
					disabledStatusElements = YD.getElementsByClassName('grid-item-status','div',currentItem);
					if(disabledStatusElements.length) {
						disabledStatusElement = disabledStatusElements[0];
						disabledStatusElement.innerHTML="";
					}
				}
				
				
				gridItem=YD.getAncestorByClassName(statusNode,'grid-item');
				statusNode.innerHTML=msg;
	
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


