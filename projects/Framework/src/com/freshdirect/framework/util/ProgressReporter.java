package com.freshdirect.framework.util;


/**
 * This is a helper class, which encapsulates the decision that the message should be logged or not, based on when the last message is logged, and how many step is taken since.
 * Usage:
 * 
 * <code>
 *   ProgressReporter p = new ProgressReporter();
 *   p.setElapsedMillis(2000); // to report at most 2000 ms
 *   p.setShowAtEvery(100); // report at every 100 item
 *   
 *   for (int i = 0;i < size;i++) { 
 *      if (p.shouldLogMessage(i)) {
 *           logger.info("we are at "+i+" from "+size);
 *      }
 *      // do stuff...   
 *   }
 * </code>
 *   
 * @author zsombor
 *
 */
public class ProgressReporter {

    long lastCheck = System.currentTimeMillis();
    long elapsedMillis = 10000;
    int showAtEvery = 1000;

    public ProgressReporter() {
    }

    public void setShowAtEvery(int showAtEvery) {
        this.showAtEvery = showAtEvery;
    }
    
    public void setElapsedMillis(long elapsedMillis) {
        this.elapsedMillis = elapsedMillis;
    }
    
    public int getShowAtEvery() {
        return showAtEvery;
    }
    
    public long getElapsedMillis() {
        return elapsedMillis;
    }

    public boolean shouldLogMessage(int counter) {
        long time = System.currentTimeMillis();
        if (time > lastCheck + elapsedMillis || counter % showAtEvery == 0) {
            lastCheck = time;
            return true;
        }
        return false;
    }

}
