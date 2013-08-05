var FreshDirect = FreshDirect || {};

(function(fd) {
	
	var $ = fd.libs.$;
	
  /****
   * @param {JQuery object} $container the container to handle "see more" links
   * @param {String} handleSelector selector for "see more" handles
   * @param {String} boxSelector selector for "see more" boxes
   * @param {String} toggleClass css class to add/remove when handle clicked
   */
  var seeMore = function ($container, handleSelector, boxSelector, toggleClass) {
    $container = $container || $('#filters');
    handleSelector = handleSelector || '.filterbox .see';
    boxSelector = boxSelector || '.filterbox';
    toggleClass = toggleClass || 'filterbox-showall';

    $container.on('click', handleSelector, function (e) {
      $(this).parent(boxSelector).toggleClass(toggleClass);
    });
  };
	
  // call with default parameters
  seeMore();

	// register in fd namespace
	fd.modules.common.utils.register("modules.search", "seeMore", seeMore, fd);
}(FreshDirect));
