package com.freshdirect.cms.search;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.content.ContentSearch;
import com.freshdirect.framework.util.log.LoggerFactory;

public class AutoComplete {

    private final static Logger LOGGER = LoggerFactory.getInstance(ContentSearch.class);
	private AutocompleteService autocompletion;
	private Thread autocompleteUpdater;
	private WordListI wordlist;
	private CounterCreatorI cc;
    
	/**
	 * Autocomplete excepts a wordlist and a counter creator object, which sets how to generate the autocomplete tree
	 * 
	 * @param wordlist
	 * @param cc
	 */
	public AutoComplete(WordListI wordlist, CounterCreatorI cc) {
		this.wordlist = wordlist;
		this.cc = cc;
	}
	
	
	public AutocompleteService createAutocompleteService() {
		AutocompleteService autocompletion = new AutocompleteService();
		autocompletion.setCounterCreator(cc);
		autocompletion.setWordList(wordlist.getWords());
    	autocompletion.addAllBadSingular(SearchRelevancyList.getBadPluralFormsFromCms());
        return autocompletion;     
    }
    
    public void setAutocompleteService(AutocompleteService serv) {
    	autocompletion = serv;
    }
    
    public AutocompleteService getAutocompleteService() {
    	return autocompletion;
    }
	
    public AutocompleteService initAutocompleter(String autocompleter) {
        LOGGER.info("initautocompleter");
        synchronized(this) {
            if (this.autocompletion == null) {
                if (autocompleteUpdater == null) {
                    autocompleteUpdater = new Thread(new Runnable() {
                        public void run() {
                            long time = System.currentTimeMillis();
                            LOGGER.info("autocomplete re-calculation started");
                            AutoComplete.this.autocompletion = createAutocompleteService();
                            time = System.currentTimeMillis() - time; 
                            LOGGER.info("autocomplete re-calculation finished, elapsed time : "+time+" ms");
                        }
                    }, autocompleter+"Updater");
                    autocompleteUpdater.setDaemon(true);
                    autocompleteUpdater.start();
                }
                return null;
            } else {
                return autocompletion;
            }
        }
    }

}
