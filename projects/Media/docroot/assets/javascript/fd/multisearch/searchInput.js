/*global multisrch*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;
  var DISPATCHER = fd.common.dispatcher;
  var SIGNALTARGET = fd.common.signalTarget;
  var MAXTERMS = fd.multisearch.limit || 25;

  var searchInput = Object.create(WIDGET,{
	signal:{
	  value:'multiSearchTerms'
	},
	template:{
	  value:multisrch.searchInput
	},
	placeholder:{
	  value:'#multisearch-input'
	},
	terms:{
	  value:[],
	  writable: true
	},
	suggestions:{
	  value:[],
	  writable: true
	},
	handleSubmit:{
	  value: function (e) {
		e.preventDefault();

		var $el = $('[data-component="multisearch-input"] input'),
			term = $el.val();
		
		if (fd.gtm && fd.gtm.updateDataLayer) {
			//analytics
			fd.gtm.updateDataLayer({
				'event': 'expresssearch-click',
				'eventCategory': 'express search',
				'eventAction': 'text search',
				'eventLabel': term.toLowerCase().trim().replace(/\s+/g, '+')
			});
		}
	

		this.addTerm(term);
		$el.val('');
	  }
	},
	addTerm:{
	  value: function (term) {
		term = term.toLowerCase();

		var idx = this.terms.map(function (t) { return t.toLowerCase(); }).indexOf(term);

		if (term && this.terms.length < MAXTERMS) {
		  if (idx === -1) {
			this.terms.unshift(fd.utils.escapeHtml(term));

			this.termsChanged();
		  }
		}
	  }
	},
	toggleTermEH:{
	  value: function (e) {
		var el = e.target,
			term = el.value,
			checked = el.checked;

		this.toggleTerm(term, !checked);
	  }
	},
	toggleTerm:{
	  value: function (term, disable) {
		var tterms = this.terms.map(function (t) { return t.toLowerCase(); }),
			idx = tterms.indexOf(term.toLowerCase());

		if (idx > -1) {
		  if (disable) {
			this.terms[idx] = term.toUpperCase();
		  } else {
			this.terms[idx] = term.toLowerCase();
		  }

		  if (fd.gtm && fd.gtm.updateDataLayer) {
				//analytics
				fd.gtm.updateDataLayer({
					'event': 'expresssearch-click',
					'eventCategory': 'express search',
					'eventAction': (disable) ? 'deactivate' : 'activate',
					'eventLabel': term.toLowerCase().trim().replace(/\s+/g, '+')
				});
			}

		}

		this.termsChanged();
	  }
	},
	removeTerm:{
	  value: function (e) {
		var termEl = $(e.currentTarget).parent(),
			term = termEl.attr('data-searchterm'),
			idx = this.terms.map(function (t) { return t.toLowerCase(); }).indexOf(term);

		if (idx > -1) {
		  this.terms.splice(idx, 1);
		}
		
		if (fd.gtm && fd.gtm.updateDataLayer) {
			//analytics
			fd.gtm.updateDataLayer({
				'event': 'expresssearch-click',
				'eventCategory': 'express search',
				'eventAction': 'remove term',
				'eventLabel': term.toLowerCase().trim().replace(/\s+/g, '+')
			});
		}

		this.termsChanged();
	  }
	},
	termsChanged: {
	  value: function (dontpush) {
		DISPATCHER.signal('multiSearch', {
		  terms: this.terms
		});

		if (!dontpush) {
		  // set new url
		  var pathname = window.location.pathname;
		  window.history.pushState({terms: this.terms}, "search: "+this.terms.join(', '), pathname + "?q=" + this.terms.map(function (t) { return encodeURIComponent(t); }).join(','));
		}

		try {
		  // save list to user
		  fetch(
			'/api/multisearch',
			{
			  method: 'POST',
			  credentials: 'same-origin',
			  headers: {
				'Content-Type': 'application/json'
			  },
			  body: JSON.stringify({
				searchTermList: this.terms.map(function (t) { return encodeURIComponent(t); }).join(',')
			  })
			}
		  ).catch(function (e) {
			console.warn('[error during saving search term list]', e);
		  });
		} catch (e) {
		  console.warn('[error during saving search term list]', e);
		}

		this.render();
	  }
	},
	render:{
	  value:function(data){
		data = data || {};
		data.terms = data.terms || this.terms;
		data.maxItems = MAXTERMS;

		if (this.terms !== data.terms) {
		  this.terms = data.terms;
		  this.termsChanged(data.dontpush);
		}

		data.terms = data.terms.map(function (term) {
		  var t = {};

		  t.name = term.toLowerCase();
		  t.slug = fd.utils.slugify(term);
		  t.enabled = term[0].toLowerCase() === term[0];

		  return t;
		});

		data.suggestions = data.suggestions || this.suggestions.map(function (s) { return s.toLowerCase(); });
		this.suggestions = data.suggestions;

		var lterms = this.terms.map(function (t) { return t.toLowerCase(); });
		data.suggestions = data.suggestions.filter(function (sug) { return lterms.indexOf(sug) === -1; });

		WIDGET.render.call(this, data);

		FreshDirect.components.autoComplete.init(this.placeholder+' input.searchinput');
		$('[data-component="multisearch-input"] input.searchinput').val("");
	  }
	}
  });
  searchInput.listen();

  var removeTerm = Object.create(SIGNALTARGET,{
	signal:{
	  value:'removeSearchTerm'
	},
	callback:{
	  value:function(data) {
		searchInput.toggleTerm(data.term, true);
	  }
	}
  });
  removeTerm.listen();

  $(document).on('submit', '[data-component="multisearch-input"] form', searchInput.handleSubmit.bind(searchInput));
  $(document).on('click', '[data-component="multisearch-input"] button.remove', searchInput.removeTerm.bind(searchInput));
  $(document).on('change', '[data-component="multisearch-input"] .termlist input', searchInput.toggleTermEH.bind(searchInput));
  $(document).on('change', '[data-component="multisearch-input"] .suggestions input', function (e) {
	var sug = e.currentTarget.value;

	if (sug) {
		if (fd.gtm && fd.gtm.updateDataLayer) {
			//analytics
			fd.gtm.updateDataLayer({
				'event': 'expresssearch-click',
				'eventCategory': 'express search',
				'eventAction': 'suggestion',
				'eventLabel': sug.toLowerCase().trim().replace(/\s+/g, '+')
			});
		}

		searchInput.addTerm(sug);
	}
  });

  fd.modules.common.utils.register("modules.multisearch", "searchInput", searchInput, fd);
}(FreshDirect));

// initialize based on query parameters
(function (fd) {
  var SUGGESTIONS = fd.multisearch.defaultList ? fd.multisearch.defaultList.split(',') : [];

  setTimeout(function () {
	var q = fd.utils.getParameterByName('q'),
		terms = (q || FreshDirect.multisearch.list || "")
		  .split(',')
		  .filter(function (kw) { return kw; })
		  .map(function (t) { return decodeURIComponent(t); });

	if (terms.length === 0) {
	  terms = [];
	}

	terms = terms.filter(function (kw) { return kw; }).map(function (kw) { return kw.trim(); }).map(fd.utils.escapeHtml);

	fd.modules.multisearch.searchInput.render({
	  suggestions: SUGGESTIONS,
	  terms: terms
	});
  }, 10);
}(FreshDirect));

// subscribe for popstate if feature is active
(function (fd) {
  var el = document.querySelector('#multisearch-input');

  if (el) {
	window.onpopstate = function (e) {
	  var state = e.state;
	  fd.modules.multisearch.searchInput.render({
		terms: state.terms,
		dontpush: true
	  });
	};
  }
}(FreshDirect));

// mobweb submen menu init
(function (fd) {
  var el = document.querySelector('.refine-btn-cont .modify-list-btn');

  if (el) {
	el.addEventListener('click', function (e) {
	  var leftnav = document.querySelector('.leftnav');

	  if (leftnav.style.display === 'none') {
		leftnav.style.display = 'block';
	  } else {
		leftnav.style.display = 'none';
	  }
	});
  }
}(FreshDirect));


