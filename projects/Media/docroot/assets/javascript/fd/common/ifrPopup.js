/*global jQuery,common*/
var FreshDirect = FreshDirect || {};

(function (fd) {

  var $ = fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;
  var maxwidth, maxheight;

  var reposition = function () {
    var $ifr = $('#ifrPopup iframe'),
        $content = $('#ifrPopup .qs-popup-content');

    try {
      var $body, bodyHeight;

      $ifr.css({
        height: 'auto',
        width: 'auto'
      });
      
      $body = $($ifr[0].contentWindow.document.body);
      bodyHeight = $body[0].scrollHeight;

      $body.css({
        display:'inline-block',
        margin:'0 auto'
      });
      $ifr.css({
        width: Math.min(maxwidth || 1000, ($body.innerWidth() || $body.parent().innerWidth() || maxwidth-25) + 25),
        height: Math.max(maxheight || 0, bodyHeight)
      });
    } catch(e) {
      
    }
    ifrPopup.noscroll();
  };

  var ifrPopup = Object.create(POPUPWIDGET,{
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
        zIndex: 10001,
        overlay:true
      }
    },
    reposition: {
      value: reposition
    },
    scrollCheck: {
        value:['.fixedPopupContent','.qs-popup-content']
    },
    open: {
      value: function (config) {
        var fpc = $('#ifrPopup .fixedPopupContent');  

        ifrPopup.popup.$overlay.css('opacity', config.opacity || 0);

        fpc.css('border-radius', "");
        if (config.borderRadius) {
          fpc.css('border-radius', config.borderRadius);
        }

        if(config.url) {
            if(config.url[0]!=='/' && config.url.substr(0,4)!=='http') {
              config.url="/"+config.url;
            }
            ifrPopup.refreshBody(config);

            if (!ifrPopup.popup.hasOwnProperty('reposition')) {
              ifrPopup.popup.reposition = reposition;
            }
            ifrPopup.popup.clicked=true;
            ifrPopup.popup.show($('body'),false);
            ifrPopup.noscroll();
        }
        var $ifr = $('#ifrPopup iframe');

        if (config.width) {
          $ifr.css({
            width: config.width
          });
          maxwidth = config.width;
        } else {
          maxwidth = null;
        }
        if (config.height) {
          $ifr.css({
            height: config.height
          });
          maxheight = config.height;
        } else {
          maxheight = null;
        }

        try {
            $ifr[0].contentWindow.close = function(){
              ifrPopup.popup.hide();
            };          
        } catch(e) {
          
        }
        
        try {
            $ifr[0].contentWindow.resizeTo=function(width,height){
              height = height || 400;
              width = width || 400;

              maxwidth = width;
              maxheight = height;

              $ifr.css({
                height: height,
                width: width
              });

              reposition();
            };
        } catch(e) {
          
        }
        
      }
    }
  });

  ifrPopup.render();
  
  fd.modules.common.utils.register("components", "ifrPopup", ifrPopup, fd);
}(FreshDirect));
