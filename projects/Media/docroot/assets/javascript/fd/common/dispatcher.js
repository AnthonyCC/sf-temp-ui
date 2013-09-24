/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict"

	var $ = fd.libs.$;

	var bus = new Bacon.Bus();

	var signal = function(to,body){
		bus.push({
			to:to,
			body:body
		});
	}

	var dispatcher = {
		bus: bus,
		signal: signal,
		value: bus.toProperty()
	};

	fd.modules.common.utils.register("common", "dispatcher", dispatcher, fd);
}(FreshDirect));
