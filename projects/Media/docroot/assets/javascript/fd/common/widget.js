/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict";

	var $ = fd.libs.$;

	var widget = Object.create(fd.common.signalTarget,{
		template: {
			value: function(){
				return '';
			}
		},
		placeholder: {
			value: 'body'
		},
		render:{
			value:function(data){
				$(this.placeholder).html(this.template(data));
        fd.modules.common.Select.selectize($(this.placeholder));
			}
		},
		callback:{
			value:function( value ) {
				this.render(value);
			}
		}
	});


	fd.modules.common.utils.register("modules.common", "widget", widget, fd);
}(FreshDirect));
