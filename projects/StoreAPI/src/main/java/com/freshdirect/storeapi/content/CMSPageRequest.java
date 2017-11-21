package com.freshdirect.storeapi.content;

import java.util.Date;
import java.util.List;

public class CMSPageRequest {
	private String pageType;
	private Date requestedDate;
	private boolean ignoreSchedule;
	private boolean preview;
	private String feedId;
	private String plantid;
    private Integer startSectionIndex;
    private Integer howManySections;
	
	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	public Date getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}

	public boolean isIgnoreSchedule() {
		return ignoreSchedule;
	}

	public void setIgnoreSchedule(boolean ignoreSchedule) {
		this.ignoreSchedule = ignoreSchedule;
	}

	public boolean isPreview() {
		return preview;
	}

	public void setPreview(boolean preview) {
		this.preview = preview;
	}

	public String getFeedId() {
		return feedId;
	}

	public void setFeedId(String feedId) {
		this.feedId = feedId;
	}
	
	public String getPlantId() {
		return plantid;
	}

	public void setPlantId(String plantid) {
		this.plantid = plantid;
	}
	
    public Integer getStartSectionIndex() {
        return startSectionIndex;
    }

    public void setStartSectionIndex(Integer startSectionIndex) {
        this.startSectionIndex = startSectionIndex;
    }

    public Integer getHowManySections() {
        return howManySections;
    }

    public void setHowManySections(Integer howManySections) {
        this.howManySections = howManySections;
    }

    public void setHeadSectionIndexes() {
        setStartSectionIndex(0);
        setHowManySections(2);
    }

    public void setBodySectionIndexes() {
        setStartSectionIndex(2);
        setHowManySections(-1);
    }

    public String getCacheKey() {
        return asCacheKey(getPageType());
    }

    public String getCacheKey(CMSWebPageModel page) {
        return asCacheKey(page.getType());
    }

    public String asCacheKey(String key) {
        if (getStartSectionIndex() != null) {
            key += "_" + getStartSectionIndex();
        }
        if (getHowManySections() != null) {
            key += "_" + getHowManySections();
        }
        return key;
    }

    public void limitSections(CMSWebPageModel page) {
        page.setSections(limitSections(page.getSections()));
    }

    public List<CMSSectionModel> limitSections(List<CMSSectionModel> sections) {
        Integer start = startSectionIndex;
        Integer howMany = howManySections;

        if (sections == null || sections.size() == 0) {
            return sections;
        }
        if (start == null && howMany == null) {
            return sections;
        }

        if (start == null) {
            start = 0;
        } else if (start >= sections.size()) {
            sections.clear();
            return sections;
        } else if (start < 0) {
            start = Math.max(0, sections.size() - 1 + start);
        }

        if (howMany == null || howMany < 0) {
            howMany = sections.size() - start;
        }

        howMany = Math.min(howMany, sections.size() - start);

        return sections.subList(start, start + howMany);
    }
	
}