package com.freshdirect.cms.classgenerator;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

import org.apache.log4j.Logger;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.BidirectionalRelationshipDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.EnumDefI;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.RelationshipI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.reverse.BidirectionalReferenceHandler;

public class ContentNodeGenerator implements NodeGeneratorI {

    private final static boolean DEBUG = false;
    private final static boolean UNSAFE = true;
    
    private final static int SWITCH = 0;
    private final static int BINARY_SEARCH = 1;
    private final static int HASHMAP_SEARCH = 2;
    private final static int PREFIX_SEARCH = 3;
    private final static boolean PREFIX_DEBUG = true;
    
    public static int GETATTRIBUTE_MODE = SWITCH;
    
    /**
     * This flag enables specific optimization, if set to true, memory consumption is greatly reduced, but 
     * in the getAttribute call, every time a new Attribute object have to be created. 
     *  For now, we don't enable this. 
     */
    boolean lazy = true; 
    
    boolean debugGetAttributeCalls = false;
    
    private static final Logger LOG             = Logger.getLogger(ContentNodeGenerator.class);

    /**
     * Map<ContentType,Class>
     */
    Map                         contentTypeMap  = new HashMap();

    /**
     * Map<EnumAttributeType,CtClass>
     * 
     */
    final Map                   fieldTypes      = new HashMap();

    /**
     * Map<ContentType,Map<String,AttributeDef>>
     */
    final Map                   attributeDefMap = new HashMap();

    private ContentTypeServiceI typeService;
    private ClassPool           pool;
    
    private String              packageName;
    
    interface ClassInitializer { 
        void initClass (GeneratedContentNode instance, ContentType type);
    }

    class AttributeMapClassInitializer implements ClassInitializer { 
        Map attributeMap;
        public AttributeMapClassInitializer(Map attributeMap) {
            this.attributeMap = attributeMap;
        }
        public void initClass(GeneratedContentNode node, ContentType arg1) {
            node.setAttributeDefs(attributeMap);
        }
    }

    class OrdinalHashClassInitializer implements ClassInitializer { 
        Map ordinalHash;
        public OrdinalHashClassInitializer(Map ordinalHash) {
            this.ordinalHash = ordinalHash;
        }
        public void initClass(GeneratedContentNode node, ContentType arg1) {
            node.setAttributeOrdinalHash(ordinalHash);
        }
    }
    
    class ClassInfo { 
        Class clazz;
        List classInitializers = new ArrayList();
        Map attributeMap;
        String implementationName;
        
        public String getImplementationName() {
            return implementationName;
        }
        
        public void setImplementationName(String implementationName) {
            this.implementationName = implementationName;
        }
        
        public void setGeneratedClass(Class clazz) {
            this.clazz = clazz;
        }

        public Class getGeneratedClazz() {
            return clazz;
        }
        public Map getAttributeMap() {
            return attributeMap;
        }
        
        public AttributeDefI getAttributeDef(String name) {
            return (AttributeDefI) attributeMap.get(name);
        }
        
        public void setAttributeMap(Map attributeMap) {
            this.attributeMap = attributeMap;
        }
        
        public void add(ClassInitializer cls) {
            this.classInitializers.add(cls);
        }
        
        public List getClassInitializers() {
            return classInitializers;
        }
    }

    static class AttributeRec { 
        int hash;
        String name;
        AttributeDefI def;
        
        public AttributeRec(String name, AttributeDefI def) {
            super();
            this.name = name;
            this.hash = name.hashCode();
            this.def = def;
        }
        
        public int getHash() {
            return hash;
        }
        
        public String getName() {
            return name;
        }
        
        public AttributeDefI getDef() {
            return def;
        }
        
        public String toString() {
            return name + ':'+ hash;
        }
    }
    
