/*global common*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;

  var ajaxPopup = Object.create(POPUPWIDGET,{
    template:{
      value:common.fixedPopup
    },
    bodySelector:{
      value:'.qs-popup-content'
    },
    bodyTemplate: {
      value: common.ajaxPopup
    },
    scrollCheck: {
        value:['.fixedPopupContent','.qs-popup-content']
    },
    $trigger: {
      value: null
    },
    hasClose: {
        value: true
    },
    popupId: {
      value: 'ajaxpopup'
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
    openEl: {
      value: function (el) {
        ajaxPopup.open({
          element: $(el)
        });
      }
    },
    open: {
      value: function (config) {
        var target = config.element,
            url = target.attr('href'),
            type = target.attr('data-ajaxpopup-type') || "",
            template = config.template ||
              target.attr('data-ajaxpopup-template') &&
              fd.modules.common.utils.discover(target.attr('data-ajaxpopup-template')),
            afterRenderCallback = fd.modules.common.utils.discover(target.attr('data-ajaxpopup-after-render-callback'));

        ajaxPopup.popup.$el.attr('data-ajaxpopup-type', type);
        ajaxPopup.popup.$el.attr('data-component', this.popupConfig.noCloseOnOverlay ? null : 'ajaxpopup-popup');

        $.get(url, function(data){
          if (template) {
            data = template(data);
          }
          ajaxPopup.refreshBody({body:data});

          if(afterRenderCallback){
            afterRenderCallback(ajaxPopup, target, data);
          } else {
            ajaxPopup.popup.show($('body'),false);
            ajaxPopup.noscroll();
          }
        });
      }
    },
    noScroll: {
      value: function(){
          POPUPWIDGET.noscroll.call(ajaxPopup);
      }
    }
  });

  ajaxPopup.render();

  $(window).on("resize", function () {
    ajaxPopup.noscroll();
  });

  $(document).on('click','[data-component="ajaxpopup"]',function(event){
    ajaxPopup.openEl(event.currentTarget);
    event.preventDefault();
  });

  fd.modules.common.utils.register("components", "ajaxPopup", ajaxPopup, fd);
}(FreshDirect));
