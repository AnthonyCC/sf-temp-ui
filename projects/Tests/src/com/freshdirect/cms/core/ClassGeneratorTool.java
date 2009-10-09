package com.freshdirect.cms.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javassist.CannotCompileException;

import org.apache.log4j.Logger;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.classgenerator.ClassGeneratorContentService;
import com.freshdirect.cms.classgenerator.ContentNodeGenerator;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.node.ContentNode;

public class ClassGeneratorTool {
    private static final Logger LOG = Logger.getLogger(ClassGeneratorTool.class);

    /**
     * @param args
     * @throws IOException
     * @throws CannotCompileException
     */
    public static void main(String[] args) throws IOException {
        LOG.info("init...");
        /*Random rnd = new Random();
        XmlContentService s3 = new ClassGeneratorContentService("t3_" + Math.abs(rnd.nextInt(10000)),
                new XmlTypeService("classpath:/com/freshdirect/cms/application/service/TestDef3.xml"), new FlexContentHandler(),
                "classpath:/com/freshdirect/cms/application/service/TestContent3.xml");
*/
        
        baseTest();
/*
        ContentNodeGenerator.GETATTRIBUTE_MODE = 0;
        double rate0 = benchmark();
        ContentNodeGenerator.GETATTRIBUTE_MODE = 1;
        double rate1 = benchmark();
        ContentNodeGenerator.GETATTRIBUTE_MODE = 2;
        double rate2 = benchmark();
        
        System.out.println("MODE SWITCH : "+rate0);
        System.out.println("MODE BINARY : "+rate1);
        System.out.println("MODE HASHMAP: "+rate2);
  */      
        //System.in.read();
    }

