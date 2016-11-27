/*global jQuery*/
// makes .unselectable elements unselectable

(function ($) {
  $('.unselectable,.qtyinput').attr('unselectable', 'on').css('user-select', 'none').on('selectstart', false);
}(jQuery));
