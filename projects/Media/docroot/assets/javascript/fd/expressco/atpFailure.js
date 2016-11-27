/*global expressco*/
var FreshDirect = FreshDirect || {};

(function (fd) {
    "use strict";

    var $ = fd.libs.$;
    var WIDGET = fd.modules.common.widget;
    var FORMS = fd.modules.common.forms;
    var DISPATCHER = fd.common.dispatcher;
    var POPUP = fd.modules.common.popupWidget;

    var atpFailure = Object.create(POPUP, {
        signal: {
          value: 'atpFailure'
        },
        template: {
          value: common.fixedPopup
        },
        bodyTemplate: {
          value: expressco.atpFailure
        },
        popupId: {
          value: 'atpfailure'
        },
        popupConfig:{
          value:{
            overlay:true,
            overlayExtraClass:'atpFailureOverlay',
            zIndex: 490,
            align: false
          }
        },
        hasClose: {
          value: true
        },
        close: {
            value: function (e) {
              fd.modules.common.forms.get("atpfail").submit();
            }
        },
        hide: {
            value: function (e) {
              if (this.popup) {
                this.popup.hide(e);
              }
            }
        },
        scrollCheck: {
          value: '.fixedPopupContent'
        },
        serialize: {
          value: function () {
            var removeIds = [];

            $("#" + this.popupId + " [data-component='atpremove']:checked").each(function(){
              removeIds.push($(this).attr("id"));
            });

            return removeIds;
          }
        },
        render: {
          value: function(data){
            POPUP.render.call(atpFailure, data);
            $(document).ready(function(){
              atpFailure.open();
            });
          }
        },
        open: {
          value: function (e) {
            var $t = e && $(e.currentTarget) || $(document.body);

            this.popup.show($t);
            this.popup.clicked = true;
            this.noscroll(true);

            fd.components.carousel && fd.components.carousel.initialize();
          }
        }
    });
    atpFailure.listen();

    fd.modules.common.forms.register({
      id: "atpfail",
      submit: function (e) {
        if(e){
          e.preventDefault();
          e.stopPropagation();
        }

        var formdata = {
            action: 'atpAdjust',
            removableStockUnavailabilityCartLineIds: atpFailure.serialize()
        };

        DISPATCHER.signal("server", {
          url: "/api/expresscheckout/atpfailure",
          method: "POST",
          data: {
            data: JSON.stringify({
              fdform: "atpfail",
              formdata: formdata
            })
          }
        });
      },
      success:function(id, data){
        id && FORMS.get(id).releaseLockWhenNotRedirecting(id, data);
        atpFailure.hide.call(atpFailure);
      },
      failure:function(id, data){
        id && FORMS.get(id).releaseLockWhenNotRedirecting(id, data);
        atpFailure.hide.call(atpFailure);
      },
      releaseLockWhenNotRedirecting:function(id, data){
        var hasRedirectUrl = data && data.redirectUrl && data.redirectUrl.length;
        if(!hasRedirectUrl){
          FORMS.releaseLockFormResubmit(FORMS.getEl(id));
        }
      }
    });

    fd.modules.common.utils.register("expressco", "atpFailure", atpFailure, fd);
}(FreshDirect));
