package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.search.BrandNameExtractor;
import com.freshdirect.cms.search.SearchHit;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.StringUtil;

public class ContentSearchUtil {

	/**
	 * Filter top search hits based on score-groups.
	 * 
	 * @param searchHits List of {@link SearchHit}
	 * @param maxHits maximum number of hits to retain
	 * @param preferredHits preferred number hits to retain
	 * 
	 * @return List of {@link SearchHit}
	 */
	public static List filterTopResults(Collection searchHits, int maxHits, int preferredHits) {
		if (searchHits == null || searchHits.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		List hits = new ArrayList(searchHits.size());
		int hitCount = 0;
		double rollingScore = 0;
		for (Iterator i = searchHits.iterator(); i.hasNext();) {
			SearchHit hit = (SearchHit) i.next();
			hitCount++;
			double score = hit.getScore();
			if (hitCount >= maxHits || (rollingScore != score && hitCount >= preferredHits)) {
				// we're done with a score-group, and we have enough results
				break;
			}
			hits.add(hit);
			rollingScore = score;
		}
		return hits;
	}

	/**
	 * Filter nodes where the full name is an exact match of
	 * the specified term.
	 * 
	 * @param nodes List of {@link ContentNodeModel}
	 * @param term search term
	 * @return List of {@link ContentNodeModel}
	 */
	public static List filterExactNodes(List nodes, String term, SearchQueryStemmer stemmer) {
		List l = new ArrayList();
		for (Iterator i = nodes.iterator(); i.hasNext();) {
			ContentNodeModel node = (ContentNodeModel) i.next();
			String name = NVL.apply(node.getFullName(), "").toLowerCase();
			if (term.equals(name)) {
				l.add(node);
			}
		}
		return l;
	}
	
	private static Set getBrandNames(ContentNodeModel node) {
		BrandNameExtractor extractor = new BrandNameExtractor();
		
		List brandsInName = extractor.extract(NVL.apply(node.getFullName(),"").toLowerCase());
		List brandsInKeywords = extractor.extract(NVL.apply(node.getKeywords(),"").toLowerCase());
		
		Set s = new HashSet(3*(brandsInKeywords.size() + brandsInName.size())/2 +1);
	
		for(Iterator it = brandsInName.iterator(); it.hasNext();) s.add(StringUtil.removeAllWhiteSpace(it.next().toString()));
		for(Iterator it = brandsInKeywords.iterator(); it.hasNext();) s.add(StringUtil.removeAllWhiteSpace(it.next().toString()));
		
		return s;
	}
	
	
	/**
	 * Count how many tokens can be found in the node's description.
	 * 
	 * @param node
	 * @param tokens search term tokens
	 * @param min minimum match required
	 * @param stemmer stemmer to use
	 * @return number of tokens matched, or 0 if tokens matched were less than min
	 */
	private static int countTokens(ContentNodeModel node, String[] tokens, int min, SearchQueryStemmer stemmer) {
		if (min < 0 || min > tokens.length) min = tokens.length;
		
		// add ALL tokens (stemmed) from node to s
		Set s = new HashSet(16);
	
		String[] nameTokens = tokenizeTerm(NVL.apply(node.getFullName(),"").toLowerCase(), " ,'");
		String[] keywordTokens = tokenizeTerm(NVL.apply(node.getKeywords(),"").toLowerCase(),  " ,");
		
		for(int i=0; i < nameTokens.length; ++i) s.add(stemmer.stemToken(nameTokens[i]));
		for(int i=0; i < keywordTokens.length; ++i) s.add(stemmer.stemToken(keywordTokens[i]));
		
		int remainingErrors = tokens.length - min;
		int total = 0;
		
		for(int i = 0; remainingErrors >= 0 && i< tokens.length; ++i) {
			if (s.contains(stemmer.stemToken(tokens[i]))) {
				++total;
			} else {
				--remainingErrors;
			}
		}

		int tokenCount = remainingErrors >= 0 ? total : 0;
				
		return tokenCount;
	}
	
	/**
	 * Only return nodes which have the maximum number of matches from tokens.
	 * @param nodes
	 * @param tokens to match in nodes full name
	 * @return
	 */
	public static List restrictToMaximumOccuringNodes(List nodes, String[] tokens, SearchQueryStemmer stemmer) {
		
		Map candidates = new TreeMap(
			new Comparator() {

				public int compare(Object o1, Object o2) {
					int i1 = ((Integer)o1).intValue();
					int i2 = ((Integer)o2).intValue();
					
					return i1 > i2 ? -1 : +1;
				}
			}
		);
		
		int min = 0;
		for(Iterator i=nodes.iterator(); i.hasNext();) {
			ContentNodeModel node = (ContentNodeModel)i.next();
			int c = countTokens(node, tokens, min, stemmer);
			if (c < min) continue;
			min = c;
			candidates.put(new Integer(c),node);
			
		}
		
		List l = new ArrayList(candidates.size());
		Integer benchMark = null;
		for(Iterator i=candidates.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry)i.next();
			
			if (benchMark == null) benchMark = (Integer)entry.getKey();
			else if (((Integer)entry.getKey()).intValue() < benchMark.intValue()) {
				break;
			}
			
			l.add(entry.getValue());
		}

		return l;
	}

