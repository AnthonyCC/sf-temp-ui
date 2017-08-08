/*global jquery, lazyloadxt (extra) */
/*
 * use to intialize or add additional lazy loading code
 * see: https://github.com/ressio/lazy-load-xt
 * 
 * generic "lazyLoad" event can be triggered from anywhere to force update
 * 
 * scroll events need to be added on elems directly since they don't bubble
 **/
var FreshDirect = FreshDirect || {};

(function (fd) {
	var $=fd.libs.$;
	var scrollLimit = false;

	$(window).on('lazyLoad ajaxComplete', function(e) {
		if (fd.utils.isDeveloper()) {
			console.log('called lazy loader', e.type, e.target);
		}
		setTimeout(function() {
			$(window).lazyLoadXT();
		}, 50);
	});
}(FreshDirect));