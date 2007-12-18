package com.freshdirect.cms.node;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.CmsException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.RelationshipI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.CmsRequest;
import com.freshdirect.cms.application.CmsUser;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.fdstore.ConfiguredProductLabelProvider;
import com.freshdirect.cms.fdstore.DomainValueLabelProvider;
import com.freshdirect.cms.fdstore.ErpMaterialLabelProvider;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.fdstore.MediaLabelProvider;
import com.freshdirect.cms.fdstore.SkuLabelProvider;
import com.freshdirect.cms.labels.AttributeLabelProvider;
import com.freshdirect.cms.labels.CompositeLabelProvider;
import com.freshdirect.cms.labels.ILabelProvider;
import com.freshdirect.cms.meta.ContentTypeUtil;
import com.freshdirect.framework.util.StringUtil;

/**
 * Provides utility methods to handle content nodes.
 * 
 * @TODO move label providers to a hivemind config
 */
public class ContentNodeUtil {

	private final static ILabelProvider LABEL_PROVIDER = new CompositeLabelProvider(new ILabelProvider[] {
		new DomainValueLabelProvider(),
		new MediaLabelProvider(),
		new SkuLabelProvider(),
		new ConfiguredProductLabelProvider(),
		new ErpMaterialLabelProvider(),
		new AttributeLabelProvider("FULL_NAME"),
		new AttributeLabelProvider("NAV_NAME"),
		new AttributeLabelProvider("GLANCE_NAME"),
		new AttributeLabelProvider("name"),
		new AttributeLabelProvider("title"),
		new AttributeLabelProvider("attribute")});

	/**
	 * Get a human-readable label for a content node.
	 * 
	 * @param node content node (never null)
	 * @return human readable label
	 */
	public static String getLabel(ContentNodeI node) {
		return LABEL_PROVIDER.getLabel(node);
	}

	/**
	 * Get a content node while traversing navigable nodes as well.
	 * 
	 * @FIXME eagerFetch is to pre-warm caches with related nodes as well
	 * 
	 * @param key
	 * @param navigableOnly
	 * @return
	 */
	public static ContentNodeI eagerFetch(ContentKey key, boolean navigableOnly) {
		ContentNodeI node = CmsManager.getInstance().getContentNode(key);
		if (node != null) {
			Set relKeys = collectRelatedKeys(node, navigableOnly);
			CmsManager.getInstance().getContentNodes(relKeys);
		}
		return node;
	}

	/**
	 * Get all navigable keys of a node.
	 * 
	 * @param node content node, never null
	 * @return Set of {@link ContentKey} (never null)
	 */
	public static Set getChildKeys(ContentNodeI node) {
		return collectRelatedKeys(node, true);
	}

	/**
	 * Recursively collect all reachable keys of the specified type.
	 * 
	 * @param node root node, never null
	 * @param type content type to search (null for all types)
	 * @return Set of {@link ContentKey} (never null)
	 */
	public static Set collectReachableKeys(ContentNodeI node, ContentType type) {
		Set s = new HashSet();
		collectReachableKeys(s, type, node);
		return s;
	}

	private static void collectReachableKeys(Set collectedKeys, ContentType targetType, ContentNodeI root) {
		Set children = root.getChildKeys();
		ContentTypeServiceI ts = CmsManager.getInstance().getTypeService();
		for (Iterator i = children.iterator(); i.hasNext();) {
			ContentKey k = (ContentKey) i.next();
			if (targetType == null || targetType.equals(k.getType())) {
				collectedKeys.add(k);
			}
			// recursion, if node may have reachable keys of targetType
			ContentTypeDefI def = ts.getContentTypeDefinition(k.getType());
			Set reachableTypes = ContentTypeUtil.getReachableContentTypes(ts, def);
			if (reachableTypes.contains(targetType)) {
				collectReachableKeys(collectedKeys, targetType, k.getContentNode());
			}
		}
	}

	/**
	 * Get all reachable keys of a node.
	 * 
	 * @param node content node, never null
	 * @return Set of {@link ContentKey} (never null)
	 */
	public static Set getAllRelatedContentKeys(ContentNodeI node) {
		return collectRelatedKeys(node, false);
	}

