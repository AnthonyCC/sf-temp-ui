/*global expressco*/
var FreshDirect = FreshDirect || {};

(function (fd) {
    "use strict";

    var $ = fd.libs.$;
    var WIDGET = fd.modules.common.widget;
    var FORMS = fd.modules.common.forms;
    var DISPATCHER = fd.common.dispatcher;

    var atpFailure = Object.create(WIDGET, {
        signal: {
          value: 'atpFailure'
        },
        placeholder:{
          value:"#atpfailure"
        },
        template: {
          value: expressco.atpFailure
        },
        serialize: {
          value: function () {
            var removeIds = [];

            $(this.placeholder + " [data-component='atpremove']:checked").each(function(){
              removeIds.push($(this).attr("id"));
            });

            return removeIds;
          }
        }
    });
    atpFailure.listen();

    fd.modules.common.forms.register({
      id: "atpfail",
      submit: function (e) {
        e.preventDefault();
        e.stopPropagation();

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
      },
      failure:function(id, data){
        id && FORMS.get(id).releaseLockWhenNotRedirecting(id, data);
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
