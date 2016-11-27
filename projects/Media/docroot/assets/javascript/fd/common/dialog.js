var FreshDirect = FreshDirect || {};

(function (fd, $) {
/*
 * dialog module
 * 
 * usage:
 * a) listens to clicks on a.dialog elements and opens a modal dialog iframe with the url from the href attribute
 * b) use the open, openUrl functions to open a dialog
 * 
 * 
 */
		var seq = 0,
			currentPanelNode;


		var getCurrentNode=function(){
			return $('#fd-dialog-'+seq);
		};
		
		var closeHandler = function(event,ui) {
			var t = event.currentTarget;
			$(t).dialog("destroy").remove();
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
			
			$('body').append('<div id="fd-dialog-'+seq+'" class="fd-dialog" style="display:none;"></div>');

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
			$('.ui-dialog-titlebar-close span',currentPanelNode.dialog('widget')).html('&#215;');
			currentPanelNode.bind('dialogclose',closeHandler);
			currentPanelNode.dialog("open");
			return currentPanelNode;
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

		var openVideo = function(title,page,titleText,width,height) {
			var w = width || 450,
				h = height || 350,
				page = page || "youtube_video_player.jsp";
			return openUrl('/common/template/includes/'+page+'?title='+title+'&ispopup=1',titleText,w,h,'fd-dialog-video');
		};
		
		$(document).on('click','a.dialog',function(e){
			var $this = $(this);
				href = this.href;
			e.preventDefault();
			if(href) {
				return openUrl(href,
								$this.data('dialog-title') || '',
								$this.data('dialog-width') || 770,
								$this.data('dialog-height') || 400,
								$this.data('dialog-css') || '');
			} else {
				return false;
			}
		});
		
		
		var dialog = {
			open:open,
			openUrl:openUrl,
			openVideo:openVideo,
			getCurrentNode:getCurrentNode
		};
	
	fd.modules.common.utils.register("modules.common", "dialog", dialog, fd);

  // partial Modalbox support
  if (!window.Modalbox) {
    window.Modalbox = {
      show: function (content, options){
        var width = (options && options.width) || 400,
            height = (options && options.height) || 400;

        dialog.open({
          width: width,
          height: height,
          html: content
        });
      },
      hide: function () {
    	  $('.fd-dialog').dialog("destroy").remove();
      }
    };
  }
}(FreshDirect, jQuery));

