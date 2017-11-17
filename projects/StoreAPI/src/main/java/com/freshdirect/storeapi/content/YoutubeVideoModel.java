package com.freshdirect.storeapi.content;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.storeapi.attributes.FDAttributeFactory;

@Deprecated
public class YoutubeVideoModel extends ContentNodeModelImpl {

	public static final String DEFAULT_YOUTUBE_FOLDER = "FDFolder:youtubeVideoList";

    public YoutubeVideoModel(ContentKey key) {
        super(key);
	}

	public String getYouTubeVideoId(){
		return getAttribute("YOUTUBE_VIDEO_ID", "");
	}

	public String getTitle(){
		return getAttribute("TITLE", "");
	}

	public Html getContent(){
		return FDAttributeFactory.constructHtml(this, "CONTENT");
	}

}