    class AttributeRecHashComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            AttributeRec a1 = (AttributeRec) o1;
            AttributeRec a2 = (AttributeRec) o2;
            return a1.hash == a2.hash ? 0 : a1.hash > a2.hash ? -1 : 1;
        }
    }
    
    class AttributeRecNameComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            AttributeRec a1 = (AttributeRec) o1;
            AttributeRec a2 = (AttributeRec) o2;
            return a2.name.compareTo(a1.name);
        }
    }

    /**
     * 
     * @param service
     */
    public ContentNodeGenerator(ContentTypeServiceI service) {
        this(service, "");
    }

    /**
     * 
     * @param service
     * @param prefix
     */
    public ContentNodeGenerator(ContentTypeServiceI service, String prefix) {
        this.typeService = service;
        if (prefix==null) {
            prefix = "";
        }
        if (!prefix.endsWith(".") && prefix.length()>0) {
            prefix = prefix + '.';
        }
        this.packageName = "com.freshdirect.cms.classgenerator.gen." + prefix;
        //this.pool = ClassPool.getDefault();
        ClassPool parent = ClassPool.getDefault();
        this.pool = new ClassPool(parent);
        this.pool.insertClassPath(new ClassClassPath(ContentNodeGenerator.class));

        try {
            fieldTypes.put(EnumAttributeType.STRING, pool.get("java.lang.String"));
            fieldTypes.put(EnumAttributeType.LONG_TEXT, pool.get("java.lang.String"));
            fieldTypes.put(EnumAttributeType.BOOLEAN, pool.get("java.lang.Boolean"));
            fieldTypes.put(EnumAttributeType.INTEGER, pool.get("java.lang.Integer"));
            fieldTypes.put(EnumAttributeType.DOUBLE, pool.get("java.lang.Double"));
            fieldTypes.put(EnumAttributeType.DATE, pool.get("java.util.Date"));
            // fieldTypes.put(EnumAttributeType.RELATIONSHIP,
            // pool.get("java.util.Date"));
        } catch (NotFoundException e) {
            throw new ClassGeneratorException("ContentNodeGenerator" + e.getMessage(), e);
        }
        createTypes();
    }

    private void createTypes() {
        Collection contentTypeDefinitions = typeService.getContentTypeDefinitions();
        for (Iterator iter = contentTypeDefinitions.iterator(); iter.hasNext();) {
            ContentTypeDefI object = (ContentTypeDefI) iter.next();
            try {
                initContentType(object);
            } catch (CannotCompileException e) {
                throw new ClassGeneratorException("Cannot compile " + object.getType() + ": " + e.getMessage(), e);
            } catch (NotFoundException e) {
                throw new ClassGeneratorException("Cannot compile " + object.getType() + ": " + e.getMessage(), e);
            }
        }
        for (Iterator iter = contentTypeDefinitions.iterator(); iter.hasNext();) {
            ContentTypeDefI object = (ContentTypeDefI) iter.next();
            try {
                ClassInfo info = createContentType(object);

                contentTypeMap.put(object.getType(), info.getGeneratedClazz());

                GeneratedContentNode node = createNodeImpl(object.getType());
                node.setContentNodeGenerator(this);
                for (Iterator citer = info.getClassInitializers().iterator();citer.hasNext();) {
                    ClassInitializer ci = (ClassInitializer) citer.next();
                    ci.initClass(node, object.getType());
                }

            } catch (CannotCompileException e) {
                throw new ClassGeneratorException("Cannot compile " + object.getType() + ": " + e.getMessage(), e);
            } catch (NotFoundException e) {
                throw new ClassGeneratorException("Cannot compile " + object.getType() + ": " + e.getMessage(), e);
            }
        }
    }

    Map<String, AttributeDefI> createAttributeMap(ContentTypeDefI def) {
        Set<String> attributeNames = def.getAttributeNames();
        Map<String, AttributeDefI> result = new TreeMap<String, AttributeDefI>();
        for (String name : attributeNames) {
            AttributeDefI attributeDef = def.getAttributeDef(name);
            result.put(name, attributeDef);
        }
        return result;
    }

    GeneratedContentNode createNodeImpl(ContentType type) {
        Class cls = (Class) contentTypeMap.get(type);
        if (cls != null) {
            try {
                return (GeneratedContentNode) cls.newInstance();
            } catch (InstantiationException e) {
                throw new ClassGeneratorException("Cannot create " + type + ": " + e.getMessage(), e);
            } catch (IllegalAccessException e) {
                throw new ClassGeneratorException("Cannot create " + type + ": " + e.getMessage(), e);
            }
        }
        throw new IllegalArgumentException("Unknown type:" + type);
    }

    public ContentNodeI createNode(ContentType key) {
        GeneratedContentNode  gcn = createNodeImpl(key);
        gcn.initAttributes();
        return gcn;
    }

    public ContentNodeI createNode(ContentKey key) {
        GeneratedContentNode node = (GeneratedContentNode) createNodeImpl(key.getType());
        node.initAttributes();
        node.setKey(key);
        return node;
    }

    private void initContentType(ContentTypeDefI def) throws NotFoundException, CannotCompileException {
        LOG.info("init class for " + def.getType());
        CtClass class1 = pool.makeClass(getImplementationClassName(def.getType()));
        CtClass parent = pool.get(GeneratedContentNode.class.getName());
        class1.setSuperclass(parent);
    }

    private ClassInfo createContentType(ContentTypeDefI def) throws CannotCompileException, NotFoundException {
        ClassInfo ci = new ClassInfo();
        Map attributeMap = createAttributeMap(def);
        ci.setAttributeMap(attributeMap);
        
        LOG.info("creating class for " + def.getType());
        String implementationClassName = getImplementationClassName(def.getType());
        ci.setImplementationName(implementationClassName);
        
        boolean specialCopy = isSpecialCopyNeeded(def);
        
        CtClass class1 = pool.get(implementationClassName);
        {
            // static Map<String,AttributeDef> attributeDefs;
            CtField f = new CtField(pool.get("java.util.Map"), "__attributeDefs", class1);
            f.setModifiers(f.getModifiers() | Modifier.STATIC);
            class1.addField(f);
        }
        {
            CtField f = new CtField(pool.get(NodeGeneratorI.class.getName()), "__nodeGenerator", class1);
            f.setModifiers(f.getModifiers() | Modifier.STATIC);
            class1.addField(f);
        }
        {
            if (specialCopy) {
                CtField f = new CtField(pool.get("boolean"), "__copy", class1);
                class1.addField(f);
            }
        }
        
        {
            // String getLabel()
            CtMethod method = CtNewMethod.make("public String getLabel() {\n" + "return com.freshdirect.cms.node.ContentNodeUtil.getLabel(this);\n" + "}",
                    class1);
            class1.addMethod(method);
        }

        {
            // ContentTypeDefI getDefinition()
            CtMethod method = CtNewMethod.make("public com.freshdirect.cms.ContentTypeDefI getDefinition() {\n"
                    + " return " + implementationClassName+ ".__nodeGenerator.getDefinition(\"" + def.getType().getName() + "\");\n"
                    + " }", class1);
            class1.addMethod(method);
        }

        {
            CtMethod method = CtNewMethod.make("public void setAttributeDefs(java.util.Map defs) { \n" 
                    + " " + implementationClassName + ".__attributeDefs = defs; "
                    + "}", class1);
            class1.addMethod(method);
        }
        {
            CtMethod method = CtNewMethod.make("public java.util.Map getAttributeDefs() { \n" 
                    + "  return " + implementationClassName + ".__attributeDefs; " 
                    + "}", class1);
            class1.addMethod(method);
        }
        {
            CtMethod method = CtNewMethod.make("public void setContentNodeGenerator("+NodeGeneratorI.class.getName()+" value) { \n"
                    +" if ( "+implementationClassName + ".__nodeGenerator != null) { throw new RuntimeException(\"Node generator is already set!\"); }\n" 
                    + " " + implementationClassName + ".__nodeGenerator = value; "
                    + "}", class1);
            class1.addMethod(method);
        }
        
        boolean debug = isDebugContentType( def.getType());
        
        {
            boolean hasAttributes = attributeMap.size() > 0;
            StringBuilder initAttributes = new StringBuilder();
            initAttributes.append("public void initAttributes() {  \n");

            StringBuilder getAttribute = new StringBuilder();
            getAttribute.append("public final com.freshdirect.cms.AttributeI getAttribute(String name) { \n" +
            		" if (name==null) { return null; } \n");
            
            StringBuilder getAttributeValue = new StringBuilder();
            getAttributeValue.append("public final Object getAttributeValue(String name) { \n" +
                        " if (name==null) { return null; } \n");

            StringBuilder setAttributeValue = new StringBuilder();
            setAttributeValue.append("public final boolean setAttributeValue(String name, Object value) { \n" +
                        " if (name==null) { return false; } \n");
            
            StringBuilder getAttributeMap = new StringBuilder();
            getAttributeMap.append(" public java.util.Map getAttributes() {\n" +
            		" java.util.Map result = new java.util.HashMap();");

            StringBuilder getChildKeys = new StringBuilder();
            getChildKeys.append("public java.util.Set getChildKeys() {  \n" +
            		" java.util.Set result = new java.util.HashSet();\n");
            if (debug || debugGetAttributeCalls) {
                getAttribute.append(" System.out.println(\""+def.getType()+ " - getAttribute : \"+name + \" of \"+getKey());");
                if (debug) {
                    getAttributeValue.append(" System.out.println(\""+def.getType()+ " - getAttributeValue : \"+name + \" of \"+getKey());");
                }
            }
            
            if (hasAttributes) {
                getAttribute.append(
                        " int hc = name.hashCode(); \n" +
                            " switch (hc) { \n");
                getAttributeValue.append(
                        " int hc = name.hashCode(); \n" +
                            " switch (hc) { \n");
                setAttributeValue.append(
                        " int hc = name.hashCode(); \n" +
                            " switch (hc) { \n");
            }

            StringBuffer copy = new StringBuffer();
            copy.append("public com.freshdirect.cms.ContentNodeI copy() {\n ")
                .append(implementationClassName).append(" result = new ").append(implementationClassName).append("();\n result.initAttributes();\n result.setKey(this.getKey()); \n");
            if (specialCopy) {
                copy.append(" result.__copy = true;\n");
            }
            
            for (Iterator iter = attributeMap.keySet().iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                AttributeDefI attributeDef = (AttributeDefI) attributeMap.get(name);
                CtClass fieldType = getType(attributeDef);
                String fieldName = getFieldName(attributeDef);
                createField(def.getType(), class1, fieldType, fieldName, attributeDef);

                String attributeFieldName = lazy ? getCreateAttribute(def.getType(), attributeDef) :  getAttributeFieldName(attributeDef);
                if (!lazy) {
                    initAttributes.append("  this.").append(attributeFieldName).append(" = new ")
                        .append(getImplementationClassName(def.getType(), attributeDef)).append("();\n");
                    initAttributes.append("  this.").append(attributeFieldName).append(".setNode(this);\n");
                }
                
                getAttribute.append(" case ").append(name.hashCode()).append(" : { \n");
                getAttributeValue.append(" case ").append(name.hashCode()).append(" : { \n");
                setAttributeValue.append(" case ").append(name.hashCode()).append(" : { \n");
                if (UNSAFE) {
                    getAttribute.append("   return ").append(attributeFieldName).append(";\n  }\n");
                    getAttributeValue.append("   return ").append(getReturnsAsObject(attributeDef)+ "\n  }\n");
                    setAttributeValue.append("   ").append(getSetStatement(attributeDef, fieldName, fieldType)).append("\n" +
                    		"    return true;\n  }\n");
                } else {
                    getAttribute.append(" if (\"").append(name).append("\".equals(name)) { \n" +
                    "   return ").append(attributeFieldName).append("; \n" +
                                    "}");
                    getAttribute.append("\n break; \n }\n");
                    
                    getAttributeValue.append(" if (\"").append(name).append("\".equals(name)) { \n" +
                            "   return ").append(getReturnsAsObject(attributeDef)).append(" \n" +
                                    "}");
                    getAttributeValue.append("\n break; \n }\n");
                    
                    setAttributeValue.append(" if (\"").append(name).append("\".equals(name)) { \n")
                            .append("   ").append(getSetStatement(attributeDef, fieldName, fieldType) + " \n    return true; \n}");
                    setAttributeValue.append("\n break; \n }\n");
                    
                }
                getAttributeMap.append(" result.put(\"").append(name).append("\", ").append(attributeFieldName).append(");\n");
                
                if (attributeDef instanceof RelationshipDefI) {
                    RelationshipDefI r = (RelationshipDefI) attributeDef;
                    if (r.isNavigable()) {
                        if (EnumCardinality.ONE==r.getCardinality()) {
                            getChildKeys.append(" if (").append(fieldName).append("!=null) { \n")
                                .append("  result.add(").append(fieldName).append("); \n }");
                        } else {
                            getChildKeys.append(" if (").append(fieldName).append("!=null) { \n")
                            .append("  result.addAll((java.util.Collection)").append(fieldName).append("); \n }");
                        }
                    }
                }
                if (!(attributeDef instanceof BidirectionalRelationshipDefI)) { 
                    copy.append(" result.").append(fieldName).append(" = ").append(createCopyStatement(fieldName, fieldType, attributeDef)).append(";\n");
                } else {
                    BidirectionalRelationshipDefI b = (BidirectionalRelationshipDefI) attributeDef;
                    copy.append(" result.").append(fieldName).append(" = ").append(getReturnsAsObject(attributeDef)).append(";\n");
                }
            }

            initAttributes.append(" }\n");
            if (hasAttributes) {
                getAttribute.append(" } ");
                getAttributeValue.append(" } ");
                setAttributeValue.append(" } ");
            }
            getAttribute.append(" return null; \n}");
            getAttributeValue.append(" return null; \n}");
            setAttributeValue.append(" return false; \n}");
            /*if (debug) {
                getAttributeMap.append(" System.out.println(\""+def.getType()+ " - getAttributeMap : \"+result);");
                getChildKeys.append(" System.out.println(\""+def.getType()+ " - getChildKeys : \"+result);");
            }*/
            
            getAttributeMap.append(" return result; \n}");
            getChildKeys.append(" return result; \n}");
            
            copy.append(" result.setDelete(this.isDelete());\n");
            copy.append(" return result; \n}");
            
            CtConstructor ct = new CtConstructor(new CtClass[0], class1);
            ct.setBody(null);
            class1.addConstructor(ct);
            
            class1.addMethod(CtNewMethod.make(initAttributes.toString(), class1));
            switch (GETATTRIBUTE_MODE) {
                case SWITCH : 
                    class1.addMethod(CtNewMethod.make(getAttribute.toString(), class1));
                    break;
                case BINARY_SEARCH : 
                    class1.addMethod(CtNewMethod.make(createBinarySearchMethod(def, attributeMap), class1));
                    break;
                case HASHMAP_SEARCH : 
                    createHashMapMethod(def, class1,ci);
                    break;
                case PREFIX_SEARCH : 
                    createPrefixSearchMethod(def, class1, ci);
                    break;
                
            }
            class1.addMethod(CtNewMethod.make(getAttributeMap.toString(), class1));
            class1.addMethod(CtNewMethod.make(getChildKeys.toString(), class1));
            class1.addMethod(CtNewMethod.make(copy.toString(), class1));
            class1.addMethod(CtNewMethod.make(getAttributeValue.toString(), class1));
            class1.addMethod(CtNewMethod.make(setAttributeValue.toString(), class1));
        }
        ci.add(new AttributeMapClassInitializer(attributeMap));
        ci.setGeneratedClass(class1.toClass());
        return ci;
    }

    private boolean isSpecialCopyNeeded(ContentTypeDefI def) {
        for (AttributeDefI a : def.getSelfAttributeDefs()) {
            if (a instanceof BidirectionalRelationshipDefI) {
//                if (((BidirectionalRelationshipDefI)a).isWritableSide()) {
                    return true;
//                }
            }
        }
        return false;
    }

    private String createCopyStatement(String fieldName, CtClass type, AttributeDefI attributeDef) {
        if (type.getName().equals("java.util.List")) {
            return "(this." + fieldName + " != null ? new java.util.ArrayList(this." + fieldName + ") : null)";
        }
        return "this." + fieldName;
    }

    private void createPrefixSearchMethod(ContentTypeDefI def, CtClass class1, ClassInfo ci) throws CannotCompileException {
        StringBuffer buffer = new StringBuffer();
        buffer.append("public final com.freshdirect.cms.AttributeI getAttribute(String name) { \n" +
        " if (name==null) { return null; } \n int len = name.length();\n");

        AttributeRec[] recs = createSortedAttributes(ci.getAttributeMap(), new AttributeRecNameComparator());
        if (recs!=null && recs.length>0) {
            createPrefixSubSearch(buffer, recs, 0, 0, recs.length, PREFIX_DEBUG ? "  " : "" );
        }
        buffer.append("\n return null;\n}");
        
        class1.addMethod(CtNewMethod.make(buffer.toString(), class1));
    }

    private void createPrefixSubSearch(StringBuffer buffer, AttributeRec[] recs, int charPos, int start, int end, String padding) {
        if (start+1==end) {
            buffer.append(padding).append("return ").append(getAttributeFieldName(recs[start].getDef())).append(";\n");
            return;
        }
        if (allSame(recs, charPos, start, end)) {
            createPrefixSubSearch(buffer, recs, charPos+1, start, end, padding);
            return;
        }
        buffer.append(padding).append("if (len>=").append(charPos).append(") {\n");
        buffer.append(padding).append("  char ch").append(charPos).append(" = name.charAt(").append(charPos).append(");\n");
        char lastCharacter = '\0';
        int prevEnd = -1;
        lastCharacter = '\0';
        
        for (int i=start;i<end;i++) {
            if (recs[i].name.length()>charPos) {
                char currentCharacter = recs[i].name.length()>charPos ? recs[i].name.charAt(charPos) : '\0';
                if (currentCharacter!=lastCharacter) {
                    // start a new block
                    if (-1!=prevEnd) {
                        // there were a previous block, we have to call recursively
                        if (recs[i].name.length()>charPos+1) {
                            createPrefixSubSearch(buffer, recs, charPos+1, prevEnd, i, PREFIX_DEBUG ? padding + "    " : "");
                            buffer.append(padding).append("  }\n");
                        } else {
                            buffer.append(padding).append(" return ").append(getAttributeFieldName(recs[i].getDef())).append(";\n");
                        }
                    }
                    prevEnd = i;
                    buffer.append(padding).append("  if (ch").append(charPos).append(" == '").append((char)currentCharacter).append("') { \n");
                    lastCharacter = currentCharacter;
                }
            } else {
                createPrefixSubSearch(buffer, recs, charPos+1, prevEnd, i, PREFIX_DEBUG ? padding + "    " : "");
                //buffer.append(padding).append("return ").append(getAttributeFieldName(recs[i].getDef())).append(";\n");
                //buffer.append(padding).append("/// return ... ??? \n");
                prevEnd = i;
            }
        }
        if (prevEnd!=-1) {
            
            createPrefixSubSearch(buffer, recs, charPos+1, prevEnd, end, PREFIX_DEBUG ? padding + "    " : "");
            buffer.append(padding).append("  }\n");
        }
        buffer.append(padding).append(" }\n");
        buffer.append(padding).append("return null;\n");
    }

    private boolean allSame(AttributeRec[] recs, int charPos, int start, int end) {
        if (recs[start].name.length()<=charPos) {
            return false;
        }
        char ch = recs[start].name.charAt(charPos);
        for (int i=start; i<end;i++) {
            if (recs[i].name.length()<=charPos || recs[i].name.charAt(charPos)!=ch) {
                return false;
            }
        }
        return true;
    }

    private void createHashMapMethod(ContentTypeDefI def, CtClass class1, ClassInfo ci) throws CannotCompileException, NotFoundException {

        if (ci.attributeMap.isEmpty()) {
            class1.addMethod(CtNewMethod.make("public final com.freshdirect.cms.AttributeI getAttribute(String name) { \n return null;\n}", class1));
            return;
        }
        
        CtField f = new CtField(pool.get("java.util.Map"), "__ordinalhash", class1);
        f.setModifiers(f.getModifiers() | Modifier.STATIC);
        class1.addField(f);
        
        CtMethod method = CtNewMethod.make("public void setAttributeOrdinalHash(java.util.Map defs) { \n" 
                + " " + ci.getImplementationName() + ".__ordinalhash = defs; "
                + "}", class1);
        class1.addMethod(method);
        
        Map ordinalhash = new HashMap();
        
        StringBuffer buffer = new StringBuffer();
        buffer.append("public final com.freshdirect.cms.AttributeI getAttribute(String name) { \n" +
        " java.lang.Integer hash = (Integer) __ordinalhash.get(name);\n" +
        " if (hash==null) { return null; }\n" +
        " int ihash = hash.intValue(); \n" +
        " switch (ihash) { \n");
        int i = 0;
        for (Iterator iter=ci.attributeMap.keySet().iterator();iter.hasNext();) {
            String key = (String) iter.next();
            ordinalhash.put(key, new Integer(i));
            buffer.append("   case ").append(i).append(" : { \n      return ").append(getAttributeFieldName(ci.getAttributeDef(key))).append(";\n }\n");
            i++;
        }
        buffer.append(" }\n return null; }");
        
        ci.add(new OrdinalHashClassInitializer(ordinalhash));
        class1.addMethod(CtNewMethod.make(buffer.toString(), class1));
    }

    private String createBinarySearchMethod(ContentTypeDefI def, Map attributeMap) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("public final com.freshdirect.cms.AttributeI getAttribute(String name) { \n" +
        " if (name==null) { return null; } \n");
        buffer.append(" int hc = name.hashCode(); \n");
        AttributeRec[] recs = createSortedAttributes(attributeMap, new AttributeRecHashComparator());
        
        createBinarySearchMethodBody(recs, buffer, 0, recs.length-1);
        
        buffer.append(" return null; \n}");
        
        return buffer.toString();
    }

    private AttributeRec[] createSortedAttributes(Map attributeMap, Comparator comparator) {
        AttributeRec[] recs = new AttributeRec[attributeMap.size()];
        int i =0;
        for (Iterator iter = attributeMap.keySet().iterator(); iter.hasNext();) {
            String key = (String) iter.next();
            AttributeRec a = new AttributeRec(key, (AttributeDefI) attributeMap.get(key));
            recs[i]= a;
            i++;
        }
        Arrays.sort(recs, comparator);
        return recs;
    }
    
    private void createBinarySearchMethodBody(AttributeRec[] recs, StringBuffer buffer, int start, int end) {
        if (start > end) {
            return;
        }
        if (end-start <=3) {
            for (int i=start;i<=end;i++) {
                buffer.append(" if (hc == ").append(recs[i].getHash()).append(") {\n");
                buffer.append("    return ").append(getAttributeFieldName(recs[i].getDef())).append(";\n   }\n");                
            }
            return;
        }
        
        int median = (start+end) / 2;
        int medValue = recs[median].getHash();
        if (start!=end) {
            if (start <= median - 1) {
                buffer.append(" if (hc > ").append(medValue).append(") { \n");
                createBinarySearchMethodBody(recs, buffer, start, median - 1 );
                
                buffer.append("\n } else ");
            }
            if (median + 1 <= end) {
                buffer.append("if (hc < ").append(medValue).append(") {\n");
                createBinarySearchMethodBody(recs, buffer, median + 1, end);
                buffer.append("\n } else ");
            }
            buffer.append("if (hc == ").append(medValue).append(") {\n");
        } else {
            buffer.append("  if (hc == ").append(medValue).append(") { \n");
        }
        if (UNSAFE) {
            buffer.append("    return ").append(getAttributeFieldName(recs[median].getDef())).append(";\n   }");
        } else {
            buffer.append("    if (\"").append(recs[median].getName()).append("\".equals(name)) { \n")
                  .append("     return ").append(getAttributeFieldName(recs[median].getDef())).append(";\n    }\n   }");
        }
    }
    
    
    private void createField(ContentType contentType, CtClass class1, CtClass type, String fieldName, AttributeDefI attributeDef) throws CannotCompileException, NotFoundException {
        boolean debug = isDebugContentType(contentType);
        LOG.info("create field " + type.getName() + " : " + attributeDef.getName());
        boolean skipFieldCreation = attributeDef instanceof BidirectionalRelationshipDefI;
        {
            CtField f = new CtField(type, fieldName, class1);
        
            class1.addField(f);
        }
        
        {
            CtMethod method = CtNewMethod.make("public java.lang.Object getAttributeValue_"+attributeDef.getName()+"() { " +
            		" return "+getReturnsAsObject(attributeDef)+ " }", class1);
            class1.addMethod(method);
        }
        {
            StringBuffer buf = new StringBuffer();
            buf.append("public void setAttributeValue_"+attributeDef.getName() + "(Object value) { \n");
            if (debug) {
                buf.append(" if (value!=null) { \n"+
                    "   System.out.println(\""+contentType.getName() + "." + attributeDef.getName()+"("+type.getName()+"):=\"+value + \"(\"+value.getClass().getName()+\")[\"+getKey()+\"]\");\n"+
                    " } ");
            }
            buf.append(getSetStatement(attributeDef, fieldName, type));
            buf.append("}");
            
            class1.addMethod(CtNewMethod.make(buf.toString(), class1));
        }
        
        CtClass attrClass = createAttributeClass( contentType, class1, attributeDef, type);

        if (!lazy) {
            CtField f2 = new CtField(attrClass, getAttributeFieldName(attributeDef), class1);
            class1.addField(f2);
        }
    }
    
    private String getSetStatement(AttributeDefI attributeDef, String fieldName, CtClass fieldType) throws NotFoundException {
        if (attributeDef instanceof BidirectionalRelationshipDefI) {
            BidirectionalRelationshipDefI b = (BidirectionalRelationshipDefI) attributeDef;
            if (b.isWritableSide()) {
                return "if (__copy) { \n" + fieldName+ " = " + getSetStatement(fieldType)+";\n } else {\n __nodeGenerator.getReferenceHandler(key.getType(), \"" + attributeDef.getName() + "\").addRelation(key, (com.freshdirect.cms.ContentKey) value); }";
            }
            return "";
        } else {
            return fieldName+ " = "+getSetStatement(fieldType)+';';
        }
    }

    private boolean isDebugContentType(ContentType contentType) {
        return DEBUG /*|| contentType.getName().equals("Image")*/;
    }

    private String getFieldName(AttributeDefI attributeDef) {
        return "_$" + attributeDef.getName();
    }

    private String getAttributeFieldName(AttributeDefI attributeDef) {
        return "_$$" + attributeDef.getName();
    }

    /**
     * Return a code fragment which creates an attribute.
     * 
     * @param type
     * @param attributeDef
     * @return
     */
    private String getCreateAttribute(ContentType type, AttributeDefI attributeDef) {
        String name= getImplementationClassName(type, attributeDef);
        return "new " + name + "(this)";
    }

    
    private CtClass createAttributeClass(ContentType type, CtClass objectClass, AttributeDefI attributeDef, CtClass fieldType) throws NotFoundException, CannotCompileException {
        String name= getImplementationClassName(type, attributeDef);
        CtClass atClass = pool.makeClass(name);
        atClass.addInterface(pool.get(AttributeI.class.getName()));
        
        {
            CtField f = new CtField(objectClass, "node", atClass); 
            atClass.addField(f);
            CtMethod setter = CtNewMethod.setter("setNode", f);
            
            atClass.addMethod(setter);
            
            CtConstructor c = new CtConstructor(new CtClass[0], atClass);
            c.setBody(null); 
            atClass.addConstructor(c);
            
        }
        {
            CtConstructor c = new CtConstructor(new CtClass[] { objectClass }, atClass);
            c.setBody(" { super(); setNode($1); }");
            atClass.addConstructor(c);
        }
        
        {
            CtMethod method = CtNewMethod.make("public com.freshdirect.cms.ContentNodeI getContentNode() { return node; }", atClass);
            atClass.addMethod(method);
        }
        {
            CtMethod method = CtNewMethod.make("public java.lang.String getName() { return \""+attributeDef.getName()+"\"; }", atClass);
            atClass.addMethod(method);
        }
        {
            CtMethod method = CtNewMethod.make("public java.lang.Object getValue() { return node.getAttributeValue_"+attributeDef.getName()+"(); }", atClass);
            atClass.addMethod(method);
        }
        {
            CtMethod method = CtNewMethod.make("public void setValue(java.lang.Object value) { return node.setAttributeValue_"+attributeDef.getName()+" (value); }", atClass);
            atClass.addMethod(method);
        }
        {
            CtMethod method = CtNewMethod.make("public com.freshdirect.cms.AttributeDefI getDefinition() { return node.getAttributeDef(\""+attributeDef.getName()+"\"); }", atClass);
            atClass.addMethod(method);
        }
        {
            atClass.addMethod(CtNewMethod.make("public String toString() { return \""+type.getName()+':'+attributeDef.getName()+"=\"+getValue() + \"("+fieldType.getSimpleName()+
                    ")\";}", atClass));
        }
        {
            atClass.addMethod(CtNewMethod.make("public boolean equals(Object object) { \n"+
                    "  return (object instanceof "+name+ ") && (node == (("+name+")object).node);\n }", atClass));
        }
        
        if (attributeDef instanceof RelationshipDefI) {
            atClass.addInterface(pool.get(RelationshipI.class.getName()));
        }
        
        Class cc = atClass.toClass();
        //LOG.info("attribute class created:"+cc.getName());
        
        return atClass;
    }
    
    private String getReturnsAsObject(AttributeDefI attributeDef) {
        if (attributeDef instanceof BidirectionalRelationshipDefI) {
            BidirectionalRelationshipDefI b = (BidirectionalRelationshipDefI) attributeDef;
            if (b.isWritableSide()) {
                return "__copy ? "+ getFieldName(attributeDef) +" : __nodeGenerator.getReferenceHandler(key.getType(), \"" + attributeDef.getName() + "\").getReference(key);";
            } else {
                return "__copy ? "+ getFieldName(attributeDef) +" : __nodeGenerator.getReferenceHandler(com.freshdirect.cms.ContentType.get(\""+b.getOtherSide().getType().getName()+"\"), \"" + b.getOtherSide().getName() + "\").getInverseReference(key);";
            }
        }
        return getFieldName(attributeDef)+";";
    }
    
    

    private String getSetStatement(CtClass fieldType) {
        return "("+fieldType.getName()+") value;";
    }

    private CtClass getType(AttributeDefI attributeDef) throws NotFoundException {
        CtClass type = (CtClass) fieldTypes.get(attributeDef.getAttributeType());
        if (type == null) {
            if (attributeDef instanceof RelationshipDefI) {
                if (attributeDef.getCardinality() == EnumCardinality.ONE) {
                    return pool.get(ContentKey.class.getName());
                } else {
                    return pool.get("java.util.List");
                }
            }
            if (attributeDef instanceof EnumDefI) {
                EnumDefI ed = (EnumDefI) attributeDef;
                CtClass enumType = (CtClass) fieldTypes.get(ed.getValueType());
                if (enumType != null) {
                    return enumType;
                }
            }
            throw new ClassGeneratorException("Unable to handle : " + attributeDef);
        }
        return type;
    }

    String getImplementationClassName(ContentType type) {
        return packageName + type.getName();
    }
    String getImplementationClassName(ContentType type, AttributeDefI attr) {
        return packageName + type.getName() + "_"+attr.getName();
    }

    public ContentTypeDefI getDefinition(ContentType type) {
        return typeService.getContentTypeDefinition(type);
    }

    @Override
    public ContentTypeDefI getDefinition(String name) {
        return typeService.getContentTypeDefinition(ContentType.get(name));
    }
    
    @Override
    public BidirectionalReferenceHandler getReferenceHandler(ContentType type, String attributeName) {
        return typeService.getReferenceHandler(type, attributeName);
    }

}
