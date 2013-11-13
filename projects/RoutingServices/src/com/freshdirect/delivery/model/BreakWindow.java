package com.freshdirect.delivery.model;

import java.util.Set;

public class BreakWindow {
	
	Set<TimeslotWindow> windows;
	Set<TimeslotWindow> breaks;
	public Set<TimeslotWindow> getWindows() {
		return windows;
	}
	public void setWindows(Set<TimeslotWindow> windows) {
		this.windows = windows;
	}
	public Set<TimeslotWindow> getBreaks() {
		return breaks;
	}
	public void setBreaks(Set<TimeslotWindow> breaks) {
		this.breaks = breaks;
	}
}
