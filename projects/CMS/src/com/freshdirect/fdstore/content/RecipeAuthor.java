package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;

public class RecipeAuthor extends ContentNodeModelImpl {

	public final static Comparator<RecipeAuthor> NAME_COMPARATOR = new Comparator<RecipeAuthor>() {
		public int compare(RecipeAuthor r1, RecipeAuthor r2) {
			int diffSurname   = r1.getSurname().compareTo(r2.getSurname());
			int diffFirstName = r1.getFirstName().compareTo(r2.getFirstName());
			return diffSurname == 0 ? diffFirstName : diffSurname;
		}
	};

	public RecipeAuthor(ContentKey cKey) {
		super(cKey);
	}

	/**
	 * Find all authors of all available RecipeSources.
	 * 
	 * @return List of RecipeAuthor, sorted by name
	 */
	public static List<RecipeAuthor> findAllAvailable() {
		List<RecipeSource> sources = RecipeSource.findAllAvailable();
		Set<RecipeAuthor> authors = new HashSet<RecipeAuthor>();
		for ( RecipeSource rs : sources ) {
			authors.addAll(rs.getAuthors());
		}
		List<RecipeAuthor> l = new ArrayList<RecipeAuthor>(authors);
		Collections.sort(l, NAME_COMPARATOR);
		return l;
	}
	
	/**
	 * Concatenate list of authors into a human-readable string, prefixed with
	 * "by ".
	 * 
	 * @param authors
	 *            List of {@link RecipeAuthor}
	 * @return String (never null)
	 */
	public static String authorsToString(List<RecipeAuthor> authors) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < authors.size(); i++) {
			RecipeAuthor author = authors.get(i);
			
			if (i == 0) {
				sb.append("by ");
			} 

			if (i == authors.size() - 1 && authors.size() > 1) {
				sb.append(" and ");
			} else if (i > 0 ){
				sb.append(", ");
			}

			sb.append(author.getName());
		}
		return sb.toString();
	}
	
	public String getFirstName() {
		return getAttribute("firstName", "");
	}
	
	public String getSurname() {
		return getAttribute("name", "");
	}

	public String getName() {
		String firstName = getAttribute("firstName", null);
		
		return firstName == null
		       ? getAttribute("name", "")
		       : firstName + " " + getAttribute("name", "");
	}
	
	/**
	 *  Return the author's name in "Lastname, Firstname"
	 *  form
	 *  
	 *  @return the author's name in "Lastname, Firstname" form.
	 */
	public String getNameLastFirst() {
		String firstName = getAttribute("firstName", null);
		
		return firstName == null
		       ? getAttribute("name", "")
		       : getAttribute("name", "") + ", " + firstName;
	}
	
	public String getBio() {
		return getAttribute("bio", "");
	}

}
