/*global YAHOO*/
var autoCompleteFunctionFactory = function (apiUrl, termsId, inputId, skipSpaces, form, dontSubmit) {
	apiUrl = apiUrl || "/api/autocompleteresults.jsp";
	termsId = termsId || "terms";
	inputId = inputId || "searchxParams";
	var input = YAHOO.util.Dom.get(inputId);
	form = form || (input && input.form) || "adv_search";
	skipSpaces = (skipSpaces === false) ? false : true;
	var autoSubmit = !dontSubmit;

	return function () {
		var oDS = new YAHOO.util.XHRDataSource(apiUrl, { connMethodPost : false });

		oDS.responseType = YAHOO.util.XHRDataSource.TYPE_TEXT;

		oDS.responseSchema = {recordDelim: '\n', fieldDelim: '\t'};

		// Instantiate the AutoComplete
		var oAC = new YAHOO.widget.AutoComplete(inputId, termsId, oDS);
		oAC.allowBrowserAutocomplete = false;
		oAC.autoHighlight = false;
		oAC.typeAhead = true;
		oAC.animVert = false;
		oAC.animHoriz = false;

		oAC.generateRequest = function (sQuery) {
			return "?prefix=" + sQuery;
		};

		if (autoSubmit && form) {
			oAC.itemSelectEvent.subscribe(function (t) {
				var f = YAHOO.util.Dom.get(form);
				if (f) {
					f.submit();
				}
			});
		}

		var termsList = document.getElementById(termsId);
		var searchInput = document.getElementById(inputId);

		YAHOO.util.Dom.setX(termsList, YAHOO.util.Dom.getX(searchInput));
		YAHOO.util.Dom.setY(termsList, YAHOO.util.Dom.getY(searchInput) + searchInput.offsetHeight);

		termsList.style.zIndex = "2";
	};
};
