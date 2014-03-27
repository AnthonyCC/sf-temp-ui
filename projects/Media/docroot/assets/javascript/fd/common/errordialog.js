/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict"

	var $ = fd.libs.$;
	var DISPATCHER = fd.common.dispatcher;
	var MESSAGE = "Timing error, please reload page.";
	var errorDialogNode = null;


	var errorDialog = Object.create(fd.common.signalTarget,{
		signal:{
			value:'errorDialog'
		},
		init:{
			value:function() {
				errorDialogNode = $('<div data-component="errorDialog"><div data-component="errorDialog.panel"><p data-component="errorDialog.message"></p><button class="cssbutton orange">Refresh</button></div></div>')
				$(document.body).append(errorDialogNode);
			}
		},
		callback:{
			value:function( config ) {
				var message = config.message || MESSAGE; 
				if( errorDialogNode === null ) {
					this.init();
				}

				$('[data-component="errorDialog.message"]').html(message);
				$('[data-component="errorDialog"]').addClass("show");

			}
		}
	});

	errorDialog.listen();

	$(document).on('click','[data-component="errorDialog"] button',function(){
		document.location.reload();
	});


	fd.modules.common.utils.register("common", "errorDialog", errorDialog, fd);
}(FreshDirect));
