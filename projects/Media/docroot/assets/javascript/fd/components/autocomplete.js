/*global jQuery*/

(function ($) {
  $('[data-component="autocomplete"]').autocomplete({
    source: '/api/autocompletejson.jsp',
    select: function (e, ui) {
      var form = $(this).parents('form').first()[0];

      $(this).val(ui.item.value);

      if (form) {
        form.submit();
      }
    },
    open: function (e, ui) {
      $(this).autocomplete('widget').css('z-index', 100);
    }
  });
}(jQuery));
