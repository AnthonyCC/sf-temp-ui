/*global jQuery,Bacon*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  //APPDEV-3971
  fd.properties = fd.properties || {};
  
  var focusedElementId;
  var DISPATCHER = fd.common.dispatcher;
  var errorMessages={
     // "401": '<div class="unauthorized">Session expired, please refresh!</div>',
      "500": function (e) {
        var result = e.responseText,
            message = "Internal Error";

        try {
          message = JSON.parse(result).error;
        } catch (err) {
        }

        message = '<div class="internalerrorheader">'+ message.primary +'</div><div class="internalerror">'+ message.secondary +'</div>';

        return message;
      }
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
    } catch(e) {}
    try {
      if (focusedElementId) {
        document.getElementById(focusedElementId).focus();
      }
    } catch(e) {}
  };
  //APPDEV-3971
  var loginSignupPopup = function (target, popupUrl) {
	    if (fd.components && fd.components.ifrPopup) {
	      fd.components.ifrPopup.open({ url: popupUrl + '?successPage=' + target, height: 590, width: 560, opacity: .5, mobWeb: fd.mobWeb});
	    }
	  };
  var socialLogin = function (target) {
		    loginSignupPopup(target, '/social/login.jsp');
		  };
  var errorHandler = function( e ){
    var status = e.status, message;
    if(status == 401){
	    var targetHolder = $('#target-link-holder').attr("href");
	    $('#target-link-holder').remove();
	    fd.user.recognized=true;
	    $("button[disabled]").removeAttr("disabled");
	    if (fd.properties.isSocialLoginEnabled) {
	    socialLogin(e.targetHolder);
	    }else{
	    window.location.href = "/login/login.jsp";
	    }
}
        message = errorMessages[status];

    if (typeof message === 'function') {
      message = message(e);
    }

    if (status) {
      DISPATCHER.signal('errorDialog',{
        message: message
      });
    }

  };
//END APPDEV 3971
  var server = Object.create(fd.common.signalTarget,{
    signal:{
      value:'server'
    },
    callback:{
      value:function( config ) {
        focusedElementId = config.dontfocus ? null : document.activeElement && document.activeElement.id;
        
        //for etipping, APPBUG-4219
        if(focusedElementId == "tipApply"){
        	$jq("#"+focusedElementId).text("wait...").css("opacity", "0.5").prop("disabled", true);
        }

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
                sp = new window.Spinner(config.spinner.config || DEFAULT_SPINNER_CONFIG).spin(spinner[0]);
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