    private static double benchmark() {
        Random rnd = new Random();
        XmlContentService s3 = new ClassGeneratorContentService("t3_" + Math.abs(rnd.nextInt(10000)),
                new XmlTypeService("classpath:/com/freshdirect/cms/application/service/TestDef3.xml"), new FlexContentHandler(),
                "classpath:/com/freshdirect/cms/application/service/TestContent3.xml");

        XmlContentService s1 = new XmlContentService(new XmlTypeService("classpath:/com/freshdirect/cms/application/service/TestDef3.xml"),
                new FlexContentHandler(), "classpath:/com/freshdirect/cms/application/service/TestContent3.xml");

        List list = new ArrayList();
        list.add(new XmlTypeService("classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));

        CompositeTypeService typeService = new CompositeTypeService(list);

        XmlContentService service = new XmlContentService(typeService, new FlexContentHandler(),
                "classpath:/com/freshdirect/cms/fdstore/content/FilteredStore2.xml");
        XmlContentService service2 = new ClassGeneratorContentService("t1_" + Math.abs(rnd.nextInt(10000)), typeService, new FlexContentHandler(),
                "classpath:/com/freshdirect/cms/fdstore/content/FilteredStore2.xml");

        int M = 10;
        
        benchmark(s1, 100000*M, new ContentKey(ContentType.get("Foo"), "fooNode"));
        benchmark(s3, 100000*M, new ContentKey(ContentType.get("Foo"), "fooNode"));

        long a = 0,b=0;
        for (int i=0;i<M;i++) {
            a+= benchmark(service, 100000, new ContentKey(FDContentTypes.PRODUCT, "dai_organi_2_milk_02"));
            b+= benchmark(service2, 100000, new ContentKey(FDContentTypes.PRODUCT, "dai_organi_2_milk_02"));
        }
        System.out.println("AVER:"+ (a/M)+"\t"+service.getClass().getName() + " -> 100");
        double rate = ((double)b/(double)a)*100;
        System.out.println("AVER:"+ (b/M)+"\t"+service2.getClass().getName() + " -> "+rate);
        return rate;
    }

    private static void baseTest() {
        ContentTypeServiceI contentService = ContentFilterTool.createTypeService();
        LOG.info("content service inited.");
        ContentNodeGenerator c = new ContentNodeGenerator(contentService);

        {
            ContentNodeI node = c.createNode(new ContentKey(FDContentTypes.PRODUCT, "prod"));
            LOG.info("node created:" + node);
            LOG.info("definition: " + node.getDefinition());
            LOG.info("key: " + node.getKey());
            LOG.info("getAttribute:" + node.getAttribute("FULL_NAME"));
            LOG.info("child keys:" + node.getChildKeys());

            AttributeI attribute = node.getAttribute("FULL_NAME");
            attribute.setValue("Hello");
            AttributeI layout = node.getAttribute("LAYOUT");
            layout.setValue(new Integer(5));

            AttributeI brands = node.getAttribute("brands");
            brands.setValue(new ArrayList());
            LOG.info("node FULL_NAME:" + node.getAttribute("FULL_NAME").getValue());
            LOG.info("node LAYOUT:" + node.getAttribute("LAYOUT").getValue());
            LOG.info("node brands:" + node.getAttribute("brands").getValue());

            
            LOG.info("node FULL_NAME:" + node.getAttributeValue("FULL_NAME"));
            LOG.info("node LAYOUT:" + node.getAttributeValue("LAYOUT"));
            LOG.info("node brands:" + node.getAttributeValue("brands"));
            
            ContentNodeI copy = node.copy();
            LOG.info("copy FULL_NAME:" + copy.getAttribute("FULL_NAME").getValue());
            LOG.info("copy LAYOUT:" + copy.getAttribute("LAYOUT").getValue());
            LOG.info("copy brands:" + copy.getAttribute("brands").getValue());
            LOG.info("copy key:" + copy.getKey());

            LOG.info("node FULL_NAME:" + copy.getAttributeValue("FULL_NAME"));
            LOG.info("node LAYOUT:" + copy.getAttributeValue("LAYOUT"));
            LOG.info("node brands:" + copy.getAttributeValue("brands"));
            
            node.setAttributeValue("FULL_NAME", "new name");
            node.setAttributeValue("LAYOUT", Integer.valueOf(1));
            
            LOG.info("node FULL_NAME:" + node.getAttributeValue("FULL_NAME"));
            LOG.info("node LAYOUT:" + node.getAttributeValue("LAYOUT"));
            
        }
        {
            ContentNodeI node = c.createNode(new ContentKey(FDContentTypes.IMAGE, "image"));
            LOG.info("node created:" + node);
            LOG.info("definition: " + node.getDefinition());
            LOG.info("key: " + node.getKey());
            LOG.info("getAttribute:" + node.getAttribute("path"));
            LOG.info("child keys:" + node.getChildKeys());

            /*
             * AttributeI attribute = node.getAttribute("path");
             * attribute.setValue("Hello"); AttributeI layout =
             * node.getAttribute("width"); layout.setValue(new Integer(5));
             * 
             * LOG.info("node path:" + node.getAttribute("path").getValue());
             * LOG.info("node width:" + node.getAttribute("width").getValue());
             */
        }
        {
            ContentNodeI node = c.createNode(new ContentKey(FDContentTypes.DEPARTMENT, "depart"));
            LOG.info("node created:" + node);
            LOG.info("definition: " + node.getDefinition());
            LOG.info("key: " + node.getKey());
            LOG.info("getAttribute:" + node.getAttribute("categories"));
            node.getAttribute("categories").setValue(new ArrayList());
            LOG.info("getAttribute after set value:" + node.getAttribute("categories"));
            
            LOG.info("child keys:" + node.getChildKeys());
            
            for (Iterator iter = node.getDefinition().getAttributeNames().iterator();iter.hasNext();) {
                String name = (String) iter.next();
                AttributeI attribute = node.getAttribute(name);
                LOG.info("attrib["+name+"] = "+attribute);
                if (attribute==null) {
                    throw new RuntimeException("attribute not found ! : "+name+ " IN "+node.getDefinition().getName());
                }
                if (!attribute.getName().equals(name)) {
                    throw new RuntimeException("attribute NAME not match! : " + name + "<> " + attribute.getName() + " IN " + node.getDefinition().getName());
                }
            }
            
        }
        
        LOG.info("end.");
    }

    public static long benchmark(ContentServiceI service, int count, ContentKey contentKey) {
        ContentNodeI node = service.getContentNode(contentKey);
        Set names = node.getDefinition().getAttributeNames();
        long time = System.currentTimeMillis();
        int inc = 0;
        for (int i = 0; i < count; i++) {
            for (Iterator iter = names.iterator(); iter.hasNext();) {
                String name = (String) iter.next();

                Object value = node.getAttribute(name).getValue();
                if (value instanceof String) {
                    inc ++;
                    node.getAttribute(name).setValue("value " + i);
                }
            }
        }

        long etime = System.currentTimeMillis() - time;
        LOG.warn("benchmark of " + service.getClass().getName() + "\tall:" + etime + " ms [set called:"+inc+"]");
        return etime;
    }

}
