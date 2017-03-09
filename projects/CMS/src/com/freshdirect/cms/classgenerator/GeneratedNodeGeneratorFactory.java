package com.freshdirect.cms.classgenerator;

import java.util.HashMap;
import java.util.Map;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.application.ContentTypeServiceI;

/**
 * A simple factory to provide class generator facility
 * 
 * @author segabor
 *
 */
public class GeneratedNodeGeneratorFactory {

    private static final GeneratedNodeGeneratorFactory INSTANCE = new GeneratedNodeGeneratorFactory();

    private static final Map<String, ContentNodeGenerator> POOL = new HashMap<String, ContentNodeGenerator>();

    private GeneratedNodeGeneratorFactory() {
    }

    public static GeneratedNodeGeneratorFactory getInstance() {
        return INSTANCE;
    }

    public ContentNodeGenerator getNodeGenerator(ContentTypeServiceI typeService, String prefix) {

        if (typeService == null) {
            throw new CmsRuntimeException("No type service was given!");
        }

        if (prefix == null) {
            prefix = ContentNodeGenerator.DEFAULT_PREFIX;
        }

        ContentNodeGenerator generator = null;
        synchronized(POOL) {
            generator = POOL.get(prefix);
            if (generator == null) {
                generator = new ContentNodeGenerator(typeService, prefix);
                
                POOL.put(prefix, generator);
            }
        }
        return generator;
    }
}
