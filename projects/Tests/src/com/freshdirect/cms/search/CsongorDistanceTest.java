package com.freshdirect.cms.search;

import com.freshdirect.cms.search.spell.CsongorDistance;
import com.freshdirect.cms.search.spell.StringDistance;

import junit.framework.TestCase;

public class CsongorDistanceTest extends TestCase {
	StringDistance sd;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		sd = new CsongorDistance();
	}
	
	public void assertDistance(String s1, String s2, int d) {
		System.out.println(s1 + "/" + s2 + ": " + sd.getDistance(s1, s2));
		assertEquals(d, sd.getDistance(s1, s2));
	}
	
	public void assertCommutativity(String s1, String s2) {
		System.out.println(s2 + "/" + s1 + ": " + sd.getDistance(s2, s1));
		assertEquals(sd.getDistance(s1, s2), sd.getDistance(s2, s1));
	}
	
	public void test1() {
		assertDistance("cheese", "chese", 1);
		assertCommutativity("cheese", "chese");
		assertDistance("cheese", "chest", 2);
		assertCommutativity("cheese", "chest");
		assertDistance("cheese", "chess", 2);
		assertCommutativity("cheese", "chess");
		assertDistance("cheese", "che-se", 1);
		assertCommutativity("cheese", "che-se");
		assertDistance("cheese", "ches'e", 1);
		assertCommutativity("cheese", "ches'e");
		assertDistance("cheese", "cheeese", 1);
		assertCommutativity("cheese", "cheeese");
		assertDistance("cheese", "chelvse", 2);
		assertCommutativity("cheese", "chelvse");
		assertDistance("cheese", "chhhese", 3);
		assertCommutativity("cheese", "chhhese");
		assertDistance("cheese", "chhheese", 2);
		assertCommutativity("cheese", "chhheese");
		assertDistance("cheese", "chee'se", 0);
		assertCommutativity("cheese", "chee'se");
		assertDistance("cheese", "cheesse", 1);
		assertCommutativity("cheese", "cheesse");
		assertDistance("cheese", "chease", 1);
		assertCommutativity("cheese", "chease");
		assertDistance("cheese", "cheasse", 2);
		assertCommutativity("cheese", "cheasse");
		assertDistance("cheese", "chsee", 3);
		assertCommutativity("cheese", "chsee");
	}
	
	public void test2() {
		assertDistance("banana", "ba nana", 1);
		assertCommutativity("banana", "ba nana");
		assertDistance("banana", "ba nna", 2);
		assertCommutativity("banana", "ba nna");
		assertDistance("banana", "baanna", 1);
		assertCommutativity("banana", "baanna");
		assertDistance("banana", "banna", 1);
		assertCommutativity("banana", "banna");
		assertDistance("banana", "ban ana", 1);
		assertCommutativity("banana", "ban ana");
		assertDistance("banana", "ban nana", 2);
		assertCommutativity("banana", "ban nana");
		assertDistance("banana", "ban an", 2);
		assertCommutativity("banana", "ban an");
		assertDistance("ba nana", "bananas", 2);
		assertCommutativity("ba nana", "bananas");
		assertDistance("ba nana", "a and", 3);
		assertCommutativity("ba nana", "a and");
	}
	
	public void test3() {
		assertDistance("ahser", "asher", 1);
		assertCommutativity("ahser", "ahser");
		assertDistance("ahser", "asher's", 2);
		assertCommutativity("ahser", "ahser's");
		assertDistance("asher", "ahser's", 2);
		assertCommutativity("asher", "asher's");
		assertDistance("shake", "shaek", 1);
		assertCommutativity("shake", "shaek");
		assertDistance("shakes", "shaek", 2);
		assertCommutativity("shakes", "shaek");
	}

	public void test4() {
		assertDistance("bear", "beer", 1);
		assertCommutativity("bear", "beer");
		assertDistance("bear", "beef", 4);
		assertCommutativity("bear", "beef");
		assertDistance("bear", "bean", 1);
		assertCommutativity("bear", "bean");
	}

	public void test5() {
		assertDistance("lemon", "lenom", 2);
		assertCommutativity("lemon", "lenom");
		assertDistance("lemon juice", "lenom juice", 2);
		assertCommutativity("lemon juice", "lenom juice");

		assertDistance("melon", "nelom", 2);
		assertCommutativity("melon", "nelom");
		assertDistance("melon flavour", "nelom flavor", 3);
		assertCommutativity("melon flavour", "nelom flavor");
		assertDistance("greek melon", "greek nelom", 2);
		assertCommutativity("greek melon", "greek nelom");
	}
	
	public void test6() {
		assertDistance("pregnan", "german", 3);
		assertCommutativity("pregnan", "german");
		assertDistance("salmon", "slamon", 1);
		assertCommutativity("salmon", "slamon");
		assertDistance("salomon", "slamon", 2);
		assertCommutativity("salomon", "slamon");
		assertDistance("salad", "salmo", 4);
		assertCommutativity("salad", "salmo");
		assertDistance("strawberry", "stwbrry", 3);
	}
	
	public void test7() {
		assertDistance("degli", "deil", 2);
		assertCommutativity("degli", "deil");
		assertDistance("deli", "deil", 1);
		assertCommutativity("deli", "deil");
	}
}
