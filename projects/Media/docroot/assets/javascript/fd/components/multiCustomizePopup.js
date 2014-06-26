/*global jQuery,common*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;

  var multiCustomizePopup = Object.create(POPUPWIDGET,{
    template:{
      value:common.fixedPopup
    },
    bodySelector:{
      value:'.qs-popup-content'
    },
    bodyTemplate: {
      value: common.multiCustomizePopup
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
    close: {
        value: function (e) {

          if(this.getStep() !== "1"){
            this.changeStep("3");
          }
          else{
            this.changeStep("2");
          }
        }
    },
    getStep : {
         value : function(){
           return this.popup.$el.find("[data-current-step]").attr("data-current-step");
         }
    },
    changeStep : {
        value : function(step){
          this.popup.$el.find("[data-current-step]").attr("data-current-step", step);
          if (+step === 3) {
            // issue empty atc call
            this.emptyAtc();
          }
        }
    },
    emptyAtc: {
      value: function () {
        var items = fd.modules.common.productSerialize($('#'+this.popupId+' [data-name="_simple_"] [data-component="product"]'));

        $('#'+this.popupId+' [data-component="product"]').remove();

        fd.common.dispatcher.signal('server', {
          url: '/api/addtocart',
          data: {
            data: JSON.stringify({
              eventSource: 'FinalizingExternal',
              items: items
            })
          },
          method: 'POST'
        });
      }
    },
    popupId: {
      value: 'multi-customize-popup'
    },
    popupConfig: {
      value: {
        valign: 'bottom',
        halign: 'left',
        placeholder: false,
        stayOnClick: false,
        noCloseOnOverlay: true,
        overlay:true,
        overlayExtraClass: 'atlpopupoverlay'
      }
    },
    processPendingCustomizations: {
      value: function (pendingCustomizations) {

        return Object.keys(pendingCustomizations).map(function (k, gi) {
          return {
            externalGroup: k,
            groupData: pendingCustomizations[k].filter(function (data) {
              return data.productData.available;
            }).map(function (data, i) {
              var atcinfo = data.addToCartItem,
                  product = data.productData;

              // atcItemId
              product.itemId = 'mulcust_'+atcinfo.atcItemId+'_'+gi+'_'+i;

              // external group, agency and source
              product.externalGroup = atcinfo.externalGroup;
              product.externalAgency = atcinfo.externalAgency;
              product.externalSource = atcinfo.externalSource;

              // salesunit
              if (atcinfo.salesUnit && product.salesUnit && product.salesUnit.length) {
                product.salesUnit = product.salesUnit.map(function (su) {
                  su.selected = su.id === atcinfo.salesUnit;

                  return su;
                });
              }

              // configuration
              if (atcinfo.configuration && Object.keys(atcinfo.configuration).length) {
                product.configuration = $.extend({}, product.configuration, atcinfo.configuration);

                if (product.variations && product.variations.length) {
                  product.variations = product.variations.map(function (variation) {
                    if (Object.keys(atcinfo.configuration).indexOf(variation.name) > -1) {
                      variation.values = variation.values.map(function (val) {
                        if (val.name === atcinfo.configuration[variation.name]) {
                          val.selected = true;
                        } else {
                          val.selected = false;
                        }

                        return val;
                      });
                    }
                    return variation;
                  });
                }
              }

              return {
                addToCartItem: atcinfo,
                productData: product
              };
            })
          };
        });

      }
    },
    open: {
      value: function (pendingCustomizations) {
        multiCustomizePopup.refreshBody({itemGroups: this.processPendingCustomizations(pendingCustomizations)});
        if (this.popup) {
          multiCustomizePopup.popup.show($('body'), false);
          multiCustomizePopup.noscroll();
        }
      }
    }
  });

  var atcResultParser = Object.create(fd.common.signalTarget,{
    signal:{
      value:'atcResult'
    },
    callback:{
      value: function(atcResultList) {
        var success;

        if (multiCustomizePopup.popup && multiCustomizePopup.popup.shown) {
          success = atcResultList.every(function (ar) { return ar.status === "SUCCESS"; });
          // reload page if all succeeded
          if (success) {
            POPUPWIDGET.close.call(multiCustomizePopup);
            window.location.reload();
          }
        }
      }
    }
  });

  multiCustomizePopup.render();
  atcResultParser.listen();

  $(window).on("resize", function (e) {
    multiCustomizePopup.noscroll();
  });

  $(document).ready(function(){  
    if (FreshDirect.pendingCustomizations && Object.keys(FreshDirect.pendingCustomizations).length) {
      multiCustomizePopup.open(FreshDirect.pendingCustomizations);
    }
  });

  $('#'+multiCustomizePopup.popupId).on('click', 'button.deletefromrecipe', function (e) {
    $(this).closest('[data-component="product"]').remove();

    if ($('#'+multiCustomizePopup.popupId).find('[data-component="product"]').size() === $('#'+multiCustomizePopup.popupId).find('[data-name="_simple_"] [data-component="product"]').size()) {
      multiCustomizePopup.changeStep("3");
    }
  });

  $('#'+multiCustomizePopup.popupId).on('click', '[data-component="cancel"]', function (e) {
      multiCustomizePopup.changeStep("1");
  });

  $('#'+multiCustomizePopup.popupId).on('click', '[data-component="delete-and-close"]', function (e) {
      multiCustomizePopup.changeStep("3");
  });

  fd.modules.common.utils.register("components", "multiCustomizePopup", multiCustomizePopup, fd);
}(FreshDirect));
