package com.freshdirect.unbxd.analytics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.freshdirect.webapp.unbxdanalytics.visitor.VisitType;
import com.freshdirect.webapp.unbxdanalytics.visitor.VisitTypeCache;
import com.freshdirect.webapp.unbxdanalytics.visitor.Visitor;


public class VisitTypeTest {

    @Test
    public void testFirstVisitor() {
    	final Visitor visitor = new Visitor("test", new VisitType(false, false));
        
        final VisitType visitType = VisitTypeCache.getInstance().createVisitType("test");
        
        assertFalse(visitor.isRepeat());
        assertEquals(visitor.getVisitType(), VisitType.VISITOR_TYPE_VALUE_FIRST.toString());
        assertEquals(visitType.toString(), VisitType.VISITOR_TYPE_VALUE_FIRST.toString());
    }


    @Test
    public void testRepeatVisitor() {
    	final Visitor visitor = new Visitor("test", new VisitType(true, false));
    	
    	assertTrue(visitor.isRepeat());
        assertEquals(visitor.getVisitType(), VisitType.VISITOR_TYPE_VALUE_REPEAT.toString());
    }

}
