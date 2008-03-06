package com.freshdirect.cms.fdstore.recipes;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.RelationshipI;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.node.ContentNodeUtil;

/**
 * Recipe bulk loader class.
 */
public class RecipeBulkLoader {

	/**
	 *  The delimiters used for tokenization of input lines.
	 */
	private static final String DELIMITERS = "\t";

	/**
	 *  Constant marking that a ConfiguredProductGroup should be processed
	 * 
	 * @see #BulkLoader
	 */
	public static final int CONFIGURED_PRODUCT_GROUP = 1;
	
	/**
	 *  Constant marking that a Recipe should be processed
	 * 
	 * @see #BulkLoader
	 */
	public static final int RECIPE = 2;
	
	/**
	 *  The name of the newborn folder, to put all new nodes into.
	 *  
	 *  @see #addToNewbornFolder(List)
	 */
	private static final String NEWBORN_FOLDER_NAME = "NewbornRecipes";
	
	/**
	 *  The input stream.
	 */
	private LineNumberReader reader;
	
	/**
	 *  The id of the recipe to insert.
	 */
	private String recipeId;

	/**
	 *  The type of operation: if to process a ConfiguredProductGroup
	 *  or a Recipe
	 * 
	 * @see #CONFIGURED_PRODUCT_GROUP
	 * @see #RECIPE
	 */
	private int type;
	
	/**
	 *  The CmsManager to use for creating new content.
	 */
	private ContentServiceI service;

	/**
	 *  A list of SectionNode objects that is the result of parsing
	 *  the input.
	 */
	private List sectionNodes;

	/**
	 *  The list of content nodes created through the parsing
	 *  and processing.
	 */
	private List nodes;
	
	/**
	 *  A counter to help generating unique ids.
	 */
	private int idIndex;
	
	/**
	 *  The section indicated in the last processed input line.
	 */
	private String lastSection;
	
	/**
	 *  A simple container class that holds a content node
	 *  and an associated section.
	 */
	public class SectionNode {
		/**
		 *  The section name the node belongs to.
		 */
		public String section;
		
		/**
		 *  The content node.
		 */
		public ContentNodeI node;
		
		/**
		 *  Constructor.
		 * 
		 *  @param section the name of the section the node belongs to.
		 *  @param node the node.
		 */
		public SectionNode(String section, ContentNodeI node) {
			this.section = section;
			this.node    = node;
		}
	}
	
	/**
	 * Constructor.
	 * 
	 * @param service the content service to work on
	 * @param input the input stream to process.
	 * @param recipeId the id of the recipe to process.
	 * @param type specify what to process: a ConfiguredProductGroup
	 *        or a Recipe
	 * @see #CONFIGURED_PRODUCT_GROUP
	 * @see #RECIPE
	 */
	public RecipeBulkLoader(ContentServiceI	service,
					  Reader 			input,
		              String 			recipeId,
					  int    			type) {
		this.service  = service;
		reader        = new LineNumberReader(input);
		this.recipeId = recipeId;
		this.type     = type;
		
		idIndex       = 0;
	}
	
	/**
	 *  Process the input file.
	 * 
	 * @return a list containing SectionNode values, that correspond
	 *         to the input parsed. 
	 * @throws IOException on I/O errors.
	 */
	public List parse() throws IOException {
		String line;
		
		sectionNodes = new ArrayList();
		nodes        = new ArrayList();
		
		// skip the very first line, should be the column declarations only
		// one may want to check these just to make sure
		reader.readLine();
		
		// look at each line
		while ((line = reader.readLine()) != null) {
			// ingore lines that are white-space only
			String trimmedLine = line.trim();
			if (trimmedLine.length() == 0) {
				continue;
			}
			
			ContentNodeI node = processConfiguredProduct(line);
			
			if (node != null) {
				sectionNodes.add(new SectionNode(lastSection, node));
				nodes.add(node);
			}
		}
		
		return sectionNodes;
	}

	/**
	 *  Process the already parsed input, and create the appropriate
	 *  content nodes. This is either a Recipe or a ConfiguredProductGroup,
	 *  dependent on the type parameter supplied to the constructor.
	 *
	 *  @return a list of ContentNodeI objects, all the nodes created
	 *          while parsing and processing the input.
	 *          The last item in the list is the Recipe or the ConiguredProductGroup
	 *          itself
	 *  @throws InvalidContentKeyException key is invalid
	 *  @see #CONFIGURED_PRODUCT_GROUP
	 *  @see #RECIPE
	 */
	public List process() throws InvalidContentKeyException {
		List		list;
		
		switch (type) {
			case CONFIGURED_PRODUCT_GROUP:
				list = processConfiguredProductGroup();
				break;
				
			case RECIPE:
				list = processRecipe();
				break;
				
			default:
				return null;
		}
		
		list = addToNewbornFolder(list);
		
		return list;
	}

