package com.freshdirect.crm.ejb;

import com.freshdirect.framework.core.PrimaryKey;

import junit.framework.TestCase;

public class CriteriaBuilderTestCase extends TestCase {

	private CriteriaBuilder cb;

	public CriteriaBuilderTestCase(String name) {
		super(name);
	}

	public void setUp() {
		this.cb = new CriteriaBuilder();
	}

	public void testAddString() {
		cb.addString("FOO", "foo");
		cb.addString("BAR", null);
		cb.addString("BAZ", "baz");

		assertEquals("FOO=? AND BAZ=?", cb.getCriteria());
		Object[] p = cb.getParams();
		assertEquals(2, p.length);
		assertEquals("foo", p[0]);
		assertEquals("baz", p[1]);
	}

	public void testAddPK() {
		cb.addPK("FOO", new PrimaryKey("foo"));
		cb.addPK("BAR", null);
		cb.addPK("BAZ", new PrimaryKey("baz"));

		assertEquals("FOO=? AND BAZ=?", cb.getCriteria());
		Object[] p = cb.getParams();
		assertEquals(2, p.length);
		assertEquals("foo", p[0]);
		assertEquals("baz", p[1]);
	}

	public void testAddSql() {
		cb.addSql("foo like ?", new Object[] { "%foo%" });
		cb.addSql("bar like ?", new Object[] { "%bar%" });

		assertEquals("foo like ? AND bar like ?", cb.getCriteria());
		Object[] p = cb.getParams();
		assertEquals(2, p.length);
		assertEquals("%foo%", p[0]);
		assertEquals("%bar%", p[1]);
	}

	public void testAddInStringOne() {
		cb.addInString("foo", new String[] { "bar" });

		assertEquals("foo=?", cb.getCriteria());
		Object[] p = cb.getParams();
		assertEquals(1, p.length);
		assertEquals("bar", p[0]);
	}

	public void testAddInStringMultiple() {
		cb.addInString("foo", new String[] { "bar", "baz" });

		assertEquals("foo IN (?,?)", cb.getCriteria());
		Object[] p = cb.getParams();
		assertEquals(2, p.length);
		assertEquals("bar", p[0]);
		assertEquals("baz", p[1]);
	}

}
