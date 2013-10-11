/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict"

	var $ = fd.libs.$;
	var DISPATCHER = fd.common.dispatcher;
	var errorMessages={
			"401":'<div class="unauthorized">You have to be logged in with a FreshDirect account.<br><br><a href="/login/login.jsp">Please Log In</a></div>'
	}


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
						if(v==='show') $(config.spinner.element).addClass('loading');
					});					
				
					state.onValue(function(show){
						if(!show) {
							$(config.spinner.element).removeClass('loading');
						}
					});
				}
			}
		}
	});

	server.listen();

	fd.modules.common.utils.register("common", "server", server, fd);
}(FreshDirect));