	/**
	 * Add a content key to a relationship. If it's cardinality ONE, set the key as the value.
	 * For cardinality MANY, it ensures uniqueness of keys.
	 * 
	 * @param relationship
	 * @param key
	 * 
	 * @return true if the key was added/set
	 */
	public static boolean addRelationshipKey(RelationshipI relationship, ContentKey key) {
		if (EnumCardinality.MANY.equals(relationship.getDefinition().getCardinality())) {
			List value = (List) relationship.getValue();
			if (value == null) {
				value = new ArrayList();
				relationship.setValue(value);
			} else {
				if (value.contains(key)) {
					return false;
				}
			}
			value.add(key);
		} else {
			relationship.setValue(key);
		}
		return true;
	}

	private static Set collectRelatedKeys(ContentNodeI node, boolean navigableOnly) {
		Set s = new HashSet();
		Map attrs = node.getAttributes();

		for (Iterator i = attrs.values().iterator(); i.hasNext();) {
			AttributeI attr = (AttributeI) i.next();
			if (attr instanceof RelationshipI) {
				RelationshipI rel = (RelationshipI) attr;
				RelationshipDefI relDef = (RelationshipDefI) rel.getDefinition();
				if (!navigableOnly || relDef.isNavigable()) {
					if (EnumCardinality.ONE.equals(relDef.getCardinality())) {
						if (rel.getValue() != null) {
							s.add(rel.getValue());
						}
					} else {
						if (rel.getValue() != null) {
							s.addAll((List) rel.getValue());
						}
					}
				}
			}
		}
		return s;
	}

	/**
	 * Get an index of inverse (ie. child -> parent) relationships.
	 * 
	 * @return Map of {@link ContentKey} (child) -> Set of {@link ContentKey} (parents)
	 */
	public static Map getParentIndex(Collection nodes) {
		Map parentsByKey = new HashMap(nodes.size());

		for (Iterator i = nodes.iterator(); i.hasNext();) {
			ContentNodeI node = (ContentNodeI) i.next();

			Set childKeys = ContentNodeUtil.getChildKeys(node);

			for (Iterator di = childKeys.iterator(); di.hasNext();) {
				ContentKey childKey = (ContentKey) di.next();
				Set parentKeys = (Set) parentsByKey.get(childKey);
				if (parentKeys == null) {
					parentKeys = new HashSet();
				}
				parentKeys.add(node.getKey());
				parentsByKey.put(childKey, parentKeys);
			}

		}
		return parentsByKey;
	}

