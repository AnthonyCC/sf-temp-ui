package com.freshdirect.cms.ui.model.changeset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GwtContentNodeChange implements Serializable {

    private static final long serialVersionUID = 1L;

    private GwtChangeSet changeSet;    
    private List<GwtChangeDetail> changeDetails = new ArrayList<GwtChangeDetail>();
    
    private String changeType;
    private String label;

    private String contentKey;
    private String contentType;
    private String contentId;
    

    protected GwtContentNodeChange() {    	
    }
    
	public GwtContentNodeChange( String contentKey, String changeType ) {
		setContentKey( contentKey );
		setChangeType( changeType );
	}

	public void setContentKey( String contentKey ) {
    	try { 
			this.contentKey = contentKey;
	    	int idx = contentKey.indexOf( ':' );
	    	this.contentType = contentKey.substring( 0, idx );
	    	this.contentId = contentKey.substring( idx + 1 );    	
    	} catch ( IndexOutOfBoundsException e ) {
    		e.printStackTrace();
		} catch ( IllegalArgumentException e ) {
    		e.printStackTrace();
		} catch ( NullPointerException e ) {
    		e.printStackTrace();
		}				
	}
    public String getContentKey() {
        return contentKey;
    } 
    public String getContentType() {
        return contentType;
    }    
    public String getContentId() {
        return contentId;
    }
    
    
    /**
     * @return List of {@link GwtChangeDetail}
     */
    public List<GwtChangeDetail> getChangeDetails() {
        return changeDetails;
    }    
    public void setChangeDetails(List<GwtChangeDetail> changeDetails) {
        this.changeDetails = changeDetails;
    }

    
    public String getChangeType() {
        return changeType;
    }
    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }
 
    
    public void setLabel(String label) {
        this.label = label;
    }    
    public String getLabel() {
        return label;
    }

    
    public void setChangeSet(GwtChangeSet changeSet) {
        this.changeSet = changeSet;
    }
    public GwtChangeSet getChangeSet() {
        return this.changeSet;
    }

    
    
    public void addDetail(GwtChangeDetail detail) {
        this.changeDetails.add(detail);
    }

    public int length() {
        // delete/create events have no details, but count as one change.
        return changeDetails.size() == 0 ? 1 : changeDetails.size();
    }

}
