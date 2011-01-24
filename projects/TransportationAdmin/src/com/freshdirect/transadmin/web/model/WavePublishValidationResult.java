package com.freshdirect.transadmin.web.model;

import java.io.Serializable;

public class WavePublishValidationResult implements Serializable {
		boolean hasPerviousPublish;
		boolean isPreviousPublishScrib;
		boolean isPreviousPublishPlan;
		public boolean isHasPerviousPublish() {
			return hasPerviousPublish;
		}
		public boolean isPreviousPublishScrib() {
			return isPreviousPublishScrib;
		}
		public boolean isPreviousPublishPlan() {
			return isPreviousPublishPlan;
		}
		public void setHasPerviousPublish(boolean hasPerviousPublish) {
			this.hasPerviousPublish = hasPerviousPublish;
		}
		public void setPreviousPublishScrib(boolean isPreviousPublishScrib) {
			this.isPreviousPublishScrib = isPreviousPublishScrib;
		}
		public void setPreviousPublishPlan(boolean isPreviousPublishPlan) {
			this.isPreviousPublishPlan = isPreviousPublishPlan;
		}
		
		
	}