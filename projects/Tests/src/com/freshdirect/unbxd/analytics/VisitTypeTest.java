package com.freshdirect.unbxd.analytics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.freshdirect.webapp.unbxdanalytics.visitor.VisitTypeCache;
import com.freshdirect.webapp.unbxdanalytics.visitor.Visitor;


public class VisitTypeTest {

    @Test
    public void testNewVisitor() {
        Visitor visitor = new Visitor("1234");
        visitor.setRepeat(false);
        
        final boolean sendEvent = VisitTypeCache.doUpdateVisitType(visitor, System.currentTimeMillis(), null);
        
        assertTrue(sendEvent);
        assertFalse(visitor.isRepeat());
        assertEquals(visitor.getVisitType(), Visitor.VISITOR_TYPE_VALUE_FIRST );
    }


    @Test
    public void testVisitorFirstCheck() {
        Visitor visitor = new Visitor("1234");
        visitor.setRepeat(false);
        
        final long lastCheck = 0L;
        final long now = lastCheck+(VisitTypeCache.TIMEOUT/2);
        
        final boolean sendEvent = VisitTypeCache.doUpdateVisitType(visitor, now, Long.valueOf(lastCheck));
        
        assertFalse(sendEvent);
        assertTrue(visitor.isRepeat());
        assertEquals(visitor.getVisitType(), Visitor.VISITOR_TYPE_VALUE_REPEAT );
    }


    @Test
    public void testVisitorTimeoutExpired() {
        Visitor visitor = new Visitor("1234");
        visitor.setRepeat(false);
        
        final long lastCheck = 0L;
        final long now = lastCheck+VisitTypeCache.TIMEOUT;
        
        final boolean sendEvent = VisitTypeCache.doUpdateVisitType(visitor, now, Long.valueOf(lastCheck));

        assertTrue(sendEvent);
        assertTrue(visitor.isRepeat());
        assertEquals(visitor.getVisitType(), Visitor.VISITOR_TYPE_VALUE_REPEAT );
    }
}
