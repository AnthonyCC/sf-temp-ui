package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Iterator;
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
	public static List<SearchHit> filterTopResults(Collection<SearchHit> searchHits, int maxHits, int preferredHits) {
		if (searchHits == null || searchHits.isEmpty()) {
			return Collections.<SearchHit>emptyList();
		}
		List<SearchHit> hits = new ArrayList<SearchHit>(searchHits.size());
		int hitCount = 0;
		double rollingScore = 0;
		for ( SearchHit hit : searchHits ) {
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
	 * Filter nodes where the full name is an exact match of the specified term.
	 * 
	 * @param nodes
	 * @param term		search term
	 * @return Filtered list
	 */
	public static List<SearchHit> filterExactNodes( List<SearchHit> nodes, String term, SearchQueryStemmer stemmer ) {
		List<SearchHit> l = new ArrayList<SearchHit>();
		for ( SearchHit hit : nodes ) {
			ContentNodeModel node = hit.getNode();
			String name = NVL.apply( node.getFullName(), "" ).toLowerCase();
			if ( term.equals( name ) ) {
				l.add( hit );
			}
		}
		return l;
	}
	
	@SuppressWarnings( "unused" )
	private static Set<String> getBrandNames(ContentNodeModel node) {
		BrandNameExtractor extractor = new BrandNameExtractor();
		
		List<CharSequence> brandsInName = extractor.extract(NVL.apply(node.getFullName(),"").toLowerCase());
		List<CharSequence> brandsInKeywords = extractor.extract(NVL.apply(node.getKeywords(),"").toLowerCase());
		
		Set<String> s = new HashSet<String>(3*(brandsInKeywords.size() + brandsInName.size())/2 +1);
	
		for(CharSequence cs : brandsInName ) 
			s.add(StringUtil.removeAllWhiteSpace(cs.toString()));
		for(CharSequence cs : brandsInKeywords) 
			s.add(StringUtil.removeAllWhiteSpace(cs.toString()));
		
		return s;
	}
	
	
	/**
	 * Count how many tokens can be found in the node's description.
	 * 
	 * @param node
	 * @param tokens search term tokens
	 * @param min minimum match required
	 * @param stemmer stemmer to use
	 * @param fullName TODO
	 * @param keywords TODO
	 * @return number of tokens matched, or 0 if tokens matched were less than min
	 */
	private static int countTokens(String[] tokens, int min, SearchQueryStemmer stemmer, String fullName, String keywords) {
		if (min < 0 || min > tokens.length) {
		    min = tokens.length;
		}
		
		// add ALL tokens (stemmed) from node to s
		Set<String> s = new HashSet<String>(16);
	
		List<String> nameTokens = tokenizeTerm(NVL.apply(fullName,"").toLowerCase(), " ,'");
		List<String> keywordTokens = tokenizeTerm(NVL.apply(keywords,"").toLowerCase(),  " ,");
		
		for(int i=0; i < nameTokens.size(); ++i) {
		    s.add(stemmer.stemToken(nameTokens.get(i)));
		}
		for(int i=0; i < keywordTokens.size(); ++i) { 
		    s.add(stemmer.stemToken(keywordTokens.get(i)));
		}
		
		int remainingErrors = tokens.length - min;
		int total = 0;
		
		for ( int i = 0; remainingErrors >= 0 && i < tokens.length; ++i ) {
			if ( s.contains( stemmer.stemToken( tokens[i] ) ) ) {
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
	 * @param nodes List of {@link SearchHit}
	 * @param tokens to match in nodes full name
	 * @return List of {@link SearchHit}
	 */
	public static List<SearchHit> restrictToMaximumOccuringNodes(List<SearchHit> nodes, String[] tokens, SearchQueryStemmer stemmer) {
		
		Map<Integer, SearchHit> candidates = new TreeMap<Integer, SearchHit>(
			new Comparator<Integer>() {
				public int compare(Integer o1, Integer o2) {
					int i1 = o1.intValue();
					int i2 = o2.intValue();					
					return i1 > i2 ? -1 : +1;
				}
			}
		);
		
		int min = 0;
		for ( SearchHit hit : nodes ) {
			ContentNodeModel node = hit.getNode();
			int c = countTokens(tokens, min, stemmer, node.getFullName(), hit.getKeywords());
			if (c < min) {
			    continue;
			}
			min = c;
			candidates.put(new Integer(c),hit);
		}
		
		List<SearchHit> l = new ArrayList<SearchHit>(candidates.size());
		Integer benchMark = null;
		for ( Map.Entry<Integer,SearchHit> entry : candidates.entrySet() ) {
			if (benchMark == null) {
			    benchMark = entry.getKey();
			} else if (entry.getKey().intValue() < benchMark.intValue()) {
			    break;
			}
			
			l.add(entry.getValue());
		}

		return l;
	}

	/**
	 * @param nodes List of {@link SearchHit}
	 * @param tokens
	 * @return List of {@link SearchHit}
	 */
	public static List<SearchHit> filterRelevantNodes(List<SearchHit> nodes, String[] tokens, SearchQueryStemmer stemmer) {
		List<SearchHit> l = new ArrayList<SearchHit>( nodes.size() );
		for ( SearchHit hit : nodes ) {
			ContentNodeModel node = hit.getNode();
			if ( countTokens( tokens, tokens.length, stemmer, node.getFullName(), hit.getKeywords() ) == tokens.length ) {
				l.add( hit );
			}
		}
		return l;
	}

	/**
	 * Resolves search hits into content objects, discarding orphans.
	 * 
	 * @param searchHits List of {@link SearchHit}
	 * @return List of {@link SearchHit}
	 */
	public static List<SearchHit> resolveHits(Collection<SearchHit> searchHits) {
		if (searchHits == null || searchHits.isEmpty()) {
			return Collections.<SearchHit>emptyList();
		}
		List<SearchHit> l = new ArrayList<SearchHit>(searchHits.size());
		for ( SearchHit hit : searchHits ) {
			ContentNodeModel node = ContentFactory.getInstance().getContentNodeByKey(hit.getContentKey());
			if (node == null || node.isOrphan()) {
				//LOGGER.debug("ContentFactory.resolveHits() discarding orphan: " + hit.getContentKey());
				continue;
			}
			hit.setNode(node);
			l.add(hit);
		}
		return l;
	}

	/**
	 * Group search hits by content type.
	 * 
	 * @param searchHits List of {@link SearchHit}
	 * @return Map of {@link ContentType} -> List of {@link SearchHit}
	 */
	public static Map<ContentType,List<SearchHit>> mapHitsByType(Collection<SearchHit> searchHits) {
		Map<ContentType,List<SearchHit>> m = new HashMap<ContentType,List<SearchHit>>();
		for ( SearchHit hit : searchHits ) {
			ContentType t = hit.getContentKey().getType();
			List<SearchHit> l = m.get(t);
			if (l == null) {
				l = new ArrayList<SearchHit>();
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
		List<String> tokens = new ArrayList<String>();
		int missedTokens = 0;
		for (StringTokenizer st = new StringTokenizer(term, " "); st.hasMoreTokens();) {
			String token = st.nextToken();
			if (token.length() > 2) {
				tokens.add(token);
			} else {
			    missedTokens++;
			}
		}
		if (tokens.size()==0 && missedTokens>1) {
		    // there is no longer than 2 character word in the search term, but at least there are 2 of them, 
		    // try it with without whitespace. The only relevant term for this case is '7 up'.
		    tokens.add(term.replaceAll("\\W", ""));
		}
		return tokens.toArray(new String[tokens.size()]);
	}

	
	public static List<String> tokenizeTerm( String term, String separator ) {
		List<String> tokens = new ArrayList<String>();
		for ( StringTokenizer st = new StringTokenizer( term, separator ); st.hasMoreTokens(); ) {
			String token = st.nextToken();
			if ( token.length() > 1 ) {
				tokens.add( token );
			} else {
				if ( token.length() == 1 && Character.isLetter( token.charAt( 0 ) ) ) {
					tokens.add( token );
				}
			}

		}
		return tokens;
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
	 * @param products List of {@link SearchHit}
	 * @return
	 */
	public static List<SearchHit> filterProductsByDisplay(List<SearchHit> products) {
		for (Iterator<SearchHit> i = products.iterator(); i.hasNext();) {
			ProductModel prod = (ProductModel) i.next().getNode();
			if (!isDisplayable(prod) || prod.getPrimaryHome()==null) {
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
	public static List<ProductModel> filterProductsByDepartment(List<ProductModel> products, String departmentKey) {
		if (departmentKey==null || products == null) {
			return products;
		}
		List<ProductModel> result = new ArrayList<ProductModel>(products.size());
		for ( ProductModel prod : products ) {
			if (prod.getDepartment().getContentKey().getId().equals(departmentKey)) {
				result.add(prod);
			}
		}
		return result;
	}
	

	/**
	 * This is a destructive operation: the initial list is modified!
	 * 
	 * @param products List of {@link SearchHit}
	 * @return List of {@link SearchHit}
	 */
	public static List<SearchHit> filterCategoriesByVisibility( List<SearchHit> categories ) {
		for ( Iterator<SearchHit> i = categories.iterator(); i.hasNext(); ) {
			CategoryModel cat = (CategoryModel)i.next().getNode();
			if ( cat.isHidden() || !cat.isSearchable() ) {
				i.remove();
			}
		}
		return categories;
	}
	
	/**
	 * This is a destructive operation: the initial list is modified!
	 * 
	 * @param recipes List of {@link SearchHit}
	 * @return
	 */
	public static List<SearchHit> filterRecipesByAvailability(List<SearchHit> recipes) {
		for (Iterator<SearchHit> i = recipes.iterator(); i.hasNext();) {
			Recipe recipe = (Recipe) i.next().getNode();
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
	public static SearchResults.SpellingResultsDifferences chopSets(Collection<? extends ContentNodeModel>[] S1, Collection<? extends ContentNodeModel>[] S2) {
		
		int S1MinusS2 = 0;
		int S2MinusS1 = 0;
		int S1AndS2 = 0;
		
		// compare content keys and ContentNodeI's
		final Comparator<ContentKey> keyComp = new Comparator<ContentKey>() {
			public int compare(ContentKey k1, ContentKey k2) {
				// try to be lucky and imagine they have different hashcodes
				if (k1.hashCode() < k2.hashCode()) return -1;
				else if (k1.hashCode() > k2.hashCode()) return +1;
				
				// see if they have different ids
				int byId = k1.getId().compareTo(k2.getId());
				
				// if that does not help compare type name
				return byId == 0 ? k1.getType().getName().compareTo(k2.getType().getName()) : byId;
			}
		};
		final Comparator<ContentNodeModel> nodeComp = new Comparator<ContentNodeModel>() {
			public int compare( ContentNodeModel n1, ContentNodeModel n2 ) {
				return keyComp.compare( n1.getContentKey(), n2.getContentKey() );
			}
		};
		
		// sorted set made up by the collections in S1
		TreeSet<ContentNodeModel> SS1 = new TreeSet<ContentNodeModel>(nodeComp);
		for(int i=0; i< S1.length; SS1.addAll(S1[i++]));
		
		// sorted set made up by the collections in S2
		TreeSet<ContentNodeModel> SS2 = new TreeSet<ContentNodeModel>(nodeComp);
		for(int i=0; i< S2.length; SS2.addAll(S2[i++]));
		
		Iterator<ContentNodeModel> i1 = SS1.iterator(), i2 = SS2.iterator(); // iterators
		ContentKey c1 = i1.hasNext() ? i1.next().getContentKey() : null; // head element 1
		ContentKey c2 = i2.hasNext() ? i2.next().getContentKey() : null; // head element 2
		
		// step through the steps in parallel; each time processing at least one
		while(c1 != null && c2 != null) {
			int c = keyComp.compare(c1,c2);
			if (c == 0) ++S1AndS2; // in both
			else if (c < 0) ++S1MinusS2; // in S1 only
			else ++S2MinusS1; // in S2 only
			
			// move pointer(s)
			if (c <= 0) c1 = i1.hasNext() ? i1.next().getContentKey() : null;
			if (c >= 0) c2 = i2.hasNext() ? i2.next().getContentKey() : null;		
		}
		
		// leftovers
		for(;i1.hasNext();++S1MinusS2,i1.next());
		for(;i2.hasNext();++S2MinusS1,i2.next());
		
		return new SearchResults.SpellingResultsDifferences(S1MinusS2,S2MinusS1,S1AndS2);
	}
	
	public static List<? extends ContentNodeModel> collectFromSearchHits(Collection<SearchHit> searchHits) {
	    if (searchHits==null) {
	        return null;
	    }
	    if (searchHits.isEmpty()) {
	        return Collections.<ContentNodeModel>emptyList();
	    }
	    List<ContentNodeModel> result = new ArrayList<ContentNodeModel>(searchHits.size());
	    for ( SearchHit sh : searchHits ) {
	        result.add(sh.getNode());
	    }
	    return result;
	}
	
	public static Map<ContentKey,SearchHit> toMap(Collection<SearchHit> hits) {
	    Map<ContentKey,SearchHit> result = new HashMap<ContentKey,SearchHit> ();
	    if (hits!=null) {
	        for (SearchHit h : hits) {
	            result.put(h.getContentKey(), h);
	        }
	    }
	    return result;
	}
	
}