	/**
	 *  Add the processed list to a newborn folder, so that they are not
	 *  scattered as orphans all over the CMS.
	 *  
	 *  @param list the processessed list, with the last item being the 
	 *         Recipe or the ConiguredProductGroup itself
	 *  @return a list, with still the last item being the Recipe or
	 *          the ConfiguredProductGroup, but including changes to
	 *          add the node to the newborn folder as well, if one exists.  
	 * @throws InvalidContentKeyException 
	 */
	private List addToNewbornFolder(List list) throws InvalidContentKeyException {
		ContentKey		newbornKey    = ContentKey.create(FDContentTypes.FDFOLDER, NEWBORN_FOLDER_NAME);
		ContentNodeI		newbornFolder = service.getContentNode(newbornKey);
		
		if (newbornFolder == null) {
			return list;
		}
		
		newbornFolder = newbornFolder.copy();
		ContentNodeI node = (ContentNodeI) list.get(list.size() - 1);
		ContentNodeUtil.addRelationshipKey((RelationshipI) newbornFolder.getAttribute("children"),
										   node.getKey());
		
		// add thew newbornFolder so that it is not the last one in the list,
		// as the last one needs to be the Recipe of ConfiguredProductGroup itself
		list.add(list.size() - 1, newbornFolder);
		
		return list;
	}
	
	/**
	 *  Process a ConfiguredProductGroup based on the parsed information.
	 * 
	 *  @return a list of ContentNodeI objects, all the nodes created
	 *          while parsing and processing the input. 
	 *  @throws InvalidContentKeyException 
	 */
	private List processConfiguredProductGroup() throws InvalidContentKeyException {
		ContentKey		groupKey  = ContentKey.create(FDContentTypes.CONFIGURED_PRODUCT_GROUP, recipeId);
		ContentNodeI    groupNode = service.createPrototypeContentNode(groupKey);
		List			keyList   = new ArrayList();
		
		// get the list of keys of the existing nodes
		for (Iterator it = nodes.iterator(); it.hasNext();) {
			ContentNodeI node = (ContentNodeI) it.next();
			
			keyList.add(node.getKey());
		}

		// associate the ConfiguredProducts with the group
		groupNode.getAttribute("items").setValue(keyList);
		
		// add the group node to our node list
		nodes.add(groupNode);
		
		return nodes;
	}
	
	/**
	 *  Process a Recipe based on the parsed information.
	 * 
	 *  @return a list of ContentNodeI objects, all the nodes created
	 *          while parsing and processing the input. 
	 *  @throws InvalidContentKeyException 
	 */
	private List processRecipe() throws InvalidContentKeyException {
		ContentNodeI 	recipe;
		ContentNodeI 	variant;
		ContentNodeI 	section;
		ContentKey	 	key;
		String	     	variantId;
		String			id;
		List				keyList;
		List				sectionList;
		
		// create a recipe
		key    = ContentKey.create(FDContentTypes.RECIPE, recipeId);
		recipe = service.createPrototypeContentNode(key);
		
		// add the main variant to the recipe
		variantId = recipeId + "_default";
		key       = ContentKey.create(FDContentTypes.RECIPE_VARIANT, variantId);
		variant   = service.createPrototypeContentNode(key);
		ContentNodeUtil.addRelationshipKey((RelationshipI) recipe.getAttribute("variants"), key);

		
		// list all sections
		sectionList = new ArrayList();
		
		for (Iterator it = sectionNodes.iterator(); it.hasNext();) {
			SectionNode sectionNode = (SectionNode) it.next();
			
			if (!sectionList.contains(sectionNode.section)) {
				sectionList.add(sectionNode.section);
			}
		}

		
		// insert all sections, with the appropriate ingredients
		for (Iterator it = sectionList.iterator(); it.hasNext();) {
			String sectionName = (String) it.next();
			
			// lower case main because of the naming convention for main sections
			id = sectionName.equals("Main")
			   ? variantId + "_main"
			   : variantId + "_" + sectionName;

			// add the section to the main variant
			key     = ContentKey.create(FDContentTypes.RECIPE_SECTION, id);
			section = service.createPrototypeContentNode(key);
			ContentNodeUtil.addRelationshipKey((RelationshipI) variant.getAttribute("sections"), key);
			
			// add all ingredients to the section
			keyList   = new ArrayList();

			// get the list of keys of the sections nodes
			for (Iterator iter = sectionNodes.iterator(); iter.hasNext();) {
				SectionNode sectionNode = (SectionNode) iter.next();
				
				if (sectionNode.section.equals(sectionName)) {
					key = sectionNode.node.getKey();
					
					// if it's a reference to a configured product group
					// remove it from the list of new nodes to insert
					// and update the key to its real value
					ContentKey skuKey = (ContentKey) sectionNode.node.getAttribute("SKU").getValue();
					String     sku    = skuKey.getId();
					if (sku.startsWith("[") && sku.endsWith("]")) {
						nodes.remove(sectionNode.node);
						
						sku = sku.substring(1, sku.length());
						sku = sku.substring(0, sku.length() - 1);
						key = ContentKey.create(FDContentTypes.CONFIGURED_PRODUCT_GROUP, sku);
					}

					keyList.add(key);
				}
			}
			
			// associate the section with the appropriate ingredients
			section.getAttribute("ingredients").setValue(keyList);
			
			// insert the section into the nodes list
			nodes.add(section);
		}
		
		// insert the variant and the recipe into the nodes list
		nodes.add(variant);
		nodes.add(recipe);
		
		
		return nodes;
	}
	
