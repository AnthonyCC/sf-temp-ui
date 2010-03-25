var SafariBehaviorAutoComplete = function(searchField, listContainer, dataSource, skipSpaces) {
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
    
    this._updateValue = function(elListItem) {
        if(!this.suppressInputUpdate) {    
            var elTextbox = this._elTextbox;
            var sDelimChar = (this.delimChar) ? (this.delimChar[0] || this.delimChar) : null;
            var sResultMatch = elListItem._sResultMatch;
        
            // Calculate the new value
            var sNewValue = "";
            if(sDelimChar) {
                // Preserve selections from past queries
                sNewValue = this._sPastSelections;
                // Add new selection plus delimiter
                sNewValue += sResultMatch + sDelimChar;
                if(sDelimChar != " ") {
                    NewValue += " ";
                }
            }
            else { 
                sNewValue = sResultMatch;
            }
            
            /**
             * [APPREQ-285]
             */
            var skipChange = false;
            var old_str_len = elTextbox.value.length;
            if (old_str_len > 2 && skipSpaces) {
            	var firstWhitespacePos = sNewValue.indexOf(' ', old_str_len);
            	if (firstWhitespacePos == old_str_len) {
            		skipChange = true;
            	}
            }

            // Update input field
            if (!skipChange) {
            	elTextbox.value = sNewValue;
            }

            // Scroll to bottom of textarea if necessary
            if(elTextbox.type == "textarea") {
                elTextbox.scrollTop = elTextbox.scrollHeight;
            }
        
            // Move cursor to end
            var end = elTextbox.value.length;
            this._selectText(elTextbox,end,end);
        
            this._elCurListItem = elListItem;
        }
    };
    
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
    };
};

YAHOO.lang.extend(SafariBehaviorAutoComplete, YAHOO.widget.AutoComplete);

var autoCompleteFunctionFactory = function(apiUrl,termsId,inputId, skipSpaces) {
	var apiUrl = apiUrl || "/api/autocompleteresults.jsp";
	var termsId = termsId || "terms";
	var inputId = inputId || "searchxParams";
	var skipSpaces = (skipSpaces===false) ? false:true;
	
	
	return function() {
		var oDS = new YAHOO.util.XHRDataSource(apiUrl, { connMethodPost : true } );
		oDS.responseType = YAHOO.util.XHRDataSource.TYPE_TEXT;	    	   
	    
	    oDS.responseSchema = {recordDelim: '\n', fieldDelim: '\t'};   
	    
	    // Instantiate the AutoComplete
	    var oAC = new SafariBehaviorAutoComplete(inputId, termsId, oDS, skipSpaces);
	
	    oAC.generateRequest = function(sQuery) { 
	    	return "prefix=" + sQuery; 
	    };
	    
	    var termsList = document.getElementById(termsId);
	    var searchInput = document.getElementById(inputId);
	        
	    YAHOO.util.Dom.setX(termsList, YAHOO.util.Dom.getX(searchInput));
	    YAHOO.util.Dom.setY(termsList, YAHOO.util.Dom.getY(searchInput) + searchInput.offsetHeight);
	    
	    termsList.style.zIndex = "2";    
	    
	    // one-click submit solution
	    oAC.itemSelectEvent.subscribe(function(sType, aArgs) {
	    	document.forms['adv_search'].submit();
	    });	
	};
};
