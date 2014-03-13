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
      var maxHeight = $(document.body).height() * 0.95 - 50,
          $body = $($ifr[0].contentWindow.document.body);

      $body.css({
        display:'inline-block',
        margin:'0 auto'
      });
      $ifr.css({
        width: Math.min(maxwidth || 2000, ($body.innerWidth() || $body.parent().innerWidth() || maxwidth-25) + 25),
        height: Math.min(maxheight || 2000, maxHeight)
      });
    } catch(e) {
      
    }
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
    open: {
      value: function (config) {

        ifrPopup.popup.$overlay.css('opacity', config.opacity || 0);

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

        }
        var $ifr = $('#ifrPopup iframe');

        if (config.width) {
          $ifr.css({
            width: config.width
          });
        }
        if (config.height) {
          $ifr.css({
            width: config.height
          });
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
