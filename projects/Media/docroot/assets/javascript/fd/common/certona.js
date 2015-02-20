/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict";

	var DISPATCHER = fd.common.dispatcher;
	var $ = fd.libs.$;

	var certona = Object.create(fd.common.signalTarget,{
		signal:{
			value:'certona'
		},
		updateCertona:{
			value:function( certonaDataString ){
				var certonaData = $.parseJSON(certonaDataString);
				var ss = '';

				//add to page (possibly again)
				if (certonaData.hasOwnProperty('SCRIPT_SRC')) {
					ss = certonaData.SCRIPT_SRC;
					delete certonaData.SCRIPT_SRC;
				}

				window['certona'] = $.extend({}, window['certona'], certonaData);

				if (ss !== '') {
					this.addScriptSrc(ss);
				}
			}
		},
		serialize:{
		  value: function () {
			return {
			};
		  }
		},
		callback:{
			value: function( certonaData ) {
				this.updateCertona(certonaData);
			}
		},
		addScriptSrc:{
			value: function(scriptSrc) {
				var head = document.getElementsByTagName('head')[0];
				var script = document.createElement('script');
				script.type = 'text/javascript';
				script.src = scriptSrc;
				head.appendChild(script);
			}
		}
	});

	certona.listen();

	if (fd.certonaData) {
		fd.certonaData.each(certona.updateCertona, certona);
	}

	fd.modules.common.utils.register("components", "certona", certona, fd);
}(FreshDirect));
