package com.freshdirect.cms.search;

import org.apache.log4j.Logger;

import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.framework.util.log.LoggerFactory;

public class AutoComplete {

    static boolean disableAutocompleter = false;

    private final static Logger LOGGER = LoggerFactory.getInstance(AutoComplete.class);
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
    
    public static void setDisableAutocompleter(boolean d) {
        disableAutocompleter = d;
    }
    
    public AutocompleteService getAutocompleteService() {
    	return autocompletion;
    }
	
    public AutocompleteService initAutocompleter(String autocompleter) {
        if (disableAutocompleter) {
            LOGGER.info("auto completer is disabled");
            return null;
        }
        synchronized(this) {
            if (this.autocompletion == null) {
                if (autocompleteUpdater == null) {
                    autocompleteUpdater = new Thread(new Runnable() {
                        public void run() {
                            // TODO this is a hack, update usercontext in every new thread, create UserContext with factory method everywhere
                            ContentFactory.getInstance().setCurrentUserContext(UserContext.createUserContext(CmsManager.getInstance().getEStoreEnum()));
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
