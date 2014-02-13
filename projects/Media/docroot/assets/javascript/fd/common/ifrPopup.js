/*global jQuery,quickshop*/
var FreshDirect = FreshDirect || {};

(function (fd) {

  var $ = fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;

  var ifrPopup = Object.create(POPUPWIDGET,{
    customClass: {
    },
    template:{
      value:common.fixedPopup
    },
    bodySelector:{
    	value:'.qs-popup-content'
    },    
    bodyTemplate: {
      value: common.ifrPopup
    },
    $trigger: {
      value: null
    },
    hasClose: {
        value: true
    },
    popupId: {
      value: 'ifrPopup'
    },
    popupConfig: {
      value: {
	    valign: 'bottom',
	    halign: 'left',
	    placeholder: false,
	    stayOnClick: false,
        overlay:true
      }
    },
    open: {
      value: function (config) {
    	  if(config.url) {
        	  if(config.url[0]!=='/' && config.url.substr(0,4)!=='http') {
        		  config.url="/"+config.url;
        	  }
        	  ifrPopup.refreshBody(config);
        	  ifrPopup.popup.clicked=true;
        	  ifrPopup.popup.show($('body'),false);

    	  }
    	  var $ifr = $('#ifrPopup iframe');
    	  try {
        	  $ifr[0].contentWindow.close = function(){
        		  ifrPopup.popup.hide();
        	  };    		  
    	  } catch(e) {
    		  
    	  }
    	  
    	  try {
        	  $ifr[0].contentWindow.resizeTo=function(width,height){
        		  $ifr.css({
        			  width:width+'px',
        			  height:height+'px'
        		  });
        	  };
        	  
    		  
    	  } catch(e) {
    		  
    	  }
    	  
    	  try {
        	  $($ifr[0].contentWindow).on('load',function(){
        		  var 	body = this.document.body,
        		  		$body = $(body); 
        		  $body.css({
        			  display:'inline-block',
        			  margin:'0 auto'
        		  });
        		  var crect = body.getBoundingClientRect();
        		  $body.css({
        			  	display:'block',
            			width:crect.width+'px',
            			height:crect.height+'px'
        		  });
        		  $ifr.css({
        			  width:(crect.width+20)+'px',
        			  height:crect.height+'px'
        		  });
        	  });
    		  
    	  } catch(e) {
    		  
    	  }
      }
    }
  });

  ifrPopup.render();
  

  
  fd.modules.common.utils.register("components", "ifrPopup", ifrPopup, fd);
}(FreshDirect));
