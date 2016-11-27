var FreshDirect = FreshDirect || {};

(function (fd) {
  /**
   * Autocomplete for input elements
   *
   * @param {String} selector The css selector for the affected elements
   * @param {String} acPanelId id for the autocomplete panel (if we need multiple type of panels
   */
	var $ = fd.libs.$;
	
  var autocomplete = function (selector, acPanelId) {
    $(document).on('keyup', selector, showAutoCompletePanel);
    autocomplete.PANEL_ID = acPanelId || autocomplete.PANEL_ID;
  };

  // constants
  autocomplete.PANEL_ID = 'autoCompletePanel';
  autocomplete.$TARGET = null;
  autocomplete.$OVERLAY = $('<div id="autoCompleteOverlay" style="display: none; position: fixed; top: 0; left: 0; right: 0; bottom: 0; z-index: 999;"></div>').appendTo(document.body);

  var showAutoCompletePanel = function (e) {
    var $acpanel = $('#'+autocomplete.PANEL_ID),
        $t = $(e.target), val = $t.val().toLowerCase(),
        toff = $t.offset(), h = $t.outerHeight(), w = $t.width(),
        searchList, found = [], selected, next,
        key = e.keyCode;

    // tab or esc
    if (key === 27 || key === 9) {
      hideAutoCompletePanel();
      return;
    }

    // enter
    if (key === 13) {
      selected = $acpanel.find('li.selected').text();
      doAutoComplete($t, selected);
      return;
    }

    // up
    if (key === 38) {
      selected = $acpanel.find('li.selected');
      if (selected.length > 0) {
        next = selected.prev();
        if (next.length > 0) {
          next.addClass('selected');
          selected.removeClass('selected');
        }
      }
      return;
    }

    // down
    if (key === 40) {
      selected = $acpanel.find('li.selected');
      if (selected.length > 0) {
        next = selected.next();
        if (next.length > 0) {
          next.addClass('selected');
          selected.removeClass('selected');
        }
      }
      return;
    }

    searchList = fd.autocomplete && (fd.autocomplete[$t.attr('name')] || fd.autocomplete.GENERAL);

    if (!searchList || !val) {
      hideAutoCompletePanel();
      return;
    }

    found = $.grep(searchList, function (el, i) {
      return el.toLowerCase().indexOf(val) > -1;
    });

    if (found.length === 0) {
      hideAutoCompletePanel();
      return;
    }

    if ($acpanel.length === 0) {
      $acpanel = $('<div id="'+autocomplete.PANEL_ID+'"></div>').appendTo(document.body);
    }
    $acpanel.css({
      top: (toff.top+h)+'px',
      left: toff.left+'px',
      width: w+'px'
    }).show();
    autocomplete.$OVERLAY.show();

    autocomplete.$TARGET = $t;
    $acpanel.html('<ul><li>'+found.join('</li><li>')+'</li></ul>');
    $acpanel.find('li').first().addClass('selected');
  };

  var hideAutoCompletePanel = function (e) {
    var $acpanel = $('#'+autocomplete.PANEL_ID);
    $acpanel.hide();
    autocomplete.$OVERLAY.hide();
    autocomplete.$TARGET = null;
  };

  var doAutoComplete = function ($el, val) {
    var oldVal = '';
    if ($el && val) {
      oldVal = $el.val();
      $el.val(val);
      $el.trigger('change');
      $el.trigger('autocomplete', [ val, oldVal, $el ]);
    }
    hideAutoCompletePanel();
  };

  $(document).on('click', '#'+autocomplete.PANEL_ID+' li', function (e) {
    var $t = $(e.target), val = $t.text();

    doAutoComplete(autocomplete.$TARGET, val);
  });

  autocomplete.$OVERLAY.on('click', hideAutoCompletePanel);

  // register in fd namespace
  fd.modules.common.utils.register("modules.common", "autocomplete", autocomplete, fd);
}(FreshDirect));

// module initialization
(function () {
  FreshDirect.modules.common.autocomplete('input.autocomplete');
}());
