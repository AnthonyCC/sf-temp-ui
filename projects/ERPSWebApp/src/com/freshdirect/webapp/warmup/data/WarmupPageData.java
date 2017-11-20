package com.freshdirect.webapp.warmup.data;


public class WarmupPageData {

    private boolean notTriggered;
    private boolean inProgress;
    private boolean repeatedWarmupCanHappen;
    private boolean manualWarmupAllowed;

    public WarmupPageData() {
    }

    public boolean isNotTriggered() {
        return notTriggered;
    }

    public void setNotTriggered(boolean notTriggered) {
        this.notTriggered = notTriggered;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public boolean isRepeatedWarmupCanHappen() {
        return repeatedWarmupCanHappen;
    }

    public void setRepeatedWarmupCanHappen(boolean repeatedWarmupCanHappen) {
        this.repeatedWarmupCanHappen = repeatedWarmupCanHappen;
    }

    public boolean isManualWarmupAllowed() {
        return manualWarmupAllowed;
    }

    public void setManualWarmupAllowed(boolean manualWarmupAllowed) {
        this.manualWarmupAllowed = manualWarmupAllowed;
    }

}