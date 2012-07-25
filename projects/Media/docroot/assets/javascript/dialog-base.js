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
				closeText:''
			});

			if(width) {
				currentPanelNode.dialog( "option", "width", width );
			}

			if(height) {
				currentPanelNode.dialog( "option", "height", height );
			}

			if(cssClass) {
				currentPanelNode.addClass(cssClass);
			}

			currentPanelNode.html(html);				
			jq('.ui-dialog-titlebar-close span',currentPanelNode.dialog('widget')).html('&#215;');
			currentPanelNode.bind('dialogclose',closeHandler);
			currentPanelNode.dialog("open");
		};

		var openUrl = function(url,title,width,height,cssClass) {
			return open({
				html:'<iframe src="'+url+'" border="0" frameborder="0" style="border:none;width:100%;height:100%;"></iframe>',
				width:width,
				height:height,
				cssClass:cssClass,
				title:title
			});
		}

		var openVideo = function(title,titleText,width,height) {
			var w = width || 450,
				h = height || 350;
			
			return openUrl('/common/template/includes/youtube_video_player.jsp?title='+title+'&ispopup=1',titleText,w,h,'fd-dialog-video');
		};
		
		return {
			open:open,
			openUrl:openUrl,
			openVideo:openVideo,
			getCurrentNode:getCurrentNode
		};
	}(jQuery);
}
