package com.freshdirect.cms.core;

import junit.framework.TestCase;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.reverse.BidirectionalReferenceHandler;

public class BidirectionTest extends TestCase {
    BidirectionalReferenceHandler b;

    ContentType stype;
    ContentType dtype;
    ContentKey a1;
    ContentKey a2;
    ContentKey a3;
    ContentKey a4;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        b = new BidirectionalReferenceHandler(null);
        stype = ContentType.get("SourceType");
        dtype = ContentType.get("DestType");
        a1 = ContentKey.create(stype, "ablak");
        a2 = ContentKey.create(dtype, "ajto");
        a3 = ContentKey.create(stype, "alma");
        a4 = ContentKey.create(dtype, "abrak");
    }

    public void testReferences() {
        b.addRelation(a1, a2);
        assertEquals("rel", a2, b.getReference(a1));
        assertEquals("invrel", a1, b.getInverseReference(a2));

        assertNull("rel", b.getReference(a2));
        assertNull("invrel", b.getInverseReference(a1));
        
        b.addRelation(a1, a4);

        assertEquals("rel-x", a4, b.getReference(a1));
        assertEquals("invrel-x", a1, b.getInverseReference(a4));
        assertNull("invrel-x2",  b.getInverseReference(a2));

        b.addRelation(a1, null);
        assertNull("rel", b.getReference(a1));
        assertNull("invrel-x", b.getInverseReference(a4));
        
    }
}
