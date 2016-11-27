/*global common*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;
  var maxwidth, maxheight;

  var reposition = function (resetHW) {
    // check parent
    if (window.top !== window) {
      window.top.FreshDirect.components.ifrPopup.reposition(resetHW);
      return;
    }

    if (resetHW) {
      maxheight = resetHW.height || 0;
      maxwidth = resetHW.width || 0;
    }

    var $ifr = $('#ifrPopup iframe'),
        $container = $('#ifrPopup .fixedPopupContent'),
        windowHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);

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

      if (bodyHeight >= windowHeight * .95) {
        $container.height('95%');
      } else {
        $container.height(null);
      }

    } catch(e) {

    }
    //ifrPopup.noscroll();
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
            //ifrPopup.noscroll();
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

        //re-size overlay based on contents loaded
        $($ifr[0].contentWindow).load(function() {
        	var $ifr = $('#ifrPopup iframe'), $body = $($ifr[0].contentWindow.document.body), height, width;

            try {
            	var configH = config.height||400, configW = config.width||400;
            	height = configH+$('.qs-popup-close-icon').height();
            	width = Math.min(1000, Math.max(configW, $body.innerWidth()+$('.qs-popup-close-icon').width()));
            	$ifr[0].contentWindow.resizeTo(width, height);
            } catch(e) {
            	
            }
        });
        
      }
    }
  });

  ifrPopup.render();
  
  $(document).on('click', '[data-ifrpopup]', function (e) {
    var $el = $(e.currentTarget),
        url = $el.attr('data-ifrpopup'),
        w = $el.attr('data-ifrpopup-width'),
        h = $el.attr('data-ifrpopup-height');

    ifrPopup.open({
      url: url,
      width: w,
      height: h
    });

    e.preventDefault();
  });

  // stop event propagation on fixed popup overlays
  setTimeout(function () {
    $('.fixedpopup').on('click', function (e) {
      var $t = $(e.target);

      if (!$t.is('button,a,[data-component]')) {
        e.stopPropagation();
      }
    });
  }, 10);

  fd.modules.common.utils.register("components", "ifrPopup", ifrPopup, fd);
}(FreshDirect));
