package com.freshdirect.cms.search;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.AvailabilityChangeListener;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.framework.util.log.LoggerFactory;

public class AutoComplete implements AvailabilityChangeListener {
	private final static Logger LOGGER = LoggerFactory.getInstance(AutoComplete.class);
	private static Executor updater;
	private static boolean disableAutocompleter = false;
	
	private final class BuildAutoCompleteServiceTask implements Runnable {
		public void run() {
			long time = System.currentTimeMillis();
			LOGGER.info("autocomplete re-calculation started");
			version = FDCachedFactory.getMaxKnownVersion();
			AutoComplete.this.service = createAutocompleteService();
			time = System.currentTimeMillis() - time;
			LOGGER.info("autocomplete re-calculation finished, elapsed time : " + time + " ms");
		}
	}

	private AutocompleteService service;
	private boolean inited;
	private AutocompleteTermList terms;
	private int version;
	private final Object buildLock = new Object();
	private boolean permute;

	/**
	 * Autocomplete excepts a wordlist and a counter creator object, which sets how to generate the autocomplete tree
	 * 
	 * @param terms
	 */
	public AutoComplete(AutocompleteTermList terms, boolean permute) {
		this.permute = permute;
		this.terms = terms;
	}

	private AutocompleteService createAutocompleteService() {
		synchronized (buildLock) {
			terms.invalidate();
			AutocompleteService service = new AutocompleteService(terms.getTerms(), permute);
			LOGGER.info("AUTOCOMPLETE RELOADED");
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				LOGGER.info("interrupted", e);
			}
			return service;
		}
	}

	public static void setDisableAutocompleter(boolean d) {
		disableAutocompleter = d;
	}

	/**
	 * returns the autocomplete service, if already created, or initiate the building of that service in the background.
	 * 
	 * @return
	 */
	public AutocompleteService getService() {
		if (disableAutocompleter) {
			LOGGER.info("auto completer is disabled");
			return null;
		}
		LOGGER.info("initautocompleter");
		synchronized (AutoComplete.class) {
			if (this.service == null) {
				if (!inited) {
					inited = true;
					backgroundBuild();
				}
			}
			return this.service;
		}
	}

	private void backgroundBuild() {
		getExecutor().execute(new BuildAutoCompleteServiceTask());
	}

	private synchronized static Executor getExecutor() {
		if (updater == null) {
			updater = Executors.newSingleThreadExecutor();
		}
		return updater;
	}

	@Override
	public void availabilityInfoReceived(int version) {
		if (this.version == version) {
			LOGGER.info("availabilityInfoReceived called with same version as previous:" + version);
			return;
		}
		LOGGER.info("availabilityInfoReceived called with new version:" + version + ", previous: " + this.version);
		this.version = version;
		backgroundBuild();
	}

	@Override
	public void cacheReloaded() {
		LOGGER.info("cacheReloaded called");
		version = 0;
		backgroundBuild();
	}

	public void rebuild() {
		this.service = createAutocompleteService();
	}
}
