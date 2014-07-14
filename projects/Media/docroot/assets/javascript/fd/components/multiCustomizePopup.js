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
          this.changeStep("2");
        }
    },
    getStep : {
         value : function(){
           return this.popup.$el.find("[data-current-step]").attr("data-current-step");
         }
    },
    changeStep : {
        value : function(step){
          this.popup.$el.toggleClass("qs-hasclose", +step !== 2);

          if (+step === 3) {
            // issue empty atc call
            this.emptyAtc();
          }
          this.popup.$el.find("[data-current-step]").attr("data-current-step", step);
        }
    },
    emptyAtc: {
      value: function () {
        var $els = $('#'+this.popupId+' [data-name="_simple_"] [data-component="product"]'),
            items = fd.modules.common.productSerialize($els);

        $('#'+this.popupId+' [data-name~="_simple_"] [data-component="product"]').remove();

        if(!items.length){
          POPUPWIDGET.close.call(multiCustomizePopup);
          fd.common.dispatcher.signal('server', {
            url: '/api/addtocart',
            data: {
              data: JSON.stringify({
                eventSource: 'FinalizingExternal',
                items: null
              })
            },
            method: 'POST'
          });
        } else {
          fd.components.AddToCart.addToCart($els, {eventSource: 'FinalizingExternal'});
        }

        $('#'+this.popupId+' [data-component="product"]').remove();

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

        var data = Object.keys(pendingCustomizations).map(function (k, gi) {
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

              // quantity
              product.quantity.quantity = atcinfo.quantity;

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

        if (Object.keys(pendingCustomizations).length === 1 && pendingCustomizations._simple_) {
          data.state = 3;
        }

        // making sure that __name__ is the first item
        data.sort(function (a, b) {
            if (a.externalGroup < b.externalGroup)
              return 1;
            if (a.externalGroup > b.externalGroup)
              return -1;
            // a must be equal to b
            return 0;
        });

        return data;
      }
    },
    showSubtotalEstimationByDefault : {
      value : function(){
        this.popup.$el.find("input").change();
      }
    },
    validate : {
      value : function(){
          var self = this,
              isValid = true,
              $pop = self.popup.$el;

          $.each(this.popup.$el.find("[data-atc-required='true']"), function(e){
              if(!$(this).val()){
                $(this).addClass("missing-data");
                isValid = false;
              }
              else{
                $(this).removeClass("missing-data");
              }
          });

          $pop.toggleClass("valid", isValid);
          $pop.toggleClass("invalid", !isValid);
      }
    },
    open: {
      value: function (pendingCustomizations) {
        var data = this.processPendingCustomizations(pendingCustomizations);
        multiCustomizePopup.refreshBody({itemGroups: data, state: data.state});
        if (this.popup) {
          multiCustomizePopup.popup.show($('body'), false);
          multiCustomizePopup.noscroll();
        }

        if (data.state) {
          this.changeStep(data.state);
        }

        this.showSubtotalEstimationByDefault();
        this.validate();
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
          // hide errors from "_simple_" items
          if (success || $('#'+multiCustomizePopup.popupId).find('[data-component="product"]').size() === $('#'+multiCustomizePopup.popupId).find('[data-name="_simple_"] [data-component="product"]').size()) {
            if (atcResultList.length) {
              // all item got into cart, cart changed, reloading
              multiCustomizePopup.popup.$el.find("[data-current-step]").attr("data-current-step", 3);
              setTimeout(function () {
                window.location.reload();
              }, (fd.tests && fd.tests.waitBeforeReload) || 100);

            } else {
              // no cart change (or modifybrd popup), close popup and don't reload
              POPUPWIDGET.close.call(multiCustomizePopup);
            }
          }
        }
      }
    }
  });

  if (FreshDirect.pendingCustomizations && Object.keys(FreshDirect.pendingCustomizations).length) {
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
      var shoplist = $(this).closest(".shopfromlists");

      $(this).closest('[data-component="product"]').remove();

      if(!shoplist.children().length){
          shoplist.remove();
      }

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

    $('#'+multiCustomizePopup.popupId).on('click', '[data-atc-required="true"]', function (e) {
        multiCustomizePopup.validate();
    });

    $('#'+multiCustomizePopup.popupId).on('click', '.deletefromrecipe', function (e) {
        multiCustomizePopup.validate();
    });

  }

  fd.modules.common.utils.register("components", "multiCustomizePopup", multiCustomizePopup, fd);
}(FreshDirect));
