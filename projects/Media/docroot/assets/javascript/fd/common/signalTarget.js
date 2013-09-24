/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict";

	var DISPATCHER = fd.common.dispatcher;

	var signalTarget = {
		signal:'',
    	DISPATCHER: DISPATCHER,
    	allowNull: false, // switch to accept signal with null body
		callback:function(value){
		},
		listen:function() {
			var that = this;
			DISPATCHER.value.filter( function( value ){
				return ('to' in value) && (value.to === that.signal ) && (that.allowNull || value.body !== null);
			} ).onValue( function(value){
				that.callback(value.body);
			} );
		}
	};


	fd.modules.common.utils.register("common", "signalTarget", signalTarget, fd);
}(FreshDirect));
