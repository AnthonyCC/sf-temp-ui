/*global jQuery, FreshDirect*/
var FreshDirect = FreshDirect || {};

(function ($, fd) {

  function autoCompleteInit(elementQueryPath){
    var queryPath = elementQueryPath && elementQueryPath.length ? elementQueryPath : '[data-component="autocomplete"]';

    $(queryPath).autocomplete({
      source: '/api/autocompletejson.jsp',
      select: function (e, ui) {
        var form = $(this).parents('form').first()[0],
            button = $(this).siblings('.searchbutton').first();

        $(this).val(ui.item.value);

        if (form) {
          form.submit();
        } else if (button.size()) {
          button.click();
        }
      },
      open: function (e, ui) {
        $(this).autocomplete('widget').css('z-index', 1010);
      }
    });
    
    $(queryPath).on('keydown', function (e) {
      var button = $(this).siblings('.searchbutton').first();

      if (e.keyCode === 13 && button.size()) {
        $(this).autocomplete && $(this).autocomplete('close');
        button.click();
        e.preventDefault();
      }
    });
  }

  autoCompleteInit();

  fd.modules.common.utils.register("components", "autoComplete", { 'init' : autoCompleteInit }, fd);

}(jQuery, FreshDirect));
