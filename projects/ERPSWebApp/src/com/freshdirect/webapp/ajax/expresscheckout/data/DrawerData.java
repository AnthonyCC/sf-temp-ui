package com.freshdirect.webapp.ajax.expresscheckout.data;

import java.util.List;

public class DrawerData {

	private String id;
	private String title;
	private boolean enabled;
	private List<String> onOpenCoremetrics;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getOnOpenCoremetrics() {
		return onOpenCoremetrics;
	}

	public void setOnOpenCoremetrics(List<String> onOpenCoremetrics) {
		this.onOpenCoremetrics = onOpenCoremetrics;
	}
}
