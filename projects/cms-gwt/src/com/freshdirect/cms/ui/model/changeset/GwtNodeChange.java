package com.freshdirect.cms.ui.model.changeset;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.ui.model.ContentNodeModel;

public class GwtNodeChange extends ContentNodeModel {

    private static final long serialVersionUID = 1L;

    private List<GwtChangeDetail> changeDetails = new ArrayList<GwtChangeDetail>();
    
    private String changeType;
    private String previewLink;
    
    
	protected GwtNodeChange() {    	
    }
    
	public GwtNodeChange( String type, String label, String contentKey, String changeType, String previewLink ) {
		super( type, label, contentKey );
		setChangeType( changeType );
		setPreviewLink( previewLink );
	}

    
    public List<GwtChangeDetail> getChangeDetails() {
        return changeDetails;
    }    
    public void setChangeDetails(List<GwtChangeDetail> changeDetails) {
        this.changeDetails = changeDetails;
    }
    public void addDetail(GwtChangeDetail detail) {
        this.changeDetails.add(detail);
    }

    
    public String getChangeType() {
        return changeType;
    }
    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }
 
    
	public String getPreviewLink() {
		return previewLink;
	}	
	public void setPreviewLink( String previewLink ) {
		this.previewLink = previewLink;
	}
   

    public int length() {
        // delete/create events have no details, but count as one change.
        return changeDetails.size() == 0 ? 1 : changeDetails.size();
    }
    
    @Override
    public String toString() {
        return "GwtNodeChange[" + getKey() + ": " + changeType + " - " + length() + "change(s) " + ']';
    }

}
