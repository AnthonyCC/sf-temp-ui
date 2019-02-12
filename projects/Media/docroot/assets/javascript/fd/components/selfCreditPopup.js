/*global jQuery, common*/

var FreshDirect = window.FreshDirect || {};

(function(fd) {
  "use strict";
  var $ = fd.libs.$;
  var DISPATCHER = fd.common.dispatcher;

  var API_URL = "/api/selfcredit/orders";

  var selfCreditInitPopup = Object.create(fd.modules.common.overlayWidget, {
    bodyTemplate: {
      value: function(orders) {
        return common.selfCreditInit({ user: fd.user, orders: orders.data });
      }
    },
    headerTemplate: {
      value: function(data) {
        return "";
      }
    },
    signal: {
      value: "orderhistory"
    },
    trigger: {
      value: "[data-selfcreditinit-popup]"
    },
    closeTrigger: {
      value: "[data-close-selfcreditinit]"
    },
    overlayId: {
      value: "selfCreditInit"
    },
    ariaDescribedby: {
      value: "selfcredit-describe"
    },
    ariaLabelledby: {
      value: "selfcredit-label"
    },
    overlayConfig: {
      value: {
        zIndex: 460
      }
    },
    customClass: {
      value: "overlay-small selfcredit-overlay"
    },
    openPopup: {
      value: function() {
        if(fd.user && fd.user.userId.length && !fd.user.recognized) {
          this.refresh();
          this.overlayEl = $("#" + this.overlayId);
          // set close callback
          this.overlayEl.attr(
            "data-close-cb",
            "FreshDirect.components.selfCreditInitPopup.closeCB"
          );
  
          $(".spinner-overlay").addClass("active");
          DISPATCHER.signal("server", {
            url: API_URL,
            method: "GET"
          });
          fd.gtm.updateDataLayer({
            selfCreditRequestACredit: null
          });
        };
      }
    },
    callback: {
      value: function(result) {
        this.orders = $.map(result.orders, function(order) {
          var startDate = fd.utils.dateFormatter(order.deliveryStart);
          var endDate = fd.utils.dateFormatter(order.deliveryEnd);
          order.dayName = startDate.shortDayName();
          order.timeSlot = startDate.shortTime() + "-" + endDate.shortTime();
          order.date = startDate.shortDate();
          order.longDate = order.date + "/" + startDate.year();
          order.orderId = order.saleId;
          return order;
        });

        var user = fd.user;
        user.firstName = result.userFirstName;
        user.lastName = result.userLastName;

        this.render(this.orders);

        var selectEl = $("#self-credit-order-select");
        selectEl.change(
          function() {
            setTimeout(function () {
              if (selectEl.val() !== "") {
                var tabindex = this.overlayEl.find('.overlay-close-icon').first().attr('tabindex') || 1000;
                this.overlayEl
                  .find('button[value="Continue"]')
                  .prop("disabled", false)
                  .prop("tabindex", +tabindex - 1);
              }
            }.bind(this), 100);
          }.bind(this)
        );
      }
    },
    closeCB: {
      value: function() {
        DISPATCHER.signal("selfCreditInitClosed", {});
      }
    },
    submit: {
      value: function() {
        fd.gtm.updateDataLayer({
          selfCreditContinueToReviewCredit: null
        });
        var orderId = $("#self-credit-order-select").val();
        if (!orderId) return;
        var order = $.grep(this.orders, function(order) {
          return parseInt(order.orderId) === parseInt(orderId);
        })[0];
        
        this.close({ currentTarget: this.overlayEl });

        fd.components.selfCreditIssueReportPopup.openPopup(order);
      }
    }
  });
  selfCreditInitPopup.listen();

  $(document).on('click', '[data-component="self-credit-open-button"]', selfCreditInitPopup.openPopup.bind(selfCreditInitPopup));
  fd.modules.common.utils.register(
    "components",
    "selfCreditInitPopup",
    selfCreditInitPopup,
    fd
  );
})(FreshDirect);
