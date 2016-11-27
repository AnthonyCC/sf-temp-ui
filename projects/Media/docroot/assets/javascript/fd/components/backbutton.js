/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict";

	var $ = fd.libs.$;

	$(document).on('click','button.back', function(e){
    window.history.go(-1);
	});

}(FreshDirect));
