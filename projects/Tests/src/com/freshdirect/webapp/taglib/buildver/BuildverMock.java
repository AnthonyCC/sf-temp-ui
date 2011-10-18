package com.freshdirect.webapp.taglib.buildver;

import com.freshdirect.fdstore.util.Buildver;

public class BuildverMock extends Buildver {
	private String buildver;
	private boolean developerMode;
	private boolean useMinified;

	public BuildverMock() {
	}

	@Override
	public String getBuildver() {
		return buildver;
	}

	public void setBuildver(String buildver) {
		this.buildver = buildver;
	}

	@Override
	public boolean isDeveloperMode() {
		return developerMode;
	}

	public void setDeveloperMode(boolean developerMode) {
		this.developerMode = developerMode;
	}

	@Override
	public boolean useMinified() {
		return useMinified;
	}

	public void setUseMinified(boolean useMinified) {
		this.useMinified = useMinified;
	}
}