	/*
	 public static boolean equalNodes(ContentNode node, Object obj) {
	 if (obj == this) {
	 return true;
	 }
	 if (obj instanceof ContentNodeI) {
	 if (!key.equals(((ContentNodeI) obj).getKey()))
	 return false;

	 ContentNodeI node = (ContentNodeI) obj;

	 Map attrs = getAttributes();
	 for (Iterator i = attrs.values().iterator(); i.hasNext();) {
	 AttributeI myAttr = (AttributeI) i.next();
	 AttributeI newAttr = node.getAttribute(myAttr.getName());
	 if (!newAttr.equals(myAttr))
	 return true;
	 }
	 return false;
	 }
	 return false;
	 }
	 */
	
	
	/**
	 * Performs a bulkload on the XLS file.
	 * 
	 * @param is XLS file as input stream
	 * @param userId user id
	 * @param successes (RETURN) the list of product keys successfully inserted, or if null nothing
	 * @param failures (RETURN) a map of product keys to exception messages of product keys that failed, or if null nothing
	 */
	public static void XLSBulkLoad(InputStream is, String userId, List successes, Map failures) throws IOException {
		//open file

		POIFSFileSystem fs = new POIFSFileSystem(is);
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFSheet sheet = wb.getSheetAt(0);

		// Iterate over each row in the sheet
		Iterator rows = sheet.rowIterator();
		
		Map attributeColumns = new HashMap();
		CmsRequest request = new CmsRequest(new CmsUser(userId));
		while (rows.hasNext()) {
			HSSFRow row = (HSSFRow) rows.next();

			if (row.getRowNum() == 0) {
				
				Map columnMap = new HashMap();

				Iterator cells = row.cellIterator();
				while (cells.hasNext()) {
					HSSFCell cell = (HSSFCell) cells.next();

					if (cell.getCellNum() == 0) {
						columnMap.put(new Integer(0), "PRODUCT_ID");
					} else {
						columnMap.put(new Integer(cell.getCellNum()), cell.getStringCellValue().toUpperCase());
					}
				}
				attributeColumns = columnMap;
				
				continue;
			}
			
			//create product
			ContentKey prodKey = new ContentKey(FDContentTypes.PRODUCT, row.getCell((short) 0).getStringCellValue());
			try {
				if(prodKey.lookupContentNode() != null){
					throw new CmsException("Duplicate Node " + prodKey);
				}
				
				ContentNodeI prod = CmsManager.getInstance().createPrototypeContentNode(prodKey);
				
				Iterator cells = row.cellIterator();

				while (cells.hasNext()) {
					HSSFCell cell = (HSSFCell) cells.next();

					if (cell.getCellNum() != 0 && cell.getCellType() != HSSFCell.CELL_TYPE_BLANK) {
						String attrName = (String) attributeColumns.get(new Integer(cell.getCellNum()));

						AttributeI attr = prod.getAttribute(attrName);
						if (attr == null) {
							attr = prod.getAttribute(attrName.toLowerCase());
						}
						
						if(attrName.toLowerCase().equals("skus")){
							ContentKey key = new ContentKey(FDContentTypes.SKU, cell.getStringCellValue());
			                
							createNode(key, request, attr);
						}	
						
						if(attrName.toLowerCase().equals("brands")){
							String fullname = cell.getStringCellValue();
			                String tmpBrandName = StringUtil.adjustAlphaHTMLEntities(fullname.toLowerCase().trim());
							int strLen=( tmpBrandName.length()>20?21:tmpBrandName.length() );

			                String id = "bd_"+removeNonAlpha(tmpBrandName.substring(0,strLen));
			                ContentKey key = new ContentKey(FDContentTypes.BRAND, id);
			                
			                createNode(key, request, attr);
							
						} else if (attr instanceof RelationshipI)
							setRelationshipValue(cell.getStringCellValue(), attr);
						else {
							attr.setValue(cell.getStringCellValue());
						}
					}
				}

				request.addNode(prod);
				if (successes != null) successes.add(prod.getKey());
			} catch (Exception e) {
				e.printStackTrace();
				if (failures != null) failures.put(prodKey.getId(),(e.getMessage() == null ? e.getClass().toString() : e.getMessage()));
				continue;
			}

		}
		CmsManager.getInstance().handle(request);
	}
	
	public static void createNode(ContentKey key, CmsRequest request, AttributeI attr){
        ContentNodeI node = key.lookupContentNode();
        if(node == null){
        	node = CmsManager.getInstance().createPrototypeContentNode(key);
            request.addNode(node);
        }
        
        setRelationshipValue(key.getId(), attr);
	}

	public static void setRelationshipValue(String value, AttributeI attr) {
		RelationshipI r = (RelationshipI) attr;
		RelationshipDefI relDef = (RelationshipDefI) r.getDefinition();
		if (relDef.getContentTypes().size() != 1) {
			throw new IllegalArgumentException(relDef.toString());
		}
		ContentType t = (ContentType) relDef.getContentTypes().iterator().next();

		ContentKey key = new ContentKey(t, value);
		if (r.getDefinition().getCardinality().equals(EnumCardinality.MANY)) {
			List list = new ArrayList();
			list.add(key);
			attr.setValue(list);
		} else {
			attr.setValue(key);
		}
	}
	
	
	private static String removeNonAlpha(String inComing) {
        // convert the html entitied
        if (inComing==null || inComing.length()<1) return inComing;
        String wrkString = StringUtil.removeHTMLEntities(inComing);
        StringBuffer outGoing = new StringBuffer();
        boolean lastCharNotValid=false;
        for (int charIdx=0;charIdx<wrkString.length();charIdx++) {
            char oneChar = wrkString.charAt(charIdx);
            if (lastCharNotValid  && (Character.isWhitespace(oneChar) || !Character.isLetterOrDigit(oneChar))) continue;
            if ((!Character.isWhitespace(oneChar) && !Character.isLetterOrDigit(oneChar))) continue; //skip this
            if (Character.isWhitespace(oneChar)) {
                lastCharNotValid=true;
                oneChar = '_';
            } else {
                lastCharNotValid = false;
            }
            outGoing.append(oneChar);
        }
        //LOGGER.info("brndContentName in: ["+inComing+"]  Out:["+outGoing.toString()+"]");
        return outGoing.toString();

    }

}