/*global jQuery,Bacon*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var DISPATCHER = fd.common.dispatcher;
  var errorMessages={
      "401":'<div class="unauthorized">Session expired, please refresh!</div>'
  };
  var DEFAULT_SPINNER_CONFIG = {
    top: "20%"
  };

  /* helper function for successHandler
   * "this" means the data object
   * @param name property name
   */
  var _signalWidgets = function( name ) {
    DISPATCHER.signal( name, this[name] );
  };

  var successHandler = function( data ){
    try{
      Object.keys( data ).forEach( _signalWidgets, data );      
    } catch(e){
      // console.log(e);
    }
  };

  var errorHandler = function( e ){
    var status = e.status;
    DISPATCHER.signal('errorDialog',{
      message:errorMessages[status]
    });

  };

  var server = Object.create(fd.common.signalTarget,{
    signal:{
      value:'server'
    },
    callback:{
      value:function( config ) {

        var ajax = Bacon.fromPromise($.ajax({
                type:config.method || 'GET',
                url:config.url,
                data:config.data || {}
              }));

        ajax.onValue(successHandler);
        ajax.onError(errorHandler);

        var state = ajax.map(false).toProperty(true);

        if('spinner' in config) {
          Bacon.later(config.spinner.timeout,'show').filter(state).onValue(function(v){
            var $el = $(config.spinner.element), spinner, sp;
            if(v==='show') { 
              $el.first().addClass('loading');
              spinner = $el.find('.spinner-container');
              if (spinner.size() > 0) {
                sp = new window.Spinner(config.spinner.config || DEFAULT_SPINNER_CONFIG).spin(spinner[0]); // TODO spinner config
              }
            }
          });         
        
          state.onValue(function(show){
            var $el = $(config.spinner.element), spinner;
            if(!show) {
              $el.first().removeClass('loading');
              spinner = $el.find('.spinner-container');
              if (spinner.size() > 0) {
                spinner.html('');
              }
            }
          });
        }
      }
    }
  });

  server.listen();

  fd.modules.common.utils.register("common", "server", server, fd);
}(FreshDirect));
