/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict"

	var redirector = Object.create(fd.common.signalTarget,{
		signal:{
			value:'redirectUrl'
		},
    callback:{
			value:function( redirectUrl ) {
        if(redirectUrl && typeof redirectUrl === 'string'){
          if (redirectUrl === 'currentUrl') {
            location.reload();
          } else {
            location.href=redirectUrl;
          }
        }
			}
		}
	});

	redirector.listen();

	fd.modules.common.utils.register("components", "redirector", redirector, fd);
}(FreshDirect));
