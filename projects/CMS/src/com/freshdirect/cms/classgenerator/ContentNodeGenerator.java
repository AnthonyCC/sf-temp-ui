package com.freshdirect.cms.classgenerator;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.EnumDefI;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.RelationshipI;
import com.freshdirect.cms.application.ContentTypeServiceI;

public class ContentNodeGenerator {

    private final static boolean DEBUG = false;
    private final static boolean UNSAFE = true;
    private final static boolean SWITCH = true;
    
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

    public ContentNodeGenerator(ContentTypeServiceI service) {
        this(service,"");
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
                Map attributeMap = createAttributeMap(object);
                contentTypeMap.put(object.getType(), createContentType(object, attributeMap));

                GeneratedContentNode node = createNodeImpl(object.getType());
                node.setAttributeDefs(attributeMap);
                node.setContentNodeGenerator(this);

            } catch (CannotCompileException e) {
                throw new ClassGeneratorException("Cannot compile " + object.getType() + ": " + e.getMessage(), e);
            } catch (NotFoundException e) {
                throw new ClassGeneratorException("Cannot compile " + object.getType() + ": " + e.getMessage(), e);
            }
        }
    }

    Map createAttributeMap(ContentTypeDefI def) {
        Set attributeNames = def.getAttributeNames();
        Map result = new TreeMap();
        for (Iterator iter = attributeNames.iterator(); iter.hasNext();) {
            String name = (String) iter.next();
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

    private Class createContentType(ContentTypeDefI def, Map attributeMap) throws CannotCompileException, NotFoundException {
        LOG.info("creating class for " + def.getType());
        String implementationClassName = getImplementationClassName(def.getType());
        CtClass class1 = pool.get(implementationClassName);
        {
            // static Map<String,AttributeDef> attributeDefs;
            CtField f = new CtField(pool.get("java.util.Map"), "__attributeDefs", class1);
            f.setModifiers(f.getModifiers() | Modifier.STATIC);
            class1.addField(f);
        }
        {
            CtField f = new CtField(pool.get(ContentNodeGenerator.class.getName()), "__nodeGenerator", class1);
            f.setModifiers(f.getModifiers() | Modifier.STATIC);
            class1.addField(f);
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
            CtMethod method = CtNewMethod.make("public void setContentNodeGenerator("+ContentNodeGenerator.class.getName()+" value) { \n"
                    +" if ( "+implementationClassName + ".__nodeGenerator != null) { throw new RuntimeException(\"Node generator is already set!\"); }\n" 
                    + " " + implementationClassName + ".__nodeGenerator = value; "
                    + "}", class1);
            class1.addMethod(method);
        }

        
        boolean debug = isDebugContentType( def.getType());
        
        {
            boolean hasAttributes = attributeMap.size() > 0;
            StringBuffer initAttributes = new StringBuffer();
            initAttributes.append("public void initAttributes() {  \n");

            StringBuffer getAttribute = new StringBuffer();
            getAttribute.append("public final com.freshdirect.cms.AttributeI getAttribute(String name) { \n" +
            		" if (name==null) { return null; } \n");
            StringBuffer getAttributeMap = new StringBuffer();
            getAttributeMap.append(" public java.util.Map getAttributes() {\n" +
            		" java.util.Map result = new java.util.HashMap();");

            StringBuffer getChildKeys = new StringBuffer();
            getChildKeys.append("public java.util.Set getChildKeys() {  \n" +
            		" java.util.Set result = new java.util.HashSet();\n");
            if (debug) {
                getAttribute.append(" System.out.println(\""+def.getType()+ " - getAttribute : \"+name + \" of \"+getKey());");
            }
            
            if (hasAttributes) {
                getAttribute.append(
                        " int hc = name.hashCode(); \n" +
                            " switch (hc) { \n");
            }

            StringBuffer copy = new StringBuffer();
            copy.append("public com.freshdirect.cms.ContentNodeI copy() {\n ")
                .append(implementationClassName).append(" result = new ").append(implementationClassName).append("();\n result.initAttributes();\n result.setKey(this.getKey()); \n");
                
            
            
            
            
            for (Iterator iter = attributeMap.keySet().iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                AttributeDefI attributeDef = (AttributeDefI) attributeMap.get(name);
                CtField field = createField(def.getType(), class1, attributeDef);

                String attributeFieldName = getAttributeFieldName(attributeDef);
                initAttributes.append("  this.").append(attributeFieldName).append(" = new ")
                    .append(getImplementationClassName(def.getType(), attributeDef)).append("();\n");
                initAttributes.append("  this.").append(attributeFieldName).append(".setNode(this);\n");

                getAttribute.append(" case ").append(name.hashCode()).append(" : { \n");
                if (UNSAFE) {
                    getAttribute.append("   return ").append(attributeFieldName).append(";\n  }\n");
                } else {
                    getAttribute.append(" if (\"").append(name).append("\".equals(name)) { \n" +
                    		"   return ").append(attributeFieldName).append("; \n" +
                    				"}");
                    getAttribute.append("\n break; \n }\n");
                }
                getAttributeMap.append(" result.put(\"").append(name).append("\", ").append(attributeFieldName).append(");\n");
                
                if (attributeDef instanceof RelationshipDefI) {
                    RelationshipDefI r = (RelationshipDefI) attributeDef;
                    if (r.isNavigable()) {
                        if (EnumCardinality.ONE==r.getCardinality()) {
                            getChildKeys.append(" if (").append(field.getName()).append("!=null) { \n")
                                .append("  result.add(").append(field.getName()).append("); \n }");
                        } else {
                            getChildKeys.append(" if (").append(field.getName()).append("!=null) { \n")
                            .append("  result.addAll((java.util.Collection)").append(field.getName()).append("); \n }");
                        }
                    }
                }
                
                copy.append(" result.").append(attributeFieldName).append(" = this.").append(attributeFieldName).append(";\n");
                
            }

            initAttributes.append(" }\n");
            if (hasAttributes) {
                getAttribute.append(" } ");
            }
            getAttribute.append(" return null; \n}");
            /*if (debug) {
                getAttributeMap.append(" System.out.println(\""+def.getType()+ " - getAttributeMap : \"+result);");
                getChildKeys.append(" System.out.println(\""+def.getType()+ " - getChildKeys : \"+result);");
            }*/
            
            getAttributeMap.append(" return result; \n}");
            getChildKeys.append(" return result; \n}");
            
            copy.append(" return result; \n}");
            
            CtConstructor ct = new CtConstructor(new CtClass[0], class1);
            ct.setBody(null);
            class1.addConstructor(ct);
            
            class1.addMethod(CtNewMethod.make(initAttributes.toString(), class1));
            if (SWITCH) {
                class1.addMethod(CtNewMethod.make(getAttribute.toString(), class1));
            } else {
                class1.addMethod(CtNewMethod.make(createBinarySearchMethod(def, attributeMap), class1));
            }
            class1.addMethod(CtNewMethod.make(getAttributeMap.toString(), class1));
            class1.addMethod(CtNewMethod.make(getChildKeys.toString(), class1));
            class1.addMethod(CtNewMethod.make(copy.toString(), class1));
        }

        return class1.toClass();
    }

    private String createBinarySearchMethod(ContentTypeDefI def, Map attributeMap) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("public final com.freshdirect.cms.AttributeI getAttribute(String name) { \n" +
        " if (name==null) { return null; } \n");
        buffer.append(" int hc = name.hashCode(); \n");
        AttributeRec[] recs = new AttributeRec[attributeMap.size()];
        int i =0;
        for (Iterator iter = attributeMap.keySet().iterator(); iter.hasNext();) {
            String key = (String) iter.next();
            AttributeRec a = new AttributeRec(key, (AttributeDefI) attributeMap.get(key));
            recs[i]= a;
            i++;
        }
        Arrays.sort(recs, null);
        
        createBinarySearchMethodBody(recs, buffer, 0, recs.length-1);
        
        buffer.append(" return null; \n}");
        
        return buffer.toString();
    }
    
    private void createBinarySearchMethodBody(AttributeRec[] recs, StringBuffer buffer, int start, int end) {
        if (start > end) {
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
    
    static class AttributeRec implements Comparable { 
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
        public int compareTo(Object o) {
            AttributeRec x =(AttributeRec)o;
            return hash == x.hash ? 0 : hash > x.hash ? -1 : 1;
        }
    }
    
    
    
    private CtField createField(ContentType contentType, CtClass class1, AttributeDefI attributeDef) throws CannotCompileException, NotFoundException {
        CtClass type = getType(attributeDef);
        boolean debug = isDebugContentType(contentType);
        LOG.info("create field " + type.getName() + " : " + attributeDef.getName());
        String fieldName = getFieldName(attributeDef);
        CtField f = new CtField(type, fieldName, class1);
        
        class1.addField(f);
        
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
            //if (!attributeDef.isReadOnly()) {
                buf.append(" this."+fieldName+ " = "+getSetStatement(attributeDef, type));
            //}
            buf.append("}");
            
            class1.addMethod(CtNewMethod.make(buf.toString(), class1));
        }
        CtClass attrClass = createAttributeClass( contentType, class1, attributeDef, type);
        
        
        CtField f2 = new CtField(attrClass, getAttributeFieldName(attributeDef), class1);
        class1.addField(f2);
        
        return f;
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
        if (attributeDef instanceof RelationshipDefI) {
            atClass.addInterface(pool.get(RelationshipI.class.getName()));
        }
        
        Class cc = atClass.toClass();
        //LOG.info("attribute class created:"+cc.getName());
        
        return atClass;
    }
    private String getReturnsAsObject(AttributeDefI attributeDef) {
        if (attributeDef instanceof EnumDefI) {
            EnumDefI ed = (EnumDefI) attributeDef;
            return getReturnsAsObject(attributeDef, ed.getValueType());
        }
        return getReturnsAsObject(attributeDef, attributeDef.getAttributeType());
    }
    
    private String getReturnsAsObject(AttributeDefI attributeDef, EnumAttributeType type) {
        /*if (type==EnumAttributeType.BOOLEAN) {
            return "Boolean.valueOf("+getFieldName(attributeDef)+");";
        } 
        if (type==EnumAttributeType.INTEGER) {
            // TODO: For Java 1.5 Integer.valueOf is preferred
            return "new Integer("+getFieldName(attributeDef)+");";
        }
        if (type==EnumAttributeType.DOUBLE) {
            return "new Double("+getFieldName(attributeDef)+");";
        }*/
        return getFieldName(attributeDef)+";";
    }
    
    private String getSetStatement(AttributeDefI attributeDef,CtClass fieldType) {
        if (attributeDef instanceof EnumDefI) {
            EnumDefI ed = (EnumDefI) attributeDef;
            return getSetStatement(attributeDef, ed.getValueType(),fieldType);
        }
        return getSetStatement(attributeDef, attributeDef.getAttributeType(), fieldType);
    }
    

    private String getSetStatement(AttributeDefI attributeDef, EnumAttributeType type,CtClass fieldType) {
        /*if (type==EnumAttributeType.BOOLEAN) {
            return "((Boolean)value).booleanValue();";
        } 
        if (type==EnumAttributeType.INTEGER) {
            // TODO: For Java 1.5 Integer.valueOf is preferred
            return "((Number)value).intValue();";
        }
        if (type==EnumAttributeType.DOUBLE) {
            return "((Number)value).doubleValue();";
        }*/
        return "("+fieldType.getName()+") value;";
    }

    private CtClass getType(AttributeDefI attributeDef) throws NotFoundException {
        CtClass type = (CtClass) fieldTypes.get(attributeDef.getAttributeType());
        if (type == null) {
            if (attributeDef instanceof RelationshipDefI) {
                RelationshipDefI rd = (RelationshipDefI) attributeDef;
                if (attributeDef.getCardinality() == EnumCardinality.ONE) {
//                    if (rd.getContentTypes().size() == 1) {
//                        ContentType ctype = (ContentType) rd.getContentTypes().iterator().next();
//                        return pool.get(getImplementationClassName(ctype));
//                    }
//                    return pool.get(ContentNodeI.class.getName());
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

    public ContentTypeDefI getDefinition(String name) {
        return typeService.getContentTypeDefinition(ContentType.get(name));
    }

}
