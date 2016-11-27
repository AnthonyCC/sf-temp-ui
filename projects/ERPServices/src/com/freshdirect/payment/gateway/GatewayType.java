package com.freshdirect.payment.gateway;

import java.util.HashMap;
import java.util.Map;
 
public enum GatewayType  implements java.io.Serializable {
    CYBERSOURCE(1, "Cybersource"),
    PAYMENTECH(2, "Paymentech"),
    PAYPAL(3, "PayPal");
    private int id;
    private String name;
    
 
    private static Map<Integer, GatewayType> GatewayTypes;
    static {
    	init();
    }
 
    public static GatewayType getPrimary() {
    	return CYBERSOURCE;
    }
    private static void init() {
    	GatewayTypes = new HashMap<Integer, GatewayType>();
        for (GatewayType s : values()) {
        	GatewayTypes.put(s.id, s);
        }
    }
    private GatewayType(int id, String name) {
        this.id = id;
        this.name = name;
    }
 
    public static GatewayType get(int i) {
        /*GatewayType result = null;
        for (GatewayType s : values()) {
            result = GatewayTypes.get(i);
        }
        return result;*/
    	return GatewayTypes.get(i);
    }
 
    public static GatewayType get(String i) {
        /*GatewayType result = null;
        for (GatewayType s : values()) {
            result = GatewayTypes.get(i);
        }
        return result;*/
    	return GatewayTypes.get(Integer.parseInt(i));
    }
 
    public int getId() {
        return id;
    }
 
    public String getName() {
        return name;
    }
 
   
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("GatewayType");
        sb.append("{id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
 
    public static void main(String[] args) {
        System.out.println(GatewayType.CYBERSOURCE.equals(GatewayType.CYBERSOURCE));
        System.out.println(GatewayType.get(-1));
    }
}
