package com.freshdirect.cms.ui.model.publish;

import java.io.Serializable;
import java.util.ArrayList;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.freshdirect.cms.ui.model.SummaryData;

public class GwtPublishData extends BaseModel implements Serializable {
	private static final long serialVersionUID = -7617157630696728670L;
	
	public static final int FETCH_SIZE = 10;

	public static final String COMPLETE = "COMPLETE";
	public static final String FAILED = "FAILED";
	public static final String PROGRESS = "PROGRESS";

	public GwtPublishData() {
		set("fullyLoaded", false);
	}

	public void setId(String id) {
		set("id", id);
	}

	public String getId() {
		return get("id");
	}

	public void setStatus(String statusCode) {
		set("statusCode", statusCode);
	}

	public String getStatus() {
		return get("statusCode");
	}
	
	public boolean isInProgress() {
		return PROGRESS.equals(getStatus());
	}

	public void setCreated(String created) {
		set("created", created);
	}

	public final String getCreated() {
		return get("created");
	}

	public void setLastModified(String created) {
		set("lastModified", created);
	}

	public final String getLastModified() {
		return get("lastModified");
	}

	public void setPublisher(String publisher) {
		set("publisher", publisher);
	}

	public String getPublisher() {
		return get("publisher");
	}

	public void setComment(String comment) {
		set("comment", comment);
	}

	public String getComment() {
		return get("comment");
	}
	
	public Boolean isFullyLoaded() {
		return get("fullyLoaded");
	}

	public void setFullyLoaded(boolean loaded) {
		if (loaded) {
			set("changeCount", 0);
			set("contributors", new ArrayList<SummaryData>());
			set("types", new ArrayList<SummaryData>());
			set("messages", new ArrayList<SummaryData>());
		} else {
			set("changeCount", null);
			set("contributors", null);
			set("types", null);
			set("messages", null);
		}
		set("fullyLoaded", loaded);
	}

	public void setChangeCount(int changeCount) {
		set("changeCount", changeCount);
	}

	public Integer getChangeCount() {
		return get("changeCount");
	}

	public ArrayList<SummaryData> getContributors() {
		return get("contributors");
	}

	public ArrayList<SummaryData> getTypes() {
		return get("types");
	}

	public ArrayList<SummaryData> getMessages() {
		return get("messages");
	}

	public void addContributor(String name) {
		addContributor(name, 1);
	}
	
	public void addContributor(String name, int count) {
		int index = 0;
		SummaryData data = null;
		while (index < getContributors().size() && data == null) {
			if (getContributors().get(index).getKey().equals(name)) {
				data = getContributors().get(index);
			}
			index++;
		}
		if (data == null) {
			getContributors().add(new SummaryData(name, count));
			return;
		}
		data.setValue(data.getValue() + count);
	}

	public void addType(String type) {
		addType(type, 1);
	}
	
	public void addType(String type, int count) {
		int index = 0;
		SummaryData data = null;
		while (index < getTypes().size() && data == null) {
			if (getTypes().get(index).getKey().equals(type)) {
				data = getTypes().get(index);
			}
			index++;
		}
		if (data == null) {
			getTypes().add(new SummaryData(type, count));
			return;
		}
		data.setValue(data.getValue() + count);
	}

	public void addMessage(String messageType) {
		int index = 0;
		SummaryData data = null;
		while (index < getMessages().size() && data == null) {
			if (getMessages().get(index).getKey().equals(messageType)) {
				data = getMessages().get(index);
			}
			index++;
		}
		if (data == null) {
			getMessages().add(new SummaryData(messageType, 1));
			return;
		}
		data.setValue(data.getValue() + 1);
	}
}
