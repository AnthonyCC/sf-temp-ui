package com.freshdirect.cms.ui.client.action;

import com.freshdirect.cms.ui.client.MainLayout;
import com.freshdirect.cms.ui.service.BaseCallback;


/**
 * Abstract class to provide basic services for callback based actions
 * 
 * @author segabor
 *
 * @param <T>
 */
public abstract class BasicAction<T> extends BaseCallback<T> {
	@Override
	public void errorOccured(Throwable error) {
		stopProgress("Failed to execute action due to an error.");
	}

	
	/**
	 * Start progress with custom messages
	 * 
	 * @param title Title of Progress Panel
	 * @param message Message of Progress Panel
	 * @param progressText Progress text of Progress Panel
	 * 
	 * @param statusText Text shown in status bar (optional)
	 */
	protected void startProgress(String title, String message, String progressText, String statusText) {
		final MainLayout ml = MainLayout.getInstance();

		ml.startProgress(title, message, progressText);
	}
	
	protected void startProgress(String title, String message, boolean isLoading, String statusText) {
		startProgress(title, message, isLoading ? "loading ..." : "saving ...", statusText);
	}

	protected void startLoadProgress(String message, String statusText) {
		startProgress("Load", message, "loading ...", statusText);
	}

	protected void startSaveProgress(String message, String statusText) {
		startProgress("Save", message, "saving ...", statusText);
	}

	protected void stopProgress() {
		MainLayout.getInstance().stopProgress();
	}
	
	@Deprecated
	protected void stopProgress(@SuppressWarnings( "unused" ) String message) {
		MainLayout.getInstance().stopProgress();
	}
}
