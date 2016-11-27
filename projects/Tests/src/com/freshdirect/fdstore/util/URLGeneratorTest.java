package com.freshdirect.fdstore.util;

import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.TestCase;

public class URLGeneratorTest extends TestCase {
    
    public void testBasic() {
        Map m = new LinkedHashMap();
        m.put("param1" , "value1");
        m.put("param2" , "value2");
        URLGenerator u = new URLGenerator("/test", m);
        
        assertEquals("base","/test?param1=value1&amp;param2=value2", u.build());
        u.set("param3", "value3");
        assertEquals("base-new-param","/test?param1=value1&amp;param2=value2&amp;param3=value3", u.build());
        u.reset();
        assertEquals("base-reset","/test?param1=value1&amp;param2=value2", u.build());

        u.set("param2", "NEW_VALUE");
        assertEquals("base-overwrite","/test?param1=value1&amp;param2=NEW_VALUE", u.build());
        u.remove("param2");
        assertEquals("base-null-out","/test?param1=value1", u.build());
        u.remove("param1");
        assertEquals("base-empty","/test", u.build());
        u.set("param1", "SOMETHING");
        assertEquals("base-empty+","/test?param1=SOMETHING", u.build());
        u.set("paramX", "SOMETHING");
        assertEquals("base-empty+","/test?param1=SOMETHING&amp;paramX=SOMETHING", u.build());
        
    }

}