	/**
	 * @param nodes List of {@link ContentNodeModel}
	 * @param tokens
	 * @return List of {@link ContentNodeModel}
	 */
	public static List filterRelevantNodes(List nodes, String[] tokens, SearchQueryStemmer stemmer) {
		List l = new ArrayList(nodes.size());
		for (Iterator i = nodes.iterator(); i.hasNext();) {
			ContentNodeModel node = (ContentNodeModel) i.next();
			if (countTokens(node,tokens,tokens.length,stemmer) == tokens.length) l.add(node);
		}
		return l;
	}

	/**
	 * Resolves search hits into content objects, discarding orphans.
	 * 
	 * @param searchHits List of {@link SearchHit}
	 * @return List of {@link ContentNodeModel}
	 */
	public static List resolveHits(Collection searchHits) {
		if (searchHits == null || searchHits.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		List l = new ArrayList(searchHits.size());
		for (Iterator i = searchHits.iterator(); i.hasNext();) {
			SearchHit hit = (SearchHit) i.next();
			String id = hit.getContentKey().getId();
			ContentNodeModel node = ContentFactory.getInstance().getContentNode(id);
			if (node == null || node.isOrphan()) {
				//LOGGER.debug("ContentFactory.resolveHits() discarding orphan: " + hit.getContentKey());
				continue;
			}
			l.add(node);
		}
		return l;
	}

	/**
	 * Group search hits by content type.
	 * 
	 * @param searchHits List of {@link SearchHit}
	 * @return Map of {@link ContentType} -> List of {@link SearchHit}
	 */
	public static Map mapHitsByType(Collection searchHits) {
		Map m = new HashMap();
		for (Iterator i = searchHits.iterator(); i.hasNext();) {
			SearchHit hit = (SearchHit) i.next();
			ContentType t = hit.getContentKey().getType();
			List l = (List) m.get(t);
			if (l == null) {
				l = new ArrayList();
				m.put(t, l);
			}
			l.add(hit);
		}
		return m;
	}

	/**
	 * Normalize a term to lowercase string containing only letters, digits and
	 * spaces.
	 * 
	 * @return the normalized term
	 */
	public static String normalizeTerm(String term) {
		char[] chrz = new char[term.length()];
		term.getChars(0, chrz.length, chrz, 0);
		StringBuffer buf = new StringBuffer(chrz.length);
		for (int i = 0; i < chrz.length; i++) {
			char c = chrz[i];
			if (Character.isLetterOrDigit(c)) {
				buf.append(c);
			} else {
				buf.append(' ');
			}
		}
	
		return buf.toString().trim().toLowerCase();
	}

	public static String[] tokenizeTerm(String term) {
		List tokens = new ArrayList();
		for (StringTokenizer st = new StringTokenizer(term, " "); st.hasMoreTokens();) {
			String token = st.nextToken();
			if (token.length() > 2) {
				tokens.add(token);
			}
		}
		return (String[]) tokens.toArray(new String[tokens.size()]);
	}

	
	public static String[] tokenizeTerm(String term, String separator) {
            List tokens = new ArrayList();
            for (StringTokenizer st = new StringTokenizer(term, separator); st.hasMoreTokens();) {
                String token = st.nextToken();
                if (token.length() > 2) {
                    tokens.add(token);
                }
            }
            return (String[]) tokens.toArray(new String[tokens.size()]);
        }

	/**
	 * @return false if product is hidden/not searchable/discontinued
	 */
	public static boolean isDisplayable(ProductModel product) {
		
		return !product.isHidden() && product.isSearchable() && !product.isDiscontinued();
	}
	
	/**
	 * This is a destructive operation: the initial list is modified!
	 * 
	 * @param products
	 * @return
	 */
	public static List filterProductsByDisplay(List products) {
		for (Iterator i = products.iterator(); i.hasNext();) {
			ProductModel prod = (ProductModel) i.next();
			if (!isDisplayable(prod)) {
				i.remove();
			}
		}
		return products;
	}
	
	/**
	 * return a list of products, this is not a destructive operation. 
	 * @param products
	 * @param departmentKey the content key of the department node.
	 * @return
	 */
	public static List filterProductsByDepartment(List products, String departmentKey) {
		if (departmentKey==null || products == null) {
			return products;
		}
		List result = new ArrayList(products.size());
		for (Iterator iter = products.iterator(); iter.hasNext();) {
			ProductModel prod = (ProductModel) iter.next();
			if (prod.getDepartment().getContentKey().getId().equals(departmentKey)) {
				result.add(prod);
			}
		}
		return result;
	}
	

	/**
	 * This is a destructive operation: the initial list is modified!
	 * 
	 * @param products
	 * @return
	 */
	public  static List filterCategoriesByVisibility(List categories) {
		for (ListIterator i = categories.listIterator(); i.hasNext();) {
			CategoryModel cat = (CategoryModel) i.next();
			if (cat.isHidden() || !cat.isSearchable()) {
				i.remove();
			}
		}
		return categories;
	}
	
	/**
	 * This is a destructive operation: the initial list is modified!
	 * 
	 * @param products
	 * @return
	 */
	public static List filterRecipesByAvailability(List recipes) {
		for (ListIterator i = recipes.listIterator(); i.hasNext();) {
			Recipe recipe = (Recipe) i.next();
			if (!recipe.isAvailable() || !recipe.isActive()) {
				i.remove();
			}
		}
		
		return recipes;
	}
	
	/**
	 * Calculate individual set properties.
	 * 
	 * The method calculates the cardinalities of 
	 * <ul>
	 *    <li>S1 - S2: elements only in the union of S1
	 *    <li>S2 - S1: elements only in the union of S2
	 *    <li>S1 intersection S2: elements in both S1 and S2
	 * </ul>
	 * 
	 *  The algorithm is relatively efficient; similar to the "merge" phase of merge sort.
	 * 
	 * @param S1 collections that make up S1
	 * @param S2 collections that make up S2
	 * @return the cardinalities S1-S2, S2-S1 and S1 intersection S2
	 */
	public static SearchResults.SpellingResultsDifferences chopSets(Collection[] S1, Collection[] S2) {
		
		int S1MinusS2 = 0;
		int S2MinusS1 = 0;
		int S1AndS2 = 0;
		
		// compare content keys and ContentNodeI's
		Comparator comp = new Comparator() {
			public int compare(Object o1, Object o2) {
				ContentKey k1 = o1 instanceof ContentNodeI ? ((ContentNodeI)o1).getContentKey() : (ContentKey)o1;
				ContentKey k2 = o2 instanceof ContentNodeI ? ((ContentNodeI)o2).getContentKey() : (ContentKey)o2;
				
				// try to be lucky and imagine they have different hashcodes
				if (k1.hashCode() < k2.hashCode()) return -1;
				else if (k1.hashCode() > k2.hashCode()) return +1;
				
				// see if they have different ids
				int byId = k1.getId().compareTo(k2.getId());
				
				// if that does not help compare type name
				return byId == 0 ? k1.getType().getName().compareTo(k2.getType().getName()) : byId;
			}
		};
		
		// sorted set made up by the collections in S1
		TreeSet SS1 = new TreeSet(comp);
		for(int i=0; i< S1.length; SS1.addAll(S1[i++]));
		
		// sorted set made up by the collections in S2
		TreeSet SS2 = new TreeSet(comp);
		for(int i=0; i< S2.length; SS2.addAll(S2[i++]));
		
		Iterator i1 = SS1.iterator(), i2 = SS2.iterator(); // iterators
		ContentKey c1 = i1.hasNext() ? ((ContentNodeI)i1.next()).getContentKey() : null; // head element 1
		ContentKey c2 = i2.hasNext() ? ((ContentNodeI)i2.next()).getContentKey() : null; // head element 2
		
		// step through the steps in parallel; each time processing at least one
		while(c1 != null && c2 != null) {
			int c = comp.compare(c1,c2);
			if (c == 0) ++S1AndS2; // in both
			else if (c < 0) ++S1MinusS2; // in S1 only
			else ++S2MinusS1; // in S2 only
			
			// move pointer(s)
			if (c <= 0) c1 = i1.hasNext() ? ((ContentNodeI)i1.next()).getContentKey() : null;
			if (c >= 0) c2 = i2.hasNext() ? ((ContentNodeI)i2.next()).getContentKey() : null;		
		}
		
		// leftovers
		for(;i1.hasNext();++S1MinusS2,i1.next());
		for(;i2.hasNext();++S2MinusS1,i2.next());
		
		return new SearchResults.SpellingResultsDifferences(S1MinusS2,S2MinusS1,S1AndS2);
	}
}
