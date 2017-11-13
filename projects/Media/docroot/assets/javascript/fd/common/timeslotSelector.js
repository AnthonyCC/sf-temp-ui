/*global jQuery,browse,srch*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;

  function monthOfYearAsString(monthIndex) {
    return ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"][monthIndex];
  }

  function daySelectorTittle(timeslot) {
    return timeslot.dayOfWeek +', ' + monthOfYearAsString(timeslot.month-1) + ' ' + timeslot.dayOfMonth;
  }

  function timeslotsFormating(data, _selectedDay) {
    timeslotSelector.timeSlots = data;
    var times = {},
        zonePromoAmount = false,
        selectedDay = _selectedDay,
        days;

    data.timeSlots.forEach(function(e) {
      var timeslot = e,
          a = timeslot.startDate.split(/[^0-9]/),
          date = new Date(a[0],a[1]-1,a[2],a[3],a[4],a[5]),
          dayId = e.year * 10000 + e.month * 100 + e.dayOfMonth;

      if (!times[dayId]) {
        times[dayId] = {'times':[[],[],[]]};
        times[dayId].title = daySelectorTittle(timeslot);
      }

      if (data.selectedTimeslotId === e.id) {
        timeslot.selected = true;
        times[dayId].selected = !selectedDay ? true : false;
        selectedDay = selectedDay || dayId;
      }

      if (data.reservedTimeslotId === e.id) {
        timeslot.reserved = true;
      }

      if (data.zonePromoAmount) {
        zonePromoAmount = true;
      }

      if (times[dayId] && Number(dayId) === Number(selectedDay)) {
        times[dayId].selected = true;
      }

      if (times[dayId]) {
        if(date.getHours() < 17) {
          times[dayId].times[0].push(timeslot);
        } else if (date.getHours() < 23) {
          times[dayId].times[1].push(timeslot);
        } else {
          times[dayId].times[2].push(timeslot);
        }
      }
    });
    days = Object.keys(times).sort();
    selectedDay = selectedDay ? selectedDay : days[0];
    return {days: days, times: times, selectedDay: selectedDay, zonePromoAmount: zonePromoAmount};
  }

  var timeslotSelector = Object.create(WIDGET,{
    signal:{
      value:'timeslotData'
    },
    template:{
      value:common.timeslotSelector
    },
    placeholder:{
      value:'.timeslot-selector'
    },
    timeSlots:{
      value: null,
      writable:true
    },
    render:{
      value:function(data, selectedDay){
        var $ph = $(this.placeholder),
            data = data.result || this.timeSlots;


        if ($ph.length) {
          $ph.html(this.template(timeslotsFormating(data, selectedDay)));
          fd.modules.common.Select.selectize($ph);
          fd.modules.common.Elements.decorate($ph);
          fd.modules.common.aria.decorate();
          $('.timeslot-selector .select-day select').focus();
        }
      }
    }
  });

  $(document).on('change', '[selectDay] select', function(e) {
    timeslotSelector.render(timeslotSelector.timeSlots, e.currentTarget.value);
  });

  $(document).on('click', 'div.timeslot-selector:not(.delivery-info-timeslot) [selectTimeslot] li', function(e) {
    var deliveryTimeslotIdInput = document.getElementById("deliveryTimeslotId");
    if (deliveryTimeslotIdInput) {
      deliveryTimeslotIdInput.value = e.currentTarget.firstChild.firstChild.value;
    }
    var previousSelected = $(document).find('.timeslot-selector .selected-timeslot');
    if (previousSelected.length > 0) {
      $(previousSelected[0]).removeClass('selected-timeslot');
    }
    $(e.currentTarget).addClass('selected-timeslot');
    var timeslot = timeslotSelector.timeSlots.timeSlots.filter(function (timeslot) {
      return timeslot.id === e.currentTarget.firstChild.firstChild.value;
    });
    fd.components.coremetrics.sendTimeslotSelectingInfo(timeslot[0]);
    fd.common.dispatcher.signal('selectedTimeslotId', e.currentTarget.firstChild.firstChild.value);
  });

  timeslotSelector.listen();

  fd.modules.common.utils.register("common", "timeselector", timeslotSelector, fd);
}(FreshDirect));
