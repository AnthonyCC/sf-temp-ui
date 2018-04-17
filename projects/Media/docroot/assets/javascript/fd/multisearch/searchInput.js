/*global multisrch*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;
  var DISPATCHER = fd.common.dispatcher;
  var MAXTERMS = 10; // TODO configurable

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
    handleSubmit:{
      value: function (e) {
        e.preventDefault();

        var $el = $('[data-component="multisearch-input"] input'),
            term = $el.val();

        this.addTerm(term);
        $el.val('');
      }
    },
    addTerm:{
      value: function (term) {
        var idx = this.terms.indexOf(term);

        if (term && this.terms.length < MAXTERMS) {
          if (idx === -1) {
            this.terms.push(fd.utils.escapeHtml(term));

            this.termsChanged();
          }
        }
      }
    },
    removeTerm:{
      value: function (e) {
        var termEl = $(e.currentTarget).parent(),
            term = termEl.attr('data-searchterm'),
            idx = this.terms.indexOf(term);

        if (idx > -1) {
          this.terms.splice(idx, 1);
        }

        this.termsChanged();
      }
    },
    termsChanged: {
      value: function () {
        DISPATCHER.signal('multiSearch', {
          terms: this.terms
        });

        this.render();
      }
    },
    render:{
      value:function(data){
        data = data || {};
        data.terms = data.terms || this.terms;

        if (this.terms !== data.terms) {
          this.terms = data.terms;
          this.termsChanged();
        }

        WIDGET.render.call(this, data);
        FreshDirect.components.autoComplete.init(this.placeholder+' input');
        $('[data-component="multisearch-input"] input').val("");
      }
    }
  });
  searchInput.listen();

  $(document).on('submit', '[data-component="multisearch-input"] form', searchInput.handleSubmit.bind(searchInput));
  $(document).on('click', '[data-component="multisearch-input"] button.remove', searchInput.removeTerm.bind(searchInput));

  fd.modules.common.utils.register("modules.multisearch", "searchInput", searchInput, fd);
}(FreshDirect));

(function (fd) {
  setTimeout(function () {
    var q = fd.utils.getParameterByName('q').split(',').map(fd.utils.escapeHtml);
    fd.modules.multisearch.searchInput.render({
      terms: q
    });
  }, 10);
}(FreshDirect));