	/**
	 *  Generate a new id for a ConfiguredProduct.
	 * 
	 * @return a new id for a ConfiguredProduct.
	 */
	private String generateId() {
		++idIndex;
		
		return recipeId + "_" + Integer.toString(idIndex);
	}
	
	/**
	 *  Process an input line describing a ConfiguredProduct.
	 *  The format of the input line is a tab-separated list with the following
	 *  values:
	 * <pre><code>
	 *	ORD	
	 *	REQUIRED?
	 *	SECTION
	 *	SKU_CODE
	 *	QUANTITY
	 *	SALES_UNIT
	 *	CONFIGURATION
	 *	DESCRIPTION
	 *	CONFIGURATION_DESC
	 *	MATERIAL_ID
	 *	PRICE
     * </code></pre>
	 * 
	 * @param line the input line to process.
	 * @return the ConfiguredProduct corresponding to the input line.
	 */
	private ContentNodeI processConfiguredProduct(String line) {
		StringTokenizer tokenizer = new StringTokenizer(line, DELIMITERS, true);

		ContentKey		key       = new ContentKey(FDContentTypes.CONFIGURED_PRODUCT, generateId());
		ContentNodeI    node      = service.createPrototypeContentNode(key);
		String			token;
		Boolean			bool;
		Double        	quantity;
		ContentKey		kkey;
		
		// ORDER, disregard
		token = nextToken(tokenizer, DELIMITERS);
		
		// REQUIRED?
		token = nextToken(tokenizer, DELIMITERS);
		bool  = token.equals("YES") ? new Boolean(true) : new Boolean(false);
		node.getAttribute("REQUIRED").setValue(bool);
		
		// SECTION
		token = nextToken(tokenizer, DELIMITERS);
		lastSection = token;
		
		// SKU_CODE	
		token = nextToken(tokenizer, DELIMITERS);
	    kkey  = new ContentKey(FDContentTypes.SKU, token);
		node.getAttribute("SKU").setValue(kkey);
		
		// QUANTITY
		token = nextToken(tokenizer, DELIMITERS);
		if (token.length() > 0) {
			quantity = new Double(token);
			node.getAttribute("QUANTITY").setValue(quantity);
		}
		
		// SALES_UNIT
		token = nextToken(tokenizer, DELIMITERS);
		node.getAttribute("SALES_UNIT").setValue(token);

		// CONFIGURATION, which is actually the options attribute
		token = nextToken(tokenizer, DELIMITERS);
		node.getAttribute("OPTIONS").setValue(token);
		
		// DESCRIPTION, disregard
		token = nextToken(tokenizer, DELIMITERS);
		
		// CONFIGURATION_DESC, disregard
		token = nextToken(tokenizer, DELIMITERS);
		
		// MATERIAL_ID, disregard
		token = nextToken(tokenizer, DELIMITERS);
		
		// PRICE, disregard
		token = nextToken(tokenizer, DELIMITERS);
		
		
		return node;
	}
	
	/**
	 *  Get the next token.
	 *  Returns an empty string of there is no next token.
	 *  If the token is surrounded by quotation marks, those marks are stripped.
	 * 
	 *  @param tokenizer a StringTokenizer that returns the delimiters as well.
	 *  @param delimiters the delimiters of tokenizer
	 *  @return the next token.
	 */
	private String nextToken(StringTokenizer tokenizer,
		                     String          delimiters) {
		if (!tokenizer.hasMoreTokens()) {
			return "";
		}
		
		String token = tokenizer.nextToken();
		
		if (token.length() == 1 && delimiters.indexOf(token.charAt(0)) != -1) {
			// if a delimiter was returned...
			return "";
		}
		
		// disregard the next delimiter
		if (tokenizer.hasMoreTokens()) {
			tokenizer.nextToken();
		}
		
		if (token.startsWith("\"") && token.endsWith("\"")) {
			token = token.substring(1, token.length()-1);
		}
		
		return token;
	}
}
