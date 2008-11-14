var SafariBehaviorAutoComplete = function(searchField, listContainer, dataSource) {
    var that = this;
    var originalQuery = "";

    var searchInput = document.getElementById(searchField);
    
    SafariBehaviorAutoComplete.superclass.constructor.call(this, searchField, listContainer, dataSource);

    this.forceSelection = false;
    this.typeAhead = true;
    this.maxResultsDisplayed = 8;

    this.allowBrowserAutocomplete = false;	
    	    
    this.typeAheadEvent.subscribe(function(type, args) {		    
	    originalQuery = unescape(args[1]);			    
    });
    
    this.itemMouseOverEvent.subscribe(function(type, args) {
	    var autoComplete = args[0];
	    var listItem = args[1];
	    
	    var nStart = originalQuery.length;
            that._updateValue(listItem);
            var nEnd = searchInput.value.length;
            that._selectText(searchInput,nStart,nEnd);            		   
    });

    this._moveSelection = function(nKeyCode) {			    
	    SafariBehaviorAutoComplete.superclass._moveSelection.call(that, nKeyCode);			    
	    that._selectText(searchInput,originalQuery.length,searchInput.value.length);
    }	
};

YAHOO.lang.extend(SafariBehaviorAutoComplete, YAHOO.widget.AutoComplete);

YAHOO.util.Event.onDOMReady(function() {
    
    var oDS = new YAHOO.util.XHRDataSource("/api/autocompleteresults.jsp", { connMethodPost : true } );
    oDS.responseType = YAHOO.util.XHRDataSource.TYPE_TEXT;	    	   
    
    oDS.responseSchema = {recordDelim: '\n', fieldDelim: '\t'};   
    
    // Instantiate the AutoComplete
    var oAC = new SafariBehaviorAutoComplete("searchxParams", "terms", oDS);

    oAC.generateRequest = function(sQuery) { 
    	return "prefix=" + sQuery; 
    };
    
    var termsList = document.getElementById("terms");
    var searchInput = document.getElementById('searchxParams');
        
    YAHOO.util.Dom.setX(termsList, YAHOO.util.Dom.getX(searchInput));
    YAHOO.util.Dom.setY(termsList, YAHOO.util.Dom.getY(searchInput) + searchInput.offsetHeight);
    
    termsList.style.zIndex = "2";    
});
