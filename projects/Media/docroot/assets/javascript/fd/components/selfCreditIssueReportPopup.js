/*global jQuery, common*/

var FreshDirect = window.FreshDirect || {};

(function(fd) {
  "use strict";
  var $ = fd.libs.$;
  
  var DISPATCHER = fd.common.dispatcher;
  
  var API_URL = "/api/selfcredit/orderdetails";
  
  var CUSTOM_CLASSES = {
    MEDIUM: "overlay-medium selfcredit-overlay selfcredit-review",
    FULL_SCREEN: "overlay-fullscreen selfcredit-overlay"
  };

  var selfCreditIssueReportPopup = Object.create(
    fd.modules.common.overlayWidget,
    {
      bodyTemplate: {
        value: function(input) {
          var data = input.data;
          return common.selfCreditIssueReport({
            readonly: data.readonly,
            order: data.order,
            orderlines: data.orderlines,
            sample: data.sample,
            free: data.free,
            comment: data.comment,
            reviewData: getReviewData(data),
            mealBundles: data.mealBundles,
            customerServiceContact: data.customerServiceContact
          });
        }
      },
      headerTemplate: {
        value: function(input) {
          var data = input.data || {};
          return data.readonly ? "" : "Credit Request";
        }
      },
      signal: {
        value: ["orderdetails", "selfcreditresponse"]
      },
      trigger: {
        value: "[data-selfcreditissuereport-popup]"
      },
      closeTrigger: {
        value: "[data-close-selfcreditissuereport]"
      },
      overlayId: {
        value: "selfCreditIssueReport"
      },
      ariaDescribedby: {
        value: "selfcreditform"
      },
      ariaLabelledby: {
        value: ""
      },
      overlayConfig: {
        value: {
          zIndex: 2001
        }
      },
      customClass: {
        get: function() {
          return this.data && this.data.readonly
            ? CUSTOM_CLASSES.MEDIUM
            : CUSTOM_CLASSES.FULL_SCREEN;
        }
      },
      updateCustomClass: {
        value: function() {
          this.overlayEl
            .removeClass(CUSTOM_CLASSES.MEDIUM)
            .removeClass(CUSTOM_CLASSES.FULL_SCREEN)
            .addClass(this.customClass);
            this.overlayEl && this.overlayEl[0] && this.overlayEl[0].scrollIntoView(true);
          }
      },
      extendedRefresh: {
        value: function(data) {
          this.refresh(data);
          this.updateCustomClass();
          fd.modules.common.Textarea.addMaxLengthCounters(this.overlayEl);
          fd.modules.common.Scroll.addHorizontalScrollFade(this.overlayEl);
          this.commentField = this.overlayEl.find("#self-credit-comment");
        }
      },
      openPopup: {
        value: function(order) {
          this.data = {};
          this.data.order = order;
          this.render(this.data);
          this.overlayEl = $("#" + this.overlayId);
          // set close callback
          this.overlayEl.attr(
            "data-close-cb",
            "FreshDirect.components.selfCreditIssueReportPopup.closeCB"
          );
          $(".spinner-overlay").addClass("active");
          DISPATCHER.signal("server", {
            url: API_URL + "?orderId=" + order.orderId,
            method: "GET"
          });
        }
      },
      callback: {
        value: function(result, signalSource) {
          if (signalSource === "orderdetails") {
            this.data.orderlines = result.orderLines;
            this.data.oderlines = transformCartonNumbersArray(this.data.orderlines);
            this.data.customerServiceContact = result.customerServiceContact;
            this.data.mealBundles = filterSpecialItems(this.data.orderlines, 'mealBundle');  
            this.extendedRefresh(this.data);
          } else if (signalSource === "selfcreditresponse") {
            this.close({ currentTarget: this.overlayEl });
            if(!!result.success) {
              fd.components.selfCreditRequestCompletePopup.openPopup();
            } else {
              fd.components.selfCreditRequestErrorPopup.openPopup();
            }
          }
        }
      },
      closeCB: {
        value: function() {
          DISPATCHER.signal("selfCreditIssueReportPopupClosed", {});
        }
      },
      commentMouseDown: {
        value: function() {
          if (document.activeElement === this.commentField[0]) {
            this.commentField.removeClass("comment-field-animate");
            setTimeout(
              function() {
                this.commentField.addClass("comment-field-animate");
              }.bind(this),
              100
            );
          }
        }
      },
      commentClick: {
        value: function(e) {
          var button = $(e.currentTarget.parentElement);
          button.addClass("hide");
          $(button[0].parentElement)
            .find(".self-credit-comment-container.hide")
            .removeClass("hide");
          this.commentField.focus();
          fd.gtm.updateDataLayer({
            selfCreditCommentClick: null
          });
        }
      },
      reviewRequest: {
        value: function(data) {
          if (validate(data)) {
            fd.gtm.updateDataLayer({
              selfCreditReviewRequest: null
            });
            this.complaints = getValidComplaintLines(data);
            this.data.complaints = $.map(this.complaints, function(
              complaint,
              key
            ) {
              var lineId = key;
              return {
                orderLineId: lineId,
                complaintId: complaint.reasonId,
                quantity: complaint.qty,
                cartonNumbers: complaint.cartonNumbers
              };
            });
            mapComplaintsToOrderLines(this.complaints, this.data.orderlines, $);
            setReadOnly.call(this, true);
          } else {
            var MESSAGE = "Select an issue and quantity of each affected item";
            $("#self-credit-form-alert").html(
              '<span class="form-instructions-aria-trigger">.</span>' + MESSAGE
            );
            setTimeout(function() {
              // workaround to trigger aria live
              $("#self-credit-form-alert").text(MESSAGE);
            }, 300);
            $(".self-credit-footer").addClass("form-instructions-active");
            $("#selfcreditform select").on('change', function() {
              var selectNodes = document.querySelectorAll("#selfcreditform select");
              var selectArray = Array.from(selectNodes).map(function(select) {
                return [select.name, select.value];
              })
              var complaintMap = selectArray.reduce(function(prev,curr){
                var tid = curr[0].split('-');
                var t = tid[0];
                var id = tid[1];
                prev[id] = prev[id] || {};
                prev[id][t] = curr[1];
                return prev;
              }, {});
              var complaintLineValues = Object.keys(complaintMap).map(function(key) {
                return complaintMap[key]
              });
              var invalidComplaintsArray = complaintLineValues.filter(function(complaint){
                return !complaint.reason !== !complaint.qty;
              });
              if(!invalidComplaintsArray.length) {
                $(".self-credit-footer").removeClass("form-instructions-active");
                removeChangeHandler('#selfcreditform select');
              }
            });
          }
        }
      },
      editRequest: {
        value: function() {
          this.data.comment = $("#self-credit-comment")[0].value;
          setReadOnly.call(this, false);
          fd.gtm.updateDataLayer({
            selfCreditEditClick: null
          });
        }
      },
      submitRequest: {
        value: function() {
          if (!this.data.comment) {
            this.data.comment = $("#self-credit-comment")[0].value;
          }
          $(".spinner-overlay").addClass("active");
          DISPATCHER.signal("server", {
            url: "/api/selfcredit/issuecredit",
            method: "POST",
            data: {
              data: JSON.stringify({
                orderId: this.data.order.orderId,
                orderLineParams: this.data.complaints,
                note: this.data.comment
              })
            }
          });
          fd.gtm.updateDataLayer({
            selfCreditSubmitRequest: null
          });
        }
      }
    }
  );

  function removeChangeHandler(node) {
      $(node).off('change');
  }

  function getReviewData(data) {
    if (!data || !data.orderlines) return {};
    var totalCredit = 0;
    var totalTax = 0;
    var totalPromo = 0;
    var complaintLines = $.grep(data.orderlines, function(line, i) {
      if (line.complaint) {
        line.complaint.price = line.finalPrice * Number(line.complaint.qty);
        totalCredit += line.complaint.price;
        line.complaint.price = parseFloat(line.complaint.price).toFixed(2);

        line.complaint.promo = line.savedAmount * Number(line.complaint.qty);
        totalPromo += line.complaint.promo;
        line.complaint.promo = parseFloat(line.complaint.promo).toFixed(2);

        line.complaint.tax = line.taxDepositSum * Number(line.complaint.qty);
        totalTax += line.complaint.tax;
        line.complaint.tax = parseFloat(line.complaint.tax).toFixed(2);

      }
      return !!line.complaint;
    });

    return {
      complaintLines: complaintLines,
      totalCredit: parseFloat(totalCredit).toFixed(2),
      totalTax: parseFloat(totalTax).toFixed(2),
      totalPromo: parseFloat(totalPromo).toFixed(2)
    };
  }

  function validate(formData) {
    var keys = Object.keys(formData),
      hasValid = false,
      key,
      qty,
      lineId;

    for (var i = 0; i < keys.length; i++) {
      key = keys[i];
      if (key.indexOf("reason") === 0) {
        lineId = key.replace("reason-", "");
        qty = formData["qty-" + lineId];
        if ((formData[key] && !qty) || (!formData[key] && qty)) {
          return false;
        } else if (formData[key] && qty) {
          hasValid = true;
        }
      }
    }
    return hasValid;
  }

  function filterSpecialItems(orderlines, key) {
    var specialItems = [];
    for (var i = 0; i < orderlines.length; i++) {
      if(orderlines[i][key]) {
        specialItems.push(orderlines[i]);
      }
    }
    return specialItems;
  }

  function transformCartonNumbersArray(orderlines) {
    for (var i = 0; i < orderlines.length; i++) {
      if(Array.isArray(orderlines[i].cartonNumbers) && orderlines[i].cartonNumbers.length > 0) {
        orderlines[i].cartonNumbers = orderlines[i].cartonNumbers.join("|")
      } else if (orderlines[i].cartonNumbers.length === 0) {
        orderlines[i].cartonNumbers = '';
      }
    }
    return orderlines;
  }

  function getValidComplaintLines(formData) {
    var list = {};

    $.grep(Object.keys(formData), function(key) {
      if (key.indexOf("reason") !== 0 || !formData[key]) return false;
      var lineId = key.replace("reason-", "");
      var qty = formData["qty-" + lineId];
      if (!qty) return false;
      var cartonNumbers = formData["carton-" + lineId];
      if (cartonNumbers.length > 0) {
        cartonNumbers = cartonNumbers.split("|")
      } else {
        cartonNumbers = [];
      }
      list[lineId] = {
        reasonId: formData["reason-" + lineId],
        qty: qty,
        cartonNumbers: cartonNumbers
      };
    });
    return list;
  }

  function mapComplaintsToOrderLines(list, orderlines, $) {
    $.each(orderlines, function(_, orderline) {
      var complaint = list[orderline.orderLineId];
      if (complaint) {
        complaint.description = $.grep(orderline.complaintReasons, function(
          reason
        ) {
          return reason.id === complaint.reasonId;
        })[0].reason;
        orderline.complaint = complaint;
      } else {
        orderline.complaint = null;
      }
    });
  }

  function setReadOnly(bool) {
    this.data.readonly = bool;
    this.extendedRefresh(this.data);
  }

  selfCreditIssueReportPopup.listen();
  fd.modules.common.utils.register(
    "components",
    "selfCreditIssueReportPopup",
    selfCreditIssueReportPopup,
    fd
  );
})(FreshDirect);
