if(!window.parent['FDModalDialog']) {
	window.FDModalDialog = function(jq){

		var seq = 0,
			currentPanelNode;


		var getCurrentNode=function(){
			return jq('#fd-dialog-'+seq);
		};
		
		var closeHandler = function(event,ui) {
			var t = event.currentTarget;
			jq(t).dialog("destroy").remove();
			seq--;
		};
		
		var open = function(config){
			var html=config.html,
				cssClass=config.cssClass,
				width = config.width,
				height = config.height,
				title = config.title || '',
				dialogNode,
				currentPanelNode;


			seq++;
			
			jq('body').append('<div id="fd-dialog-'+seq+'" class="fd-dialog" style="display:none;"></div>');

			currentPanelNode=getCurrentNode();
			
			currentPanelNode.dialog({
				autoOpen:false,
				modal:true,
				draggable:false,
				resizable:false,
				stack:true,
				closeText:'',
				title:title,
				dialogClass:cssClass || '',
				zIndex:10001
			});

			if(width) {
				currentPanelNode.dialog( "option", "width", width );
			}

			if(height) {
				currentPanelNode.dialog( "option", "height", height );
			}
/*
			if(cssClass) {
				currentPanelNode.addClass(cssClass);
			}
*/
			currentPanelNode.html(html);				
			jq('.ui-dialog-titlebar-close span',currentPanelNode.dialog('widget')).html('&#215;');
			currentPanelNode.bind('dialogclose',closeHandler);
			currentPanelNode.dialog("open");
			return currentPanelNode;
		};

		var openUrl = function(url,title,width,height,cssClass) {
			return open({
				html:'<iframe src="'+url+'" border="0" frameborder="0" style="border:none;width:100%;height:100%;"></iframe>',
				width:width || 450,
				height:height || 350,
				cssClass:cssClass || '',
				title:title || ''
			});
		}
		
		var closeDialog = function(selector) {
			var nCssClass = selector || '#fd-dialog-'+seq;
			jq(nCssClass).dialog('close');
		}

		var resizeY = function(newHeight,selector) {
			var nCssClass = selector+' .fd-dialog';
			jq(nCssClass).css('height',parseInt(newHeight,10)+'px');
		}
		
		return {
			open:open,
			openUrl:openUrl,
			getCurrentNode:getCurrentNode,
			close:closeDialog,
			resizeY:resizeY
		};
	}(jQuery);
}
