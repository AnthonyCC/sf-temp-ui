package com.freshdirect.fdstore.content;

import java.util.Collection;
import java.util.Comparator;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentKey;

public interface ContentNodeModel {

	public final static Comparator<ContentNodeModel> FULL_NAME_COMPARATOR = new Comparator<ContentNodeModel>() {
		public int compare(ContentNodeModel cn1, ContentNodeModel cn2) {
			String name1 = cn1.getFullName().toLowerCase();
			String name2 = cn2.getFullName().toLowerCase();

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
    public final static Comparator<ContentNodeModel> FULL_NAME_WITH_ID_COMPARATOR = new Comparator<ContentNodeModel>() {
        public int compare(ContentNodeModel cn1, ContentNodeModel cn2) {
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

    // from the old ContentNodeI
    
    String TYPE_STORE = "X";
    String TYPE_TEMPLATE = "T";
    String TYPE_DEPARTMENT = "D";
    String TYPE_CATEGORY = "C";
    String TYPE_PRODUCT = "P";
    String TYPE_CONFIGURED_PRODUCT = "PC";
    String TYPE_SKU = "S";
    String TYPE_BRAND = "B";
    String TYPE_PRODUCTPROXY = "Q";
    String TYPE_DOMAIN = "Z";
    String TYPE_DOMAINVALUE = "V";
    String TYPE_COMPONENTGROUP = "G";
    String TYPE_RECIPE_DEPARTMENT = "RD";
    String TYPE_RECIPE_CATEGORY = "RC";
    String TYPE_RECIPE_SUBCATEGORY = "RSC";
    String TYPE_RECIPE = "R";
    String TYPE_RECIPE_VARIANT = "RV";
    String TYPE_RECIPE_SECTION = "RS";
    String TYPE_RECIPE_SOURCE = "RSO";
    String TYPE_RECIPE_AUTHOR = "RA";
    String TYPE_FD_FOLDER = "FDF";
    String TYPE_BOOK_RETAILER = "BKR";
    String TYPE_RECIPE_SEARCH_PAGE = "RSP";
    String TYPE_RECIPE_SEARCH_CRITERIA = "RSPC";
    String TYPE_YMAL_SET = "YS";
    String TYPE_STARTER_LIST = "SL";
    String TYPE_FAVORITE_LIST = "FL";
    String TYPE_RECOMMENDER = "RDR";
    String TYPE_RECOMMENDER_STRATEGY = "RDS";    
    String TYPE_FAQ="FAQ";

    public ContentKey getContentKey();

    public String getContentName();

    public String getContentType();

    
    /**
     * This method is need to be declared here, because some helper class FDAttributeFactory 
     * and ContentNodeModelUtil needs this, but do not use from JSP-s or other 'client' codes.
     * @param name
     * @return
     */
    public Object getCmsAttributeValue(String name);

    
    public ContentNodeModel getParentNode();

    public boolean hasParentWithName(String[] contentNames);
    
    public String getParentId();

    // the end of the ContentNodeI methods
	
    public Object getNotInheritedAttributeValue(String name);
    
    public AttributeDefI getAttributeDef(String name);
    
	public String getAltText();

	public String getBlurb();

	public Html getEditorial();

	public String getEditorialTitle();

	public String getFullName();

	public String getGlanceName();

	public String getNavName();

	public String getKeywords();

	public boolean isSearchable();

	public boolean isHidden();

	public String getHideUrl();
	
	public Image getSideNavImage();

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
	public Collection<ContentKey> getParentKeys();
}
