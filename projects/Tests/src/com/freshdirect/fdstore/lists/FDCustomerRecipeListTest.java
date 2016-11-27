package com.freshdirect.fdstore.lists;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import com.freshdirect.fdstore.lists.FDCustomerRecipeList;
import com.freshdirect.fdstore.lists.FDCustomerRecipeListLineItem;
import com.freshdirect.framework.core.PrimaryKey;

public class FDCustomerRecipeListTest extends TestCase {

	public void testMergeRecipe() {
		FDCustomerRecipeList list = new FDCustomerRecipeList(new PrimaryKey("foo"), "foo");
		list.mergeRecipe("rec_foo", false);
		list.mergeRecipe("rec_bar", false);
		list.mergeRecipe("rec_foo", true);
		list.mergeRecipe("rec_bar", true);
		list.mergeRecipe("rec_baz", false);
		list.mergeRecipe("rec_foo", false);
		list.mergeRecipe("rec_bar", false);

		List items = list.getLineItems();
		assertEquals(3, items.size());
		for (Iterator i = items.iterator(); i.hasNext(); ) {
			FDCustomerRecipeListLineItem item = (FDCustomerRecipeListLineItem) i.next();
			if ("rec_foo".equals(item.getRecipeId())) {
				assertEquals(2, item.getFrequency());
			} else if ("rec_bar".equals(item.getRecipeId())) {
				assertEquals(2, item.getFrequency());
			} else if ("rec_baz".equals(item.getRecipeId())) {
				assertEquals(1, item.getFrequency());
			}
		}
	}

}
