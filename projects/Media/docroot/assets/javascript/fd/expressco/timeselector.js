/*global expressco*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  'use strict';

  var $ = fd.libs.$;
  var DRAWER_WIDGET = fd.modules.common.drawerWidget;
  var WIDGET = fd.modules.common.widget;
  var DISPATCHER = fd.common.dispatcher;

  var timeslot = Object.create(DRAWER_WIDGET,{
    signal:{
      value:'timeslot' // TODO
    },
    contentTemplate: {
      value: expressco.timeselectorcontent
    },
    previewTemplate: {
      value: expressco.timeselectorpreview
    },
    createRequestConfig : {
      value: function(){
        var t = (new Date()).getTime();

        return { url: '/expressco/timeslots.jsp?thxie='+t, type: 'GET', dataType:'html' };
      }
    },
    expandDefaultColumn: {
      value: function(timeSlot){
        if(!timeSlot){
          return;
        }

        var dayIndex = 0,
            slotIndex = 0;

        if (window.dayIndex){
          dayIndex = window.dayIndex;
        }
        if (window.slotIndex) {
          slotIndex = window.slotIndex;
        }

        if (timeSlot.opts && !timeSlot.opts.timeSlotInfo) {
          /* click radio */
          if (timeSlot.slotObjs[timeSlot.getID('tsId', dayIndex, slotIndex)] && timeSlot.slotObjs[timeSlot.getID('tsId', dayIndex, slotIndex)].radioExt) {
            timeSlot.slotObjs[timeSlot.getID('tsId', dayIndex, slotIndex)].radioExt.click();
          }else{
            //passed indexes have no radio, just expand
            timeSlot.setDayAsExpanded(timeSlot.getID('dayId', dayIndex));
          }
        }else{
          /* just expand day */
          timeSlot.setDayAsExpanded(timeSlot.getID('dayId', dayIndex));
        }
      }
    },
    renderContent:{
      value:function(data){
        var ajax = Bacon.fromPromise($.ajax(timeslot.createRequestConfig()));

        ajax.onError(function(){
          $(timeslot.contentHolder()).
            html('<p class="error">Something went wrong. Please refresh the page to continue.</p>');

          return false;
        });

        ajax.onValue(function(ajaxData){
          $(timeslot.contentHolder()).html(timeslot.contentTemplate());
          $(timeslot.contentHolder() + ' [data-component="timeselectorcontent"]').html(ajaxData);

          // init timeslot selector behaviour

          if(FreshDirect.fdTSDisplay){
            window["fdTSDisplay"] = new FreshDirect.fdTSDisplay("fdTSDisplay");
          }

          timeslot.expandDefaultColumn(window.fdTSDisplay);
        });
      }
    },
    renderPreview:{
      value:function(data){
        $(timeslot.previewHolder()).html(timeslot.previewTemplate(data));
      }
    },
    callback:{
      value:function( value ) {
        timeslot.renderContent();
        timeslot.renderPreview(value);
      }
    },
    serialize:{
      value:function(){
        var ser = {};

        $(timeslot.contentHolder() + " form").serializeArray().map(function(k){
          ser[k.name] = k.value;
        });

        DISPATCHER.signal('server', {
          url: '/api/expresscheckout/timeslot',
          method: 'POST',
          data: {
            data: JSON.stringify({
              fdform: 'deliveryTimeslot',
              formdata: ser
            })
          }
        });
      }
    }
  });

  timeslot.listen();

  $(document).on('click', timeslot.contentHolder() + " [data-component='timeslotchangebutton']", timeslot.serialize.bind(timeslot));

  // timeslot related forms
  fd.modules.common.forms.register({
    id: "deliveryTimeslot",
    success: function () {
      if (fd.expressco.drawer) {
        fd.expressco.drawer.reset();
      }
    }
  });

  fd.modules.common.utils.register('expressco', 'timeselector', timeslot, fd);
}(FreshDirect));
