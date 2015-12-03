/*global expressco, Bacon*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  'use strict';

  var $ = fd.libs.$;
  var DRAWER_WIDGET = fd.modules.common.drawerWidget;
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
      value: function(data){
        var t = (new Date()).getTime();
        var requestURI = '/expressco/timeslots.jsp?thxie='+t;
        if(data && data.forceorder){
          requestURI += '&forceorder=true';
        }

        return { url: requestURI, type: 'GET', dataType:'html' };
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
      value:function(drawerTemplateData){
        $(timeslot.contentHolder()).html('');
        $(timeslot.contentHolder()).html(timeslot.contentTemplate(drawerTemplateData));
      }
    },
    getActualTimeSlotJsp:{
      value:function(requestConfig){
        var ajax = Bacon.fromPromise($.ajax(requestConfig));
        var drawerContent = timeslot.contentHolder() + ' [data-component="timeselectorcontent"]';

        ajax.onError(function(){
          $(drawerContent).html('<p class="error">Something went wrong. Please refresh the page to continue.</p>');
          return false;
        });

        ajax.onValue(function(ajaxData){
          $(drawerContent).html(ajaxData);
          timeslot.initTimeSlot();
        });
      }
    },
    initTimeSlot:{
      value:function(){
        var note;

        if(FreshDirect.fdTSDisplay){
          window.fdTSDisplay = new FreshDirect.fdTSDisplay("fdTSDisplay");
        }

        timeslot.expandDefaultColumn(window.fdTSDisplay);

        note = $(timeslot.contentHolder()).find('.timeslot-note');

        if (note.size()) {
          $('.timeslot-note').remove();
          note.insertBefore($('#ec-drawer'));
        }
      }
    },
    renderPreview:{
      value:function(data){
        $(timeslot.previewHolder()).html(timeslot.previewTemplate(data));
      }
    },
    callback:{
      value:function( value ) {
        timeslot.renderContent(value);
        timeslot.renderPreview(value);
        timeslot.getActualTimeSlotJsp(timeslot.createRequestConfig({ forceorder: !!value.forceOrderEnabled }));
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

  $(document).on('change', "[data-component='forceorder']", function(e){
    var checked = $(e.currentTarget).prop('checked');
    timeslot.getActualTimeSlotJsp(timeslot.createRequestConfig({ forceorder: checked }));
  });

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
