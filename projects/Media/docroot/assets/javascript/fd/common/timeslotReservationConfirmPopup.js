/*global common*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;

  var tsReservConfirmPopup = Object.create(POPUPWIDGET,{
    template:{
      value:common.fixedPopup
    },
    bodySelector:{
      value:'.qs-popup-content'
    },
    bodyTemplate: {
      value: common.timeslotReservationConfirmPopup
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
      value: 'tsReservConfirm'
    },
    popupConfig: {
      value: {
        placeholder: false,
        stayOnClick: false,
        overlay:true
      }
    },
    open: {
      value: function () {
        tsReservConfirmPopup.popup.show($('body'),false);
      }
    }
  });

  var getDay = function (d) {
    return ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'][d];
  };

  var toAmPm = function (h, m) {
    var ampm = h >= 12 ? 'pm' : 'am';

    h = h === 0 ? 24 : h;
    h = h > 12 ? h - 12 : h;

    return h + (m ? ':' + m : '') + ' ' + ampm;
  };

  if (fd.timeslotReservation) {
    var tsr = fd.timeslotReservation,
        eDate = new Date(+tsr.expiration),
        rStart = new Date(+tsr.reservationStart),
        rEnd = new Date(+tsr.reservationEnd),
        tsData = {
          hasPreReserved: tsr.hasPreReserved==='true',
          reservationDate: getDay(rStart.getDay()) + ' ' + (rStart.getMonth() + 1) + '/' + rStart.getDate(),
          reservationStart: toAmPm(rStart.getHours()),
          reservationEnd: toAmPm(rEnd.getHours()),
          expiration: getDay(eDate.getDay()) + ' ' + toAmPm(eDate.getHours(), eDate.getMinutes())
        };

    tsReservConfirmPopup.render(tsData);
    tsReservConfirmPopup.open();
  }

  fd.modules.common.utils.register("components", "tsReservConfirmPopup", tsReservConfirmPopup, fd);
}(FreshDirect));
