package com.freshdirect.fdstore.content;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import com.freshdirect.cms.AttributeI;

public interface ContentNodeModel extends ContentNodeI {

	public final static Comparator FULL_NAME_COMPARATOR = new Comparator() {
		public int compare(Object obj1, Object obj2) {
			ContentNodeModelImpl cn1 = (ContentNodeModelImpl) obj1;
			ContentNodeModelImpl cn2 = (ContentNodeModelImpl) obj2;
			String name1 = cn1.getFullName();
			String name2 = cn2.getFullName();

			if (name1 == null)
				name1 = "";
			if (name2 == null)
				name2 = "";

			return name1.compareTo(name2);
		}
	};

	/**
	 * This comparator sort the content nodes according to theirs fullname, and if the full names are equals, sort by it's content ids.
	 * 
	 */
    public final static Comparator FULL_NAME_WITH_ID_COMPARATOR = new Comparator() {
        public int compare(Object obj1, Object obj2) {
            ContentNodeModel cn1 = (ContentNodeModel) obj1;            
            ContentNodeModel cn2 = (ContentNodeModel) obj2;
            String name1 = cn1.getFullName();
            String name2 = cn2.getFullName();          
            
            if (name1 == null)
                    name1 = "";
            if (name2 == null)
                    name2 = "";

            int result = name1.compareTo(name2);
            if (result==0) {
                result = cn1.getContentKey().getId().compareTo(cn2.getContentKey().getId());
            }
            return result;
        }
    };
	
        public AttributeI getNotInheritedAttribute(String name);
    
	public String getAltText();

	public String getBlurb();

	public Html getEditorial();

	public String getEditorialTitle();

	public String getFullName();

	public String getGlanceName();

	public String getNavName();

	public String getKeywords();

	public List getAssocEditorial();

	public boolean isSearchable();

	public boolean isHidden();

	public boolean isDisplayable();
	
	public String getHideUrl();

	/**
	 * Generate a path string to this content object, that is suitable for
	 * OpenAdServer.
	 * 
	 * @return sitepage path for OAS
	 */
	public String getPath();

	public boolean isOrphan();

	/**
	 * A collection of ContentKeys.
	 * @return
	 */
	public Collection getParentKeys();
}
